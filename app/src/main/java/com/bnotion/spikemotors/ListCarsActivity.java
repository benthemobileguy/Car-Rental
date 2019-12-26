package com.bnotion.spikemotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bnotion.spikemotors.adapter.CarRecyclerAdapter;
import com.bnotion.spikemotors.model.Cars;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListCarsActivity extends AppCompatActivity {
    private static final String TAG = "ERROR:";
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private List<Cars> post_list;
    private ProgressBar mProgressBar;
    private FirebaseFirestore firebaseFirestore;
    private CarRecyclerAdapter carRecyclerAdapter;
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
        setContentView(R.layout.activity_list_cars);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        TextView toolbarTitle = findViewById(R.id.toolbar_text);
        toolbarTitle.setText(category);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        post_list = new ArrayList<>();
        mTextView =  findViewById(R.id.nothing_text);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recyclerView);
        carRecyclerAdapter = new CarRecyclerAdapter(post_list, ListCarsActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(carRecyclerAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").whereEqualTo("category", category).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e!=null){
                Log.d(TAG,"Error:"+e.getMessage());
            } else {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String blogPostID = doc.getDocument().getId();
                        Cars cars = doc.getDocument().toObject(Cars.class).withId(blogPostID);
                        post_list.add(cars);
                        carRecyclerAdapter.notifyDataSetChanged();
                        mTextView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
        Query mQuery = firebaseFirestore.collection("Posts").whereEqualTo("category", category);
        mQuery.get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()){
                mRecyclerView.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
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
