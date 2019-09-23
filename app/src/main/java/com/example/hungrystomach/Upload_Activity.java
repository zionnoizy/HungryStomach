package com.example.hungrystomach;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private StorageReference storage_ref = FirebaseStorage.getInstance().getReference();
    private DatabaseReference database_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_main);

        m_btn_choose = findViewById(R.id.btn_choose);
        btn_upload = findViewById(R.id.btn_upload);

        et_fname = findViewById(R.id.et_fname);
        et_fdesc = findViewById(R.id.et_fdesc);
        et_fprice = findViewById(R.id.et_fprice);

        progress_bar = findViewById(R.id.progress_bar);
        img_view = findViewById(R.id.img_view);

        storage_ref = FirebaseStorage.getInstance().getReference("images");
        database_ref = FirebaseDatabase.getInstance().getReference("images");


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


    private String get_extension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }



    private void uploading_image(){
        if(image_uri!=null) {
            m_btn_choose.setVisibility(View.GONE);
            progress_bar.setVisibility(View.VISIBLE);
            progress_bar.setIndeterminate(true);

            storage_ref.putFile(image_uri) //getDownloadUrl
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progress_bar.setProgress(0);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress_bar.setVisibility(View.VISIBLE);
                                    progress_bar.setIndeterminate(true);
                                    progress_bar.setProgress(0);
                                }

                            }, 500);
                            Toast.makeText(Upload_Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                            Food uploadFI = new Food(et_fname.getText().toString(),et_fdesc.getText().toString(),et_fprice.getText().toString());

                            String upload_id = database_ref.push().getKey();
                            database_ref.child(upload_id).setValue(uploadFI);

                            progress_bar.setVisibility(View.INVISIBLE);

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
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progress_bar.setProgress((int) progress);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image_uri = data.getData();
            Picasso.get().load(image_uri).into(img_view);
            /*
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
            })

            */

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
