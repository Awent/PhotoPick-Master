package com.awen.photo.photopick.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.awen.photo.BaseActivity;
import com.awen.photo.R;
import com.awen.photo.photopick.util.AppPathUtil;
import com.awen.photo.photopick.util.ImageUtils;
import com.awen.photo.photopick.widget.clipimage.ClipImageLayout;

/**
 * 用户头像裁剪
 *
 * @author Homk-M <Awentljs@gmail.com>
 */
public class ClipPictureActivity extends BaseActivity {

    public static final String CLIPED_PHOTO_PATH = "cliped_photo_path"; // 已经裁剪的图片地址
    public static final String USER_PHOTO_PATH = "user_photo_path"; // 选择头像的本地地址
    private ClipImageLayout mClipImageLayout;
    private String photoPath;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_clipicture);
        toolbar.setTitle(R.string.clip);
        photoPath = getIntent().getStringExtra(USER_PHOTO_PATH);
        if (photoPath == null) {
            finish();
            return;
        }
        int start = photoPath.lastIndexOf("/");
        bitmap = ImageUtils.getBitmap(photoPath, 480, 480);

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.init(this, bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        MenuItem item = menu.findItem(R.id.ok);
        item.setTitle(R.string.clip);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok) {// 裁剪
            Bitmap bitmap = mClipImageLayout.clip();
            // 保存图片
            String photoName = String.valueOf(System.currentTimeMillis());
//            photoName = photoPath.substring(start + 1, photoPath.length());
            photoPath = AppPathUtil.getClipPhotoPath() + photoName;
            boolean isSaveStatu = ImageUtils.saveImage2(photoPath, bitmap);
            bitmap.recycle();
            Intent intent = new Intent();
            if (isSaveStatu) {
                intent.putExtra(CLIPED_PHOTO_PATH, photoPath);
            }
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}