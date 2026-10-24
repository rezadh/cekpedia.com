package me.cekpedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Locale;
import me.cekpedia.models.ImageUpload;

/** One consistent list, search and loading/empty/error presentation for all categories. */
public abstract class CategoryActivity extends AppCompatActivity {
    protected abstract int layoutResource();
    protected abstract int listResource();
    protected abstract String categoryKey();

    private final ArrayList<ImageUpload> places = new ArrayList<>();
    private final ArrayList<ImageUpload> visible = new ArrayList<>();
    private ImageListAdapter adapter;
    private DatabaseReference reference;
    private ValueEventListener listener;
    private String query = "";
    private boolean loading = true;
    private boolean failed;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(layoutResource());
        setTitle("Jelajah tempat");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ListView list = findViewById(listResource());
        list.setEmptyView(findViewById(R.id.category_empty));
        adapter = new ImageListAdapter(this, R.layout.list_item, visible, categoryKey());
        list.setAdapter(adapter);
        SearchView search = findViewById(R.id.cari);
        search.clearFocus();
        View content = findViewById(android.R.id.content);
        content.setFocusableInTouchMode(true);
        content.requestFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String value) { return true; }
            @Override public boolean onQueryTextChange(String value) {
                query = value.trim().toLowerCase(Locale.ROOT);
                filter();
                return true;
            }
        });
        findViewById(R.id.retry_places).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { loadPlaces(); }
        });
        reference = FirebaseDatabase.getInstance().getReference("cekpedia")
                .child("cekpediaItem").child(categoryKey());
        loadPlaces();
    }

    private void loadPlaces() {
        if (listener != null) reference.removeEventListener(listener);
        loading = true;
        failed = false;
        filter();
        listener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                places.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ImageUpload place = child.getValue(ImageUpload.class);
                    if (place != null && place.getName() != null) places.add(place);
                }
                loading = false;
                failed = false;
                filter();
            }
            @Override public void onCancelled(DatabaseError error) {
                loading = false;
                failed = true;
                filter();
            }
        };
        reference.addValueEventListener(listener);
    }

    private void filter() {
        visible.clear();
        for (ImageUpload place : places) {
            String value = place.getName() + " " + (place.getLokasi() == null ? "" : place.getLokasi());
            if (value.toLowerCase(Locale.ROOT).contains(query)) visible.add(place);
        }
        adapter.notifyDataSetChanged();
        TextView title = findViewById(R.id.empty_places_title);
        TextView message = findViewById(R.id.empty_places_message);
        title.setText(loading ? "Menyiapkan tempat untukmu." : failed ? "Tempat belum bisa dimuat."
                : query.isEmpty() ? "Masih ada yang bisa ditemukan." : "Belum ada yang cocok.");
        message.setText(loading ? "Sedang memuat informasi tempat…" : failed ? "Periksa koneksi, lalu coba muat ulang."
                : query.isEmpty() ? "Belum ada tempat untuk kategori ini. Jelajahi kategori lainnya."
                : "Coba nama tempat atau area yang berbeda.");
        findViewById(R.id.retry_places).setVisibility(failed ? View.VISIBLE : View.GONE);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        if (reference != null && listener != null) reference.removeEventListener(listener);
        super.onDestroy();
    }
}
