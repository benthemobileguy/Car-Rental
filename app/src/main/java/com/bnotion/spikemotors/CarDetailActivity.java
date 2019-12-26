package com.bnotion.spikemotors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CarDetailActivity extends AppCompatActivity {
private String car_name, fuelType, engine, no_of_seats, fuelEconomy, category, air_condition, hourlyPrice, dailyPrice;
private LinearLayout  mImagesArray;
private ArrayList<String> images;
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
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent  = getIntent();
        car_name = intent.getStringExtra("name");
        fuelType = intent.getStringExtra("fuel_type");
        engine = intent.getStringExtra("engine");
        fuelEconomy = intent.getStringExtra("fuel_economy");
        air_condition = intent.getStringExtra("airCondition");
        no_of_seats = intent.getStringExtra("no_seats");
        hourlyPrice = intent.getStringExtra("hourly_price");
        dailyPrice = intent.getStringExtra("daily_price");
        category = intent.getStringExtra("category");
        images  = intent.getStringArrayListExtra("imageURLS");
        mImagesArray =  findViewById(R.id.imagesArray);
        ImageView mImageView = findViewById(R.id.main_img);
        RequestOptions placeholderRequest = new RequestOptions().fitCenter();
        placeholderRequest.placeholder(R.drawable.placeholder);
        Glide.with(this).applyDefaultRequestOptions(placeholderRequest).load(images.get(0)).into(mImageView);
        TextView mFullNames =  findViewById(R.id.full_name);
        mFullNames.setText(category   + " " + car_name);
        //get images loop inside linear layout
        for(int i=0;i<images.size();i++)
        {
            ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(150,150));
            image.setMaxHeight(50);
            image.setMaxWidth(50);
            image.setPadding(0,0,6,0);
            Glide.with(this)
                    .load(images.get(i))
                    .into(image);
            // Adds the view to the layout
            mImagesArray.addView(image);
        }
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
        mHourlyPrice.setText("$" + hourlyPrice);
        mDailyPrice.setText("$" + dailyPrice);
        mEngine.setText(engine);
        mCarName.setText(car_name);
        mContinueBooking.setOnClickListener(v -> {

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }
}
