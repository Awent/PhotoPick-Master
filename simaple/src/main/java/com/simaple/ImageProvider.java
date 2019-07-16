package com.simaple;

import com.awen.photo.FrescoImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ImageProvider {

    public static List<String> getImageUrls(){
        List<String> list = new ArrayList<>();
        list.add(FrescoImageLoader.getResUrl(R.mipmap.resx));//长图
        list.add(FrescoImageLoader.getAssetUrl("test.jpeg"));
//        //如果加载本地图片，可按照下面的来
//        list.add(FrescoImageLoader.getFileUrl("/storage/emulated/0/Pictures/Screenshots/Screenshot_20170919-203120.png"));
//        list.add(FrescoImageLoader.getFileUrl("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1505817817979.jpg"));
        //网络长图,包括横向和纵向的长图
        list.add("https://user-images.githubusercontent.com/39898537/61259085-3449aa80-a7ab-11e9-90bb-0359971e3d15.jpg");
        list.add("https://user-images.githubusercontent.com/39898537/61259086-34e24100-a7ab-11e9-9643-2ad2f50dee6f.jpg");
        list.add("https://user-images.githubusercontent.com/39898537/61259087-34e24100-a7ab-11e9-9c6c-6b2d8ffa43a1.jpg");
        list.add("https://wx2.sinaimg.cn/mw690/005MctNqgy1fx674gpkbvj30gf4k2wzp.jpg");
        list.add("https://wx2.sinaimg.cn/mw690/0062Xesrgy1fx8uu4ltdyj30j635g47x.jpg");
        list.add("https://wx1.sinaimg.cn/mw690/0062Xesrgy1fx8uu5d856j30j64v0h7z.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/0062Xesrgy1fx8uu55neej30j63sq4le.jpg");
        list.add("https://wx1.sinaimg.cn/mw690/0062Xesrgy1fx8uu5p4stj30j66d6b29.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/0062Xesrgy1fx8uu5tkicj30j65fu4qp.jpg");
        list.add("https://raw.githubusercontent.com/Awent/PhotoPick-Master/master/pictrue/WechatIMG20.jpeg");
        list.add("https://raw.githubusercontent.com/Awent/PhotoPick-Master/master/pictrue/WechatIMG21.jpeg");
        //网络图片
        list.add("https://wx1.sinaimg.cn/mw690/7325792bly1fx9oma87k1j21900u04jf.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/7325792bly1fx9oma3jhpj21900u04h0.jpg");
        list.add("https://wx2.sinaimg.cn/mw690/7325792bly1fx9oylai59j22040u0hdu.jpg");
        list.add("https://wx4.sinaimg.cn/mw690/0061VhPpgy1fx9x54op3oj30u00pc1kx.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/006cZ2iWgy1fskddvgmwoj30mq0vu7ik.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/006l0mbogy1fi68udt62wj30u010h79j.jpg");
        list.add("https://wx4.sinaimg.cn/mw690/006l0mbogy1fi68ud4uwwj30u00zrtdj.jpg");
        list.add("https://wx1.sinaimg.cn/mw690/006DQg3tly1fuvkrwjforg30b40alb29.gif");
        list.add("https://wx2.sinaimg.cn/mw690/006DQg3tly1fwhen1vuudg30go09e7wi.gif");
        list.add("https://wx1.sinaimg.cn/mw690/006DQg3tly1fuvkrxsntcg30g409xx6q.gif");
        list.add("https://wx1.sinaimg.cn/mw690/006DQg3tly1fuvks859e4g30dw0atqv8.gif");
        return list;
    }

    /**
     * 大图
     * @return
     */
    public static List<String> getBigImgUrls(){
        List<String> list = new ArrayList<>();
        list.add("https://wx3.sinaimg.cn/mw690/0061VhPpgy1fx8w3jn6o8j30u00qd774.jpg");
        list.add("https://wx4.sinaimg.cn/mw690/006mQAf4ly1fx8yoea4zuj30vy1bzk0x.jpg");
        list.add("https://wx1.sinaimg.cn/mw690/006mQAf4ly1fx8yoipc10j30vy1bzwnt.jpg");
        return list;
    }

    /**
     * 小图，这里随便找两张小图的
     * @return
     */
    public static List<String> getSmallImgUrls(){
        List<String> list = new ArrayList<>();
        list.add("https://wx3.sinaimg.cn/mw690/006qDXTKgy1fx8q6x78hkj30c80953yt.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/006qDXTKgy1fx8q6x78hkj30c80953yt.jpg");
        list.add("https://wx3.sinaimg.cn/mw690/006qDXTKgy1fx8q6x78hkj30c80953yt.jpg");
        return list;
    }
}
