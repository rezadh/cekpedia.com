package me.cekpedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import me.cekpedia.models.ImageUpload;

public class TransportasiActivity extends AppCompatActivity {
    ListView listView;
    private RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<ImageUpload> imgList;
    private ImageListAdapter adapter;
    private ProgressDialog mProgressDialog;
    public static final String FB_DATABASE_PATH = "cekpedia";
    ArrayList<String>JudulList;
    ArrayList<String>LokasiList;
    ArrayList<String>NomorList;
    ArrayList<String>GambarList;
    ArrayList<String>nameSub;
    private RecyclerView mResult;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportasi);
        listView = (ListView) findViewById(R.id.listviewtransportasi);
        final ArrayList<String> Kategori = new ArrayList<>();
        searchView = (SearchView) findViewById(R.id.cari);
        mResult = (RecyclerView) findViewById(R.id.result_list_transportasi);
        JudulList = new ArrayList<>();
        LokasiList = new ArrayList<>();
        NomorList = new ArrayList<>();
        nameSub = new ArrayList<>();
        GambarList = new ArrayList<>();
        imgList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Kategori);
        listView.setAdapter(arrayAdapter);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait Loading List...");
        mProgressDialog.show();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                if (!string.toString().isEmpty()){
                    setAdapter(string.toString());
//                    listView.setVisibility(View.GONE);
                    mResult.setVisibility(View.VISIBLE);
                }else {
                    JudulList.clear();
                    NomorList.clear();
                    GambarList.clear();
                    LokasiList.clear();
                    nameSub.clear();
                    mResult.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    mResult.removeAllViews();
                }
                return false;
            }
        });
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("cekpediaItem").child("transportasi");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = postSnapshot.getValue(ImageUpload.class);
                    imgList.add(img);

                }
//                String nama = dataSnapshot.child("name").getValue(String.class);
//                String Lokasi = dataSnapshot.child("lokasi").getValue(String.class);
//                String imgurl = dataSnapshot.child("url").getValue(String.class);
//                Log.d("TAG", nama + " / " + Lokasi + " / " + imgurl);

                mAdapter = new ImageListAdapter(TransportasiActivity.this, R.layout.list_item, imgList, "transportasi");
                listView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TransportasiActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void tomaps(View view){
        Intent intent = new Intent(TransportasiActivity.this, SubMenuActivity.class);
        startActivity(intent);
    }
    private void setAdapter(final String searchString) {
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    String judul = Snapshot.child("name").getValue(String.class);
                    String lokasi = Snapshot.child("lokasi").getValue(String.class);
                    String number = Snapshot.child("number").getValue(String.class);
                    String gambar = Snapshot.child("url").getValue(String.class);
                    String namaSub = Snapshot.child("nameSub").getValue(String.class);

                    if (!judul.contains(searchString)) {
                        listView.setVisibility(View.GONE);
                        JudulList.add(judul);
                        LokasiList.add(lokasi);
                        NomorList.add(number);
                        GambarList.add(gambar);
                        nameSub.add(namaSub);
                        mResult.setVisibility(View.VISIBLE);
                        counter++;
                    } else {
                        listView.setVisibility(View.VISIBLE);
                        mResult.setVisibility(View.GONE);
                        mResult.removeAllViews();
                        JudulList.clear();
                        NomorList.clear();
                        GambarList.clear();
                        LokasiList.clear();
                    }
                    if (counter == 15) {
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}