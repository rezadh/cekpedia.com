"""Cekpedia's shared Android visual system and layouts. Run from the repository root."""
from pathlib import Path
import re

RES = Path('app/src/main/res')
NS = 'xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto"'
def write(folder, name, text):
    (RES/folder).mkdir(exist_ok=True)
    (RES/folder/(name+'.xml')).write_text('<?xml version="1.0" encoding="utf-8"?>\n'+text.strip()+'\n')
def layout(name, text): write('layout', name, text)
def shape(name, color, radius=20, stroke=None):
    write('drawable',name,f'<shape xmlns:android="http://schemas.android.com/apk/res/android"><solid android:color="{color}"/><corners android:radius="{radius}dp"/>'+ (f'<stroke android:width="1dp" android:color="{stroke}"/>' if stroke else '')+'</shape>')
def txt(value, size=14, color='ink', extra=''):
    return f'<TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:text="{value}" android:textSize="{size}sp" android:textColor="@color/{color}" android:fontFamily="sans-serif" {extra}/>'
def kicker(value, extra=''):
    return txt(value,10,'colorPrimary',f'android:fontFamily="sans-serif-medium" android:letterSpacing="0.16" {extra}'.replace('android:fontFamily="sans-serif-medium" ', 'android:textStyle="bold" '))
def button(id, title, primary=True, extra=''):
    return f'<Button android:id="@+id/{id}" style="@style/Cek.Button.{"Primary" if primary else "Secondary"}" android:text="{title}" {extra}/>'
def field(id, hint, input='textCapSentences', extra=''):
    return f'<EditText android:id="@+id/{id}" style="@style/Cek.Field" android:hint="{hint}" android:inputType="{input}" {extra}/>'

write('values','colors','''<resources>
 <color name="colorPrimary">#0D7568</color><color name="editorColorPrimary">#F6F8F5</color>
 <color name="colorPrimaryapp">#0D7568</color><color name="colorPrimaryDark">#F6F8F5</color>
 <color name="colorAccent">#0D7568</color><color name="colorvector">#FFFFFF</color><color name="black">#183B35</color>
 <color name="colorHeader">#123F36</color><color name="sponsor">#0D7568</color>
 <color name="canvas">#F6F8F5</color><color name="surface">#FFFFFF</color><color name="ink">#193B34</color>
 <color name="muted">#60736A</color><color name="mint">#E6F1E9</color><color name="line">#DDE7DF</color>
 <color name="peach">#F7E5D8</color><color name="coral">#B65B37</color><color name="deep">#123F36</color>
</resources>''')
write('values','styles','''<resources>
 <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
  <item name="colorPrimary">@color/canvas</item><item name="colorPrimaryDark">@color/canvas</item>
  <item name="colorAccent">@color/colorPrimary</item><item name="android:windowBackground">@color/canvas</item>
  <item name="android:fontFamily">sans-serif</item><item name="android:colorBackground">@color/canvas</item>
  <item name="android:textColorPrimary">@color/ink</item><item name="android:textColorSecondary">@color/muted</item>
  <item name="actionBarTheme">@style/Cek.ActionBar</item><item name="actionBarStyle">@style/Cek.ActionBarStyle</item>
 </style>
 <style name="EditorTheme" parent="AppTheme"/>
 <style name="AppTheme.NoActionBar"><item name="windowActionBar">false</item><item name="windowNoTitle">true</item></style>
 <style name="AppBarOverlay" parent="ThemeOverlay.AppCompat.Light"><item name="android:textColorPrimary">@color/ink</item></style>
 <style name="PopupOverlay" parent="ThemeOverlay.AppCompat.Light"/>
 <style name="Cek.ActionBar" parent="ThemeOverlay.AppCompat.Light"><item name="colorControlNormal">@color/ink</item><item name="android:textColorPrimary">@color/ink</item></style>
 <style name="Cek.ActionBarStyle" parent="Widget.AppCompat.ActionBar.Solid"><item name="background">@color/canvas</item><item name="elevation">0dp</item><item name="titleTextStyle">@style/Cek.ActionBarTitle</item></style>
 <style name="Cek.ActionBarTitle" parent="TextAppearance.AppCompat.Widget.ActionBar.Title"><item name="android:textColor">@color/ink</item><item name="android:textSize">18sp</item><item name="android:fontFamily">sans-serif-medium</item></style>
 <style name="Cek.Field" parent="@android:style/Widget.EditText"><item name="android:layout_width">match_parent</item><item name="android:layout_height">56dp</item><item name="android:layout_marginTop">8dp</item><item name="android:layout_marginBottom">16dp</item><item name="android:background">@drawable/bg_field</item><item name="android:paddingLeft">16dp</item><item name="android:paddingRight">16dp</item><item name="android:textColor">@color/ink</item><item name="android:textColorHint">@color/muted</item><item name="android:textSize">15sp</item><item name="android:fontFamily">sans-serif</item><item name="android:singleLine">true</item></style>
 <style name="Cek.Button" parent="@android:style/Widget.Button"><item name="android:layout_width">match_parent</item><item name="android:layout_height">56dp</item><item name="android:textAllCaps">false</item><item name="android:textSize">15sp</item><item name="android:fontFamily">sans-serif-medium</item><item name="android:elevation">0dp</item><item name="android:stateListAnimator">@null</item><item name="android:paddingLeft">20dp</item><item name="android:paddingRight">20dp</item></style>
 <style name="Cek.Button.Primary" parent="Cek.Button"><item name="android:background">@drawable/bg_button_primary</item><item name="android:textColor">@color/surface</item></style>
 <style name="Cek.Button.Secondary" parent="Cek.Button"><item name="android:background">@drawable/bg_button_secondary</item><item name="android:textColor">@color/colorPrimary</item></style>
</resources>''')
write('values-v23','styles','''<resources><style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
 <item name="colorPrimary">@color/canvas</item><item name="colorPrimaryDark">@color/canvas</item><item name="colorAccent">@color/colorPrimary</item>
 <item name="android:windowBackground">@color/canvas</item><item name="android:fontFamily">sans-serif</item><item name="android:colorBackground">@color/canvas</item><item name="android:textColorPrimary">@color/ink</item><item name="android:textColorSecondary">@color/muted</item><item name="actionBarTheme">@style/Cek.ActionBar</item><item name="actionBarStyle">@style/Cek.ActionBarStyle</item>
 <item name="android:statusBarColor">@color/canvas</item><item name="android:windowLightStatusBar">true</item><item name="android:navigationBarColor">@color/deep</item>
</style></resources>''')
for name,color,radius,stroke in [('bg_card','#FFFFFF',22,'#E3EBE4'),('bg_field','#FFFFFF',16,'#DDE7DF'),('bg_mint','#E6F1E9',18,None),('bg_peach','#F7E5D8',18,None),('bg_deep','#123F36',24,None),('bg_photo','#E6F1E9',22,None),('bg_badge','#FFFFFF',12,None),('bg_avatar','#E6F1E9',40,None)]:shape(name,color,radius,stroke)
for name,color,pressed,stroke in [('primary','#0D7568','#0A5C52',None),('secondary','#E6F1E9','#D2E7D8',None)]:
    write('drawable','bg_button_'+name,f'<selector xmlns:android="http://schemas.android.com/apk/res/android"><item android:state_pressed="true"><shape><solid android:color="{pressed}"/><corners android:radius="16dp"/></shape></item><item><shape><solid android:color="{color}"/><corners android:radius="16dp"/></shape></item></selector>')
write('drawable','bg_hero_overlay','''<shape xmlns:android="http://schemas.android.com/apk/res/android"><gradient android:angle="90" android:startColor="#EB123F36" android:centerColor="#55123F36" android:endColor="#15123F36"/></shape>''')
write('drawable','nav_item_background','''<selector xmlns:android="http://schemas.android.com/apk/res/android"><item android:state_checked="true"><shape><solid android:color="#E6F1E9"/><corners android:radius="14dp"/></shape></item><item android:drawable="@android:color/transparent"/></selector>''')
paths={
 'search':'M21,21 L16,16 M18,10 A8,8 0,1 1,2 10 A8,8 0,1 1,18 10',
 'pin':'M20,10 C20,16 12,22 12,22 C12,22 4,16 4,10 A8,8 0,1 1,20 10 M15,10 A3,3 0,1 1,9 10 A3,3 0,1 1,15 10',
 'heart':'M12,21 L3,12 C-2,5 7,0 12,7 C17,0 26,5 21,12 Z',
 'arrow':'M5,12 L19,12 M13,6 L19,12 L13,18',
 'back':'M19,12 L5,12 M11,6 L5,12 L11,18',
 'person':'M16,7 A4,4 0,1 1,8 7 A4,4 0,1 1,16 7 M4,21 L4,19 C4,11 20,11 20,19 L20,21',
 'photo':'M3,5 L21,5 L21,21 L3,21 Z M3,17 L9,11 L14,16 L17,13 L21,17 M9,8 L9,8.1',
 'phone':'M7,3 L3,5 C2,13 11,22 19,21 L21,17 L16,14 L13,17 C10,15 8,13 7,10 L10,7 Z',
 'share':'M9,7 L12,4 L15,7 M12,4 L12,16 M7,10 L4,10 L4,22 L20,22 L20,10 L17,10',
 'info':'M22,12 A10,10 0,1 1,2 12 A10,10 0,1 1,22 12 M12,11 L12,17 M12,7 L12,7.1',
 'plus':'M12,4 L12,20 M4,12 L20,12',
 'masjid':'M3,21 L21,21 M5,21 L5,12 L19,12 L19,21 M8,12 C8,7 12,5 12,5 C12,5 16,7 16,12 M3,6 L3,21 M21,6 L21,21 M10,21 L10,17 C10,14 14,14 14,17 L14,21 M2,6 L4,6 M20,6 L22,6',
 'wisata':'M3,21 L21,21 M12,21 C12,13 13,9 14,5 M14,5 C10,1 5,4 5,7 C8,5 10,5 14,5 C19,1 23,5 22,8 C19,5 17,5 14,5 M14,5 C10,5 8,9 10,12 C10,8 12,7 14,5 M14,5 C19,5 21,9 19,12 C18,8 16,6 14,5',
 'hotel':'M3,8 L3,21 M21,8 L21,21 M3,16 L21,16 M5,9 L10,9 L10,14 L5,14 Z M13,10 L20,10 L20,14 L13,14 Z',
 'rumahsakit':'M4,21 L4,7 L9,7 L9,3 L15,3 L15,7 L20,7 L20,21 Z M9,12 L15,12 M12,9 L12,15 M10,21 L10,18 L14,18 L14,21',
 'restoran':'M5,3 L5,21 M2,3 L2,8 C2,12 8,12 8,8 L8,3 M15,3 L15,13 L21,13 L21,3 M18,13 L18,21',
 'supermarket':'M2,3 L5,3 L8,16 L20,16 L22,7 L6,7 M10,21 L10,21.1 M19,21 L19,21.1',
 'sekolah':'M3,21 L3,10 L12,3 L21,10 L21,21 Z M12,3 L12,1 M9,21 L9,16 L15,16 L15,21 M7,12 L7,12.1 M17,12 L17,12.1 M14,9 A2,2 0,1 1,10 9 A2,2 0,1 1,14 9',
 'transportasi':'M5,18 L5,22 M19,18 L19,22 M4,4 Q12,1 20,4 L20,18 L4,18 Z M4,11 L20,11 M8,15 L8,15.1 M16,15 L16,15.1',
 'input_lokasi':'M10,20 L3,22 L3,4 L10,2 L17,4 M10,2 L10,12 M17,4 L22,2 L22,12 M18,15 L18,23 M14,19 L22,19',
 'spbu':'M3,21 L3,3 L14,3 L14,21 M1,21 L16,21 M6,6 L11,6 L11,11 L6,11 Z M14,9 L17,9 L17,17 Q17,21 21,19 L21,7 L18,4',
 'apotek':'M5,4 L19,4 L19,21 L5,21 Z M9,4 L9,1 L15,1 L15,4 M8,12 L16,12 M12,8 L12,16',
 'bidan':'M12,21 C2,15 2,9 5,6 C7,4 10,4 12,7 C14,4 17,4 19,6 C22,9 22,15 12,21 M7,12 L10,12 L12,9 L14,15 L16,12 L19,12',
}
for name,path in paths.items():
    write('drawable','ic_ui_'+name,f'<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="24dp" android:height="24dp" android:viewportWidth="24" android:viewportHeight="24"><path android:pathData="{path}" android:fillColor="@android:color/transparent" android:strokeColor="#0D7568" android:strokeWidth="1.7" android:strokeLineCap="round" android:strokeLineJoin="round"/></vector>')

layout('grid_view',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="112dp" android:orientation="vertical" android:gravity="center_horizontal" android:paddingTop="6dp" android:paddingBottom="7dp" android:clickable="true" android:focusable="true" android:background="?android:attr/selectableItemBackground">
 <ImageView android:id="@+id/image_grid" android:layout_width="54dp" android:layout_height="54dp" android:padding="14dp" android:background="@drawable/bg_mint" android:scaleType="fitCenter"/>
 <TextView android:id="@+id/text_grid" android:layout_width="match_parent" android:layout_height="36dp" android:layout_marginTop="9dp" android:gravity="top|center_horizontal" android:textColor="@color/ink" android:textSize="12sp" android:fontFamily="sans-serif-medium" android:maxLines="2" android:ellipsize="end"/>
</LinearLayout>''')

layout('fragment_home',f'''<ScrollView {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/canvas" android:fillViewport="true" android:focusable="true" android:focusableInTouchMode="true" android:descendantFocusability="beforeDescendants" android:clipToPadding="false">
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingBottom="28dp">
 {kicker('JELAJAH LOKAL, CERITA BARU', 'android:layout_marginTop="16dp"')}
 {txt('Mau ke mana&#10;hari ini?',34,extra='android:fontFamily="sans-serif-medium" android:lineSpacingExtra="0dp" android:layout_marginTop="10dp"'.replace('android:fontFamily="sans-serif-medium"','android:textStyle="bold"'))}
 <EditText android:id="@+id/cari" style="@style/Cek.Field" android:layout_marginTop="22dp" android:layout_marginBottom="20dp" android:drawableLeft="@drawable/ic_ui_search" android:drawablePadding="12dp" android:hint="Cari tempat atau kebutuhanmu" android:focusable="false" android:clickable="true" android:inputType="text"/>
 <FrameLayout android:layout_width="match_parent" android:layout_height="196dp" android:background="@drawable/bg_photo" android:clipToOutline="true">
  <ImageView android:layout_width="match_parent" android:layout_height="match_parent" android:src="@drawable/transtudiobandung" android:scaleType="centerCrop" android:contentDescription="Suasana destinasi wisata"/>
  <com.daimajia.slider.library.SliderLayout android:id="@+id/slider" android:layout_width="match_parent" android:layout_height="match_parent"/>
  <View android:layout_width="match_parent" android:layout_height="match_parent" android:background="@drawable/bg_hero_overlay"/>
  <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:padding="20dp" android:gravity="bottom">
   {txt('SELANGKAH DARI RUTINITAS',9,'surface',extra='android:letterSpacing="0.15" android:textStyle="bold"')}
   {txt('Dekat di peta.&#10;Jauh dari biasa.',25,'surface',extra='android:textStyle="bold" android:layout_marginTop="8dp"')}
  </LinearLayout>
 </FrameLayout>
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="26dp" android:layout_marginBottom="14dp" android:gravity="center_vertical" android:orientation="horizontal">
  <TextView android:layout_width="0dp" android:layout_weight="1" android:layout_height="wrap_content" android:text="Jelajahi kategori" android:textSize="19sp" android:textColor="@color/ink" android:fontFamily="sans-serif-medium"/>
  <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="12 pilihan" android:textSize="11sp" android:textColor="@color/muted"/>
 </LinearLayout>
 <android.support.v7.widget.RecyclerView android:id="@+id/recycler_view" android:layout_width="match_parent" android:layout_height="336dp" android:nestedScrollingEnabled="false" android:overScrollMode="never"/>
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="16dp" android:orientation="vertical" android:padding="20dp" android:background="@drawable/bg_peach">
  {kicker('DARI YANG DEKAT, UNTUK SEMUA')}
  {txt('Tempat baik layak ditemukan.',18,extra='android:textStyle="bold" android:layout_marginTop="9dp"')}
  {txt('Dari kebutuhan harian sampai tujuan akhir pekan. Temukan semuanya di Cekpedia.',13,'ink',extra='android:lineSpacingExtra="4dp" android:layout_marginTop="8dp"')}
 </LinearLayout>
 </LinearLayout>
</ScrollView>''')

toolbar=f'''<android.support.v7.widget.Toolbar android:id="@+id/toolbar" android:layout_width="match_parent" android:layout_height="64dp" android:background="@color/canvas" app:theme="@style/AppBarOverlay" app:popupTheme="@style/PopupOverlay" app:contentInsetStart="20dp" app:contentInsetEnd="24dp">
 <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="cekpedia" android:textColor="@color/deep" android:fontFamily="sans-serif-black" android:letterSpacing="-0.04" android:textSize="27sp"/>
</android.support.v7.widget.Toolbar>'''
layout('app_bar_utama',f'<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas">{toolbar}<FrameLayout android:id="@+id/frameLayout" android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1"/></LinearLayout>')
layout('content_utama',f'<FrameLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent"/>')
layout('activity_utama',f'''<android.support.v4.widget.DrawerLayout {NS} android:id="@+id/drawer_layout" android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/canvas">
 <include layout="@layout/app_bar_utama" android:layout_width="match_parent" android:layout_height="match_parent"/>
 <android.support.design.widget.NavigationView android:id="@+id/nav_view" android:layout_width="300dp" android:layout_height="match_parent" android:layout_gravity="start" android:background="@color/surface" app:headerLayout="@layout/nav_header_utama" app:menu="@menu/activity_utama_drawer" app:itemIconTint="@color/colorPrimary" app:itemTextColor="@color/ink" app:itemBackground="@drawable/nav_item_background"/>
</android.support.v4.widget.DrawerLayout>''')
layout('nav_header_utama',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="260dp" android:background="@color/deep" android:orientation="vertical" android:padding="28dp">
 {txt('cekpedia',27,'surface',extra='android:textStyle="bold"')}
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="26dp" android:gravity="center_vertical" android:orientation="horizontal">
  <ImageView android:id="@+id/imageViewProfil" android:layout_width="52dp" android:layout_height="52dp" android:background="@drawable/bg_avatar" android:src="@drawable/ic_ui_person" android:padding="12dp" android:contentDescription="Foto profil"/>
  <View android:layout_width="0dp" android:layout_height="1dp" android:layout_weight="1"/>
  <ImageView android:id="@+id/logoutBtn" android:layout_width="48dp" android:layout_height="48dp" android:padding="12dp" android:src="@drawable/ic_power_settings_new_black_24dp" android:tint="@color/surface" android:contentDescription="Keluar akun" android:onClick="logoutbn" android:background="?android:attr/selectableItemBackground"/>
 </LinearLayout>
 <TextView android:id="@+id/username" android:layout_width="match_parent" android:layout_height="wrap_content" android:text="Selamat datang" android:textColor="@color/surface" android:fontFamily="sans-serif-medium" android:textSize="19sp" android:maxLines="1" android:ellipsize="end" android:layout_marginTop="18dp"/>
 <TextView android:id="@+id/useremail" android:layout_width="match_parent" android:layout_height="wrap_content" android:text="Tempat favorit, selalu dekat." android:textColor="#BFDBCC" android:textSize="12sp" android:layout_marginTop="6dp" android:maxLines="1" android:ellipsize="end"/>
</LinearLayout>''')

# Preserve every category's ListView/RecyclerView ID used by its Activity.
categories=[('masjid','Masjid','Ruang untuk singgah dan beribadah.'),('wisata','Wisata','Sedikit jeda, banyak cerita baru.'),('hotel','Penginapan','Temukan tempat untuk beristirahat.'),('rumahsakit','Rumah Sakit','Informasi layanan kesehatan di sekitarmu.'),('restoran','Restoran','Perjalanan enak dimulai dari sini.'),('supermarket','Supermarket','Lengkapi kebutuhan, lanjutkan harimu.'),('sekolah','Sekolah','Kenali pilihan tempat belajar.'),('transportasi','Transportasi','Langkah pertama menuju tujuanmu.'),('spbu','SPBU','Isi energi, lanjutkan perjalanan.'),('apotek','Apotek','Temukan kebutuhan kesehatanmu.'),('bidan','Bidan','Informasi layanan ibu dan keluarga.')]
for key,title,desc in categories:
    old=(RES/'layout'/f'activity_{key}.xml').read_text()
    lists=re.findall(r'<ListView[\s\S]*?android:id="@\+id/([^"]+)"',old)
    results=re.findall(r'<android.support.v7.widget.RecyclerView[\s\S]*?android:id="@\+id/([^"]+)"',old)
    list_id=lists[0] if lists else 'listview'+key
    result_id=results[0] if results else 'result_list_'+key
    # Persist IDs so rerunning the generator is deterministic.
    layout('activity_'+key,f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas">
     <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingTop="14dp">
      {kicker('PILIH TUJUANMU')}{txt(title,30,extra='android:textStyle="bold" android:layout_marginTop="10dp"')}{txt(desc,14,'muted',extra='android:layout_marginTop="8dp" android:lineSpacingExtra="3dp"')}
      <SearchView android:id="@+id/cari" android:layout_width="match_parent" android:layout_height="56dp" android:layout_marginTop="22dp" android:layout_marginBottom="16dp" android:background="@drawable/bg_field" android:iconifiedByDefault="false" android:queryHint="Nama tempat atau area" android:queryBackground="@android:color/transparent" android:paddingLeft="8dp" android:paddingRight="8dp"/>
     </LinearLayout>
     <FrameLayout android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1">
      <ListView android:id="@+id/{list_id}" android:layout_width="match_parent" android:layout_height="match_parent" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingBottom="24dp" android:clipToPadding="false" android:divider="@android:color/transparent" android:dividerHeight="14dp" android:scrollbars="none"/>
      <android.support.v7.widget.RecyclerView android:id="@+id/{result_id}" android:layout_width="match_parent" android:layout_height="match_parent" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingBottom="24dp" android:clipToPadding="false" android:visibility="gone"/>
     </FrameLayout>
    </LinearLayout>''')

layout('list_item',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:background="@drawable/bg_card" android:padding="12dp">
 <FrameLayout android:layout_width="match_parent" android:layout_height="156dp" android:background="@drawable/bg_photo" android:clipToOutline="true">
  <ImageView android:id="@+id/imgview" android:background="@color/mint" android:layout_width="match_parent" android:layout_height="match_parent" android:scaleType="centerCrop" android:contentDescription="Foto tempat"/>
  <TextView android:id="@+id/tvsponsor" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_gravity="top|right" android:layout_margin="10dp" android:background="@drawable/bg_badge" android:padding="8dp" android:text="Pilihan mitra" android:textColor="@color/colorPrimary" android:textSize="10sp" android:fontFamily="sans-serif-medium" android:visibility="gone"/>
 </FrameLayout>
 <TextView android:id="@+id/judul" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="15dp" android:paddingLeft="4dp" android:paddingRight="4dp" android:textSize="19sp" android:fontFamily="sans-serif-medium" android:textColor="@color/ink" android:maxLines="2" android:ellipsize="end"/>
 <TextView android:id="@+id/lokasi" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="7dp" android:paddingLeft="4dp" android:paddingRight="4dp" android:textSize="13sp" android:textColor="@color/muted" android:maxLines="2" android:ellipsize="end" android:lineSpacingExtra="3dp"/>
 <TextView android:id="@+id/telp" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="8dp" android:paddingLeft="4dp" android:paddingRight="4dp" android:textSize="12sp" android:textColor="@color/colorPrimary"/>
 <TextView android:id="@+id/deskripsi" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="8dp" android:paddingLeft="4dp" android:paddingRight="4dp" android:paddingBottom="8dp" android:textSize="13sp" android:textColor="@color/muted" android:maxLines="2" android:ellipsize="end" android:lineSpacingExtra="3dp"/>
</LinearLayout>''')

layout('activity_list_cari',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas">
 <android.support.v7.widget.Toolbar android:id="@+id/my_toolbar" android:layout_width="match_parent" android:layout_height="64dp" android:background="@color/canvas" app:title="Cari tempat" app:titleTextColor="@color/ink" app:theme="@style/AppBarOverlay"/>
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:paddingLeft="24dp" android:paddingRight="24dp">
  {kicker('ADA BANYAK YANG BISA DITEMUKAN')}{txt('Kamu sedang&#10;mencari apa?',30,extra='android:textStyle="bold" android:layout_marginTop="10dp"')}
  <SearchView android:id="@+id/carilist" android:layout_width="match_parent" android:layout_height="56dp" android:layout_marginTop="22dp" android:layout_marginBottom="18dp" android:background="@drawable/bg_field" android:iconifiedByDefault="false" android:queryHint="Nama tempat, kategori, atau area" android:queryBackground="@android:color/transparent" android:paddingLeft="8dp"/>
 </LinearLayout>
 <FrameLayout android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1">
  <LinearLayout android:id="@+id/search_empty" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_gravity="top" android:layout_margin="24dp" android:background="@drawable/bg_mint" android:padding="24dp" android:orientation="vertical">
   <ImageView android:layout_width="36dp" android:layout_height="36dp" android:src="@drawable/ic_ui_search"/>
   {txt('Mulai dari rasa ingin tahu.',19,extra='android:textStyle="bold" android:layout_marginTop="16dp"')}
   <TextView android:id="@+id/search_empty_message" android:layout_width="match_parent" android:layout_height="wrap_content" android:text="Ketik nama tempat atau area. Tujuan berikutnya mungkin lebih dekat dari yang kamu kira." android:textColor="@color/muted" android:textSize="14sp" android:lineSpacingExtra="5dp" android:layout_marginTop="8dp"/>
  </LinearLayout>
  <android.support.v7.widget.RecyclerView android:id="@+id/reult_list" android:layout_width="match_parent" android:layout_height="match_parent" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingBottom="24dp" android:clipToPadding="false" android:visibility="gone"/>
 </FrameLayout>
</LinearLayout>''')

for filename,list_id in [('fragment_favourite','listviewfav1'),('activity_favourite','listviewfav')]:
    layout(filename,f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas">
     <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="24dp">
      {kicker('RUANG UNTUK YANG BERKESAN')}{txt('Ingin kembali&#10;ke sini.',30,extra='android:textStyle="bold" android:layout_marginTop="10dp"')}{txt('Tempat yang kamu simpan, untuk perjalanan berikutnya.',14,'muted',extra='android:layout_marginTop="10dp" android:lineSpacingExtra="4dp"')}
      <EditText android:id="@+id/cari" style="@style/Cek.Field" android:hint="Tempat favoritmu" android:visibility="gone"/>
     </LinearLayout>
     <ListView android:id="@+id/{list_id}" android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingBottom="24dp" android:clipToPadding="false" android:divider="@android:color/transparent" android:dividerHeight="14dp" android:scrollbars="none"/>
    </LinearLayout>''')

layout('activity_login',f'''<ScrollView {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:fillViewport="true" android:background="@color/canvas">
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical">
  <FrameLayout android:layout_width="match_parent" android:layout_height="340dp">
   <ImageView android:layout_width="match_parent" android:layout_height="match_parent" android:src="@drawable/transtudiobandung" android:scaleType="centerCrop" android:contentDescription="Destinasi wisata lokal"/>
   <View android:layout_width="match_parent" android:layout_height="match_parent" android:background="@drawable/bg_hero_overlay"/>
   <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:padding="28dp">
    {txt('cekpedia',29,'surface',extra='android:textStyle="bold"')}
    <View android:layout_width="1dp" android:layout_height="0dp" android:layout_weight="1"/>
    {txt('Setiap tempat&#10;punya cerita.',36,'surface',extra='android:textStyle="bold"')}
    {txt('Temukan ceritamu berikutnya.',14,'surface',extra='android:layout_marginTop="12dp"')}
   </LinearLayout>
  </FrameLayout>
  <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:padding="28dp" android:orientation="vertical">
   {kicker('SELAMAT DATANG DI CEKPEDIA')}
   {txt('Jelajah lebih dekat.&#10;Kenangan lebih banyak.',27,extra='android:textStyle="bold" android:layout_marginTop="14dp"')}
   {txt('Dari tempat makan sampai tujuan akhir pekan. Semua kebutuhan perjalananmu, dalam satu ruang.',15,'muted',extra='android:lineSpacingExtra="5dp" android:layout_marginTop="14dp"')}
   <FrameLayout android:layout_width="match_parent" android:layout_height="64dp" android:background="@drawable/bg_card" android:padding="5dp" android:layout_marginTop="26dp">
    <com.google.android.gms.common.SignInButton android:id="@+id/bn_login" android:layout_width="match_parent" android:layout_height="match_parent" app:buttonSize="wide" app:colorScheme="light"/>
   </FrameLayout>
   {txt('Masuk untuk menyimpan tempat favoritmu.',12,'muted',extra='android:gravity="center" android:layout_marginTop="16dp" android:layout_marginBottom="16dp"')}
  </LinearLayout>
 </LinearLayout>
</ScrollView>''')

layout('activity_input_lokasi',f'''<ScrollView {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:fillViewport="true" android:background="@color/canvas">
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="24dp">
  {kicker('DARI KAMU, UNTUK PENJELAJAH LAIN')}{txt('Tempat baik&#10;layak dibagikan.',30,extra='android:textStyle="bold" android:layout_marginTop="12dp"')}{txt('Bantu orang lain menemukan tempat yang kamu kenal.',14,'muted',extra='android:lineSpacingExtra="4dp" android:layout_marginTop="10dp" android:layout_marginBottom="28dp"')}
  {kicker('01 / KENALI TEMPATNYA')}{txt('Nama tempat',13,extra='android:layout_marginTop="16dp" android:textStyle="bold"')}{field('etNamaTempat','Contoh: Taman Kota')}
  {txt('Kategori',13,extra='android:textStyle="bold"')}
  <Spinner android:id="@+id/spinner" android:layout_width="match_parent" android:layout_height="56dp" android:background="@drawable/bg_field" android:padding="12dp" android:layout_marginTop="8dp" android:layout_marginBottom="20dp" android:entries="@array/kategori"/>
  {txt('Tentang tempat ini',13,extra='android:textStyle="bold"')}{field('etdeskripsi','Apa yang membuat tempat ini menarik?','textMultiLine','android:minLines="3" android:singleLine="false" android:gravity="top" android:paddingTop="16dp" android:layout_height="112dp"')}
  {kicker('02 / LOKASI &amp; KONTAK', 'android:layout_marginTop="12dp"')}
  {button('pilihalamat','Pilih lokasi di peta',False,'android:layout_marginTop="16dp" android:onClick="openMaps"')}
  {field('etSetLokasi','Alamat akan muncul setelah dipilih','textPostalAddress','android:enabled="false"')}
  {txt('Nomor telepon',13,extra='android:textStyle="bold"')}{field('etPhone','Nomor yang dapat dihubungi','phone')}
  {kicker('03 / TAMBAHKAN FOTO', 'android:layout_marginTop="12dp"')}
  <ImageView android:id="@+id/iv_camera" android:layout_width="match_parent" android:layout_height="164dp" android:layout_marginTop="16dp" android:background="@drawable/bg_photo" android:src="@drawable/ic_ui_photo" android:scaleType="centerInside" android:padding="50dp" android:contentDescription="Preview foto tempat" android:clipToOutline="true"/>
  {button('selectImage','Pilih foto tempat',False,'android:layout_marginTop="12dp" android:onClick="selectImage"')}
  {txt('Gunakan foto yang menggambarkan tempat dengan jelas.',12,'muted',extra='android:lineSpacingExtra="3dp" android:layout_marginTop="12dp" android:layout_marginBottom="24dp"')}
  {button('bt_camera','Kirim usulan tempat',True,'android:onClick="uploadImage"')}
  {txt('Usulanmu masuk ke daftar peninjauan sebelum ditampilkan.',12,'muted',extra='android:gravity="center" android:lineSpacingExtra="3dp" android:layout_marginTop="14dp" android:layout_marginBottom="12dp"')}
 </LinearLayout>
</ScrollView>''')

actions=''
for id_,icon,label in [('imageButtonInfo','info','Info'),('imageButtonTelp','phone','Telepon'),('imageButtonPos','pin','Lokasi'),('imageButtonSent','share','Bagikan')]:
    actions+=f'''<LinearLayout android:layout_width="0dp" android:layout_weight="1" android:layout_height="wrap_content" android:gravity="center" android:orientation="vertical"><ImageButton android:id="@+id/{id_}" android:layout_width="54dp" android:layout_height="54dp" android:background="@drawable/bg_mint" android:padding="16dp" android:src="@drawable/ic_ui_{icon}" android:contentDescription="{label}"/>{txt(label,11,'ink',extra='android:gravity="center" android:layout_marginTop="8dp"')}</LinearLayout>'''
layout('activity_sub_menu',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas">
 <ScrollView android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1" android:fillViewport="false">
  <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical">
   <FrameLayout android:layout_width="match_parent" android:layout_height="260dp">
    <ImageView android:id="@+id/gambarmenu" android:layout_width="match_parent" android:layout_height="match_parent" android:scaleType="centerCrop" android:background="@color/mint" android:contentDescription="Foto tempat"/>
    <ImageButton android:id="@+id/detail_back" android:layout_width="48dp" android:layout_height="48dp" android:layout_margin="20dp" android:padding="14dp" android:background="@drawable/bg_badge" android:src="@drawable/ic_ui_back" android:contentDescription="Kembali" android:onClick="closeDetail"/>
    <ImageView android:id="@+id/imgfav" android:layout_width="48dp" android:layout_height="48dp" android:layout_gravity="top|right" android:layout_margin="20dp" android:padding="14dp" android:background="@drawable/bg_badge" android:src="@drawable/ic_ui_heart" android:contentDescription="Simpan ke favorit"/>
   </FrameLayout>
   <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="24dp">
    {kicker('TEMUKAN CERITA BARUMU')}
    <TextView android:id="@+id/tvjudul" android:layout_width="match_parent" android:layout_height="wrap_content" android:text="Detail tempat" android:textSize="30sp" android:textColor="@color/ink" android:fontFamily="sans-serif-medium" android:layout_marginTop="12dp"/>
    <TextView android:id="@+id/tvalamat" android:layout_width="match_parent" android:layout_height="wrap_content" android:textSize="14sp" android:textColor="@color/muted" android:lineSpacingExtra="4dp" android:layout_marginTop="10dp"/>
    <TextView android:id="@+id/tvphonenumber" android:layout_width="match_parent" android:layout_height="wrap_content" android:textSize="13sp" android:textColor="@color/colorPrimary" android:layout_marginTop="8dp"/>
    <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="horizontal" android:layout_marginTop="24dp" android:layout_marginBottom="28dp">{actions}</LinearLayout>
    {txt('Temukan di peta',20,extra='android:textStyle="bold"')}{txt('Kenali lokasinya sebelum berangkat.',13,'muted',extra='android:layout_marginTop="6dp" android:layout_marginBottom="14dp"')}
    <android.support.v7.widget.CardView android:layout_width="match_parent" android:layout_height="210dp" app:cardCornerRadius="20dp" app:cardElevation="0dp" app:cardBackgroundColor="@color/mint">
     <fragment android:id="@+id/map" android:name="com.google.android.gms.maps.SupportMapFragment" android:layout_width="match_parent" android:layout_height="match_parent"/>
    </android.support.v7.widget.CardView>
    <ImageButton android:id="@+id/imageButtonInfo1" android:layout_width="48dp" android:layout_height="48dp" android:visibility="gone"/>
    <ImageButton android:id="@+id/imageButtonRateUs" android:layout_width="48dp" android:layout_height="48dp" android:visibility="gone"/>
   </LinearLayout>
  </LinearLayout>
 </ScrollView>
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:paddingLeft="24dp" android:paddingRight="24dp" android:paddingTop="12dp" android:paddingBottom="16dp" android:background="@color/canvas">
  {button('buttonNavigation','Petunjuk arah  →')}
 </LinearLayout>
</LinearLayout>''')

layout('fragment_profile',f'''<ScrollView {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/canvas" android:fillViewport="true">
 <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="24dp">
  {kicker('RUANG JELAJAHMU')}{txt('Perjalananmu,&#10;caramu sendiri.',31,extra='android:textStyle="bold" android:layout_marginTop="12dp"')}
  <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:background="@drawable/bg_deep" android:orientation="vertical" android:padding="24dp" android:layout_marginTop="28dp">
   <ImageView android:id="@+id/profile_avatar" android:layout_width="64dp" android:layout_height="64dp" android:background="@drawable/bg_avatar" android:src="@drawable/ic_ui_person" android:padding="18dp" android:contentDescription="Foto profil"/>
   <TextView android:id="@+id/profile_name" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="20dp" android:text="Penjelajah Cekpedia" android:textColor="@color/surface" android:textSize="22sp" android:fontFamily="sans-serif-medium"/>
   <TextView android:id="@+id/profile_email" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="8dp" android:text="Selamat datang di ruang jelajahmu." android:textColor="#BFDBCC" android:textSize="13sp"/>
  </LinearLayout>
  {txt('Bawa yang berkesan&#10;ke perjalanan berikutnya.',22,extra='android:textStyle="bold" android:layout_marginTop="28dp"')}
  {txt('Kumpulkan tempat favoritmu. Bagikan tempat baik yang kamu temukan.',14,'muted',extra='android:lineSpacingExtra="5dp" android:layout_marginTop="12dp" android:layout_marginBottom="22dp"')}
  {button('open_favorites','Lihat tempat favorit')}{button('open_contribution','Usulkan tempat baru',False,'android:layout_marginTop="12dp"')}
 </LinearLayout>
</ScrollView>''')

# Bring legacy entry points and template screens into the same system.
layout('activity_main',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas"><include layout="@layout/fragment_home" android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1"/><android.support.design.widget.BottomNavigationView android:id="@+id/btm_nav" android:layout_width="match_parent" android:layout_height="64dp" android:background="@color/surface" app:itemIconTint="@color/colorPrimary" app:itemTextColor="@color/colorPrimary" app:menu="@menu/menu_bottom_nav_view"/></LinearLayout>''')
layout('app_bar_main2',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:orientation="vertical" android:background="@color/canvas">{toolbar}<FrameLayout android:id="@+id/framelayout" android:layout_width="match_parent" android:layout_height="0dp" android:layout_weight="1"/><android.support.design.widget.FloatingActionButton android:id="@+id/fab" android:layout_width="56dp" android:layout_height="56dp" android:layout_margin="20dp" android:layout_gravity="right" android:visibility="gone" app:srcCompat="@drawable/ic_ui_plus"/></LinearLayout>''')
header=(RES/'layout/nav_header_utama.xml').read_text().replace('imageViewProfil','prof_pic').replace('username','name').replace('useremail','email')
layout('nav_header_main2',header.split('?>',1)[1])
layout('content_main2',f'<FrameLayout {NS} android:layout_width="match_parent" android:layout_height="match_parent"/>')
layout('fragment_list_cari',f'<include {NS} layout="@layout/activity_list_cari" android:layout_width="match_parent" android:layout_height="match_parent"/>')
layout('custom_layout',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="12dp" android:background="@drawable/bg_card"><ImageView android:id="@+id/imageview" android:layout_width="match_parent" android:layout_height="140dp" android:scaleType="centerCrop"/><TextView android:id="@+id/textview" android:layout_width="match_parent" android:layout_height="wrap_content" android:textSize="16sp" android:textColor="@color/ink" android:paddingTop="12dp"/></LinearLayout>''')
layout('activity_login2',f'''<ScrollView {NS} android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/canvas" android:fillViewport="true"><LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:padding="28dp">
 {txt('cekpedia',29,extra='android:textStyle="bold" android:layout_marginTop="32dp"')}{kicker('SELAMAT DATANG KEMBALI','android:layout_marginTop="44dp"')}{txt('Lanjutkan&#10;ceritamu.',34,extra='android:textStyle="bold" android:layout_marginTop="14dp"')}
 <ProgressBar android:id="@+id/login_progress" android:layout_width="48dp" android:layout_height="48dp" android:visibility="gone"/>
 <LinearLayout android:id="@+id/login_form" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="30dp" android:orientation="vertical">
  <AutoCompleteTextView android:id="@+id/email" style="@style/Cek.Field" android:hint="Alamat email" android:inputType="textEmailAddress"/>
  {field('password','Kata sandi','textPassword')}{button('email_sign_in_button','Masuk')}
 </LinearLayout>
</LinearLayout></ScrollView>''')
# Empty states are shared across categories and saved places.
layout('view_empty_places',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:background="@drawable/bg_mint" android:padding="24dp">
 <ImageView android:layout_width="40dp" android:layout_height="40dp" android:src="@drawable/ic_ui_pin"/>
 {txt('Masih ada yang bisa ditemukan.',21,extra='android:id="@+id/empty_places_title" android:textStyle="bold" android:layout_marginTop="18dp"')}
 {txt('Belum ada tempat untuk ditampilkan. Coba pencarian lain atau jelajahi kategori berikutnya.',14,'muted',extra='android:id="@+id/empty_places_message" android:lineSpacingExtra="5dp" android:layout_marginTop="10dp"')}
{button('retry_places','Muat ulang',False,'android:visibility="gone" android:layout_marginTop="18dp"')}
</LinearLayout>''')
for key,_,_ in categories:
 p=RES/'layout'/('activity_'+key+'.xml');content=p.read_text()
 content=content.replace('     </FrameLayout>', '<include layout="@layout/view_empty_places" android:id="@+id/category_empty" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginLeft="24dp" android:layout_marginRight="24dp" android:visibility="gone"/>\n     </FrameLayout>')
 p.write_text(content)
for name in ['fragment_favourite','activity_favourite']:
 p=RES/'layout'/(name+'.xml');content=p.read_text()
 # Keep empty view beside the weighted list; ListView toggles it through setEmptyView.
 offset=content.rfind('</LinearLayout>')
 content=content[:offset]+'<include layout="@layout/view_empty_places" android:id="@+id/favorites_empty" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginLeft="24dp" android:layout_marginRight="24dp" android:visibility="gone"/>'+content[offset:]
 p.write_text(content)
layout('fragment_item',f'''<LinearLayout {NS} android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" android:background="@drawable/bg_card" android:padding="20dp" android:layout_marginBottom="12dp"><TextView android:id="@+id/id" android:layout_width="match_parent" android:layout_height="wrap_content" android:textSize="11sp" android:textColor="@color/colorPrimary"/><TextView android:id="@+id/content" android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginTop="8dp" android:textSize="17sp" android:textColor="@color/ink"/></LinearLayout>''')
layout('sliding_images',f'''<android.support.v7.widget.CardView {NS} android:id="@+id/card" android:layout_width="match_parent" android:layout_height="match_parent" app:cardCornerRadius="24dp" app:cardElevation="0dp"><ImageView android:id="@+id/slider" android:layout_width="match_parent" android:layout_height="match_parent" android:scaleType="centerCrop" android:contentDescription="Tempat pilihan Cekpedia"/></android.support.v7.widget.CardView>''')
layout('fragment_item_list',f'''<android.support.v7.widget.RecyclerView {NS} android:id="@+id/list" android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/canvas" android:padding="24dp" android:clipToPadding="false" app:layoutManager="android.support.v7.widget.LinearLayoutManager"/>''')
# Round profile photos once the real image is loaded.
for name in ['fragment_profile','nav_header_utama','nav_header_main2']:
 p=RES/'layout'/(name+'.xml');content=p.read_text().replace('android:background="@drawable/bg_avatar"','android:background="@drawable/bg_avatar" android:clipToOutline="true"');p.write_text(content)
# Use CardView outlines so photo corners stay rounded on hardware renderers.
for name,height in [('fragment_home','196dp'),('list_item','156dp')]:
 p=RES/'layout'/(name+'.xml');content=p.read_text()
 opening='<FrameLayout android:layout_width="match_parent" android:layout_height="'+height+'" android:background="@drawable/bg_photo" android:clipToOutline="true">'
 replacement='<android.support.v7.widget.CardView android:layout_width="match_parent" android:layout_height="'+height+'" app:cardCornerRadius="22dp" app:cardElevation="0dp" app:cardPreventCornerOverlap="false"><FrameLayout android:layout_width="match_parent" android:layout_height="match_parent">'
 content=content.replace(opening,replacement,1)
 # This is the first FrameLayout in each of these two layouts.
 content=content.replace('</FrameLayout>','</FrameLayout></android.support.v7.widget.CardView>',1)
 p.write_text(content)
# Keep a clear dropdown affordance while retaining the native Spinner.
p=RES/'layout'/'activity_input_lokasi.xml';content=p.read_text()
spinner=re.search(r'<Spinner[^>]+/>',content)
if spinner:
 content=content[:spinner.start()]+'''<FrameLayout android:layout_width="match_parent" android:layout_height="56dp" android:layout_marginTop="8dp" android:layout_marginBottom="20dp" android:background="@drawable/bg_field"><Spinner android:id="@+id/spinner" android:layout_width="match_parent" android:layout_height="match_parent" android:background="@android:color/transparent" android:paddingLeft="16dp" android:paddingRight="40dp" android:entries="@array/kategori"/><ImageView android:layout_width="18dp" android:layout_height="18dp" android:layout_gravity="right|center_vertical" android:layout_marginRight="16dp" android:rotation="90" android:src="@drawable/ic_ui_arrow" android:importantForAccessibility="no"/></FrameLayout>'''+content[spinner.end():]
p.write_text(content)
print('Wrote shared design system and redesigned application layouts.')
