package com.agd.sa.tictactoe2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sagor Ahamed on 3/31/2018.
 */

public class SplashActivity extends AppCompatActivity {

    int count=0;
    ImageButton splashImage;
    CountDownTimer splashTimer;
    MediaPlayer splashTone;
    GlideDrawableImageViewTarget imageViewTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage=(ImageButton) findViewById(R.id.splash_image);
        imageViewTarget = new GlideDrawableImageViewTarget(splashImage);





        splashImage.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Glide.with(SplashActivity.this).load(R.raw.splash).into(imageViewTarget);
      /* splashTone=MediaPlayer.create(this,R.raw.splashm2);

        if (!splashTone.isPlaying()) {
            splashTone.start();
        }
        if (splashTone.isPlaying()) {
            splashTone.seekTo(0);
        }*/
        splashTimer=new CountDownTimer(1500,30) {
            @Override
            public void onTick(long millisUntilFinished) {
                count++;
            }

            @Override
            public void onFinish() {
                count=0;
                splashTimer.cancel();

                goToSignIn();

                /*if (isNetworkAvailable()==true){

                }else {
                    Toast.makeText(SplashActivity.this, "Please check your wifi internet connection", Toast.LENGTH_SHORT).show();
//
                }*/

            }
        }.start();

    }





    private void goToSignIn(){
        splashTimer.cancel();
        Intent i= new Intent(getApplicationContext(),GoogleSignIn.class);
        startActivity(i);
        finish();
    }
}
