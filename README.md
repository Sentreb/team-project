# [Project Name] Meal Planner

## Team Information

**Team:** [TUT5102] Group 01

### Team Members
- Member A: Mochammad Fathirrazi Nafisandy
- Member B: Clifford Nathanael Julienson
- Member C: Muhammad Fauzan Hidayat
- Member D: Alang Babega Mahareza

---

## Project Description

### Domain
**Food & Productivity**

Our Meal Planner is a Java Swing desktop app that helps users decide what to cook and what to buy. Users search an online recipe database, save recipes to their own collection (with personal notes and edits), place saved recipes into a weekly meal plan, and generate a grocery list from that plan. Saved recipes, meal plans and grocery lists are stored locally in a SQLite database so they persist between sessions.

### Specification
A user searches for recipes by name or ingredient. Each result shows the recipe's name, category and image. The user can save a recipe, which stores its full details (ingredients, measures, instructions) in their collection. Saved recipes can be edited or deleted. The user can assign a saved recipe to a day of the week and a meal slot (breakfast, lunch or dinner) in their weekly plan, and remove or move entries. From the weekly plan, the user can generate a grocery list that groups ingredients across all planned meals, and check items off while shopping.

---

## User Stories

### User Story 1: Search recipes
As a user, I want to search for recipes by name or ingredient so that I can find something to cook.

### User Story 2: Save and manage recipes
As a user, I want to save a recipe and edit it (notes, ingredient swaps, servings) so that my collection matches how I actually cook.

### User Story 3: Plan my week
As a user, I want to assign saved recipes to days of the week so that I know what I am cooking each day.

### User Story 4: Grocery list
As a user, I want a grocery list generated from my weekly plan that I can check off so that I buy everything I need in one trip.

---

## Use Cases

### Use Case 1: Search Recipes
- **Actor:** User
- **Main Flow:**
  1. User enters a recipe name or an ingredient and chooses the search type.
  2. System queries TheMealDB API.
  3. System displays a list of matching recipes with name, category and image.
- **Alternative Flows:**
  - No results: system shows "No recipes found."
  - Network or API error: system shows an error message and keeps the previous results.

### Use Case 2: Save Recipe
- **Actor:** User
- **Precondition:** User is viewing search results.
- **Main Flow:**
  1. User selects a recipe and clicks "Save".
  2. System fetches the full recipe details from TheMealDB.
  3. System stores the recipe in the user's collection.
  4. System confirms the recipe was saved.
- **Alternative Flows:**
  - Recipe already saved: system shows "Recipe already in your collection."
  - API error: system shows an error and saves nothing.

### Use Case 3: Add Meal to Plan
- **Actor:** User
- **Precondition:** User has at least one saved recipe.
- **Main Flow:**
  1. User selects a saved recipe, a day of the week and a meal slot.
  2. System adds the entry to the weekly plan and stores it.
  3. System updates the weekly plan view.
- **Alternative Flows:**
  - Slot already filled: system asks whether to replace the existing meal.

### Use Case 4: Generate Grocery List
- **Actor:** User
- **Precondition:** The weekly plan has at least one meal.
- **Main Flow:**
  1. User clicks "Generate grocery list".
  2. System collects the ingredients of every planned recipe.
  3. System groups ingredients by name and lists their measures.
  4. System stores and displays the grocery list.
- **Alternative Flows:**
  - Empty plan: system shows "Add meals to your plan first."

### Use Cases After MVP
- **Edit Saved Recipe:** update notes, ingredients or servings of a saved recipe.
- **Delete Saved Recipe:** remove a recipe from the collection (and from any plan entries).
- **Remove or Move Plan Entry:** change or clear a meal slot.
- **Check Off Grocery Item:** mark items as bought; state persists.
- **Random Recipe:** "Inspire me" button that shows a random recipe.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case | Also Responsible For | Post-MVP Use Case |
|-------------|--------------|----------------------|-------------------|
| [Member A] | Search Recipes | API gateway, app builder, view manager | Random Recipe |
| [Member B] | Save Recipe | Recipe/Ingredient entities, recipe DAO, saved recipes view | Edit and Delete Saved Recipe |
| [Member C] | Add Meal to Plan | MealPlan/MealPlanEntry entities, plan DAO, meal plan view | Remove or Move Plan Entry |
| [Member D] | Generate Grocery List | GroceryItem entity, grocery DAO, grocery list view | Check Off Grocery Item |

---

## Entities

### Recipe
- **Instance variables:** `id`, `apiId`, `name`, `category`, `ingredients: List<Ingredient>`, `instructions`, `imageUrl`, `servings`, `userNotes`
- **Methods:** getters, `updateNotes(String)`, `replaceIngredient(Ingredient, Ingredient)`, `setServings(int)`

### Ingredient
- **Instance variables:** `name`, `measure`
- **Methods:** getters

### MealPlan
- **Instance variables:** `weekStartDate`, `entries: List<MealPlanEntry>`
- **Methods:** `addEntry(MealPlanEntry)`, `removeEntry(DayOfWeek, MealType)`, `getEntriesForDay(DayOfWeek)`, `getAllRecipes()`

### MealPlanEntry
- **Instance variables:** `day: DayOfWeek`, `mealType: MealType`, `recipe: Recipe`
- **Methods:** getters

### GroceryItem
- **Instance variables:** `ingredientName`, `measures: List<String>`, `checked`
- **Methods:** `addMeasure(String)`, `toggleChecked()`, getters

---

## API Usage

**API Name:** TheMealDB

**Documentation:** https://www.themealdb.com/api.php

**How it is used in the program:**
- `search.php?s={name}` to search recipes by name (Use Case 1)
- `filter.php?i={ingredient}` to search recipes by a single ingredient (Use Case 1)
- `lookup.php?i={id}` to fetch full recipe details before saving (Use Case 2)
- `random.php` for the "Inspire me" feature (post-MVP)

We use the free developer test key (`1`). Multi-ingredient filtering is a paid feature, so we only support single-ingredient search.

**Status:** Not yet called from our Java program. [Update once tested.]

---

## Data Persistence

Data is stored locally in a SQLite database using the `sqlite-jdbc` library.

| Table | Stores |
|-------|--------|
| `recipes` | saved recipes and user edits |
| `recipe_ingredients` | ingredients and measures for each saved recipe |
| `meal_plan_entries` | day, meal slot and recipe for each planned meal |
| `grocery_items` | generated grocery list items and checked state |

Edited recipes are stored in our database, so user changes are never overwritten by the API.

---

## Architecture

We follow Clean Architecture, mirroring the structure of the `ca-login` example from lecture.

```
src/main/java/
├── app/                 AppBuilder, Main
├── entity/              Recipe, Ingredient, MealPlan, MealPlanEntry, GroceryItem
├── use_case/            one package per use case (interactor, boundaries, data, DAO interface)
├── interface_adapter/   controllers, presenters, view models, ViewManagerModel
├── data_access/         MealDbApiGateway, SQLite DAOs, in-memory DAOs for testing
└── view/                Swing views and ViewManager
```

Dependencies point inward: `view` → `interface_adapter` → `use_case` → `entity`. Use cases depend only on interfaces they define; concrete SQLite and HTTP classes are wired in `AppBuilder`.

---

## Design Diagrams

### UML Class Diagram
[To be added. PlantUML source will live in `docs/`.]

### Sequence Diagrams
[To be added for each MVP use case.]

---

## How to Run

[To be completed once the project builds.]

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Run `app.Main`.

---

## Notes

- All changes go through pull requests; at least one teammate reviews each PR.
- Each PR updates this README if it changes a use case, entity or API status.
