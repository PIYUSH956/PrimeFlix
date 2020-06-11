package com.piyushjaiswal12.primeflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.piyushjaiswal12.primeflix.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {


    EditText number,otp;
    Button login,verifyotp;
    private FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mAuth = FirebaseAuth.getInstance();
        number = findViewById(R.id.phone);
        otp = findViewById(R.id.number);
        login = findViewById(R.id.login);
        verifyotp = findViewById(R.id.verify);
        verifyotp.setClickable(false);
        progressBar = findViewById(R.id.progress_phone);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
    }

    private void verifyOtp() {
        String code = otp.getText().toString().trim();
        if(mVerificationId == null){
         Toast.makeText(getApplicationContext(),"First Request For OTP",Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);


        }}
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            otp.setText(credential.getSmsCode());
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                            // ...
                            Intent intent;
                            intent = new Intent(PhoneActivity.this, Main3Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.GONE);
                        } else {

                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Invalid Code",Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void logIn() {
        String  phoneNumber = "+91"+number.getText().toString();
        if(phoneNumber.length()!=13 )
        {
            Toast.makeText(getApplicationContext(),"Wrong Number",Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);

        }

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            signInWithPhoneAuthCredential(credential);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(getApplicationContext(),"Invalid Request",Toast.LENGTH_SHORT).show();
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(getApplicationContext(),"Too many request",Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;
            mResendToken = token;
            Toast.makeText(getApplicationContext(),"Wait for a minute",Toast.LENGTH_SHORT).show();
            login.setClickable(false);
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    login.setClickable(true);
                }
            },60000);progressBar.setVisibility(View.GONE);

            // ...
        }
    };
}
