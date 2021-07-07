package com.piyushjaiswal12.primeflix.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.adapter.WebSeriesAdapter;
import com.piyushjaiswal12.primeflix.model.WebSeriesClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    Spinner dropdown,male_female;
    EditText name,occupation,age,bio,adress;
    String nameS,occupationS,ageS,bioS,adressS;
    String sex,martial;
    ProgressBar progressBar;
    String userr;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
         dropdown = findViewById(R.id.spinner1);
         male_female = findViewById(R.id.male_female);
         name = findViewById(R.id.name);
         occupation = findViewById(R.id.occupation);
         age = findViewById(R.id.age);
         textView = findViewById(R.id.number);
         bio = findViewById(R.id.bio);
         adress = findViewById(R.id.adress);
         progressBar = findViewById(R.id.progress_bar);
        String[] items = new String[]{"Single", "Married", "Looking For Partner"};
        String[] items2 = new String[]{"Male","Female","Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_layout, items);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,R.layout.spinner_layout, items2);
        dropdown.setAdapter(adapter);
        male_female.setAdapter(adapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                martial = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                martial = "Single";
            }
        });
        male_female.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString().trim();
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               sex = "Others";
            }
        });

       FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();


       String email = user.getEmail();
       String num = user.getPhoneNumber();
       if(email==null) {
        userr = num;
           textView.setText(num);
       }
       else {
        userr= email;
           textView.setText(email);

       }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public  boolean checkProfile()
    {
        
        nameS = name.getText().toString().trim();
        bioS = bio.getText().toString().trim();
        occupationS = occupation.getText().toString().trim();
        ageS = age.getText().toString().trim();
        adressS = adress.getText().toString().trim();
        boolean check = true;
        if(nameS.length()<=1|| bioS.length()<=1|| occupationS.length()<=1 || ageS.length()==0|| adressS.length()<=1)
            check = false;
        
        return check;
    }
    public void updateProfile(View view) {
        
        if(checkProfile())
        {
            updateInMysql();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Some field missing",Toast.LENGTH_SHORT).show();
        }
    }
    private void updateInMysql() {

    Fetching("https://geekstocode.com/testinggg/NewUser.php");
    }
    private void Fetching(String url)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                           progressBar.setVisibility(View.GONE);
                           Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 try{
                     Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                 }
                 catch (Exception e)
                 {

                 }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("name",nameS);
                params.put("bio",bioS);
                params.put("sex",sex);
                params.put("age",ageS);
                params.put("occupation",occupationS);
                params.put("martial",martial);
                params.put("address",adressS);
                params.put("number",userr);
                return params;
            }

        };
        queue.add(sr);

    }

    private void FetchingProfile(String url)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {

                }
            }
        });

        queue.add(sr);
    }
}
