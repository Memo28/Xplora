package com.example.memovaradegante.xploraapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterComment;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterListPlace;
import com.example.memovaradegante.xploraapp.models.Comment;
import com.example.memovaradegante.xploraapp.models.Places_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostDestinyActivity extends AppCompatActivity {

    private List<Comment> comments;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutManager;

    private String id_Destitny;
    private String user_actual;
    private String photo_UrlUser;


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    private EditText editTextComment;
    private ImageButton btnAddComment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_destiny);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");



        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id_Destitny = extras.getString("Id");
            user_actual = extras.getString("user_actual");
            photo_UrlUser = extras.getString("photo_UrlUser");
        }else {
            Log.e("Erro","No Info");
        }

        editTextComment = (EditText) findViewById(R.id.editTextAddComment);
        btnAddComment = (ImageButton) findViewById(R.id.imageButtonAddComment);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextComment.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(),R.string.errorNoComment,Toast.LENGTH_SHORT).show();
                }else{
                    final String id = databaseReference.push().getKey();

                    String uid = firebaseAuth.getCurrentUser().getUid();
                    String user = firebaseAuth.getCurrentUser().getEmail();
                    String comment_d = editTextComment.getText().toString().trim();

                    Comment comment = new Comment(id_Destitny,user,uid,photo_UrlUser,"0","0",comment_d);
                    databaseReference.child(id).setValue(comment);
                    Log.e("NO Vacio","OK");
                }
            }
        });




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


    }

    private List<Comment> getAllComments() {

        /*final ArrayList<Comment> comments_list = new ArrayList<Comment>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String comment = ds.child("comment").getValue().toString();
                        if (comment.contains(id_Destitny)){
                            Comment pl = ds.getValue(Comment.class);
                            comments_list.add(pl);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return comments_list;*/
        return new ArrayList<Comment>(){{
            add(new Comment("21","Memo","23423","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9T_rFDwsYhr553CtZ?alt=media&token=2554cc75-1ef4-4f5a-aade-5a38c3ec6fbf","21","2","isadjsadjasdjsadjsadjsakdjdsjkskjsadkjsads"));
            add(new Comment("21","Memo","23423","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9T_rFDwsYhr553CtZ?alt=media&token=2554cc75-1ef4-4f5a-aade-5a38c3ec6fbf","21","2","isadjsadjasdjsadjsadjsakdjdsjkskjsadkjsads"));
            add(new Comment("21","Memo","23423","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9T_rFDwsYhr553CtZ?alt=media&token=2554cc75-1ef4-4f5a-aade-5a38c3ec6fbf","21","2","isadjsadjasdjsadjsadjsakdjdsjkskjsadkjsads"));
        }
        };

    }

}
