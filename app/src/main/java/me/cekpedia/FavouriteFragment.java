package me.cekpedia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;

import me.cekpedia.models.ImageUpload;
import me.cekpedia.utils.Constants;


public class FavouriteFragment extends Fragment {
    View view;
    ListView listView;
    private ImageListAdapter mAdapter;
    private DatabaseReference mDatabaseRef, mDatabaseRefAll, Reference, Reference2;
    private StorageReference mStorageRef;
    private List<ImageUpload> imgList;
    private ImageListAdapter adapter;
    private ProgressDialog mProgressDialog;
    public static final String FB_DATABASE_PATH = "cekpedia";
    FirebaseAuth firebaseAuth;
    private String UserID, namaMenu, namaSub, favorit;
    String[] FavoriteNameLike;
    int FavoriteNameSize;
    boolean FavoriteNameLikeIsEmpty;
    private int merchantNameLikeSize;
    private String favoritTemp;


    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        listView = view.findViewById(R.id.listviewfav1);
        final ArrayList<String> Kategori = new ArrayList<>();
        imgList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Kategori);
        listView.setAdapter(arrayAdapter);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please Wait Loading List...");
        mProgressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser UserID = firebaseAuth.getCurrentUser();
        FavoriteNameLike = new String[10];
        FavoriteNameSize = 0;
        FavoriteNameLikeIsEmpty = false;
//        FavouriteFragment i = new FavouriteFragment();
//        namaSub = getArguments().getString("SUB");
        mDatabaseRefAll = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("cekpediaItem");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.USER_KEY).child(UserID.getEmail().replace(".",","));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
                favorit = detailMenu.get("favourite").toString();
                if (!favorit.equals("")){
                        merchantNameLikeSize = 0;
                        while (favorit.length() > 1) {
                            favorit = favorit.substring(1);
                            FavoriteNameLike[merchantNameLikeSize] = favorit.substring(0, favorit.indexOf("/"));
                            favorit = favorit.substring(favorit.indexOf("/"));
                            merchantNameLikeSize++;
                        }
                        for (int i=0; i<merchantNameLikeSize; i++){
                            Reference = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("keterangan");
                            final int finalI = i;
                            Reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Map<String, Object> detail = (Map<String, Object>) dataSnapshot.getValue();
                                    final String sub = detail.get(FavoriteNameLike[finalI]).toString();
                                    Reference2 = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("cekpediaItem").child(sub).child(FavoriteNameLike[finalI]);
                                    Reference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ImageUpload img = dataSnapshot.getValue(ImageUpload.class);
                                            imgList.add(img);

                                            //Toast.makeText(getActivity(), "nama1 "+dataSnapshot., Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(getActivity(), "nama2 "+img.getName().toString(), Toast.LENGTH_SHORT).show();

                                            mAdapter = new ImageListAdapter(getActivity(), R.layout.list_item, imgList);
                                            listView.setAdapter(mAdapter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment

        return view;
    }
}
