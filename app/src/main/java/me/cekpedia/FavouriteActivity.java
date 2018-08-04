package me.cekpedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import me.cekpedia.models.ImageUpload;
import me.cekpedia.utils.Constants;

public class FavouriteActivity extends AppCompatActivity {

    ListView listView;
    private ImageListAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<ImageUpload> imgList;
    private ImageListAdapter adapter;
    private ProgressDialog mProgressDialog;
    public static final String FB_DATABASE_PATH = "cekpedia";
    FirebaseAuth firebaseAuth;
    private String namaMenu, namaSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        listView = findViewById(R.id.listviewfav);


        final ArrayList<String> Kategori = new ArrayList<>();
        imgList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Kategori);
        listView.setAdapter(arrayAdapter);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait Loading List...");
        mProgressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser UserID = firebaseAuth.getCurrentUser();
        Intent i = getIntent();
        namaMenu = i.getStringExtra("JUDUL");
        namaSub = i.getStringExtra("SUB");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.USER_KEY).child(UserID.getEmail().replace(".",",")).child(namaSub);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = postSnapshot.getValue(ImageUpload.class);
                    imgList.add(img);

                }
                mAdapter = new ImageListAdapter(FavouriteActivity.this, R.layout.list_item, imgList);
                listView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
