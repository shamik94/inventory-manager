package com.meaty.meatyselleradmin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.meaty.meatyselleradmin.R;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        findViewById(R.id.bViewSales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(new Intent(HomeScreenActivity.this, ViewSalesActivity.class));
            }
        });

        findViewById(R.id.bUpdateFishPrice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(new Intent(HomeScreenActivity.this, UpdateFishPriceActivity.class));
            }
        });

        findViewById(R.id.bUpdateSellerCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(new Intent(HomeScreenActivity.this, UpdateSellerCartActivity.class));
            }
        });
    }

    private void goToActivity(Intent intent) {
        startActivity(intent);
    }
}
