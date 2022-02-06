package com.example.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//This is the activity for actually viewing the posts


public class ViewPostActivity extends AppCompatActivity {

    Button btn_back;
    ArrayList<Post> postList;
    MyApplication myApplication = (MyApplication) this.getApplication();
    TextView tv_postTitle;
    TextView tv_post;
    ImageView iv_postImage;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        tv_postTitle = findViewById(R.id.tv_postTitle);
        tv_post = findViewById(R.id.tv_post);
        iv_postImage = findViewById(R.id.iv_postImage);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();



        if(b!=null)
        {
            String title = (String) b.get("title");
            String post = (String) b.get("post");
            String url = (String) b.get("url");
            tv_postTitle.setText(title);
            tv_post.setText(post);
            Glide.with(ViewPostActivity.this).load(url).into(iv_postImage); //this line is the problem child of the family

        }



        postList = myApplication.getPostList();
        btn_back = findViewById(R.id.btn_backPost);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPostActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}