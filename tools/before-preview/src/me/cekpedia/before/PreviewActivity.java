package me.cekpedia.before;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;
import java.lang.reflect.*;

/** Renders original packaged layouts; never launches original activities or Firebase. */
public class PreviewActivity extends android.support.v7.app.AppCompatActivity {
    private String screen;
    private int id(String type, String name) { return getResources().getIdentifier(name, type, "me.cekpedia"); }
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density); }
    private View find(String name) { return findViewById(id("id", name)); }
    private void label(String name, String value) {
        View v = find(name); if (v instanceof TextView) { ((TextView)v).setText(value); v.setVisibility(View.VISIBLE); }
    }
    private void image(String name, String drawable) {
        View v = find(name); int r = id("drawable", drawable);
        if (v instanceof ImageView && r != 0) ((ImageView)v).setImageResource(r);
    }
    private View inflate(String name) {
        LayoutInflater inflater = getLayoutInflater().cloneInContext(this);
        inflater.setFactory2(new LayoutInflater.Factory2() {
            public View onCreateView(View parent, String name, Context c, AttributeSet a) {
                if (!name.equals("fragment")) return null;
                TextView map = new TextView(c); map.setText("Peta tidak dimuat · preview offline");
                map.setGravity(17); map.setBackgroundColor(0xffdddddd); return map;
            }
            public View onCreateView(String n, Context c, AttributeSet a) { return onCreateView(null, n, c, a); }
        });
        return inflater.inflate(id("layout", name), null);
    }
    @Override public void onCreate(Bundle state) {
        screen = getIntent().getStringExtra("screen"); if (screen == null) screen = "home";
        boolean category = screen.startsWith("category:");
        setTheme(id("style", category || screen.startsWith("input") ? "EditorTheme" : "AppTheme.NoActionBar"));
        super.onCreate(state);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        try {
            String layout = category ? "activity_" + screen.substring(9) :
                screen.equals("home") || screen.equals("drawer") || screen.equals("favorites") ? "activity_utama" :
                screen.equals("search") ? "activity_list_cari" :
                screen.equals("input") || screen.equals("input-bottom") ? "activity_input_lokasi" :
                screen.equals("detail") ? "activity_sub_menu" :
                screen.equals("favorites") ? "fragment_favourite" :
                screen.equals("profile-placeholder") ? "fragment_profile" :
                screen.equals("login") ? "activity_login" : screen;
            setContentView(inflate(layout));
            if (category) {
                setTitle(getIntent().getStringExtra("title"));
                if (getSupportActionBarReflect() != null) Class.forName("android.support.v7.app.ActionBar").getMethod("setDisplayHomeAsUpEnabled", boolean.class).invoke(getSupportActionBarReflect(), true);
                if (!getIntent().getBooleanExtra("empty", false)) populateList();
            }
            if (screen.equals("home") || screen.equals("drawer") || screen.equals("favorites")) home();
            if (screen.equals("detail")) detail();
            if (screen.equals("favorites")) {
                FrameLayout frame = (FrameLayout)find("frameLayout"); frame.removeAllViews();
                frame.addView(inflate("fragment_favourite")); populateList();
            }
            if (screen.startsWith("input")) setTitle("Input Location");
            if (screen.equals("input-bottom")) new android.os.Handler().postDelayed(() -> scrollBottom((ViewGroup)findViewById(android.R.id.content)), 500);
            if (screen.equals("search")) {
                // Keep the original empty search state; no invented live results.
                View v = find("carilist"); if (v != null) v.clearFocus();
            }
            android.util.Log.i("BeforePreview", "READY " + screen);
        } catch (Throwable e) {
            android.util.Log.e("BeforePreview", "FAILED " + screen, e);
            TextView error = new TextView(this); error.setText("Preview gagal: " + screen + "\n" + e); setContentView(error);
        }
    }
    private Object getSupportActionBarReflect() throws Exception {
        return getClass().getMethod("getSupportActionBar").invoke(this);
    }
    private void home() throws Exception {
        View toolbar = find("toolbar");
        Class<?> toolbarClass = Class.forName("android.support.v7.widget.Toolbar");
        getClass().getMethod("setSupportActionBar", toolbarClass).invoke(this, toolbar);
        Object bar = getSupportActionBarReflect();
        Class.forName("android.support.v7.app.ActionBar").getMethod("setDisplayShowTitleEnabled", boolean.class).invoke(bar, false);
        Class.forName("android.support.v7.app.ActionBar").getMethod("setDisplayHomeAsUpEnabled", boolean.class).invoke(bar, true);
        Object toggle = Class.forName("android.support.v7.app.ActionBarDrawerToggle")
            .getConstructor(android.app.Activity.class, Class.forName("android.support.v4.widget.DrawerLayout"), toolbarClass, int.class, int.class)
            .newInstance(this, find("drawer_layout"), toolbar, id("string", "navigation_drawer_open"), id("string", "navigation_drawer_close"));
        toggle.getClass().getMethod("syncState").invoke(toggle);
        ((FrameLayout)find("frameLayout")).addView(inflate("fragment_home"));
        String[] names = {"    Masjid", "    Wisata", "Penginapan", "Rumah Sakit", "  Restoran", "Supermarket", "   Sekolah", "Transportasi", "Input Lokasi", "     SPBU", "    Apotek", "    Bidan"};
        String[] icons = {"ic_masjid", "ic_wisata", "ic_penginapan", "ic_rumah_sakit", "ic_restoran", "ic_supermarket", "ic_sekolah", "ic_transportasi", "ic_input_lokasi", "ic_spbu", "ic_apotek", "ic_bidan"};
        int[] resources = new int[icons.length]; for (int i=0;i<icons.length;i++) resources[i]=id("drawable",icons[i]);
        Object adapter = Class.forName("me.cekpedia.ImageAdapter").getConstructor(Context.class,int[].class,String[].class).newInstance(this,resources,names);
        Object manager = Class.forName("android.support.v7.widget.GridLayoutManager").getConstructor(Context.class,int.class).newInstance(this,4);
        Object recycler = find("recycler_view");
        recycler.getClass().getMethod("setLayoutManager",Class.forName("android.support.v7.widget.RecyclerView$LayoutManager")).invoke(recycler,manager);
        recycler.getClass().getMethod("setAdapter",Class.forName("android.support.v7.widget.RecyclerView$Adapter")).invoke(recycler,adapter);
        if (screen.equals("drawer")) {
            label("username","Pengguna Contoh"); label("useremail","contoh@example.test");
            new android.os.Handler().postDelayed(() -> {
                try { find("drawer_layout").getClass().getMethod("openDrawer",int.class).invoke(find("drawer_layout"),Gravity.START); }
                catch(Exception e) { android.util.Log.e("BeforePreview","drawer",e); }
            },500);
        }
    }
    private void populateList() {
        final ListView list = findList((ViewGroup)findViewById(android.R.id.content));
        if (list == null) return;
        final String title = getIntent().getStringExtra("title");
        list.setAdapter(new BaseAdapter() {
            public int getCount(){return 2;} public Object getItem(int p){return p;} public long getItemId(int p){return p;}
            public View getView(int p, View old, ViewGroup parent) {
                View row = getLayoutInflater().inflate(id("layout","list_item"),parent,false);
                ((TextView)row.findViewById(id("id","judul"))).setText((title == null ? "Tempat" : title) + " Contoh " + (p+1));
                View location = row.findViewById(id("id","lokasi"));
                if (location instanceof TextView) ((TextView)location).setText("Bandung · data contoh untuk preview");
                ((TextView)row.findViewById(id("id","telp"))).setText("Kontak belum tersedia");
                ((TextView)row.findViewById(id("id","deskripsi"))).setText("Bandung · data contoh untuk preview");
                String kind = screen.startsWith("category:") ? screen.substring(9) : "wisata";
                String photo = kind.equals("apotek") ? "kimiafarmabubat" : kind.equals("bidan") ? "bidanida" :
                    kind.equals("hotel") ? "hotelhorison" : kind.equals("masjid") ? "masjidraya" :
                    kind.equals("restoran") ? "rmbakuldaun" : kind.equals("rumahsakit") ? "rshasansadikin" :
                    kind.equals("sekolah") ? "sman5bandung" : kind.equals("spbu") ? "spbupertamina3440225" :
                    kind.equals("supermarket") ? "toserbayogyasunda" : kind.equals("transportasi") ? "stasuinbandung" : "transtudiobandung";
                ((ImageView)row.findViewById(id("id","imgview"))).setImageResource(id("drawable",photo));
                return row;
            }
        });
    }
    private ListView findList(ViewGroup root) {
        for(int i=0;i<root.getChildCount();i++) {
            View v=root.getChildAt(i); if(v instanceof ListView)return (ListView)v;
            if(v instanceof ViewGroup){ListView found=findList((ViewGroup)v);if(found!=null)return found;}
        } return null;
    }
    private void detail() {
        label("tvjudul","Tempat Contoh"); label("tvalamat","Bandung · data contoh untuk preview"); label("tvphonenumber","Kontak belum tersedia");
        image("gambarmenu","transtudiobandung");
    }
    private void scrollBottom(ViewGroup root) {
        if(root instanceof ScrollView){((ScrollView)root).fullScroll(View.FOCUS_DOWN);return;}
        for(int i=0;i<root.getChildCount();i++)if(root.getChildAt(i) instanceof ViewGroup)scrollBottom((ViewGroup)root.getChildAt(i));
    }
}
