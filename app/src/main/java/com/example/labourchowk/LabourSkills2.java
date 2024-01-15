package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LabourSkills2 extends AppCompatActivity {
    EditText Wage;
    EditText age;
    EditText work_experience;
    Button save;
    Spinner work_type;
    TextView job_description;
    ImageView job_image;
    //HashMap<Object, String> value = new HashMap<>();
    String value;

    String selectedWorkType;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_skills2);
        job_description = findViewById(R.id.labour_job_title);
        job_image = findViewById(R.id.labour_type);
        Wage = findViewById(R.id.wage_ask);
        age = findViewById(R.id.age);
        work_experience = findViewById(R.id.work_experience);


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("value")){
            value = (String) intent.getSerializableExtra("value");
        }
        StringBuilder value2 = new StringBuilder();
        char[] arr = value.toCharArray();
        char type = arr[13];
        for (int i = 0; i < 13; i++){
            value2.append(arr[i]);
        }
        StringBuilder value3 = new StringBuilder();
        for (int i=14;i<arr.length;i++){
            value3.append(arr[i]);
        }
        String mobile_number = value2.toString();
        String new_value3 = value3.toString();
        //String new_value = "R.drawable."+value.toLowerCase();
        String resourceName = new_value3.toLowerCase(); // replace with your resource name
        String packageName = getPackageName();

        int resourceId = getResources().getIdentifier(resourceName, "drawable", packageName);

        // Now, resourceId contains the integer id of the drawable
        job_description.setText(new_value3);
        job_image.setImageResource(resourceId);

        work_type = findViewById(R.id.spinner);
        save = findViewById(R.id.save);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.work_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        work_type.setAdapter(adapter);

        work_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item (e.g., save it to a variable or perform an action)
                selectedWorkType = parentView.getItemAtPosition(position).toString();
                //job_description.setText(selectedWorkType);
                // You can do something with the selected work type here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(mobile_number).child("job_preference").setValue(new_value3);
                reference.child(mobile_number).child("age").setValue(age.getText().toString());
                reference.child(mobile_number).child("work_type").setValue(selectedWorkType);
                reference.child(mobile_number).child("work_experience").setValue(work_experience.getText().toString());
                //Toast.makeText(LabourSkills2.this,"Now you can log in with your mobile number.",Toast.LENGTH_SHORT).show();
                reference.child(mobile_number).child("wage").setValue(Wage.getText().toString());
                if (type == 'L'){
                    Intent i = new Intent(LabourSkills2.this,MainActivity.class);
                    startActivity(i);
                    finish();
                } else if (type == 'E') {

                    Intent i = new Intent(LabourSkills2.this,SuccessPage.class);
                    i.putExtra("mob",mobile_number);
                    startActivity(i);
                    finish();
                }


            }
        });
    }
}




