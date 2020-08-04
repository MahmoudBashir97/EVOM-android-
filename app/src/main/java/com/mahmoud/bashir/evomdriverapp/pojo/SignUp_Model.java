package com.mahmoud.bashir.evomdriverapp.pojo;

public class SignUp_Model {

    String id;
    String full_name;
    String birth_date;
    String National_ID;
    String email;
    String phone_no;
    String driver_image;
    String National_ID_pic;

    public SignUp_Model(String id, String full_name, String birth_date, String national_ID, String email, String phone_no,String driver_image,String National_ID_pic) {
        this.id = id;
        this.full_name = full_name;
        this.birth_date = birth_date;
        National_ID = national_ID;
        this.email = email;
        this.phone_no = phone_no;
        this.driver_image=driver_image;
        this.National_ID_pic=National_ID_pic;
    }

    public String getNational_ID_pic() {
        return National_ID_pic;
    }

    public void setNational_ID_pic(String national_ID_pic) {
        National_ID_pic = national_ID_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getNational_ID() {
        return National_ID;
    }

    public void setNational_ID(String national_ID) {
        National_ID = national_ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }
}
