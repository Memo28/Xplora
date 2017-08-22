package com.example.memovaradegante.xploraapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterListPlace;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterPlace;
import com.example.memovaradegante.xploraapp.models.Places_Model;

import java.util.ArrayList;
import java.util.List;

public class ListDestiniesActivity extends AppCompatActivity {

    private List<Places_Model> places;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_destinies);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        places = this.getAllPlaces();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_list_destinies);
        mlayoutManager = new LinearLayoutManager(this);
        madapter = new MyAdapterListPlace(R.layout.recycler_view_item_list_destiny,places,
        new MyAdapterListPlace.OnItemClickListener() {
            @Override
            public void onItemClick(Places_Model place, int position) {
                Log.e("Push",place.getTitle());
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(madapter);

    }


    //Obtenemos todos los lugares
    private List<Places_Model> getAllPlaces(){
        return new ArrayList<Places_Model>(){{
            add(new Places_Model("0","Piramide","Mexico","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/ch.jpg?alt=media&token=2f2923b5-5e84-4ec3-8fae-aaf212542d2a",""));
            add(new Places_Model("1","Volcan","Colombia","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9dZei6FmqKa4l7eFT%2F15965071_1250192065027150_5835714856368535408_n.jpg?alt=media&token=bdf10fa9-bd5c-4e1a-b4bb-bd3da12beeeb",""));
            add(new Places_Model("2","Desierto","Chile","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/CHias.jpg?alt=media&token=49bbb595-d68d-4866-8d51-cdb1d62f46fd",""));
        }
        };
    }
}
