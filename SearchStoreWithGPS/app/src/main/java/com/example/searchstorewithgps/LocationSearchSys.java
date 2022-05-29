package com.example.searchstorewithgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationSearchSys {
    Store[] storeArray;
    static LatLng deviceLocation = null;
    private FusedLocationProviderClient fusedLocationClient;

    public LatLng getDeviceLocation(Activity activity, Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Task a = fusedLocationClient.getLastLocation();
        a.addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    deviceLocation = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        });

        return deviceLocation;
    }

    public String getStoreLocation() {
        return "매장 위치";
    }

    public boolean checkConnected() {
        return true;
    }

}
