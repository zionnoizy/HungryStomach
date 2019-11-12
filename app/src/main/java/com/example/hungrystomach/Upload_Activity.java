package com.example.hungrystomach;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//import com.example.hungrystomach.Adapter.DetailAdapter;
import com.example.hungrystomach.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


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

    //time
    private SimpleDateFormat mSimpleDataFormat;
    private Calendar mCalender;
    private Button btnDateTime;

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

        //time
        btnDateTime = findViewById(R.id.choose_time);
        btnDateTime.setOnClickListener(textListener);
        mSimpleDataFormat = new SimpleDateFormat("MM/dd/yyy hh:mm a", Locale.getDefault());

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
        if(image_name.length()==0){
            et_fname.setError("Food name cannot be empty");
            et_fname.requestFocus();
        }
        else if (!image_name.matches("[a-zA-Z0-9()=`!@%^&*+\"\' ]*")){
            et_fname.setError("food name cannot contain symbol(s).");
            et_fname.requestFocus();
        } else if (img_view.getDrawable() == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Activity.this);
            builder.setTitle("Upload Your Image")
                    .setMessage("Food Image should not be empty.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
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
                                        String uuid = m_auth.getCurrentUser().getUid();
                                        String uploader = m_auth.getCurrentUser().getDisplayName();
                                        String DateTime = mSimpleDataFormat.format(mCalender.getTime());

                                        String key = database_ref.push().getKey();
                                        Food fd = new Food(et_fname.getText().toString().trim(),
                                                et_fdesc.getText().toString().trim(),
                                                et_fprice.getText().toString().trim(),
                                                url, DateTime, uuid, key, uploader);
                                        database_ref.child(key).setValue(fd);

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

    }

    private void redirect_home() {
    Intent i = new Intent(this,Home_Activity.class);
    startActivity(i);
    }
    /////////////////////////////////////////////////////////////////////////////
    private final View.OnClickListener textListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            mCalender = Calendar.getInstance();
            new DatePickerDialog(Upload_Activity.this, mDataDataSet, mCalender.get(Calendar.YEAR),
                    mCalender.get(Calendar.MONTH), mCalender.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private final DatePickerDialog.OnDateSetListener mDataDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            mCalender.set(Calendar.YEAR, y);
            mCalender.set(Calendar.MONTH, m);
            mCalender.set(Calendar.DAY_OF_MONTH, d);
            new TimePickerDialog(Upload_Activity.this, mTimeDataSet, mCalender.get(Calendar.HOUR_OF_DAY), mCalender.get(Calendar.MINUTE), false).show();

        }
    };

    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int h, int m) {
            mCalender.set(Calendar.HOUR_OF_DAY, h);
            mCalender.set(Calendar.MINUTE, m);
        }
    };
}
