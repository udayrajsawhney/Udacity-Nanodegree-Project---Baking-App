package com.udaysawhney.letsbake.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udaysawhney.letsbake.R;
import com.udaysawhney.letsbake.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    private ArrayList<Recipe> recipeList;
    private Context context;
    final private ListItemClickListener itemOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedItemIndex);
    }

    public void setRecipeData(ArrayList<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
        notifyDataSetChanged();
    }

    public RecipeAdapter(ListItemClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context viewContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_cardview_item;
        LayoutInflater inflater = LayoutInflater.from(viewContext);
        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        recyclerViewHolder.textView.setText(recipeList.get(i).getName());
        String imageUrl = recipeList.get(i).getImage();
        Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
        Picasso.get()
                .load(builtUri)
                .placeholder(R.drawable.ic_cake_white_100dp)
                .into(recyclerViewHolder.imageView);
        recyclerViewHolder.imageView.setScaleType(imageUrl.equals("") ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipeTitle)
        TextView textView;
        @BindView(R.id.recipeImage)
        ImageView imageView;

        private RecyclerViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            itemOnClickListener.onListItemClick(recipeList.get(clickedPosition));
        }
    }
}
