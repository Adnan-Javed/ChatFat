package com.example.adnan.chatfat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.adnan.chatfat.main.Login;
import com.hanks.htextview.HTextView;


public class Launcher extends AppCompatActivity {

    HTextView hTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        hTextView = findViewById(R.id.chatfat);

        hTextView.animateText("Welcome ChatFat");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Launcher.this, Login.class));
                finish();
            }
        }, 1000);

    }
}
