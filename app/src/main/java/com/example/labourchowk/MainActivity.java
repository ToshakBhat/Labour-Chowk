package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText edit;
    Button send;
    Button create_account;
    TextView title_bar;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference reference = database.getReference().child("Labour");
    DatabaseReference reference2 = database.getReference();
    String mobile_number;
    String name;
    Button emergency;
    //String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //emergency = findViewById(R.id.emergency);
        send = findViewById(R.id.Log_In_button);
        edit = findViewById(R.id.mob);
        title_bar = findViewById(R.id.title_bar);
        create_account = findViewById(R.id.create_account);
        List<String> mobileNumbersList = new ArrayList<>();
        DataSnapshot mobile_object; //Its the json array we needed having key as mobile number and value as the data in the database.
        List<DataSnapshot> mobileNumbersList2 = new ArrayList<>();


        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //List<String> mobileNumbersList = new ArrayList<>();

                // Check if dataSnapshot is not null
                if (dataSnapshot.exists()) {
                    // Iterate through the data
                    //val = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //String mobileNumber = Objects.requireNonNull(snapshot.getValue()).toString();
                        String val = snapshot.getKey(); //snapshot is like a dictionary( in python).So every JSON file has data in the form of dictionaries
                        //String mobileNumber = Objects.requireNonNull(snapshot.getValue()).toString();
                        if (val != null) {
                            mobileNumbersList.add(val);
                            mobileNumbersList2.add(snapshot);
                        }
                    }

                    // Rest of your code
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mobile_number = edit.getText().toString();
               String complete_mobile_number = "+91" + mobile_number;
               //title_bar.setText(mobileNumbersList.toString());
               
               if(mobile_number.length() != 10){ //check if the mobile number has 10 digits or not
                   Toast.makeText(MainActivity.this,"Please enter Mobile Number correctly",Toast.LENGTH_SHORT).show();
               } else if (mobileNumbersList.contains(complete_mobile_number)) {
                   //Account exists !!

                   //Basically we got the index of mobile number in the list and then we used the same index to get the Snapshot object
                   //of required mobile number so that we can access the name and other details of user associated with that mobile number.
                   int index = mobileNumbersList.indexOf(complete_mobile_number);
                   DataSnapshot s = mobileNumbersList2.get(index); //To get the Snapshot object of mobile number

                   //So we created a HasMap(like a Python dictionary) of the database with concerned mobile number.
                   HashMap<String,Object> dictionary = new HashMap<>();
                   dictionary.put("user_name",s.child("user_name").getValue().toString());
                   dictionary.put("Location",s.child("Location").getValue().toString());

                   String status = s.child("Status").getValue().toString();
                   if(status.equals("Labour")){
                       Intent i = new Intent(MainActivity.this,LabourPortal.class);
                       /*
                        In Android, if you want to pass a DataSnapshot object in the putExtra method of an Intent, we have a few options.
                        Since DataSnapshot is not directly Parcelable, we need to find a way to convert it into a format that can be passed
                        through an Intent.
                        One common approach is to convert the relevant data from the DataSnapshot into a format that can be passed through
                        Intent extras e.g HashMap*/
                       i.putExtra("mob",complete_mobile_number);
                       startActivity(i);

                   } else if (status.equals("Employer")) {
                       Intent i = new Intent(MainActivity.this,EmployerPortal.class);
                       i.putExtra("mob",complete_mobile_number);
                       startActivity(i);

                   }else if(status.equals("Sahayak")){
                       Intent i = new Intent(MainActivity.this,SahayakPortal.class);
                       i.putExtra("mob",complete_mobile_number);
                       startActivity(i);

                   }
                   //Toast.makeText(MainActivity.this,value1,Toast.LENGTH_SHORT).show();

               }else{
                   //Mobile number doesn't exist in the database !
                   Toast.makeText(MainActivity.this,"Please make an account first !!",Toast.LENGTH_SHORT).show();
               }

                //edit.setText(name);
                //reference.child("userName").setValue(userName); To set value in the database
            }
        });

        
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });

    }
}