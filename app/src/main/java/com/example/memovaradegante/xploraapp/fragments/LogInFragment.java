package com.example.memovaradegante.xploraapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LogInFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPsw;
    private Button btnLogin;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        return inflater.inflate(R.layout.fragment_log_in, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (firebaseAuth.getCurrentUser() != null){
            //Usuario ha iniciado sesion previamente
            getActivity().finish();
            startActivity(new Intent(getContext(), HomeActivity.class));
        }
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPsw = (EditText) getView().findViewById(R.id.editTextPsw);
        btnLogin = (Button) getView().findViewById(R.id.buttonLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String psw = editTextPsw.getText().toString();
                if (checkInfo(email,psw)){
                    LogIn(email,psw);
                }
            }
        });
    }

    public boolean checkInfo(String email,String psw){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getContext(),"Ingrese un email valido",Toast.LENGTH_LONG).show();
            return false;
        } if(TextUtils.isEmpty(psw) || psw.length() < 6){
            Toast.makeText(getContext(),"Constraseña invalida",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void LogIn(String email,String psw){
        progressDialog.setMessage("Iniciando Sesión");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,psw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Inicio de Session exitoso
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), HomeActivity.class));
                        }else {
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext(),"Por favor intente de nuevo",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

    }
}
