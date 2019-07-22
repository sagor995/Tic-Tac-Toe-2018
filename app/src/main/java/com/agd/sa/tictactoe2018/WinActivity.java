package com.agd.sa.tictactoe2018;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WinActivity extends AppCompatActivity {
    Typeface custom_font;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference();
    String MY_PREFS_NAME = "MyPrefsFile";
    TextView winT;
    SharedPreferences settings;
    ArrayList<Long> valueAdd = new ArrayList<Long>();  //hold player one data
    TextView w1,w2;
    MediaPlayer level_complete;
    Button ok;
    String name,SR;
    Long oldS;
    int newS;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);
        settings = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        level_complete= MediaPlayer.create(this,R.raw.level_complete);
        custom_font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.custome_font3));
        Bundle b =getIntent().getExtras();
        name=b.getString("name");
        //oldS=b.getLong("oldValue");
        winT = (TextView)findViewById(R.id.winText);
        winT.setTypeface(custom_font);
        newS=b.getInt("newValue");
        SR=b.getString("SP");
        w1=(TextView)findViewById(R.id.win1);
        w1.setTypeface(custom_font);
        w2=(TextView)findViewById(R.id.win2);
        w2.setVisibility(View.GONE);

        w1.setText(name+" : "+newS);

        ok=(Button)findViewById(R.id.winOk);
        ok.setTypeface(custom_font);

        if (!(SR.equals("cancel") || SR.equals("draw"))){
            if (!level_complete.isPlaying()) {
                level_complete.start();
            }
            if (level_complete.isPlaying()) {
                level_complete.seekTo(0);
            }
        }


        getPointValue(name);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((SR.equals("cancel") || SR.equals("draw"))){
                    Intent i = new Intent(WinActivity.this, MenuActivity.class);
                    i.putExtra("SP", SR);
                    //   i.putExtra("oldValue",s);
                    startActivity(i);
                    finish();
                }else {
                    settings.edit().clear().commit();
                    // Toast.makeText(WinActivity.this, ""+name, Toast.LENGTH_SHORT).show();

                    myRef.child("Players")
                            .child(name).child("Point").setValue((valueAdd.get(0) + 5));
                    Intent i = new Intent(WinActivity.this, MenuActivity.class);
                    i.putExtra("SP", SR);
                    //   i.putExtra("oldValue",s);
                    startActivity(i);
                    finish();
                }
            }
        });



    }

    void setPointValue(){
        try {
            myRef.child("Players").child(name).child("Point").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    long s = (Long) dataSnapshot.getValue();
                    Toast.makeText(WinActivity.this, "Found: " + s, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(WinActivity.this,MenuActivity.class);
                    //   i.putExtra("oldValue",s);
                    startActivity(i);
                    myRef.child("Players")
                            .child(name).child("Point").setValue((s+5));
                    finish();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    void getPointValue(String n){
        myRef.child("Players").child(n).child("Point").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Long old= (Long) dataSnapshot.getValue();
                    valueAdd.add(old);
                    //setValue(old);
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

   public  void setValue(Long olS) {
        oldS=olS;
    }

    @Override
    protected void onPause() {
        super.onPause();
       /* myRef.child("Playing").child(SR).removeValue();*/
    }
}
