package com.bnotion.spikemotors.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bnotion.spikemotors.CarDetailActivity;
import com.bnotion.spikemotors.R;
import com.bnotion.spikemotors.model.Cars;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CarRecyclerAdapter extends RecyclerView.Adapter <CarRecyclerAdapter.ViewHolder>{
    List<Cars> postList;
    private Context context;
    private Activity activity;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String user_id;

    public CarRecyclerAdapter(List<Cars> post_list, Context context) {
        this.postList = post_list;
        this.activity = (Activity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_view, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String blogPostID = postList.get(position).BlogPostId;
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final String uniqueID = UUID.randomUUID().toString();
        if(firebaseUser!=null){
            user_id = firebaseAuth.getCurrentUser().getUid();
        }
        final String car_name = postList.get(position).getName();
        final String fuelType = postList.get(position).getFuel_type();
        final String fuelEconomy = postList.get(position).getFuel_economy();
        final String airCondition = postList.get(position).getAir_condition();
        final String no_seats = postList.get(position).getNo_seats();
        final String hourlyPrice = postList.get(position).getHourly_price();
        final String dailyPrice = postList.get(position).getDaily_price();
        final String price_range = postList.get(position).getPrice_range();
        final String engine = postList.get(position).getEngine();
        final String car_category = postList.get(position).getCategory();
        final ArrayList<String> car_image = postList.get(position).getImage_urls();
        holder.setCarNameText(car_name);
        holder.setCategoryText(car_category);
        holder.setCarDailyPrice("$"+dailyPrice);
        //set Post Image
        final String image_url = postList.get(position).getImage_urls().get(0);
        final ArrayList<String> image_urls = postList.get(position).getImage_urls();
         holder.setCarImage(image_url);
         holder.cardView.setOnClickListener(v -> {
             Intent intent = new Intent(activity, CarDetailActivity.class);
             intent.putExtra("name", car_name);
             intent.putExtra("fuel_type", fuelType);
             intent.putExtra("fuel_economy", fuelEconomy);
             intent.putExtra("airCondition", airCondition);
             intent.putExtra("no_seats", no_seats);
             intent.putExtra("hourly_price", hourlyPrice);
             intent.putExtra("daily_price", dailyPrice);
             intent.putExtra("engine", engine);
             intent.putExtra("price_range", price_range);
             intent.putExtra("imageURL", image_url);
             intent.putExtra("imageURLS", image_urls);
             activity.startActivity(intent);
             activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
         });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mCategory, mCarName, mDailyPrice;
        private ImageView mCarImage;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardView = mView.findViewById(R.id.parent);
            mCarImage = mView.findViewById(R.id.image_car);

        }

        public void setCategoryText(String text) {
            mCategory = mView.findViewById(R.id.category);
            mCategory.setText(text);
        }

        public void setCarNameText(String text) {
            mCarName = mView.findViewById(R.id.car_name);
            mCarName.setText(text);
        }
        public void setCarDailyPrice(String text) {
            mDailyPrice= mView.findViewById(R.id.daily_price);
            mDailyPrice.setText(text);
        }

        public void setCarImage(String downloadUri) {
            RequestOptions placeholderRequest = new RequestOptions().fitCenter();
            placeholderRequest.placeholder(R.drawable.placeholder);
            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(downloadUri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(mCarImage);
        }

    }

    public void updateList(List<Cars> newList) {
        postList = new ArrayList<>();
        postList.addAll(newList);
        notifyDataSetChanged();
    }
}
