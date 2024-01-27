package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SahayakPortal extends AppCompatActivity {
    String mobile_number;
    String Location;
    String name;
    TextView title_sahayak;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference reference = database.getReference().child("Labour");
    DatabaseReference reference = database.getReference();
    List<String> mobileNumbersListSahayak = new ArrayList<>();
    List<DataSnapshot> mobileNumbersListSnapshot = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahayak_portal);
        title_sahayak = findViewById(R.id.sahayak_top);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("mob")){
            mobile_number = (String) intent.getSerializableExtra("mob");
        }


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user data
                    if(Objects.equals(userSnapshot.getKey().toString(), mobile_number)){
                        if(userSnapshot.child("Location").exists()){
                            Location = userSnapshot.child("Location").getValue().toString();
                        }else{
                            Location = "None";
                        }
                        name = userSnapshot.child("user_name").getValue().toString();
                        title_sahayak.setText("Namaste "+ name + " !");
                        //location.setText(my_location);
                    }
                    if (Objects.equals(userSnapshot.child("Status").getValue(), "Employer")){
                        mobileNumbersListSahayak.add(userSnapshot.getKey());
                        mobileNumbersListSnapshot.add(userSnapshot);
                    }
                }
                for (DataSnapshot Employer_Snapshot : mobileNumbersListSnapshot){
                    createLinearLayout(Employer_Snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createLinearLayout(DataSnapshot EmployerSnapshot) {
        LinearLayout img_container = new LinearLayout(this);
        img_container.setOrientation(LinearLayout.HORIZONTAL);
        //img_container.setBackgroundColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams layoutParamsimg = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT // Height
        );
        layoutParamsimg.gravity = Gravity.CENTER;
        int leftMargin2 = 0; // in pixels
        int topMargin2 = 10; // in pixels
        int rightMargin2 = 0; // in pixels
        int bottomMargin2 = 10; // in pixels

        layoutParamsimg.setMargins(leftMargin2, topMargin2, rightMargin2, bottomMargin2);
        img_container.setLayoutParams(layoutParamsimg);

        CircleImageView img = new CircleImageView(this);

        int widthInPixels = 250; // Replace with your desired width in pixels
        int heightInPixels = 250; // Replace with your desired height in pixels

        ViewGroup.LayoutParams layoutParamsImg = new ViewGroup.LayoutParams(widthInPixels, heightInPixels);
        img.setLayoutParams(layoutParamsImg);
        String resourceName = EmployerSnapshot.child("job_preference").getValue().toString().toLowerCase(); // replace with your resource name
        String packageName = getPackageName();

        int resourceId = getResources().getIdentifier(resourceName, "drawable", packageName);

        // Now, resourceId contains the integer id of the drawable
        img.setImageResource(resourceId);
        img_container.addView(img);

        LinearLayout layout1 = new LinearLayout(this);

        //layout1.setId(uniqueId);
        layout1.setOrientation(LinearLayout.VERTICAL);
        layout1.setBackgroundColor(getResources().getColor(R.color.blue));

        int widthInPixels2 = 500;
        int heightInPixels2 = 500;
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
        description.setText(EmployerSnapshot.child("job_preference").getValue().toString());

        TextView location = new TextView(this);
        location.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        location.setText(EmployerSnapshot.child("Location").getValue().toString());

        Button view = new Button(this);
        view.setText("Apply");


        layout1.addView(img_container);
        layout1.addView(description);
        layout1.addView(location);
        layout1.addView(view);


        LinearLayout container = findViewById(R.id.target);
        container.addView(layout1);


    }
}