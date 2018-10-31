package com.udaysawhney.letsbake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.udaysawhney.letsbake.adapters.RecipeDetailAdapter;
import com.udaysawhney.letsbake.fragments.RecipeDetailFragment;
import com.udaysawhney.letsbake.model.Constants;
import com.udaysawhney.letsbake.model.Recipe;

import java.util.ArrayList;

import static com.udaysawhney.letsbake.model.Constants.INTENT_EXTRA_NAME_STEP_DETAILS_INDEX;
import static com.udaysawhney.letsbake.model.Constants.INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener {

    private Recipe recipe;
    private FragmentManager fragmentManager;
    private RecipeDetailFragment recipeDetailFragment;

    private static final String SAVED_STEP_SELECTED_INDEX_KEY = "saved_step_selected_index";
    private static final String SAVED_RECIPE_KEY = "saved_recipe";
    private int stepSelectedIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            loadDataFromExtras();
            return;
        }
        loadFromSavedInstanceState(savedInstanceState);
    }

    private void loadDataFromExtras() {
        Intent intent = getIntent();
        if (!intent.hasExtra(Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS)) {
            return;
        }
        Bundle data = intent.getExtras();
        assert data != null;
        recipe = data.getParcelable(Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS);
        updateActionBar();
        openRecipeDetailFragment();
    }

    private void updateActionBar() {
        assert recipe != null;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(recipe.getName());
    }

    private void openRecipeDetailFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_DETAILS_FRAGMENT_ARGUMENT, recipe);
        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_details_fragment_container, recipeDetailFragment)
                .commit();
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {
        recipe = savedInstanceState.getParcelable(SAVED_RECIPE_KEY);
        recipeDetailFragment = (RecipeDetailFragment) fragmentManager.
                findFragmentById(R.id.recipe_details_fragment_container);
        stepSelectedIndex = savedInstanceState.getInt(SAVED_STEP_SELECTED_INDEX_KEY, 0);
        recipeDetailFragment.setSelectionIndex(stepSelectedIndex);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_RECIPE_KEY, recipe);
        outState.putInt(SAVED_STEP_SELECTED_INDEX_KEY, stepSelectedIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(int index) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putParcelableArrayListExtra(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST, new ArrayList<>(recipe.getSteps()));
        intent.putExtra(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX, index);
        startActivity(intent);
    }
}
