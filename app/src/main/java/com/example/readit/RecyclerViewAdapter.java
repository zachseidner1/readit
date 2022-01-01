package com.example.readit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//This class allows for the Post objects to be put into the RecyclerView so that it looks like a feed.


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<Post> postList;
    Context context;

    public RecyclerViewAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feeditem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_postTitle.setText(postList.get(position).getTitle());
        Glide.with(this.context).load(postList.get(position).getImageURL()).into(holder.iv_postImage);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra("title", postList.get(position).getTitle());
                intent.putExtra("post", postList.get(position).getPost());
                intent.putExtra("url", postList.get(position).getImageURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    //This class defines the things that are variables in each post for the feed, which is only the image and title.
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_postImage;
        TextView tv_postTitle;
        RelativeLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_postImage = itemView.findViewById(R.id.iv_feedPostImage);
            tv_postTitle = itemView.findViewById(R.id.tv_feedPostTitle);
            parentLayout = itemView.findViewById(R.id.onePostLayout);
        }
    }
}
