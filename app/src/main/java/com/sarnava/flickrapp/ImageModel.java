package com.sarnava.flickrapp;

public class ImageModel {
    private String img_url,img_title,width,height;

    public ImageModel(String img_url, String img_title, String width, String height) {
        this.img_url = img_url;
        this.img_title = img_title;
        this.width = width;
        this.height = height;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_title() {
        return img_title;
    }

    public void setImg_title(String img_title) {
        this.img_title = img_title;
    }

    public int getWidth() {
        int w = Integer.parseInt(width);
        while(w>500){
            w=w/2;
        }
        return w;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public int getHeight() {
        int h = Integer.parseInt(height);
        while(h>500){
            h=h/2;
        }
        return h;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
