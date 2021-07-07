package com.piyushjaiswal12.primeflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.piyushjaiswal12.primeflix.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {


    EditText number,otp,cc;
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
        cc =findViewById(R.id.code);
        login = findViewById(R.id.login);
        verifyotp = findViewById(R.id.verify);
        verifyotp.setClickable(false);
        progressBar = findViewById(R.id.progress_phone);
        login.setOnClickListener(
                v -> logIn());
        verifyotp.setOnClickListener(v -> verifyOtp());
    }

    private void verifyOtp() {
        String code = otp.getText().toString().trim();
        if(mVerificationId == null){
         Toast.makeText(getApplicationContext(),"First Request For OTP",Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);

            }
            catch (Exception e)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Verification Code is wrong, try again", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }

        }}
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
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
                });
    }

    private void logIn() {
        String cd = cc.getText().toString().trim();
        String  phoneNumber = "+"+cd+number.getText().toString();
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
             new Handler().postDelayed(() -> login.setClickable(true),60000);progressBar.setVisibility(View.GONE);

            // ...
        }
    };
}
