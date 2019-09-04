package com.example.hungrystomach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static java.security.AccessController.getContext;

public class Upload_Activity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btn_choose_image;
    private Button btn_upload_image;
    private TextView tv_show_upload;
    private EditText et_enter_filename;
    private ImageView img_view;
    private ProgressBar progress_bar;
    private Uri image_uri;
    private StorageReference storage_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage_ref = FirebaseStorage.getInstance().getReference();

        btn_choose_image = findViewById(R.id.btn_choose_image);
        btn_upload_image = findViewById(R.id.btn_upload_image);
        tv_show_upload = findViewById(R.id.tv_show_upload);
        et_enter_filename = findViewById(R.id.et_enter_filename);
        img_view = findViewById(R.id.img_view);
        progress_bar = findViewById(R.id.progress_bar);

        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_file_chooser();
            }
        });

        tv_show_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });




    }


    private void open_file_chooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /*
    private void uploading_image(){
        @Override
        public void onClick(View view) {
            if(image_uri!=null) {
                final ProgressDialog dialog = new ProgressDialog(getContext()); //ProgressBar?
                dialog.setTitle("Uploading Image.");
                btn_choose_image.setVisibility(View.GONE);

                StorageReference storage_ref = storage_ref.child(image_uri.getLastPathSegment());

                storage_ref.putFile(image_uri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot task_snapshot) {
                        Uri downloadUrl = task_snapshot.getDownloadUrl();
                        String content = downloadUrl.toString();
                        Log.e(TAG, "Image Uploaded");
                        if (content.length() > 0) {
                            Message msg = new Message();
                            msg.text = content;
                            FirebaeDatabase.getInstance().getReference().child("message/").push.setValue(msg);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Log.e(TAG, "");

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploading " + (int) progress + "%");
                    }
                });
            }
        }
    }
    */




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image_uri = data.getData();

            Picasso.with(this).load(image_uri).into(img_view);
        }
    }



    /*
    //ArrayList<Food> m_food_list;
    //read_image_files();
    //load_image();
    private void read_image_files(){
        for(Food fd : m_food_list){
            Log.d(TAG, "Image filename" + fd.ImageFile);
            loadImage(fd);
        }
    }

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


}
