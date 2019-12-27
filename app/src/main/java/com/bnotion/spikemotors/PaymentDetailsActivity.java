package com.bnotion.spikemotors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailsActivity extends AppCompatActivity {
private TextView mItemId, mDailyPrice, mStatus, mCarName;
private String car_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mItemId = findViewById(R.id.item_id);
        mDailyPrice = findViewById(R.id.daily_price);
        mStatus = findViewById(R.id.status);
        mCarName = findViewById(R.id.car_name);
        Intent intent = getIntent();
        car_name = intent.getStringExtra("fullNames");
        try {
            JSONObject  jsonObject = new JSONObject(intent.getStringExtra("payment_details"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("payment_amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String payment_amount) throws JSONException {
        mItemId.setText(response.getString("id"));
        mStatus.setText(response.getString("state"));
        mStatus.setText(response.getString("$%s"));
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
