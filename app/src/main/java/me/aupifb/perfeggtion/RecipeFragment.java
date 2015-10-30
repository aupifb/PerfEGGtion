package me.aupifb.perfeggtion;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.util.UUID;

public class RecipeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_PHOTO = 2, CHANGED_PHOTO = 0;
    private Recipe mRecipe;
    private EditText mTitleField, mEditTextDuration;
    private Button mButtonDelete;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private ImageView mGlideView;

    public static RecipeFragment newInstance(long recipeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, recipeId);

        RecipeFragment fragment = new RecipeFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        PackageManager packageManager = getActivity().getPackageManager();

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mRecipe.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                mRecipe.save();
            }
        });

        mEditTextDuration = (EditText) v.findViewById(R.id.recipe_duration);
        mEditTextDuration.setText(Integer.toString(mRecipe.getDurationSec()));
        mEditTextDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    mRecipe.setDurationSec(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mRecipe.save();
            }
        });

        mButtonDelete = (Button) v.findViewById(R.id.button_recipe_delete);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeKitchen.get(getContext()).deleteRecipe(mRecipe);
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });


        mPhotoButton = (ImageButton) v.findViewById(R.id.recipe_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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

        mGlideView = (ImageView) v.findViewById(R.id.recipe_photo_fresco);
        Glide.with(this).load(mPhotoFile).signature(new StringSignature(mRecipe.getSignature())).into(mGlideView);
        mGlideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment zoomFragment = ZoomFragment.newInstance(mRecipe.getId());
                zoomFragment.setTargetFragment(RecipeFragment.this, CHANGED_PHOTO);
                zoomFragment.show(getFragmentManager(), "zoomdialog");
            }
        });

        return v;
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

        if (requestCode == CHANGED_PHOTO) {
            updatePhotoView();
        }

    }
}
