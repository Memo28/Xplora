package com.example.memovaradegante.xploraapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.Executor;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACTIVITY_SERVICE;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPsw;
    private EditText editTextPswConfirm;
    private Button btnRegister;
    private Spinner spinnerCountry;
    private ImageButton imageButtonPhoto;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private static final int GALLERY_INTENT = 1;
    private int STORAGE_PERMISSION_CODE = 23;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        return inflater.inflate(R.layout.fragment_register, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextName = (EditText) getView().findViewById(R.id.editTextNameRegister);
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmailRegister);
        editTextPsw = (EditText) getView().findViewById(R.id.editTextPswRegister);
        editTextPswConfirm = (EditText) getView().findViewById(R.id.editTextPswConfirmRegister);
        btnRegister = (Button) getView().findViewById(R.id.buttonRegister);
        spinnerCountry = (Spinner) getView().findViewById(R.id.spinnerCountry);
        imageButtonPhoto = (ImageButton) getView().findViewById(R.id.imageButtonPhotoRegister);
        Picasso.with(getActivity()).load(R.drawable.camera_profile)
                .transform(new CropCircleTransformation()).fit().into(imageButtonPhoto);


        //Obtenemos los paises para el Spinner
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);

        //Metemos los paises dentro del Spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
        spinnerCountry.setSelection(countryAdapter.getPosition("Mexico"));

        btnRegister.setOnClickListener(this);
        imageButtonPhoto.setOnClickListener(this);
    }


    private void registerUser(){
        String name = editTextName.getText().toString().trim();
        String country = spinnerCountry.getSelectedItem().toString();
        String email = editTextEmail.getText().toString().trim();
        String psw = editTextPsw.getText().toString().trim();
        String pswConfirm = editTextPswConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(),"Por favor ingresa un nombre",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Por favor ingresa un correo valido",Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(psw)){
            Toast.makeText(getActivity(),"Por favor ingresa una constraseña",Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(pswConfirm)){
            Toast.makeText(getActivity(),"Por favor ingresa una contraseña",Toast.LENGTH_SHORT).show();
            return;
        }if (!psw.equals(pswConfirm)){
            Toast.makeText(getActivity(),"Las contraseñas no son iguales",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registrando Usuario...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,psw)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"Registro Exitos",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(getContext(), task.getException()+ "",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == btnRegister){
            registerUser();
        }
        if (view == imageButtonPhoto){
            //Verificamos si se tienen permisos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Comprobar si ha aceptado, no ha aceptado o nunca se le ha preguntado
                if (CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) return;
                    startActivityForResult(intent,GALLERY_INTENT);
                } else {
                    //Ha denegado la primera vez el permiso
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    } else {
                        Toast.makeText(getActivity(), "Por favor concede el permiso", Toast.LENGTH_LONG);
                        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + getActivity().getPackageName()));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT ) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Picasso.with(getActivity()).load(uri)
                        .transform(new CropCircleTransformation()).fit().into(imageButtonPhoto);
            }
        }
    }

        private  boolean CheckPermission(String permision){
            int result = getActivity().checkCallingOrSelfPermission(permision);
            return result == PackageManager.PERMISSION_GRANTED;
        }

        private void olderVersion(){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
            if (CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
                startActivityForResult(intent,GALLERY_INTENT);
            }else {
                Toast.makeText(getActivity(), "Permiso no concedido", Toast.LENGTH_LONG).show();
            }
        }
    }
