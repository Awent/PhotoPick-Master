package com.awen.photo.photopick.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.awen.photo.FrescoBaseActivity;
import com.awen.photo.R;

/**
 * 使用videoView进行视频的简单播放,通过intent接收“videoUrl”，
 * 单独显示一个本地视频或网络视频都可以通过intent传递进来
 */
public class VideoPlayActivity extends FrescoBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        setOpenToolBar(false);
        super.onCreate(arg0);
        String videoUrl = getIntent().getStringExtra("videoUrl");
        if (TextUtils.isEmpty(videoUrl)) {
            Log.e("VideoPlayActivity", "videoUrl must not null");
            finish();
            return;
        }
        setContentView(R.layout.activity_video_play);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoUrl));
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }
}
