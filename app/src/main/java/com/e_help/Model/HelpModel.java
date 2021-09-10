package com.e_help.Model;


import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
// مودل خاصة بالمساعدة الفورية + صفحة التحديثات
public class HelpModel {
    public String id;
    public String name;
    public String number;
    public String type;
    public String location;
    public String city;
    public String img_url;
    public String details;
    public String time_end;
    long createdAt;
    long EndAt;
    public String key_user_added;
    boolean Active;

    List<LatestUpdatesModel>latestUpdatesModels;

    public HelpModel() {
    }

    public HelpModel(String id, String name, String number, String type, String location,
                     String city, String img_url, String details, String time_end, long endAt,String key_user_added,boolean Active) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.location = location;
        this.city = city;
        this.img_url = img_url;
        this.details = details;
        this.time_end = time_end;
        createdAt = new Date().getTime();
        EndAt = endAt;
        this.key_user_added = key_user_added;
        this.Active=Active;

    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getKey_user_added() {
        return key_user_added;
    }

    public void setKey_user_added(String key_user_added) {
        this.key_user_added = key_user_added;
    }

    public List<LatestUpdatesModel> getLatestUpdatesModels() {
        return latestUpdatesModels;
    }

    public void setLatestUpdatesModels(List<LatestUpdatesModel> latestUpdatesModels) {
        this.latestUpdatesModels = latestUpdatesModels;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getEndAt() {
        return EndAt;
    }

    public void setEndAt(long endAt) {
        EndAt = endAt;
    }
    public static class LatestUpdatesModel {
        public String id;
        public String name;
        public String img_url;
        long createdAt;

        public LatestUpdatesModel() {
        }

        public LatestUpdatesModel(String id, String name, String img_url) {
            this.id = id;
            this.name = name;
            this.img_url = img_url;
            createdAt = new Date().getTime();

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

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
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


}
