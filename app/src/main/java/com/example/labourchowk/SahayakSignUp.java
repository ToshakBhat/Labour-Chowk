package com.example.labourchowk;

import androidx.annotation.NonNull;
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

public class SahayakSignUp extends AppCompatActivity {
    EditText phoneNumber_sahayak;
    Button sendCode_sahayak;
    EditText name_sahayak;
    EditText location_sahayak;
    EditText smsCode_sahayak;
    Button signWithPhone_sahayak;
    String codeSent;
    FirebaseAuth auth = FirebaseAuth.getInstance(); //To create a firebase authentication object
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahayak_sign_up);

        phoneNumber_sahayak = findViewById(R.id.enter_mobile_number_sahayak);
        sendCode_sahayak= findViewById(R.id.send1_sahayak);
        smsCode_sahayak = findViewById(R.id.verification_code_sahayak);
        signWithPhone_sahayak = findViewById(R.id.send2_sahayak);
        name_sahayak = findViewById(R.id.name_sahayak);
        location_sahayak = findViewById(R.id.location_sahayak);

        sendCode_sahayak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsCode_sahayak.setVisibility(View.VISIBLE);
                signWithPhone_sahayak.setVisibility(View.VISIBLE);
                String userPhoneNumber = phoneNumber_sahayak.getText().toString();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+userPhoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS).setActivity(SahayakSignUp.this)//Valid for 60 seconds(1 minute) as we have written seconds in the 2nd parameter
                        .setCallbacks(mCallbacks)//mCallbacks is a function
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        signWithPhone_sahayak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signWithPhoneCode();
            }
        });


    }
    public void signWithPhoneCode(){
        String enterUserCode = smsCode_sahayak.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,enterUserCode);
        signInWithPhoneAuthCredential(credential);
    }
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Actions taken if the authentication is verified
                    reference.child("+91"+phoneNumber_sahayak.getText().toString()).child("user_name").setValue(name_sahayak.getText().toString());
                    reference.child("+91"+phoneNumber_sahayak.getText().toString()).child("Location").setValue(location_sahayak.getText().toString());
                    reference.child("+91"+phoneNumber_sahayak.getText().toString()).child("Status").setValue("Sahayak");
                    Toast.makeText(SahayakSignUp.this,"The code you entered is correct",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SahayakSignUp.this,MainActivity.class);
                    i.putExtra("mob","+91"+phoneNumber_sahayak.getText().toString());
                    startActivity(i);
                    finish();

                }
                else{
                    //If the authentication fails
                    Toast.makeText(SahayakSignUp.this,"The code you entered is incorrect",Toast.LENGTH_SHORT).show();
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