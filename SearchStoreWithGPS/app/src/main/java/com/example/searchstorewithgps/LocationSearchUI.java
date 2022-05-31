package com.example.searchstorewithgps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.searchstorewithgps.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class LocationSearchUI extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback{

    private LocationSearchSys ctrlSys;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button searchButton1;
    private Button searchButton2;
    private int MY_LOCATION_REQUEST_CODE = 1;

    private static LatLng deviceLocation = null;


    public void openStoreCheckUI() {
        StoreCheckUI.setDeviceLocation(deviceLocation);
        StoreCheckUI.setStoreArray(ctrlSys.getStoreLocation(deviceLocation));
        Intent intent = new Intent(getApplicationContext(), StoreCheckUI.class);
        startActivity(intent);
    }

    public void showErrorMsg(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ctrlSys = new LocationSearchSys();
        searchButton2 = findViewById(R.id.searchButton2);

        //추가분
        searchButton1 = (Button)findViewById(R.id.searchButton1);
        final EditText et = (EditText)findViewById(R.id.editText);

        final Geocoder geocoder = new Geocoder(this);
        searchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> list = null;

                String str = et.getText().toString();
                try {
                    list = geocoder.getFromLocationName(
                            str, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        showErrorMsg("주소를 조금 더 자세히 입력해주세요.");
                    } else {
                        double Latitude, Longitude;
                        Latitude = list.get(0).getLatitude();
                        Longitude = list.get(0).getLongitude();

                        // 주소에 해당하는 위치로 지도 이동
                        deviceLocation = new LatLng(Latitude, Longitude);
                        if (deviceLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(deviceLocation));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 18));
                        }

                        openStoreCheckUI();

                    }
                }
            }
        }); //여기까지

        searchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStoreCheckUI();
            }
        });
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
                mMap.setMyLocationEnabled(true);
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
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

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
        searchButton2.setText(String.valueOf(deviceLocation));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        showErrorMsg("현재 위치로 이동합니다.");
        return false;
    }

}