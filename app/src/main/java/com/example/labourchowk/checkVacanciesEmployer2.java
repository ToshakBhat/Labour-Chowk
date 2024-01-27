package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class checkVacanciesEmployer2 extends AppCompatActivity {
    ImageView job_image;
    TextView job_description;
    TextView applier_name;
    TextView job_experience;
    TextView job_work_type;
    Button contact;
    String applier_mobile_number;
    String val = "demo";
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_vacancies_employer2);

        job_image = findViewById(R.id.job_image3);
        job_description = findViewById(R.id.applier_job);
        applier_name = findViewById(R.id.job_applier_name);
        job_experience = findViewById(R.id.applier_job_experience);
        job_work_type = findViewById(R.id.applier_job_type);
        contact = findViewById(R.id.contact);
        status = findViewById(R.id.status);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            applier_mobile_number = (String) intent.getSerializableExtra("mob");
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), applier_mobile_number)) {
                        if (userSnapshot.child("job_preference").exists()) {
                            job_description.setText(userSnapshot.child("job_preference").getValue().toString());
                            String resourceName = userSnapshot.child("job_preference").getValue().toString().toLowerCase(); // replace with your resource name
                            String packageName = getPackageName();

                            int resourceId = getResources().getIdentifier(resourceName, "drawable", packageName);

                            // Now, resourceId contains the integer id of the drawable
                            job_image.setImageResource(resourceId);
                        } else {
                            job_description.setText("Not Provided");
                        }

                        if (userSnapshot.child("work_experience").exists()) {
                            job_experience.setText("Work Experience : " + userSnapshot.child("work_experience").getValue().toString() + " years");
                        } else {
                            job_experience.setText("Not provided");
                        }

                        if (userSnapshot.child("work_type").exists()) {
                            job_work_type.setText("Work-Type : " + userSnapshot.child("work_type").getValue().toString());
                        } else {
                            job_work_type.setText("Not provided");
                        }

                        if (userSnapshot.child("user_name").exists()) {
                            applier_name.setText("Employer Name : " + userSnapshot.child("user_name").getValue().toString());
                        } else {
                            applier_name.setText("Not provided");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(checkVacanciesEmployer2.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED){
                    //When permission is granted
                    sendMessage();
                }else{
                    ActivityCompat.requestPermissions(checkVacanciesEmployer2.this,new String[]{Manifest.permission.SEND_SMS},
                            100);
                }


            }
        });
    }

    private void sendMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(applier_mobile_number,null,"Yayy!! You got work on Labour Chowk. Contact this number to enquire.",null,null);
        status.setVisibility(View.VISIBLE);
        Toast toast = Toast.makeText(this,"Message Sent Successfully",Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length>0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
            sendMessage();

        }else{
            Toast.makeText(getApplicationContext(),"Permisson Denied!",Toast.LENGTH_SHORT).show();
        }
    }
}