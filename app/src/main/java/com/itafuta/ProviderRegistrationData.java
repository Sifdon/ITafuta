package com.itafuta;

/**
 * Created by Victor on 25/10/2016.
 */



public class ProviderRegistrationData{


    private String  provLocation, provName, provDetails, provFavCount;
    int provImage, provIdFront,  providerIdBack;
    float provRate;

    //Empty constructor
    ProviderRegistrationData(){
    }

    //int, int, int, string, string, string, string, float
    ProviderRegistrationData(int  mImage, int mIdFront, int mIdBack, String mLocation, String mDetails, String mName, String mFavCount, float mProvRate){
        this.provImage = mImage;
        this.provLocation = mLocation;
        this.provDetails = mDetails;
        this.provName = mName;
        this.provFavCount= mFavCount;
        this.provRate = mProvRate;
    }

    public int getProvImage(){
        return provImage;
    }
    public int getProvIdFront() {
        return provIdFront;
    }
    public int getProviderIdBack() {
        return providerIdBack;
    }
    public String getProvLocation() {
        return provLocation;
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
    public String getProvName(){
        return  provName;
    }

    //SETTERS
    public void setProvImage(int image) {
        this.provImage = image;
    }
    public void setProvIdFront(int provIdFront) {
        this.provIdFront = provIdFront;
    }
    public void setProviderIdBack(int providerIdBack) {
        this.providerIdBack = providerIdBack;
    }
    public void setProvLocation(String provLocation) {
        this.provLocation = provLocation;
    }
    public void setProvDetails(String details){
        this.provDetails = details;
    }
    public void setProvFavCount(String favCount){
        this.provFavCount = favCount;
    }
    public void setProvName(String name){
        this.provName = name;
    }
    public void setProvRate(float provRate) {
        this.provRate = provRate;
    }
}
