package me.aupifb.perfeggtion;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aupifb on 28/09/2015.
 */
public class RecipeKitchen {

    private static RecipeKitchen sRecipeKitchen;

    private List<Recipe> mRecipes;

    private Context mContext;

    private RecipeKitchen(Context context) {
        mContext = context.getApplicationContext();
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

    public File getPhotoFile(Recipe recipe) {
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, recipe.getPhotoFilename());
    }


}
