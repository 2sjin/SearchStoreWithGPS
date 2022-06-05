package com.example.searchstorewithgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocationSearchSys {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ArrayList<Store> storeArray = new ArrayList<>();
    private static LatLng deviceLocation = null;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private FusedLocationProviderClient fusedLocationClient;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LatLng getDeviceLocation(Activity activity, Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Store> getStoresLocation(LatLng deviceLocation, Context context) {
        ArrayList<Store> tempStoreArray = getStoreArrayFromFile(context);

        if (tempStoreArray == null)
            return null;

        filterNearbyStore(tempStoreArray);
        return storeArray;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean checkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LatLng getLocationFromAddress(EditText et, Geocoder geocoder) {
        List<Address> list = null;

        String str = et.getText().toString();
        try {       // 파라미터: 지역 이름(str), 읽을 개수(10)
            list = geocoder.getFromLocationName(str, 10);
        } catch (IOException e) {
            deviceLocation =  new LatLng(999.999, 999.999);    // 에러코드 대신 위도,경도 범위 외의 값인 999.999를 리턴함
            return deviceLocation;
        }

        if (list != null) {
            if (list.size() == 0)
                deviceLocation = null;
            else
                deviceLocation = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
        }

        return deviceLocation;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Store> getStoreArrayFromFile(Context context) {
        ArrayList<Store> arr = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(R.raw.test);
        String line = "";
        String strArray[];

        try {
            BufferedReader fr = new BufferedReader(new InputStreamReader(is));
            while((line = fr.readLine()) != null) {
                strArray = line.split(",");

                arr.add(new Store(Integer.parseInt(strArray[0]), strArray[1],
                        Double.parseDouble(strArray[2]), Double.parseDouble(strArray[3])));
            }
        } catch (IOException e) {
            return null;
        }

        return arr;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void filterNearbyStore(ArrayList<Store> arr) {
        double range = 0.01;   // 반경 설정
        storeArray.clear();     // 메인 ArrayList 초기화

        // 임시 ArrayList에서 반경 내의 매장 정보만 메인 ArrayList에 추가
        for (int i = 0; i < arr.size(); i++)
            if ((arr.get(i).getAddr1() < (deviceLocation.latitude + range)) &&
                    (arr.get(i).getAddr1() > (deviceLocation.latitude - range)) &&
                    (arr.get(i).getAddr2() < (deviceLocation.longitude + range)) &&
                    (arr.get(i).getAddr2() > (deviceLocation.longitude - range)))
                storeArray.add(arr.get(i));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}
