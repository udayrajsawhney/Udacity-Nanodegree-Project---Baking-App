package com.udaysawhney.letsbake;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.udaysawhney.letsbake.adapters.RecipeAdapter;
import com.udaysawhney.letsbake.idling_resource.SimpleIdlingResource;
import com.udaysawhney.letsbake.model.Ingredient;
import com.udaysawhney.letsbake.model.Recipe;
import com.udaysawhney.letsbake.widget.ListRemoteViewsService;

import java.util.ArrayList;

import static com.udaysawhney.letsbake.model.Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS;
import static com.udaysawhney.letsbake.model.Constants.WIDGET_EXTRA_NAME_INGREDIENT;
import static com.udaysawhney.letsbake.model.Constants.WIDGET_EXTRA_NAME_QUANTITY;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @Nullable
    private SimpleIdlingResource idlingResource;

    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Check","In main activity");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    @Override
    public void onListItemClick(Recipe recipe) {
        Log.d("Check","In main activity item list");
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(INTENT_EXTRA_NAME_RECIPE_DETAILS, recipe);
            startActivity(intent);
            return;
        }
        updateAppWidget(recipe);
    }

    private void updateAppWidget(Recipe recipe) {
        Log.d("Check","In main activity update widget");
        applyWidgetConfiguration(recipe);
        Intent intentWidget = new Intent();
        intentWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intentWidget);
        finish();
    }

    private void applyWidgetConfiguration(Recipe recipe) {
        Log.d("Check","In main activity apply widget");
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.baking_widget_provider);
        views.setTextViewText(R.id.widget_title, recipe.getName());
        Intent ingredientWidgetListIntent = getWidgetIngredientListIntent(context, recipe);
        views.setRemoteAdapter(R.id.widget_list, ingredientWidgetListIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private Intent getWidgetIngredientListIntent(Context context, Recipe recipe) {
        Log.d("Check","In main activity get widget ingredient");
        Intent intent = new Intent(context, ListRemoteViewsService.class);
        ArrayList<String> ingredientList = new ArrayList<>();
        ArrayList<String> quantityList = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientList.add(ingredient.getIngredient());
            quantityList.add(String.format("%s %s", ingredient.getQuantity(), ingredient.getMeasure()));
        }
        intent.putStringArrayListExtra(WIDGET_EXTRA_NAME_INGREDIENT, ingredientList);
        intent.putStringArrayListExtra(WIDGET_EXTRA_NAME_QUANTITY, quantityList);
        return intent;
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        Log.d("Check","In main activity idling");
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}
