package com.udaysawhney.letsbake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.udaysawhney.letsbake.adapters.RecipeAdapter;
import com.udaysawhney.letsbake.model.Recipe;

import static com.udaysawhney.letsbake.model.Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Log.d("Check","In main activity");
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListItemClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_NAME_RECIPE_DETAILS, recipe);
        startActivity(intent);
    }
}
