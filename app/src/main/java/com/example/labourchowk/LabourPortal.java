package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabourPortal extends AppCompatActivity {
    TextView name;
    TextView location;
    TextView real_location;
    HashMap<String, Object> dataMap = new HashMap<>();
    String mobile_number;
    String my_name;
    String my_location;
    String my_job_preference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference reference = database.getReference().child("Labour");
    DatabaseReference reference3 = database.getReference();
    List<String> mobileNumbersListEmployer = new ArrayList<>();
    List<DataSnapshot> mobileNumbersListSnapshot = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_portal);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);

        //DataSnapshot mobile_object; //Its the json array we needed having key as mobile number and value as the data in the database.
        List<DataSnapshot> mobileNumbersListSnapshot = new ArrayList<>();
        //Retrieve the intent extras from Main Activity
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            mobile_number = (String) intent.getSerializableExtra("mob");
        }

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user data
                    if(Objects.equals(userSnapshot.getKey().toString(), mobile_number)){
                        if(userSnapshot.child("job_preference").exists()){
                            my_job_preference = userSnapshot.child("job_preference").getValue().toString();
                        }else{
                            my_job_preference = "None";
                        }
                        if(userSnapshot.child("Location").exists()){
                            my_location = userSnapshot.child("Location").getValue().toString();
                        }else{
                            my_location = "None";
                        }
                        my_name = userSnapshot.child("user_name").getValue().toString();
                        name.setText("Namaste "+ my_name);
                        location.setText(my_location);
                    }
                    if (Objects.equals(userSnapshot.child("Status").getValue(), "Employer")){
                        mobileNumbersListEmployer.add(userSnapshot.getKey());
                        mobileNumbersListSnapshot.add(userSnapshot);
                    }
                }
                for (DataSnapshot Employer_snapshot : mobileNumbersListSnapshot) {
                    if(Employer_snapshot.child("Location").getValue().toString().toLowerCase().equals(my_location.toLowerCase())){
                        createLinearLayout(Employer_snapshot,"ground");
                    }else{
                        createLinearLayout(Employer_snapshot,"ground2");
                    }

                    // Create a Linearlayout dynamically for each mobile number

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //name.setText(mobileNumbersListSnapshot.toString());
        //name.setText(dataMap.get("user_name").toString());
        //location.setText(dataMap.get("Location").toString());

        //int index = mobileNumbersList.indexOf(mobile_number);
        //DataSnapshot MobileSnapshot = mobileNumbersListSnapshot.get(index);
        //name.setText(MobileSnapshot.child("user_name").getValue().toString());
        //location.setText(MobileSnapshot.child("Location").getValue().toString());

    }

    private void createLinearLayout(DataSnapshot mobileNumberObj,String name_of_linear_layout) {
        // Create a ScrollView
        /*HorizontalScrollView scrollView = new HorizontalScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
*/      //Set the Image

        CircleImageView img = new CircleImageView(this);

        int widthInPixels = 200; // Replace with your desired width in pixels
        int heightInPixels = 200; // Replace with your desired height in pixels

        ViewGroup.LayoutParams layoutParamsImg = new ViewGroup.LayoutParams(widthInPixels, heightInPixels);
        img.setLayoutParams(layoutParamsImg);
        String resourceName = mobileNumberObj.child("job_preference").getValue().toString().toLowerCase(); // replace with your resource name
        String packageName = getPackageName();

        int resourceId = getResources().getIdentifier(resourceName, "drawable", packageName);

        // Now, resourceId contains the integer id of the drawable
        img.setImageResource(resourceId);

        LinearLayout layout1 = new LinearLayout(this);
        int uniqueId = generateUniqueId();

        //layout1.setId(uniqueId);
        layout1.setOrientation(LinearLayout.VERTICAL);
        layout1.setBackgroundColor(getResources().getColor(R.color.blue));

        int widthInPixels2 = 400;
        int heightInPixels2 = 400;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                widthInPixels2, // Width
                heightInPixels2 // Height
        );
        layoutParams.gravity = Gravity.CENTER;

        int leftMargin = 10; // in pixels
        int topMargin = 30; // in pixels
        int rightMargin = 10; // in pixels
        int bottomMargin = 30; // in pixels

        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        // Apply LayoutParams to the LinearLayout
        layout1.setLayoutParams(layoutParams);

        TextView description = new TextView(this);
        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        description.setAllCaps(true);
        description.setTextSize(12);
        description.setText(mobileNumberObj.child("job_preference").getValue().toString());

        TextView location = new TextView(this);
        location.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        location.setText(mobileNumberObj.child("Location").getValue().toString());

        Button view = new Button(this);
        view.setText("View");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LabourPortal.this,LabourPortal2.class);
                i.putExtra("mob",mobile_number+mobileNumberObj.getKey());
                startActivity(i);
            }
        });

        layout1.addView(img);
        layout1.addView(description);
        layout1.addView(location);
        layout1.addView(view);

        if (Objects.equals(name_of_linear_layout, "ground")){
            LinearLayout container = findViewById(R.id.ground);
            container.addView(layout1);
        } else if (Objects.equals(name_of_linear_layout, "ground2")) {
            LinearLayout container = findViewById(R.id.ground2);
            container.addView(layout1);
        }


        //Button button = new Button(this);
        //button.setText(mobileNumber);
        /*
        // Set an OnClickListener for each button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click (e.g., navigate to a new activity with details)
                // You can use the mobileNumber to retrieve specific data for this button
            }
        });*/

        // Add the button to your layout (assuming you have a LinearLayout with id "container")
        //LinearLayout container = findViewById(R.id.ground);
        //container.addView(button);
    }
    private int generateUniqueId() {
        // You can use various methods to generate a unique ID, depending on your requirements.
        // For example, you can use the current timestamp as a simple way to generate a unique ID.
        return (int) System.currentTimeMillis();
    }
}