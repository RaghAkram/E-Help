package com.e_help.Model;


import java.util.List;

public class RequestModel {

    public String id;
    public String id_team;
    public String id_userJoin;
    public String fname;
    public String lname;
    public String email_u;

    public RequestModel() {
    }


    public RequestModel(String id, String id_team, String id_userJoin, String fname, String lname, String email_u) {
        this.id = id;
        this.id_team = id_team;
        this.id_userJoin = id_userJoin;
        this.fname = fname;
        this.lname = lname;
        this.email_u = email_u;
    }

    public String getEmail() {
        return email_u;
    }

    public void setEmail(String email_u) {
        this.email_u = email_u;
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

    public String getId_userJoin() {
        return id_userJoin;
    }

    public void setId_userJoin(String id_userJoin) {
        this.id_userJoin = id_userJoin;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}


