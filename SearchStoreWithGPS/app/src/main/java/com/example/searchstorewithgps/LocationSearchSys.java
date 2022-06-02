package com.example.searchstorewithgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
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

    public ArrayList<Store> getStoresLocation(LatLng deviceLocation) {
        ArrayList<Store> tempStoreArray = new ArrayList<>();

        // 매장 정보를 임시 ArrayList에 추가
        tempStoreArray.add(new Store(0, "맘스터치 동의대지천관점", 35.1442925, 129.0347551));
        tempStoreArray.add(new Store(1, "CAFE SPAZiO", 35.144225, 129.0352422));
        tempStoreArray.add(new Store(2, "할리스 부산동의대점", 35.1442099, 129.0348774));
        tempStoreArray.add(new Store(3, "CU 동의대지천관점", 35.1442809, 129.0350693));
        tempStoreArray.add(new Store(4, "GS25 동의대정보관점", 35.1463446, 129.0356169));
        tempStoreArray.add(new Store(5, "GS25 동의대공대점", 35.1444746, 129.0363684));
        tempStoreArray.add(new Store(6, "밀탑동의대점", 35.1432048, 129.0340628));
        tempStoreArray.add(new Store(7, "투썸플레이스 동의대점", 35.1459356, 129.035277));
        tempStoreArray.add(new Store(8, "커피에반하다 부산신평점", 35.0932288, 128.9734027));
        tempStoreArray.add(new Store(9, "파리바게트 신평럭키점", 35.0934613, 128.9735863));
        tempStoreArray.add(new Store(10, "치킨신드롬사하시장점", 35.0984485, 128.9864968));
        tempStoreArray.add(new Store(11, "뚱땡이삼겹살", 35.0898791, 128.9757606));

        double range = 0.01;   // 반경 설정
        storeArray.clear();     // 메인 ArrayList 초기화

        // 임시 ArrayList에서 반경 내의 매장 정보만 메인 ArrayList에 추가
        for (int i = 0; i < tempStoreArray.size(); i++)
            if ((tempStoreArray.get(i).getAddr1() < (deviceLocation.latitude + range)) &&
                    (tempStoreArray.get(i).getAddr1() > (deviceLocation.latitude - range)) &&
                    (tempStoreArray.get(i).getAddr2() < (deviceLocation.longitude + range)) &&
                    (tempStoreArray.get(i).getAddr2() > (deviceLocation.longitude - range)))
                storeArray.add(tempStoreArray.get(i));

        return storeArray;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean checkConnected() {
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LatLng getLocationFromAddress(EditText et, Geocoder geocoder) {
        List<Address> list = null;

        String str = et.getText().toString();
        try {
            list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10); // 읽을 개수
        } catch (IOException e) {
            return new LatLng(999.999, 999.999);    // 에러코드 대신 위도,경도 범위 외의 값인 999.999를 리턴함
        }

        if (list != null) {
            if (list.size() == 0)
                return null;
            else
                return new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
        }
        return null;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}
