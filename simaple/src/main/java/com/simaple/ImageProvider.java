package com.simaple;

import com.awen.photo.FrescoImageLoader;

import java.util.ArrayList;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ImageProvider {

    public static ArrayList<String> getImageUrls(){
        ArrayList<String> list = new ArrayList<>();
        //也可以传本地图片，格式如下：
//                list.add("file:///storage/emulated/0/xxxx.jpg");
//                list.add("file:///storage/emulated/0/zzzzz.jpg");
        list.add(FrescoImageLoader.getResUrl(R.mipmap.larger_long_photo));//长图
        list.add(FrescoImageLoader.getAssetUrl("test.jpeg"));
//        //如果加载本地图片，可按照下面的来
//        list.add(FrescoImageLoader.getFileUrl("/storage/emulated/0/Pictures/Screenshots/Screenshot_20170919-203120.png"));
//        list.add(FrescoImageLoader.getFileUrl("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1505817817979.jpg"));
        //网络长图
        list.add("http://img.zcool.cn/community/01e92257c93edb0000018c1b8776b8.jpg");
        list.add("http://pic90.nipic.com/file/20160302/4627410_000612662875_2.jpg");
        list.add("http://pic1.16xx8.com/allimg/170813/16xx8_ps20.jpg");
        //网络图片
        list.add("http://p1.wmpic.me/article/2015/06/29/1435559754_fnZtksvI.jpg");
        list.add("http://p2.wmpic.me/article/2015/06/29/1435559758_rMcNLvQq.jpg");
        list.add("http://res.mamiw.com/uploads/allimg/170215/19-1F215094J3952.gif");
        list.add("http://res.mamiw.com/uploads/allimg/151223/2241033125-6.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/0b7ab36c-26d4-469e-806c-ca396d9e1da6.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/196566b6-f4cd-4067-9fb9-0b7c46468c73.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/e94ac816-f2e9-4456-9697-589fb29e4dda.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/82001295-c8f4-4b79-bc19-b029d868c00d.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/c037f911-105f-41ba-a5ad-6b6ca6b70efc.gif");
        list.add("http://res.mamiw.com/uploads/allimg/170214/19-1F21410225bc.gif");
        list.add("http://res.mamiw.com/uploads/allimg/170214/19-1F214102102J8.gif");
        list.add("http://res.mamiw.com/uploads/allimg/170212/19-1F21213252A02.gif");
        list.add("http://pic1.win4000.com/pic/6/da/50bc520323.jpg");
        list.add("http://www.wmpic.me/wp-content/uploads/2014/02/20140218150739227.jpg");
        list.add("http://www.bz55.com/uploads/allimg/140904/138-140Z4092036.jpg");
        list.add("http://pic18.nipic.com/20111223/5252423_182312570000_2.jpg");
        list.add("http://pic15.nipic.com/20110621/6632244_101716433621_2.jpg");
        list.add("http://pic18.nipic.com/20111223/5252423_185908474000_2.jpg");
        list.add("http://pic18.nipic.com/20120207/9040008_163401147000_2.jpg");
        return list;
    }

    /**
     * 大图
     * @return
     */
    public static ArrayList<String> getBigImgUrls(){
        ArrayList<String> list = new ArrayList<>();
        list.add("http://pic18.nipic.com/20120103/8783405_180811375100_2.jpg");
        list.add("http://pic25.nipic.com/20121201/10258080_144012468179_2.jpg");
        return list;
    }

    /**
     * 小图，这里随便找两张小图的
     * @return
     */
    public static ArrayList<String> getLowImgUrls(){
        ArrayList<String> list = new ArrayList<>();
        list.add("http://img.qq745.com/uploads/hzbimg/0907/hzb33617.png");
        list.add("http://img.qq745.com/uploads/hzbimg/0907/hzb33616.png");
        return list;
    }
}
