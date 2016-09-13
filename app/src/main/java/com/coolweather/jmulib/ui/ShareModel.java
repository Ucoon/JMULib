package com.coolweather.jmulib.ui;

import java.util.HashMap;

/**
 * @data: 2014-7-21 下午2:42:34
 * @version: V1.0
 */
public class ShareModel {
    private String title;
    private String text;
    private String url;
    private String imageUrl;


    public void setImageFilepath(String imageFilepath) {
        this.imageFilepath = imageFilepath;
    }

    private String imageFilepath;
    private HashMap<String, Object> params;


    public String getImageFilepath() {
        return imageFilepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    /** imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段 */
//    public void setImagePath(String imagePath) {
//
//        params = new HashMap<String, Object>();
//        params.put("customers", new ArrayList<CustomerLogo>());
//        params.put("hiddenPlatforms", new HashMap<String, String>());
//        if(!TextUtils.isEmpty(imagePath))
//            params.put("imagePath", imagePath);
//    }

}
