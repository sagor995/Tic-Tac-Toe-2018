package com.agd.sa.tictactoe2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignIn extends AppCompatActivity {
    private static final String TAG ="GoogleSignIn.this";
    SignInButton signInButton;

    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    public static boolean comeFromOffline=false;


// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        mAuth = FirebaseAuth.getInstance();

        signInButton= (SignInButton) findViewById(R.id.gSignIn);

        dialog=new ProgressDialog(this);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_webs_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(GoogleSignIn.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
        .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()){
                    signIn();
                }else {
                    Toast.makeText(GoogleSignIn.this, getString(R.string.check_wifi_text), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            dialog.setTitle("Google Sign-in");
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                //Toast.makeText(this, "Please Wait...", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Unable to get result, Please Try Again!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
       Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

       AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
       mAuth.signInWithCredential(credential)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // Sign in success, update UI with the signed-in user's information
                           Log.d(TAG, "signInWithCredential:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           sendToTheMainActivity();
                           dialog.dismiss();
                       } else {
                           // If sign in fails, display a message to the user.
                           Log.w(TAG, "signInWithCredential:failure", task.getException());
                           String error=task.getException().toString();
                           Toast.makeText(GoogleSignIn.this, "Not Authenticated: "+error, Toast.LENGTH_SHORT).show();
                          /* Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();*/
                           sendToTheLoginActivity();

                          dialog.dismiss();
                       }

                       // ...
                   }
               });
   }


    private void sendToTheMainActivity() {
        Intent i  =new Intent(this,MenuActivity.class);
        startActivity(i);
        finish();
    }
    private void sendToTheLoginActivity() {
        Intent i  =new Intent(this,GoogleSignIn.class);
        startActivity(i);
        finish();
    }


    public void offLineGame(View view) {
        comeFromOffline=true;
        Intent i  =new Intent(this, SelectionActivity.class);
        startActivity(i);
        finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
