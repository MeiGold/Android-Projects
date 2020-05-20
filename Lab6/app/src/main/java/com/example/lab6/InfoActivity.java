package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void setMainActivity(View view){
        Intent e = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(e);
    }
}
