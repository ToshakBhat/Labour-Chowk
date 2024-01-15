package com.example.labourchowk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.concurrent.TimeUnit;


public class LabourSignUp extends AppCompatActivity {

    EditText phoneNumber;
    Button sendCode;
    ImageView profile_image;
    Button upload;
    ProgressBar progress;
    ImageView imageView;
    Uri imageUri;
    EditText labour_name;
    EditText labour_location;
    EditText smsCode;
    Button signWithPhone;
    String codeSent;
    FirebaseAuth auth = FirebaseAuth.getInstance(); //To create a firebase authentication object
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_sign_up);
        phoneNumber = findViewById(R.id.enter_mobile_number);
        sendCode = findViewById(R.id.send1);
        smsCode = findViewById(R.id.verification_code);
        signWithPhone = findViewById(R.id.send2);
        labour_name = findViewById(R.id.labour_name);
        labour_location = findViewById(R.id.labour_location);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsCode.setVisibility(View.VISIBLE);
                signWithPhone.setVisibility(View.VISIBLE);
                String userPhoneNumber = phoneNumber.getText().toString();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+userPhoneNumber)
                                .setTimeout(60L, TimeUnit.SECONDS).setActivity(LabourSignUp.this)//Valid for 60 seconds(1 minute) as we have written seconds in the 2nd parameter
                        .setCallbacks(mCallbacks)//mCallbacks is a function
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        signWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signWithPhoneCode();
            }
        });


    }
    public void signWithPhoneCode(){
        String enterUserCode = smsCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,enterUserCode);
        signInWithPhoneAuthCredential(credential);
    }
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Actions taken if the authentication is verified
                    reference.child("+91"+phoneNumber.getText().toString()).child("user_name").setValue(labour_name.getText().toString());
                    reference.child("+91"+phoneNumber.getText().toString()).child("Location").setValue(labour_location.getText().toString());
                    reference.child("+91"+phoneNumber.getText().toString()).child("Status").setValue("Labour");
                    Toast.makeText(LabourSignUp.this,"The code you entered is correct",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LabourSignUp.this,LabourSkills.class);
                    i.putExtra("mob","+91"+phoneNumber.getText().toString());
                    startActivity(i);
                    finish();

                }
                else{
                    //If the authentication fails
                    Toast.makeText(LabourSignUp.this,"The code you entered is incorrect",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);//s means the code sent by Firebase to the user
            codeSent = s;
        }
    };

}