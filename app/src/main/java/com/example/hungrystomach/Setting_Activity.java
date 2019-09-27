package com.example.hungrystomach;

import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Setting_Activity extends AppCompatActivity{

    private Toolbar tool_bar; //toolbar?
    FirebaseAuth m_auth;
    EditText et_email, et_password;
    String email = et_email.getText().toString().trim();

    tool_bar = findViewById(R.id.tool_bar);
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    AuthCredential credential = EmailAuthProvider.getCredential(email, "password1234");

    OnCreate(){
        update_email();
        update_password();
        upadte_username();
        delete_user();
    }
    @Override
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    private void update_email(){
        user.updateEmail(newEmailText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Setting_Activity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_SHORT).show();
                            m_auth.signOut();
                        } else {
                            Toast.makeText(Setting_Activity.this, "Failed to update email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}