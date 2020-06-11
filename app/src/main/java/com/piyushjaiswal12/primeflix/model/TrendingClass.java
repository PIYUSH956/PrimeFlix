package com.piyushjaiswal12.primeflix.model;

import java.io.Serializable;

public class TrendingClass implements Serializable {


    private String name;
    private String path;
    private String thumb;
    private String description;

    public TrendingClass(String name, String path, String thumb, String description) {
        this.name = name;
        this.path = path;
        this.thumb = thumb;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
