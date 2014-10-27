
package cn.com.incito.driver;

import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 百度地图
 * @description 百度地图
 * @author lizhan
 * @createDate 2014年10月13日
 * @version 1.0
 */
public class BaiduMapActivity extends FragmentActivity {

    private ProgressiveDialog mProgressiveDialog;

    private MapView mMapView = null; // 地图View

    private BaiduMap mBaiduMap;

    private Button mBackBtn;

    // 搜索相关
    private RoutePlanSearch mRoutePlanSearch = null; // 搜索模块，也可去掉地图模块独立使用

    private boolean useDefaultIcon = true;

    private OverlayManager routeOverlay = null;

    private RouteLine route = null;

    private LatLng stNodeLatLng;

    private LatLng enNodeLatLng;

    private GeoCoder mGeoCoderSearch = null;

    private String province;

    private String city;

    private String region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_location);

        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BaiduMapActivity.this.finish();
            }
        });

        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 卫星地图
        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        // 地理编码
        mGeoCoderSearch = GeoCoder.newInstance();
        mGeoCoderSearch.setOnGetGeoCodeResultListener(myGeoCoderResultListener);
        // 初始化搜索模块，注册事件监听
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener(myGetRoutePlanResultListener);

        double cLat = 39.963175;
        double cLon = 116.400244;
        stNodeLatLng = new LatLng(cLat, cLon);
        Intent intent = getIntent();
        if (intent.hasExtra("lat") && intent.hasExtra("lon")) {
            Bundle b = intent.getExtras();
            stNodeLatLng = new LatLng(b.getDouble("lat"), b.getDouble("lon"));
            addMaker(stNodeLatLng);
        } else if (intent.hasExtra("currLocation_lat") && intent.hasExtra("currLocation_lon")
                && intent.hasExtra("agent_lat") && intent.hasExtra("agent_lon")) {// 定位货代

            Bundle b = intent.getExtras();
            stNodeLatLng = new LatLng(b.getDouble("currLocation_lat"), b.getDouble("currLocation_lon"));
            enNodeLatLng = new LatLng(b.getDouble("agent_lat"), b.getDouble("agent_lon"));

            addMaker(stNodeLatLng);

            drivingSearch(stNodeLatLng, enNodeLatLng);

        } else if (intent.hasExtra("currLocation_lat") && intent.hasExtra("currLocation_lon")
                && intent.hasExtra("province") && intent.hasExtra("city") && intent.hasExtra("region")) {// 定位货主//开始导航
            Bundle b = intent.getExtras();
            stNodeLatLng = new LatLng(b.getDouble("currLocation_lat"), b.getDouble("currLocation_lon"));
            province = b.getString("province");
            city = b.getString("city");
            region = b.getString("region");

            // Geo搜索
            mGeoCoderSearch.geocode(new GeoCodeOption().city(province + city + region)
                    .address(province + city + region));
        } else {
            addMaker(stNodeLatLng);
        }

        // LatLng stNodeLatLng = new LatLng(39.963175, 116.400244);
        // LatLng enNodeLatLng = new LatLng(32.081231, 112.13792);
        //
        // drivingSearch(stNodeLatLng, enNodeLatLng);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 添加标注
     * 
     * @param lat
     * @param lon
     */
    private void addMaker(LatLng latLng) {
        // 定义Maker坐标点
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_goods);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    OnGetGeoCoderResultListener myGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(BaiduMapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            }

            String strInfo = String
                    .format("纬度：%f 经度：%f", result.getLocation().latitude, result.getLocation().longitude);
            Toast.makeText(BaiduMapActivity.this, strInfo, Toast.LENGTH_LONG).show();
            enNodeLatLng = result.getLocation();
            drivingSearch(stNodeLatLng, enNodeLatLng);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            // if (result == null || result.error !=
            // SearchResult.ERRORNO.NO_ERROR) {
            // Toast.makeText(BaiduMapActivity.this, "抱歉，未能找到结果",
            // Toast.LENGTH_LONG).show();
            // }
            // mBaiduMap.clear();
            // mBaiduMap.addOverlay(new MarkerOptions().position(
            // result.getLocation())
            // .icon(BitmapDescriptorFactory
            // .fromResource(R.drawable.icon_marka)));
            // mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
            // .getLocation()));
            // Toast.makeText(BaiduMapActivity.this, result.getAddress(),
            // Toast.LENGTH_LONG).show();
        }
    };

    private void drivingSearch(LatLng stNodeLatLng, LatLng enNodeLatLng) {

        showDialog();
        // PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "龙泽");
        // PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西单");
        PlanNode stNode = PlanNode.withLocation(stNodeLatLng);
        PlanNode enNode = PlanNode.withLocation(enNodeLatLng);

        mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    OnGetRoutePlanResultListener myGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // 获取步行线路规划结果
        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            // 获取公交换乘路径规划结果
        }

        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            dismissDialog();
            // 获取驾车线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                route = result.getRouteLines().get(0);
                MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
    };

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGeoCoderSearch.destroy();
        mRoutePlanSearch.destroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    protected void showDialog() {
        if (mProgressiveDialog == null) {
            mProgressiveDialog = new ProgressiveDialog(this);
        }
        mProgressiveDialog.setMessage(R.string.loading);
        mProgressiveDialog.show();
    }

    protected void dismissDialog() {
        if (mProgressiveDialog != null && mProgressiveDialog.isShowing()) {
            mProgressiveDialog.dismiss();
        }
    }
}
