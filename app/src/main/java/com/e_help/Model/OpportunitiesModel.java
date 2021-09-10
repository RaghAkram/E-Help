package com.e_help.Model;


import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OpportunitiesModel {
    public String id;
    public String name;
    public String description;
    public String type;
    public String name_organization;
    public String skills;
    public String num_hour;
    public String num_valunteer;
    public String gender;
    public String city;
    public String time_start;
    public String time_end;
    public boolean actual;
    public boolean degree;

    public double lat;
    public double lng;

    long createdAt;

    public String key_user_added;
    boolean Active;

    public OpportunitiesModel(String id, String name, String description, String type, String name_organization, String skills, String num_hour, String num_valunteer, String gender, String city, String time_start, String time_end, boolean actual, boolean degree, double lat, double lng, String key_user_added, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.name_organization = name_organization;
        this.skills = skills;
        this.num_hour = num_hour;
        this.num_valunteer = num_valunteer;
        this.gender = gender;
        this.city = city;
        this.time_start = time_start;
        this.time_end = time_end;
        this.actual = actual;
        this.degree = degree;
        this.lat = lat;
        this.lng = lng;
        this.key_user_added = key_user_added;
        Active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName_organization() {
        return name_organization;
    }

    public void setName_organization(String name_organization) {
        this.name_organization = name_organization;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getNum_hour() {
        return num_hour;
    }

    public void setNum_hour(String num_hour) {
        this.num_hour = num_hour;
    }

    public String getNum_valunteer() {
        return num_valunteer;
    }

    public void setNum_valunteer(String num_valunteer) {
        this.num_valunteer = num_valunteer;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public boolean isDegree() {
        return degree;
    }

    public void setDegree(boolean degree) {
        this.degree = degree;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getKey_user_added() {
        return key_user_added;
    }

    public void setKey_user_added(String key_user_added) {
        this.key_user_added = key_user_added;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public OpportunitiesModel() {
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Exclude
    public String getFormattedTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
        Date date = new Date();
        date.setTime(createdAt);
        return sdf.format(date);
    }
}



