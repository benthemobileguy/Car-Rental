package com.bnotion.spikemotors.model;

import com.bnotion.spikemotors.BlogPostId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Cars extends com.bnotion.spikemotors.BlogPostId implements Serializable {
    public String name;
    public String fuel_type;
    public String fuel_economy;
    public String air_condition;
    public String no_seats;
    public String hourly_price;
    public String daily_price;
    public String category;
    public String engine;
    private String price_range;
    public Date timestamp;
    public ArrayList<String> image_urls;

    public Cars() {

    }

    public Cars(String name, String fuel_type, String engine, String fuel_economy, String air_condition, String no_seats, String hourly_price, String daily_price, String category, String price_range, Date timestamp, ArrayList<String> image_urls) {
        this.name = name;
        this.fuel_type = fuel_type;
        this.fuel_economy = fuel_economy;
        this.air_condition = air_condition;
        this.no_seats = no_seats;
        this.hourly_price = hourly_price;
        this.daily_price = daily_price;
        this.category = category;
        this.price_range = price_range;
        this.timestamp = timestamp;
        this.image_urls = image_urls;
        this.engine = engine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getFuel_economy() {
        return fuel_economy;
    }

    public void setFuel_economy(String fuel_economy) {
        this.fuel_economy = fuel_economy;
    }

    public String getAir_condition() {
        return air_condition;
    }

    public void setAir_condition(String air_condition) {
        this.air_condition = air_condition;
    }

    public String getNo_seats() {
        return no_seats;
    }

    public void setNo_seats(String no_seats) {
        this.no_seats = no_seats;
    }

    public String getHourly_price() {
        return hourly_price;
    }

    public void setHourly_price(String hourly_price) {
        this.hourly_price = hourly_price;
    }

    public String getDaily_price() {
        return daily_price;
    }

    public void setDaily_price(String daily_price) {
        this.daily_price = daily_price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(ArrayList<String> image_urls) {
        this.image_urls = image_urls;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
}
