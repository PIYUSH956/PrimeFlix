package com.piyushjaiswal12.primeflix.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.activity.Main2Activity;
import com.piyushjaiswal12.primeflix.activity.Main3Activity;
import com.piyushjaiswal12.primeflix.activity.PhoneActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogIn extends Fragment {

    public LogIn() {
        // Required empty public constructor
    }
    private FirebaseAuth mAuth;
    private EditText email_edittext,password_edittext;
    private Button login;
    private ImageView imageView;
  private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        email_edittext = v.findViewById(R.id.email);
        progressBar = v.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        password_edittext = v.findViewById(R.id.password);
        login = v.findViewById(R.id.login);
        imageView = v.findViewById(R.id.phone);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        login.setOnClickListener(v -> {
            String email = Objects.requireNonNull(email_edittext.getText()).toString().trim();
            String password = Objects.requireNonNull(password_edittext.getText()).toString().trim();
            if(password.length()>=6 && email.length()>=10) {

                checkLogIn(email, password);

            }
            else
            {
                Toast.makeText(getContext(),"Short Password or Email",Toast.LENGTH_SHORT).show();
            }
        });
      imageView.setOnClickListener(v -> {
          Intent intent = new Intent(getActivity(), PhoneActivity.class);
          startActivity(intent);
      });
    }



    private void checkLogIn(String email, String password)
    {

        custom();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        updateUI(user);
                        progressBar.setVisibility(View.GONE);


                    } else {

                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    updateUI(null);
                    progressBar.setVisibility(View.GONE);

                    }


                });

    }


    private void updateUI(FirebaseUser user)
    {
        if(user == null)
        {
            Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                Intent intent;
                intent = new Intent(getActivity(), Main3Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                Toast.makeText(getContext(), "Login Sucess", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Intent intent;
                intent = new Intent(getContext(), Main3Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void custom() {
        progressBar.setVisibility(View.VISIBLE);
    }



}
