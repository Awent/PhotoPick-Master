package com.simaple;

import com.awen.photo.FrescoImageLoader;

import java.util.ArrayList;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ImageProvider {

    public static ArrayList<String> getImageUrls(){
        ArrayList<String> list = new ArrayList<>();
        list.add(FrescoImageLoader.getResUrl(R.mipmap.resx));//长图
        list.add(FrescoImageLoader.getAssetUrl("test.jpeg"));
//        //如果加载本地图片，可按照下面的来
//        list.add(FrescoImageLoader.getFileUrl("/storage/emulated/0/Pictures/Screenshots/Screenshot_20170919-203120.png"));
//        list.add(FrescoImageLoader.getFileUrl("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1505817817979.jpg"));
        //网络长图,包括横向和纵向的长图
        list.add("http://www.qqleju.com/uploads/allimg/130724/24-035110_951.jpg");
        list.add("https://ww4.sinaimg.cn/bmiddle/73851b25jw1du81xj0psuj.jpg");
        list.add("https://mat1.gtimg.com/ent/0000/xmsct.jpg");
        list.add("http://img.zcool.cn/community/01e92257c93edb0000018c1b8776b8.jpg");
        list.add("http://pic90.nipic.com/file/20160302/4627410_000612662875_2.jpg");
        list.add("http://pic1.16xx8.com/allimg/170813/16xx8_ps20.jpg");
        list.add("https://raw.githubusercontent.com/Awent/PhotoPick-Master/master/pictrue/WechatIMG20.jpeg");
        list.add("https://raw.githubusercontent.com/Awent/PhotoPick-Master/master/pictrue/WechatIMG21.jpeg");
        //网络图片
        list.add("http://d.5857.com/xgmn_161229/013.jpg");
        list.add("http://t1.mmonly.cc/uploads/allimg/20150522/5ic0qfprp3s.jpg");
        list.add("https://newimg.uumnt.cc:8092/Pics/2017/0822/05/04.jpg");
        list.add("http://t1.mmonly.cc/uploads/tu/201706/9999/7a4088b7b6.jpg");
        list.add("https://www.swbox.cn/wp-content/uploads/2018/04/21304Mc8-1.jpg");
        list.add("http://t1.mmonly.cc/uploads/tu/201602/145/0jhmx2jcfzw.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/82001295-c8f4-4b79-bc19-b029d868c00d.gif");
        list.add("http://i-3.yxdown.com/2016/5/15/c037f911-105f-41ba-a5ad-6b6ca6b70efc.gif");
        list.add("http://www.wmpic.me/wp-content/uploads/2014/02/20140218150739227.jpg");
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
    public static ArrayList<String> getSmallImgUrls(){
        ArrayList<String> list = new ArrayList<>();
        list.add("http://img.qq745.com/uploads/hzbimg/0907/hzb33617.png");
        list.add("http://img.qq745.com/uploads/hzbimg/0907/hzb33616.png");
        return list;
    }
}
