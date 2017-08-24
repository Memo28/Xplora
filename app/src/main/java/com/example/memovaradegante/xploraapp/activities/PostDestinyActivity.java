package com.example.memovaradegante.xploraapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterComment;
import com.example.memovaradegante.xploraapp.adapters.MyAdapterListPlace;
import com.example.memovaradegante.xploraapp.models.Comment;
import com.example.memovaradegante.xploraapp.models.Places_Model;

import java.util.ArrayList;
import java.util.List;

public class PostDestinyActivity extends AppCompatActivity {

    private List<Comment> comments;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_destiny);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        comments = this.getAllComments();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewComments);
        mlayoutManager = new LinearLayoutManager(this);
        madapter = new MyAdapterComment(R.layout.recycler_view_item_recommendation,comments,
                new MyAdapterComment.OnItemClickListener() {
                    @Override
                    public void onItemClick(Comment comment, int position) {
                     Log.e("OK",comment.getUser());
                    }
                });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(madapter);


    }

    private List<Comment> getAllComments() {
        return new ArrayList<Comment>(){{
            add(new Comment("21","Memo","23423","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9T_rFDwsYhr553CtZ?alt=media&token=2554cc75-1ef4-4f5a-aade-5a38c3ec6fbf","21","2","isadjsadjasdjsadjsadjsakdjdsjkskjsadkjsads"));
            add(new Comment("21","Memo","23423","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9T_rFDwsYhr553CtZ?alt=media&token=2554cc75-1ef4-4f5a-aade-5a38c3ec6fbf","21","2","isadjsadjasdjsadjsadjsakdjdsjkskjsadkjsads"));
            add(new Comment("21","Memo","23423","https://firebasestorage.googleapis.com/v0/b/xplora-15a7b.appspot.com/o/profileImage-Kr9T_rFDwsYhr553CtZ?alt=media&token=2554cc75-1ef4-4f5a-aade-5a38c3ec6fbf","21","2","isadjsadjasdjsadjsadjsakdjdsjkskjsadkjsads"));
        }
        };
    }
}
