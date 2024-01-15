package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EmployerPortal extends AppCompatActivity {
    String mobile_number;
    ImageView create_vacancies;
    ImageView check_vacancies;
    TextView name;
    TextView location;
    String val;
    String val2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference_employer = database.getReference();
    DataSnapshot Snapshot_object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_portal);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            mobile_number = intent.getStringExtra("mob");
        }
        name = findViewById(R.id.textView_name);
        location = findViewById(R.id.textView_location);
        create_vacancies = findViewById(R.id.create_vacancies);
        check_vacancies = findViewById(R.id.check_vacancies);

        reference_employer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user data
                    if(Objects.equals(userSnapshot.getKey().toString(), mobile_number)){
                        val = userSnapshot.child("user_name").getValue().toString();
                        val2 = userSnapshot.child("Location").getValue().toString();
                        break;
                    }
                    //String name = userSnapshot.child("name").getValue(String.class);
                    //String email = userSnapshot.child("email").getValue(String.class);

                    // Log user data (you can use this data as needed in your app)
                    //Log.d(TAG, "Name: " + name + ", Email: " + email);
                }
                name.setText("Namaste "+val + " !");
                location.setText(val2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //name.setText(mobile_number);
        //reference_employer.child(mobile_number).child("user_name")
        create_vacancies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployerPortal.this,LabourSkills.class);
                i.putExtra("mob",mobile_number);
                startActivity(i);
            }
        });

        check_vacancies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployerPortal.this,checkVacanciesEmployer.class);
                i.putExtra("mob",mobile_number);
                startActivity(i);
            }
        });

    }
}