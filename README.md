
 
### 注意事项：如果sdk>=29,需要在自己的**AndroidManifest.xml**的  `application`节点添加适配代码：`android:requestLegacyExternalStorage="true"`才可正常使用图库选图和保存网络大图功能，[issues](https://github.com/Awent/PhotoPick-Master/issues/13)
 
# [已适配androidX的点这里，不用继续往下看了](https://github.com/Awent/PhotoPick-Master/blob/master/androidX-README.md)

# PhotoPick-Master
我把项目中做的图库抽了出来，本项目中包括了以下内容：1、图库选图，可多选，单选，可选完图片后进行裁剪，2、查看大图(包括sd卡，res，网络图)，并可以保存到手机图库,3、适配沉浸式状态栏 ,4 、使用fresco加载图片(图片是随便网上找的，性感吧- -!)

下面来看几张效果图：

![image](https://github.com/Awent/PhotoPick-Master/blob/master/pictrue/304079-052c8fd0c9d22efd.gif)

![image](https://github.com/Awent/PhotoPick-Master/blob/master/pictrue/304079-8d726553c6c0b6ba.gif)

![image](https://github.com/Awent/PhotoPick-Master/blob/master/pictrue/304079-e4c819f695ed83c0.gif)

![image](https://github.com/Awent/PhotoPick-Master/blob/master/pictrue/device-2017-10-25-033458.png)

你可以先[下载apk](https://github.com/Awent/PhotoPick-Master/blob/master/simaple/simaple-release.apk)运行看下，apk比较大，里面有几张很大的长图


![tips.png](http://upload-images.jianshu.io/upload_images/304079-2344f7ec6b950a05.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

项目我已经挂载在[github](https://github.com/Awent/PhotoPick-Master)，如果有需要的朋友，欢迎fork，项目有什么issue可以在这里留言，也可以[点击这里](https://github.com/Awent/PhotoPick-Master/issues)进行commit。希望能帮到你。

##下面讲解如何引入到你的项目中
以下是implementation引入方式，如果是compile引入，请参考[v1.096](https://github.com/Awent/PhotoPick-Master/blob/master/v1.096-README.md)

1、在你的root gradle(也就是项目最外层的gradle)添加如下代码

```

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

ext {
    android = [
            compileSdkVersion : 28,        //根据自己项目的来配置
            buildToolsVersion : '28.0.3',  //根据自己项目的来配置
            targetSdkVersion  : 28,        //根据自己项目的来配置
            supportVersion    : '28.0.0',  //android support version
            frescoVersion     : '1.11.0'   //fresco的版本，可自行修改
    ]
}

```

2、然后在module gradle dependencies 添加以下依赖，如果添加失败，请打开vpn后重试

```
    implementation "com.android.support:appcompat-v7:${rootProject.ext.android.supportVersion}"
    implementation "com.android.support:recyclerview-v7:${rootProject.ext.android.supportVersion}"
    implementation "com.facebook.fresco:fresco:${rootProject.ext.android.frescoVersion}"
    // 支持 GIF 动图，需要添加
    implementation "com.facebook.fresco:animated-gif:${rootProject.ext.android.frescoVersion}"
    // 支持 WebP （静态图+动图），需要添加
    implementation "com.facebook.fresco:animated-webp:${rootProject.ext.android.frescoVersion}"
    implementation "com.facebook.fresco:webpsupport:${rootProject.ext.android.frescoVersion}"
    //跟随viewpager的点
    implementation 'me.relex:circleindicator:1.1.8@aar'
    //上滑控制面板,项目中的potopick中有使用案例
    implementation 'com.sothree.slidinguppanel:library:3.3.0'
    //android6.0权限工具类
    implementation 'com.lovedise:permissiongen:0.1.1'
    //加载超长图必备库
    implementation 'com.davemorrissey.labs:subsampling-scale-image-view:3.10.0'
    implementation "com.android.support:support-v4:${rootProject.ext.android.supportVersion}"
    //图库
    implementation 'com.github.Awent:PhotoPick-Master:v2.9'
```

3、然后在你的Application的onCreate()方法里初始化即可使用

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoImageLoader.init(this);
        //下面是配置toolbar颜色和存储图片地址的
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light);
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light,"/storage/xxxx/xxx");
    }
}

```

##下面讲解如何使用
- Hao to use(可进行动态设置参数，不用的可以不设置)

- 图库

```
new PhotoPickConfig.Builder(this)
    .pickMode(PhotoPickConfig.MODE_MULTIP_PICK) //多选，这里有单选和多选
    .maxPickSize(15)                            //最多可选15张
    .showCamera(false)                          //是否展示拍照icon,默认展示
    .clipPhoto(true)                            //是否选完图片进行图片裁剪，默认是false,如果这里设置成true,就算设置了是多选也会被配置成单选
    .spanCount(4)                               //图库的列数，默认3列，这个数建议不要太大
    .showGif(true)//default true                //是否展示gif
    .setOnPhotoResultCallback(OnPhotoResultCallback onPhotoResultCallback) //设置数据回调，如果不想在Activity通过onActivityResult()获取回传的数据，可实现此接口
    .build();

```

- 获取图库选择了(或裁剪好)的图片地址

```
方法一：
new PhotoPickConfig.Builder(this)
    .pickMode(PhotoPickConfig.MODE_MULTIP_PICK)
    .maxPickSize(15)
    .showCamera(true)
    .setOriginalPicture(true)//让用户可以选择原图
    .setOnPhotoResultCallback(new PhotoPickConfig.Builder.OnPhotoResultCallback() {
          @Override
          public void onResult(PhotoResultBean result) {
                 Log.e("MainActivity", "result = " + result.getPhotoLists().size());
          }
    })
    .build();
    
    
方法二：
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK || data == null) {
        return;
    }
    switch (requestCode) {
        case PhotoPickConfig.PICK_REQUEST_CODE:
            //返回的图片地址集合
            ArrayList<String> photoLists = data.getStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST);
            if (photoLists != null && !photoLists.isEmpty()) {
                File file = new File(photoLists.get(0));//获取第一张图片
                if (file.exists()) {
                    //you can do something

                } else {
                    //toast error
                }
            }
            break;
    }
}

```

- 查看大图

```
new PhotoPagerConfig.Builder(this)
    .setBigImageUrls(ImageProvider.getImageUrls())      //大图片url,可以是sd卡res，asset，网络图片.
    .setSmallImageUrls(ArrayList<String> smallImgUrls)  //小图图片的url,用于大图展示前展示的
    .addSingleBigImageUrl(String bigImageUrl)           //一张一张大图add进ArrayList
    .addSingleSmallImageUrl(String smallImageUrl)       //一张一张小图add进ArrayList
    .setSavaImage(true)                                 //开启保存图片，默认false
    .setPosition(2)                                     //默认展示第2张图片
    .setSaveImageLocalPath("Android/SD/xxx/xxx")        //这里是你想保存大图片到手机的地址,可在手机图库看到，不传会有默认地址
    .setBundle(bundle)                                  //传递自己的数据，如果数据中包含java bean，必须实现Parcelable接口
    .setOpenDownAnimate(false)                          //是否开启下滑关闭activity，默认开启。类似微信的图片浏览，可下滑关闭一样
    .setOnPhotoSaveCallback(new OnPhotoSaveCallback()   //保存网络图片到本地图库的回调,保存成功则返回本地图片路径，失败返回null
    .build();
    
自定义界面：

new PhotoPagerConfig.Builder(this,Class<?> clazz)       //这里传入你自定义的Activity class,自定义的activity必须继承PhotoPagerActivity
    ...
    ...
    ...
    .build();
```

- 混淆

```
参考simple中的proguard-rules文件

```

### v2.9
2020-06-30
implementation 'com.github.Awent:PhotoPick-Master:v2.9'
适配android 10黑暗模式，跟随系统切换


### v2.8
2019-07-19
implementation 'com.github.Awent:PhotoPick-Master:v2.8'
修复优化:
1、fix bug
2、使用aip导入依赖包，已经导入的包不用再导入一次

### v2.3
2019-07-19
implementation 'com.github.Awent:PhotoPick-Master:v2.06'
修复优化:
1、修改横向长图放大比例
2、新增网络图片保存到本地图库回调
3、fix (#12 )


### v2.2
2019-04-16
implementation 'com.github.Awent:PhotoPick-Master:v2.05'
修复优化:
1、修复某些图片保存后无法在图库查看到的bug
2、图库选图，即使没有选中图片，点击“发送”按钮也会把当前的图片返回

### v2.1
2019-01-17
implementation 'com.github.Awent:PhotoPick-Master:v2.04'
修复:
fix (#7)

### v2.0
2018-10-25
implementation 'com.github.Awent:PhotoPick-Master:v2.02'

### v1.9
2018-10-16
compile 'com.github.Awent:PhotoPick-Master:v1.096'

修复:
查看网络大图保存图片失败的bug

### v1.8
2018-1-0
compile 'com.github.Awent:PhotoPick-Master:v1.095'

### v1.7
2017-12-22
compile 'com.github.Awent:PhotoPick-Master:v1.094'

修复:
8.0以上的bug:java.lang.IllegalStateException: Only fullscreen opaque activities can request orientation

优化:
去除manifest注册activity步骤

### v1.6
2017-11-10
compile 'com.github.Awent:PhotoPick-Master:v1.092'

优化:
1、图片下拉多指操作不再导致图片错位
2、解决了图片多指缩放和图片下拉冲突问题 
3、优化图片下拉关闭activity过度动画
4、觉得小图命名使用low字眼不是很好，相关的api已更名为small,旧的api不受影响

ps:建议更新最新版

### v1.5.1
2017-10-28
compile 'com.github.Awent:PhotoPick-Master:v1.09'

优化:超长图的加载

### v1.5
2017-10-25

compile 'com.github.Awent:PhotoPick-Master:v1.08'

新增：1、仿微信图片下拉关闭效果，可以设置是否开启这个效果：.setOpenDownAnimate(false),
        注意，要背景透明才行，所以PhotoPagerActivity的theme要这样设置：android:theme="@style/PhoAppTheme.Transparent"
     2、查看图库大图时，图片可侵入到导航栏
     
修复：修复部分手机查看图库的时候出现崩溃的bug

优化：去除仿微信图片归位效果，因为效果感觉太差，已去掉，后期优化好了再放出来

### v1.4
2017-10-08

compile 'com.github.Awent:PhotoPick-Master:v1.07'

在root gradle里添加ext，gradle根据自己项目自行配置，解决引入了该library导致项目build失败的原因。优化自定义图片浏览器，可通过Bundle传递自己的数据。
详情使用请参考Demo中的MyPhotoPagerActivity 

### v1.311
2017-09-17

compile 'com.github.Awent:PhotoPick-Master:v1.06'

新增：

1、获取图库回传的数据，不在局限在Activity获得，在任何地方实现了PhotoPickConfig.Builder.OnPhotoResultCallback接口都可获得

2、图片浏览器可扩展自己的界面，使用请参考Demo中的MyPhotoPagerActivity 

3、支持长图(包括横向和纵向)和webp图片的浏览，对于网络的纵向长图，宽度最好大于400，这样可以填充整个屏幕，体验更好，
长图使用的库：[subsampling-scale-image-view](https://github.com/davemorrissey/subsampling-scale-image-view)

4、图库选图可预览，查看网络大图可类似微信有移动效果

优化:

1、大图加载进度条仿微信

2、关于想加载本地或res、asset大图片的，请参考[FrescoImageLoader](https://github.com/Awent/PhotoPick-Master/blob/master/photoLibrary/src/main/java/com/awen/photo/FrescoImageLoader.java)

3、优化图片加载内存

4、build version更新到26

ps:如果下载demo后build失败的，应该是网络不可翻墙导致，可用蓝灯全局翻墙试下

### v1.31
2017-04-10 PM-17：00

1、gradle更新到3.4

2、修复查看大图，单张图片添加bug
方法：addSingleBigImageUrl(String bigImageUrl) 和 addSingleLowImageUrl(String lowImageUrl)
请更新到最新library

### v1.3
2017-02-19 PM-10：00
修复清单文件aplication节点中lable被替换的bug
请更新到最新library

### v1.2
2017-02-17 AM-00:06
把项目抽成Library形式，更加方便引入到你的项目中去

### v1.1
2017-01-10 PM-19:00
新增图片下载展示圆形进度条和下载百分比，新增仿微信图片预览，gif播放，并可保存gif到本地图库

### v1.0.1
2016-9-04 PM-17：00
P:最近开发意识到之前的代码设计有点龊，改起来有点麻烦，所以现在换了一种方法，在javabean里进行参数的配置，这样修改更加方便清晰，注意如果项目打包的时候用到混淆，记得这里的javabean不要进行混淆，因为这里的javabean实现了Parcelable接口，详情可见代码中的['proguard-rules.pro'](https://github.com/Awent/PhotoPick-Master/blob/master/photoLibrary/proguard-rules.pro)文件。另外，还新增两点内容：1、android6.0权限适配，2、大图展示前先展示小图。详情可见项目代码，我已更新到github.



