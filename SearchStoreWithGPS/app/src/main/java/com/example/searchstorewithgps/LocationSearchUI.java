package com.example.searchstorewithgps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.searchstorewithgps.databinding.ActivityLocationSearchUiBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LocationSearchUI extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback{

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private LocationSearchSys ctrlSys;
    private GoogleMap mMap;
    private Button searchButton1;
    private Button searchButton2;
    private EditText et;
    private static LatLng deviceLocation = null;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ActivityLocationSearchUiBinding binding;
    private int MY_LOCATION_REQUEST_CODE = 1;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationSearchUiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ctrlSys = new LocationSearchSys();

        if (!ctrlSys.checkConnected(this)) {
            finish();
            showErrorMsg("네트워크 접속 실패. 네트워크 연결 상태를 확인 후 다시 시도해주세요.");
        }

        searchButton1 = (Button)findViewById(R.id.searchButton1);
        searchButton2 = (Button)findViewById(R.id.searchButton2);
        et = (EditText)findViewById(R.id.editText);

        final Geocoder geocoder = new Geocoder(this);
        searchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceLocation = ctrlSys.getLocationFromAddress(et, geocoder);

                if (deviceLocation == null) {
                    showErrorMsg("주소를 더 정확하게 입력해주세요.");
                }
                else if(deviceLocation.latitude == 999.999 && deviceLocation.longitude == 999.999) {
                    showErrorMsg("입출력 오류 발생!");
                }
                else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(deviceLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 18));
                    openStoreCheckUI();
                }

            }
        });

        searchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openStoreCheckUI(); }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        printDeviceLocation();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onMyLocationButtonClick() {
        showErrorMsg("현재 위치로 이동합니다.");
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void openStoreCheckUI() {
        Intent intent = new Intent(getApplicationContext(), StoreCheckUI.class);
        startActivity(intent);
        StoreCheckUI.setDeviceLocation(deviceLocation);

        ArrayList<Store> arr = ctrlSys.getStoresLocation(deviceLocation, this);
        StoreCheckUI.setStoreArray(arr);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void showErrorMsg(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void printDeviceLocation() {
        // 현재 위치로 지도 이동
        deviceLocation = ctrlSys.getDeviceLocation(this, this);
        if (deviceLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(deviceLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 18));
        }
        else {
            finish();
            showErrorMsg("위치 탐색 실패. 위치 서비스 확인 후 다시 시도하세요.");
        }
        // searchButton2.setText(String.valueOf(deviceLocation));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}