package com.simaple.activity

import android.Manifest
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.awen.photo.photopick.controller.PhotoPagerConfig
import com.awen.photo.photopick.ui.PhotoPagerActivity
import com.simaple.R
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess

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
    companion object {
        const val requestSDCode = 100
    }

    private lateinit var indicator: TextView
    private var size: Int = 0
    private val userId: Long by lazy {
        bundle.getLong("user_id")//通过bundle获取传递进来的参数信息
    }

    override fun setCustomView(layoutId: Int) {
        super.setCustomView(R.layout.activity_kt_custom_page)//设置自定义view
    }

    override fun init() {
        super.init()
        setIndicatorVisibility(false)//需要主动隐藏默认的下标
        size = photoPagerBean.bigImgUrls.size
        indicator = customView.findViewById(R.id.indicator)//通过customView来获取自定义view的控件
        indicator.text = "1/$size"

        //保存图片回调
        setOnPhotoSaveCallback {
            AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(if (it.isNullOrEmpty()) "保存失败" else "保存成功，保存地址：$it")
                    .show()
        }

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
        val items = arrayOf("获取当前用户id", "获取当前图片地址", "发给朋友", "收藏", "保存图片", "举报", "取消")
        AlertDialog.Builder(this)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> toast("通过bundle传过来的用户userId = $userId")
                        1 -> toast(currentImageUrl)
                        4 -> {//保存图片，注意这里要自己先获取写sd卡权限，具体的使用哪个库来获取权限，这里自己进行修改
                            //以下操作会回调这两个方法:#requestPermissionSDSuccess(), #requestPermissionSDFailed()
                            PermissionGen.needPermission(this, requestSDCode, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                        else
                        -> toast(items[which])
                    }
                }.show()
        return true
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        //转移权限请求由PermissionGen处理
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @PermissionSuccess(requestCode = requestSDCode)
    fun requestPermissionSDSuccess() {
        //调用startPermissionSDSuccess此方法可以保存图片
        startPermissionSDSuccess()
    }

    @PermissionFail(requestCode = requestSDCode)
    fun requestPermissionSDFailed() {
        startPermissionSDFaild()
    }

}