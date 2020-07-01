package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.awen.photo.photopick.controller.PhotoPagerConfig;

import java.util.ArrayList;

/**
 * Created by Awen <Awentljs@gmail.com>.
 */
public class PhotoPagerBean implements Parcelable {

    /**
     * 是否开启图片保存到本地功能
     */
    private boolean saveImage;
    /**
     * 保存图片到本地的地址
     */
    private String saveImageLocalPath;
    /**
     * 展示第几张图片
     */
    private int pagePosition;
    /**
     * 大图
     */
    private ArrayList<String> bigImgUrls;
    /**
     * 小图,可用于在展示大图前进行小图的展示
     */
    private ArrayList<String> smallImgUrls;
    /**
     * 是否开启下滑关闭activity，默认开启。类似微信的图片浏览，可下滑关闭一样，但是没有图片归位效果
     */
    private boolean isOpenDownAnimate;

    private PhotoPagerConfig.Builder.OnPhotoSaveCallback onPhotoSaveCallback;

    public PhotoPagerBean() {
    }


    private PhotoPagerBean(Parcel in) {
        saveImage = in.readByte() != 0;
        saveImageLocalPath = in.readString();
        pagePosition = in.readInt();
        bigImgUrls = in.createStringArrayList();
        smallImgUrls = in.createStringArrayList();
        isOpenDownAnimate = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (saveImage ? 1 : 0));
        dest.writeString(saveImageLocalPath);
        dest.writeInt(pagePosition);
        dest.writeStringList(bigImgUrls);
        dest.writeStringList(smallImgUrls);
        dest.writeByte((byte) (isOpenDownAnimate ? 1 : 0));
    }

    public static final Creator<PhotoPagerBean> CREATOR = new Creator<PhotoPagerBean>() {
        @Override
        public PhotoPagerBean createFromParcel(Parcel in) {
            return new PhotoPagerBean(in);
        }

        @Override
        public PhotoPagerBean[] newArray(int size) {
            return new PhotoPagerBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSaveImage() {
        return saveImage;
    }

    public void setSaveImage(boolean saveImage) {
        this.saveImage = saveImage;
    }

    public String getSaveImageLocalPath() {
        return saveImageLocalPath;
    }

    public void setSaveImageLocalPath(String saveImageLocalPath) {
        this.saveImageLocalPath = saveImageLocalPath;
    }

    public int getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }

    public ArrayList<String> getBigImgUrls() {
        return bigImgUrls;
    }

    public void setBigImgUrls(ArrayList<String> bigImgUrls) {
        this.bigImgUrls = bigImgUrls;
    }

    /**
     * @deprecated use {@link #getSmallImgUrls()}
     */
    public ArrayList<String> getLowImgUrls() {
        return smallImgUrls;
    }

    /**
     * @deprecated use {@link #setSmallImgUrls(ArrayList)}
     */
    public void setLowImgUrls(ArrayList<String> lowImgUrls) {
        setSmallImgUrls(lowImgUrls);
    }

    public void setSmallImgUrls(ArrayList<String> smallImgUrls) {
        this.smallImgUrls = smallImgUrls;
    }

    public ArrayList<String> getSmallImgUrls() {
        return smallImgUrls;
    }

    /**
     * 一张一张大图add进ArrayList
     *
     * @param bigImageUrl 大图url
     */
    public void addSingleBigImageUrl(String bigImageUrl) {
        if (bigImageUrl == null) {
            throw new NullPointerException("bigImageUrl is null");
        }
        if (bigImgUrls == null) {
            bigImgUrls = new ArrayList<>();
        }
        bigImgUrls.add(bigImageUrl);
    }

    /**
     * 一张一张小图add进ArrayList
     *
     * @param smallImageUrl 小图url
     */
    public void addSingleSmallImageUrl(String smallImageUrl) {
        if (smallImageUrl == null) {
            throw new NullPointerException("smallImageUrl is null");
        }
        if (smallImgUrls == null) {
            smallImgUrls = new ArrayList<>();
        }
        smallImgUrls.add(smallImageUrl);
    }

    /**
     * 一张一张小图add进ArrayList
     *
     * @param lowImageUrl 小图url
     * @deprecated use {@link #addSingleSmallImageUrl(String)}
     */
    public void addSingleLowImageUrl(String lowImageUrl) {
        addSingleSmallImageUrl(lowImageUrl);
    }

    public boolean isOpenDownAnimate() {
        return isOpenDownAnimate;
    }

    public void setOpenDownAnimate(boolean openDownAnimate) {
        isOpenDownAnimate = openDownAnimate;
    }

    public PhotoPagerConfig.Builder.OnPhotoSaveCallback getOnPhotoSaveCallback() {
        return onPhotoSaveCallback;
    }

    public void setOnPhotoSaveCallback(PhotoPagerConfig.Builder.OnPhotoSaveCallback onPhotoSaveCallback) {
        this.onPhotoSaveCallback = onPhotoSaveCallback;
    }
}
