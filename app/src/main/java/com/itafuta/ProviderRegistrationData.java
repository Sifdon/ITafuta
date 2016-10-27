package com.itafuta;

import java.util.List;
import java.util.Map;

/**
 * Created by Victor on 25/10/2016.
 */



public class ProviderRegistrationData{


    private String  provImage, provContact, provIdFront, providerIdBack, provLocation, provName, provDetails, provFavCount;
    float provRate;





    Map<String, Object> provOccupation;

    //Empty constructor
    ProviderRegistrationData(){
    }

    //int, int, int, string, string, string, string, float
    ProviderRegistrationData(String  mImage, String mContact, String mIdFront, int mIdBack, Map<String, Object> mOccupation, String mLocation, String mDetails, String mName, String mFavCount, float mProvRate){
        this.provImage = mImage;
        this.provContact = mContact;
        this.provLocation = mLocation;
        this.provDetails = mDetails;
        this.provName = mName;
        this.provFavCount= mFavCount;
        this.provRate = mProvRate;
        this.provOccupation =  mOccupation;
    }

    public String getProvImage(){
        return provImage;
    }
    public String getProvContact() {
        return provContact;
    }
    public void setProvContact(String provContact) {
        this.provContact = provContact;
    }
    public String getProvIdFront() {
        return provIdFront;
    }
    public String getProviderIdBack() {
        return providerIdBack;
    }
    public String getProvLocation() {
        return provLocation;
    }
    public Map<String, Object> getProvOccupation() {
        return provOccupation;
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
    public void setProvImage(String image) {
        this.provImage = image;
    }
    public void setProvIdFront(String provIdFront) {
        this.provIdFront = provIdFront;
    }
    public void setProviderIdBack(String providerIdBack) {
        this.providerIdBack = providerIdBack;
    }
    public void setProvLocation(String provLocation) {
        this.provLocation = provLocation;
    }
    public void setProvOccupation(Map<String, Object> provOccupation) {
        this.provOccupation = provOccupation;
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
