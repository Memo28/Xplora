package com.example.memovaradegante.xploraapp.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.adapters.ChatHolder;
import com.example.memovaradegante.xploraapp.models.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    protected DatabaseReference mChatRef;
    private FirebaseAuth mAuth;



    private ImageButton mSendBtn;
    private EditText mMessageEdit;

    private RecyclerView mMessages;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Message,ChatHolder> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mSendBtn = (ImageButton) findViewById(R.id.imageButtonSendMessage);
        mMessageEdit = (EditText) findViewById(R.id.editTextMessageChat);

        mChatRef = FirebaseDatabase.getInstance().getReference().child("messages");

        mSendBtn.setOnClickListener(this);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(false);

        mMessages = (RecyclerView) findViewById(R.id.messagesList);
        mMessages.setHasFixedSize(true);
        mManager.setStackFromEnd(true);
        mMessages.setLayoutManager(mManager);


        attachRecyclerViewAdapter();


    }

    @Override
    public void onClick(View view) {
        String uid = mAuth.getCurrentUser().getEmail();

        Message msgn= new Message(uid,"memovdg@gmail.com",mMessageEdit.getText().toString());
        mChatRef.push().setValue(msgn, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null){
                    Log.e("Error", "Failed to write message", databaseError.toException());

                }
            }
        });

        mMessageEdit.setText("");
    }

    private void attachRecyclerViewAdapter() {
        mAdapter = getAdapter();
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mManager.smoothScrollToPosition(mMessages,null,mAdapter.getItemCount());
            }
        });

        mMessages.setAdapter(mAdapter);
    }


    protected FirebaseRecyclerAdapter<Message, ChatHolder> getAdapter() {
        Query lastFifty = mChatRef.limitToLast(50);
        return new FirebaseRecyclerAdapter<Message, ChatHolder>(
                Message.class,
                R.layout.listview_chat_item,
                ChatHolder.class,
                lastFifty) {
            @Override
            protected void populateViewHolder(ChatHolder viewHolder, Message model, int position) {
                viewHolder.bind(model);
            }
        };
    }
}

