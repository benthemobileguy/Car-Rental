package com.bnotion.spikemotors;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class CompleteBookingActivity extends AppCompatActivity {
public static final int PAYPAL_REQEST_CODE = 2110;
private String price, fullNames;
private static final String CLIENT_ID = "ASCMiz8bkUy14cs1mk7UAf2woIMYCiE14qmRCZjReqBbK4TbP-DieGA9q6Ok0GjPqEWr19rl2GRouNsa";
private static PayPalConfiguration configuration = new PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(CLIENT_ID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // locking out landscape screen orientation for mobiles
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // locking out portait screen orientation for tablets
        }
        if (getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_complete_booking);
        //start paypal service
        Intent intent3 = new Intent(this, PayPalService.class);
        intent3.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent3);
        Button mCompleteBooking = findViewById(R.id.complete_booking);
        Intent  intent = getIntent();
        String imgLink = intent.getStringExtra("imgLink");
        fullNames = intent.getStringExtra("fullNames");
        ImageView mMainImg  = findViewById(R.id.main_img);
        Glide.with(this).load(imgLink).into(mMainImg);
        mCompleteBooking.setOnClickListener(v -> {
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(price), "USD", "Hire Purchase for " +  fullNames, PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent1 = new Intent(CompleteBookingActivity.this, PaymentActivity.class);
            intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
            intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            intent1.putExtra("fullNames", fullNames);
            startActivityForResult(intent1, PAYPAL_REQEST_CODE);

        });

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalPayment.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetailsActivity.class).putExtra("payment_details", paymentDetails)
                                .putExtra("payment_amount", price));
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "invalid payment", Toast.LENGTH_SHORT).show();
        }
    }
}
