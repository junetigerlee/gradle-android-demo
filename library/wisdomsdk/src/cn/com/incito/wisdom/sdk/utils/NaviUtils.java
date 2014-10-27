//package cn.com.incito.wisdom.sdk.utils;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//
//import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
//import com.baidu.mapapi.navi.BaiduMapNavigation;
//import com.baidu.mapapi.navi.NaviPara;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//
///**
// * 调用百度地图导航工具类
// * 
// * @author qiujiaheng
// * 
// */
//public class NaviUtils {
//    /**
//     * 
//     * @param mLat1
//     *            起始地点纬度
//     * @param mLon1
//     *            起始地点经度
//     * @param mLat2
//     *            目的地纬度
//     * @param mLon2
//     *            目的地经度
//     * @param context
//     */
//    public static void startNavi(double mLat1, double mLon1, double mLat2,
//            double mLon2, final Activity context) {
//        int lat = (int) (mLat1 * 1E6);
//        int lon = (int) (mLon1 * 1E6);
//        GeoPoint pt1 = new GeoPoint(lat, lon);
//        lat = (int) (mLat2 * 1E6);
//        lon = (int) (mLon2 * 1E6);
//        GeoPoint pt2 = new GeoPoint(lat, lon);
//        // 构建 导航参数
//        NaviPara para = new NaviPara();
//        para.startPoint = pt1;
//        para.startName = "从这里开始";
//        para.endPoint = pt2;
//        para.endName = "到这里结束";
//
//        try {
//            BaiduMapNavigation.openBaiduMapNavi(para, context);
//
//        } catch (BaiduMapAppNotSupportNaviException e) {
//            e.printStackTrace();
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
//            builder.setTitle("提示");
//            builder.setPositiveButton("确认", new OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    BaiduMapNavigation.GetLatestBaiduMapApp(context);
//                }
//            });
//
//            builder.setNegativeButton("取消", new OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builder.create().show();
//        }
//    }
//
//}
