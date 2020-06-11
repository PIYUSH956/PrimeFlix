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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.activity.Main4Activity;
import com.piyushjaiswal12.primeflix.adapter.MovieAdapter;
import com.piyushjaiswal12.primeflix.adapter.MovieTrendingAdapter;
import com.piyushjaiswal12.primeflix.model.MoviesClass;

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
public class Movies extends Fragment implements  MovieAdapter.onClickListener,MovieTrendingAdapter.onClickListener {

    public Movies() {
        // Required empty public constructor
    }

    private MovieTrendingAdapter movieAdapter_trending;
    private List<MoviesClass> list = new ArrayList<>();
    private List<MoviesClass> list2 = new ArrayList<>();


    private RecyclerView recyclerView2;
    private MovieAdapter movieAdapter;
    private Movies movies= this;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_movies, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recycler);
        recyclerView2 = v.findViewById(R.id.recycler_crime);
        progressBar = v.findViewById(R.id.progressbar_movies);
            movieAdapter_trending = new MovieTrendingAdapter(getContext(),list,  this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter_trending);
        recyclerView2.setLayoutManager(layoutManager2);
        list.clear();
        String url = "https://geekstocode.com/testinggg/TrendingMovies.php";
        Fetching(url,list,true);
        list2.clear();
        String url2 = "https://geekstocode.com/testinggg/Movies.php";
        Fetching(url2,list2,false);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieAdapter.getFilter().filter(newText);
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
            case R.id.profile:


                return false;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(),Main4Activity.class);
        intent.putExtra("NameOfMovies",list2.get(position).getName());
        Log.d("click listining",list2.get(position).getName());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.sample2,0);
    }
    private void Fetching(String url, final List<MoviesClass> list2, final boolean t)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

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

                                Log.d("cccc", String.valueOf(jsonArray.length()));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    MoviesClass moviesClass;
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("Name");
                                    int integer = Integer.parseInt(object.getString("Total_Time"));
                                    //   String date = object.getString("Date");
                                    Date date1 = Date.valueOf("2000-01-01");
                                    String thumb = object.getString("Thumb");
                                    String category = object.getString("Category");

                                    moviesClass = new MoviesClass(name, integer, date1, category, thumb);
                                    list2.add(moviesClass);
                                }


                            }
                            if(t)
                            {
                                movieAdapter_trending.notifyDataSetChanged();
                            }
                            else {


                                movieAdapter = new MovieAdapter(getContext(), list2, movies);
                                recyclerView2.setAdapter(movieAdapter);
                                recyclerView2.scheduleLayoutAnimation();
                                progressBar.setVisibility(View.GONE);

                            }
                            Log.d("length", String.valueOf(list2.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onClick1(int position) {
        Intent intent = new Intent(getActivity(),Main4Activity.class);
        intent.putExtra("NameOfMovies",list.get(position).getName());
        Log.d("clicktrending listining",list.get(position).getName());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.sample2,0);
    }
}



