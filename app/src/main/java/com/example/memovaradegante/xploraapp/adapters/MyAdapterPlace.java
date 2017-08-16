package com.example.memovaradegante.xploraapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.models.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Memo Vara De Gante on 13/08/2017.
 */

public class MyAdapterPlace extends RecyclerView.Adapter<MyAdapterPlace.ViewHolder> {
    private List<Place> places;
    private int layout;
    private OnItemClickLister lister;
    private Context context;

    public MyAdapterPlace(List<Place> places, int layout, OnItemClickLister lister) {
        this.places = places;
        this.layout = layout;
        this.lister = lister;
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
        holder.bind(places.get(position),lister);

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewCountry;
        public ImageView imageViewCountry;

        public ViewHolder(View itemView) {

            super(itemView);
            textViewCountry = (TextView) itemView.findViewById(R.id.textViewTitle);
            imageViewCountry = (ImageView) itemView.findViewById(R.id.imageViewPoster);
        }

        public void bind(final Place place,final OnItemClickLister lister){
            textViewCountry.setText(place.getCountry());
            Picasso.with(context).load(place.getPoster()).fit().into(imageViewCountry);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lister.onItemClick(place,getAdapterPosition());
                }
            });
        }

    }

    public interface OnItemClickLister {
        void onItemClick(Place place,int position);
    }
}
