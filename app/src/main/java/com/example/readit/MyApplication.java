package com.example.readit;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;


//This class is used as a global variable so that each activity can access all of the posts, eventually this will be replaced with firebase i think
public class MyApplication extends Application {

    public static ArrayList<Post> postList = new ArrayList<>();
    private static Context sContext;

    public MyApplication(){
    fillPostList();
    }


    //I just put some example posts in here so i could test it and used this method to do so, i'm only dealing with tips for now bcz questions are harder.
    private void fillPostList() {

        Post p0 = new Post("Super epic math tip", "Hey guys here is my tip, get gud at math kid or uninstall idk what else to tell ya.", "https://www.pngkit.com/png/detail/403-4037364_6848425-integral-symbol.png", "course_0", "id_0", false);
        Post p1 = new Post("How to get the same grade on all of Mr. Hillman's assignments", "Step 1: Do nothing", "https://www.adazing.com/wp-content/uploads/2019/02/open-book-clipart-03.png", "course_1", "id_1", false);
        Post p2 = new Post("How do I not fall asleep in Mr. Pring's class?", "I have no idea I just made this post to get thanks.", "https://media.istockphoto.com/vectors/courthouse-icon-flat-vector-template-design-trendy-vector-id1222068323?k=20&m=1222068323&s=612x612&w=0&h=XcravWfpjswLGbJr4lRuvvFdp1IXHwvw8p-3dGXKh8Q=", "course_2", "id_2", true);
        Post p3 = new Post("Quizlet for new Spanish Verbs", "Hey, here is a quizlet for the verbs we just learned", "https://cdn.britannica.com/36/4336-004-6BD81071/Flag-Spain.jpg", "course_3", "id_3", false);
        Post p4 = new Post("Physics equations to know for the test", "Do you think I know physics lol", "https://media.istockphoto.com/vectors/physics-illustration-vector-id615915320?s=612x612", "course_4", "id_4", false);
        Post p5 = new Post("Bio tip: I ran out of creativity", "bio post", "https://process.filepicker.io/APHE465sSbqvbOIStdwTyz/rotate=deg:exif/resize=fit:crop,height:283,width:472/output=quality:80,compress:true,strip:true,format:jpg/cache=expiry:max/https://cdn.filestackcontent.com/IUWL051KTdSg5UDGaV1h", "course_5", "id_5", false);
        Post p6 = new Post("Stat tip", "Mr. Morgan here", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Standard_Normal_Distribution.png/1200px-Standard_Normal_Distribution.png", "course_6", "id_6", false);
        Post p7 = new Post("Chem tip", "chemistry", "https://www.thoughtco.com/thmb/EpsKarnLz-v0VXY0KKnS_fTX5-U=/2121x1414/filters:fill(auto,1)/GettyImages-545286316-433dd345105e4c6ebe4cdd8d2317fdaa.jpg", "course_7", "id_7", false);

        postList.addAll(Arrays.asList(new Post []{p0, p1, p2, p3, p4, p5, p6, p7}));
    }

    public static ArrayList<Post> getPostList() {
        return postList;
    }

    public static void setPostList(ArrayList<Post> postList) {
        MyApplication.postList = postList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext=   getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }
}
