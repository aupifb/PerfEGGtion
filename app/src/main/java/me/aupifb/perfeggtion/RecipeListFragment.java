package me.aupifb.perfeggtion;


import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

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

    private boolean isSearching = false;
    private Menu menuList;


    private File mPhotoFile;
    private ImageView mPhotoViewList;

    private TextView mTextViewEmpty;
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

        View view2 = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        setRetainInstance(true);

        ButterKnife.bind(this, view2);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mRecipeRecyclerView = (RecyclerView) view2
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTextViewEmpty = (TextView) view2.findViewById(R.id.fragment_recipe_list_text_empty);



        return (view2);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe_list, menu);

        this.menuList = menu;
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView =
                ( android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        MenuItem menuItem = menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                isSearching = false;
                updateUI(false);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            /*case R.id.reset_search:
                isSearching = false;
                updateUI(false);
                menuList.findItem(R.id.reset_search).setVisible(false);
                menuList.findItem(R.id.search).collapseActionView();
                return true;*/
        }
    }


    public void updateUI(boolean forceit) {
        if (!isSearching || forceit) {
        RecipeKitchen recipeKitchen = RecipeKitchen.get(getActivity());
        List<Recipe> recipes = recipeKitchen.getRecipes();
        mAdapter = new RecipeAdapter(recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
        mTextViewEmpty.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void updateUISEARCH(String query) {
        RecipeKitchen recipeKitchen = RecipeKitchen.get(getActivity());
        List<Recipe> recipes = recipeKitchen.getRecipesSearch(query);
        if (recipes.isEmpty()) {
            isSearching = true;
            mTextViewEmpty.setVisibility(View.VISIBLE);
            mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        } else {
        mAdapter = new RecipeAdapter(recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
        isSearching = true;
        //menuList.findItem(R.id.reset_search).setVisible(true);
        mTextViewEmpty.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
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
            mTitleTextView.setText(String.format(getResources().getString(R.string.recipe_title_list_fragment), mRecipe.getTitle()));
            mDateTextView.setText(String.format(getResources().getString(R.string.recipe_duration_list_fragment), mRecipe.getDurationSec()));
            mPhotoFile = RecipeKitchen.get(getActivity()).getPhotoFile(mRecipe);
            Glide.with(getContext()).load(mPhotoFile).signature(new StringSignature(mRecipe.getSignature())).into(mPhotoViewList);
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
