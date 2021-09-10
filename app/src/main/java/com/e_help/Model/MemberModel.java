package com.e_help.Model;


public class MemberModel {
    public String id;
    public String id_team;
    public String id_user;
    public String email_user;
    public String f_name_user;
    public String lname_user;

    public boolean accepted;

    public MemberModel() {
    }

    public MemberModel(String id, String id_team, String id_user, String email_user,String f_name_user,String lname_user, boolean accepted) {
        this.id = id;
        this.id_team = id_team;
        this.id_user = id_user;
        this.email_user = email_user;
        this.f_name_user = f_name_user;
        this.lname_user = lname_user;

        this.accepted = accepted;

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

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getFname_user() {
        return f_name_user;
    }

    public void setFname_user(String f_name_user) {
        this.f_name_user = f_name_user;
    }

    public String getLname_user() {
        return lname_user;
    }

    public void setLname_user(String lname_user) {
        this.lname_user = lname_user;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
