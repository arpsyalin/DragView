package com.lyl.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toBottom(View view) {
        startActivity(new Intent(this, BottomActivity.class));
    }

    public void toBottomList(View view) {
        startActivity(new Intent(this, BottomActivity.class));
    }

    public void toLeft(View view) {
        startActivity(new Intent(this, LeftActivity.class));
    }

    public void toRight(View view) {
        startActivity(new Intent(this, RightActivity.class));
    }

    public void toTop(View view) {
        startActivity(new Intent(this, TopActivity.class));
    }
}
