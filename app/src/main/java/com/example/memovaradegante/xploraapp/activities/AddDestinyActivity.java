package com.example.memovaradegante.xploraapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterSpinner;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AddDestinyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextTitle;
    private EditText editTextPlace;
    private Spinner spinnerType;
    private Spinner spinnerPrice;
    private EditText editTextDescription;
    private ImageView imageViewPoster;


    private static final int GALLERY_INTENT = 1;
    private int STORAGE_PERMISSION_CODE = 23;

    private Uri uriImageAddDestiny;


    private String[] costs = {"Bajo","Medio","Alto"};
    private int[] coins = {R.drawable.coin_g,R.drawable.coin_g,R.drawable.coin_g};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destiny);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        editTextTitle = (EditText) findViewById(R.id.editTextTitleAddDestiny);
        editTextPlace = (EditText) findViewById(R.id.editTextPlaceAddDestiny);
        spinnerType = (Spinner) findViewById(R.id.spinnerTypeAddDestiny);
        editTextDescription = (EditText) findViewById(R.id.editTextDescriptionAddDestiny);
        imageViewPoster = (ImageView) findViewById(R.id.imageViewAddDestiny);
        spinnerPrice = (Spinner) findViewById(R.id.spinnerCostAddDestiny);

        imageViewPoster.setOnClickListener(this);

        //Set info in the spinner

        MyAdapterSpinner adapterSpinner = new MyAdapterSpinner(this,costs,coins);
        spinnerPrice.setAdapter(adapterSpinner);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_destiny:
                this.addDestiny();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDestiny() {

    }

    @Override
    public void onClick(View view) {
        if (view == imageViewPoster){
            //Verificamos si se tienen permisos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Comprobar si ha aceptado, no ha aceptado o nunca se le ha preguntado
                if (CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) return;
                    startActivityForResult(intent,GALLERY_INTENT);
                } else {
                    //Ha denegado la primera vez el permiso
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    } else {
                        Toast.makeText(this, "Por favor concede el permiso", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(i);
                    }
                }
            } else {
                olderVersion();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_INTENT ) {
            if (resultCode == Activity.RESULT_OK) {
                uriImageAddDestiny = data.getData();
                Picasso.with(this).load(uriImageAddDestiny)
                        .transform(new RoundedCornersTransformation(10, 0)).fit().into(imageViewPoster);
            }
        }
    }

    private  boolean CheckPermission(String permision){
        int result = checkCallingOrSelfPermission(permision);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void olderVersion(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
        if (CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            startActivityForResult(intent,GALLERY_INTENT);
        }else {
            Toast.makeText(this, "Permiso no concedido", Toast.LENGTH_LONG).show();
        }
    }
}
