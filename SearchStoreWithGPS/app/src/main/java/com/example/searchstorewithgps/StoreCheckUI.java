package com.example.searchstorewithgps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreCheckUI extends AppCompatActivity {

    ArrayList<String> storeArray;
    WebView map;
    ListView storeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_check_ui);

        storeListView = findViewById(R.id.storeListView);
        map = findViewById(R.id.map);

        map.getSettings().setJavaScriptEnabled(true);   // 자바스크립트 허용
        map.setWebViewClient(new WebViewClient());  // 새로운 창을 띄우지 않고 내부에서 웹뷰를 실행시킨다.
        map.loadUrl("https://map.kakao.com/");  //웹뷰 실행

        storeArray = new ArrayList<>();

        storeArray.add("맘스터치 동의대지천관점");
        storeArray.add("CAFE SPAZiO");
        storeArray.add("할리스 부산동의대점");
        storeArray.add("CU 동의대지천관점");
        storeArray.add("GS25 동의대정보관점");
        storeArray.add("GS25 동의대공대점");
        storeArray.add("미니스톱 동의대기숙사점");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storeArray);
        storeListView.setAdapter(arrayAdapter);

    }
}