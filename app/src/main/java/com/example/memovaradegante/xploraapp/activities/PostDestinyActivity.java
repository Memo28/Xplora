package com.example.memovaradegante.xploraapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterComment;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterListPlace;
import com.example.memovaradegante.xploraapp.models.Comment;
import com.example.memovaradegante.xploraapp.models.Places_Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.R.attr.delay;

public class PostDestinyActivity extends AppCompatActivity {

    private List<Comment> comments;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutManager;

    private String id_Destitny;
    private String user_actual;
    private String photo_UrlUser;
    private String name_actual_u;


    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceDestinies;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    private EditText editTextComment;
    private ImageButton btnAddComment;
    private ImageView imageViewComment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_destiny);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
        databaseReferenceDestinies = FirebaseDatabase.getInstance().getReference("places");
        progressDialog = new ProgressDialog(this);




        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id_Destitny = extras.getString("Id");
            user_actual = extras.getString("user_actual");
            photo_UrlUser = extras.getString("photo_UrlUser");
            name_actual_u = extras.getString("name_actual_u");
        }else {
            Log.e("Erro","No Info");
        }

        editTextComment = (EditText) findViewById(R.id.editTextAddComment);
        btnAddComment = (ImageButton) findViewById(R.id.imageButtonAddComment);
        imageViewComment = (ImageView) findViewById(R.id.imageViewCommentsV);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextComment.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(),R.string.errorNoComment,Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.setMessage("Agregando Comentario...");
                    progressDialog.show();

                    final String id = databaseReference.push().getKey();

                    String uid = firebaseAuth.getCurrentUser().getUid();

                    String comment_d = editTextComment.getText().toString().trim();
                    Comment comment = new Comment(id_Destitny,name_actual_u,uid,photo_UrlUser,"0","0",comment_d);
                    databaseReference.child(id).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Agregado Correctamente...",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error al agregar...",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }
                    });
                }
            }
        });

        //Buscar la imagen del destino

        comments = this.getAllComments();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewComments);
        mlayoutManager = new LinearLayoutManager(this);
        madapter = new MyAdapterComment(R.layout.recycler_view_item_recommendation,comments,
                new MyAdapterComment.OnItemClickListener() {
                    @Override
                    public void onItemClick(Comment comment, int position) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostDestinyActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.dialog_comment,null);
                        TextView textViewUserDialog = (TextView) mView.findViewById(R.id.textViewUserDialog);
                        TextView textViewDateDialo = (TextView) mView.findViewById(R.id.textViewDateDialog);
                        TextView textView = (TextView) mView.findViewById(R.id.textViewCommentDialog);
                        ImageButton btnContacDialog = (ImageButton) mView.findViewById(R.id.imageButtonContact);

                        btnContacDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(PostDestinyActivity.this);
                                confirmBuilder.setIcon(R.drawable.ic_audiotrack);
                                confirmBuilder.setTitle("Contactar Usuario");
                                confirmBuilder.setMessage("Mensaje");
                                confirmBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                confirmBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog alertDialogConfirm = confirmBuilder.create();
                                alertDialogConfirm.show();

                            }
                        });
                        Log.e("OK",comment.getUser());
                        mBuilder.setView(mView);
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();
                    }
                });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(madapter);

        getImagesDestiny();



    }

    private void getImagesDestiny() {
        databaseReferenceDestinies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String place_id = ds.child("id").getValue().toString();
                        if(place_id.equals(id_Destitny)){
                            Picasso.with(getApplicationContext()).load(ds.child("poster").getValue().toString())
                                    .fit().into(imageViewComment);
                            Log.e("Photo",ds.child("poster").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private List<Comment> getAllComments() {

        final ArrayList<Comment> comments_list = new ArrayList<Comment>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String place_id = ds.child("id").getValue().toString();
                        if(place_id.equals(id_Destitny)){
                            Comment comment = ds.getValue(Comment.class);
                            comments_list.add(comment);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return comments_list;

    }

}
