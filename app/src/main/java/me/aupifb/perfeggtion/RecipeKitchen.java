package me.aupifb.perfeggtion;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by aupifb on 28/09/2015.
 */
public class RecipeKitchen {

    private static RecipeKitchen sRecipeKitchen;

    private ArrayList<Recipe> mRecipes;

    private RecipeKitchen(Context context) {
        mRecipes = new ArrayList<>();
    }

    public static RecipeKitchen get(Context context) {
        if (sRecipeKitchen == null) {
            sRecipeKitchen = new RecipeKitchen(context);
        }
        return sRecipeKitchen;
    }

    public void addRecipe(Recipe r) {
        mRecipes.add(r);
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }


}
