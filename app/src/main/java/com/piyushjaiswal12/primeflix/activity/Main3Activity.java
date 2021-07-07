package com.piyushjaiswal12.primeflix.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.piyushjaiswal12.primeflix.Fragment.Movies;
import com.piyushjaiswal12.primeflix.Fragment.UpComing;
import com.piyushjaiswal12.primeflix.Fragment.WebSeries;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.adapter.TabAdapter;

public class Main3Activity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabAdapter tabAdapter;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toolbar = findViewById(R.id.toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabAdapter.addFragment(new WebSeries(), "Web Series");
        tabAdapter.addFragment(new Movies(), "Movies");
        tabAdapter.addFragment(new UpComing(), "Up Coming");
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
               Log.d("cehck","In Search");
                return false;
           /* case R.id.profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                return true;
            */case R.id.filter:
                Toast.makeText(getApplicationContext(),"Please Rate Us",Toast.LENGTH_SHORT).show();
                rateUs();
                return  true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Main3Activity.this,Main2Activity.class));
                finish();
                return  true;
            /*case R.id.edit_profile:
                startActivity( new Intent(getApplicationContext(), EditProfile.class));
                return true;
              */  default:
                return super.onOptionsItemSelected(item);
        }
    }

   private void rateUs()
    {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }



    @Override
    protected void onResume() {

        super.onResume();
    }


    @Override
    protected void onPause() {

        super.onPause();
    }


}