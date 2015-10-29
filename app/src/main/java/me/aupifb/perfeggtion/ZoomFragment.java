package me.aupifb.perfeggtion;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoomFragment extends DialogFragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_PHOTO = 2;
    private Recipe mRecipe;
    private ImageView mGlideView;
    private Button mButtonDelete;
    private File mPhotoFile;
    private ImageButton mPhotoButton;

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

        mPhotoButton = (ImageButton) v.findViewById(R.id.recipe_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

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

    private void updatePhotoView() {
        mRecipe.setSignature(UUID.randomUUID().toString());
        mRecipe.save();
        Glide.with(this).load(mPhotoFile).signature(new StringSignature(mRecipe.getSignature())).into(mGlideView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            updatePhotoView();
        }

    }
}