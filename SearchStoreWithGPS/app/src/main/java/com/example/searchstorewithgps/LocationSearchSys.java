package com.example.searchstorewithgps;

public class LocationSearchSys {
    Store[] storeArray;

    public String getDeviceLocation() {
        return "장치 위치";
    }

    public String getStoreLocation() {
        return "매장 위치";
    }

    public boolean checkConnected() {
        return true;
    }

}
