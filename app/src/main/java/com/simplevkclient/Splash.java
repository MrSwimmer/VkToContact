package com.simplevkclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.simplevkclient.R;

public class Splash extends Activity {
    ImageView logoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoview = (ImageView) findViewById(R.id.logo);
        logoview.setImageResource(R.drawable.logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // По истечении времени, запускаем главный активити, а Splash Screen закрываем
                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                startActivity(mainIntent);
                Splash.this.finish();
            }
        }, 2500);
    }
}
