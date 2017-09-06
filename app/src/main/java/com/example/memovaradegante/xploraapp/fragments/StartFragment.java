package com.example.memovaradegante.xploraapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.activities.AddDestinyActivity;
import com.example.memovaradegante.xploraapp.activities.ListDestiniesActivity;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterPlace;
import com.example.memovaradegante.xploraapp.models.Places_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private List<Places_Model> places;
    private List<Places_Model> placesFab;

    private RecyclerView mRecyclerViewCountries;
    private RecyclerView.Adapter mAdapterCountries;
    private RecyclerView.LayoutManager mlayoutManagerCountries;

    private RecyclerView mRecyclerViewFab;
    private RecyclerView.Adapter mAdapterFab;
    private RecyclerView.LayoutManager mlayoutManagerFab;

    private FloatingActionButton fabAdd;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String user_actual;
    private String photo_UrlUser;
    private String name_actual_u;




    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        places = this.getAllPlaces();
        placesFab = this.getAllFabPlaces();

        fabAdd = (FloatingActionButton) v.findViewById(R.id.fltActionBtnAdd);

        //Inflando RecyclerView de Paises
        mRecyclerViewCountries = (RecyclerView) v.findViewById(R.id.my_recycler_view_countries);
        mlayoutManagerCountries = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mAdapterCountries = new MyAdapterPlace(places,R.layout.recycler_view_item,new MyAdapterPlace.OnItemClickLister(){

            @Override
            public void onItemClick(Places_Model place, int position) {
                //Pass Information to the Fragment
                Intent intent = new Intent(getActivity(), ListDestiniesActivity.class);
                intent.putExtra("Country",place.getCountry());
                intent.putExtra("user_actual",user_actual);
                intent.putExtra("photo_UrlUser",photo_UrlUser);
                intent.putExtra("name_actual_u",name_actual_u);
                startActivity(intent);
            }
        });

        mRecyclerViewCountries.setHasFixedSize(true);
        mRecyclerViewCountries.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewCountries.setLayoutManager(mlayoutManagerCountries);
        mRecyclerViewCountries.setAdapter(mAdapterCountries);

        //Inflando RecyclerView de Favoritos
        mRecyclerViewFab = (RecyclerView) v.findViewById(R.id.my_recycler_view_fab);
        mlayoutManagerFab = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mAdapterFab = new MyAdapterPlace(placesFab,R.layout.recycler_view_item,new MyAdapterPlace.OnItemClickLister(){

            @Override
            public void onItemClick(Places_Model place, int position) {
                Toast.makeText(getContext(),"OK", Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerViewFab.setHasFixedSize(true);
        mRecyclerViewFab.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewFab.setLayoutManager(mlayoutManagerFab);
        mRecyclerViewFab.setAdapter(mAdapterFab);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Obtenemos informacion del Usuario
        firebaseAuth = FirebaseAuth.getInstance();
        user_actual = firebaseAuth.getCurrentUser().getEmail();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddDestinyActivity.class);
                intent.putExtra("user_actual",user_actual);
                intent.putExtra("photo_UrlUser",photo_UrlUser);
                intent.putExtra("name_actual_u",name_actual_u);
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String user = ds.child("email").getValue().toString();
                        if (user.equals(user_actual)){
                            photo_UrlUser = ds.child("urlImage").getValue().toString();
                            name_actual_u = ds.child("name").getValue().toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.e("Start","Inicio");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Obtenemos todos los lugares
    private List<Places_Model> getAllPlaces(){
        return new ArrayList<Places_Model>(){{
            add(new Places_Model("0","Memo","Piramide","Mexico","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/ch.jpg?alt=media&token=2f2923b5-5e84-4ec3-8fae-aaf212542d2a",""));
            add(new Places_Model("1","Memo","Volcan","Colombia","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9dZei6FmqKa4l7eFT%2F15965071_1250192065027150_5835714856368535408_n.jpg?alt=media&token=bdf10fa9-bd5c-4e1a-b4bb-bd3da12beeeb",""));
            add(new Places_Model("2","Memo","Desierto","Chile","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/CHias.jpg?alt=media&token=49bbb595-d68d-4866-8d51-cdb1d62f46fd",""));
        }
        };
    }

    //Obtenemos todos los lugares favoritos

    //Obtenemos todos los lugares
    private List<Places_Model> getAllFabPlaces(){
        return new ArrayList<Places_Model>(){{
            add(new Places_Model("3","Memo","Piramide","Mexico","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/ch.jpg?alt=media&token=2f2923b5-5e84-4ec3-8fae-aaf212542d2a",""));
            add(new Places_Model("4","Memo","Volcan","Colombia","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9dZei6FmqKa4l7eFT%2F15965071_1250192065027150_5835714856368535408_n.jpg?alt=media&token=bdf10fa9-bd5c-4e1a-b4bb-bd3da12beeeb",""));
            add(new Places_Model("5","Memo","Desierto","Chile","Toma bus","Economico","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/CHias.jpg?alt=media&token=49bbb595-d68d-4866-8d51-cdb1d62f46fd",""));
        }
        };
    }



}
