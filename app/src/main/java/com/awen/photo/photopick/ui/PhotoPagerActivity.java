package com.awen.photo.photopick.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import com.awen.photo.BaseActivity;
import com.awen.photo.R;
import com.awen.photo.photopick.util.PhotoPagerConfig;
import com.awen.photo.photopick.widget.HackyViewPager;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * 图片查看器<br>
 * <p/>
 * 服务端返回的图片链接都是小图的,比如：imageFile = "xxxxx.jpg#64x64.jpg";<br>
 * 大图就需要客户端去掉"&64x64.jpg"部分,所以大图就是bigImageFile = "xxxxx.jpg";以"&"作为分隔符
 *
 * @author Homk-M <Awentljs@gmail.com>
 */
public class PhotoPagerActivity extends BaseActivity {
    private static final String TAG = PhotoPagerActivity.class.getSimpleName();
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private CircleIndicator indicator;
    private ArrayList<String> urls;
    private ImagePagerAdapter adapter;
    private Bundle bundle;
    private boolean saveImage;
    private String saveImageLocalPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOpenToolBar(false);
        setContentView(R.layout.activity_photo_detail_pager);
        bundle = getIntent().getBundleExtra(PhotoPagerConfig.EXTRA_PAGER_BUNDLE);
        urls = bundle.getStringArrayList(PhotoPagerConfig.EXTRA_IMAGE_URLS);
        pagerPosition = bundle.getInt(PhotoPagerConfig.EXTRA_DEFAULT_POSITION);
        saveImage = bundle.getBoolean(PhotoPagerConfig.EXTRA_SAVA_IMAGE);
        saveImageLocalPath = bundle.getString(PhotoPagerConfig.EXTRA_SAVE_IMAGE_LOCAL_PATH);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        mPager = (HackyViewPager) findViewById(R.id.pager);
        adapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(adapter);
        if (urls.size() == 1) {
            // if the urls has a single image,to set indicator not visible
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(mPager);
        }
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return PhotoDetailFragment.newInstance(url,saveImage,saveImageLocalPath);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }
}
