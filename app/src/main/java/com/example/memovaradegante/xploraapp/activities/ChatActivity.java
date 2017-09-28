package com.example.memovaradegante.xploraapp.activities;

import android.text.format.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.models.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextMessage;
    private ImageButton imageButtonSendMessage;
    private ListView mrecyclerView;
    private FirebaseListAdapter<Message> madapter;
    private RecyclerView.LayoutManager mlayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        editTextMessage = (EditText) findViewById(R.id.editTextMessageChat);
        imageButtonSendMessage = (ImageButton) findViewById(R.id.imageButtonSendMessage);
        mrecyclerView = (ListView) findViewById(R.id.recyclerViewMensagges);

        imageButtonSendMessage.setOnClickListener(this);
        displayMessages();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonSendMessage:
                sendMessage();
                return;
            default:
                return;
        }

    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString();
        FirebaseDatabase.getInstance().getReference("messages").push().setValue(
                new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"clausdama@hotmail.com",message));
        editTextMessage.setText("");
    }

    private void displayMessages(){
        madapter = new FirebaseListAdapter<Message>(this,Message.class,R.layout.listview_chat_item,FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView textViewMessage = (TextView) v.findViewById(R.id.editTextMessage);
                TextView textViewDate = (TextView) v.findViewById(R.id.editTextTimeMessage);
                textViewMessage.setText(model.getUser_recibe());
                textViewDate.setText(DateFormat.format("dd-MM-yy (HH:mm:ss)",model.getDate()));

            }
        };
        mrecyclerView.setAdapter(madapter);

    }
}
