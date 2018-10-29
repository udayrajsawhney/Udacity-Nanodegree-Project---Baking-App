package com.udaysawhney.letsbake.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udaysawhney.letsbake.MainActivity;
import com.udaysawhney.letsbake.R;
import com.udaysawhney.letsbake.adapters.RecipeAdapter;
import com.udaysawhney.letsbake.model.Recipe;
import com.udaysawhney.letsbake.network.APIClient;
import com.udaysawhney.letsbake.network.APIInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recyclerView;

    private Bundle savedInstanceState;
    private static final String SAVED_LAYOUT_MANAGER_KEY = "saved_layout_manager";

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        this.savedInstanceState = savedInstanceState;
        applyConfiguration(rootView);
        Log.d("Check","In recipe fragment");
        return rootView;
    }

    private void applyConfiguration(View rootView) {
        RecipeAdapter recipesAdapter = new RecipeAdapter((MainActivity) getActivity());
        applyLayoutManager(rootView);
        recyclerView.setAdapter(recipesAdapter);
        fetchRecipeData(recipesAdapter);
        Log.d("Check","In recipe fragment apply config");
    }

    private void applyLayoutManager(View rootView) {
        if (rootView.getTag() != null && rootView.getTag().equals("sw-600")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),
                    getResources().getInteger(R.integer.grid_view_landscape_column_number));
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }
        Log.d("Check","In recipe fragment apply layout");
    }

    private void fetchRecipeData(final RecipeAdapter recipesAdapter) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<Recipe>> call = apiInterface.getRecipe();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipeList = response.body();
                recipesAdapter.setRecipeData(recipeList, getContext());
                restoreViewState();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable throwable) {
                Log.e("http error: ", throwable.getMessage());
            }
        });
        Log.d("Check","In recipe fragment retrofit");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_LAYOUT_MANAGER_KEY, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void restoreViewState() {
        if (savedInstanceState == null) {
            return;
        }
        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

}
