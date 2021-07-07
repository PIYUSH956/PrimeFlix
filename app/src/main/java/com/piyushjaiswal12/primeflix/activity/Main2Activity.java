package com.piyushjaiswal12.primeflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.piyushjaiswal12.primeflix.Fragment.LogIn;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.Fragment.SignUp;

public class Main2Activity extends AppCompatActivity {

    TextView login,signup;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.frameLayout, new LogIn(), "")
                .commit();
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> {
            login.setTextSize(45);
            signup.setTextSize(25);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
           fragmentTransaction.replace(R.id.frameLayout, new LogIn()).commit();
        });

        signup = findViewById(R.id.signup);

        signup.setOnClickListener(v -> {
            signup.setTextSize(45);
            login.setTextSize(25);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.exit_to_left, R.anim.exit_from_right);

            fragmentTransaction.replace(R.id.frameLayout,new SignUp()).commit();
            Log.d("checking","SignUp");
        });



    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            Intent intent;
            intent = new Intent(Main2Activity.this, Main3Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }



}
