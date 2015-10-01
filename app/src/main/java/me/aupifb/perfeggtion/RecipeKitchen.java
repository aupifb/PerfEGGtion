package me.aupifb.perfeggtion;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aupifb on 28/09/2015.
 */
public class RecipeKitchen {

    private static RecipeKitchen sRecipeKitchen;

    private List<Recipe> mRecipes;

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
        r.save();
        mRecipes.add(r);
    }

    public void deleteRecipe(Recipe r) {
        mRecipes.remove(r);
        r.delete();
    }

    public void updateRecipes() {
        mRecipes = Recipe.listAll(Recipe.class);
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(long id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }


}
