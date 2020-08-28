package com.awen.photo.photopick.bean;

import com.awen.photo.photopick.widget.photodraweeview.PhotoDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

public class PageReferenceBean {

    private ImageRequest request;
    private PhotoDraweeView photoDraweeView;

    public PageReferenceBean(ImageRequest request, PhotoDraweeView photoDraweeView) {
        this.request = request;
        this.photoDraweeView = photoDraweeView;
    }

    public ImageRequest getRequest() {
        return request;
    }

    public void setRequest(ImageRequest request) {
        this.request = request;
    }

    public PhotoDraweeView getPhotoDraweeView() {
        return photoDraweeView;
    }

    public void setPhotoDraweeView(PhotoDraweeView photoDraweeView) {
        this.photoDraweeView = photoDraweeView;
    }
}
