
package cn.com.incito.driver.fragments.orders;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.R;
import cn.com.incito.driver.models.AbstractModel;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.orders.MyOrdersAllModel;
import cn.com.incito.driver.models.orders.MyOrdersReimburseModel;
import cn.com.incito.driver.provider.DataProvider;
import cn.com.incito.wisdom.uicomp.widget.refreshable.RefreshableListView;
import cn.com.incito.wisdom.uicomp.widget.slideexpandable.SlideExpandableListAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 我的订单-退款订单
 * 
 * @description
 * @author 我的订单-退款订单
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyOrdersReimburseFragment extends AbstractMyOrderFragment<ListView, MyOrdersReimburseModel.Response> {
    private static final int DEFUALT_ONE_PAGE_COUNT = 20;

    private static final String[] PROJECTIONS = new String[] {
            AbstractModel.COLUMN_ID, MyOrdersAllModel.COLUMN_ORDERS_ORDER, MyOrdersAllModel.COLUMN_ORDERS_GOODS,
            MyOrdersAllModel.COLUMN_ORDERS_CONSIGNE, MyOrdersAllModel.COLUMN_ORDERS_SHIPPER,
            MyOrdersAllModel.COLUMN_ORDERS_AGENT, AbstractModel.COLUMN_CREATE_TIME
    };

    /** 时间范围 */
    private int mTimeRange;

    private RadioGroup mTabs;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPageSize = DEFUALT_ONE_PAGE_COUNT;
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_my_orders_common, null);
        mTimeRange = Constants.ORDER_WITHIN_THREE_MONTHS;
        mPullToRefreshWidget = (RefreshableListView) view.findViewById(R.id.listView);
        mPullToRefreshWidget.setMode(mRefreshMode);
        mPullToRefreshWidget.setOnRefreshListener(this);
        ListView listView = mPullToRefreshWidget.getRefreshableView();
        mAdapter = new MyOrderAdapter(getActivity(), null, false);
        listView.setAdapter(new SlideExpandableListAdapter(mAdapter, R.id.expandable_toggle_button, R.id.expandable));
        // TODO
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

            }
        });
        mTabs = (RadioGroup) view.findViewById(R.id.rg_date_tabs);
        mTabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_in_three_months:
                        mTimeRange = Constants.ORDER_WITHIN_THREE_MONTHS;
                        break;
                    case R.id.rbtn_three_months_away:
                        mTimeRange = Constants.ORDER_THREE_MONTHS_AWAY;
                        break;
                    default:
                        break;
                }
                loadData(true, false);
            }
        });
        return view;
    }

    @Override
    protected void loadData(boolean update, boolean loadmore) {

        Log.e(LOG_TAG, TAG + " loadData(" + update + ", " + loadmore + ")    ");
        int offset = loadmore ? mAdapter.getCount() : 0;
        if (update) {
            //
            fragment.queryTotal(mTimeRange);

            StringBuffer selection = new StringBuffer();
            selection.append(ListAbstractModel.VCOLUMN_CAR_ID);
            selection.append(" =? AND ");
            selection.append(ListAbstractModel.VCOLUMN_START);
            selection.append(" =? AND ");
            selection.append(ListAbstractModel.VCOLUMN_PAGE_SIZE);
            selection.append(" =? AND ");
            selection.append(MyOrdersAllModel.COLUMN_ORDERS_STATUS);
            selection.append(" =? AND ");
            selection.append(MyOrdersAllModel.COLUMN_ORDERS_DATESTATUS);
            selection.append(" =?");
            String[] selectionArgs = new String[] {
                    GlobalModel.getInst().mLoginModel.getCarId(), Integer.toString(offset),
                    Integer.toString(mPageSize), String.valueOf(Constants.ORDER_TYPE_REIMBURSE),
                    String.valueOf(mTimeRange)
            };
            query(update, loadmore, DataProvider.getUri(MyOrdersReimburseModel.class, loadmore), null,
                    selection.toString(), selectionArgs, null);
        } else {
            StringBuffer sortOrder = new StringBuffer();
            sortOrder.append(AbstractModel.COLUMN_ID);
            sortOrder.append(" ASC");// 降序排列
            sortOrder.append(" LIMIT ");
            sortOrder.append(Integer.toString(mPageSize));
            sortOrder.append(" OFFSET ");
            sortOrder.append(offset);
            query(update, loadmore, DataProvider.getUri(MyOrdersReimburseModel.class, loadmore), PROJECTIONS, null,
                    null, sortOrder.toString());
        }

    }

    public void onEvent(final MyOrdersReimburseModel.Response resp) {
        handleProviderResponse(resp);
    }

    @Override
    protected void showEmptyPage(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void showToast(int isid) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void refresh() {
        // TODO Auto-generated method stub

    }
}
