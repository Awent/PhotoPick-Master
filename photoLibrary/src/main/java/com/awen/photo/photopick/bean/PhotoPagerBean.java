package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
    private ArrayList<String> lowImgUrls;

    public PhotoPagerBean(){
    }

    private PhotoPagerBean(Parcel in) {
        saveImage = in.readByte() != 0;
        saveImageLocalPath = in.readString();
        pagePosition = in.readInt();
        bigImgUrls = in.createStringArrayList();
        lowImgUrls = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (saveImage ? 1 : 0));
        dest.writeString(saveImageLocalPath);
        dest.writeInt(pagePosition);
        dest.writeStringList(bigImgUrls);
        dest.writeStringList(lowImgUrls);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public ArrayList<String> getLowImgUrls() {
        return lowImgUrls;
    }

    public void setLowImgUrls(ArrayList<String> lowImgUrls) {
        this.lowImgUrls = lowImgUrls;
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
        if(bigImgUrls == null){
            bigImgUrls = new ArrayList<>();
        }
        bigImgUrls.add(bigImageUrl);
    }

    /**
     * 一张一张小图add进ArrayList
     *
     * @param lowImageUrl 小图url
     */
    public void addSingleLowImageUrl(String lowImageUrl) {
        if (lowImageUrl == null) {
            throw new NullPointerException("lowImageUrl is null");
        }
        if(lowImgUrls == null){
            lowImgUrls = new ArrayList<>();
        }
        lowImgUrls.add(lowImageUrl);
    }


}
