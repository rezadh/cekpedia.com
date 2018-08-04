package me.cekpedia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    private SliderLayout sliderLayout;
    private EditText searchView;
    private BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
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
    public MainActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final ArrayList<Image> cekpedia = new ArrayList<>();
//
//        cekpedia.add(new Image(R.drawable.ic_masjid, "MASJID"));
//        cekpedia.add(new Image(R.drawable.ic_wisata, "Wisata"));

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(MainActivity.this, 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter myAdapter = new ImageAdapter(MainActivity.this, gambar, namaMenu);
        recyclerView.setAdapter(myAdapter);
        searchView = (EditText) findViewById(R.id.cari);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListCariActivity.class));
//                setTitle("Trafinder");
//                HomeFragment fragment = new HomeFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayout, fragment);
//                fragmentTransaction.commit();
            }
        });
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_home :
                        Toast.makeText(MainActivity.this, "Fitur Masih dalam pengembangan", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_loc :
                        Toast.makeText(MainActivity.this, "Fitur Masih dalam pengembangan", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_fav :
                        Intent intentfav = new Intent(MainActivity.this, FavouriteActivity.class);
                        startActivity(intentfav);
                        break;
                }

                return true;
            }
        });
//        gridView = (GridView) findViewById(R.id.grid_view1);
//        ImageAdapter adapter = new ImageAdapter(this, gambar, namaMenu);

        sliderLayout = findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Apotek K24 Gerlong", R.drawable.apotekk24gerlong);
        file_maps.put("RM Bakul Daun", R.drawable.rmbakuldaun);
        file_maps.put("Bandara Husein Sastranegara", R.drawable.bandarahusein);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                switch (position){
//                    case 0:
//                        Intent intentmasjid = new Intent(MainActivity.this, MasjidActivity.class);
//                        startActivity(intentmasjid);
//                        break;
//                    case 1:
//                        Intent intentwisata = new Intent(MainActivity.this, WisataActivity.class);
//                        startActivity(intentwisata);
//                        break;
//                    case 2:
//                        Intent intenthotel = new Intent(MainActivity.this, HotelActivity.class);
//                        startActivity(intenthotel);
//                        break;
//                    case 3:
//                        Intent intentrs = new Intent(MainActivity.this, RumahsakitActivity.class);
//                        startActivity(intentrs);
//                        break;
//                    case 4:
//                        Intent intentrestoran = new Intent(MainActivity.this, RestoranActivity.class);
//                        startActivity(intentrestoran);
//                        break;
//                    case 5:
//                        Intent intentsupermarket = new Intent(MainActivity.this, SupermarketActivity.class);
//                        startActivity(intentsupermarket);
//                        break;
//                    case 6:
//                        Intent intentsekolah = new Intent(MainActivity.this, SekolahActivity.class);
//                        startActivity(intentsekolah);
//                        break;
//                    case 7:
//                        Intent intenttransport = new Intent(MainActivity.this, TransportasiActivity.class);
//                        startActivity(intenttransport);
//                        break;
//                    case 8:
//                        Intent intentinput = new Intent(MainActivity.this, InputLokasiActivity.class);
//                        startActivity(intentinput);
//                        break;
//                    case 9:
//                        Intent intentspbu = new Intent(MainActivity.this, SPBUActivity.class);
//                        startActivity(intentspbu);
//                        break;
//                    case 10:
//                        Intent intentapotek = new Intent(MainActivity.this, ApotekActivity.class);
//                        startActivity(intentapotek);
//                        break;
//                    case 11:
//                        Intent intentbidan = new Intent(MainActivity.this, BidanActivity.class);
//                        startActivity(intentbidan);
//                        break;
//                }
//                Toast.makeText(MainActivity.this, "" + namaMenu[position],
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

