package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class checkVacanciesEmployer extends AppCompatActivity {
    String mobile_number;
    TextView title;
    TextView wage_ask;
    TextView vacancy_title;
    ImageView job_image;
    TextView vacancy_experience;
    TextView employment_status;
    TextView vacancy_work_type;
    String job_preference;
    String age;
    String wage2;
    String experience;
    String work_type;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_vacancies_employer);
        //title = findViewById(R.id.title_vacancy_created);
        vacancy_title = findViewById(R.id.vacany_title);
        vacancy_experience = findViewById(R.id.vacancy_expreience);
        vacancy_work_type = findViewById(R.id.vacancy_work_type);
        job_image = findViewById(R.id.job_image);
        wage_ask = findViewById(R.id.wage);
        employment_status = findViewById(R.id.employment_status);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            mobile_number = intent.getStringExtra("mob");
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user data
                    if(Objects.equals(userSnapshot.getKey().toString(), mobile_number)){
                        if (userSnapshot.child("job_preference").exists()){
                            job_preference = Objects.requireNonNull(userSnapshot.child("job_preference").getValue()).toString();
                        }else{
                            job_preference = "Not provided";
                        }
                        if(userSnapshot.child("age").exists()){
                            age = Objects.requireNonNull(userSnapshot.child("age").getValue()).toString();
                        }
                        else{
                            age = "Not provided";
                        }
                        if (userSnapshot.child("work_type").exists()){
                            work_type = Objects.requireNonNull(userSnapshot.child("work_type").getValue()).toString();
                        }else{
                            work_type = "Not provided";
                        }
                        if (userSnapshot.child("work_experience").exists()){
                            experience = Objects.requireNonNull(userSnapshot.child("work_experience").getValue()).toString();
                        }else{
                            experience = "Not-provided";
                        }
                        if (userSnapshot.child("wage").exists()){
                            wage2 = Objects.requireNonNull(userSnapshot.child("wage").getValue()).toString();
                        }else{
                            wage2 = "Not provided";
                        }
                        break;
                    }
                }

                vacancy_title.setText(job_preference);
                vacancy_experience.setText("Experience : "+experience + " years");
                vacancy_work_type.setText("Work-Type : "+work_type);
                wage_ask.setText("Wage(one day) : "+wage2 +" Rs");
                employment_status.setText("Not yet assigned work");

                String resourceName = job_preference.toLowerCase(); // replace with your resource name
                String packageName = getPackageName();

                int resourceId = getResources().getIdentifier(resourceName, "drawable", packageName);

                // Now, resourceId contains the integer id of the drawable
                job_image.setImageResource(resourceId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(checkVacanciesEmployer.this,"Please create a vacancy first",Toast.LENGTH_LONG).show();
                Intent i = new Intent(checkVacanciesEmployer.this,EmployerPortal.class);
                i.putExtra("mob",mobile_number);
                startActivity(i);
                finish();
            }
        });
    }

}