package com.e_help.Model;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {
    //متطوع
    String id;
    String first_name;
    String last_name;
    String phone_num;
    String id_num;
    String email;
    String date_of_birth;
    String Voluntary_interests;
    String Educational_level;
    String genderName;
    int gender_id;
    int point;
    //فريق
    String name_team;
    String leader_name;
    String phone_num_team;
    String id_num_leader;
    String social_media;
    String the_fieldName;
    int the_field_id;
    String cityName;
    int city_id;
    String description;
    boolean is_ads;
    //منظمة
    String name_entity;
    String phone_num_entity;
    String id_num_entity;
  //  String email_team_entity;
    String entity_typeName;
    int entity_type_id;
    String the_field_entityName;
    int the_field_entity_id;
    String city_entityName;
    int city_entity_id;
    int user_type;
    long createdAt;
    String Image_url;

    boolean Active;

    public User() {
    }
// متطوع
    public User(String id, String first_name, String last_name, String phone_num, String id_num,
                String email, String date_of_birth, String voluntary_interests, String educational_level, String genderName,
                int gender_id, String city_entityName, int city_entity_id, int user_type,boolean Active,int point) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_num = phone_num;
        this.id_num = id_num;
        this.email = email;
        this.date_of_birth = date_of_birth;
        Voluntary_interests = voluntary_interests;
        Educational_level = educational_level;
        this.genderName = genderName;
        this.gender_id = gender_id;
        this.city_entityName = city_entityName;
        this.city_entity_id = city_entity_id;
        this.user_type = user_type;

        createdAt = new Date().getTime();
        this.Active = Active;
        this.point=point;
    }
// فريق
    public User(String id, String name_team, String leader_name, String phone_num_team, String id_num_leader, String email_team, String social_media, String the_fieldName, int the_field_id, String cityName, int city_id, int user_type,boolean is_ads,boolean Active) {
        this.id = id;
        this.name_team = name_team;
        this.leader_name = leader_name;
        this.phone_num_team = phone_num_team;
        this.id_num_leader = id_num_leader;
        this.email = email_team;
        this.social_media = social_media;
        this.the_fieldName = the_fieldName;
        this.the_field_id = the_field_id;
        this.cityName = cityName;
        this.city_id = city_id;
        this.user_type = user_type;
        createdAt = new Date().getTime();
        this.is_ads = is_ads;
        this.Active = Active;
    }

    public User(String id, String name_team, String leader_name, String phone_num_team, String id_num_leader, String email_team, String social_media, String the_fieldName, int the_field_id, String cityName, int city_id, int user_type,String description,boolean is_ads,boolean Active) {
        this.id = id;
        this.name_team = name_team;
        this.leader_name = leader_name;
        this.phone_num_team = phone_num_team;
        this.id_num_leader = id_num_leader;
        this.email = email_team;
        this.social_media = social_media;
        this.the_fieldName = the_fieldName;
        this.the_field_id = the_field_id;
        this.cityName = cityName;
        this.city_id = city_id;
        this.user_type = user_type;
        createdAt = new Date().getTime();
        this.description = description;
        this.is_ads = is_ads;
        this.Active = Active;
    }
    // منظمة
    public User(String id, String name_entity, String phone_num_entity, String id_num_entity, String email_team_entity, String entity_typeName, int entity_type_id, String the_field_entityName, int the_field_entity_id, String city_entityName, int city_entity_id, int user_type,boolean Active) {
        this.id = id;
        this.name_entity = name_entity;
        this.phone_num_entity = phone_num_entity;
        this.id_num_entity = id_num_entity;
        this.email = email_team_entity;
        this.entity_typeName = entity_typeName;
        this.entity_type_id = entity_type_id;
        this.the_field_entityName = the_field_entityName;
        this.the_field_entity_id = the_field_entity_id;
        this.city_entityName = city_entityName;
        this.city_entity_id = city_entity_id;
        this.user_type = user_type;
        createdAt = new Date().getTime();
        this.Active = Active;

    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public boolean isIs_ads() {
        return is_ads;
    }

    public void setIs_ads(boolean is_ads) {
        this.is_ads = is_ads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getVoluntary_interests() {
        return Voluntary_interests;
    }

    public void setVoluntary_interests(String voluntary_interests) {
        Voluntary_interests = voluntary_interests;
    }

    public String getEducational_level() {
        return Educational_level;
    }

    public void setEducational_level(String educational_level) {
        Educational_level = educational_level;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }

    public String getName_team() {
        return name_team;
    }

    public void setName_team(String name_team) {
        this.name_team = name_team;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getPhone_num_team() {
        return phone_num_team;
    }

    public void setPhone_num_team(String phone_num_team) {
        this.phone_num_team = phone_num_team;
    }

    public String getId_num_leader() {
        return id_num_leader;
    }

    public void setId_num_leader(String id_num_leader) {
        this.id_num_leader = id_num_leader;
    }



    public String getSocial_media() {
        return social_media;
    }

    public void setSocial_media(String social_media) {
        this.social_media = social_media;
    }

    public String getThe_fieldName() {
        return the_fieldName;
    }

    public void setThe_fieldName(String the_fieldName) {
        this.the_fieldName = the_fieldName;
    }

    public int getThe_field_id() {
        return the_field_id;
    }

    public void setThe_field_id(int the_field_id) {
        this.the_field_id = the_field_id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName_entity() {
        return name_entity;
    }

    public void setName_entity(String name_entity) {
        this.name_entity = name_entity;
    }

    public String getPhone_num_entity() {
        return phone_num_entity;
    }

    public void setPhone_num_entity(String phone_num_entity) {
        this.phone_num_entity = phone_num_entity;
    }

    public String getId_num_entity() {
        return id_num_entity;
    }

    public void setId_num_entity(String id_num_entity) {
        this.id_num_entity = id_num_entity;
    }


    public String getEntity_typeName() {
        return entity_typeName;
    }

    public void setEntity_typeName(String entity_typeName) {
        this.entity_typeName = entity_typeName;
    }

    public int getEntity_type_id() {
        return entity_type_id;
    }

    public void setEntity_type_id(int entity_type_id) {
        this.entity_type_id = entity_type_id;
    }

    public String getThe_field_entityName() {
        return the_field_entityName;
    }

    public void setThe_field_entityName(String the_field_entityName) {
        this.the_field_entityName = the_field_entityName;
    }

    public int getThe_field_entity_id() {
        return the_field_entity_id;
    }

    public void setThe_field_entity_id(int the_field_entity_id) {
        this.the_field_entity_id = the_field_entity_id;
    }

    public String getCity_entityName() {
        return city_entityName;
    }

    public void setCity_entityName(String city_entityName) {
        this.city_entityName = city_entityName;
    }

    public int getCity_entity_id() {
        return city_entity_id;
    }

    public void setCity_entity_id(int city_entity_id) {
        this.city_entity_id = city_entity_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage_url() {
        return Image_url;
    }

    public void setImage_url(String image_url) {
        Image_url = image_url;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Exclude
    public String getFormattedTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Date date = new Date();
        date.setTime(createdAt);
        return sdf.format(date);
    }
}
