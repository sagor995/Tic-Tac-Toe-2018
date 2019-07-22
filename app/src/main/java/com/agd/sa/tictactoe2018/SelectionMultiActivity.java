package com.agd.sa.tictactoe2018;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectionMultiActivity extends AppCompatActivity {
    public static boolean opponentActie=false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private ProgressDialog dialog;
    RadioGroup gameType,gameMode;
    RadioButton time,point,level;
    Button startGameBtwn;
    RelativeLayout part3;
    LinearLayout part2;
    public static boolean multiPointA=false,multiLevelA=false;
    TextView targetText;
    EditText friendEmail;
    public static int levelValue;
    public static String uEmail;
    Typeface custom_font;
    String playerSymbol="X";
    String SyntexPlayer="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_multi);
        custom_font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.custome_font2));
        Bundle b =getIntent().getExtras();
        uEmail = b.getString("UE");

        gameMode=(RadioGroup)findViewById(R.id.gameMode);


        gameType=(RadioGroup)findViewById(R.id.gameType);
      //  time=(RadioButton)findViewById(R.id.timeChallenge);
       /* point=(RadioButton)findViewById(R.id.PointChallenge);*/
        level=(RadioButton)findViewById(R.id.levelChallenge);

        part2=(LinearLayout) findViewById(R.id.selectionPart2);
        part3=(RelativeLayout)findViewById(R.id.selectionPart3);

        startGameBtwn=(Button)findViewById(R.id.startMultiGame);
        startGameBtwn.setTypeface(custom_font);

        targetText=(TextView)findViewById(R.id.targetText);
        targetText.setTypeface(custom_font);

        friendEmail=(EditText)findViewById(R.id.fEmail);
        friendEmail.setTypeface(custom_font);

        //levelValue =Integer.parseInt(targtValue.getText().toString());
       /* expandMethod();*/
        dialog=new ProgressDialog(this);
       enterGame();
    }

    public void enterGame(){
        startGameBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!isEmailValid(friendEmail.getText().toString())){
                    Toast.makeText(SelectionMultiActivity.this, getString(R.string.vaild_email_text), Toast.LENGTH_SHORT).show();
                }else {

            /*if (Integer.parseInt(targtValue.getText().toString())>20){
                Toast.makeText(getApplicationContext(),"Maximum 20 level allowed",Toast.LENGTH_SHORT).show();
            }else if(Integer.parseInt(targtValue.getText().toString())<1){
                Toast.makeText(getApplicationContext(),"Maximum 1 level allowed",Toast.LENGTH_SHORT).show();
            }else {*/
                        myRef.child("Players")
                                .child(beforeAt(friendEmail.getText().toString()))
                                .child("Request")
                                .push().setValue(uEmail);
                        // goToMultiGame(value);


                        /*MultiplayerActivity.startMultiplayerGame();*/
                        SyntexPlayer = beforeAt(friendEmail.getText().toString()) + ":" + beforeAt(uEmail);



                        playerSymbol = "X";


                        try {
                            myRef.child("Playing").child("invite").child(SyntexPlayer).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    String value = dataSnapshot.child("Playing").child("invite").child(SyntexPlayer).child("Invite").getValue(String.class);

                                    if (dataSnapshot.hasChild("Invite")){

                                        goToMultiActivity();
                                        myRef.child("Playing")
                                                .child("invite")
                                                .child(SyntexPlayer)
                                                .child("Invite").removeValue();
                                        if (!SelectionMultiActivity.this.isFinishing()){
                                            dialog.dismiss();
                                        }

                                    }else {
                                        dialog.show();
                                        dialog.setTitle("Request Sending!");
                                        dialog.setMessage("Please wait Until Your Friend Accept it....");
                                        dialog.setCanceledOnTouchOutside(true);

                                    }

                                   // checkValue(value);


                                    /*Toast.makeText(MultiplayerActivity.this, ""+value, Toast.LENGTH_SHORT).show();*/
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    dialog.dismiss();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                        /* }*/

                }
            }
        });
    }

    void checkValue(String value){
        if (value.equals("accept")){

        }else {

        }
    }





    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


   /* private void expandMethod(){
        point.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part2.setVisibility(View.VISIBLE);
                part3.setVisibility(View.VISIBLE);
                targetText.setText("Enter Targeted Point");
                multiPointA=true;
                multiLevelA=false;
            }
        });

        level.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part2.setVisibility(View.VISIBLE);
                part3.setVisibility(View.VISIBLE);
                targetText.setText("Enter Targeted Level");
                multiLevelA=true;
                multiPointA=false;
            }
        });

        time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part3.setVisibility(View.VISIBLE);
            }
        });


    }*/

    public void goToGame(View view){

    }


  /*  public void enterMethod() {


        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.find_player, null);

        TextView requestText = (TextView) view1.findViewById(R.id.requestText);
        final EditText requestEmail = (EditText) view1.findViewById(R.id.requestEmail);
        Button sendBtwn = (Button) view1.findViewById(R.id.SendRequest);
        Button closeBtwn = (Button) view1.findViewById(R.id.closeRequest);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




           sendBtwn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*if (multiPointA==true){
                        if (Integer.parseInt(targtValue.getText().toString())>100){
                            Toast.makeText(getApplicationContext(),"Maximum 100 points allowed",Toast.LENGTH_SHORT).show();
                        }else if (Integer.parseInt(targtValue.getText().toString())<5){
                            Toast.makeText(getApplicationContext(),"Minimum 5 point allowed",Toast.LENGTH_SHORT).show();
                        }else {
                            myRef.child("Players")
                                    .child(beforeAt(requestEmail.getText().toString()))
                                    .child("Request")
                                    .push().setValue(uEmail);

                            if (opponentActie==false){

                            }else if(opponentActie==true) {
                               // goToMultiGame(value);
                                startMultiplayerGame(beforeAt(requestEmail.getText().toString())+":"+beforeAt(uEmail));
                                opponentActie=false;
                            }
                            //
                        }
                    }else *//*
                        *//*if (multiLevelA==true){
                        if (Integer.parseInt(targtValue.getText().toString())>20){
                            Toast.makeText(getApplicationContext(),"Maximum 20 level allowed",Toast.LENGTH_SHORT).show();
                        }else if(Integer.parseInt(targtValue.getText().toString())<1){
                            Toast.makeText(getApplicationContext(),"Maximum 1 level allowed",Toast.LENGTH_SHORT).show();
                        }else {
                            myRef.child("Players")
                                    .child(beforeAt(requestEmail.getText().toString()))
                                    .child("Request")
                                    .push().setValue(uEmail);
                               // goToMultiGame(value);
                            myRef.child("Playing")
                                    .child("Available").setValue("appeard");

                            myRef.child("Playing")
                                    .child("Level").setValue(Integer.parseInt(targtValue.getText().toString()));

                            myRef.child("Playing")
                                    .child("Reset").setValue("noOne");

                            myRef.child("Playing")
                                    .child("Draw").setValue(1);
                                *//**//*MultiplayerActivity.startMultiplayerGame();*//**//*
                            SyntexPlayer=beforeAt(requestEmail.getText().toString())+":"+beforeAt(uEmail);
                                playerSymbol="X";
                                goToMultiActivity();
                        }
                    }*//*

                }
            });

        closeBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });
    }*/

    private void goToMultiActivity(){
        Intent i =new Intent(SelectionMultiActivity.this,MultiplayerActivity.class);
        i.putExtra("SP",SyntexPlayer);
        i.putExtra("pEmail",uEmail);
        i.putExtra("pSymbol",playerSymbol);
        startActivity(i);
        finish();
    }


    String beforeAt(String e){
        String[] split = e.split("@");
        return split[0];
    }




    public void menuBack(View view){
        Intent i = new Intent(this,MenuActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MenuActivity.class);
        startActivity(i);
        finish();
    }
}
