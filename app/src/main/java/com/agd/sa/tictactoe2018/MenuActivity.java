package com.agd.sa.tictactoe2018;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {
    Button startBtwn,multiBtwn,optionBtwn,exitBtwn,signOutNow;
    TextView sH,mH,oH;
    TextView menuHeader;
    Typeface custom_font;
    TextView profilePoint,profileName;


    public static boolean requestCanceled=false;
    private static final int RC_SIGN_IN = 123;
    public String mUsername,mUserEmail,uId;
    private int mPoint=0;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int REQUEST_INVITE = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    String playerSymbol="X";
    String SyntexPlayer="";

    /*Ads*/
    private AdView mAdView;

    long score;

    String sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        /*nwe();*/

        custom_font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.custome_font2));

        startBtwn=(Button)findViewById(R.id.start_btwn);
        startBtwn.setTypeface(custom_font);

        multiBtwn=(Button)findViewById(R.id.multi_btwn);
        multiBtwn.setTypeface(custom_font);

       optionBtwn=(Button)findViewById(R.id.option_btwn);
        optionBtwn.setTypeface(custom_font);

        exitBtwn=(Button)findViewById(R.id.exit_btwn);
        exitBtwn.setTypeface(custom_font);

        signOutNow=(Button)findViewById(R.id.signoutNow);
        signOutNow.setTypeface(custom_font);

        profileName=(TextView)findViewById(R.id.profileName);
        profileName.setTypeface(custom_font);

        profilePoint=(TextView)findViewById(R.id.profilePoint);
        profilePoint.setTypeface(custom_font);

        menuHeader=(TextView)findViewById(R.id.menuHeader);
        menuHeader.setTypeface(custom_font);

        sH=(TextView)findViewById(R.id.start_h);


        mH=(TextView)findViewById(R.id.multi_h);


        oH=(TextView)findViewById(R.id.option_h);






        /*myRef.child("Players")
                .child(beforeAt(user.getEmail().toString())).child("Point").setValue(10);*/


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser!=null){
                    //sign in
                    OnSignedInInitialize(mUser.getDisplayName(),mUser.getEmail(),mUser.getUid());
                    myRef.child("Players")
                            .child(beforeAt(mUserEmail))
                            .child("Request")
                            .setValue(mUser.getUid());

                    checkIfPointExist(beforeAt(mUser.getEmail().toString()));


                    getPointValue(mUser.getEmail());
                    //Receive player Request
                    incomingRequest();
                }else {
                    //sign out
                    onSignedOutCleanUp();
                }
            }
        };


        signoutMethod();



        FirebaseDynamicLink();

        mAdView = (AdView) findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




    }

    void getPointValue(String email){
        try {
            myRef.child("Players").child(beforeAt(email)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        if (ds.getKey().equals("Point")){
                            profilePoint.setText("Point: "+ds.getValue());
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    void  incomingRequest(){
        // Read from the database
        myRef.child("Players")
                .child(beforeAt(mUserEmail))
                .child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //FirebaseCrash.log("Activity created");
                try {
                    HashMap<String,Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    //if a request is received
                    if (td!=null){
                        String values;
                        for (String key:td.keySet()){
                            values=(String) td.get(key);
                            Log.d("User Request",values);

                            myRef.child("Players")
                                    .child(beforeAt(mUserEmail))
                                    .child("Request").setValue(uId);
                            receiveRequestLayout(values);
                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void receiveRequestLayout(final String ReceiveText1){
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.receive_request, null);

        TextView receiveText1 = (TextView) view1.findViewById(R.id.receiveText1);
        receiveText1.setTypeface(custom_font);
        TextView receiveText2 = (TextView) view1.findViewById(R.id.receiveText2);
        receiveText2.setTypeface(custom_font);
        Button receiveOk = (Button) view1.findViewById(R.id.receiveOk);
        receiveOk.setTypeface(custom_font);
        Button receiveCancel = (Button) view1.findViewById(R.id.receiveCancel);
        receiveCancel.setTypeface(custom_font);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.TOP);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        receiveText2.setText(""+ReceiveText1);

        receiveOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child("Players")
                        .child(beforeAt(mUserEmail))
                        .child("Request")
                        .push().setValue(ReceiveText1);



                //MultiplayerActivity.startMultiplayerGame();
                SyntexPlayer=beforeAt(mUserEmail)+":"+beforeAt(ReceiveText1.toString());

                myRef.child("Playing")
                        .child("invite")
                        .child(SyntexPlayer)
                        .child("Invite").setValue("accept");

                myRef.child("Playing")
                        .child("Quit")
                        .child(SyntexPlayer)
                        .child("quit").setValue("accept");




                playerSymbol="O";
                goToMultiActivity();
            }
        });
        receiveCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();

            }
        });
    }

    private void goToMultiActivity(){
        Intent i =new Intent(MenuActivity.this,MultiplayerActivity.class);
        i.putExtra("SP",SyntexPlayer);
        i.putExtra("pEmail",mUserEmail);
        i.putExtra("pSymbol",playerSymbol);
        startActivity(i);
        finish();
    }
    String afterAt(String e){
        String[] split = e.split("@");
        return split[1];
    }
    String beforeAt(String e){
        String[] split = e.split("@");
        return split[0];
    }

    public void startGame(View view){
        Intent i = new Intent(this,SelectionActivity.class);
        startActivity(i);
        finish();
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

        final AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
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
                System.exit(0);
            }
        });

        noBtwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }



    public void goToOption(View view) {
        Intent i = new Intent(this,OptionActivity.class);
        startActivity(i);
        finish();
    }

    public void goToMultiSelection(View view) {
            Intent i = new Intent(this, SelectionMultiActivity.class);
            i.putExtra("UE", mUserEmail);
            startActivity(i);
            finish();
    }
    String v;






    @Override
    public void onBackPressed() {

    }

    public void inviteNow(View view) {

        onInviteClicked();
    }
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MenuActivity", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Hope to see you again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("MenuAcitivity", "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                Toast.makeText(this, "Sending invitations failed", Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }
    }


    private void OnSignedInInitialize(String displayName, String userEmail, String uid) {

        mUsername = displayName;
        mUserEmail=userEmail;
        uId = uid;
        profileName.setText("Hi! "+displayName);
    }

    private void onSignedOutCleanUp() {
        mUsername = "Player";
    }

    /*@Override
    public void onStart() {
        super.onStart();
       mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /*@Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
           mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
    private void signoutMethod() {
        signOutNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MenuActivity.this);
                Intent i = new Intent(MenuActivity.this,GoogleSignIn.class);
                startActivity(i);
                finish();
                mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            }
        });

    }





    /*public void profile_open(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.profile_layout,null);

        ImageView profileImage=(ImageView)view1.findViewById(R.id.profile_image);
        TextView profileName=(TextView)view1.findViewById(R.id.profile_name);
        TextView profilePoint=(TextView)view1.findViewById(R.id.profile_point);

        final AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        builder.setTitle("User Profile!");

        profileName.setText("Name: "+mUsername);
        profilePoint.setText("Point: "+mPoint);

        builder.setView(view1);
        builder.setCancelable(true);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.TOP);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }*/

    public void FirebaseDynamicLink(){
        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d("MenuActivity", "getInvitation: no data");
                            return;
                        }

                        // Get the deep link
                        Uri deepLink = data.getLink();

                        // Extract invite
                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                        if (invite != null) {
                            String invitationId = invite.getInvitationId();
                        }

                        // Handle the deep link
                        // [START_EXCLUDE]
                        Log.d("MenuActivity", "deepLink:" + deepLink);
                        if (deepLink != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setPackage(getPackageName());
                            intent.setData(deepLink);

                            startActivity(intent);
                        }
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MenuActivity", "getDynamicLink:onFailure", e);
                    }
                });
    }


    public void oHelp(View view) {
        helpMessage(getString(R.string.option_help));

    }



    public void multiHelp(View view) {
        helpMessage(getString(R.string.multi_help));
    }

    public void startHelp(View view) {
        helpMessage(getString(R.string.start_help));
    }

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

    /*void nwe(){
        try {
            myRef.child("Players").child("shekhsagor28").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    score= (Long) dataSnapshot.child("Point").getValue();
                    setPreviousScore(score,"shekhsagor28");
                  Log.d("TAG", String.valueOf(score));
                  br
                    //levelCounter.setText(""+previousScroe);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    void setPreviousScore(Long s,String email){
       score=s;
    }*/


    void checkIfPointExist(final String n){
        myRef.child("Players").child(n).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("Point")){
                    myRef.child("Players")
                            .child(beforeAt(n)).child("Point").setValue(10);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.log("Activity created"+databaseError);
            }
        });
    }


}
