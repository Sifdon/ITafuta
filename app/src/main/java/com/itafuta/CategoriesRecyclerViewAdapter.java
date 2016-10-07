package com.itafuta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by victor on 9/21/16.
 */
public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoryHolder> {

    ArrayList<CategoriesObject> mCategory;

    //Constructor
    public CategoriesRecyclerViewAdapter(ArrayList<CategoriesObject> myCategory){
        this.mCategory = myCategory;
    }
    public class CategoryHolder extends RecyclerView.ViewHolder{

        ImageView catImage;
        TextView catText;

        public CategoryHolder(View itemView){
            super(itemView);
            catImage = (ImageView) itemView.findViewById(R.id.imgCategory);
            catText = (TextView) itemView.findViewById(R.id.txtCategory);
        }

    }

    //DEFINE WHICH LAYOUT TO REUSE
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_categories, parent, false);
        return new CategoryHolder(v);
    }

    //Bind dataset to your defined view
    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
//        holder.catImage.setImageBitmap(mCategory.get(position).getCatImage());
        holder.catImage.setImageResource(mCategory.get(position).getCatImage());
        holder.catText.setText(mCategory.get(position).getCatTitle());
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }
}
