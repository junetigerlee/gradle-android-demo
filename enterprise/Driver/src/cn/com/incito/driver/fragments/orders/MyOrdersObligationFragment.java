
package cn.com.incito.driver.fragments.orders;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.models.AbstractModel;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.orders.MyOrdersAllModel;
import cn.com.incito.driver.models.orders.MyOrdersObligationModel;
import cn.com.incito.driver.provider.DataProvider;

import android.util.Log;
import android.widget.ListView;

/**
 * 我的订单-待付款
 * 
 * @description 我的订单-待付款
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyOrdersObligationFragment extends AbstractMyOrderFragment<ListView, MyOrdersObligationModel.Response> {

    private static final String[] PROJECTIONS = new String[] {
            AbstractModel.COLUMN_ID, MyOrdersAllModel.COLUMN_ORDERS_ORDER, MyOrdersAllModel.COLUMN_ORDERS_GOODS,
            MyOrdersAllModel.COLUMN_ORDERS_CONSIGNE, MyOrdersAllModel.COLUMN_ORDERS_SHIPPER,
            MyOrdersAllModel.COLUMN_ORDERS_AGENT, AbstractModel.COLUMN_CREATE_TIME
    };

    @Override
    protected void loadData(boolean update, boolean loadmore) {

        Log.e(LOG_TAG, TAG + " loadData(" + update + ", " + loadmore + ")    ");
        int offset = loadmore ? mAdapter.getCount() : 0;
        if (update) {
            //
            fragment.queryTotal(fragment.mTimeRange);

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
                    Integer.toString(mPageSize), String.valueOf(Constants.MYORDER_TYPE_OBLIGATION),
                    String.valueOf(fragment.mTimeRange)
            };
            query(update, loadmore, DataProvider.getUri(MyOrdersObligationModel.class, loadmore), null,
                    selection.toString(), selectionArgs, null);
        } else {
            StringBuffer sortOrder = new StringBuffer();
            sortOrder.append(AbstractModel.COLUMN_ID);
            sortOrder.append(" ASC");// 降序排列
            sortOrder.append(" LIMIT ");
            sortOrder.append(Integer.toString(mPageSize));
            sortOrder.append(" OFFSET ");
            sortOrder.append(offset);
            query(update, loadmore, DataProvider.getUri(MyOrdersObligationModel.class, loadmore), PROJECTIONS, null,
                    null, sortOrder.toString());
        }

    }

    public void onEvent(final MyOrdersObligationModel.Response resp) {
        handleProviderResponse(resp);
    }
}
