package com.piyushjaiswal12.primeflix.model;

import java.io.Serializable;

public class UpComingClass implements Serializable {
    private String name;
    private String thumb;
    private String url;

    public UpComingClass(String name, String thumb, String url) {
        this.name = name;
        this.thumb = thumb;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
