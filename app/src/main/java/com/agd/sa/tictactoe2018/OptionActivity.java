package com.agd.sa.tictactoe2018;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class OptionActivity extends AppCompatActivity {
//    TextView tC;
//    CountDownTimer t;
//    int c=0;
Typeface custom_font;
public static boolean soundOn=true;
TextView soundheadText,rateheadText,optionText;
Button sOn,sOff,rate,shareBtwn;
    MediaPlayer ring;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custom_font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.custome_font2));
        setContentView(R.layout.activity_option);
        ring= MediaPlayer.create(this,R.raw.select);

        optionText=(TextView)findViewById(R.id.optionText);
        optionText.setTypeface(custom_font);

        soundheadText=(TextView)findViewById(R.id.soundheadText);
        soundheadText.setTypeface(custom_font);

        rateheadText=(TextView)findViewById(R.id.rateheadText);
        rateheadText.setTypeface(custom_font);


        sOn=(Button)findViewById(R.id.soundOn);
        sOn.setTypeface(custom_font);

        sOff=(Button)findViewById(R.id.soundOff);
        sOff.setTypeface(custom_font);

        rate=(Button)findViewById(R.id.rateNow);
        rate.setTypeface(custom_font);

        shareBtwn=(Button)findViewById(R.id.shareButton);
        shareBtwn.setTypeface(custom_font);

/*        tC=(TextView)findViewById(R.id.timeCount);
        t=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                c++;
                tC.setText("Time: "+c);
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Time Up",Toast.LENGTH_SHORT).show();
                c=0;
            }
        };*/
        sOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ring.isPlaying()) {
                    ring.start();
                }
                if (ring.isPlaying()) {
                    ring.seekTo(0);
                }
                soundOn=true;
            }
        });
        sOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundOn=false;
            }
        });

        mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void goToMenu(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {

        }
        Intent i = new Intent(this,MenuActivity.class);
        startActivity(i);
        finish();

    }



    public void rateMethod(View view) {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getPackageName())));
        }
        finish();
    }

    public void showInfo(View view) {
        helpMessage(getString(R.string.info_game));

    }

/*    @Override
    public void onBackPressed() {

    }

    public void starTime(View view) {
        t.start();
    }

    public void stopTime(View view) {
        c=0;
        tC.setText("Time:"+c);
        t.cancel();
    }

    public void pauseTime(View view) {


        t.cancel();

    }*/

    public void helpMessage(String text1){
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.help_layout,null);

        Button closeBtwn=(Button)view1.findViewById(R.id.closeEnter);
        closeBtwn.setTypeface(custom_font);
        TextView hText=(TextView)view1.findViewById(R.id.helpText);
        hText.setTypeface(custom_font);


        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.help);
        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        hText.setMovementMethod(new ScrollingMovementMethod());
        hText.setText(""+text1);


        closeBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    public void shareitNow(View view) {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        String uri="http://play.google.com/store/apps/details?id="+ getPackageName();//any Url
        i.putExtra(Intent.EXTRA_SUBJECT,"TicTacToe 2018");
        i.putExtra(Intent.EXTRA_TEXT,uri);

        startActivity(Intent.createChooser(i,"Share via "));
    }
}
