package com.example.memovaradegante.xploraapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.models.Comment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Memo Vara De Gante on 23/08/2017.
 */

public class MyAdapterComment extends RecyclerView.Adapter<MyAdapterComment.ViewHolder> {

    private Context context;
    private int layout;
    private List<Comment> comments;
    private OnItemClickListener listener;
    private DatabaseReference database;

    public MyAdapterComment(int layout, List<Comment> comments, OnItemClickListener listener) {
        this.layout = layout;
        this.comments = comments;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        database = FirebaseDatabase.getInstance().getReference("comments");
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(comments.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUser;
        public TextView textVieDescription;
        public TextView textViewLike;
        public TextView textViewDisLike;
        public ImageView imageViewUser;
        public ImageButton imageButtonLike;
        public ImageButton imageButtonDislike;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewUser = (TextView) itemView.findViewById(R.id.text_view_User_Comment);
            textVieDescription = (TextView) itemView.findViewById(R.id.text_view_Information_Comment);
            textViewLike = (TextView) itemView.findViewById(R.id.textViewLikeComment);
            textViewDisLike = (TextView) itemView.findViewById(R.id.textViewDisLikeComment);
            imageViewUser = (ImageView) itemView.findViewById(R.id.imageView_Comment);
            imageButtonLike = (ImageButton) itemView.findViewById(R.id.imageButtonLike);
            imageButtonDislike = (ImageButton) itemView.findViewById(R.id.imageButtonDislike);

        }
        public void bind(final Comment comment, final OnItemClickListener listener){
            textViewUser.setText(comment.getUser());
            textVieDescription.setText(comment.getDescription());
            textViewLike.setText(comment.getLikes());
            textViewDisLike.setText(comment.getDislikes());
            Picasso.with(context).load(comment.getUserImage())
                    .transform(new CropCircleTransformation()).fit().into(imageViewUser);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(comment,getAdapterPosition());
                }
            });

            imageButtonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int like = Integer.parseInt(textViewLike.getText().toString());
                    like = like+1;
                    database.child(comment.getComment_Id()).child("likes").setValue(Integer.toString(like));
                    textViewLike.setText(like+"");
                }
            });

            imageButtonDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int dislike = Integer.parseInt(textViewDisLike.getText().toString());
                    dislike = dislike + 1;
                    database.child(comment.getComment_Id()).child("dislikes").setValue(Integer.toString(dislike));
                    textViewDisLike.setText(dislike+"");
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onItemClick(Comment comment,int position);
    }
}
