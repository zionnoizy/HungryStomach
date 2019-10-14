package com.example.hungrystomach;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Upload_Activity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button m_btn_choose, btn_upload, btn_return;

    private EditText et_fname;
    private EditText et_fdesc;
    private EditText et_fprice;

    private ProgressBar progress_bar;
    private ImageView img_view;

    private Uri image_uri;
    private StorageReference storage_ref ;
    StorageReference sf;
    private DatabaseReference database_ref;

    private ArrayList<Food> entries;

    private FirebaseAuth m_auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_);

        m_btn_choose = findViewById(R.id.btn_choose);
        btn_upload = findViewById(R.id.btn_upload);
        btn_return = findViewById(R.id.btn_return);

        et_fname = findViewById(R.id.et_fname);
        et_fdesc = findViewById(R.id.et_fdesc);
        et_fprice = findViewById(R.id.et_fprice);
        img_view = findViewById(R.id.img_view);
        progress_bar = findViewById(R.id.progress_bar);

        database_ref = FirebaseDatabase.getInstance().getReference().child("all_uploaded_image");
        storage_ref = FirebaseStorage.getInstance().getReference().child("all_uploaded_image");



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

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_home();
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
        String image_name = et_fname.getText().toString().trim();
        sf = storage_ref.child(image_name + "." + get_extension(image_uri));
        if (image_uri != null) {
            sf.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Food fd = new Food(et_fname.getText().toString().trim(),
                                            et_fdesc.getText().toString().trim(),
                                            et_fprice.getText().toString().trim(),
                                            url);
                                    String uploadId = database_ref.push().getKey();
                                    database_ref.child(uploadId).setValue(fd);
                                    Toast.makeText(Upload_Activity.this, "Upload successfully", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Upload_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
        redirect_home();
        finish();

    }

    private void redirect_home() {
    Intent i = new Intent(this,Home_Activity.class);
    startActivity(i);
    }

}
