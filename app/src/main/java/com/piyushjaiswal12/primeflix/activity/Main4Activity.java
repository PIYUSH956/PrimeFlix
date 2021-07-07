package com.piyushjaiswal12.primeflix.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.piyushjaiswal12.primeflix.InternetConnection;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.adapter.EpisodesAdapter;
import com.piyushjaiswal12.primeflix.model.EpisodesClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class Main4Activity extends AppCompatActivity implements EpisodesAdapter.clickListener, RewardedVideoAdListener {
    Intent intent;
    RecyclerView recyclerView_episodes;
    EpisodesAdapter episodesAdapter;
    List<EpisodesClass> episodesClassList = new ArrayList<>();
    ProgressBar progressBar;
    ImageView imageView;
    ImageView play;
    String nameofwebseries,nameofmovies;
    int position = 0;
    TextView textView;

    TextView type,internetspeed;
    private RewardedVideoAd mRewardedVideoAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        type = findViewById(R.id.type);
        intent =  getIntent();
        internetspeed = findViewById(R.id.netspeed);
        nameofwebseries = intent.getStringExtra("NameOfWebSeries");
        nameofmovies = intent.getStringExtra("NameOfMovies");
        String cat = intent.getStringExtra("Type");
        type.setText(cat);
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

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    @Override
    protected void onStart() {
        super.onStart();
        play.setOnClickListener(v -> {
            Intent intent = new Intent(Main4Activity.this,PlayerActivity.class);
            if(episodesClassList.size()>position) {
                intent.putExtra("Url", episodesClassList.get(position).getUrl());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No Video",Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
        if (mRewardedVideoAd.isLoaded()) {
            Log.d("checking","ADSLOADED");
            mRewardedVideoAd.show();
        }

        float speed = InternetConnection.isNetworkConnectedAndSpeed(getApplicationContext())/(1000);
        String qulaity ="poor";
        if(speed>=500)
            qulaity = "Good";
        if(speed>=1000)
            qulaity = "Excellent";
        internetspeed.setText("BandWidth is "+speed+" Kbps  "+qulaity);
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    private void Fetching(String url, final String name)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("userId", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                               for (int i = 0; i < jsonArray.length(); i++) {
                                   EpisodesClass episodesClass;
                                   JSONObject object = jsonArray.getJSONObject(i);
                                   String name1 = object.getString("Name");
                                   String episode = object.getString("Episode");
                                   String desc = object.getString("Description");
                                   String thumb = object.getString("Thumb");
                                   String url1 = object.getString("Url");

                                   episodesClass = new EpisodesClass(name1, episode, desc, thumb, url1);
                                   episodesClassList.add(episodesClass);
                                   try {
                                       Glide.with(getApplicationContext()).load("https://geekstocode.com/" + episodesClassList.get(0).getThumb()).into(imageView);
                                       if (episodesClassList.size() >= 1)
                                           textView.setText(episodesClassList.get(0).getDesc());
                                   }
                                   catch (Exception e)
                                   {
//
                                   }
                               }
                               episodesAdapter.notifyDataSetChanged();
                               recyclerView_episodes.scheduleLayoutAnimation();
                               progressBar.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<>();
                map.put("Episode",name);
                return map;
            }
        };

        queue.add(stringRequest);
    }
    @Override
    public void onClicker(int position) {
        try {
            Toast.makeText(getApplicationContext(), episodesClassList.get(position).getName(), Toast.LENGTH_SHORT).show();
            Glide.with(getApplicationContext()).load("https://geekstocode.com/" + episodesClassList.get(position).getThumb()).into(imageView);
            textView.setText(episodesClassList.get(position).getDesc());
            this.position = position;
        }
        catch (IndexOutOfBoundsException e)
        {
//
        }
    }


   /* public void startDownload(String u) {
        DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(u));
        request.setDescription(episodesClassList.get(position).getName()+episodesClassList.get(position).getEpisodes());
        assert mManager != null;
        long idDownLoad=mManager.enqueue(request);
    }*/


    @Override
    public void onRewarded(RewardItem reward) {

        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-2685099075999149/9404703497",
                new AdRequest.Builder().build());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
