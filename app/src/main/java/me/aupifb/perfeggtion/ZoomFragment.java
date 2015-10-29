package me.aupifb.perfeggtion;


import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoomFragment extends DialogFragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private File mPhotoFile;
    private ImageView mGlideZoomView;
    private Recipe mRecipe;
    private ImageView mGlideView;

    public ZoomFragment() {
        // Required empty public constructor
    }

    public static ZoomFragment newInstance(long recipeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, recipeId);

        ZoomFragment fragment = new ZoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long recipeId = getArguments().getLong(ARG_CRIME_ID, 1);
        mRecipe = RecipeKitchen.get(getActivity()).getRecipe(recipeId);
        mPhotoFile = RecipeKitchen.get(getActivity()).getPhotoFile(mRecipe);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_photozoom, null);

        mGlideView = (ImageView) v.findViewById(R.id.zoom_photo);
        Glide.with(this).load(mPhotoFile).signature(new StringSignature(mRecipe.getSignature())).into(mGlideView);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
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
