package com.piyushjaiswal12.primeflix.model;

import java.io.Serializable;
import java.sql.Date;

public class WebSeriesClass implements Serializable {

    private String name;
    private int totaltime;
    private Date date;
    private String category;
    private String thumb;


    public WebSeriesClass(String name,  int totaltime, Date date, String category, String thumb) {
        this.name = name;

        this.totaltime = totaltime;
        this.date = date;
        this.category = category;
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(int totaltime) {
        this.totaltime = totaltime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
