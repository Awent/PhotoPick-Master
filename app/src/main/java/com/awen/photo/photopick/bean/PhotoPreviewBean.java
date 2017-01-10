package com.awen.photo.photopick.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class PhotoPreviewBean implements Serializable {

    private int position;
    private ArrayList<Photo> photos;
    private ArrayList<String> selectPhotos;
    private int maxPickSize;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public ArrayList<String> getSelectPhotos() {
        return selectPhotos;
    }

    public void setSelectPhotos(ArrayList<String> selectPhotos) {
        this.selectPhotos = selectPhotos;
    }

    public int getMaxPickSize() {
        return maxPickSize;
    }

    public void setMaxPickSize(int maxPickSize) {
        this.maxPickSize = maxPickSize;
    }
}
