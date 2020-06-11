package com.piyushjaiswal12.primeflix.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.activity.Main3Activity;
import com.piyushjaiswal12.primeflix.activity.PhoneActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {

    public SignUp() {



    }
    private EditText email_edittext,password_edittext;
    private Button login;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       mAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        email_edittext = v.findViewById(R.id.email);
        password_edittext = v.findViewById(R.id.password);
        login = v.findViewById(R.id.signup);
        progressBar = v.findViewById(R.id.progress_signup);
        progressBar.setVisibility(View.GONE);
        imageView = v.findViewById(R.id.phone);

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String email;
                email = Objects.requireNonNull(email_edittext.getText()).toString().trim();
                String password = Objects.requireNonNull(password_edittext.getText()).toString().trim();
                if(password.length()>=6) {
                    login.setText("Signing Up...");
                    isVerified(email, password);

                }
                else
                    Toast.makeText(getContext(),"Length Short",Toast.LENGTH_SHORT).show();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhoneActivity.class);
                startActivity(intent);
            }
        });

    }
    private void isVerified(String email, String password)
    {

        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(),"Wait a minute",Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(true);
                            progressBar.setVisibility(View.GONE);

                        } else {

                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                              updateUI(false);
                              progressBar.setVisibility(View.GONE);
                              login.setText("Sign Up");

                        }

                        // ...
                    }
                });
    }
    private void updateUI(boolean check)
    {

        if(check)
        {
            Intent intent;
            intent = new Intent(getActivity(), Main3Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
            Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(),"Log In Failed",Toast.LENGTH_SHORT).show();
        }
    }


}
