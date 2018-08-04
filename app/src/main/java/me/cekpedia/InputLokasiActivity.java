package me.cekpedia;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import me.cekpedia.models.ImageUpload;
import me.cekpedia.utils.Constants;

public class InputLokasiActivity extends AppCompatActivity {
    private Button btCamera;
    private ImageView ivCamera;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST_CODE = 7777;
    private static final int SELECT_PHOTO = 100;
    private static final int PLACE_PICKER_REQUEST = 2;
    public static final String FB_STORAGE_PATH = "cekpediaItem/";
    public static final String FB_DATABASE_PATH = "cekpedia";
    private String addressLL, addressMaps, UserID;
    private Double longitude, langitude;
    private MapView mMapView;
    private GoogleMap m_map;
    private StorageTask mUploadTask;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    DatabaseReference database, mDatabase;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    EditText etNama, etLokasi, etPhone, etDeskripsi;
    Spinner myspinner;
    Button submit;
    FirebaseAuth firebaseAuth;
    GoogleSignInAccount account;
    final FirebaseDatabase FBdatabase = FirebaseDatabase.getInstance();
    final DatabaseReference mDatabaseRef = FBdatabase.getReference(Constants.USER_KEY);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_lokasi);
        btCamera = (Button) findViewById(R.id.bt_camera);
        etNama = (EditText)findViewById(R.id.etNamaTempat);
        etLokasi = (EditText)findViewById(R.id.etSetLokasi);
        etPhone = findViewById(R.id.etPhone);
        etDeskripsi = (EditText) findViewById(R.id.etdeskripsi);
        progressDialog = new ProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.iv_camera);
        submit = (Button)findViewById(R.id.bt_camera);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        //creates a storage reference
        storageRef = storage.getReference();
        myspinner = findViewById(R.id.spinner);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(requestCode == SELECT_PHOTO){
                if (resultCode == RESULT_OK) {
                    Toast.makeText(InputLokasiActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imageView.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//
                }
        }else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){
            Place place = PlacePicker.getPlace(InputLokasiActivity.this,imageReturnedIntent);
                    addressLL = place.getLatLng().toString();
                    longitude = Double.parseDouble (addressLL.substring(addressLL.indexOf("(")+1, addressLL.indexOf(",")));
                    langitude = Double.parseDouble (addressLL.substring(addressLL.indexOf(",")+1, addressLL.indexOf(")")));
                    addressMaps = place.getAddress().toString();
                    etLokasi.setText(addressMaps);
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(View view) {
        Intent i = getIntent();
        final String namaMenu = i.getStringExtra("JUDUL");
        final String namaSub = i.getStringExtra("SUB");
        //create reference to images folder and assing a name to the file that will be uploaded

        if (etNama.getText().toString().equals("") && etLokasi.getText().toString().equals("") && imageView.getDrawable() == null) {
            Toast.makeText(InputLokasiActivity.this, "Data is Empty", Toast.LENGTH_SHORT).show();
        } else {

            //creating and showing progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);

            //starting upload
            StorageReference ref = storageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(selectedImage));
            mUploadTask = ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload finished...", Toast.LENGTH_SHORT).show();
                    finish();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.setProgress(0);
//                        }
//                    }, 500);
                    String kategori = myspinner.getSelectedItem().toString();
                    //Upload to database
                    ImageUpload imageUpload = new ImageUpload(etNama.getText().toString(), etLokasi.getText().toString()
                            , etPhone.getText().toString(), taskSnapshot.getDownloadUrl().toString()
                            , langitude, longitude, etDeskripsi.getText().toString(), false, kategori + "");

                    final FirebaseUser FBUser = firebaseAuth.getCurrentUser();
                    database.child("waitinglist").child(kategori).child(etNama.getText().toString()).setValue(imageUpload);
                    ImageUpload imageUploadket = new ImageUpload(kategori);
                    database.child("keterangan").child(etNama.getText().toString() ).setValue(kategori);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(InputLokasiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    startActivity(new Intent(InputLokasiActivity.this, UtamaActivity.class));
                }
            });
        }
//            uploadTask = imageRef.putFile(selectedImage);
//            // Observe state change events such as progress, pause, and resume
//            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    //sets and increments value of progressbar
//                    progressDialog.incrementProgressBy((int) progress);
//                }
//            });
//            // Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(InputLokasiActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(InputLokasiActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    etNama.setText("");
//                    etLokasi.setText("");
//                    //showing the uploaded image in ImageView using the download url
//                    Picasso.with(InputLokasiActivity.this).load(downloadUrl).into(imageView);
//                }
//            });
        }

    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void openMaps(View view){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            //i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
//    }
}