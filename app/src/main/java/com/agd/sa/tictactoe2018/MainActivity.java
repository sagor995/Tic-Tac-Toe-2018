package com.agd.sa.tictactoe2018;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    Button TicTac1,TicTac2,TicTac3,TicTac4,TicTac5,TicTac6,TicTac7,TicTac8,TicTac9,exit;
    ImageView playerOneImage,playerTwoImage;
    TextView drawReault,playerOneName,playerTwoName,playerOnePoint,playerTwoPoint,levelCounter;
    Typeface custom_font;
    int value;

    MediaPlayer ring,level_complete;

    int point1=0,point2=0;
    int lc=0;
    int dr=0;
    int turnCount=1;

    //Timer
    CountDownTimer tCount,dCounter,delay;
    int c=0,d=0;
    boolean matchfound=false;
    boolean row11,row12,row13,row21,row22,row23,col11,col12,col13,col21,col22,col23,cross11,cross12,cross21,cross22;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b =getIntent().getExtras();
        value = Integer.parseInt(b.getString("val"));



        custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.custome_font3));

        levelCounter=(TextView)findViewById(R.id.levelCounter);
        levelCounter.setTypeface(custom_font);

        TicTac1 = (Button) findViewById(R.id.firstTicTac);
        TicTac1.setTypeface(custom_font);

        TicTac2 = (Button) findViewById(R.id.secondTicTac);
        TicTac2.setTypeface(custom_font);

        TicTac3 = (Button) findViewById(R.id.thirdTicTac);
        TicTac3.setTypeface(custom_font);

        TicTac4 = (Button) findViewById(R.id.fourthTicTac);
        TicTac4.setTypeface(custom_font);

        TicTac5 = (Button) findViewById(R.id.fifthTicTac);
        TicTac5.setTypeface(custom_font);

        TicTac6 = (Button) findViewById(R.id.sixthTicTac);
        TicTac6.setTypeface(custom_font);

        TicTac7 = (Button) findViewById(R.id.seventhTicTac);
        TicTac7.setTypeface(custom_font);

        TicTac8 = (Button) findViewById(R.id.eightTicTac);
        TicTac8.setTypeface(custom_font);

        TicTac9 = (Button) findViewById(R.id.nineTicTac);
        TicTac9.setTypeface(custom_font);

        exit=(Button)findViewById(R.id.exitButton);
        exit.setTypeface(custom_font);

        playerOneImage =(ImageView)findViewById(R.id.playerOneImage);
        playerTwoImage =(ImageView)findViewById(R.id.playerTwoImage);

        playerOneName=(TextView)findViewById(R.id.playerOneName);
        playerOneName.setTypeface(custom_font);

        playerTwoName=(TextView)findViewById(R.id.playerTwoName);
        playerTwoName.setTypeface(custom_font);

        playerOnePoint=(TextView)findViewById(R.id.playerOnePoint);
        playerOnePoint.setTypeface(custom_font);

        playerTwoPoint=(TextView)findViewById(R.id.playerTwoPoint);
        playerTwoPoint.setTypeface(custom_font);

        drawReault =(TextView)findViewById(R.id.drawResult);
        drawReault.setTypeface(custom_font);

        if (SelectionActivity.levelA==true){
            levelCounter.setText("0 :"+value);
        }else {
            levelCounter.setText(getString(R.string.target_point)+value);
        }

        ring= MediaPlayer.create(this,R.raw.select);
        level_complete= MediaPlayer.create(this,R.raw.level_complete);

        delay=new CountDownTimer(1500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                d++;
            }

            @Override
            public void onFinish() {
                delay.cancel();
                d=0;

                try {
                    autoPlay();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }


            }
        };


        tCount=new CountDownTimer(3900,300) {
            @Override
            public void onTick(long millisUntilFinished) {
                delay.cancel();
                c++;
                //Toast.makeText(OfflineActivity.this, "Time: "+c, Toast.LENGTH_SHORT).show();
                /*tC.setText("Time: "+c);*/
                setBtwnDisAbled();
                TicTacAnimation();
                playerTwoImage.setBackgroundResource(0);
                playerOneImage.setBackgroundResource(0);
            }
            @Override
            public void onFinish() {
                delay.cancel();
                tCount.cancel();
                c=0;

                if (SelectionActivity.pointA==true){
                    if (value==point1){
                        resultShow(getString(R.string.p_name),getString(R.string.c_name),point1,point2);
                    }else if (value==point2){
                        resultShow(getString(R.string.c_name),getString(R.string.p_name),point2,point1);
                    }else {
                        clearMethod();
                    }
                }else if (SelectionActivity.levelA==true){
                    if (value==lc){
                        if (point2>point1){
                            resultShow(getString(R.string.c_name),getString(R.string.p_name),point2,point1);
                        }else {
                            resultShow(getString(R.string.p_name),getString(R.string.c_name),point1,point2);
                        }
                    }else {
                        clearMethod();
                        levelCounter.setText(lc+" : "+value);
                    }
                }
            }
        };

        dCounter=new CountDownTimer(3900,300) {
            @Override
            public void onTick(long millisUntilFinished) {
                delay.cancel();
                c++;
                //Toast.makeText(OfflineActivity.this, "Time: "+c, Toast.LENGTH_SHORT).show();
                /*tC.setText("Time: "+c);*/
                setBtwnDisAbled();
                DrawAnimation();
                playerTwoImage.setBackgroundResource(0);
                playerOneImage.setBackgroundResource(0);
            }

            @Override
            public void onFinish() {
                delay.cancel();
                dCounter.cancel();
                c=0;
                if (SelectionActivity.levelA==true){
                    if (value==lc){
                        if (point1>point2){
                            resultShow(getString(R.string.p_name),getString(R.string.c_name),point1,point2);
                        }
                        else {
                            resultShow(getString(R.string.c_name),getString(R.string.p_name),point2,point1);
                        }
                    }else {
                        clearMethod();
                        levelCounter.setText(lc+" : "+value);
                    }
                }else {
                    clearMethod();
                }
            }
        };

        playerOneImage.setBackgroundResource(R.drawable.button_style7);
        playerTwoImage.setBackgroundResource(0);

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }
    public void tictacClicked(View view){

        playerTwoImage.setBackgroundResource(R.drawable.button_style7);
        playerOneImage.setBackgroundResource(0);



        Button selectedTicTac=(Button)view;

        int cellID=0;

        switch (selectedTicTac.getId()){
            case R.id.firstTicTac:
                cellID=1;
                break;
            case R.id.secondTicTac:
                cellID=2;
                break;
            case R.id.thirdTicTac:
                cellID=3;
                break;
            case R.id.fourthTicTac:
                cellID=4;
                break;
            case R.id.fifthTicTac:
                cellID=5;
                break;
            case R.id.sixthTicTac:
                cellID=6;
                break;
            case R.id.seventhTicTac:
                cellID=7;
                break;
            case R.id.eightTicTac:
                cellID=8;
                break;
            case R.id.nineTicTac:
                cellID=9;
                break;

        }
        turnCount++;
        playGame(cellID,selectedTicTac);

    }

    int activePlayer=1;//1: player One, 2: Player Two
    ArrayList<Integer> playerOne = new ArrayList<>();  //hold player one data
    ArrayList<Integer> playerTwo = new ArrayList<>();  //hold player two data

    void playGame(int cellID, Button selectedTicTac){
        Log.d("Player:",String.valueOf(cellID));

        if (activePlayer==1){
            if (OptionActivity.soundOn==true) {
                if (!ring.isPlaying()) {
                    ring.start();
                }
                if (ring.isPlaying()) {
                    ring.seekTo(0);
                }
            }
            selectedTicTac.setText(getString(R.string.symbol_x));
            selectedTicTac.setBackgroundResource(R.drawable.player_one_frame);
            playerOne.add(cellID);
            activePlayer=2;
            delay.start();
            //Computer Part
        }
       /* else if (activePlayer==2){
            if (OptionActivity.soundOn==true) {
                if (!ring.isPlaying()) {
                    ring.start();
                }
                if (ring.isPlaying()) {
                    ring.seekTo(0);
                }
            }
            selectedTicTac.setText("O");
            selectedTicTac.setBackgroundResource(R.drawable.player_two_frame);
            playerTwo.add(cellID);
            activePlayer=1;
        }*/


        selectedTicTac.setEnabled(false);//will not be able to select a button after selection
        checkWinner();

    }



    void autoPlay(){
        if (activePlayer==1){
            playerOneImage.setBackgroundResource(R.drawable.button_style7);
            playerTwoImage.setBackgroundResource(0);
        }

        ArrayList<Integer> emptyCells = new ArrayList<>(); //all un seelcted cells
        //Find Empty Cells
        for(int cellID=1;cellID<10;cellID++){
            if (!(playerOne.contains(cellID) || playerTwo.contains(cellID))){
                emptyCells.add(cellID);
            }
        }

        int cellID=0;
        //now we'll choose an random index for the emptycells
        try {
            Random rand = new Random();
            int RandIndex = rand.nextInt(emptyCells.size()-0)+0;
            cellID=emptyCells.get(RandIndex);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Toast.makeText(this, ""+cellID, Toast.LENGTH_SHORT).show();

        Button selectedTicTac;
        switch (cellID){
            case 1:
                selectedTicTac=(Button)findViewById(R.id.firstTicTac);
                break;
            case 2:
                selectedTicTac=(Button)findViewById(R.id.secondTicTac);
                break;
            case 3:
                selectedTicTac=(Button)findViewById(R.id.thirdTicTac);
                break;
            case 4:
                selectedTicTac=(Button)findViewById(R.id.fourthTicTac);
                break;
            case 5:
                selectedTicTac=(Button)findViewById(R.id.fifthTicTac);
                break;
            case 6:
                selectedTicTac=(Button)findViewById(R.id.sixthTicTac);
                break;
            case 7:
                selectedTicTac=(Button)findViewById(R.id.seventhTicTac);
                break;
            case 8:
                selectedTicTac=(Button)findViewById(R.id.eightTicTac);
                break;
            case 9:
                selectedTicTac=(Button)findViewById(R.id.nineTicTac);
                break;
            default:
                selectedTicTac=(Button)findViewById(R.id.firstTicTac);
                break;

        }
        /*playGame(cellID,selectedTicTac);*/
        if (OptionActivity.soundOn==true) {
            if (!ring.isPlaying()) {
                ring.start();
            }
            if (ring.isPlaying()) {
                ring.seekTo(0);
            }
        }
        selectedTicTac.setText(getString(R.string.symbol_o));
        selectedTicTac.setBackgroundResource(R.drawable.player_two_frame);
        playerTwo.add(cellID);
        checkWinner();
        activePlayer=1;
        turnCount++;

        playerOneImage.setBackgroundResource(R.drawable.button_style7);
        playerTwoImage.setBackgroundResource(0);

    }

    Button ani1,ani2,ani3;
    void checkWinner(){
        int winner=-1;





        //first Row
        if (playerOne.contains(1) && playerOne.contains(2) && playerOne.contains(3)){

            winner=1;
            ani1=TicTac1;
            ani2=TicTac2;
            ani3=TicTac3;
            //    animation(1,TicTac1,TicTac2,TicTac3);
            row11=true;

        }else if (playerTwo.contains(1) && playerTwo.contains(2) && playerTwo.contains(3)){
            winner=2;

            ani1=TicTac1;
            ani2=TicTac2;
            ani3=TicTac3;
            //    animation(2,TicTac1,TicTac2,TicTac3);
            row21=true;
        }else if (playerOne.contains(4) && playerOne.contains(5) && playerOne.contains(6)){

            winner=1;
            ani1=TicTac4;
            ani2=TicTac5;
            ani3=TicTac6;
            //    animation(1,TicTac4,TicTac5,TicTac6);
            row12=true;
        }else if (playerTwo.contains(4) && playerTwo.contains(5) && playerTwo.contains(6)){
            winner=2;
            ani1=TicTac4;
            ani2=TicTac5;
            ani3=TicTac6;
            //  animation(2,TicTac4,TicTac5,TicTac6);
            row22=true;

        }else if (playerOne.contains(7) && playerOne.contains(8) && playerOne.contains(9)){

            winner=1;
            ani1=TicTac7;
            ani2=TicTac8;
            ani3=TicTac9;
            //   animation(1,TicTac7,TicTac8,TicTac9);
            row13=true;
        }else if (playerTwo.contains(7) && playerTwo.contains(8) && playerTwo.contains(9)){
            winner=2;
            ani1=TicTac7;
            ani2=TicTac8;
            ani3=TicTac9;
            //   animation(2,TicTac7,TicTac8,TicTac9);
            row23=true;

        }else if (playerOne.contains(1) && playerOne.contains(4) && playerOne.contains(7)){

            winner=1;
            ani1=TicTac1;
            ani2=TicTac4;
            ani3=TicTac7;
            //   animation(1,TicTac1,TicTac4,TicTac7);
            col11=true;
        }else if (playerTwo.contains(1) && playerTwo.contains(4) && playerTwo.contains(7)){
            winner=2;
            ani1=TicTac1;
            ani2=TicTac4;
            ani3=TicTac7;
            //  animation(2,TicTac1,TicTac4,TicTac7);
            col21=true;
        }else if (playerOne.contains(2) && playerOne.contains(5) && playerOne.contains(8)){

            winner=1;
            ani1=TicTac2;
            ani2=TicTac5;
            ani3=TicTac8;
            //   animation(1,TicTac2,TicTac5,TicTac8);
            col12=true;
        }else if (playerTwo.contains(2) && playerTwo.contains(5) && playerTwo.contains(8)){
            winner=2;
            ani1=TicTac2;
            ani2=TicTac5;
            ani3=TicTac8;
            //     animation(2,TicTac2,TicTac5,TicTac8);
            col22=true;
        }else if (playerOne.contains(3) && playerOne.contains(6) && playerOne.contains(9)){

            winner=1;
            ani1=TicTac3;
            ani2=TicTac6;
            ani3=TicTac9;
            //  animation(1,TicTac3,TicTac6,TicTac9);
            col13=true;

        }else if (playerTwo.contains(3) && playerTwo.contains(6) && playerTwo.contains(9)){
            winner=2;
            ani1=TicTac3;
            ani2=TicTac6;
            ani3=TicTac9;
            //   animation(2,TicTac3,TicTac6,TicTac9);
            col23=true;
        }else if (playerOne.contains(1) && playerOne.contains(5) && playerOne.contains(9)){

            winner=1;
            ani1=TicTac1;
            ani2=TicTac5;
            ani3=TicTac9;
            //   animation(1,TicTac1,TicTac5,TicTac9);
            cross11=true;
        }else if (playerTwo.contains(1) && playerTwo.contains(5) && playerTwo.contains(9)){
            winner=2;
            ani1=TicTac1;
            ani2=TicTac5;
            ani3=TicTac9;
            //  animation(2,TicTac1,TicTac5,TicTac9);
            cross21=true;
        }else if (playerOne.contains(3) && playerOne.contains(5) && playerOne.contains(7)){

            winner=1;
            ani1=TicTac3;
            ani2=TicTac5;
            ani3=TicTac7;
            // animation(1,TicTac3,TicTac5,TicTac7);
            cross12=true;
        }else if (playerTwo.contains(3) && playerTwo.contains(5) && playerTwo.contains(7)){
            winner=2;
            ani1=TicTac3;
            ani2=TicTac5;
            ani3=TicTac7;
            //animation(2,TicTac3,TicTac5,TicTac7);
            cross22=true;
        }

        if (winner!=-1){
            //we have winner
            if (winner==1){
                point1+=5;
                lc++;
                tCount.start();
                /*Toast.makeText(this,"Player One Win",Toast.LENGTH_SHORT).show();
                playerOnePoint.setText("Point: "+winner);*/

                drawReault.setText(getString(R.string.draw)+dr);
                playerOnePoint.setText(getString(R.string.user_point)+point1);
                playerTwoPoint.setText(getString(R.string.user_point)+point2);

            }
            else  if (winner==2){
                point2+=5;
                lc++;
                tCount.start();
                drawReault.setText(getString(R.string.draw)+dr);
                playerOnePoint.setText(getString(R.string.user_point)+point1);
                playerTwoPoint.setText(getString(R.string.user_point)+point2);
                /*Toast.makeText(this,"Player Two Win",Toast.LENGTH_SHORT).show();
                playerTwoPoint.setText("Point: "+winner);*/
            }
        }else
        if (winner!=1 && winner!=2 && turnCount>9){
            dr++;
            lc++;
            dCounter.start();

            drawReault.setText(getString(R.string.draw)+dr);
            playerOnePoint.setText(getString(R.string.user_point)+point1);
            playerTwoPoint.setText(getString(R.string.user_point)+point2);
        }



    }


    private void resultShow(String name1, String name2,
                            final int point1, final int point2){
        if (OptionActivity.soundOn==true) {
            if (!level_complete.isPlaying()) {
                level_complete.start();
            }
            if (level_complete.isPlaying()) {
                level_complete.seekTo(0);
            }
        }


        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.win,null);

        TextView winText=(TextView) view1.findViewById(R.id.winText);
        TextView win1=(TextView) view1.findViewById(R.id.win1);
        TextView win2=(TextView) view1.findViewById(R.id.win2);
        Button winOk=(Button)view1.findViewById(R.id.winOk);

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        alert.getWindow().setGravity(Gravity.TOP);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        win1.setText(getString(R.string.one)+name1+getString(R.string.dash)+point1);
        win2.setText(getString(R.string.two)+name2+getString(R.string.dash)+point2);


        winOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMethod();
                turnCount=1;
                lc=0;
                value=0;
                alert.dismiss();
                if (GoogleSignIn.comeFromOffline==true){
                    Intent i = new Intent(MainActivity.this, GoogleSignIn.class);
                    startActivity(i);
                    finish();
                    GoogleSignIn.comeFromOffline=false;
                }else {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    goMenu();

                }
                value=0;

            }
        });
    }

    private void clearMethod(){
        matchfound=false;
        playerOneImage.setBackgroundResource(R.drawable.button_style7);
        playerTwoImage.setBackgroundResource(0);
        turnCount=1;
        TicTac1.setBackgroundResource(R.drawable.button_style2);
        TicTac2.setBackgroundResource(R.drawable.button_style2);
        TicTac3.setBackgroundResource(R.drawable.button_style2);
        TicTac4.setBackgroundResource(R.drawable.button_style2);
        TicTac5.setBackgroundResource(R.drawable.button_style2);
        TicTac6.setBackgroundResource(R.drawable.button_style2);
        TicTac7.setBackgroundResource(R.drawable.button_style2);
        TicTac8.setBackgroundResource(R.drawable.button_style2);
        TicTac9.setBackgroundResource(R.drawable.button_style2);
        TicTac1.setText("");
        TicTac2.setText("");
        TicTac3.setText("");
        TicTac4.setText("");
        TicTac5.setText("");
        TicTac6.setText("");
        TicTac7.setText("");
        TicTac8.setText("");
        TicTac9.setText("");
        TicTac1.setText("");
        TicTac2.setText("");
        TicTac3.setText("");
        TicTac4.setText("");
        TicTac5.setText("");
        TicTac6.setText("");
        TicTac7.setText("");
        TicTac8.setText("");
        TicTac9.setText("");
        TicTac1.setEnabled(true);
        TicTac2.setEnabled(true);
        TicTac3.setEnabled(true);
        TicTac4.setEnabled(true);
        TicTac5.setEnabled(true);
        TicTac6.setEnabled(true);
        TicTac7.setEnabled(true);
        TicTac8.setEnabled(true);
        TicTac9.setEnabled(true);
        TicTac1.setBackgroundResource(R.drawable.button_style2);
        TicTac2.setBackgroundResource(R.drawable.button_style2);
        TicTac3.setBackgroundResource(R.drawable.button_style2);
        TicTac4.setBackgroundResource(R.drawable.button_style2);
        TicTac5.setBackgroundResource(R.drawable.button_style2);
        TicTac6.setBackgroundResource(R.drawable.button_style2);
        TicTac7.setBackgroundResource(R.drawable.button_style2);
        TicTac8.setBackgroundResource(R.drawable.button_style2);
        TicTac9.setBackgroundResource(R.drawable.button_style2);
        playerOne.clear();
        playerTwo.clear();
        activePlayer=1;

        row11=false;row12=false;row13=false;row21=false;row22=false;row23=false;
        col11=false;col12=false;col13=false;col21=false;col22=false;col23=false;
        cross12=false;cross11=false;cross21=false;cross22=false;
        TicTac5.setTextSize((float) 60);
    }

    private void TicTacAnimation() {
        if (row11==true || row12==true || row13 ==true || col11==true
                || col12 == true || col13 == true || cross11==true || cross12==true){
            if (c == 2) {
                animation1(ani1,ani2,ani3);
            } else if (c == 3) {
                animation2(ani1,ani2,ani3);
            } else if (c == 4) {
                animation1(ani1,ani2,ani3);
            } else if (c == 5) {
                animation2(ani1,ani2,ani3);
            } else if (c == 6) {
                animation1(ani1,ani2,ani3);
            } else if (c == 7) {
                animation2(ani1,ani2,ani3);
            } else if (c == 8) {
                animation1(ani1,ani2,ani3);
            } else if (c == 9) {
                animation2(ani1,ani2,ani3);
            } else if (c >= 10 && c < 12) {
                animation1(ani1,ani2,ani3);

            }
        }

        //player2
        if (row21==true || row22==true || row23 ==true || col21==true
                || col22 == true || col23 == true || cross21==true || cross22==true){
            if (c == 2) {
                animation1(ani1,ani2,ani3);
            } else if (c == 3) {
                animation3(ani1,ani2,ani3);
            } else if (c == 4) {
                animation1(ani1,ani2,ani3);
            } else if (c == 5) {
                animation3(ani1,ani2,ani3);
            } else if (c == 6) {
                animation1(ani1,ani2,ani3);
            } else if (c == 7) {
                animation3(ani1,ani2,ani3);
            } else if (c == 8) {
                animation1(ani1,ani2,ani3);
            } else if (c == 9) {
                animation3(ani1,ani2,ani3);
            } else if (c >= 10 && c < 12) {
                animation1(ani1,ani2,ani3);
            }
        }

    }

    void animation1(Button b1,Button b2,Button b3){
        b1.setBackgroundResource(R.drawable.button_style_effect);
        b2.setBackgroundResource(R.drawable.button_style_effect);
        b3.setBackgroundResource(R.drawable.button_style_effect);
    }
    void animation2(Button b1,Button b2,Button b3){
        b1.setBackgroundResource(R.drawable.player_one_frame);
        b1.setText(getString(R.string.symbol_x));
        b2.setBackgroundResource(R.drawable.player_one_frame);
        b2.setText(getString(R.string.symbol_x));
        b3.setBackgroundResource(R.drawable.player_one_frame);
        b3.setText(getString(R.string.symbol_x));
    }
    void animation3(Button b1,Button b2,Button b3){
        b1.setBackgroundResource(R.drawable.player_two_frame);
        b1.setText(getString(R.string.symbol_o));
        b2.setBackgroundResource(R.drawable.player_two_frame);
        b2.setText(getString(R.string.symbol_o));
        b3.setBackgroundResource(R.drawable.player_two_frame);
        b3.setText(getString(R.string.symbol_o));
    }

    void DrawAnimation(){
        TicTac5.setText(getString(R.string.symbol_d));
        TicTac5.setTextSize((float) 30.5);

        if (c == 2) {
            TicTac5.setBackgroundResource(R.drawable.button_style_effect);
        } else if (c == 3) {
            TicTac5.setBackgroundResource(R.drawable.button_style2);
        } else if (c == 4) {
            TicTac5.setBackgroundResource(R.drawable.button_style_effect);
        } else if (c == 5) {
            TicTac5.setBackgroundResource(R.drawable.button_style2);
        } else if (c == 6) {
            TicTac5.setBackgroundResource(R.drawable.button_style_effect);
        } else if (c == 7) {
            TicTac5.setBackgroundResource(R.drawable.button_style2);
        } else if (c == 8) {
            TicTac5.setBackgroundResource(R.drawable.button_style_effect);
        } else if (c == 9) {
            TicTac5.setBackgroundResource(R.drawable.button_style2);
        } else if (c >= 10 && c < 12) {
            TicTac5.setBackgroundResource(R.drawable.button_style_effect);
        }
    }



    @Override
    public void onBackPressed() {

    }

    private void setBtwnDisAbled(){
        TicTac1.setEnabled(false);
        TicTac2.setEnabled(false);
        TicTac3.setEnabled(false);
        TicTac4.setEnabled(false);
        TicTac5.setEnabled(false);
        TicTac6.setEnabled(false);
        TicTac7.setEnabled(false);
        TicTac8.setEnabled(false);
        TicTac9.setEnabled(false);
    }


    private void exitLayout(){
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.exit_layout,null);

        TextView exit=(TextView)view1.findViewById(R.id.exitTitle);
        exit.setTypeface(custom_font);

        Button yesBtwn=(Button)view1.findViewById(R.id.exitYes);
        yesBtwn.setTypeface(custom_font);

        Button noBtwn=(Button)view1.findViewById(R.id.exitNo);
        noBtwn.setTypeface(custom_font);

        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.help);
        builder.setTitle("Quit?");
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yesBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoogleSignIn.comeFromOffline==true){
                    Intent i = new Intent(MainActivity.this, GoogleSignIn.class);
                    startActivity(i);
                    finish();
                    GoogleSignIn.comeFromOffline=false;
                }else {
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        noBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }
    public void exitMethod(View view){
        exitLayout();
    }
    private void goMenu(){
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

}
