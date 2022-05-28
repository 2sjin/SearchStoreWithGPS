package com.example.searchstorewithgps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class LocationSearchUI extends AppCompatActivity {

    private LocationSearchSys ctrlSys;
    WebView map;
    Button searchButton;

    public void openStoreCheckUI() {

    }

    public void showErrorMsg() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search_ui);

        ctrlSys = new LocationSearchSys();
        map = findViewById(R.id.map);
        searchButton = findViewById(R.id.searchButton);

        map.getSettings().setJavaScriptEnabled(true);   // 자바스크립트 허용
        map.setWebViewClient(new WebViewClient());  // 새로운 창을 띄우지 않고 내부에서 웹뷰를 실행시킨다.
        map.loadUrl("https://map.kakao.com/");  //웹뷰 실행

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), StoreCheckUI.class);
                startActivity(intent);

            }
        });

    }
}