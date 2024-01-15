package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LabourPortal2 extends AppCompatActivity {
    ImageView job_image;
    TextView job_description;
    TextView employer_name;
    TextView job_experience;
    TextView job_work_type;
    Button apply;
    String mobs;
    String labour_mobile_number;
    String employer_mobile_number;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_portal2);
        job_image = findViewById(R.id.job_image2);
        job_description = findViewById(R.id.job);
        employer_name = findViewById(R.id.job_employer_name2);
        job_experience = findViewById(R.id.job_experience2);
        job_work_type = findViewById(R.id.job_type2);
        apply = findViewById(R.id.apply);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            mobs = (String) intent.getSerializableExtra("mob");
        }
        char[] arr = mobs.toCharArray();
        StringBuilder mob1 = new StringBuilder();
        StringBuilder mob2 = new StringBuilder();
        for (int i=0;i<13;i++){
            mob1.append(arr[i]);
        }
        for (int i=13;i<arr.length;i++){
            mob2.append(arr[i]);
        }

        labour_mobile_number = mob1.toString();
        employer_mobile_number = mob2.toString();

        job_description.setText(labour_mobile_number+" "+employer_mobile_number);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), employer_mobile_number)){
                        if(userSnapshot.child("job_preference").exists()){
                            job_description.setText(userSnapshot.child("job_preference").getValue().toString());
                            String resourceName = userSnapshot.child("job_preference").getValue().toString().toLowerCase(); // replace with your resource name
                            String packageName = getPackageName();

                            int resourceId = getResources().getIdentifier(resourceName, "drawable", packageName);

                            // Now, resourceId contains the integer id of the drawable
                            job_image.setImageResource(resourceId);
                        }else{
                            job_description.setText("Not Provided");
                        }

                        if (userSnapshot.child("work_experience").exists()){
                            job_experience.setText("Work Experience : "+userSnapshot.child("work_experience").getValue().toString() + " years");
                        }else{
                            job_experience.setText("Not provided");
                        }

                        if (userSnapshot.child("work_type").exists()){
                            job_work_type.setText("Work-Type : "+userSnapshot.child("work_type").getValue().toString());
                        }else{
                            job_work_type.setText("Not provided");
                        }

                        if (userSnapshot.child("user_name").exists()){
                            employer_name.setText("Employer Name : "+userSnapshot.child("user_name").getValue().toString());
                        }else{
                            employer_name.setText("Not provided");
                        }

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LabourPortal2.this,"Applied Successfully",Toast.LENGTH_LONG).show();
                reference.child(employer_mobile_number).child("got_worker").setValue(labour_mobile_number);
                Intent i = new Intent(LabourPortal2.this,LabourPortal.class);
                i.putExtra("mob",labour_mobile_number);
                startActivity(i);
            }
        });


    }
}