package me.aupifb.perfeggtion;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.signature.StringSignature;

import java.util.UUID;

/**
 * Created by aupifb on 01/10/2015.
 */
public class NewRecipeDialog extends DialogFragment {


    public static NewRecipeDialog newInstance() {
        NewRecipeDialog fragment = new NewRecipeDialog();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_new_recipe, null);

        final EditText editText_title = (EditText) v.findViewById(R.id.editText_title);
        final EditText editText_duration = (EditText) v.findViewById(R.id.editText_duration);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.new_recipe_dialog_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String recipeTitle = editText_title.getText().toString();
                                int recipeDuration;
                                if (recipeTitle.length() > 0 && editText_duration.length() > 0) {
                                    recipeDuration = Integer.parseInt(editText_duration.getText().toString());
                                    Recipe mRecipe = new Recipe(recipeTitle, recipeDuration, new StringSignature(UUID.randomUUID().toString()));
                                    RecipeKitchen.get(getContext()).addRecipe(mRecipe);
                                } else {
                                    if (recipeTitle.length() == 0) {
                                        editText_title.setHint("Please enter a title");
                                    }
                                    if (editText_duration.length() < 1) {
                                        editText_duration.setHint("Please enter a value");
                                    }
                                }
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();
    }

}