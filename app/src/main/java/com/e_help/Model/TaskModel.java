package com.e_help.Model;


public class TaskModel {
    public String id;
    public String id_team;
    public String title;
    public String description;
    public String expired;
    public String user;
    public String user_id;

    public TaskModel() {
    }

    public TaskModel(String id, String id_team, String title, String description, String expired, String user, String user_id) {
        this.id = id;
        this.id_team = id_team;
        this.title = title;
        this.description = description;
        this.expired = expired;
        this.user = user;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_team() {
        return id_team;
    }

    public void setId_team(String id_team) {
        this.id_team = id_team;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
