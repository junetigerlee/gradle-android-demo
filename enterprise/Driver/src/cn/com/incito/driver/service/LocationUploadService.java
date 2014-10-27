
package cn.com.incito.driver.service;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverApplication;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.net.apis.LocationUploadAPI;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * location upload
 * 
 * @description location upload
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class LocationUploadService extends Service {

    private static final String TAG = LocationUploadService.class.getSimpleName();

    private static final int LOCATION_MESSAGE = 8888;

    private Context mContext;

    private SharedPreferences mShare;

    private Editor mEditor;

    // 定位相关
    private LocationClient mLocClient;

    private MyLocationListenner myListener = new MyLocationListenner();

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOCATION_MESSAGE:
                    // 定位
                    initLocation(mContext);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();
        mShare = DriverApplication.getInstance().getSharedPreferences();
        new Thread() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = LOCATION_MESSAGE;
                mHandler.sendMessage(msg);
            }

        }.start();

    }

    @Override
    public void onDestroy() {
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
            mLocClient = null;
        }
        super.onDestroy();
    }

    /**
     * 初始化百度定位
     */
    public void initLocation(Context context) {
        // 定位初始化

        mLocClient = new LocationClient(this.getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(Constants.LOCATION_UPLOAD_TIME);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocClient.setLocOption(option);
        mLocClient.start();

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            Log.v(TAG, "location.getLatitude():" + location.getLatitude());
            Log.v(TAG, "location.getLongitude():" + location.getLongitude());
            Log.v(TAG, "location.getCity():" + location.getCity());
            Log.v(TAG, "location.getProvince():" + location.getProvince());
            Log.v(TAG, "location.getAddrStr():" + location.getAddrStr());
            // 缓存当前位置
            mEditor = mShare.edit();
            mEditor.putString(Constants.LOCATION_LATITUDE, String.valueOf(location.getLatitude()));
            mEditor.putString(Constants.LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
            mEditor.putString(Constants.LOCATION_CITY, String.valueOf(location.getCity()));
            mEditor.putString(Constants.LOCATION_ADDRESS, String.valueOf(location.getAddrStr()));
            mEditor.commit();
            if (NetworkUtils.isNetworkAvaliable(mContext)) {

                if (null != location.getCity() && null != location.getProvince()) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("province", location.getProvince());
                        jsonObject.put("localcity", location.getCity());
                        jsonObject.put("location", location.getLatitude() + "," + location.getLongitude());
                        jsonObject.put("carid", GlobalModel.getInst().mLoginModel.getCarId());

                        LocationUploadAPI locationUploadAPI = new LocationUploadAPI(jsonObject.toString());
                        new WisdomCityHttpResponseHandler(locationUploadAPI, new APIFinishCallback() {

                            @Override
                            public void OnRemoteApiFinish(BasicResponse response) {
                                System.out.println(TAG + " : " + response.status);
                                if (response.status == BasicResponse.SUCCESS) {

                                } else {

                                }
                            }
                        });
                        WisdomCityRestClient.execute(locationUploadAPI);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else {

            }

        }

    }

}
