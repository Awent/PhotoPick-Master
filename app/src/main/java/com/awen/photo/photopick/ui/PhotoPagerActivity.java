package com.awen.photo.photopick.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import com.awen.photo.BaseActivity;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.PhotoPagerBean;
import com.awen.photo.photopick.util.PhotoPagerConfig;
import com.awen.photo.photopick.widget.HackyViewPager;


import me.relex.circleindicator.CircleIndicator;

/**
 * 图片查看器<br>
 *
 * @author Homk-M <Awentljs@gmail.com>
 */
public class PhotoPagerActivity extends BaseActivity {
    private static final String TAG = PhotoPagerActivity.class.getSimpleName();
    private static final String STATE_POSITION = "STATE_POSITION";
    private HackyViewPager viewPager;
    private PhotoPagerBean photoPagerBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOpenToolBar(false);
        setContentView(R.layout.activity_photo_detail_pager);
        Bundle bundle = getIntent().getBundleExtra(PhotoPagerConfig.EXTRA_PAGER_BUNDLE);
        photoPagerBean = (PhotoPagerBean) bundle.get(PhotoPagerConfig.EXTRA_PAGER_BEAN);
        if (photoPagerBean == null) {
            finish();
            return;
        }
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        if (photoPagerBean.getBigImgUrls().size() == 1) {
            // if the urls has a single image,to set indicator not visible
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(viewPager);
        }
        if (savedInstanceState != null) {
            photoPagerBean.setPagePosition(savedInstanceState.getInt(STATE_POSITION));
        }
        viewPager.setCurrentItem(photoPagerBean.getPagePosition());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return photoPagerBean.getBigImgUrls() == null ? 0 : photoPagerBean.getBigImgUrls().size();
        }

        @Override
        public Fragment getItem(int position) {
            String bigImgUrl = photoPagerBean.getBigImgUrls().get(position);
            String lowImgUrl = (photoPagerBean.getLowImgUrls() == null || photoPagerBean.getLowImgUrls().isEmpty()) ? "" : photoPagerBean.getLowImgUrls().get(position);
            boolean saveImage = photoPagerBean.isSaveImage();
            String saveImageLocalPath = photoPagerBean.getSaveImageLocalPath();
            return PhotoDetailFragment.newInstance(bigImgUrl, lowImgUrl, saveImage, saveImageLocalPath);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }
}
