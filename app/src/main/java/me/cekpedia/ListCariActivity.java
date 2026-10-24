package me.cekpedia;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Locale;

public class ListCariActivity extends AppCompatActivity {
    public static final String FB_DATABASE_PATH = "cekpedia";
    private RecyclerView results;
    private View empty;
    private TextView message;
    private int requestVersion;
    private final ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<String> locations = new ArrayList<>();
    private final ArrayList<String> numbers = new ArrayList<>();
    private final ArrayList<String> images = new ArrayList<>();
    private final ArrayList<String> categories = new ArrayList<>();

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_list_cari);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Cari tempat");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });
        empty = findViewById(R.id.search_empty);
        message = findViewById(R.id.search_empty_message);
        results = findViewById(R.id.reult_list);
        results.setLayoutManager(new LinearLayoutManager(this));
        results.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override public void getItemOffsets(Rect out, View view, RecyclerView parent, RecyclerView.State state) {
                out.bottom = (int)(16 * getResources().getDisplayMetrics().density);
            }
        });
        SearchView search = findViewById(R.id.carilist);
        search.clearFocus();
        View content = findViewById(android.R.id.content);
        content.setFocusableInTouchMode(true);
        content.requestFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String text) { return true; }
            @Override public boolean onQueryTextChange(String text) {
                int version = ++requestVersion;
                String query = text.trim().toLowerCase(Locale.ROOT);
                results.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                message.setText(query.isEmpty()
                        ? "Ketik nama tempat atau area. Tujuan berikutnya mungkin lebih dekat dari yang kamu kira."
                        : "Mencari tempat yang cocok untukmu…");
                if (!query.isEmpty()) search(query, version);
                return true;
            }
        });
    }
    private void search(final String query, final int version) {
        FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("cekpediaItem")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot data) {
                if (version != requestVersion || isFinishing()) return;
                titles.clear(); locations.clear(); numbers.clear(); images.clear(); categories.clear();
                outer: for (DataSnapshot group : data.getChildren()) {
                    for (DataSnapshot item : group.getChildren()) {
                        String title = item.child("name").getValue(String.class);
                        String location = item.child("lokasi").getValue(String.class);
                        String category = item.child("nameSub").getValue(String.class);
                        String haystack = (title == null ? "" : title) + " " + (location == null ? "" : location) + " " + (category == null ? "" : category);
                        if (title != null && haystack.toLowerCase(Locale.ROOT).contains(query)) {
                            titles.add(title); locations.add(location);
                            numbers.add(item.child("number").getValue(String.class));
                            images.add(item.child("url").getValue(String.class));
                            categories.add(category == null ? group.getKey() : category);
                            if (titles.size() == 15) break outer;
                        }
                    }
                }
                results.setAdapter(new SearchAdapter(ListCariActivity.this, titles, locations, numbers, images, categories));
                results.setVisibility(titles.isEmpty() ? View.GONE : View.VISIBLE);
                empty.setVisibility(titles.isEmpty() ? View.VISIBLE : View.GONE);
                message.setText("Belum ada tempat yang cocok. Coba nama atau area lainnya.");
            }
            @Override public void onCancelled(DatabaseError error) {
                if (version != requestVersion || isFinishing()) return;
                message.setText("Tempat belum bisa dimuat. Periksa koneksi lalu coba mencari kembali.");
            }
        });
    }
}
