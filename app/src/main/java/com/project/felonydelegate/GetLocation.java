package com.project.felonydelegate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

public class GetLocation {

    SharedPreferences sp;
    SharedPreferences.Editor ed;
    FusedLocationProviderClient fusedLocationProviderClient;

    public void userLocation(final Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            sp = context.getSharedPreferences("info", Context.MODE_PRIVATE);
                            ed = sp.edit();

                            ed.putString("Lat", Double.toString(addresses.get(0).getLatitude()));
                            ed.putString("Lng", Double.toString(addresses.get(0).getLongitude()));
                            ed.putString("Address", addresses.get(0).getAddressLine(0));
                            ed.putString("Locality", addresses.get(0).getLocality());
                            ed.putString("CountryName", addresses.get(0).getCountryName());
                            ed.putString("CountryCode", addresses.get(0).getCountryCode());
                            ed.apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
