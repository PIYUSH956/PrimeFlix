package com.piyushjaiswal12.primeflix.model;

public class EpisodesClass  {
    private String episodes;
    private String name;
    private String desc;
    private String thumb;
    private String url;


    public EpisodesClass(String name, String episodes, String desc, String thumb, String url) {
        this.episodes = episodes;
        this.name = name;
        this.desc = desc;
        this.thumb = thumb;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }



    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
