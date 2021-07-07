package com.piyushjaiswal12.primeflix.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.MobileAds;
import com.piyushjaiswal12.primeflix.InternetConnection;
import com.piyushjaiswal12.primeflix.R;

public class MainActivity extends AppCompatActivity {

    Animation animation;
    TextView textView;
    LottieAnimationView lottieAnimationView;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, initializationStatus -> {
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
 lottieAnimationView = findViewById(R.id.nointernet);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sample);
        textView = findViewById(R.id.title);
        textView.startAnimation(animation);
        if(InternetConnection.isNetworkConnected(getApplicationContext()))
        {
            lottieAnimationView.setVisibility(View.GONE);
        }
        else
        {
            lottieAnimationView.setVisibility(View.VISIBLE);
        }
        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.soft);
        mediaPlayer.start();

        new Handler().postDelayed(() -> {
            Intent intent;
            intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        },3000);
    }
}
