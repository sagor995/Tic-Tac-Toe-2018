package com.agd.sa.tictactoe2018;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MultiplayerActivity extends AppCompatActivity {
    //Firebase
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference();

    Button TicTac1,TicTac2,TicTac3,TicTac4,TicTac5,TicTac6,TicTac7,TicTac8,TicTac9,exit;
    ImageView playerOneImage,playerTwoImage;
    TextView drawReault,playerOneName,playerTwoName,playerOnePoint,playerTwoPoint,levelCounter,wN;
    Typeface custom_font;
    int value;

    MediaPlayer ring;

    int point1=0,point2=0;

    int dr=0;
    public static int turnCount=0;
    public static String emailU;
    String pSession,OName;

    public String playerSession="";

    public String playerS,syntexPlayer;

    public boolean gameExitGoToMenu=false;
    String v;
    Long previousScroe;

    CountDownTimer tCount,dCounter;
    int c=0;
    boolean row11,row12,row13,row21,row22,row23,col11,col12,col13,col21,col22,col23,cross11,cross12,cross21,cross22;

    int drawCounter=0;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    public String winnerName,drawName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Bundle b = getIntent().getExtras();

        emailU = b.getString("pEmail");
        playerS = b.getString("pSymbol");
        syntexPlayer = b.getString("SP");


        levelCounter = (TextView) findViewById(R.id.levelCounter);
        wN = (TextView) findViewById(R.id.wNer);

        custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.custome_font3));
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

        playerOneImage = (ImageView) findViewById(R.id.playerOneImage);
        playerTwoImage = (ImageView) findViewById(R.id.playerTwoImage);

        playerOneName = (TextView) findViewById(R.id.playerOneName);
        playerOneName.setTypeface(custom_font);

        playerTwoName = (TextView) findViewById(R.id.playerTwoName);
        playerTwoName.setTypeface(custom_font);

        playerOnePoint = (TextView) findViewById(R.id.playerOnePoint);
        playerOnePoint.setTypeface(custom_font);

        playerTwoPoint = (TextView) findViewById(R.id.playerTwoPoint);
        playerTwoPoint.setTypeface(custom_font);


        drawReault = (TextView) findViewById(R.id.drawResult);
        drawReault.setTypeface(custom_font);


        ring = MediaPlayer.create(this, R.raw.select);


        // Obtain the FirebaseAnalytics instance.

        //getLevelValue();

        playerOneName.setText(beforeAt(emailU));

        try {
            if (beforeAt(emailU).equals(afterCol1(syntexPlayer))) {
                OName = "" + afterCol2(syntexPlayer);
            } else {
                OName = "" + afterCol1(syntexPlayer);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        playerTwoName.setText("" + OName);

        startMultiplayerGame();

        drawReault.setText(getString(R.string.draw) + dr);
        playerOnePoint.setText(getString(R.string.user_point) + point1);
        playerTwoPoint.setText(getString(R.string.user_point) + point2);

        //if one user quit at any time
        userQuit();

        tCount = new CountDownTimer(4500, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
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
                tCount.cancel();
                c = 0;
                getResetValue();
            }
        };

        dCounter = new CountDownTimer(4500, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
                c++;

                DrawAnimation();
                setBtwnDisAbled();
                playerTwoImage.setBackgroundResource(0);
                playerOneImage.setBackgroundResource(0);
            }

            @Override
            public void onFinish() {
                tCount.cancel();
                c = 0;
                getDrawValue();
            }


        };

        playerOneImage.setBackgroundResource(R.drawable.button_style7);
        playerTwoImage.setBackgroundResource(R.drawable.button_style7);

    }

    void getResetValue(){
                myRef.child("Playing").child(syntexPlayer).child("Reset").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String v = (String) dataSnapshot.getValue();

                                if (beforeAt(emailU).equals(afterCol1(syntexPlayer))) {
                                    OName =afterCol2(syntexPlayer);
                                } else {
                                    OName =afterCol1(syntexPlayer);
                                }

                            editor.putString("winnerName", v);
                            editor.commit();
                            winnerName = prefs.getString("winnerName", "No name defined");//"No name defined" is the default value.


                                if (winnerName.equals(beforeAt(emailU))) {

                                    Intent i = new Intent(MultiplayerActivity.this, WinActivity.class);
                                    //   i.putExtra("oldValue",s);
                                    i.putExtra("newValue", 5);
                                    i.putExtra("name", winnerName);
                                    i.putExtra("SP", syntexPlayer);
                                    clearMethod();
                                    startActivity(i);
                                    finish();
                                } else if (winnerName.equals(OName)) {
                                    Intent i = new Intent(MultiplayerActivity.this, WinActivity.class);
                                    //   i.putExtra("oldValue",s);
                                    i.putExtra("newValue", 5);
                                    i.putExtra("name", winnerName);
                                    i.putExtra("SP", syntexPlayer);
                                    clearMethod();
                                    startActivity(i);
                                    finish();
                                }
                                drawCounter=0;
                            //myRef.child("Playing").child(syntexPlayer).removeValue();
                                /*else if (v.toString().equals("draw")){
                                    Intent i = new Intent(MultiplayerActivity.this, WinActivity.class);
                                    //   i.putExtra("oldValue",s);
                                    i.putExtra("newValue", 0);
                                    i.putExtra("name", "Result:\n Draw");
                                    i.putExtra("SP", "draw");

                                    startActivity(i);
                                    finish();
                                }*/

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        FirebaseCrash.log("Activity created"+databaseError);
                    }
                });

    }

    void getDrawValue(){
        myRef.child("Playing").child(syntexPlayer).child("Reset").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String v = (String) dataSnapshot.getValue();

                    editor.putString("drawName", v);
                    editor.commit();
                    drawName = prefs.getString("drawName", "No name defined");//"No name defined" is the default value.
                                 if (drawName.toString().equals("draw")){
                                    Intent i = new Intent(MultiplayerActivity.this, WinActivity.class);
                                    //   i.putExtra("oldValue",s);
                                    i.putExtra("newValue", 0);
                                    i.putExtra("name", "Result:\n Draw");
                                    i.putExtra("SP", "draw");
                                    startActivity(i);
                                    finish();
                                }
                    drawCounter=0;
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.log("Activity created"+databaseError);
            }
        });
    }

    public void startMultiplayerGame(){
        playerSession=syntexPlayer;
        myRef.child("Playing").child(syntexPlayer).removeValue();

        //Reading
        myRef.child("Playing").child(syntexPlayer)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //FirebaseCrash.log("Activity created");
                try {
                    playerOne.clear();
                    playerTwo.clear();
                    activePlayer=2; //this is just default value
                    HashMap<String,Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    //if a request is received
                    if (td!=null){
                        String values;
                        for (String key:td.keySet()){
                            values=(String) td.get(key);
                            if (!values.equals(beforeAt(emailU)))
                                activePlayer=playerS=="X"?1:2;
                            else
                                activePlayer=playerS=="X"?2:1;
                            String[] splitID=key.split(":");
                            autoPlay(Integer.parseInt(splitID[1]));

                        }
                    }
                    drawCounter++;

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void tictacClicked(View view){
       /* Toast.makeText(MultiplayerActivity.this, "Draw"+drawCounter, Toast.LENGTH_SHORT).show();*/
        if (activePlayer==2){
           /* playerOneImage.setBackgroundResource(R.drawable.button_style7);
            playerTwoImage.setBackgroundResource(0);*/
        }
        if (activePlayer==1){
           /* playerTwoImage.setBackgroundResource(R.drawable.button_style7);
            playerOneImage.setBackgroundResource(0);*/
        }
       /* myRef.child("Playing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.child("Available").exists()) {
                    *//*Toast.makeText(MultiplayerActivity.this, getString(R.string.opponent_quit_text), Toast.LENGTH_SHORT).show();*//*
                    Intent i = new Intent(MultiplayerActivity.this,MenuActivity.class);
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
                    //game not started yet
                if (playerSession.length()<=0)
                    return;

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
                myRef.child("Playing").child(playerSession).child("CellID:"+cellID).setValue(beforeAt(emailU));
       // Toast.makeText(MultiplayerActivity.this, "drawCount: "+drawCounter, Toast.LENGTH_SHORT).show();
       /* Toast.makeText(MultiplayerActivity.this, "Turn: "+turnCount, Toast.LENGTH_SHORT).show();*/

    }

    public static int activePlayer=1;//1: player One, 2: Player Two
    public static ArrayList<Integer> playerOne = new ArrayList<>();  //hold player one data
    public static  ArrayList<Integer> playerTwo = new ArrayList<>();  //hold player two data

    void playGame(int cellID, Button selectedTicTac){

        Log.d("Player:",String.valueOf(cellID));

        if (activePlayer==1){
            if (!ring.isPlaying()) {
                ring.start();
            }
            if (ring.isPlaying()) {
               ring.seekTo(0);
            }

            selectedTicTac.setText("X");
            selectedTicTac.setBackgroundResource(R.drawable.player_one_frame);
            playerOne.add(cellID);

        }else if (activePlayer==2){
            if (!ring.isPlaying()) {
                ring.start();
            }
            if (ring.isPlaying()) {
                ring.seekTo(0);
            }

            selectedTicTac.setText("O");
            selectedTicTac.setBackgroundResource(R.drawable.player_two_frame);
            playerTwo.add(cellID);

        }
        selectedTicTac.setEnabled(false);//will not be able to select a button after selection
        checkWinner(selectedTicTac);
    }
    Button ani1,ani2,ani3;
    void checkWinner(Button selectedTicTac){

        if (playerOne.contains(1) && playerOne.contains(2) && playerOne.contains(3)){
            doReset(beforeAt(emailU));
            //Toast.makeText(this, ""+getRValue(), Toast.LENGTH_SHORT).show();
            ani1=TicTac1;
            ani2=TicTac2;
            ani3=TicTac3;
            //    animation(1,TicTac1,TicTac2,TicTac3);
            row11=true;
        }
        if (playerTwo.contains(1) && playerTwo.contains(2) && playerTwo.contains(3)){
            doReset(OName);
            ani1=TicTac1;
            ani2=TicTac2;
            ani3=TicTac3;
            //    animation(2,TicTac1,TicTac2,TicTac3);
            row21=true;
        }

        //second Row

        if (playerOne.contains(4) && playerOne.contains(5) && playerOne.contains(6)){
            doReset(beforeAt(emailU));
            ani1=TicTac4;
            ani2=TicTac5;
            ani3=TicTac6;
            //    animation(1,TicTac4,TicTac5,TicTac6);
            row12=true;
        }
        if (playerTwo.contains(4) && playerTwo.contains(5) && playerTwo.contains(6)){
            doReset(OName);
            ani1=TicTac4;
            ani2=TicTac5;
            ani3=TicTac6;
            //  animation(2,TicTac4,TicTac5,TicTac6);
            row22=true;
        }
            //third Row
        if (playerOne.contains(7) && playerOne.contains(8) && playerOne.contains(9)){
            doReset(beforeAt(emailU));
            ani1=TicTac7;
            ani2=TicTac8;
            ani3=TicTac9;
            //   animation(1,TicTac7,TicTac8,TicTac9);
            row13=true;
        }
        if (playerTwo.contains(7) && playerTwo.contains(8) && playerTwo.contains(9)){
            doReset(OName);
            ani1=TicTac7;
            ani2=TicTac8;
            ani3=TicTac9;
            //   animation(2,TicTac7,TicTac8,TicTac9);
            row23=true;
        }
            //first col
        if (playerOne.contains(1) && playerOne.contains(4) && playerOne.contains(7)) {
            doReset(beforeAt(emailU));
            ani1=TicTac1;
            ani2=TicTac4;
            ani3=TicTac7;
            //   animation(1,TicTac1,TicTac4,TicTac7);
            col11=true;
        }
        if (playerTwo.contains(1) && playerTwo.contains(4) && playerTwo.contains(7)){
            doReset(OName);
            ani1=TicTac1;
            ani2=TicTac4;
            ani3=TicTac7;
            //  animation(2,TicTac1,TicTac4,TicTac7);
            col21=true;
        }
            //second col
        if (playerOne.contains(2) && playerOne.contains(5) && playerOne.contains(8)){
            doReset(beforeAt(emailU));
            ani1=TicTac2;
            ani2=TicTac5;
            ani3=TicTac8;
            //   animation(1,TicTac2,TicTac5,TicTac8);
            col12=true;

        }
        if (playerTwo.contains(2) && playerTwo.contains(5) && playerTwo.contains(8)){
            doReset(OName);
            ani1=TicTac2;
            ani2=TicTac5;
            ani3=TicTac8;
            //     animation(2,TicTac2,TicTac5,TicTac8);
            col22=true;

        }

            //third third
        if (playerOne.contains(3) && playerOne.contains(6) && playerOne.contains(9)){
            doReset(beforeAt(emailU));
            ani1=TicTac3;
            ani2=TicTac6;
            ani3=TicTac9;
            //  animation(1,TicTac3,TicTac6,TicTac9);
            col13=true;

        }
        if (playerTwo.contains(3) && playerTwo.contains(6) && playerTwo.contains(9)){
            doReset(OName);
            ani1=TicTac3;
            ani2=TicTac6;
            ani3=TicTac9;
            //   animation(2,TicTac3,TicTac6,TicTac9);
            col23=true;

        }

            ////first corner
        if (playerOne.contains(1) && playerOne.contains(5) && playerOne.contains(9)){
            doReset(beforeAt(emailU));
            ani1=TicTac1;
            ani2=TicTac5;
            ani3=TicTac9;
            //   animation(1,TicTac1,TicTac5,TicTac9);
            cross11=true;

        }
        if (playerTwo.contains(1) && playerTwo.contains(5) && playerTwo.contains(9)){
              doReset(OName);
            ani1=TicTac1;
            ani2=TicTac5;
            ani3=TicTac9;
            //  animation(2,TicTac1,TicTac5,TicTac9);
            cross21=true;
        }
            //Second Corner
        if (playerOne.contains(3) && playerOne.contains(5) && playerOne.contains(7)){
            doReset(beforeAt(emailU));
            ani1=TicTac3;
            ani2=TicTac5;
            ani3=TicTac7;
            // animation(1,TicTac3,TicTac5,TicTac7);
            cross12=true;
        }
        if (playerTwo.contains(3) && playerTwo.contains(5) && playerTwo.contains(7)){
            doReset(OName);
            ani1=TicTac3;
            ani2=TicTac5;
            ani3=TicTac7;
            //animation(2,TicTac3,TicTac5,TicTac7);
            cross22=true;
        }

        if (drawCounter>=9 && !(row11 || row12 || row13 || row21 || row22 || row23 || col11 || col12 || col13 || col21 || col22 || col23
        || cross11 || cross12 || cross21 || cross22)){
                doDraw("draw");

        }
    }

    public void doReset(String value){
        myRef.child("Playing").child(syntexPlayer).child("Reset").setValue(value);
       // tCount.start();
       tCount.start();

    }

    public void doDraw(String value){
        myRef.child("Playing").child(syntexPlayer).child("Reset").setValue(value);
        // tCount.start();
        dCounter.start();

    }
    public void autoPlay(int cellID){
       // turnCount++;
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
        playGame(cellID,selectedTicTac);
    }

    private void clearMethod(){
        turnCount=0;
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
        row11=false;row12=false;row13=false;row21=false;row22=false;row23=false;
        col11=false;col12=false;col13=false;col21=false;col22=false;col23=false;
        cross12=false;cross11=false;cross21=false;cross22=false;
        TicTac5.setTextSize((float) 60);
        //myRef.child("Playing").child(syntexPlayer).child("Reset").removeValue();
        TicTac1.setText("");
        TicTac2.setText("");
        TicTac3.setText("");
        TicTac4.setText("");
        TicTac5.setText("");
        TicTac6.setText("");
        TicTac7.setText("");
        TicTac8.setText("");
        TicTac9.setText("");
    }
    @Override
    public void onBackPressed() {

    }

    public static String beforeAt(String e){
        String[] split = e.split("@");
        return split[0];
    }
    String afterCol1(String e){
        String[] split = e.split(":");
        return split[0];
    }
    String afterCol2(String e){
        String[] split = e.split(":");
        return split[1];
    }

    public void exitMethod(View view){
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.exit_layout,null);

        TextView exit=(TextView)view1.findViewById(R.id.exitTitle);
        exit.setTypeface(custom_font);

        Button yesBtwn=(Button)view1.findViewById(R.id.exitYes);
        yesBtwn.setTypeface(custom_font);

        Button noBtwn=(Button)view1.findViewById(R.id.exitNo);
        noBtwn.setTypeface(custom_font);

        final AlertDialog.Builder builder=new AlertDialog.Builder(MultiplayerActivity.this);
        builder.setIcon(R.drawable.help);
        builder.setTitle("Exit?");

        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yesBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Playing")
                        .child("Quit")
                        .child(syntexPlayer)
                        .child("quit").removeValue();
                Intent i = new Intent(MultiplayerActivity.this,WinActivity.class);
                i.putExtra("newValue",0);
                i.putExtra("name","Result:\n Game Canceled");
                startActivity(i);
                finish();


            }
        });

        noBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    private void userQuit(){
        myRef.child("Playing")
                .child("Quit")
                .child(syntexPlayer).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.hasChild("quit")){
                    //Toast.makeText(MultiplayerActivity.this, getString(R.string.opponent_quit_text), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MultiplayerActivity.this,WinActivity.class);
                    i.putExtra("newValue",0);
                    i.putExtra("name","Result:\n Game Canceled");
                    i.putExtra("SP", "cancel");
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        b1.setText("X");
        b2.setBackgroundResource(R.drawable.player_one_frame);
        b2.setText("X");
        b3.setBackgroundResource(R.drawable.player_one_frame);
        b3.setText("X");
    }
    void animation3(Button b1,Button b2,Button b3){
        b1.setBackgroundResource(R.drawable.player_two_frame);
        b1.setText("O");
        b2.setBackgroundResource(R.drawable.player_two_frame);
        b2.setText("O");
        b3.setBackgroundResource(R.drawable.player_two_frame);
        b3.setText("O");
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
    protected void onPause() {
        super.onPause();

    }
}
