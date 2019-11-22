package com.example.hungrystomach;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Status_Update_Activity extends AppCompatActivity {

    RadioGroup radio_group;
    RadioButton radio_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_update);

        radio_group = findViewById(R.id.selectradiogroup);
    }

    public void CheckButton(View v){
        int radioID = radio_group.getCheckedRadioButtonId();
        radio_button = findViewById(radioID);

        Toast.makeText(this, "You Notified Your Buyer " + radio_button.getText(), Toast.LENGTH_SHORT).show();
    }
}
