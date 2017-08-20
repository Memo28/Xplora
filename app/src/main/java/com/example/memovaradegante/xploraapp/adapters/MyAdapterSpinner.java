package com.example.memovaradegante.xploraapp.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.memovaradegante.xploraapp.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Memo Vara De Gante on 19/08/2017.
 */

public class MyAdapterSpinner extends ArrayAdapter<String> {

    private Context context;
    private String[] costs;
    private int[] coins;


    public MyAdapterSpinner (Context context, String[] costs, int[] coins){
        super(context, R.layout.spinner_item,costs);
        this.context = context;
        this.costs = costs;
        this.coins = coins;
    }

    @Override
    public View getDropDownView(int position, View convertView,  ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item,null);

        }

        TextView textViewCostSpinner = (TextView) convertView.findViewById(R.id.cost_spinner);
        ImageView imageViewCoinSpinner = (ImageView) convertView.findViewById(R.id.coin_spinner);

        textViewCostSpinner.setText(costs[position]);
        imageViewCoinSpinner.setImageResource(coins[position]);

        return convertView;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item,null);

        }

        TextView textViewCostSpinner = (TextView) convertView.findViewById(R.id.cost_spinner);
        ImageView imageViewCoinSpinner = (ImageView) convertView.findViewById(R.id.coin_spinner);

        textViewCostSpinner.setText(costs[position]);
        imageViewCoinSpinner.setImageResource(coins[position]);

        return convertView;
    }
}
