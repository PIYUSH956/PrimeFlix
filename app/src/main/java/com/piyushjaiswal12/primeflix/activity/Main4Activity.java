package com.piyushjaiswal12.primeflix.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.adapter.EpisodesAdapter;
import com.piyushjaiswal12.primeflix.model.CastClass;
import com.piyushjaiswal12.primeflix.model.EpisodesClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class Main4Activity extends AppCompatActivity implements EpisodesAdapter.clickListener{
    Intent intent;
    RecyclerView recyclerView_episodes;
    EpisodesAdapter episodesAdapter;
    List<CastClass> classList = new ArrayList<>();
    List<EpisodesClass> episodesClassList = new ArrayList<>();
    ProgressBar progressBar;
    ImageView imageView;
    ImageView play;
    String nameofwebseries,nameofmovies;
    int position = 0;
    TextView textView;
    ImageView download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        intent =    getIntent();
        download = findViewById(R.id.download);
        nameofwebseries = intent.getStringExtra("NameOfWebSeries");
        nameofmovies = intent.getStringExtra("NameOfMovies");

        imageView = findViewById(R.id.thumb);
        textView  = findViewById(R.id.story_textview);
        play = findViewById(R.id.play_button);
        recyclerView_episodes  = findViewById(R.id.episodes_recycler_view);
        episodesAdapter = new EpisodesAdapter(getApplicationContext(), episodesClassList,this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView_episodes.setLayoutManager(layoutManager2);
        recyclerView_episodes.setAdapter(episodesAdapter);
        progressBar = findViewById(R.id.progress);
        if(nameofmovies==null)
        Fetching("https://geekstocode.com/testinggg/EpisodeConnectivity.php",nameofwebseries);
        if(nameofwebseries == null)
            Fetching("https://geekstocode.com/testinggg/MovieConnectivity.php",nameofmovies);
    }

    @Override
    protected void onStart() {
        super.onStart();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this,PlayerActivity.class);
                intent.putExtra("Url",episodesClassList.get(position).getUrl());
                startActivity(intent);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String u = "https://geekstocode.com/VideoFolderPrimeFlix/";
               u += episodesClassList.get(position).getUrl();
               startDownload(u);
            }
        });

    }

    private void Fetching(String url,final String name)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("userId", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")) {
                                   for (int i = 0; i < jsonArray.length(); i++) {
                                    EpisodesClass episodesClass;
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("Name");
                                    String episode = object.getString("Episode");
                                    String desc= object.getString("Description");
                                    String thumb = object.getString("Thumb");
                                    String url = object.getString("Url");

                                    episodesClass = new EpisodesClass(name, episode, desc,thumb,url);
                                    episodesClassList.add(episodesClass);
                                    Glide.with(getApplicationContext()).load("https://geekstocode.com/"+episodesClassList.get(0).getThumb()).into(imageView);
                                    textView.setText(episodesClassList.get(0).getDesc());
                                }
                                episodesAdapter.notifyDataSetChanged();
                                   recyclerView_episodes.scheduleLayoutAnimation();
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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Episode",name);
                return map;
            }
        };

        queue.add(stringRequest);
    }
    @Override
    public void onClicker(int position) {
        Toast.makeText(getApplicationContext(),episodesClassList.get(position).getName(),Toast.LENGTH_SHORT).show();
        Glide.with(getApplicationContext()).load("https://geekstocode.com/"+episodesClassList.get(position).getThumb()).into(imageView);
        textView.setText(episodesClassList.get(position).getDesc());
        this.position = position;
    }

    public void startDownload(String u) {
        DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(u));
        request.setDescription(episodesClassList.get(position).getName()+episodesClassList.get(position).getEpisodes());
        assert mManager != null;
        long idDownLoad=mManager.enqueue(request);
    }
}
