package com.youthink.comchatapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 이 다음 로그인 액티비티로 이동
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("state", "launch");
        startActivity(intent);
        finish();
    }
}
