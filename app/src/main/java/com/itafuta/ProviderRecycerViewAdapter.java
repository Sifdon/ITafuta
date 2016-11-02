package com.itafuta;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by victor on 9/19/16.
 */
public class ProviderRecycerViewAdapter
        extends RecyclerView.Adapter<ProviderRecycerViewAdapter.DataHolder>
{

    private ArrayList<ProviderData> mDataset;
    private ItemClickListener clickListener;//The click lister global variable


    //Create the DataHolder and inside it  #Create its constructor (That matches )
    //This represents how the text will be displayed
    public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView providerImage;
        TextView providerLocation;
        TextView providerDetails;
        TextView providerFavCount;
        TextView providerName;
        RatingBar providerRating;

        //Adds the constructor for DataHolder class here
        public DataHolder(View itemView){
            super(itemView);


            providerImage = (ImageView) itemView.findViewById(R.id.imgProviderImage);
            providerLocation = (TextView) itemView.findViewById(R.id.txtProviderInfoLocation);
            providerDetails = (TextView) itemView.findViewById(R.id.txtProviderInfo);
            providerFavCount = (TextView) itemView.findViewById(R.id.txtFavCount);
            providerName = (TextView) itemView.findViewById(R.id.txtProviderName);
            providerRating = (RatingBar) itemView.findViewById(R.id.ratingProvider);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }

    }

    //This is your class constructor that will take Arraylist
    // contains ProviderData object(you definedd this in a different class) you created
    public ProviderRecycerViewAdapter(ArrayList<ProviderData> myDataset){
        mDataset = myDataset;
    }


    //DEFINE WHICH LAYOUT TO REUSE
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType){

        //Inflate the card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_service_provider, parent, false);
        return new DataHolder(v);
    }


    //Inside the onBindViewHolder,
    // Bind the items in the arralist(mDataSet) to the ones in your card view (in the DataHolder)
    public void onBindViewHolder(DataHolder holder, int position){

        //byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        //byte[] imageAsBytes = Base64.decode(mDataset.get(position).getProfPhoto(), Base64.DEFAULT);
        /*
        byte[] decodedString = Base64.decode(StringEscapeUtils.unescapeJson(object.getString("image")), Base64.DEFAULT);
        mLoadedImage.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );
        */


        String image = String.valueOf(mDataset.get(position).getProfPhoto());
        byte[] imageAsBytes = Base64.decode(image, Base64.DEFAULT);

        holder.providerImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        //holder.providerImage.setImageResource(mDataset.get(position).getProfPhoto());
        holder.providerLocation.setText(mDataset.get(position).getLocation());
        holder.providerDetails.setText(mDataset.get(position).getOccupation().keySet().toString()); //Set this to string
        holder.providerFavCount.setText(mDataset.get(position).getProvFavCount());
        holder.providerName.setText(mDataset.get(position).getUsername());
        holder.providerRating.setRating(mDataset.get(position).getProvRate());
        mDataset.get(position).getUserid();
    }

    //Method to data objects into our arraylist (mDataSet)
    public void addItem(ProviderData data, int index){
        mDataset.add(index, data);
    }

    //Make sure you gave this count
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    //*********** Setting the click listener *********
    //listener interface.

    public interface ItemClickListener {
        void onClick(View view, int position);
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    //*********** Setting the click listener *********

}
