package me.cekpedia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

import me.cekpedia.utils.Constants;

public class UtamaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridView;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDb;
    private ImageView imgViewProf;
    private TextView usernama, useremail;
    private SliderLayout sliderLayout;
    private EditText searchView;
    private FirebaseStorage firebaseStorage;
    private String userID;
    private StorageReference storageReference;
    FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    private GoogleApiClient googleApiClient;
    GridLayoutManager layoutManager;

    int[] gambar = {
            R.drawable.ic_masjid,
            R.drawable.ic_wisata,
            R.drawable.ic_penginapan,
            R.drawable.ic_rumah_sakit,
            R.drawable.ic_restoran,
            R.drawable.ic_supermarket,
            R.drawable.ic_sekolah,
            R.drawable.ic_transportasi,
            R.drawable.ic_input_lokasi,
            R.drawable.ic_spbu,
            R.drawable.ic_apotek,
            R.drawable.ic_bidan
    };
    String [] namaMenu = {
            "    Masjid",
            "    Wisata",
            "Penginapan",
            "Rumah Sakit",
            "  Restoran",
            "Supermarket",
            "   Sekolah",
            "Transportasi",
            "Input Lokasi",
            "     SPBU",
            "    Apotek",
            "    Bidan"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        if (mAuth.getCurrentUser() == null){
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            startActivity(new Intent(this, LoginActivity.class));
        }
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
//        mFirebaseUser = mAuth.getCurrentUser();



//        setTitle("Trafinder");
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.getHeaderView(0);
        usernama = navHeaderView.findViewById(R.id.username);
        useremail = navHeaderView.findViewById(R.id.useremail);
        imgViewProf = navHeaderView.findViewById(R.id.imageViewProfil);
//        gridView = (GridView) findViewById(R.id.grid_view);
        ImageAdapter adapter = new ImageAdapter(this, gambar, namaMenu);
        FirebaseUser FBUser = mAuth.getCurrentUser();

        FirebaseDatabase.getInstance().getReference(Constants.USER_KEY).child(FBUser.getEmail().replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            User users = dataSnapshot.getValue(User.class);
                            Glide.with(UtamaActivity.this)
                                    .load(users.getPhotoUrl())
                                    .into(imgViewProf);
                            usernama.setText(users.getUser());
                            useremail.setText(users.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//            this.finish();
//        }
//    }
//    protected void signOut(){
//        mAuth.signOut();
//
//        Auth.GoogleSignInApi.signOut(googleApiClient)
//                .setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//
//                    }
//                });
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profile) {
            Toast.makeText(getApplication(), "Fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_fav) {
            FavouriteFragment fragment = new FavouriteFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_lokasi) {
//            setTitle("ABOUT US");
//            AboutFragment fragment = new AboutFragment();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frameLayout, fragment);
//            fragmentTransaction.commit();

        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplication(), "Fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplication(), "Fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void logoutbn(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UtamaActivity.this);
        builder1.setMessage("Yakin Ingin Keluar?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        dialog.cancel();

                    }
                });
        builder1.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
