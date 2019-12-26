package com.bnotion.spikemotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CarDetailActivity extends AppCompatActivity {
private String car_name, fuelType, engine, no_of_seats, fuelEconomy, air_condition, hourlyPrice, dailyPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // locking out landscape screen orientation for mobiles
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // locking out portait screen orientation for tablets
        } if(getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_car_detail);
        Intent intent  = getIntent();
        car_name = intent.getStringExtra("name");
        fuelType = intent.getStringExtra("fuel_type");
        engine = intent.getStringExtra("engine");
        fuelEconomy = intent.getStringExtra("fuel_economy");
        air_condition = intent.getStringExtra("airCondition");
        no_of_seats = intent.getStringExtra("no_seats");
        hourlyPrice = intent.getStringExtra("hourly_price");
        dailyPrice = intent.getStringExtra("daily_price");
        TextView mCarName = findViewById(R.id.car_name);
        TextView mFuelType = findViewById(R.id.fuel_type);
        TextView mEngine = findViewById(R.id.engine);
        TextView mNo_of_seats = findViewById(R.id.no_of_seats);
        TextView mFuelEconomy = findViewById(R.id.fuel_economy);
        TextView mAir_condition = findViewById(R.id.air_condition);
        TextView mHourlyPrice = findViewById(R.id.hourly_price);
        TextView mDailyPrice = findViewById(R.id.daily_price);
        Button mContinueBooking = findViewById(R.id.continue_booking);
        mCarName.setText(car_name);
        mFuelType.setText(fuelType);
        mFuelEconomy.setText(fuelEconomy);
        mAir_condition.setText(air_condition);
        mNo_of_seats.setText(no_of_seats+ " Seats");
        mHourlyPrice.setText(hourlyPrice);
        mDailyPrice.setText(dailyPrice);
        mEngine.setText(engine);
        mCarName.setText(car_name);
        mContinueBooking.setOnClickListener(v -> {

        });
    }
}
