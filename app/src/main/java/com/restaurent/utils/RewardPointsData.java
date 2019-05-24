package com.restaurent.utils;

public class RewardPointsData {


    private String mDateAdded;
    private String mDescription;
    private String mPoints;

    public RewardPointsData(String dateAdded, String description, String points) {
        this.mDateAdded = dateAdded;
        this.mDescription = description;
        this.mPoints = points;
    }

    public String getmDateAdded() {
        return mDateAdded;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmPoints() {
        return mPoints;
    }
}
