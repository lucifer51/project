package com.example.lucifer.androidappforcrimereporting;

import android.service.autofill.SaveRequest;

/**
 * Created by Lucifer on 6/29/2018.
 */

public class reports {
    private int id;
    private String date;
    private String status;
    private String details;

    private String image;
    private String time;


    public reports(String date, String status, String time, String image,String details) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.time=time;
        this.image = image;
        this.details=details;
    }

    public int getId() {
        return id;
    }
    public String getdetails() {
        return details;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }



    public String getImage() {
        return image;
    }
}
