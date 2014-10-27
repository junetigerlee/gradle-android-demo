
package cn.com.incito.driver.fragments.goods;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.fragments.PageListAbstractFragment;
import cn.com.incito.driver.models.AbstractModel;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.goods.GoodsHasAvailableModel;
import cn.com.incito.driver.provider.DataProvider;
import cn.com.incito.driver.util.ParseUtil;
import cn.com.incito.wisdom.sdk.adapter.PageCursor;
import cn.com.incito.wisdom.sdk.adapter.PageCursorAdapter;
import cn.com.incito.wisdom.uicomp.widget.refreshable.RefreshableListView;
import cn.com.incito.wisdom.uicomp.widget.textview.MarqueeTextView;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 抢货源-已抢货源
 * 
 * @description 抢货源-已抢货源
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsHasAvailableFragment extends PageListAbstractFragment<ListView, GoodsHasAvailableModel.Response> {

    private DriverMainActivity mActivity;

    private LayoutInflater mInflater;

    private DriverMainActivity driverMainActivity;

    private static final String[] PROJECTIONS = new String[] {
            AbstractModel.COLUMN_ID, GoodsHasAvailableModel.COLUMN_GOODS_ORIGINALREGION,
            GoodsHasAvailableModel.COLUMN_GOODS_RECEIPTREGION, GoodsHasAvailableModel.COLUMN_GOODS_ORIGINALCITY,
            GoodsHasAvailableModel.COLUMN_GOODS_RECEIPTCITY, GoodsHasAvailableModel.COLUMN_GOODS_ORIGINALPROVINCE,
            GoodsHasAvailableModel.COLUMN_GOODS_RECEIPTPROVINCE, GoodsHasAvailableModel.COLUMN_GOODS_NAME,
            GoodsHasAvailableModel.COLUMN_GOODS_TYPE, GoodsHasAvailableModel.COLUMN_GOODS_FARE,
            GoodsHasAvailableModel.COLUMN_GOODS_INFOFARE, GoodsHasAvailableModel.COLUMN_GOODS_CARLENGTH,
            GoodsHasAvailableModel.COLUMN_GOODS_WEIGHT, GoodsHasAvailableModel.COLUMN_GOODS_VOLUME,
            GoodsHasAvailableModel.COLUMN_GOODS_COUNT, GoodsHasAvailableModel.COLUMN_GOODS_GENERATETIME,
            GoodsHasAvailableModel.COLUMN_GOODS_GOODSTIME, GoodsHasAvailableModel.COLUMN_GOODS_COMPLETETIME,
            GoodsHasAvailableModel.COLUMN_GOODS_GOODSID, GoodsHasAvailableModel.COLUMN_GOODS_CARID,
            GoodsHasAvailableModel.COLUMN_GOODS_GRABFLAG, GoodsHasAvailableModel.COLUMN_GOODS_GRABRESULT,
            GoodsHasAvailableModel.COLUMN_GOODS_ORDERID, GoodsHasAvailableModel.COLUMN_GOODS_FAILUREREASON,
            AbstractModel.COLUMN_CREATE_TIME, ListAbstractModel.VCOLUMN_MID
    };

    private static final int DEFUALT_ONE_PAGE_COUNT = 20;

    private String mStatus = "0";

    private RadioGroup mRadioGroupGoods;

    private GrabGoodsFragment grabGoodsFragment;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        driverMainActivity = (DriverMainActivity) getActivity();
        _RESULT_NO_DATA = "";
        mPageSize = DEFUALT_ONE_PAGE_COUNT;
        mInflater = inflater;
        mActivity = (DriverMainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_goods_has_available, null);

        inintView(view);
        return view;
    }

    public void inintView(View view) {

        grabGoodsFragment = (GrabGoodsFragment) getParentFragment();

        mRadioGroupGoods = (RadioGroup) view.findViewById(R.id.rg_goods);

        mRadioGroupGoods.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                switch (arg1) {
                    case R.id.rbtn_goods_all:
                        setStatus(Constants.GOODSGRAB_STATUS_ALL);
                        loadData(true, false);
                        break;
                    case R.id.rbtn_goods_success:
                        setStatus(Constants.GOODSGRAB_STATUS_SUCCESS);
                        loadData(true, false);
                        break;
                    case R.id.rbtn_goods_failure:
                        setStatus(Constants.GOODSGRAB_STATUS_FAUILURE);
                        loadData(true, false);
                        break;
                    case R.id.rbtn_goods_audit:
                        setStatus(Constants.GOODSGRAB_STATUS_AUDIT);
                        loadData(true, false);
                        break;
                    default:
                        break;
                }
            }
        });

        mPullToRefreshWidget = (RefreshableListView) view.findViewById(R.id.listView);
        mPullToRefreshWidget.setMode(mRefreshMode);
        mPullToRefreshWidget.setOnRefreshListener(this);
        ListView listView = mPullToRefreshWidget.getRefreshableView();
        mAdapter = new GoodsAvailableFragmentAdapter(getActivity(), null, false);
        listView.setAdapter(mAdapter);
        // set listview empty view
        View emptyView = mInflater.inflate(R.layout.empty_list, null);
        listView.setEmptyView(emptyView);
    }

    public void onEvent(final GoodsHasAvailableModel.Response resp) {
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
            View convertView = mInflater.inflate(R.layout.fragment_goods_has_available_list_item, null);
            ViewHolder.findAndCacheViews(convertView);
            return convertView;
        }

        public void bindView(View view, Context context, final PageCursor cursor, final int position) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.position = cursor.getPosition();

            viewHolder.route.setText(cursor.getString(cursor
                    .getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_ORIGINALPROVINCE))
                    + cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_ORIGINALCITY))
                    + cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_ORIGINALREGION))
                    + "→"
                    + cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_RECEIPTPROVINCE))
                    + cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_RECEIPTCITY))
                    + cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_RECEIPTREGION)));

            String fare = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_FARE));

            if (TextUtils.isEmpty(fare)) {
                viewHolder.fare.setText(mContext.getResources().getString(R.string.myorders_order_item_fare));
                viewHolder.price_tv.setVisibility(View.GONE);
            } else {
                viewHolder.fare.setText(fare);
            }
            String infofare = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_INFOFARE));
            if (TextUtils.isEmpty(infofare)) {
                viewHolder.infoFare.setText(mContext.getResources().getString(R.string.myorders_order_item_fare));
                viewHolder.price2_tv.setVisibility(View.GONE);
            } else {
                viewHolder.infoFare.setText(infofare);
            }

            viewHolder.goodsName.setText(cursor.getString(cursor
                    .getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_NAME)));

            String goodstype = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_TYPE));

            viewHolder.goodsType.setText(getGoodsType(Integer.parseInt(goodstype)));

            String weight = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_WEIGHT));
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
            String volume = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_VOLUME));

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

            String count = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_COUNT));

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

            viewHolder.generateTime.setText(String.format(mContext.getResources().getString(R.string.grabtime),
                    cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_COMPLETETIME))));
            String status = cursor.getString(cursor.getColumnIndex(GoodsHasAvailableModel.COLUMN_GOODS_GRABRESULT));

            if (status.equals(Constants.GOODSGRAB_RESULT_FAUILURE)) {
                viewHolder.failureReason.setVisibility(View.VISIBLE);
                viewHolder.listItem.setBackgroundResource(R.drawable.goodsgrab_failure_bg);
            } else if (status.equals(Constants.GOODSGRAB_RESULT_SUCCESS)) {
                viewHolder.failureReason.setVisibility(View.INVISIBLE);
                viewHolder.listItem.setBackgroundResource(R.drawable.goodsgrab_success_bg);
            } else if (status.equals(Constants.GOODSGRAB_RESULT_AUDIT)) {
                viewHolder.failureReason.setVisibility(View.INVISIBLE);
                viewHolder.listItem.setBackgroundResource(R.drawable.goodsgrab_audit_bg);
            }

        }

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();
            final PageCursor cursor = (PageCursor) mAdapter.getItem(position);

        }
    }

    private static class ViewHolder {
        public LinearLayout listItem;

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

        public TextView failureReason;

        int position;

        public static ViewHolder findAndCacheViews(View convertView) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.listItem = (LinearLayout) convertView.findViewById(R.id.list_item);
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
            viewHolder.failureReason = (TextView) convertView.findViewById(R.id.tv_failure_reason);
            convertView.setTag(viewHolder);
            return viewHolder;
        }
    }

    private void setStatus(String status) {
        mStatus = status;
    }

    @Override
    protected void loadData(boolean update, boolean loadmore) {
        Log.e(LOG_TAG, TAG + " loadData(" + update + ", " + loadmore + ")    ");
        int offset = loadmore ? mAdapter.getCount() : 0;
        if (update) {
            // queryTotal
            grabGoodsFragment.queryTotal();

            StringBuffer selection = new StringBuffer();
            selection.append(GoodsHasAvailableModel.COLUMN_GOODS_CARID);
            selection.append(" =? AND ");
            selection.append(GoodsHasAvailableModel.SEARCH_VCOLUMN_TYPE);
            selection.append(" =? AND ");
            selection.append(GoodsHasAvailableModel.SEARCH_VCOLUMN_STATUS);
            selection.append(" =? AND ");
            selection.append(ListAbstractModel.VCOLUMN_START);
            selection.append(" =? AND ");
            selection.append(ListAbstractModel.VCOLUMN_PAGE_SIZE);
            selection.append(" =?");
            String[] selectionArgs = new String[] {
                    GlobalModel.getInst().mLoginModel.getCarId(), "1", mStatus, Integer.toString(offset),
                    Integer.toString(mPageSize)
            };
            query(update, loadmore, DataProvider.getUri(GoodsHasAvailableModel.class, loadmore), null,
                    selection.toString(), selectionArgs, null);
        } else {
            StringBuffer sortOrder = new StringBuffer();
            sortOrder.append(AbstractModel.COLUMN_ID);
            sortOrder.append(" ASC");// 降序排列
            sortOrder.append(" LIMIT ");
            sortOrder.append(Integer.toString(mPageSize));
            sortOrder.append(" OFFSET ");
            sortOrder.append(offset);
            query(update, loadmore, DataProvider.getUri(GoodsHasAvailableModel.class, loadmore), PROJECTIONS, null,
                    null, sortOrder.toString());
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
        MobclickAgent.onPageStart(mActivity.getResources().getString(R.string.grabgoods_goods_grabed));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mActivity.getResources().getString(R.string.grabgoods_goods_grabed));
    }

}
