package com.example.hungrystomach;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Setting_Activity {
    FirebaseAuth m_auth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    final String email = user.getEmail();
    AuthCredential credential = EmailAuthProvider.getCredential(email, "password1234");

    /*
    user.reauthenticate(credential)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "User Re-authenticated");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "User Email Address Updated.");
                                        Toast.makeText(getActivity(), "The email updated.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        });
                    }
                }
    */

    private void loadUserInfo(){
        FirebaseUser user = m_auth.getCurrentUser();
        String Name = user.getDisplayName();
    }


}