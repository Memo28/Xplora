package com.example.memovaradegante.xploraapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firebaseAuth = firebaseAuth.getInstance();
        //Verificar si existe alguna sesion activa
        if(firebaseAuth.getCurrentUser() == null){
            //Si no existe alguna sesion activa se manda al login
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Toast.makeText(this,"Bienvenido" + firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
    }
}
