
package cn.com.incito.driver.fragments.infocenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverApplication;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.WeatherInfoActivity;
import cn.com.incito.driver.WeatherInfoActivity.WeatherData;
import cn.com.incito.driver.dao.Agent;
import cn.com.incito.driver.models.BaiduTelematicsV3WeatherCity;
import cn.com.incito.driver.models.BaiduTelematicsV3WeatherData;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ModelCar;
import cn.com.incito.driver.models.ModelNotices;
import cn.com.incito.driver.models.ModelTransport;
import cn.com.incito.driver.models.goods.GoodsTotal;
import cn.com.incito.driver.models.orders.MyOrdersTotal;
import cn.com.incito.driver.net.apis.WisdomCityRestClientParameterImpl;
import cn.com.incito.driver.net.apis.infocenter.BaiduTelematicsV3API;
import cn.com.incito.driver.net.apis.infocenter.MyInfoCenterAPI;
import cn.com.incito.driver.net.apis.infocenter.MyInfoCenterAPI.MyInfoCenterAPIResponse;
import cn.com.incito.driver.util.GetResourcesUtil;
import cn.com.incito.driver.util.LunarUtil;
import cn.com.incito.driver.util.WeatherIconUtil;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.assist.FailReason;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.JsonHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;

/**
 * 个人中心
 * 
 * @description 个人中心
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyInfoFragment extends Fragment {

    private TextView mTime, mDate, mLicense, mCreditcount, mDrivername, mCompany, mCredit, mRanking, mAddress,
            mPublishCity, mLeftLoad, mTodayGoodsTotal, mAllGoodsTotal, mPayorder, mPickingorder, mSignorder,
            mEvelorder, mCancelorder;

    private TextView mNotices;

    private ImageView mStarImg;

    private ImageView mPhoto;

    private LinearLayout mWeahterInfo, mAgentsInfo, mPayorderLinearLayout, mPickingorderLinearLayout,
            mSignorderLinearLayout, mEvelorderLinearLayout, mCancelorderLinearLayout, mPublishInfo, mTodayGoods,
            mAllGoods;

    private LayoutInflater mInflater;

    private SharedPreferences mShare;

    private String locationCity = "";

    private String locationAddress = "";

    private List<BaiduTelematicsV3WeatherCity> weathFormatData;

    private GridView gridView;

    private GridAdapter gridAdapter;

    private DriverMainActivity driverMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater = inflater;
        driverMainActivity = (DriverMainActivity) getActivity();
        mShare = DriverApplication.getInstance().getSharedPreferences();

        View view = inflater.inflate(R.layout.fragment_myinfo, null);
        // 天气预报
        mWeahterInfo = (LinearLayout) view.findViewById(R.id.weather_info);

        // 时间
        mTime = (TextView) view.findViewById(R.id.time);
        mDate = (TextView) view.findViewById(R.id.date);
        // 公告
        mNotices = (TextView) view.findViewById(R.id.notices);
        // 运力
        mPublishInfo = (LinearLayout) view.findViewById(R.id.publish_info);
        mPublishCity = (TextView) view.findViewById(R.id.publish_city);
        mLeftLoad = (TextView) view.findViewById(R.id.leftload);
        // 个人信息
        mLicense = (TextView) view.findViewById(R.id.license);
        mStarImg = (ImageView) view.findViewById(R.id.starImg);
        mCreditcount = (TextView) view.findViewById(R.id.creditcount);
        mDrivername = (TextView) view.findViewById(R.id.drivername);
        mCompany = (TextView) view.findViewById(R.id.company);
        mPhoto = (ImageView) view.findViewById(R.id.photo);
        mCredit = (TextView) view.findViewById(R.id.credit);
        mRanking = (TextView) view.findViewById(R.id.ranking);
        mAddress = (TextView) view.findViewById(R.id.address);

        // 货源
        mTodayGoods = (LinearLayout) view.findViewById(R.id.today_goods);
        mAllGoods = (LinearLayout) view.findViewById(R.id.all_goods);
        mTodayGoodsTotal = (TextView) view.findViewById(R.id.today_goods_total);
        mAllGoodsTotal = (TextView) view.findViewById(R.id.all_goods_total);

        // 订单
        mPayorderLinearLayout = (LinearLayout) view.findViewById(R.id.payorder_linearLayout);
        mPayorder = (TextView) view.findViewById(R.id.payorder);
        mPickingorderLinearLayout = (LinearLayout) view.findViewById(R.id.pickingorder_linearLayout);
        mPickingorder = (TextView) view.findViewById(R.id.pickingorder);
        mSignorderLinearLayout = (LinearLayout) view.findViewById(R.id.signorder_linearLayout);
        mSignorder = (TextView) view.findViewById(R.id.signorder);
        mEvelorderLinearLayout = (LinearLayout) view.findViewById(R.id.evelorder_linearLayout);
        mEvelorder = (TextView) view.findViewById(R.id.evelorder);
        mCancelorderLinearLayout = (LinearLayout) view.findViewById(R.id.cancelorder_linearLayout);
        mCancelorder = (TextView) view.findViewById(R.id.cancelorder);
        // 货代
        //
        mAgentsInfo = (LinearLayout) view.findViewById(R.id.agents_info);

        // 初始化信息
        initData();

        mPublishInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.toggleContent(mPublishInfo.getId());
            }
        });
        mTodayGoods.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.boardSearch = new HashMap<String, String>();
                driverMainActivity.boardSearch.put("nearlycity",
                        mShare.getString(Constants.LOCATION_CITY, "").split("市")[0]);
                driverMainActivity.boardSearch.put("nearlydate", "0");
                driverMainActivity.toggleContent(mTodayGoods.getId());
            }
        });
        mAllGoods.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.boardSearch = new HashMap<String, String>();
                driverMainActivity.boardSearch.put("nearlycity",
                        mShare.getString(Constants.LOCATION_CITY, "").split("市")[0]);
                driverMainActivity.boardSearch.put("nearlydate", "");
                driverMainActivity.toggleContent(mAllGoods.getId());
            }
        });
        mWeahterInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                WeatherData data = new WeatherData();
                if (weathFormatData != null && weathFormatData.size() != 0) {
                    Intent intent = new Intent(getActivity(), WeatherInfoActivity.class);
                    data.setWeathFormatData(weathFormatData);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }

            }
        });
        //
        mPayorderLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.order_status = 1;
                driverMainActivity.toggleContent(mPayorderLinearLayout.getId());
            }
        });
        mPickingorderLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.order_status = 2;
                driverMainActivity.toggleContent(mPickingorderLinearLayout.getId());
            }
        });
        mSignorderLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.order_status = 3;
                driverMainActivity.toggleContent(mSignorderLinearLayout.getId());

            }
        });
        mEvelorderLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.order_status = 4;
                driverMainActivity.toggleContent(mEvelorderLinearLayout.getId());
            }
        });
        mCancelorderLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.order_status = 5;
                driverMainActivity.toggleContent(mCancelorderLinearLayout.getId());

            }
        });

        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agent agent = gridAdapter.list.get(position);
                driverMainActivity.boardSearch = new HashMap<String, String>();
                driverMainActivity.boardSearch.put("agentid", agent.getMId().toString());
                driverMainActivity.toggleContent(mAgentsInfo.getId());
            }
        });
        return view;
    }

    private void initData() {
        locationCity = mShare.getString(Constants.LOCATION_CITY, "").split("市")[0];
        // 天气预报
        // initWeather();//此处注掉，在初始化运力信息的时候获得天气信息

        // 时间
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        cal.setTimeZone(TimeZone.getDefault());
        // System.out.println("公历日期:"+sdf.format(cal.getTime()));
        mTime.setText(sdf.format(cal.getTime()));

        LunarUtil lunar = new LunarUtil(cal);
        mDate.setText(lunar.toString());

        if (NetworkUtils.isNetworkAvaliable(getActivity())) {
            MyInfoCenterAPI myInfoCenterAPI = new MyInfoCenterAPI(GlobalModel.getInst().mLoginModel.getCarId(),
                    locationCity);
            new WisdomCityHttpResponseHandler(myInfoCenterAPI, new APIFinishCallback() {

                @Override
                public void OnRemoteApiFinish(BasicResponse response) {
                    if (response.status == BasicResponse.SUCCESS) {

                        MyInfoCenterAPIResponse myInfoCenterAPIResponse = (MyInfoCenterAPIResponse) response;

                        // 公告
                        initNotices(myInfoCenterAPIResponse.mNoticesList);
                        // 运力
                        initTransport(myInfoCenterAPIResponse.mModelTransport);
                        // 个人信息
                        initModelCar(myInfoCenterAPIResponse.mCar);
                        // 货源
                        initGoods(myInfoCenterAPIResponse.mGoodsTotal);
                        // 订单
                        initOrder(myInfoCenterAPIResponse.mMyOrdersTotal);
                        // 货代
                        initAgentInfo(myInfoCenterAPIResponse.mAgentList);

                    } else {

                    }
                }
            });
            WisdomCityRestClient.execute(myInfoCenterAPI);
        } else {
            Toast.makeText(getActivity(), R.string.errcode_network_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 天气预报
     */
    private void initWeather() {
        // 天气预报
        String relativeUrl = "weather?";
        RequestParams requestParams = new RequestParams();
        requestParams.put("output", "json");
        requestParams.put("ak", "1b0704b98c5aa65c3bf624d125a99d12");
        requestParams.put("location", locationCity);
        BaiduTelematicsV3API.get(relativeUrl, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Log.d("BaiduTelematicsV3API onSuccess(JSONObject response)", response.toString());
                Log.d("NET_LOG", "RESPONSE " + response.toString());
                weathFormatData = parseWeatherParams(response);
                mWeahterInfo.removeAllViews();
                if (weathFormatData != null && weathFormatData.size() != 0) {
                    for (final BaiduTelematicsV3WeatherCity city : weathFormatData) {
                        View weatherView = mInflater.inflate(R.layout.fragment_myinfo_weather_info, null);
                        TextView cityName = (TextView) weatherView.findViewById(R.id.cityname);
                        ImageView weatherPic = (ImageView) weatherView.findViewById(R.id.weather_pic);
                        cityName.setText(city.getCurrentCity());
                        // String dayPictureUrl = city.getWeather_data()
                        // .get(0).getDayPictureUrl();
                        // ImageLoader.getInstance().displayImage(
                        // dayPictureUrl, weatherPic);
                        weatherPic.setBackgroundResource(new WeatherIconUtil().transferImg(city.getWeather_data()
                                .get(0).getWeather()));
                        TextView temperature = (TextView) weatherView.findViewById(R.id.temperature);
                        temperature.setText(city.getWeather_data().get(0).getTemperature());
                        mWeahterInfo.addView(weatherView);
                        break;
                    }
                }

                /*
                 * try { String status = response.getString("status");
                 * String date = response.getString("date"); String
                 * error = response.getString("error"); // if
                 * (status.equalsIgnoreCase("success") // &&
                 * error.equalsIgnoreCase("0")) { JSONArray results =
                 * response .getJSONArray("results"); for (int i = 0; i
                 * < results.length(); i++) { JSONObject jsonObject =
                 * results .getJSONObject(i); String currentCity =
                 * jsonObject .getString("currentCity");
                 * 
                 * View weatherView = mInflater.inflate(
                 * R.layout.weather_info, null); TextView cityName =
                 * (TextView) weatherView .findViewById(R.id.cityname);
                 * ImageView weatherPic = (ImageView) weatherView
                 * .findViewById(R.id.weather_pic);
                 * cityName.setText(currentCity); JSONArray weatherArray
                 * = jsonObject .getJSONArray("weather_data");
                 * JSONObject weatherObject = weatherArray
                 * .getJSONObject(0); String dayPictureUrl =
                 * weatherObject .getString("dayPictureUrl");
                 * ImageLoader.getInstance().displayImage(
                 * dayPictureUrl, weatherPic);
                 * 
                 * // for (int j = 0; j < // weatherArray.length(); j++)
                 * { // JSONObject weatherObject = //
                 * weatherArray.getJSONObject(j); // String //
                 * date1=weatherObject.getString("date"); // String //
                 * nightPictureUrl
                 * =weatherObject.getString("nightPictureUrl"); //
                 * String //
                 * dayPictureUrl=weatherObject.getString("dayPictureUrl"
                 * ); // String //
                 * weather=weatherObject.getString("weather"); // String
                 * // wind=weatherObject.getString("wind"); // String //
                 * temperature=weatherObject.getString("temperature");
                 * // // }
                 * 
                 * weatherView.setOnClickListener(new OnClickListener()
                 * {
                 * 
                 * @Override public void onClick(View arg0) { // TODO
                 * Auto-generated method stub Intent intent = new
                 * Intent(getActivity(), WeatherInfoActivity.class);
                 * startActivity(intent); } });
                 * mWeahterInfo.addView(weatherView);
                 * 
                 * } // }
                 * 
                 * } catch (JSONException e) { // TODO Auto-generated
                 * catch block e.printStackTrace(); }
                 */
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                Log.d("BaiduTelematicsV3API onFailure(Throwable e, JSONObject errorResponse)", e.getMessage());
            }

        });
    }

    /**
     * 公告信息
     * 
     * @param
     */
    private void initNotices(List<ModelNotices> mNoticesList) {
        // 公共
        StringBuilder noticeStr = new StringBuilder();
        if (mNoticesList.size() > 0) {

            for (int i = 0; i < mNoticesList.size(); i++) {
                ModelNotices modelNotices = mNoticesList.get(i);
                noticeStr.append(modelNotices.getNotice());
                noticeStr.append("\n");
            }

        } else {
            noticeStr.append("暂无公告");
        }
        mNotices.setText(noticeStr.toString());
    }

    /**
     * 运力信息
     * 
     * @param
     */
    private void initTransport(ModelTransport modelTransport) {
        StringBuilder cityBuilder = new StringBuilder();
        cityBuilder.append(modelTransport.getCarcity());
        cityBuilder.append("→");
        // cityBuilder.append("\n");
        String[] targetCity = modelTransport.getTargetcity().split(",");
        for (int i = 0; i < targetCity.length; i++) {
            cityBuilder.append(targetCity[i]);
            if (i < targetCity.length - 1) {// 不是最后一个城市
                cityBuilder.append("→");
            }
            if (!locationCity.equals(targetCity[i])) {
                locationCity += "|" + targetCity[i];
            }

        }
        mPublishCity.setText(cityBuilder.toString());
        mLeftLoad.setText(modelTransport.getLeftload());

        initWeather();
    }

    /**
     * 运力信息
     * 
     * @param
     */
    private void initModelCar(ModelCar modelCar) {
        locationAddress = mShare.getString(Constants.LOCATION_ADDRESS, "");
        mLicense.setText(modelCar.getLicense());
        mStarImg.setImageResource(GetResourcesUtil.getDrawableIdentifier(getActivity(), modelCar.getStarImg()));
        mCreditcount.setText(modelCar.getCreditcount());
        mDrivername.setText(modelCar.getDriverName());
        mCompany.setText(modelCar.getCompany());
        if (!TextUtils.isEmpty(modelCar.getPhoto())) {
            ImageLoader.getInstance().displayImage(WisdomCityRestClientParameterImpl.getUrl() + modelCar.getPhoto(),
                    mPhoto, new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            mPhoto.setImageResource(R.drawable.default_photo);

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            // TODO Auto-generated method stub

                        }
                    });
        }
        mCredit.setText(modelCar.getCredit());
        mRanking.setText(modelCar.getRankings());

        String[] addr = locationAddress.split(",");
        String address = "";
        for (int i = 0; i < addr.length; i++) {
            address += addr[i];
        }
        mAddress.setText(address);

    }

    /**
     * 货源信息
     * 
     * @param
     */
    private void initGoods(GoodsTotal mGoodsTotal) {
        mTodayGoodsTotal.setText(mGoodsTotal.getTodayTotal());
        mAllGoodsTotal.setText(mGoodsTotal.getAllTotal());
    }

    /**
     * 订单信息
     * 
     * @param
     */
    private void initOrder(MyOrdersTotal myOrdersTotal) {
        if (myOrdersTotal.getPayorder().equals("0")) {
            mPayorderLinearLayout.setVisibility(View.GONE);
        } else {
            mPayorder.setText(myOrdersTotal.getPayorder());
        }
        if (myOrdersTotal.getPickingorder().equals("0")) {
            mPickingorderLinearLayout.setVisibility(View.GONE);
        } else {
            mPickingorder.setText(myOrdersTotal.getPickingorder());
        }
        if (myOrdersTotal.getSignorder().equals("0")) {
            mSignorderLinearLayout.setVisibility(View.GONE);
        } else {
            mSignorder.setText(myOrdersTotal.getSignorder());
        }
        if (myOrdersTotal.getEvelorder().equals("0")) {
            mEvelorderLinearLayout.setVisibility(View.GONE);
        } else {
            mEvelorder.setText(myOrdersTotal.getEvelorder());
        }
        if (myOrdersTotal.getCancelorder().equals("0")) {
            mCancelorderLinearLayout.setVisibility(View.GONE);
        } else {
            mCancelorder.setText(myOrdersTotal.getCancelorder());
        }
    }

    /**
     * 货代信息
     * 
     * @param
     */
    private void initAgentInfo(List<Agent> mAgentList) {
        // 货代信息
        if (mAgentList.size() > 0) {
            for (int i = 0; i < mAgentList.size(); i++) {
                // Agent agent = mAgentList.get(i);
                // TextView newAgent = new TextView(getActivity());
                // newAgent.setId(i);
                // LinearLayout.LayoutParams layoutParams = new
                // LinearLayout.LayoutParams(
                // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                // newAgent.setLayoutParams(layoutParams);
                // newAgent.setText(i + "." + agent.getCompany());
                // newAgent.setOnClickListener(new OnClickListener() {
                //
                // @Override
                // public void onClick(View arg0) {
                // // TODO Auto-generated method
                // // stub
                //
                // }
                // });
                // mAgentsInfo.addView(newAgent);

                //
                gridAdapter = new GridAdapter(getActivity());
                gridAdapter.setList(mAgentList);
                gridView.setAdapter(gridAdapter);
                gridAdapter.notifyDataSetChanged();
            }
        } else {

            TextView noAgent = new TextView(getActivity());
            noAgent.setText("没有与我完成交易的货代");
            mAgentsInfo.addView(noAgent);
        }
    }

    public class GridAdapter extends BaseAdapter {

        private Context context;

        private List<Agent> list;

        private LayoutInflater mInflater;

        public GridAdapter(Context c) {
            super();
            this.context = c;
        }

        public void setList(List<Agent> list) {
            this.list = list;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int index) {

            return list.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            Viewholder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fragment_myinfo_agentinfo_item, null);
                holder = new Viewholder();
                holder.mAgentId = (TextView) convertView.findViewById(R.id.agent_id);
                holder.mAgentName = (TextView) convertView.findViewById(R.id.agent_name);

                convertView.setTag(holder);

            } else {
                holder = (Viewholder) convertView.getTag();

            }
            Agent info = list.get(index);
            if (info != null) {
                holder.mAgentId.setText(String.valueOf(info.getId()));
                holder.mAgentName.setText(info.getCompany());

            }
            return convertView;
        }

    }

    class Viewholder {
        TextView mAgentId;

        TextView mAgentName;

    }

    protected List<BaiduTelematicsV3WeatherCity> parseWeatherParams(JSONObject response) {
        List<BaiduTelematicsV3WeatherCity> baiduTelematicsV3WeatherCities = new ArrayList<BaiduTelematicsV3WeatherCity>();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                BaiduTelematicsV3WeatherCity baiduTelematicsV3WeatherCity = new BaiduTelematicsV3WeatherCity();
                String currentCity = jsonObject.getString("currentCity");
                baiduTelematicsV3WeatherCity.setCurrentCity(currentCity);
                JSONArray weatherArray = jsonObject.getJSONArray("weather_data");

                List<BaiduTelematicsV3WeatherData> weathers = new ArrayList<BaiduTelematicsV3WeatherData>();
                for (int j = 0; j < weatherArray.length(); j++) {
                    JSONObject weather = weatherArray.getJSONObject(j);
                    BaiduTelematicsV3WeatherData baiduTelematicsV3WeatherData = new BaiduTelematicsV3WeatherData();
                    baiduTelematicsV3WeatherData.setDate(weather.getString("date"));
                    baiduTelematicsV3WeatherData.setDayPictureUrl(weather.getString("dayPictureUrl"));
                    baiduTelematicsV3WeatherData.setNightPictureUrl(weather.getString("nightPictureUrl"));
                    baiduTelematicsV3WeatherData.setWeather(weather.getString("weather"));
                    baiduTelematicsV3WeatherData.setWind(weather.getString("wind"));
                    baiduTelematicsV3WeatherData.setTemperature(weather.getString("temperature"));
                    weathers.add(baiduTelematicsV3WeatherData);
                }
                baiduTelematicsV3WeatherCity.setWeather_data(weathers);
                baiduTelematicsV3WeatherCities.add(baiduTelematicsV3WeatherCity);
            }
        } catch (Exception e) {
            return null;
        }

        return baiduTelematicsV3WeatherCities;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}
