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

import java.util.ArrayList;

public class LocationSearchSys {
    ArrayList<Store> storeArray;
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

    public ArrayList<Store> getStoreLocation() {
        storeArray.add(new Store(0, "맘스터치 동의대지천관점", 35.1442925,129.0347551));
        storeArray.add(new Store(0, "CAFE SPAZiO", 35.144225, 129.0352422));
        storeArray.add(new Store(0, "할리스 부산동의대점", 35.1442099, 129.0348774));
        storeArray.add(new Store(0, "CU 동의대지천관점", 35.1442809,129.0350693));
        storeArray.add(new Store(0, "GS25 동의대정보관점", 35.1463446,129.0356169));
        storeArray.add(new Store(0, "GS25 동의대공대점", 35.1444746,129.0363684));
        storeArray.add(new Store(0, "밀탑동의대점", 35.1432048,129.0340628));

        return storeArray;
    }

    public boolean checkConnected() {
        return true;
    }

}
