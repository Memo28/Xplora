package com.example.memovaradegante.xploraapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.models.Places_Model;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Memo Vara De Gante on 21/08/2017.
 */

public class MyAdapterListPlace extends RecyclerView.Adapter<MyAdapterListPlace.ViewHolder> {

    private Context context;
    private int layout;
    private List<Places_Model> places;
    private Activity activity;
    private OnItemClickListener listener;



    public MyAdapterListPlace(int layout, List<Places_Model> places, OnItemClickListener lister) {
        this.layout = layout;
        this.places = places;
        this.listener = lister;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(places.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitle;
        public TextView textViewCity;
        public RatingBar ratingBarPlace;
        public ImageView imageViewPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.text_view_User_Comment);
            textViewCity = (TextView) itemView.findViewById(R.id.text_view_Information_Comment);
            ratingBarPlace = (RatingBar) itemView.findViewById(R.id.ratingBar_List_Place);
            imageViewPlace = (ImageView) itemView.findViewById(R.id.imageView_Comment);
        }
        public void bind(final Places_Model place, final OnItemClickListener listener){
            textViewTitle.setText(place.getTitle());
            textViewCity.setText(place.getCountry());
            ratingBarPlace.setRating(Float.parseFloat("2.5" ));
            Picasso.with(activity).load(place.getPoster()).fit().into(imageViewPlace);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(place,getAdapterPosition());
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onItemClick(Places_Model place,int position);
    }
}
