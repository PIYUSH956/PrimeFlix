package com.piyushjaiswal12.primeflix.model;

import java.io.Serializable;
import java.sql.Date;

public class MoviesClass implements Serializable {


    private String name;

    private Date date;
    private String category;
    private String thumb;


    public MoviesClass(String name,  int totaltime, Date date, String category, String thumb) {
        this.name = name;

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




    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
