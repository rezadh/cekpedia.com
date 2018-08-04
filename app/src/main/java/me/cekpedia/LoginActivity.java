package me.cekpedia;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.cekpedia.utils.Constants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
        ,GoogleApiClient.OnConnectionFailedListener
    {

    private LinearLayout Prof_Section;
    private Button SignOut;
    private SignInButton SignIn;
    private ImageView Prof_Pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private TextView Name,Email;
    SignInButton googlelogin;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;
    private static final String TAG = LoginActivity.class.getSimpleName();

    Button btn_home;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Name = (TextView)findViewById(R.id.name);
        Email = (TextView)findViewById(R.id.email);
//        Prof_Section = (LinearLayout)findViewById(R.id.prof_section);
//        SignOut = (Button)findViewById(R.id.bn_logout);
//        SignIn = (SignInButton)findViewById(R.id.bn_login);
//        Prof_Pic = (ImageView)findViewById(R.id.prof_pic);
        googlelogin = findViewById(R.id.bn_login);
        googlelogin.setOnClickListener(this);
        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("668210003774-dtag7e2q0sgbfld6rvk8upd6qcslqejl.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        SignIn.setOnClickListener(this);

//        SignOut.setOnClickListener(this);
//        Prof_Section .setVisibility(View.GONE);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    if (BuildConfig.DEBUG) Log.d(TAG, "onAuthStateChanged:signed_in " + mFirebaseUser.getDisplayName());
                    startActivity(new Intent(LoginActivity.this, UtamaActivity.class));
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth != null){
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    startActivity(new Intent(LoginActivity.this, UtamaActivity.class));
                    finish();
                }
            }
        };
//

//        btn_home =(Button)findViewById(R.id.home);
//
//        btn_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            }
//        });
    }
    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) firebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQ_CODE){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                }else{
                    hideProgressDialog();
                }
            }else {
                hideProgressDialog();
            }
        }else {
            hideProgressDialog();
        }
    }
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
////                updateUI(false);
//            }
//        });
//
//    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account){
//        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:complete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:" + task.getException());
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            updateUI(user);
                        }
                        else {
                            String photoUrl = null;
                            if (account.getPhotoUrl() != null){
                                photoUrl = account.getPhotoUrl().toString();
                            }
                            User user = new User(
                                    account.getDisplayName() + " " + account.getFamilyName(),
                                    account.getEmail(),
                                    photoUrl,
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(), ""
                            );
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference userRef = database.getReference(Constants.USER_KEY);
                            userRef.child(account.getEmail().replace(".", ","))
                                    .setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                           startActivity(new Intent(LoginActivity.this, UtamaActivity.class));
                                        }
                                    });
                            if (BuildConfig.DEBUG)Log.v(TAG, "Auth Successful");
                            Toast.makeText(LoginActivity.this, "Sign In Success", Toast.LENGTH_SHORT).show();

                        }
// else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }

                        // ...
                    }
                });
    }
        protected void hideProgressDialog(){
            if(mProgressDialog != null && mProgressDialog.isShowing()){
                mProgressDialog.hide();
            }
        }

        protected void showProgressDialog(){
            if(mProgressDialog == null){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }
            mProgressDialog.show();
        }
        protected void signOut(){
            firebaseAuth.signOut();

            Auth.GoogleSignInApi.signOut(googleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                        }
                    });
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
//            firebaseAuth.removeAuthStateListener(mAuthListener);
        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bn_login:
//                signIn();
//                break;
//            case R.id.bn_logout:
//                signOut();
//                break;
//        }
    }
}
