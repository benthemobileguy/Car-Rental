package com.bnotion.spikemotors.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bnotion.spikemotors.ListCarsActivity;
import com.bnotion.spikemotors.R;
import com.bnotion.spikemotors.adapter.SliderAdapter;

public class HomeFragment extends Fragment {
    private ViewPager viewPager;
    private TextView[] mDots;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    //viewPager auto slider
    private int page = 0;
    private Handler handler;
    private int delay = 5000; //milliseconds
    Runnable runnable = new Runnable() {
        public void run() {
            if (sliderAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        handler = new Handler();
        //set view pager
        viewPager = view.findViewById(R.id.viewPager);
        mDotLayout = view.findViewById(R.id.dotsLayout);
        sliderAdapter  = new SliderAdapter(getContext());
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(pageChangeListener);
        CardView honda = view.findViewById(R.id.honda);
        CardView toyota = view.findViewById(R.id.toyota);
        CardView ford = view.findViewById(R.id.ford);
        CardView jeep = view.findViewById(R.id.jeep);
        CardView nissan = view.findViewById(R.id.nissan);
        CardView lexus= view.findViewById(R.id.lexus);
        CardView audi = view.findViewById(R.id.audi);
        CardView volvo = view.findViewById(R.id.volvo);
        CardView kia = view.findViewById(R.id.kia);
        CardView peugeot = view.findViewById(R.id.peugeot);
        CardView mercedes = view.findViewById(R.id.mercedez_benz);
        CardView bmw = view.findViewById(R.id.bmw);
        honda.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Honda");
            startActivity(intent);
           getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        toyota.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Toyota");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        ford.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Ford");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        jeep.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Jeep");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        nissan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Nissan");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        lexus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Lexus");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        audi.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Audi");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        volvo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Volvo");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        kia.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Kia");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        peugeot.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Peugeot");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        mercedes.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","Mercedes-Benz");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        bmw.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListCarsActivity.class);
            intent.putExtra("category","BMW");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        return view;
    }
    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(getContext());
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.dark_gray));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length >0){
            mDots[position].setTextColor(getResources().getColor(R.color.custom_red));
        }
    }
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
