package data_access;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import use_case.common.RecipeServiceException;
import use_case.save_recipe.RecipeDetailsGateway;
import use_case.search_recipes.SearchRecipesGateway;
import use_case.search_recipes.RecipeSummary;
import entity.Recipe;
import entity.Ingredient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MealDbApiGateway implements SearchRecipesGateway, RecipeDetailsGateway {

    private static final String DEFAULT_BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final int MAX_INGREDIENTS = 20;

    private final HttpClient client;
    private final String baseUrl;

    public MealDbApiGateway() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build(),
                DEFAULT_BASE_URL);
    }

    // second constructor lets tests inject a fake client / local server URL
    public MealDbApiGateway(HttpClient client, String baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<RecipeSummary> searchByName(String name) throws RecipeServiceException {
        return parseSummaries(get("search.php?s=" + encode(name)));
    }

    @Override
    public List<RecipeSummary> searchByIngredient(String ingredient) throws RecipeServiceException {
        return parseSummaries(get("filter.php?i=" + encode(ingredient)));
    }

    @Override
    public Recipe fetchById(String apiId) throws RecipeServiceException {
        JSONArray meals = mealsArray(get("lookup.php?i=" + encode(apiId)));
        if (meals.isEmpty()) {
            throw new RecipeServiceException("Recipe " + apiId + " not found");
        }
        return parseRecipe(meals.getJSONObject(0));
    }

    // ---------- HTTP ----------

    private String get(String path) throws RecipeServiceException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new RecipeServiceException("Recipe service returned HTTP " + response.statusCode());
            }
            return response.body();
        } catch (IOException e) {
            throw new RecipeServiceException("Could not reach recipe service", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RecipeServiceException("Request interrupted", e);
        }
    }

    private static String encode(String s) {
        return URLEncoder.encode(s.trim(), StandardCharsets.UTF_8);
    }

    // ---------- Parsing (package-private so tests can call them with sample JSON) ----------

    static JSONArray mealsArray(String body) throws RecipeServiceException {
        if (body == null || body.isBlank()) {
            return new JSONArray();               // API sometimes returns an empty body for no match
        }
        try {
            JSONArray meals = new JSONObject(body).optJSONArray("meals"); // null when "meals": null
            return meals == null ? new JSONArray() : meals;
        } catch (JSONException e) {
            throw new RecipeServiceException("Unexpected response from recipe service", e);
        }
    }

    static List<RecipeSummary> parseSummaries(String body) throws RecipeServiceException {
        JSONArray meals = mealsArray(body);
        List<RecipeSummary> result = new ArrayList<>();
        for (int i = 0; i < meals.length(); i++) {
            JSONObject m = meals.getJSONObject(i);
            result.add(new RecipeSummary(
                    m.getString("idMeal"),
                    m.optString("strMeal", ""),
                    m.optString("strCategory", ""),     // absent for filter.php
                    m.optString("strMealThumb", "")));
        }
        return result;
    }

    static Recipe parseRecipe(JSONObject m) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 1; i <= MAX_INGREDIENTS; i++) {
            String name = m.optString("strIngredient" + i, "").trim();
            String measure = m.optString("strMeasure" + i, "").trim();
            if (!name.isEmpty()) {            // unused slots are "", " " or null
                ingredients.add(new Ingredient(name, measure));
            }
        }
        return new Recipe(
                m.getString("idMeal"),
                m.optString("strMeal", ""),
                m.optString("strCategory", ""),
                ingredients,
                m.optString("strInstructions", ""),
                m.optString("strMealThumb", ""));
    }
}