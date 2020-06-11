package com.piyushjaiswal12.primeflix.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.piyushjaiswal12.primeflix.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity  {

    ImageView profile;
    EditText name, work, phoneNumber;
    Button upload, update;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    Bitmap bitmap;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    String encodedImage;
    TextView name_t, work_t;
    String user, origrnal_name, original_work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name_edit_text);
        work = findViewById(R.id.work_edit_text);
        phoneNumber = findViewById(R.id.number);
        name_t = findViewById(R.id.profileName);
        work_t = findViewById(R.id.occupation);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        update = findViewById(R.id.update);
        upload = findViewById(R.id.upload);
        progressBar = findViewById(R.id.progress);
        swipeRefreshLayout = findViewById(R.id.swipe);
        user =gettingUser();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fetching(user);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        progressBar.setVisibility(View.GONE);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onResume() {
        super.onResume();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
                   }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempname = name.getText().toString().trim();
                String tempwork = work.getText().toString().trim();
                if(!tempname.equals(origrnal_name)  && tempname.length()>=1) {
                    origrnal_name = tempname;
                }
                if(!tempwork.equals(original_work) && tempwork.length()>=1)
                {
                    original_work = tempwork;
                }
                user = gettingUser();
                if(!user.equals("send") && !user.equals("not"))
                checking(origrnal_name, original_work, user, encodedImage);
                if(user.equals("send"))
                    sendEmailConfirmation();
            }
        });


    }

    public void logOut(View view) {
        firebaseAuth.signOut();
        Intent intent;
        intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void checking(final String name, final String work, final String number, final String encodedImage) {

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://geekstocode.com/testinggg/AddUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Refresh to see result", Toast.LENGTH_SHORT).show();

                        Fetching(user);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> map = new HashMap<>();

                map.put("Name", name);
                map.put("Work", work);
                map.put("UserId", number);
                if (encodedImage != null)
                    map.put("image", encodedImage);
                return map;

            }

        };

        queue.add(stringRequest);


    }

    private void uploadData() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();
            try {
                assert filePath != null;
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
                imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Toast.makeText(getApplicationContext(), "Click on update button to Update your profile", Toast.LENGTH_LONG).show();

    }


    public void Fetching(final String number) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://geekstocode.com/testinggg/FetchUserProfile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("userId", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.d("JSONARRAY", jsonArray.toString());
                            if (success.equals("1")) {
                                JSONObject object = jsonArray.getJSONObject(0);

                                String name = object.getString("name");
                                origrnal_name = name;
                                Log.d("Name", name);
                                String work = object.getString("work");
                                original_work = work;
                                String url = "https://geekstocode.com/UserImages/" + object.getString("image");
                                name_t.setText(name);
                                work_t.setText(work);
                                Glide.with(getApplicationContext()).load(url).apply(RequestOptions.circleCropTransform()).into(profile);
                                progressBar.setVisibility(View.GONE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> map = new HashMap<>();
                map.put("UserId", number);

                return map;

            }
        };

        queue.add(stringRequest);


    }


    public void sendEmailConfirmation() {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button

                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this,
                                    "Verification email sent to " + firebaseUser.getEmail() + "Wait for 5 minutes to get your account approved",
                                    Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(ProfileActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            ;
                        }
                    }
                });
    }


    public String gettingUser() {
        if (firebaseUser.getEmail() != null && firebaseUser.getEmail().length() > 10) {
            if (firebaseUser.isEmailVerified()) {
                Fetching(firebaseUser.getEmail());
                return firebaseUser.getEmail();
            } else {
                return "send";
            }
        } else {
            if (firebaseUser.getPhoneNumber() != null) {
               Fetching (firebaseUser.getPhoneNumber());
                return firebaseUser.getPhoneNumber();
            }


        }
        return "not";


    }

}
