package com.example.memovaradegante.xploraapp.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Memo Vara De Gante on 01/10/2017.
 */

public class ChatHolder extends RecyclerView.ViewHolder {


    private TextView mNameField;
    private TextView mTextField;
    private FrameLayout mLeftArrow;
    private FrameLayout mRightArrow;
    private RelativeLayout mMessageContainer;
    private LinearLayout mMessage;

    private final int mGreen300;
    private final int mGray300;



    public ChatHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.name_text);
        mTextField = (TextView) itemView.findViewById(R.id.message_text);
        mMessageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
        mMessage = (LinearLayout) itemView.findViewById(R.id.message);

        mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.material_green_300);
        mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);


    }

    public void bind(Message message){
        setName(message.getUser_send());
        setText(message.getMessage());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setIsSender(currentUser != null && message.getUser_send().equals(currentUser.getEmail()));

    }

    private void setName(String name){
        mNameField.setText(name);
    }
    private void setText(String text){
        mTextField.setText(text);
    }

    private void setIsSender(boolean isSender){
        final int color;
        if(isSender){
            Log.e("Ok","Verde");
            color = mGray300;
            mMessageContainer.setGravity(Gravity.END);
        }else{
            Log.e("Ok","Gray");
            color = mGray300;
            mMessageContainer.setGravity(Gravity.START);
        }

        ((GradientDrawable) mMessage.getBackground()).setColor(color);
    }



}
