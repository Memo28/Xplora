package com.example.memovaradegante.xploraapp.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {


    private String interests;
    private String aboutme;
    private String psw;
    private String id;
    private String pwsConfirm;

    private EditText editTextAboutMe;
    private EditText editTextInterests;
    private EditText editTextPsw;
    private EditText editTextPswConfirm;
    private FloatingActionButton floatingActionButtonSave;

    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        database = FirebaseDatabase.getInstance().getReference("users");
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            aboutme = extras.getString("About_me");
            interests = extras.getString("Interest");
            psw = extras.getString("password");
            id = extras.getString("id");
        }

        editTextAboutMe = (EditText) findViewById(R.id.editTextEditAboutMe);
        editTextInterests = (EditText) findViewById(R.id.editTextEditInterests);
        editTextPsw = (EditText) findViewById(R.id.editTextEditPsw);
        editTextPswConfirm = (EditText) findViewById(R.id.editTextEditConfirmPsw);
        floatingActionButtonSave = (FloatingActionButton) findViewById(R.id.fltActionBtnEditProfile);


        editTextAboutMe.setText(aboutme);
        editTextInterests.setText(interests);
        editTextPsw.setText(psw);
        floatingActionButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInfo()){
                    editInfo();
                    Toast.makeText(getApplicationContext(),"Informacion actualizada de manera correcta",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("Fragment","EditProfileActivity");
                    startActivity(intent);
                }

            }
        });

    }

    private boolean checkInfo() {
        if(TextUtils.isEmpty(editTextAboutMe.getText().toString().trim())){
            Toast.makeText(this,"Cuentanos algo acerca de ti...",Toast.LENGTH_SHORT).show();
            return false;
        }if (TextUtils.isEmpty(editTextInterests.getText().toString().trim()))   {
            Toast.makeText(this,"¿Que te gusta hacer en tu tiempo libre...?",Toast.LENGTH_SHORT).show();
            return false;
        }if (TextUtils.isEmpty(editTextPsw.getText().toString().trim()) || editTextPsw.getText().length() < 6)   {
            Toast.makeText(this,"Por favor ingresa una contraseña correcta",Toast.LENGTH_SHORT).show();
            return false;
        }if(!((editTextPswConfirm.getText().toString().trim()).equals(editTextPsw.getText().toString()))){
            Toast.makeText(this,"Las contraseñas deben de ser iguales",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    public void editInfo() {
        database.child(id).child("infoUser").setValue(editTextAboutMe.getText().toString());
        database.child(id).child("interest").setValue(editTextInterests.getText().toString());
        database.child(id).child("psw").setValue(editTextPsw.getText().toString());
    }
}
