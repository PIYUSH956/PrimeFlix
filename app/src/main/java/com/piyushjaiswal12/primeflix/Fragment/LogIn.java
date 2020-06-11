package com.piyushjaiswal12.primeflix.Fragment;

import android.app.AlertDialog;
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
public class LogIn extends Fragment {

    public LogIn() {
        // Required empty public constructor
    }
    private FirebaseAuth mAuth;
    private EditText email_edittext,password_edittext;
    private Button login;
    private ImageView imageView;
    private AlertDialog alertDialog;
    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
      v = inflater.inflate(R.layout.fragment_log_in, container, false);
        email_edittext = v.findViewById(R.id.email);
        password_edittext = v.findViewById(R.id.password);
        login = v.findViewById(R.id.login);
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
                String email = Objects.requireNonNull(email_edittext.getText()).toString().trim();
                String password = Objects.requireNonNull(password_edittext.getText()).toString().trim();
                if(password.length()>=6) {

                    checkLogIn(email, password);

                }
                else
                {
                    Toast.makeText(getContext(),"Short Password",Toast.LENGTH_SHORT).show();
                }
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



    private void checkLogIn(String email, String password)
    {

        custom();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                            alertDialog.hide();

                        } else {

                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        alertDialog.hide();
                        }


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
            Intent intent;
            intent = new Intent(getActivity(), Main3Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
            Toast.makeText(getContext(),"Login Sucess",Toast.LENGTH_SHORT).show();
        }
    }

    private void custom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.progress_layout, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }



}
