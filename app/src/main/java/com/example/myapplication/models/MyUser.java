package com.example.myapplication.models;

import java.util.Date;

public class MyUser {
    int FstId;
    String FSguidId;
    Date FScTime;
    String FSuserName;

    public int getFstId() {
        return FstId;
    }

    public void setFstId(int fstId) {
        FstId = fstId;
    }

    public String getFSguidId() {
        return FSguidId;
    }

    public void setFSguidId(String FSguidId) {
        this.FSguidId = FSguidId;
    }

    public Date getFScTime() {
        return FScTime;
    }

    public void setFScTime(Date FScTime) {
        this.FScTime = FScTime;
    }

    public String getFSuserName() {
        return FSuserName;
    }

    public void setFSuserName(String FSuserName) {
        this.FSuserName = FSuserName;
    }
}
