package com.piyushjaiswal12.primeflix.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.activity.PlayerActivity;
import com.piyushjaiswal12.primeflix.adapter.UpComingAdapter;
import com.piyushjaiswal12.primeflix.model.UpComingClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComing extends Fragment implements UpComingAdapter.clickListener  {

    public UpComing() {
        // Required empty public constructor
    }
    private UpComingAdapter upComingAdapter;
    private RecyclerView recyclerView2;
    private List<UpComingClass> upComingClasses = new ArrayList<>();
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_up_coming, container, false);
        AdView mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
       recyclerView2 = v.findViewById(R.id.recycler_crime);
        upComingAdapter = new UpComingAdapter(getContext(),upComingClasses,this);
        progressBar = v.findViewById(R.id.progressBar);


        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 2);


        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(upComingAdapter);
        progressBar.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(upComingClasses.isEmpty())
            Fetching();

    }





    @Override
    public void onResume() {
        super.onResume();

    }






    private void Fetching()
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://geekstocode.com/testinggg/Upcoming.php",
                response -> {
                    Log.d("userId", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {

                            Log.d("cccc", String.valueOf(jsonArray.length()));
                            for (int i = 0; i < jsonArray.length(); i++) {

                                UpComingClass upComingClass;
                                JSONObject object = jsonArray.getJSONObject(i);

                                String name = object.getString("Name");
                                String thumb = object.getString("Thumb");
                                String url1 = object.getString("Url");
                                upComingClass= new UpComingClass(name, thumb, url1);
                                upComingClasses.add(upComingClass);
                            }


                        }
                        upComingAdapter.notifyDataSetChanged();
                        recyclerView2.scheduleLayoutAnimation();
                        progressBar.setVisibility(View.GONE);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                });

        queue.add(stringRequest);
    }

    @Override
    public void onClicker(int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("Url",upComingClasses.get(position).getUrl());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.sample2,0);
    }
}



