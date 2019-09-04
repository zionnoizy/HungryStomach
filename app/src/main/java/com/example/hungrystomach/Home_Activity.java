package com.example.hungrystomach;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btn_choose_image;
    private Button btn_upload_image;
    private TextView tv_show_upload;
    private EditText et_enter_filename;
    private ImageView img_view;
    private ProgressBar progress_bar;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_choose_image = findViewById(R.id.btn_choose_image);
        btn_upload_image = findViewById(R.id.btn_upload_image);
        tv_show_upload = findViewById(R.id.tv_show_upload);
        
    //ArrayList<Food> m_food_list;
    //read_image_files();
    //load_image();


}
    /*
    private void read_image_files(){
        for(Food fd : m_food_list){
            Log.d(TAG, "Image filename" + fd.ImageFile);
            loadImage(fd);
        }
    }
    */

    /*
    private void load_image(Food fd){
    if(fd.imageFile.isEmty()) return;

    String imageFilename = fd.image_file;
    StorageReference ref = m_storage.getRefernceFromUrl("gs://fir").child(ImageFilename);

        OnSuccessListener<byte[]> successListener = new OnSuccessListener<byte[]>(){
            @Override
            public void onSuccess(byte[] bytes){
                fd.imageBitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
            }
        };
    }
    */
