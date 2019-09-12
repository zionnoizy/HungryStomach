package com.example.hungrystomach;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.example.hungrystomach.model.food_information;
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

import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class Upload_Activity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btn_choose_image;
    private Button btn_upload_image;

    private EditText et_foodname;
    private EditText et_description;
    private EditText et_price;


    private ProgressBar progress_bar;
    private ImageView img_view;

    private Uri image_uri;
    private StorageReference storage_ref = FirebaseStorage.getInstance().getReference();
    private DatabaseReference database_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_choose_image = findViewById(R.id.btn_choose_image);
        btn_upload_image = findViewById(R.id.btn_upload_image);

        et_foodname = findViewById(R.id.et_foodname);
        et_description = findViewById(R.id.et_description);
        et_price = findViewById(R.id.et_price);

        progress_bar = findViewById(R.id.progress_bar);
        img_view = findViewById(R.id.img_view);

        storage_ref = FirebaseStorage.getInstance().getReference("images");
        database_ref = FirebaseDatabase.getInstance().getReference("images");


        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_file_chooser();
            }
        });

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
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
            btn_choose_image.setVisibility(View.GONE);
            progress_bar.setVisibility(View.VISIBLE);
            progress_bar.setIndeterminate(true);





            storage_ref.getDownloadUrl() //getDownloadUrl
                    .addOnSuccessListener(this, new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
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


                            //final Uri downloadUri = uri;
                            //String TempImageName = et_foodname.getText().toString().trim();

                            food_information uploadFI = new food_information();
                            et_description.getText().toString();
                            et_foodname.getText().toString();
                            Integer.parseInt(et_price.getText().toString());

                            String upload_id = database_ref.push().getKey();
                            database_ref.push().setValue(upload_id);

                            progress_bar.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                         public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Upload_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    /*
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progress_bar.setProgress((int) progress);

                        }
                    })
                    */
                    ;

        }else
            Toast.makeText(Upload_Activity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

    }

    private void go_home_act() {
    Intent intent = new Intent(this,Home_Activity.class);
    startActivity(intent);
    }




    /*
    private void upload_limited(){
        Toast.makeText(Upload_Activity.this, "Your Upload Limited Is 5", Toast.LENGTH_LONG).show();
        int total = data.getClipData().getItemCount(); //
        int curr_counter = 0;
        while (curr_counter < total){
            image_uri = data.getClipData().getItemAt(curr_counter).getUri(); //data
            FileList.add(image_uri); //FileList
            curr_counter = curr_counter +1;
        }
    }
    */


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
