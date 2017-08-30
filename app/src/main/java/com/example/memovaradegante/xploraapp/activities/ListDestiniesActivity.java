package com.example.memovaradegante.xploraapp.activities;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListDestiniesActivity extends AppCompatActivity {

    private List<Places_Model> places;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutManager;

    private String city_pass;
    private String user_actual;
    private String photo_UrlUser;
    private String name_actual_u;


    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_destinies);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("places");
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            city_pass = extras.getString("Country");
            user_actual = extras.getString("user_actual");
            photo_UrlUser = extras.getString("photo_UrlUser");
            name_actual_u = extras.getString("name_actual_u");
        }else {
            onRestoreInstanceState(savedInstanceState);
            Log.e("City",city_pass);

        }



        places = this.getAllPlaces();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_list_destinies);
        mlayoutManager = new LinearLayoutManager(this);
        madapter = new MyAdapterListPlace(R.layout.recycler_view_item_list_destiny,places,
        new MyAdapterListPlace.OnItemClickListener() {
            @Override
            public void onItemClick(Places_Model place, int position) {
                Log.e("Push",place.getTitle());

                //Pass Information to the PostDestinyActivity
                Intent intent = new Intent(getApplicationContext(), PostDestinyActivity.class);
                intent.putExtra("Id",place.getId());
                intent.putExtra("user_actual",user_actual);
                intent.putExtra("photo_UrlUser",photo_UrlUser);
                intent.putExtra("name_actual_u",name_actual_u);
                startActivity(intent);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(madapter);

    }


    //Obtenemos todos los lugares
    private List<Places_Model> getAllPlaces(){

        final ArrayList<Places_Model> places_list = new ArrayList<Places_Model>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String city = ds.child("country").getValue().toString();
                        if (city.contains(city_pass)){
                            Places_Model pl = ds.getValue(Places_Model.class);
                            places_list.add(pl);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return places_list;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city",city_pass);
        Log.e("onSaved",outState.getString("city"));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        city_pass = savedInstanceState.getString("city");
    }
}
