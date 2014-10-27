
package cn.com.incito.driver.fragments.goods;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.UI.detailDialog.GoodsGrabFailureDialog;
import cn.com.incito.driver.UI.detailDialog.GoodsGrabSucessDialog;
import cn.com.incito.driver.fragments.PageListAbstractFragment;
import cn.com.incito.driver.models.AbstractModel;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.goods.GoodsAvailableModel;
import cn.com.incito.driver.net.apis.goods.GoodsGrabAPI;
import cn.com.incito.driver.provider.DataProvider;
import cn.com.incito.driver.util.DateUtil;
import cn.com.incito.driver.util.ParseUtil;
import cn.com.incito.wisdom.sdk.adapter.PageCursor;
import cn.com.incito.wisdom.sdk.adapter.PageCursorAdapter;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.refreshable.RefreshableListView;
import cn.com.incito.wisdom.uicomp.widget.textview.MarqueeTextView;

import com.umeng.analytics.MobclickAgent;

/**
 * 抢货源-可抢货源
 * 
 * @description 抢货源-可抢货源
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsAvailableFragment extends PageListAbstractFragment<ListView, GoodsAvailableModel.Response> {

    private DriverMainActivity mActivity;

    private LayoutInflater mInflater;

    private DriverMainActivity driverMainActivity;

    private static final String[] PROJECTIONS = new String[] {
            AbstractModel.COLUMN_ID, GoodsAvailableModel.COLUMN_GOODS_ORIGINALREGION,
            GoodsAvailableModel.COLUMN_GOODS_RECEIPTREGION, GoodsAvailableModel.COLUMN_GOODS_ORIGINALCITY,
            GoodsAvailableModel.COLUMN_GOODS_RECEIPTCITY, GoodsAvailableModel.COLUMN_GOODS_ORIGINALPROVINCE,
            GoodsAvailableModel.COLUMN_GOODS_RECEIPTPROVINCE, GoodsAvailableModel.COLUMN_GOODS_NAME,
            GoodsAvailableModel.COLUMN_GOODS_TYPE, GoodsAvailableModel.COLUMN_GOODS_FARE,
            GoodsAvailableModel.COLUMN_GOODS_INFOFARE, GoodsAvailableModel.COLUMN_GOODS_CARLENGTH,
            GoodsAvailableModel.COLUMN_GOODS_WEIGHT, GoodsAvailableModel.COLUMN_GOODS_VOLUME,
            GoodsAvailableModel.COLUMN_GOODS_COUNT, GoodsAvailableModel.COLUMN_GOODS_GENERATETIME,
            GoodsAvailableModel.COLUMN_GOODS_GOODSTIME, GoodsAvailableModel.COLUMN_GOODS_COMPLETETIME,
            GoodsAvailableModel.COLUMN_GOODS_GOODSID, GoodsAvailableModel.COLUMN_GOODS_CARID,
            GoodsAvailableModel.COLUMN_GOODS_GRABFLAG, GoodsAvailableModel.COLUMN_GOODS_GRABRESULT,
            GoodsAvailableModel.COLUMN_GOODS_ORDERID, GoodsAvailableModel.COLUMN_GOODS_FAILUREREASON,
            AbstractModel.COLUMN_CREATE_TIME, ListAbstractModel.VCOLUMN_MID
    };

    private static final int DEFUALT_ONE_PAGE_COUNT = 20;

    private GrabGoodsFragment grabGoodsFragment;

    // 操作成功
    private Map<String, String> grabedMap = new HashMap<String, String>();

    // 操作失败
    private Map<String, String> disableMap = new HashMap<String, String>();

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        driverMainActivity = (DriverMainActivity) getActivity();
        _RESULT_NO_DATA = "";
        mPageSize = DEFUALT_ONE_PAGE_COUNT;
        mInflater = inflater;
        mActivity = (DriverMainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_goods_available, null);

        inintView(view);
        return view;
    }

    public void inintView(View view) {

        grabGoodsFragment = (GrabGoodsFragment) getParentFragment();

        mPullToRefreshWidget = (RefreshableListView) view.findViewById(R.id.listView);
        mPullToRefreshWidget.setMode(mRefreshMode);
        mPullToRefreshWidget.setOnRefreshListener(this);
        ListView listView = mPullToRefreshWidget.getRefreshableView();
        mAdapter = new GoodsAvailableFragmentAdapter(getActivity(), null, false);
        listView.setAdapter(mAdapter);
        // set listview empty view
        View emptyView = mInflater.inflate(R.layout.fragment_goods_grap_empty_list, null);
        listView.setEmptyView(emptyView);
    }

    public void onEvent(final GoodsAvailableModel.Response resp) {
        handleProviderResponse(resp);
    }

    public class GoodsAvailableFragmentAdapter extends PageCursorAdapter implements OnClickListener {

        public GoodsAvailableFragmentAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            mInflater = LayoutInflater.from(context);
        }

        public GoodsAvailableFragmentAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, PageCursor cursor, ViewGroup parent) {
            View convertView = mInflater.inflate(R.layout.fragment_goods_available_list_item, null);
            ViewHolder.findAndCacheViews(convertView);
            return convertView;
        }

        public void bindView(View view, Context context, final PageCursor cursor, final int position) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.position = cursor.getPosition();

            viewHolder.route.setText(cursor.getString(cursor
                    .getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_ORIGINALPROVINCE))
                    + cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_ORIGINALCITY))
                    + cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_ORIGINALREGION))
                    + "→"
                    + cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_RECEIPTPROVINCE))
                    + cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_RECEIPTCITY))
                    + cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_RECEIPTREGION)));

            String fare = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_FARE));

            if (TextUtils.isEmpty(fare)) {
                viewHolder.fare.setText(mContext.getResources().getString(R.string.myorders_order_item_fare));
                viewHolder.price_tv.setVisibility(View.GONE);
            } else {
                viewHolder.fare.setText(fare);
            }
            String infofare = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_INFOFARE));
            if (TextUtils.isEmpty(infofare)) {
                viewHolder.infoFare.setText(mContext.getResources().getString(R.string.myorders_order_item_fare));
                viewHolder.price2_tv.setVisibility(View.GONE);
            } else {
                viewHolder.infoFare.setText(infofare);
            }

            viewHolder.goodsName
                    .setText(cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_NAME)));

            String goodstype = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_TYPE));

            viewHolder.goodsType.setText(getGoodsType(Integer.parseInt(goodstype)));

            String weight = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_WEIGHT));
            if (!TextUtils.isEmpty(weight)) {
                viewHolder.weight.setText(ParseUtil.parseDoubleToString(Double.parseDouble(weight)));
                viewHolder.llytWeight.setVisibility(View.VISIBLE);
                viewHolder.weight.setVisibility(View.VISIBLE);
                viewHolder.textWeight.setVisibility(View.VISIBLE);
            } else {
                viewHolder.llytWeight.setVisibility(View.GONE);
                viewHolder.weight.setVisibility(View.GONE);
                viewHolder.textWeight.setVisibility(View.GONE);

            }
            String volume = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_VOLUME));

            if (!TextUtils.isEmpty(volume)) {
                viewHolder.volume.setText(ParseUtil.parseDoubleToString(Double.parseDouble(volume)));
                viewHolder.llytVolume.setVisibility(View.VISIBLE);
                viewHolder.volume.setVisibility(View.VISIBLE);
                viewHolder.textVolume.setVisibility(View.VISIBLE);
            } else {
                viewHolder.llytVolume.setVisibility(View.GONE);
                viewHolder.volume.setVisibility(View.GONE);
                viewHolder.textVolume.setVisibility(View.GONE);
            }

            String count = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_COUNT));

            if (!TextUtils.isEmpty(count)) {
                viewHolder.count.setText(count);
                viewHolder.llytCount.setVisibility(View.VISIBLE);
                viewHolder.count.setVisibility(View.VISIBLE);
                viewHolder.textCount.setVisibility(View.VISIBLE);
            } else {
                viewHolder.llytCount.setVisibility(View.GONE);
                viewHolder.count.setVisibility(View.GONE);
                viewHolder.textCount.setVisibility(View.GONE);
            }

            viewHolder.generateTime.setText(String.format(mContext.getResources().getString(R.string.generatetime),
                    cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_GENERATETIME))));

            String id = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.VCOLUMN_MID));
            if (grabedMap.containsKey(id)) {
                viewHolder.grabOrder.setBackgroundResource(R.drawable.goods_grab_btn_grabed);
                viewHolder.grabOrder.setEnabled(false);
            } else if (disableMap.containsKey(id)) {
                viewHolder.grabOrder.setBackgroundResource(R.drawable.goods_grab_btn_disabled);
                viewHolder.grabOrder.setEnabled(false);
            } else {
                viewHolder.grabOrder.setBackgroundResource(R.drawable.goods_grab_btn_selector);
                viewHolder.grabOrder.setEnabled(true);
                viewHolder.grabOrder.setFocusable(false);
                viewHolder.grabOrder.setFocusableInTouchMode(false);
                viewHolder.grabOrder.setTag(viewHolder.position);
                viewHolder.grabOrder.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            final View view = v;
            int position = (Integer) v.getTag();
            final PageCursor cursor = (PageCursor) mAdapter.getItem(position);

            if (v.getId() == R.id.btn_grab_order) {
                if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
                    showToast(R.string.errcode_network_unavailable);
                    return;
                }
                showDialog();

                final String id = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.VCOLUMN_MID));
                String goodId = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_GOODSID));

                String generatetime = cursor.getString(cursor
                        .getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_GENERATETIME));
                generatetime = DateUtil.dateToString(DateUtil.stringtoDate(generatetime, DateUtil.FORMAT_CHINA),
                        DateUtil.FORMAT_ONE);
                String goodstime = cursor.getString(cursor.getColumnIndex(GoodsAvailableModel.COLUMN_GOODS_GOODSTIME));

                GoodsGrabAPI goodsGrabAPI = new GoodsGrabAPI(id, goodId, generatetime, goodstime);

                new WisdomCityHttpResponseHandler(goodsGrabAPI, new APIFinishCallback() {

                    @Override
                    public void OnRemoteApiFinish(BasicResponse response) {
                        dismissDialog();
                        if (getActivity() == null || getActivity().isFinishing()) {
                            return;
                        }
                        if (response.status == BasicResponse.SUCCESS) {
                            // loadData(true, false);
                            view.setBackgroundResource(R.drawable.goods_grab_btn_grabed);
                            view.setEnabled(false);
                            // 缓存抢单成功数据
                            grabedMap.put(id, id);

                            final GoodsGrabSucessDialog goodsGrabSucessDialog = new GoodsGrabSucessDialog(
                                    getActivity(), null);
                            goodsGrabSucessDialog.setCanceledOnTouchOutside(true);
                            goodsGrabSucessDialog.show();

                            final Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                public void run() {

                                    // when the task active then close
                                    // the dialog
                                    goodsGrabSucessDialog.dismiss();
                                    // cancel the timer,
                                    // otherwise, you may
                                    // receive a crash
                                    t.cancel();
                                }
                            }, 3000);// after 3 second ( 3000
                            // miliseconds), the task will
                            // be active.

                        } else {
                            // loadData(true, false);

                            view.setBackgroundResource(R.drawable.goods_grab_btn_disabled);
                            view.setEnabled(false);

                            // 缓存抢单失败数据
                            disableMap.put(id, id);

                            final GoodsGrabFailureDialog goodsGrabFailureDialog = new GoodsGrabFailureDialog(
                                    getActivity(), null);
                            goodsGrabFailureDialog.setCanceledOnTouchOutside(true);
                            goodsGrabFailureDialog.show();

                            final Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                public void run() {

                                    // when the task active then close
                                    // the dialog
                                    goodsGrabFailureDialog.dismiss();
                                    // cancel the timer,
                                    // otherwise, you may
                                    // receive a crash
                                    t.cancel();
                                }
                            }, 3000);// after 3 second ( 3000
                            // miliseconds), the task will
                            // be active.
                        }
                    }
                });
                WisdomCityRestClient.execute(goodsGrabAPI);
            }
        }
    }

    private static class ViewHolder {

        public MarqueeTextView route;// 收发货地

        public TextView fareLable;

        public TextView infoFareLable;

        public TextView fare;

        public TextView infoFare;

        public TextView price_tv;

        public TextView price2_tv;

        public LinearLayout llytWeight;

        public LinearLayout llytCount;

        public LinearLayout llytVolume;

        public MarqueeTextView weight;

        public MarqueeTextView count;

        public MarqueeTextView volume;

        public TextView textWeight;

        public TextView textCount;

        public TextView textVolume;

        public TextView goodsName;

        public TextView goodsType;

        public TextView generateTime;

        public Button grabOrder;

        int position;

        public static ViewHolder findAndCacheViews(View convertView) {
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.route = (MarqueeTextView) convertView.findViewById(R.id.tv_route);
            viewHolder.route.setInnerFocus(true);
            viewHolder.llytWeight = (LinearLayout) convertView.findViewById(R.id.llyt_weight);
            viewHolder.weight = (MarqueeTextView) convertView.findViewById(R.id.tv_weight);
            viewHolder.weight.setInnerFocus(true);
            viewHolder.llytCount = (LinearLayout) convertView.findViewById(R.id.llyt_count);
            viewHolder.count = (MarqueeTextView) convertView.findViewById(R.id.tv_count);
            viewHolder.count.setInnerFocus(true);
            viewHolder.llytVolume = (LinearLayout) convertView.findViewById(R.id.llyt_volume);
            viewHolder.volume = (MarqueeTextView) convertView.findViewById(R.id.tv_volume);
            viewHolder.volume.setInnerFocus(true);
            viewHolder.fareLable = (TextView) convertView.findViewById(R.id.tv_fare_lable);
            viewHolder.fare = (TextView) convertView.findViewById(R.id.tv_fare);
            viewHolder.infoFareLable = (TextView) convertView.findViewById(R.id.tv_infofare_lable);
            viewHolder.infoFare = (TextView) convertView.findViewById(R.id.tv_infofare);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            viewHolder.goodsType = (TextView) convertView.findViewById(R.id.tv_goodstype);
            viewHolder.generateTime = (TextView) convertView.findViewById(R.id.tv_generatetime);
            viewHolder.price_tv = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.price2_tv = (TextView) convertView.findViewById(R.id.tv_price2);
            viewHolder.textWeight = (TextView) convertView.findViewById(R.id.tv_weight_text);
            viewHolder.textCount = (TextView) convertView.findViewById(R.id.tv_count_text);
            viewHolder.textVolume = (TextView) convertView.findViewById(R.id.tv_volume_text);
            viewHolder.grabOrder = (Button) convertView.findViewById(R.id.btn_grab_order);

            convertView.setTag(viewHolder);
            return viewHolder;
        }
    }

    @Override
    protected void loadData(boolean update, boolean loadmore) {
        Log.e(LOG_TAG, TAG + " loadData(" + update + ", " + loadmore + ")    ");
        int offset = loadmore ? mAdapter.getCount() : 0;
        if (update) {
            // 清空抢单成功数据
            grabedMap.clear();
            // 清空抢单失败数据
            disableMap.clear();
            // queryTotal
            grabGoodsFragment.queryTotal();

            StringBuffer selection = new StringBuffer();
            selection.append(GoodsAvailableModel.COLUMN_GOODS_CARID);
            selection.append(" =? AND ");
            selection.append(GoodsAvailableModel.SEARCH_VCOLUMN_TYPE);
            selection.append(" =? AND ");
            selection.append(ListAbstractModel.VCOLUMN_START);
            selection.append(" =? AND ");
            selection.append(ListAbstractModel.VCOLUMN_PAGE_SIZE);
            selection.append(" =?");
            String[] selectionArgs = new String[] {
                    GlobalModel.getInst().mLoginModel.getCarId(), "0", Integer.toString(offset),
                    Integer.toString(mPageSize)
            };
            query(update, loadmore, DataProvider.getUri(GoodsAvailableModel.class, loadmore), null,
                    selection.toString(), selectionArgs, null);
        } else {
            StringBuffer sortOrder = new StringBuffer();
            sortOrder.append(AbstractModel.COLUMN_ID);
            sortOrder.append(" ASC");// 降序排列
            sortOrder.append(" LIMIT ");
            sortOrder.append(Integer.toString(mPageSize));
            sortOrder.append(" OFFSET ");
            sortOrder.append(offset);
            query(update, loadmore, DataProvider.getUri(GoodsAvailableModel.class, loadmore), PROJECTIONS, null, null,
                    sortOrder.toString());
        }
    }

    private String getGoodsType(int type) {
        String str = "";
        switch (type) {
            case Constants.GOODS_TYPE_NORMAL:
                str = mActivity.getResources().getString(R.string.myorders_order_item_goodstype_nolimit);
                break;
            case Constants.GOODS_TYPE_LIGHT_CARGO:
                str = mActivity.getResources().getString(R.string.myorders_order_item_goodstype_light);
                break;
            case Constants.GOODS_TYPE_HEAVY_CARGO:
                str = mActivity.getResources().getString(R.string.myorders_order_item_goodstype_heavy);
                break;
            case Constants.GOODS_TYPE_DEVICES:
                str = mActivity.getResources().getString(R.string.myorders_order_item_goodstype_device);
                break;
            default:
                str = mActivity.getResources().getString(R.string.myorders_order_item_goodstype_nolimit);
                break;
        }
        return str;
    }

    @Override
    protected void showEmptyPage(String text) {
    }

    @Override
    protected void showToast(int isid) {
    }

    @Override
    protected void refresh() {
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true, false);
        MobclickAgent.onPageStart(mActivity.getResources().getString(R.string.grabgoods_goods_available));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mActivity.getResources().getString(R.string.grabgoods_goods_available));
    }

}
