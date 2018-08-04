package me.cekpedia;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

import me.cekpedia.models.ImageUpload;
import me.cekpedia.utils.Constants;

public class SubMenuActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;
    private String namaMenu, title, address, nmphone, nomortelp, UserID, favorit, favorit1, favorit2, url;
    private ImageButton info, MyLoc, RateUs, Telp, Sent, info1;
    private Button navigasi;
    Uri openNavigation;
    private ImageView imgMenu, fav, favfull;
    private double kordinatMaps1, kordinatMaps2;
    private FirebaseDatabase mDb;
    private TextView judul, alamat, nomor;
    private GoogleMap m_map;
    MapView mMapView;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    DatabaseReference database, mDatabase;
    ProgressDialog progressDialog;
    private String namaSub;
    public static final String FB_STORAGE_PATH = "images/";
    public static final String FB_DATABASE_PATH = "cekpedia";
    FirebaseAuth firebaseAuth;
    ListView listView;
    private RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private List<ImageUpload> imgList;
    GoogleSignInAccount account;
    DatabaseReference mDatabaseRef;
    FirebaseUser FBUser;
    String[] FavoriteNameLike;
    int FavoriteNameSize;
    boolean FavoriteNameLikeIsEmpty;
    private int merchantNameLikeSize;
    private boolean isFavorite;
    private String favoritTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        info = findViewById(R.id.imageButtonInfo);
        info1 = findViewById(R.id.imageButtonInfo1);
        RateUs = findViewById(R.id.imageButtonRateUs);
        Telp = findViewById(R.id.imageButtonTelp);
        Sent = findViewById(R.id.imageButtonSent);
        imgMenu = findViewById(R.id.gambarmenu);
//        mMapView = findViewById(R.id.map);
        fav = findViewById(R.id.imgfav);
//        favfull = findViewById(R.id.imgfavmark);
        judul = findViewById(R.id.tvjudul);
        alamat = findViewById(R.id.tvalamat);
        nomor = findViewById(R.id.tvphonenumber);
        navigasi = findViewById(R.id.buttonNavigation);
        MyLoc = findViewById(R.id.imageButtonPos);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        //creates a storage reference
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FBUser = firebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.USER_KEY);
        UserID = firebaseAuth.getCurrentUser().getUid();
//        fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        final FirebaseDatabase FBfav = FirebaseDatabase.getInstance();
        FavoriteNameLike = new String[50];
        FavoriteNameSize = 0;
        FavoriteNameLikeIsEmpty = false;

        mDb = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        namaMenu = i.getStringExtra("JUDUL");
        namaSub = i.getStringExtra("SUB");
        nmphone = i.getStringExtra("NUMBER");

        Bundle bundle = new Bundle();
        bundle.putString("SUB", "SUB");
// set Fragmentclass Arguments
        FavouriteFragment fragobj = new FavouriteFragment();
        fragobj.setArguments(bundle);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judul.setVisibility(View.VISIBLE);
                alamat.setVisibility(View.VISIBLE);
                info1.setVisibility(View.VISIBLE);
                nomor.setVisibility(View.VISIBLE);
                fav.setVisibility(View.VISIBLE);
                imgMenu.setVisibility(View.INVISIBLE);
                info.setVisibility(View.INVISIBLE);

            }
        });
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judul.setVisibility(View.INVISIBLE);
                alamat.setVisibility(View.INVISIBLE);
                info1.setVisibility(View.INVISIBLE);
                nomor.setVisibility(View.INVISIBLE);
                fav.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);
                imgMenu.setVisibility(View.VISIBLE);
            }
        });

//        mMapView.onCreate(savedInstanceState);
//        mMapView.onResume(); // needed to get the map to display immediately
//        MapsInitializer.initialize(this.getApplicationContext());
        RateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SubMenuActivity.this, "Fitur ini masih dalam pengembangan", Toast.LENGTH_SHORT).show();
            }
        });
        Sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SubMenuActivity.this, "Fitur ini masih dalam pengembangan", Toast.LENGTH_SHORT).show();
            }
        });
//        m_map.getUiSettings().setZoomControlsEnabled(true);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
//            return;
//        }
//        m_map.setMyLocationEnabled(true);


        final DatabaseReference submenu = mDb.getReference("cekpedia").child("cekpediaItem").child(namaSub).child(namaMenu);
        final DatabaseReference submenusub = mDb.getReference(Constants.USER_KEY).child(FBUser.getEmail().replace(".", ","));
        final DatabaseReference submenufav = mDb.getReference(Constants.USER_KEY).child(FBUser.getEmail().replace(".", ","));


        submenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
                //String s = detailMenu.get("lokasi").toString();
                kordinatMaps1 = Double.parseDouble(detailMenu.get("longi").toString());
                kordinatMaps2 = Double.parseDouble(detailMenu.get("lang").toString());

                Glide.with(getBaseContext())
                        .load(detailMenu.get("url").toString())
                        .into(imgMenu);

                title = detailMenu.get("name").toString();
                address = detailMenu.get("lokasi").toString();
                nomortelp = detailMenu.get("number").toString();
                url = detailMenu.get("url").toString();
                LOADFavorite();
                judul.setText(title);
                alamat.setText(address);
                nomor.setText(nomortelp);

                Telp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + nomortelp));
                        startActivity(intent);
                    }
                });
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    //                    @SuppressLint("MissingPermission")

                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        m_map = googleMap;
                        final LatLng klinik = new LatLng(kordinatMaps1, kordinatMaps2);
                        m_map.addMarker(new MarkerOptions().position(klinik).title(namaMenu));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(klinik).zoom(17).build();
                        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        m_map.setMyLocationEnabled(true);
                        MyLoc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                m_map.addMarker(new MarkerOptions().position(klinik).title(namaMenu));
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(klinik).zoom(17).build();
                                m_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });
                        navigasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String googleMaps = "com.google.android.apps.maps";
                                openNavigation = Uri.parse("google.navigation:q=" + address);
                                Intent intent = new Intent(Intent.ACTION_VIEW, openNavigation);
                                intent.setPackage(googleMaps);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite){
                    if (favorit.equals("")){
                        submenufav.child("favourite").setValue("/" + namaMenu + "/");
                        favorit = "/" + namaMenu + "/";
//                        Toast.makeText(SubMenuActivity.this, "1", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        submenufav.child("favourite").setValue(favorit + namaMenu + "/");
                        favorit = favorit + namaMenu + "/";
//                        Toast.makeText(SubMenuActivity.this, "2", Toast.LENGTH_SHORT).show();
                    }
                    fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    isFavorite=true;
                    Toast.makeText(SubMenuActivity.this, "Add To Favorite", Toast.LENGTH_SHORT).show();
                    merchantNameLikeSize=0;
                    while (favorit.length()>1){
                        favorit = favorit.substring(1);
                        FavoriteNameLike[merchantNameLikeSize] = favorit.substring(0, favorit.indexOf("/"));
                        favorit = favorit.substring(favorit.indexOf("/"));
                        merchantNameLikeSize++;
                    }
                }
                else {
                    if (merchantNameLikeSize == 1){
                        favorit = "";
                    }else {
//                        Toast.makeText(SubMenuActivity.this, "size= "+merchantNameLikeSize, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(SubMenuActivity.this, "namamenu= "+namaMenu, Toast.LENGTH_SHORT).show();
                        favorit = "/";
//                        Toast.makeText(SubMenuActivity.this, "fav= "+favorit, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < merchantNameLikeSize; i++){
                            if (!FavoriteNameLike[i].equals(namaMenu)){
                                favorit = favorit + FavoriteNameLike[i] + "/";
//                                Toast.makeText(SubMenuActivity.this, "fav= "+favorit, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    submenufav.child("favourite").setValue(favorit);
                    fav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(SubMenuActivity.this, "Remo  ve From Favorite", Toast.LENGTH_SHORT).show();
                    isFavorite=false;
                }
            }
        });
    }
    public void LOADFavorite(){
        FirebaseDatabase.getInstance().getReference(Constants.USER_KEY)
                .child(FBUser.getEmail().replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
                        favorit = detailMenu.get("favourite").toString();
                        favoritTemp = favorit;
                        if (!favoritTemp.equals("")){
                            merchantNameLikeSize=0;
                            while (favoritTemp.length()>1){
//                                Toast.makeText(SubMenuActivity.this, "size "+merchantNameLikeSize+"", Toast.LENGTH_SHORT).show();
                                favoritTemp = favoritTemp.substring(1);
                                FavoriteNameLike[merchantNameLikeSize] = favoritTemp.substring(0, favoritTemp.indexOf("/"));

//                                Toast.makeText(SubMenuActivity.this, "fav= "+FavoriteNameLike[merchantNameLikeSize], Toast.LENGTH_SHORT).show();
                                favoritTemp = favoritTemp.substring(favoritTemp.indexOf("/"));
                                merchantNameLikeSize++;
                            }
                            for (int i = 0; i < merchantNameLikeSize; i++){
                                if (FavoriteNameLike[i].equals(namaMenu)){
                                    fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                                    isFavorite = true;
//                                    Toast.makeText(SubMenuActivity.this, "is "+isFavorite+"", Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    fav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                                    isFavorite = false;
                                }

                            }
                        }
                        else {
                            fav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                            isFavorite = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case LOCATION_REQUEST:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    m_map.setMyLocationEnabled(true);
//                }
//                break;
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}