package com.itafuta;

/**
 * Created by victor on 9/19/16.
 */
public class ProviderData {


    private String location, username, provDetails, provFavCount;
    String profPhoto;
    float provRate;

    //Empty constructor
    ProviderData(){
    }

    //int, int, int, string, string, string, string, float
    ProviderData(String  mImage, String mLocation, String mDetails, String mName, String mFavCount, float mProvRate){
        this.profPhoto = mImage;
        this.location = mLocation;
        this.provDetails = mDetails;
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
    public String getProvDetails(){
        return provDetails;
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
    public void setProvDetails(String details){
        this.provDetails = details;
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
}
