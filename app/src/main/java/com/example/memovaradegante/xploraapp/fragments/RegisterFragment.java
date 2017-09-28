package com.example.memovaradegante.xploraapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.activities.HomeActivity;
import com.example.memovaradegante.xploraapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPsw;
    private EditText editTextPswConfirm;
    private Button btnRegister;
    private Spinner spinnerCountry;
    private ImageButton imageButtonPhoto;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUser;
    private StorageReference storageReference;


    private static final int GALLERY_INTENT = 1;
    private int STORAGE_PERMISSION_CODE = 23;
    private Uri uriImageProfile;
    private ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        return inflater.inflate(com.example.memovaradegante.xploraapp.R.layout.fragment_register, container, false);

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
        Picasso.with(getActivity()).load(R.drawable.camera_profile).fit().into(imageButtonPhoto);


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

        //Agregamos los listener a los botones
        btnRegister.setOnClickListener(this);
        imageButtonPhoto.setOnClickListener(this);

        //Referenciamos hacia la BD de Firebase
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();


    }


    private void registerUser(){
        final String name = editTextName.getText().toString().trim();
        final String country = spinnerCountry.getSelectedItem().toString();
        final String email = editTextEmail.getText().toString().trim();
        final String psw = editTextPsw.getText().toString().trim();
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
        }if(psw.length() < 6){
            Toast.makeText(getActivity(),"La contraseña debe ser mayor a 6 caracteres",Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Registrando Usuario...");
        progressDialog.show();



        //Autentificacion hacia Firebase por correo y contraseña
        firebaseAuth.createUserWithEmailAndPassword(email,psw)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"Registro exitos",Toast.LENGTH_LONG).show();
                            String uid= firebaseAuth.getCurrentUser().getUid();
                            addUser(uid,name,email,psw,country);
                            progressDialog.dismiss();
                            //Mandar al Usuaeio a la pagina Prinicipal
                            getActivity().finish();
                            startActivity(new Intent(getContext(), HomeActivity.class));
                        }else {
                            if(task.getException().getMessage() == "The email address is already in use by another account."){
                                Toast.makeText(getContext(),"Email ya registrado",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                return;
                            }if(task.getException().getMessage() == "The email address is badly formatted."){
                                Toast.makeText(getContext(),"Por favor introdusca una direccion de correo correcta",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                return;
                            }
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //Agregamos informacion del usuario
    private void addUser(final String uid, final String name,final String email, final String country,final String psw) {
        final String id = databaseUser.push().getKey();
        final String pathImageUser = "profileImage"+id;

        //Caso en el que el usuario haya seleccionado su imagen
        if(uriImageProfile != null){
            StorageReference filepath = storageReference.child(pathImageUser).child(uriImageProfile.getLastPathSegment());
            filepath.putFile(uriImageProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri uriImage = taskSnapshot.getDownloadUrl();
                    User user = new User(uid,name,email,country,psw,uriImage.toString(),"Cuentanos acerca de ti...","¿Que cosas te gusta hacer");
                    databaseUser.child(id).setValue(user);
                }
            });
        }else{
            //Caso en el que usuario no selecciona una image
            //Le pasamos como URL de imagen una cadena vacia debido a que no selecciona nada
            User user = new User(id,name,email,country,psw,"https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/defaultImages%2FprofileDefaultImage.png?alt=media&token=1caa795f-db62-4a5e-8ced-c853745ee8f1","Cuentanos acerca de ti...","¿Que cosas te gusta hacer");
            databaseUser.child(id).setValue(user);
        }
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
                        Toast.makeText(getActivity(), "Por favor concede el permiso", Toast.LENGTH_LONG).show();
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
                uriImageProfile = data.getData();
                Picasso.with(getActivity()).load(uriImageProfile)
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
