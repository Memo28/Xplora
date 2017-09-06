package com.example.memovaradegante.xploraapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterSpinner;
import com.example.memovaradegante.xploraapp.adapters.PlaceArrayAdapter;
import com.example.memovaradegante.xploraapp.models.Places_Model;
import com.example.memovaradegante.xploraapp.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AddDestinyActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {
    private EditText editTextTitle;
    private EditText editTextPlace;
    private Spinner spinnerType;
    private Spinner spinnerPrice;
    private EditText editTextDescription;
    private ImageView imageViewPoster;
    private AutoCompleteTextView mAutocompleteTextView;


    private static final int GALLERY_INTENT = 1;
    private int STORAGE_PERMISSION_CODE = 23;

    private Uri uriImageAddDestiny;
    private DatabaseReference databasePlace;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;




    private String[] costs = {"Bajo", "Medio", "Alto"};
    private int[] coins = {R.drawable.moneda_bronce, R.drawable.moneda_plata, R.drawable.moned_oro};
    private String city_pass;
    private String user_actual;
    private String photo_UrlUser;
    private String name_actual_u;

    private static final String TAG = "AddDestinyActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destiny);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            user_actual = extras.getString("user_actual");
        }



        editTextTitle = (EditText) findViewById(R.id.editTextTitleAddDestiny);
        spinnerType = (Spinner) findViewById(R.id.spinnerTypeAddDestiny);
        editTextDescription = (EditText) findViewById(R.id.editTextDescriptionAddDestiny);
        imageViewPoster = (ImageView) findViewById(R.id.imageViewAddDestiny);
        spinnerPrice = (Spinner) findViewById(R.id.spinnerCostAddDestiny);
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewAddDestiny);

        mAutocompleteTextView.setThreshold(3);
        imageViewPoster.setOnClickListener(this);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);


        //Set info in the spinner

        MyAdapterSpinner adapterSpinner = new MyAdapterSpinner(this,costs,coins);
        spinnerPrice.setAdapter(adapterSpinner);

        //AutoComplete in Destiny
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        //FireBase
        databasePlace = FirebaseDatabase.getInstance().getReference("places");
        storageReference = FirebaseStorage.getInstance().getReference();
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
        final String title = editTextTitle.getText().toString().trim();
        final String type = spinnerType.getSelectedItem().toString();
        final String cost = spinnerPrice.getSelectedItem().toString();
        final String place = mAutocompleteTextView.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();


        if (TextUtils.isEmpty(title)){
            Toast.makeText(this,"Por favor ingresa un titulo",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(place) && place.length() < 3){
            Toast.makeText(this,"Por favor ingresa un lugar",Toast.LENGTH_SHORT).show();
            return;
        }
        if (type == "Tipo de Destino"){
            Toast.makeText(this,"Por favor seleccione un tipo de destino",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Publicando experiencia...");
        progressDialog.show();



        final String id = databasePlace.push().getKey();
        final String pathImagePlace = "profileDestiny"+id;


        //Caso en el que el usuario haya seleccionado su imagen
        if(uriImageAddDestiny != null){
            StorageReference filepath = storageReference.child(pathImagePlace).child(uriImageAddDestiny.getLastPathSegment());
            filepath.putFile(uriImageAddDestiny).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri uriImage = taskSnapshot.getDownloadUrl();

                    Places_Model destiny = new Places_Model(id,user_actual,title,place,description,type,uriImage.toString(),cost);
                    databasePlace.child(id).setValue(destiny);
                    Toast.makeText(getApplicationContext(),"Destino agregado correctamente",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }else {
            Toast.makeText(this,"Por favor selecciona una imagen",Toast.LENGTH_SHORT).show();
        }








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


    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for id: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mAutocompleteTextView.setText(Html.fromHtml(place.getAddress() + ""));


        }
    };
    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }
}
