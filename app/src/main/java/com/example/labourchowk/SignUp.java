package com.example.labourchowk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SignUp extends AppCompatActivity {
    ImageView labour;
    ImageView employer;
    ImageView sahayak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        labour = findViewById(R.id.LabourSignUp);
        employer = findViewById(R.id.EmployerSignUp);
        sahayak = findViewById(R.id.SahayakSignUp);

        labour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this,LabourSignUp.class);
                startActivity(i);
                //finish();
            }
        });

        employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, EmployerSignUp.class);
                startActivity(i);
            }
        });

        sahayak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this,SahayakSignUp.class);
                startActivity(i);
            }
        });
    }
}