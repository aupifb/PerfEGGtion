package me.aupifb.perfeggtion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

public class RecipePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID =
            "me.aupifb.perfeggtion.recipe_id";

    private ViewPager mViewPager;
    private List<Recipe> mCrimes;

    public static Intent newIntent(Context packageContext, long crimeId) {
        Intent intent = new Intent(packageContext, me.aupifb.perfeggtion.RecipePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_pager);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long crimeId = getIntent()
                .getLongExtra(EXTRA_CRIME_ID, 1);

        mViewPager = (ViewPager) findViewById(R.id.activity_recipe_pager_view_pager);

        mCrimes = RecipeKitchen.get(this).getRecipes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Recipe recipe = mCrimes.get(position);
                return RecipeFragment.newInstance(recipe.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
