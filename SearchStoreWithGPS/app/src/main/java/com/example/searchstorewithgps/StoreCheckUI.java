package com.example.searchstorewithgps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.searchstorewithgps.databinding.ActivityStoreCheckUiBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StoreCheckUI extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private GoogleMap mMap;
    private ListView storeListView;
    private static ArrayList<Store> storeArray;
    private static LatLng deviceLocation;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ActivityStoreCheckUiBinding binding;
    private int MY_LOCATION_REQUEST_CODE = 1;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStoreCheckUiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storeListView = findViewById(R.id.storeListView);   // 리스트뷰 불러오기
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        printStoreLocation();
        printStoreListView();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "현재 위치로 이동합니다.", Toast.LENGTH_SHORT).show();
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void printStoreListView() {
        // 임시 ArrayList 생성하여 항목 추가
        ArrayList<String> storeNameArray = new ArrayList<>();
        for (int i = 0; i < storeArray.size(); i++)
            storeNameArray.add(storeArray.get(i).getName());

        // 임시 ArrayList에 추가한 항목을 리스트뷰에 저장하고 출력
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storeNameArray);
        storeListView.setAdapter(arrayAdapter);
    }

    public void printStoreLocation() {
        // 마커 추가
        for(int i = 0; i< storeArray.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(storeArray.get(i).getAddr1(), storeArray.get(i).getAddr2())).title(storeArray.get(i).getName()));
        }

        // 카메라 이동
        LatLng adjustedLocation = new LatLng(deviceLocation.latitude-0.0005, deviceLocation.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(adjustedLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adjustedLocation, 18));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void setDeviceLocation(LatLng devLoc) {
        deviceLocation = devLoc;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void setStoreArray(ArrayList<Store> arr) {
        storeArray = arr;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}