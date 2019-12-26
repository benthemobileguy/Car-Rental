package com.bnotion.spikemotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RelativeLayout mParentLayout;
    private LinearLayout mLinear1, mLinear2, mLinear3, mLinear4;
    private Button mGetStarted, sign_in, register, login_btn, reg_btn, reset_btn;
    private EditText email_login, email_reset, pass_login, names_reg, email_reg, phone_reg, pass_reg;
    private TextView forgotten_pass, recovery_text;
    private ProgressBar mProgress;
    private DatabaseReference databaseReference;
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
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mParentLayout = findViewById(R.id.parent);
        mProgress =  findViewById(R.id.progress_bar);
        mLinear1 = findViewById(R.id.layout1);
        mLinear2 = findViewById(R.id.layout2);
        mLinear3 = findViewById(R.id.layout3);
        mLinear4 = findViewById(R.id.layout4);
        mGetStarted = findViewById(R.id.get_started);
        sign_in = findViewById(R.id.sign_in);
        register = findViewById(R.id.register);
        login_btn = findViewById(R.id.login_btn);
        reg_btn = findViewById(R.id.reg_btn);
        email_login = findViewById(R.id.email_login);
        email_reset = findViewById(R.id.email_reset);
        pass_login = findViewById(R.id.pass_login);
        names_reg = findViewById(R.id.names_reg);
        email_reg = findViewById(R.id.email_reg);
        phone_reg = findViewById(R.id.phone_reg);
        pass_reg = findViewById(R.id.pass_reg);
        forgotten_pass = findViewById(R.id.forgotten_pass);
        recovery_text = findViewById(R.id.recovery_text);
        reset_btn = findViewById(R.id.reset_btn);
        mGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetStarted.setVisibility(View.GONE);
                mLinear1.setVisibility(View.VISIBLE);
                mLinear2.setVisibility(View.GONE);
                mLinear3.setVisibility(View.GONE);
                mLinear4.setVisibility(View.GONE);

            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetStarted.setVisibility(View.GONE);
                mLinear1.setVisibility(View.GONE);
                mLinear2.setVisibility(View.VISIBLE);
                mLinear3.setVisibility(View.GONE);
                mLinear4.setVisibility(View.GONE);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetStarted.setVisibility(View.GONE);
                mLinear1.setVisibility(View.GONE);
                mLinear2.setVisibility(View.GONE);
                mLinear3.setVisibility(View.VISIBLE);
                mLinear4.setVisibility(View.GONE);
            }
        });
        forgotten_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetStarted.setVisibility(View.GONE);
                mLinear1.setVisibility(View.GONE);
                mLinear2.setVisibility(View.GONE);
                mLinear3.setVisibility(View.GONE);
                mLinear4.setVisibility(View.VISIBLE);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmailText = email_login.getText().toString().trim();
                String loginPassText = pass_login.getText().toString();
                if (!TextUtils.isEmpty(loginEmailText) && !TextUtils.isEmpty(loginPassText) && Patterns.EMAIL_ADDRESS.matcher(loginEmailText).matches()) {
                    hideKeyboard();
                    mProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmailText, loginPassText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendToMain();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                            mProgress.setVisibility(View.GONE);
                        }
                    });
                } else if (TextUtils.isEmpty(loginEmailText)) {
                    email_login.setError("Empty Field");
                } else if (TextUtils.isEmpty(loginPassText)) {
                    pass_login.setError("Empty Field");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmailText).matches()) {
                    email_login.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  emailInput = email_reset.getText().toString().trim();
                if(!TextUtils.isEmpty(emailInput) && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                    hideKeyboard();
                    mProgress.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(emailInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                               recovery_text.setVisibility(View.VISIBLE);
                            } else {
                                String  error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                            }
                            mProgress.setVisibility(View.GONE);
                        }
                    });
                } else {
                    email_reset.setError("Valid email required");
                }
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String names= names_reg.getText().toString().trim();
                final String email = email_reg.getText().toString();
                final String pass = pass_reg.getText().toString();
                final String phone = phone_reg.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(phone) && phone.length() >= 11 && names.length() >= 6  && !TextUtils.isEmpty(names) && Patterns.EMAIL_ADDRESS.matcher(email).matches()  && pass.length() >= 6) {
                    mProgress.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                Map<String, Object> map = new HashMap<>();
                                map.put("names", names);
                                map.put("email", email);
                                map.put("phone", phone);
                                map.put("password", pass);
                                map.put("timestamp", ServerValue.TIMESTAMP);
                                current_user_db.setValue(map);
                                sendToMain();
                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                            mProgress.setVisibility(View.INVISIBLE);
                        }
                    });

                } else if (TextUtils.isEmpty(email)) {
                    email_reg.setError("Empty Field");
                } else if (TextUtils.isEmpty(pass)) {
                    pass_reg.setError("Empty Field");

                }  else if (TextUtils.isEmpty(names)){
                    names_reg.setError("Empty Field");
                } else if(TextUtils.isEmpty(phone)){
                    phone_reg.setError("Empty Field");
                } else if (phone.length() < 10){
                    phone_reg.setError("Error");
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else if (names.length() < 6 ){
                    names_reg.setError("Error");
                    Toast.makeText(LoginActivity.this, "Full Names should have at least 6 characters", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_reg.setError("Error");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if(pass.length() < 6){
                    pass_reg.setError("Error");
                    Toast.makeText(LoginActivity.this, "Password should have at least 6 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mParentLayout.getWindowToken(), 0);
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(mLinear4.getVisibility() == View.VISIBLE){
            mGetStarted.setVisibility(View.GONE);
            mLinear1.setVisibility(View.GONE);
            mLinear2.setVisibility(View.VISIBLE);
            mLinear3.setVisibility(View.GONE);
            mLinear4.setVisibility(View.GONE);
            recovery_text.setVisibility(View.GONE);
        } else if(mLinear2.getVisibility() == View.VISIBLE){
            mGetStarted.setVisibility(View.GONE);
            mLinear1.setVisibility(View.VISIBLE);
            mLinear2.setVisibility(View.GONE);
            mLinear3.setVisibility(View.GONE);
            mLinear4.setVisibility(View.GONE);
            recovery_text.setVisibility(View.GONE);
        } else if(mLinear3.getVisibility() == View.VISIBLE){
            mGetStarted.setVisibility(View.GONE);
            mLinear1.setVisibility(View.VISIBLE);
            mLinear2.setVisibility(View.GONE);
            mLinear3.setVisibility(View.GONE);
            mLinear4.setVisibility(View.GONE);
            recovery_text.setVisibility(View.GONE);
        } else if(mLinear1.getVisibility() == View.VISIBLE){
            mGetStarted.setVisibility(View.VISIBLE);
            mLinear1.setVisibility(View.GONE);
            mLinear2.setVisibility(View.GONE);
            mLinear3.setVisibility(View.GONE);
            mLinear4.setVisibility(View.GONE);
            recovery_text.setVisibility(View.GONE);
        } else {
            finish();
            super.onBackPressed();
        }
    }
}
