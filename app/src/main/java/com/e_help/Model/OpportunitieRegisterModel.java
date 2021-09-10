package com.e_help.Model;

public class OpportunitieRegisterModel {
    public String id;
    public String nameOpportunitie;
    public String name_team;
    public String name_member;
    public String idTeam;
    public String id_member;
    public boolean actual;
    public double lat;
    public double lng;
    public String city;
    public boolean confirmAttendance;
    public boolean accepted;

    public int num_h;

    public OpportunitieRegisterModel() {
    }

    public OpportunitieRegisterModel(String id, String nameOpportunitie, String nameTeam, String nameMember, String idTeam, String id_member, boolean actual, double lat, double lng, String city, boolean confirmAttendance,boolean accepted,int num_h) {
        this.id = id;
        this.nameOpportunitie = nameOpportunitie;
        this.name_team = nameTeam;
        this.name_member = nameMember;
        this.idTeam = idTeam;
        this.id_member = id_member;
        this.actual = actual;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.confirmAttendance = confirmAttendance;
        this.accepted = accepted;
        this.num_h=num_h;

    }

    public String getName_team() {
        return name_team;
    }

    public void setName_team(String name_team) {
        this.name_team = name_team;
    }

    public String getName_member() {
        return name_member;
    }

    public void setName_member(String name_member) {
        this.name_member = name_member;
    }

    public int getNum_h() {
        return num_h;
    }

    public void setNum_h(int num_h) {
        this.num_h = num_h;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameOpportunitie() {
        return nameOpportunitie;
    }

    public void setNameOpportunitie(String nameOpportunitie) {
        this.nameOpportunitie = nameOpportunitie;
    }

    public String getNameTeam() {
        return name_team;
    }

    public void setNameTeam(String nameTeam) {
        this.name_team = nameTeam;
    }

    public String getNameMember() {
        return name_member;
    }

    public void setNameMember(String nameMember) {
        this.name_member = nameMember;
    }

    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getId_member() {
        return id_member;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isConfirmAttendance() {
        return confirmAttendance;
    }

    public void setConfirmAttendance(boolean confirmAttendance) {
        this.confirmAttendance = confirmAttendance;
    }
}



