
package cn.com.incito.driver;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.net.apis.WisdomCityRestClientParameterImpl;
import cn.com.incito.wisdom.sdk.cache.disk.impl.TotalSizeLimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.naming.Md5FileNameGenerator;
import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoaderConfiguration;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRULimitedMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRUMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.QueueProcessingType;
import cn.com.incito.wisdom.sdk.net.download.BaseImageDownloader;
import cn.com.incito.wisdom.sdk.net.download.SlowNetworkImageDownloader;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.openudid.OpenUDIDManager;
import cn.com.incito.wisdom.sdk.utils.StorageUtils;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.MKGeneralListener;
//import com.baidu.mapapi.map.MKEvent;
import com.umeng.analytics.MobclickAgent;

/**
 * 全局应用环境
 * 
 * @description 初始配置全局应用环境
 * @author lizhan
 * @createDate 2014年10月13日
 * @version 1.0
 */
public class DriverApplication extends FrontiaApplication {
    private static final String TAG = DriverApplication.class.getSimpleName();

    private static DriverApplication mInstance = null;

    private static String IMEI;

    private SharedPreferences mPrefs;

    public SharedPreferences getSharedPreferences() {
        return mPrefs;
    }

    public LocationClient mLocClient;

    public BDLocationListener myListener = new MyLocationListener();

    public BDLocation bdLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // 科大讯飞语言服务
        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 设置你申请的应用appid
        // SpeechUtility.createUtility(DriverApplication.this, "appid="
        // + getString(R.string.app_id));
        // SpeechUtility.createUtility(DriverApplication.this, "appid="
        // + MetadataUtils.getMetaValue(DriverApplication.this, "app_id"));

        // 百度地图、定位、导航
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());

        MobclickAgent.openActivityDurationTrack(false);// 禁止友盟的自动统计功能
        mInstance = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();
        WisdomCityRestClient.init(getApplicationContext(), new WisdomCityRestClientParameterImpl(
                getApplicationContext().getResources()), null, Constants.REST_CLIENT_LOG_SENABLE_LOGGING);
        GlobalModel.init(getApplicationContext());
        OpenUDIDManager.sync(this);
        // initEngineManager(this);
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
                Constants.WISDOMCITY_IAMGE_CACHE_SDCARD_PATH);
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        AbstractMemoryCache<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LRUMemoryCacheBitmapCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCacheBitmapCache(memoryCacheSize);
        }
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(memoryCache)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new TotalSizeLimitedDiscCache(cacheDir, new Md5FileNameGenerator(), 10 * 1024 * 1024))
                .imageDownloader(new SlowNetworkImageDownloader(new BaseImageDownloader(this)))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .enableLogging() // Not necessary in common
                .build();
        ImageLoader.getInstance().init(config);

        // 初始化百度定位
        initLocation();

    }

    private void initLocation() {
        mLocClient = new LocationClient(this.getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        // option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(1000 * 30);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            bdLocation = location;

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            sb.append("\nprovince : ");
            sb.append(location.getProvince());
            sb.append("\ncity : ");
            sb.append(location.getCity());
            sb.append("\ndistrict : ");
            sb.append(location.getDistrict());
            sb.append("\nstreet : ");
            sb.append(location.getStreet());
            sb.append("\nstreet number: ");
            sb.append(location.getStreetNumber());
            sb.append("\naddstr : ");
            sb.append(location.getAddrStr());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }

            Log.d(TAG, sb.toString());
        }
    }

    public static DriverApplication getInstance() {
        return mInstance;
    }

    public final String getUDID() {
        if (OpenUDIDManager.isInitialized()) {
            return IMEI + "_" + OpenUDIDManager.getOpenUDID();
        }
        return IMEI;
    }

    public final String getAuthUserId() {
        return GlobalModel.getInst().mLoginModel.getCarId();
    }

    public final String getAuthUserType() {
        return Constants.USER_AUTH_TYPE;
    }

}
