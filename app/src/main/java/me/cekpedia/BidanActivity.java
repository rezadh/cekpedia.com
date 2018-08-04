package me.cekpedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


public class BidanActivity extends AppCompatActivity {
    ListView listView;
    private RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<ImageUpload> imgList;
    private ImageListAdapter adapter;
    private ProgressDialog mProgressDialog;
    public static final String FB_DATABASE_PATH = "cekpedia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidan);
        listView = (ListView) findViewById(R.id.listviewbidan);
        final ArrayList<String> Kategori = new ArrayList<>();
        imgList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Kategori);
        listView.setAdapter(arrayAdapter);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait Loading List...");
        mProgressDialog.show();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("cekpediaItem").child("bidan");

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

                mAdapter = new ImageListAdapter(BidanActivity.this, R.layout.list_item, imgList, "bidan");
                listView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BidanActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tomaps(View view) {
        Intent intent = new Intent(BidanActivity.this, SubMenuActivity.class);
        startActivity(intent);
    }
}
