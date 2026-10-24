package me.cekpedia.after;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;
import java.lang.reflect.*;

/** Renders original packaged layouts; never launches original activities or Firebase. */
public class PreviewActivity extends android.support.v7.app.AppCompatActivity {
    private String screen;
    private String query = "";
    private BaseAdapter fixtureAdapter;
    private ListView fixtureList;
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
                map.setGravity(17); map.setTextSize(13); map.setTextColor(0xff0d7568); map.setBackgroundResource(id("drawable", "bg_mint")); return map;
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
        if (getIntent().getBooleanExtra("capture",false)) getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        try {
            String layout = category ? "activity_" + screen.substring(9) :
                screen.equals("home") || screen.equals("home-bottom") || screen.equals("drawer") || screen.equals("favorites") || screen.equals("profile") ? "activity_utama" :
                screen.equals("search") ? "activity_list_cari" :
                screen.equals("input") || screen.equals("input-bottom") ? "activity_input_lokasi" :
                screen.equals("detail") ? "activity_sub_menu" :
                screen.equals("favorites") ? "fragment_favourite" :
                screen.equals("profile") || screen.equals("profile-placeholder") ? "fragment_profile" :
                screen.equals("login") ? "activity_login" : screen;
            setContentView(inflate(layout));
            if (category) {
                setTitle("Jelajah tempat");
                if (getSupportActionBarReflect() != null) Class.forName("android.support.v7.app.ActionBar").getMethod("setDisplayHomeAsUpEnabled", boolean.class).invoke(getSupportActionBarReflect(), true);
                if (!getIntent().getBooleanExtra("empty", false)) populateList();
                else { find("category_empty").setVisibility(View.VISIBLE); findList((ViewGroup)findViewById(android.R.id.content)).setVisibility(View.GONE); }
            }
            if (screen.equals("home") || screen.equals("home-bottom") || screen.equals("drawer") || screen.equals("favorites") || screen.equals("profile")) home();
            if (screen.equals("detail")) detail();
            if (screen.equals("favorites")) {
                FrameLayout frame = (FrameLayout)find("frameLayout"); frame.removeAllViews();
                frame.addView(inflate("fragment_favourite")); populateList();
            }
            if (screen.equals("profile")) {
                FrameLayout frame = (FrameLayout)find("frameLayout"); frame.removeAllViews(); frame.addView(inflate("fragment_profile"));
            }
            if (screen.startsWith("input")) {
                setTitle("Usulkan tempat");
                Class.forName("android.support.v7.app.ActionBar").getMethod("setDisplayHomeAsUpEnabled",boolean.class).invoke(getSupportActionBarReflect(),true);
            }
            if (screen.equals("input-bottom") || screen.equals("home-bottom")) new android.os.Handler().postDelayed(() -> scrollBottom((ViewGroup)findViewById(android.R.id.content)), 500);
            if (screen.equals("search")) {
                // Keep the original empty search state; no invented live results.
                View v = find("carilist"); if (v != null) v.clearFocus();
            }
            setupNavigation();
            if (category || screen.equals("search")) {
                View content=findViewById(android.R.id.content); content.setFocusableInTouchMode(true); content.requestFocus();
            }
            android.util.Log.i("AfterPreview", "READY " + screen);
        } catch (Throwable e) {
            android.util.Log.e("AfterPreview", "FAILED " + screen, e);
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
        String[] names = {"Masjid", "Wisata", "Penginapan", "Rumah Sakit", "Restoran", "Supermarket", "Sekolah", "Transportasi", "Input Lokasi", "SPBU", "Apotek", "Bidan"};
        String[] icons = {"ic_ui_masjid", "ic_ui_wisata", "ic_ui_hotel", "ic_ui_rumahsakit", "ic_ui_restoran", "ic_ui_supermarket", "ic_ui_sekolah", "ic_ui_transportasi", "ic_ui_input_lokasi", "ic_ui_spbu", "ic_ui_apotek", "ic_ui_bidan"};
        int[] resources = new int[icons.length]; for (int i=0;i<icons.length;i++) resources[i]=id("drawable",icons[i]);
        Object adapter = Class.forName("me.cekpedia.ImageAdapter").getConstructor(Context.class,int[].class,String[].class).newInstance(this,resources,names);
        Class<?> click = Class.forName("me.cekpedia.ImageAdapter$ClickListener");
        Object categoryClick = java.lang.reflect.Proxy.newProxyInstance(getClassLoader(),new Class[]{click},(proxy,method,args) -> {
            if (method.getName().equals("itemClicked")) {
                String[] keys={"masjid","wisata","hotel","rumahsakit","restoran","supermarket","sekolah","transportasi","input","spbu","apotek","bidan"};
                String key=keys[(Integer)args[1]]; open(key.equals("input") ? "input" : "category:"+key);
            }
            return null;
        });
        adapter.getClass().getMethod("setClickListener",click).invoke(adapter,categoryClick);
        Object manager = new NonScrollingGridLayoutManager(this,4);
        Object recycler = find("recycler_view");
        ((View)recycler).setNestedScrollingEnabled(false);
        recycler.getClass().getMethod("setLayoutManager",Class.forName("android.support.v7.widget.RecyclerView$LayoutManager")).invoke(recycler,manager);
        recycler.getClass().getMethod("setAdapter",Class.forName("android.support.v7.widget.RecyclerView$Adapter")).invoke(recycler,adapter);
        final android.widget.ScrollView home = (android.widget.ScrollView)((FrameLayout)find("frameLayout")).getChildAt(0);
        home.post(() -> { home.requestFocus(); home.scrollTo(0,0); });
        if (screen.equals("drawer")) {
            label("username","Penjelajah Cekpedia"); label("useremail","contoh@example.test");
            new android.os.Handler().postDelayed(() -> {
                try { find("drawer_layout").getClass().getMethod("openDrawer",int.class).invoke(find("drawer_layout"),Gravity.START); }
                catch(Exception e) { android.util.Log.e("AfterPreview","drawer",e); }
            },500);
        }
    }
    private void populateList() {
        final ListView list = findList((ViewGroup)findViewById(android.R.id.content));
        if (list == null) return;
        final String title = getIntent().getStringExtra("title");
        fixtureList = list;
        fixtureAdapter = new BaseAdapter() {
            public int getCount(){return matchingRows().size();} public Object getItem(int p){return p;} public long getItemId(int p){return p;}
            public View getView(int position, View old, ViewGroup parent) {
                final int p = matchingRows().get(position);
                View row = getLayoutInflater().inflate(id("layout","list_item"),parent,false);
                ((TextView)row.findViewById(id("id","judul"))).setText(exampleTitle(p));
                View location = row.findViewById(id("id","lokasi"));
                if (location instanceof TextView) ((TextView)location).setText("Bandung, Jawa Barat");
                row.findViewById(id("id","telp")).setVisibility(View.GONE);
                ((TextView)row.findViewById(id("id","deskripsi"))).setText("Lihat detail tempat dan rencanakan kunjunganmu.");
                String kind = screen.startsWith("category:") ? screen.substring(9) : "wisata";
                String photo = kind.equals("apotek") ? "kimiafarmabubat" : kind.equals("bidan") ? "bidanida" :
                    kind.equals("hotel") ? "hotelhorison" : kind.equals("masjid") ? "masjidraya" :
                    kind.equals("restoran") ? "rmbakuldaun" : kind.equals("rumahsakit") ? "rshasansadikin" :
                    kind.equals("sekolah") ? "sman5bandung" : kind.equals("spbu") ? "spbupertamina3440225" :
                    kind.equals("supermarket") ? "toserbayogyasunda" : kind.equals("transportasi") ? "stasuinbandung" : "transtudiobandung";
                if (p == 1) {
                    photo = kind.equals("apotek") ? "apotekk24gerlong" : kind.equals("bidan") ? "bidandelima" :
                        kind.equals("hotel") ? "hotelgrandia" : kind.equals("masjid") ? "syamsululum" :
                        kind.equals("restoran") ? "ikanbakar" : kind.equals("rumahsakit") ? "rsmuhammadiyahbdg" :
                        kind.equals("sekolah") ? "sdcipagalo1" : kind.equals("spbu") ? "spbupertamina3440117" :
                        kind.equals("supermarket") ? "trasnmartbuahbatu" : kind.equals("transportasi") ? "bandarahusein" : "transtudiobandung";
                } else if (kind.equals("wisata")) photo = "farmhouse";
                ((ImageView)row.findViewById(id("id","imgview"))).setImageResource(id("drawable",photo));
                final String selectedPhoto = photo;
                row.setOnClickListener(v -> openPlace(exampleTitle(p), selectedPhoto));
                return row;
            }
        };
        list.setAdapter(fixtureAdapter);
    }
    private java.util.List<Integer> matchingRows() {
        java.util.ArrayList<Integer> rows = new java.util.ArrayList<>();
        for(int i=0;i<2;i++)if((exampleTitle(i)+" Bandung Jawa Barat "+screen).toLowerCase(java.util.Locale.ROOT).contains(query)) rows.add(i);
        return rows;
    }
    private String exampleTitle(int index) {
        String key = screen.startsWith("category:") ? screen.substring(9) : "wisata";
        String[] examples;
        switch(key) {
            case "apotek":examples=new String[]{"Kimia Farma Buah Batu","Apotek K24 Gegerkalong"};break;
            case "masjid":examples=new String[]{"Masjid Raya Bandung","Masjid Syamsul Ulum"};break;
            case "hotel":examples=new String[]{"Hotel Horison Bandung","Hotel Grandia"};break;
            case "rumahsakit":examples=new String[]{"RS Hasan Sadikin","RS Muhammadiyah Bandung"};break;
            case "restoran":examples=new String[]{"Bakul Daun","Ikan Bakar"};break;
            case "supermarket":examples=new String[]{"Yogya Sunda","Transmart Buah Batu"};break;
            case "sekolah":examples=new String[]{"SMA Negeri 5 Bandung","SD Cijagra"};break;
            case "transportasi":examples=new String[]{"Stasiun Bandung","Bandara Husein Sastranegara"};break;
            case "spbu":examples=new String[]{"SPBU Pertamina Buah Batu","SPBU Pertamina Bandung"};break;
            case "bidan":examples=new String[]{"Bidan Ida","Bidan Delima"};break;
            default:examples=new String[]{"Farmhouse Lembang","Trans Studio Bandung"};
        }
        return examples[index%examples.length];
    }
    private ListView findList(ViewGroup root) {
        for(int i=0;i<root.getChildCount();i++) {
            View v=root.getChildAt(i); if(v instanceof ListView)return (ListView)v;
            if(v instanceof ViewGroup){ListView found=findList((ViewGroup)v);if(found!=null)return found;}
        } return null;
    }
    private void detail() {
        label("tvjudul",getIntent().getStringExtra("place") == null ? "Farmhouse Lembang" : getIntent().getStringExtra("place")); label("tvalamat","Bandung, Jawa Barat"); label("tvphonenumber","Informasi kontak belum tersedia");
        image("gambarmenu",getIntent().getStringExtra("photo") == null ? "farmhouse" : getIntent().getStringExtra("photo"));
    }
    private void openPlace(String title,String photo) {
        android.content.Intent intent = new android.content.Intent(this,PreviewActivity.class);
        intent.putExtra("screen","detail"); intent.putExtra("place",title); intent.putExtra("photo",photo); startActivity(intent);
    }
    private void open(String destination) {
        android.content.Intent intent = new android.content.Intent(this, PreviewActivity.class);
        intent.putExtra("screen", destination);
        if (destination.startsWith("category:")) intent.putExtra("title", categoryTitle(destination.substring(9)));
        startActivity(intent);
    }
    private String categoryTitle(String key) {
        if (key.equals("hotel")) return "Penginapan";
        if (key.equals("rumahsakit")) return "Rumah Sakit";
        if (key.equals("spbu")) return "SPBU";
        return key.substring(0,1).toUpperCase() + key.substring(1);
    }
    private void setupNavigation() throws Exception {
        if (screen.equals("search")) {
            Object toolbar=find("my_toolbar"); Class<?> type=Class.forName("android.support.v7.widget.Toolbar");
            getClass().getMethod("setSupportActionBar",type).invoke(this,toolbar);
            setTitle("Cari tempat");
            Class.forName("android.support.v7.app.ActionBar").getMethod("setDisplayHomeAsUpEnabled",boolean.class).invoke(getSupportActionBarReflect(),true);
            type.getMethod("setNavigationOnClickListener",View.OnClickListener.class).invoke(toolbar,(View.OnClickListener)v -> finish());
            View old = find("reult_list"); ViewGroup parent = (ViewGroup)old.getParent();
            ListView list = new ListView(this); list.setId(old.getId()); list.setLayoutParams(old.getLayoutParams());
            list.setPadding(old.getPaddingLeft(),0,old.getPaddingRight(),old.getPaddingBottom()); list.setClipToPadding(false);
            list.setDivider(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)); list.setDividerHeight(dp(14));
            parent.removeView(old); parent.addView(list); populateList(); list.setVisibility(View.GONE);
            ((SearchView)find("carilist")).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextSubmit(String value){return true;}
                public boolean onQueryTextChange(String value){
                    query=value.trim().toLowerCase(java.util.Locale.ROOT); fixtureAdapter.notifyDataSetChanged();
                    boolean show=!query.isEmpty() && !matchingRows().isEmpty();
                    fixtureList.setVisibility(show ? View.VISIBLE : View.GONE); find("search_empty").setVisibility(show ? View.GONE : View.VISIBLE);
                    label("search_empty_message",query.isEmpty() ? "Ketik nama tempat atau area. Tujuan berikutnya mungkin lebih dekat dari yang kamu kira." : "Belum ada tempat yang cocok. Coba nama atau area lainnya.");
                    return true;
                }
            });
        }
        if (screen.equals("search") && getIntent().getStringExtra("query") != null) ((SearchView)find("carilist")).setQuery(getIntent().getStringExtra("query"),false);
        if (screen.startsWith("category:") && fixtureAdapter != null) {
            fixtureList.setEmptyView(find("category_empty"));
            ((SearchView)find("cari")).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextSubmit(String value){return true;}
                public boolean onQueryTextChange(String value){query=value.trim().toLowerCase(java.util.Locale.ROOT); fixtureAdapter.notifyDataSetChanged(); return true;}
            });
        }

        View homeSearch = find("cari");
        if (homeSearch instanceof EditText && !screen.equals("favorites")) {
            homeSearch.setFocusable(false); homeSearch.setFocusableInTouchMode(false);
            ((EditText)homeSearch).setCursorVisible(false);
            homeSearch.setOnClickListener(v -> open("search"));
        }
        if (screen.equals("profile") || screen.equals("profile-placeholder")) {
            find("open_favorites").setOnClickListener(v -> open("favorites"));
            find("open_contribution").setOnClickListener(v -> open("input"));
        }
        if (screen.equals("login")) find("bn_login").setOnClickListener(v -> open("home"));
        if (screen.equals("detail")) {
            ((ImageView)find("imgfav")).setColorFilter(0xff0d7568);
            find("imgfav").setOnClickListener(v -> {
                boolean saved = !getPreferences(0).getBoolean("saved",false);
                getPreferences(0).edit().putBoolean("saved",saved).apply();
                ((ImageView)find("imgfav")).setImageResource(id("drawable",saved ? "ic_favorite_black_24dp" : "ic_ui_heart"));
                Toast.makeText(this,saved ? "Disimpan di favorit preview" : "Dihapus dari favorit preview",Toast.LENGTH_SHORT).show();
            });
            find("buttonNavigation").setOnClickListener(v -> offline());
            for(String name:new String[]{"imageButtonInfo","imageButtonTelp","imageButtonPos","imageButtonSent"}) find(name).setOnClickListener(v -> offline());
        }
        Object nav = find("nav_view");
        if (nav != null) {
            Class<?> listener = Class.forName("android.support.design.widget.NavigationView$OnNavigationItemSelectedListener");
            Object callback = java.lang.reflect.Proxy.newProxyInstance(getClassLoader(),new Class[]{listener},(proxy,method,args) -> {
                if (!method.getName().equals("onNavigationItemSelected")) return null;
                int item = ((android.view.MenuItem)args[0]).getItemId();
                if (item==id("id","nav_home"))open("home");
                else if(item==id("id","nav_profile"))open("profile");
                else if(item==id("id","nav_fav"))open("favorites");
                else if(item==id("id","nav_send"))open("input");
                else if(item==id("id","nav_lokasi"))open("search");
                else offline();
                return true;
            });
            nav.getClass().getMethod("setNavigationItemSelectedListener",listener).invoke(nav,callback);
        }
    }
    private void offline() { new android.app.AlertDialog.Builder(this).setTitle("Preview offline").setMessage("Tampilan ini memakai data contoh. Login, layanan peta, telepon, dan pengiriman data belum dijalankan pada preview.").setPositiveButton("Mengerti",null).show(); }
    @Override public boolean onOptionsItemSelected(android.view.MenuItem item) { if(item.getItemId()==android.R.id.home){finish();return true;} return super.onOptionsItemSelected(item); }
    public void closeDetail(View view) { finish(); }
    public void logoutbn(View view) { open("login"); }
    public void openMaps(View view) { offline(); }
    public void selectImage(View view) { offline(); }
    public void uploadImage(View view) { offline(); }
    private void scrollBottom(ViewGroup root) {
        if(root instanceof ScrollView){((ScrollView)root).fullScroll(View.FOCUS_DOWN);return;}
        for(int i=0;i<root.getChildCount();i++)if(root.getChildAt(i) instanceof ViewGroup)scrollBottom((ViewGroup)root.getChildAt(i));
    }
}
