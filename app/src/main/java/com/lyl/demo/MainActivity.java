package com.lyl.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void R1(View view) {
        Toast.makeText(this, "xxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();

    }
}
