package com.piyushjaiswal12.primeflix.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.piyushjaiswal12.primeflix.InternetConnection;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.activity.Main4Activity;
import com.piyushjaiswal12.primeflix.adapter.WebSeriesAdapter;
import com.piyushjaiswal12.primeflix.adapter.TrendingAdapter;
import com.piyushjaiswal12.primeflix.model.WebSeriesClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * A simple {@link Fragment} subclass.
 */
public class WebSeries extends Fragment implements TrendingAdapter.onClickListener, WebSeriesAdapter.onClickListener {

    public WebSeries() {
        // Required empty public constructor
    }
    private LottieAnimationView lottieAnimationView;
    private TrendingAdapter trendingAdapter;
    private List<WebSeriesClass> list = new ArrayList<>();

    private RecyclerView recyclerView2;
    private WebSeriesAdapter webSeriesAdapter;
    private List<WebSeriesClass> list2 = new ArrayList<>();
    private ProgressBar progressBar;
    final private WebSeries activity = this;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web_series, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recycler);
        recyclerView2 = v.findViewById(R.id.recycler_crime);
        AdView mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        lottieAnimationView = v.findViewById(R.id.nointernet);

        trendingAdapter = new TrendingAdapter(getContext(),list,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trendingAdapter);

        recyclerView2.setLayoutManager(layoutManager2);
        progressBar = v.findViewById(R.id.progress_bar);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(InternetConnection.isNetworkConnected(Objects.requireNonNull(getContext())))
        {
            String url = "https://geekstocode.com/testinggg/WebSeries.php";
            Fetching(url, list2, false);
            String url2 = "https://geekstocode.com/testinggg/TrendingWebSeries.php";
            Fetching(url2, list, true);
            lottieAnimationView.setVisibility(View.GONE);
        }
        else
        {
            lottieAnimationView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    webSeriesAdapter.getFilter().filter(query);
                }
                catch (NullPointerException e)
                {
                    //Loading
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    webSeriesAdapter.getFilter().filter(newText);
                }
                catch (NullPointerException e)
                {
                    //Loading
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:

                Log.d("Inside Search","Inside Search");

                return true;
        //    case R.id.profile:


          //      return false;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(),Main4Activity.class);
        intent.putExtra("NameOfWebSeries",list2.get(position).getName());
        intent.putExtra("Type",list2.get(position).getCategory());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.sample2,0);
    }
    private void Fetching(String url, final List<WebSeriesClass> list2, final boolean t)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Log.d("userId", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")) {

                                Log.d("cccc", String.valueOf(jsonArray.length()));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    WebSeriesClass webSeriesClass;
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("Name");
                                    //   String date = object.getString("Date");
                                    Date date1 = Date.valueOf("2000-01-01");
                                    String thumb = object.getString("Thumb");
                                    String category = object.getString("Category");
                                    int totaltime = object.getInt("Total Time");
                                    webSeriesClass = new WebSeriesClass(name,  totaltime, date1, category, thumb);
                                    list2.add(webSeriesClass);
                                }


                            }
                            if(t)
                            {
                                trendingAdapter.notifyDataSetChanged();
                            }
                            else {


                                webSeriesAdapter = new WebSeriesAdapter(getContext(), list2, activity);
                                recyclerView2.setAdapter(webSeriesAdapter);
                                recyclerView2.scheduleLayoutAnimation();
                                progressBar.setVisibility(View.GONE);

                            }
                            Log.d("length", String.valueOf(list2.size()));
                            lottieAnimationView.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, error -> {

                        try {
                            Toast.makeText(getContext(), "Try To Restart App", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                        catch (Exception e)
                        {
    //
                        }
                    });

            queue.add(stringRequest);
        }

    @Override
    public void onClick1(int position) {
        Intent intent = new Intent(getActivity(),Main4Activity.class);
        intent.putExtra("NameOfWebSeries",list.get(position).getName());
        intent.putExtra("Type",list.get(position).getCategory());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.sample2,0);
    }
}



