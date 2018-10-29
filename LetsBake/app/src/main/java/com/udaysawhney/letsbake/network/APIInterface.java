package com.udaysawhney.letsbake.network;

import com.udaysawhney.letsbake.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
