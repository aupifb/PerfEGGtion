package me.aupifb.perfeggtion;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String DIALOG_NEW_RECIPE = "DialogNewRecipe";
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;


    private File mPhotoFile;
    private ImageView mPhotoViewList;

    /*@Bind(R.id.alacoque)
    ImageButton alacoque;*/


    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        RecipeKitchen.get(getContext()).updateRecipes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view2 = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        setRetainInstance(true);

        ButterKnife.bind(this, view2);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mRecipeRecyclerView = (RecyclerView) view2
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return (view2);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                FragmentManager manager = getFragmentManager();
                NewRecipeDialog dialog = NewRecipeDialog
                        .newInstance();
                dialog.show(manager, DIALOG_NEW_RECIPE);
                //Recipe recipe = new Recipe("ha", 20);
                //RecipeKitchen.get(getActivity()).addRecipe(recipe);
                //Intent intent = RecipePagerActivity
                //.newIntent(getActivity(), crime.getId());
                //startActivity(intent);
                updateUI();
                return true;
            default:
                updateUI();
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateUI() {
        RecipeKitchen recipeKitchen = RecipeKitchen.get(getActivity());
        List<Recipe> recipes = recipeKitchen.getRecipes();

        if (mAdapter == null) {
            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
    }

    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mStartButton;

        private Recipe mRecipe;

        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);

            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);

            mPhotoViewList = (ImageView) itemView.findViewById(R.id.recipe_photo_list);

            mStartButton = (Button) itemView.findViewById(R.id.button_start);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).countdownstart(mRecipe.getDurationSec());
                    ((MainActivity) getActivity()).setCurrentMainViewPagerItem(0);
                }
            });

        }

        public void bindRecipe(Recipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(mRecipe.getTitle());
            mDateTextView.setText(Integer.toString(mRecipe.getDurationSec()));
            mPhotoFile = RecipeKitchen.get(getActivity()).getPhotoFile(mRecipe);
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mPhotoViewList.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), getActivity());
                mPhotoViewList.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = RecipePagerActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
            Toast.makeText(getActivity(), mRecipe.getId().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<Recipe> mRecipes;

        public RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.bindRecipe(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }
    }

    /*@OnClick(R.id.alacoque)
    public void dothis() {
        Log.d("lol", "dothis ");
        ((MainActivity) getActivity()).countdownstart(180);
    }*/

}
