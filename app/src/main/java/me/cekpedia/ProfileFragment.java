package me.cekpedia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            ((TextView) view.findViewById(R.id.profile_name)).setText(
                    name == null || name.trim().isEmpty() ? "Penjelajah Cekpedia" : name);
            ((TextView) view.findViewById(R.id.profile_email)).setText(user.getEmail());
            if (user.getPhotoUrl() != null) {
                ImageView avatar = view.findViewById(R.id.profile_avatar);
                avatar.setPadding(0, 0, 0, 0);
                avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(user.getPhotoUrl()).into(avatar);
            }
        }
        view.findViewById(R.id.open_favorites).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new FavouriteFragment())
                        .addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.open_contribution).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(getActivity(), InputLokasiActivity.class));
            }
        });
        return view;
    }

}
