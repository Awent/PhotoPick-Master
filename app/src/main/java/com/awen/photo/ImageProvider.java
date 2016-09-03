package com.awen.photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ImageProvider {

    public static ArrayList<String> getImageUrls(){
        ArrayList<String> list = new ArrayList<>();
        //也可以传本地图片，格式如下：
//                list.add("file:///storage/emulated/0/frgdr/frgdr_Video/im_101460431984904.jpg");
//                list.add("file:///storage/emulated/0/frgdr/frgdr_Video/im_101460432332951.jpg");
        list.add("http://p1.wmpic.me/article/2015/06/29/1435559754_fnZtksvI.jpg");
        list.add("http://p2.wmpic.me/article/2015/06/29/1435559758_rMcNLvQq.jpg");
        list.add("http://pic1.win4000.com/pic/6/da/50bc520323.jpg");
        list.add("http://www.wmpic.me/wp-content/uploads/2014/02/20140218150739227.jpg");
        list.add("http://www.bz55.com/uploads/allimg/140904/138-140Z4092036.jpg");
        list.add("http://pic18.nipic.com/20111223/5252423_182312570000_2.jpg");
        list.add("http://pic15.nipic.com/20110621/6632244_101716433621_2.jpg");
        list.add("http://pic18.nipic.com/20111223/5252423_185908474000_2.jpg");
        list.add("http://pic18.nipic.com/20120207/9040008_163401147000_2.jpg");
        list.add("http://file2.desktx.com/pc/wallpaper/scenery/20110720/mait_2.jpg");
        list.add("http://file2.desktx.com/pc/wallpaper/scenery/20130227/30_12158_koodianCom_Zoj.jpg");
        return list;
    }

    /**
     * 大图
     * @return
     */
    public static ArrayList<String> getBigImgUrls(){
        ArrayList<String> list = new ArrayList<>();
        list.add("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg");
        list.add("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg");
        return list;
    }

    /**
     * 小图
     * @return
     */
    public static ArrayList<String> getLowImgUrls(){
        ArrayList<String> list = new ArrayList<>();
        list.add("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg&64X64.jpg");
        list.add("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg&64X64.jpg");
        return list;
    }
}
