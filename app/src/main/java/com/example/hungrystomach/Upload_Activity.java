package com.example.hungrystomach;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;



public class Upload_Activity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button m_btn_choose;
    private Button btn_upload;

    private EditText et_fname;
    private EditText et_fdesc;
    private EditText et_fprice;

    private ProgressBar progress_bar;
    private ImageView img_view;

    private Uri image_uri;

    private StorageReference storage_ref ;
    private DatabaseReference database_ref;
    private FirebaseAuth m_auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_main);

        m_btn_choose = findViewById(R.id.btn_choose);
        btn_upload = findViewById(R.id.btn_upload);

        et_fname = findViewById(R.id.et_fname);
        et_fdesc = findViewById(R.id.et_fdesc);
        et_fprice = findViewById(R.id.et_fprice);
        img_view = findViewById(R.id.img_view);

        progress_bar = findViewById(R.id.progress_bar);


        storage_ref = FirebaseStorage.getInstance().getReference("all_uploaded_image");
        m_btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_file_chooser();
            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploading_image();
            }
        });
    }


    private void open_file_chooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&& resultCode==RESULT_OK
           && data != null && data.getData() !=null){
            image_uri = data.getData();
            Picasso.get().load(image_uri).into(img_view);
        }
    }


    private String get_extension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }



    private void uploading_image(){
        final String usr_uid = m_auth.getCurrentUser().getUid();

        if(image_uri!=null) {
            String image_name = et_fname.getText().toString().trim();
            StorageReference sf = storage_ref.child(image_name);
            //to storage
            sf.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String tmp_name = et_fname.getText().toString().trim();
                            String tmp_desc = et_fdesc.getText().toString().trim();
                            String tmp_price = et_fprice.getText().toString().trim();
                            String tmp_image_uri = image_uri.toString().trim();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("all_uploaded_image");
                            DatabaseReference food_name = ref.child(tmp_name);
                            //to database
                            food_name.child("name").setValue(tmp_name);
                            food_name.child("description").setValue(tmp_desc);
                            food_name.child("price").setValue(tmp_price);
                            food_name.child("image_uri").setValue(tmp_image_uri);
                            food_name.child("uploader_uid").setValue(m_auth.getCurrentUser().getUid());

                            //to model
                            Food fd = new Food(tmp_name, tmp_desc, tmp_price, tmp_image_uri);
                            /*
                            //uploadID
                            String uploadId = food_name.push().getKey();
                            food_name.child(uploadId).setValue(fd);
                            */
                            Toast.makeText(Upload_Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                         public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Upload_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(Upload_Activity.this, "Image is Uploading", Toast.LENGTH_SHORT).show();
                            }
                    })
                    ;

        }else
            Toast.makeText(Upload_Activity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        redirect_home();

    }

    private void redirect_home() {
    Intent i = new Intent(this,Home_Activity.class);
    startActivity(i);
    }

}
