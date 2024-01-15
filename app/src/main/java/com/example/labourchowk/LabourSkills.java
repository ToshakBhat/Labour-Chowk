package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class LabourSkills extends AppCompatActivity {
    TextView labour_skills_title;
    ImageView Construction_Worker;
    ImageView Brick_kiln_worker;
    ImageView Household_Helper;
    ImageView Security_Guard;
    ImageView Canteen_Worker;
    ImageView Carpenter;
    ImageView Delivery_assistant;
    ImageView Farm_labourer;
    ImageView Handicraft_worker;
    ImageView Gardener;
    HashMap<ImageView, String> array = new HashMap<>();
    String mobile_number;
    String val;
    String val2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_skills);
        labour_skills_title = findViewById(R.id.labour_skill_title);

        Construction_Worker = findViewById(R.id.construction_worker);
        array.put(Construction_Worker,"Construction_Worker"); //An imageview object created to hold imageView objects....
        Brick_kiln_worker = findViewById(R.id.Brick_kiln_worker);
        array.put(Brick_kiln_worker,"Brick_Kiln_Worker");
        Household_Helper = findViewById(R.id.household_helper);
        array.put(Household_Helper,"Household_Helper");
        Security_Guard = findViewById(R.id.Security_Guard);
        array.put(Security_Guard, "Security_Guard");
        Canteen_Worker = findViewById(R.id.canteen_worker);
        array.put(Canteen_Worker, "Canteen_Worker");
        Carpenter = findViewById(R.id.Carpenter_assistant);
        array.put(Carpenter,"Carpenter_Assistant");
        Delivery_assistant = findViewById(R.id.Delivery_assistant);
        array.put(Delivery_assistant,"Delivery_Assistant");
        Farm_labourer = findViewById(R.id.farm_labourer);
        array.put(Farm_labourer,"Farm_Labourer");
        Handicraft_worker = findViewById(R.id.handicraft_worker);
        array.put(Handicraft_worker,"Handicraft_Worker");
        Gardener = findViewById(R.id.Daily_wage_gardener);
        array.put(Gardener,"Daily_Wage_Gardener");

        Intent i = getIntent();
        if (i != null && i.hasExtra("mob")){
            mobile_number = (String) i.getSerializableExtra("mob");
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user data
                    if(Objects.equals(userSnapshot.getKey().toString(), mobile_number)){
                        val = userSnapshot.child("Status").getValue().toString();
                        break;
                    }
                }
                if (val.equals("Employer")){
                    labour_skills_title.setText("Create any one vacancy");
                    for (ImageView i : array.keySet()){
                        i.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //HashMap<ImageView,String> value = new HashMap<>();
                                //value.put(i,array.get(i));
                                //labour_skills_title.setText(array.get(i));

                                //i.setImageResource(R.drawable.labourchowk);
                                Intent intent = new Intent(LabourSkills.this,LabourSkills2.class);
                                intent.putExtra("value", mobile_number+"E"+array.get(i));
                                startActivity(intent);

                            }
                        });
                    }
                }
                else if(val.equals("Labour")){
                    for (ImageView i : array.keySet()){
                        i.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //HashMap<ImageView,String> value = new HashMap<>();
                                //value.put(i,array.get(i));
                                //labour_skills_title.setText(array.get(i));

                                //i.setImageResource(R.drawable.labourchowk);
                                Intent intent = new Intent(LabourSkills.this,LabourSkills2.class);
                                intent.putExtra("value", mobile_number+"L"+array.get(i));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}