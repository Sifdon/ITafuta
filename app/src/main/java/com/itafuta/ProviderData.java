package com.itafuta;

import java.util.Map;

/**
 * Created by victor on 9/19/16.
 */
public class ProviderData {


    private String location, username, contact, provFavCount, userid;
    Map<String, Object> occupation;
    String profPhoto;
    float provRate;

    //Empty constructor
    ProviderData(){
    }

    //int, int, int, string, string, string, string, float
    ProviderData(String  mImage, String mLocation, Map<String, Object> mDetails, String mName, String mFavCount, float mProvRate){
        this.profPhoto = mImage;
        this.location = mLocation;
        this.occupation = mDetails;
        this.username = mName;
        this.provFavCount= mFavCount;
        this.provRate = mProvRate;
    }

    public String getProfPhoto(){
        return profPhoto;
    }
    public String getLocation() {
        return location;
    }
    public Map<String, Object> getOccupation(){
        return occupation;
    }
    public String getProvFavCount(){
        return provFavCount;
    }
    public float getProvRate() {
        return provRate;
    }
    public String getUsername(){
        return username;
    }

    //SETTERS
    public void setProfPhoto(String image) {
        this.profPhoto = image;
    }
    public void setLocation(String provLocation) {
        this.location = provLocation;
    }
    public void setOccupation(Map<String, Object> details){
        this.occupation = details;
    }
    public void setProvFavCount(String favCount){
        this.provFavCount = favCount;
    }
    public void setUsername(String name){
        this.username = name;
    }
    public void setProvRate(float provRate) {
        this.provRate = provRate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
