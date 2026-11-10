package use_case.save_recipe;

import entity.Recipe;
import use_case.common.RecipeServiceException;

public interface RecipeDetailsGateway {
    Recipe fetchById(String apiId) throws RecipeServiceException;
}
