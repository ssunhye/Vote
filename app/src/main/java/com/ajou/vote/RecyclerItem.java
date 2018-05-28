package com.ajou.vote;

public class RecyclerItem {

    private int image;
    private String title, detail, time;

    public RecyclerItem(int image, String title, String detail, String time) {

        this.image = image;
        this.title = title;
        this.detail = detail;
        this.time = time;
    }

    public int getImage() {

        return image;
    }

    public String getTitle() {

        return title;
    }

    public String getDetail() {

        return detail;
    }

    public String getTime() {

        return time;
    }
}