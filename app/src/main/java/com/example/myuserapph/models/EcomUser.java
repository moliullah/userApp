package com.example.myuserapph.models;

public class EcomUser {
    private String uid;
    private String name;
    private String email;
    private String photo;
    private String deleveryAddress;
    private String phoneNumber;
    public EcomUser() {
    }

    public EcomUser(String uid, String name, String email, String photo, String phoneNumber) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getDeleveryAddress() {
        return deleveryAddress;
    }

    public void setDeleveryAddress(String deleveryAddress) {
        this.deleveryAddress = deleveryAddress;
    }
}
