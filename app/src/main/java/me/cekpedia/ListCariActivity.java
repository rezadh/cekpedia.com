package me.cekpedia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCariActivity extends AppCompatActivity {

    private SearchView searchView;
    private String namaMenu, namaSub;
    private RecyclerView mResult;
    private DatabaseReference mDatabaseRef;
    public static final String FB_DATABASE_PATH = "cekpedia";
    ArrayList<String>JudulList;
    ArrayList<String>LokasiList;
    ArrayList<String>NomorList;
    ArrayList<String>GambarList;
    ArrayList<String>nameSub;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cari);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


        searchView = (SearchView) findViewById(R.id.carilist);
        mResult = (RecyclerView) findViewById(R.id.reult_list);
        mResult.setHasFixedSize(true);
        mResult.setLayoutManager(new LinearLayoutManager(this));
        mResult.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        JudulList = new ArrayList<>();
        LokasiList = new ArrayList<>();
        NomorList = new ArrayList<>();
        nameSub = new ArrayList<>();
        GambarList = new ArrayList<>();
        Intent i = getIntent();
        namaMenu = i.getStringExtra("JUDUL");
        namaSub = i.getStringExtra("SUB");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.toString().isEmpty()){
                    setAdapter(newText.toString());
                    mResult.setVisibility(View.VISIBLE);
                }else {
                    JudulList.clear();
                    NomorList.clear();
                    GambarList.clear();
                    LokasiList.clear();
                    nameSub.clear();
                    mResult.setVisibility(View.INVISIBLE);
                    mResult.removeAllViews();
                }
                return false;
            }
        });

//        searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!editable.toString().isEmpty()){
//                    setAdapter(editable.toString());
//                }else {
//                    JudulList.clear();
//                    NomorList.clear();
//                    GambarList.clear();
//                    LokasiList.clear();
//                    nameSub.clear();
//                    mResult.removeAllViews();
//                }
//
//            }
//        });

//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseSearch();
//            }
//        });
//    }
//
//    private void firebaseSearch(){
//        FirebaseRecyclerAdapter<ImageUpload, ListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImageUpload, ListViewHolder>(
//                ListViewHolder.class,
//                R.layout.list_item,
//                ImageUpload.class,
//                mDatabaseRef
//        ) {
//            @Override
//            protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull ImageUpload model) {
//
//            }
//
//            @Override
//            public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                return null;
//            }
//        };
//    }
//
//    public class ListViewHolder extends RecyclerView.ViewHolder{
//
//        View mView;
//        public ListViewHolder(View itemView) {
//            super(itemView);
//
//            mView = itemView;
//        }
    }

    private void setAdapter(final String searchString) {

        mDatabaseRef.child("cekpediaItem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JudulList.clear();
                NomorList.clear();
                GambarList.clear();
                LokasiList.clear();
                mResult.removeAllViews();
                int counter = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                        for (DataSnapshot Snapshot : ds.getChildren()) {
                            String judul = Snapshot.child("name").getValue(String.class);
                            String lokasi = Snapshot.child("lokasi").getValue(String.class);
                            String number = Snapshot.child("number").getValue(String.class);
                            String gambar = Snapshot.child("url").getValue(String.class);
                            String namaSub = Snapshot.child("nameSub").getValue(String.class);

                            //                    if (Snapshot.child("name").getValue(String.class).contains(searchString)){
                            if (judul.toLowerCase().contains(searchString)) {
                                JudulList.add(judul);
                                LokasiList.add(lokasi);
                                NomorList.add(number);
                                GambarList.add(gambar);
                                nameSub.add(namaSub);
//                                mResult.setVisibility(View.VISIBLE);
                                counter++;
                            } else {
//                                mResult.setVisibility(View.INVISIBLE);
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
                searchAdapter = new SearchAdapter(ListCariActivity.this, JudulList, LokasiList, NomorList, GambarList, nameSub);
                mResult.setAdapter(searchAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
