package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    SharedPreferences sp;
    GoogleMap mMap;
    GetLocation getLocation = new GetLocation();

    Button target;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;
            sp = requireActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
            getLocation.userLocation(requireContext());

            double Lat = Double.parseDouble(sp.getString("Lat",""));
            double Lng = Double.parseDouble(sp.getString("Lng",""));
            LatLng userLoc = new LatLng(Lat,Lng);
            googleMap.addMarker(new MarkerOptions().position(userLoc).title("User").snippet("Current Location"));
            CameraPosition cameraPosition = CameraPosition.builder().target(userLoc).zoom(20).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_maps, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

            target = view.findViewById(R.id.userTarget);

            target.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapFragment.getMapAsync(callback);
                }
            });
        }
    }
}