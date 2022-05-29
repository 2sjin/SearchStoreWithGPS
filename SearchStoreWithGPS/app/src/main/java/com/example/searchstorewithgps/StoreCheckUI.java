package com.example.searchstorewithgps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.searchstorewithgps.databinding.ActivityMaps2Binding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StoreCheckUI extends FragmentActivity implements OnMapReadyCallback {

    private ListView storeListView;
    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private int MY_LOCATION_REQUEST_CODE = 1;

    private static ArrayList<Store> storeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storeListView = findViewById(R.id.storeListView);   // 리스트뷰 불러오기
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        }
    }

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
        checkStore();
    }



    public void checkStore() {
        // 임시 ArrayList 생성
        ArrayList<String> storeNameArray = new ArrayList<>();

        // 임시 ArrayList에 항목 추가
        for(int i = 0; i< storeArray.size(); i++)
            storeNameArray.add(storeArray.get(i).getName());

        // 임시 ArrayList에 추가한 항목을 리스트뷰에 저장
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storeNameArray);
        storeListView.setAdapter(arrayAdapter);

        // 마커 추가
        for(int i = 0; i< storeArray.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(storeArray.get(i).getAddr1(), storeArray.get(i).getAddr2())).title(storeArray.get(i).getName()));
        }

        // 카메라 이동
        LatLng tour = new LatLng(35.1442809-0.0007, 129.0350693);
        mMap.addMarker(new MarkerOptions().position(tour).title("동의대학교 지천관"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tour));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tour, 18));
    }

    public static void setStoreArray(ArrayList<Store> arr) {
        storeArray = arr;
    }

}