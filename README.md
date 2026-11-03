# SOLID Meals: Meal Planner

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
- **Primary actor:** User
- **Supporting actor:** TheMealDB (external recipe service)
- **Preconditions:** None.
- **Trigger:** The user asks to search for recipes by name or by ingredient.
- **Success outcome:** The user sees the recipes that match their search, each with its name, category and image.
- **Main success scenario:**
  1. The user enters a recipe name or a single ingredient and says which kind of search it is.
  2. The system requests matching recipes from the recipe service.
  3. The system displays the matching recipes with their name, category and image.
- **Extensions:**
  - **2a.** The recipe service cannot be reached or returns an error.
    1. The system explains that the search failed and the user may try again.
    2. The previously displayed results remain, and the use case ends.
  - **2b.** No recipes match.
    1. The system tells the user that no recipes were found, and the use case ends.

### Use Case 2: Save Recipe
- **Primary actor:** User
- **Supporting actor:** TheMealDB (external recipe service)
- **Preconditions:** The user is viewing search results.
- **Trigger:** The user asks to save one of the displayed recipes.
- **Success outcome:** The recipe, with its full ingredients, measures and instructions, is in the user's saved collection and is still there after the app restarts.
- **Main success scenario:**
  1. The user asks to save a recipe from the results.
  2. The system checks that the recipe is not already in the user's collection.
  3. The system retrieves the recipe's full details from the recipe service.
  4. The system stores the recipe in the user's collection.
  5. The system tells the user the recipe was saved.
- **Extensions:**
  - **2a.** The recipe is already in the collection.
    1. The system tells the user the recipe is already in their collection.
    2. The collection is unchanged, and the use case ends.
  - **3a.** The recipe service cannot provide the details.
    1. The system explains that the recipe was not saved and the user may try again.
    2. Nothing is saved, and the use case ends.

### Use Case 3: Add Meal to Plan
- **Primary actor:** User
- **Preconditions:** The user has at least one saved recipe.
- **Trigger:** The user asks to add a saved recipe to the weekly plan.
- **Success outcome:** The chosen day and meal slot in the weekly plan holds the chosen recipe, and the plan is kept between sessions.
- **Main success scenario:**
  1. The user chooses a saved recipe, a day of the week and a meal slot (breakfast, lunch or dinner).
  2. The system checks that the slot is empty.
  3. The system records the recipe in that slot of the weekly plan.
  4. The system shows the updated weekly plan.
- **Extensions:**
  - **2a.** The slot already holds a meal.
    1. The system shows the meal currently in the slot and asks whether to replace it.
    2. If the user confirms, the use case continues at step 3 and the new recipe replaces the old one.
    3. If the user declines, the plan is unchanged and the use case ends.
  - **3a.** The system cannot record the entry.
    1. The system explains that the meal was not added and the user may try again.
    2. The plan is unchanged, and the use case ends.

### Use Case 4: Generate Grocery List
- **Primary actor:** User
- **Preconditions:** None.
- **Trigger:** The user asks for a grocery list for their weekly plan.
- **Success outcome:** A grocery list is saved and shown. It contains every ingredient from the planned meals, with each ingredient listed once alongside all of its measures.
- **Main success scenario:**
  1. The user asks to generate a grocery list.
  2. The system gathers the ingredients of every recipe in the weekly plan.
  3. The system combines ingredients with the same name, keeping each measure.
  4. The system saves the grocery list, replacing any previous list.
  5. The system displays the grocery list.
- **Extensions:**
  - **2a.** The weekly plan has no meals.
    1. The system tells the user to add meals to their plan first.
    2. No list is created, and the use case ends.

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
