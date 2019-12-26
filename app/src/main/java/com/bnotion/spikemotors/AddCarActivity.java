package com.bnotion.spikemotors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private Toolbar mToolbar;
    private EditText car_name_field, fuel_type_field, mEngine, no_of_seats, mFuelEconomy, mAirCondition, hourly_price_field,
            daily_price_field;
    private int myNum = 1;
    private ScrollView scrollView;
    private UploadTask mUploadTask;
    private ProgressBar progress;
    private UploadListAdapter uploadListAdapter;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private Uri mainImageURI = null;
    private List<String> fileDoneList;
    private List<String> fileNameList;
    private Spinner price_spinner, category_spinner;
    private TextView upload_instruction, number_photos, photo_text;
    private ImageView mUploadCar;
    private RecyclerView recycler_photos;
    private static final int PICK_IMAGE_FILE = 10;
    private Button mPostCar;
    private ArrayList<String> image_urls;
    private String fileName = "null";
    private String selected_category = "Category";
    private String selected_price = "Daily Price Range";
    private String downloadUri = "null";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BringImagePicker();
                } else {
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

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
        setContentView(R.layout.activity_add_car);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, progress);
        storageReference = FirebaseStorage.getInstance().getReference();
        image_urls = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        fileNameList = new ArrayList<>();
        car_name_field = findViewById(R.id.car_name_field);
        fuel_type_field = findViewById(R.id.fuel_type_field);
        mEngine = findViewById(R.id.engine);
        scrollView = findViewById(R.id.scrollView);
        no_of_seats = findViewById(R.id.no_of_seats);
        mFuelEconomy = findViewById(R.id.fuel_economy);
        mAirCondition = findViewById(R.id.air_condition);
        hourly_price_field = findViewById(R.id.hourly_price_field);
        daily_price_field = findViewById(R.id.daily_price_field);
        category_spinner = findViewById(R.id.category_spinner);
        price_spinner = findViewById(R.id.price_spinner);
        upload_instruction = findViewById(R.id.upload_instruction);
        number_photos = findViewById(R.id.number_photos);
        photo_text = findViewById(R.id.photo_text);
        mUploadCar = findViewById(R.id.upload_car);
        recycler_photos = findViewById(R.id.recycler_photos);
        mPostCar = findViewById(R.id.postCar);
        //set spinner adapter
        ArrayAdapter<CharSequence> adapter = android.widget.ArrayAdapter.createFromResource(this, R.array.Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_category = category_spinner.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter2 = android.widget.ArrayAdapter.createFromResource(this, R.array.price_range, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        price_spinner.setAdapter(adapter2);
        price_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_price= price_spinner.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //handling oposting process
        mPostCar.setOnClickListener(v -> {
            // fetching string values
            final String car_name = car_name_field.getText().toString().trim();
            final String engine = mEngine.getText().toString().trim();
            final String fuel_type = fuel_type_field.getText().toString().trim();
            final String fuel_economy = mFuelEconomy.getText().toString().trim();
            final String air_condition = mAirCondition.getText().toString().trim();
            final String no_seats = no_of_seats.getText().toString().trim();
            final String hourly_price = hourly_price_field.getText().toString().trim();
            final String daily_price = daily_price_field.getText().toString().trim();
            if (!selected_category.equals("Category") && !selected_price.equals("Daily Price Range") &&!TextUtils.isEmpty(car_name) && !TextUtils.isEmpty(engine) && !TextUtils.isEmpty(fuel_type) && !TextUtils.isEmpty(fuel_economy) && !TextUtils.isEmpty(fuel_type) && !TextUtils.isEmpty(air_condition) && !TextUtils.isEmpty(no_seats) && !TextUtils.isEmpty(hourly_price) && !TextUtils.isEmpty(daily_price) && image_urls.size() != 0) {
                progressBar.setVisibility(View.VISIBLE);
                Map<String, Object> map = new HashMap<>();
                map.put("image_urls", image_urls);
                map.put("name", car_name);
                map.put("fuel_type", fuel_type);
                map.put("fuel_economy", fuel_economy);
                map.put("air_condition", air_condition);
                map.put("no_seats", no_seats);
                map.put("hourly_price", hourly_price);
                map.put("daily_price", daily_price);
                map.put("engine", engine);
                map.put("category", selected_category);
                map.put("price_range", selected_price);
                firebaseFirestore.collection("Posts").add(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                        Toast.makeText(AddCarActivity.this, "New Post Added", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(AddCarActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();

                    }
                    progressBar.setVisibility(View.GONE);
                });

            } else if (TextUtils.isEmpty(car_name)) {
                car_name_field.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(engine)) {
                mEngine.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(fuel_type)) {
                fuel_type_field.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(fuel_economy)) {
                mFuelEconomy.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(air_condition)) {
                mAirCondition.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(no_seats)) {
                no_of_seats.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(hourly_price)) {
                hourly_price_field.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(daily_price)) {
                daily_price_field.setError("Empty Field");
                Toast.makeText(AddCarActivity.this, "You missed something. Check for errors", Toast.LENGTH_LONG).show();
            } else if (image_urls.size() == 0) {
                Toast.makeText(AddCarActivity.this, "Please upload interior images of the car", Toast.LENGTH_SHORT).show();
            }else if (selected_category.equals("Category")) {
                Toast.makeText(AddCarActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
            }else if (selected_price.equals("Daily Price Range")) {
                Toast.makeText(AddCarActivity.this, "Please select a daily price range", Toast.LENGTH_SHORT).show();
            }
        });
        mUploadCar.setOnClickListener(view -> postImages());

    }

    private void postImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddCarActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddCarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                BringImagePicker();
            }
        } else {
            BringImagePicker();
        }
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddCarActivity.this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressBar.setVisibility(View.VISIBLE);
                for (int i = 0; i < 1; i++) {
                    mainImageURI = result.getUri();
                    fileName = getFileName(mainImageURI);
                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();
                    mUploadCar.setImageURI(mainImageURI);
                    String user_id = mAuth.getCurrentUser().getUid();
                    final StorageReference postPath = storageReference.child("Images").child(user_id).child(String.valueOf(System.currentTimeMillis())).child(fileName);
                    final int finalI = i;
                    mUploadTask = postPath.putFile(mainImageURI);
                    Task<Uri> urlTask = mUploadTask.continueWithTask(task -> {
                        if (task.isSuccessful()) {
                        }
                        // Continue with the task to get the download URL
                        return postPath.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            final Uri downloadUri = task.getResult();
                            number_photos.setText("" + myNum++);
                            image_urls.add(String.valueOf(downloadUri));
                            //   fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                }
            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }

        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
