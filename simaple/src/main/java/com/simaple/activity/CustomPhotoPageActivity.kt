package com.simaple.activity

import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.awen.photo.photopick.ui.PhotoPagerActivity
import com.simaple.R

/**
 * 自定义view实现下标和长按功能等，如果需要传递参数进来，参考
 * @see MyPhotoPagerActivity
 *
 * 如果自定义activity开启了下滑关闭功能，记得当前自定义activity的Theme要引用{@style/PhoAppTheme.Transparent}
 * 如下：
 * <activity
            android:theme="@style/PhoAppTheme.Transparent"
            android:name=".activity.CustomPhotoPageActivity"/>
 */
class CustomPhotoPageActivity : PhotoPagerActivity() {

    private lateinit var indicator: TextView
    private var size: Int = 0

    override fun setCustomView(layoutId: Int) {
        super.setCustomView(R.layout.activity_kt_custom_page)//设置自定义view
    }

    override fun init() {
        super.init()
        setIndicatorVisibility(false)//需要主动隐藏默认的下标
        size = photoPagerBean.bigImgUrls.size
        indicator = customView.findViewById(R.id.indicator)//通过customView来获取自定义view的控件
        indicator.text = "1/$size"
    }

    override fun onStart() {
        super.onStart()
        AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("1、可长按保存图片\n\np：前两张为res和asset图片，会保存失败\n\n2、这是继承PhotoPagerActivity的自定义界面，在这里只展示自定义下标、长按保存功能、下滑关闭界面功能，更多功能需要自己去实现")
                .show()
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        indicator.text = "${currentPosition + 1}/$size"
    }

    override fun onLongClick(view: View?): Boolean {
        val items = arrayOf("获取当前图片地址", "发给朋友", "收藏", "保存图片", "举报", "取消")
        AlertDialog.Builder(this)
                .setItems(items, DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> toast(currentImageUrl)
                        //调用startPermissionSDSuccess此方法可以保存图片，注意这里要自己先获取写sd卡权限
                        3 -> startPermissionSDSuccess()
                        else
                        -> toast(items[which])
                    }
                }).show()
        return true
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}