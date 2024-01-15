package com.example.labourchowk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessPage extends AppCompatActivity {

    Button done;
    String mobile_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);

        done = findViewById(R.id.done);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            mobile_number = (String) intent.getSerializableExtra("mob");
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuccessPage.this,EmployerPortal.class);
                i.putExtra("mob",mobile_number);
                startActivity(i);
                finish();
            }
        });
    }
}