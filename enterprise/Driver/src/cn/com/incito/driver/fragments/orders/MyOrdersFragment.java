
package cn.com.incito.driver.fragments.orders;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.net.apis.goods.GoodsGrabTotalAPI;
import cn.com.incito.driver.net.apis.goods.GoodsGrabTotalAPI.GoodsGrabTotalAPIResponse;
import cn.com.incito.driver.net.apis.orders.MyOrdersTotalAPI;
import cn.com.incito.driver.net.apis.orders.MyOrdersTotalAPI.MyOrdersTotalAPIResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomRadioGroup;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomRadioGroup.OnCheckedChangeListener;
import cn.com.incito.wisdom.uicomp.widget.textview.BadgeView;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 我的订单
 * 
 * @description 我的订单_新
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyOrdersFragment extends Fragment {
    public static final String TAG = "MyOrdersFragment";

    public Button mAllOrderTotal, mPayOrderTotal, mPickingOrderTotal, mEvelOrderTotal, mCancelOrderTotal,
            mSignOrderTotal, mReceiveOrderTotal;// 总数

    private CustomRadioGroup mTabs;

    private RadioButton mRadioAll, mRadioObligation, mRadioDistribution, mRadioSign, mRadioEvaluated, mRadioCanceled;

    private Fragment mContent;

    private DriverMainActivity driverMainActivity;

    private BadgeView badge;

    public RadioGroup mTimeRangeTabs;

    private RadioButton mInsideThreeMonths;

    public int mTimeRange = Constants.ORDER_WITHIN_THREE_MONTHS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        driverMainActivity = (DriverMainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_my_orders, null);
        initViews(view);

        View target = view.findViewById(R.id.tv_grab_goods);
        badge = new BadgeView(getActivity(), target);
        badge.setBackgroundResource(R.drawable.goodsgrab_red_icon);
        // badge.setText("");
        // badge.show();

        view.findViewById(R.id.tv_grab_goods).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 切换到抢货源界面
                driverMainActivity.toggleToGrabGoods();
            }
        });

        return view;
    }

    /**
     * initViews
     * 
     * @description initViews
     * @author lizhan
     * @createDate 2014年10月15日
     * @param view
     */
    private void initViews(View view) {
        mAllOrderTotal = (Button) view.findViewById(R.id.btn_all_total);
        mCancelOrderTotal = (Button) view.findViewById(R.id.btn_cancelorder_total);
        mEvelOrderTotal = (Button) view.findViewById(R.id.btn_evelorder_total);
        mSignOrderTotal = (Button) view.findViewById(R.id.btn_signorder_total);
        mPickingOrderTotal = (Button) view.findViewById(R.id.btn_pickingorder_total);
        mPayOrderTotal = (Button) view.findViewById(R.id.btn_payorder_total);
        mReceiveOrderTotal = (Button) view.findViewById(R.id.btn_received_total);
        mTabs = (CustomRadioGroup) view.findViewById(R.id.crg_tabs);
        mRadioAll = (RadioButton) view.findViewById(R.id.rbtn_all);
        mRadioObligation = (RadioButton) view.findViewById(R.id.rbtn_obligation);
        mRadioDistribution = (RadioButton) view.findViewById(R.id.rbtn_distribution);
        mRadioSign = (RadioButton) view.findViewById(R.id.rbtn_sign);
        mRadioEvaluated = (RadioButton) view.findViewById(R.id.rbtn_evaluated);
        mRadioCanceled = (RadioButton) view.findViewById(R.id.rbtn_canceled);
        mTabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CustomRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_all:// 全部
                        switchContent(Constants.TAG_ORDERS_ALL);
                        break;
                    case R.id.rbtn_received:// 待接单
                        switchContent(Constants.TAG_ORDERS_RECEIVED);
                        break;
                    case R.id.rbtn_obligation:// 待付款
                        switchContent(Constants.TAG_ORDERS_OBLIGATION);
                        break;
                    case R.id.rbtn_distribution:// 待配货
                        switchContent(Constants.TAG_ORDERS_DISTRIBUTION);
                        break;
                    case R.id.rbtn_sign:// 待签收
                        switchContent(Constants.TAG_ORDERS_SIGN);
                        break;
                    case R.id.rbtn_evaluated:// 待评价
                        switchContent(Constants.TAG_ORDERS_EVALUATED);
                        break;
                    case R.id.rbtn_canceled:// 已取消
                        switchContent(Constants.TAG_ORDERS_CANCELED);
                        break;
                    // case R.id.radio_reimburse:// 退款订单
                    // switchContent(Constants.TAG_ORDERS_REIMBURSE);
                    // break;

                    default:
                        break;
                }
            }
        });

        mTimeRangeTabs = (RadioGroup) view.findViewById(R.id.rg_date_tabs);
        mTimeRangeTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_in_three_months:
                        mTimeRange = Constants.ORDER_WITHIN_THREE_MONTHS;
                        AbstractMyOrderFragment myOrderFragment = (AbstractMyOrderFragment) mContent;
                        myOrderFragment.loadData(true, false);
                        break;
                    case R.id.rbtn_three_months_away:
                        mTimeRange = Constants.ORDER_THREE_MONTHS_AWAY;
                        AbstractMyOrderFragment myOrderFragment2 = (AbstractMyOrderFragment) mContent;
                        myOrderFragment2.loadData(true, false);
                        break;
                    default:
                        break;
                }

            }
        });

        mInsideThreeMonths = (RadioButton) view.findViewById(R.id.rbtn_in_three_months);
    }

    /**
     * 切换tab
     * 
     * @param tagName
     */
    protected void switchContent(String tagName) {
        int index = -1;
        for (int i = 0; i < Constants.MYORDERS_TAGS.length; i++) {
            if (Constants.MYORDERS_TAGS[i].equals(tagName)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return;
        }
        switchContent(index, false);
    }

    private void switchContent(int position, boolean isMain) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(Constants.MYORDERS_TAGS[position]);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (mContent != null) {
            ft.detach(mContent);
        }
        if (fragment == null) {
            fragment = Fragment.instantiate(getActivity(), Constants.MYORDERS_CLASSES[position].getName());
            ft.add(R.id.content_frame, fragment, Constants.MYORDERS_TAGS[position]);
        } else {
            ft.attach(fragment);
        }
        mContent = fragment;
        ft.commit();
        getChildFragmentManager().executePendingTransactions();

    }

    private Handler mcheckReddotHandler = new Handler();

    private Runnable mcheckReddotRunnable = new Runnable() {

        @Override
        public void run() {
            this.refresh();
            mcheckReddotHandler.postDelayed(this, 1000 * 5);// 定时刷新，间隔x秒
        }

        public void refresh() {
            checkReddot();
        }
    };

    /**
     * 检查是否有数据更新
     * 
     * @description 检查是否有数据更新,小红点显示
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void checkReddot() {
        if (NetworkUtils.isNetworkAvaliable(getActivity())) {
            GoodsGrabTotalAPI goodsGrabTotalAPI = new GoodsGrabTotalAPI(GlobalModel.getInst().mLoginModel.getCarId());
            new WisdomCityHttpResponseHandler(goodsGrabTotalAPI, new APIFinishCallback() {

                @Override
                public void OnRemoteApiFinish(BasicResponse response) {

                    if (response.status == BasicResponse.SUCCESS) {
                        GoodsGrabTotalAPIResponse goodsGrabTotalAPIResponse = (GoodsGrabTotalAPIResponse) response;

                        // 小红点显示的条件，有可抢资源，或已抢资源状态发生变更
                        if (Integer.parseInt(goodsGrabTotalAPIResponse.mGoodsGrabTotal.getAvailableGoods()) > Integer
                                .parseInt(Constants.GOODGRAB_AVAILABLEGOODS_TOTAL)
                                || Integer.parseInt(goodsGrabTotalAPIResponse.mGoodsGrabTotal.getSuccGrabNum()) > Integer
                                        .parseInt(Constants.GOODGRAB_SUCCGRABNUM)
                                || Integer.parseInt(goodsGrabTotalAPIResponse.mGoodsGrabTotal.getFailGrabNum()) > Integer
                                        .parseInt(Constants.GOODGRAB_FAILGRABNUM)) {
                            badge.show();
                        } else {
                            badge.hide();
                        }

                    }
                }
            });

            WisdomCityRestClient.execute(goodsGrabTotalAPI);

        }
    }

    /**
     * queryTotal
     * 
     * @description queryTotal
     * @author lizhan
     * @createDate 2014年10月15日
     * @param dataStatus
     */
    public void queryTotal(int dataStatus) {
        if (NetworkUtils.isNetworkAvaliable(getActivity())) {

            MyOrdersTotalAPI myOrdersTotalAPI = new MyOrdersTotalAPI(GlobalModel.getInst().mLoginModel.getCarId(),
                    dataStatus, 0);
            new WisdomCityHttpResponseHandler(myOrdersTotalAPI, new APIFinishCallback() {

                @Override
                public void OnRemoteApiFinish(BasicResponse response) {

                    if (response.status == BasicResponse.SUCCESS) {
                        MyOrdersTotalAPI.MyOrdersTotalAPIResponse myGoodsTotalAPIResponse = (MyOrdersTotalAPIResponse) response;
                        mAllOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getAllorder());
                        mCancelOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getCancelorder());
                        mEvelOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getEvelorder());
                        mPickingOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getPickingorder());
                        mPayOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getPayorder());
                        mSignOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getSignorder());

                        mReceiveOrderTotal.setText(myGoodsTotalAPIResponse.mMyOrdersTotal.getReceiveorder());
                    } else {
                        mAllOrderTotal.setText("0");
                        mCancelOrderTotal.setText("0");
                        mEvelOrderTotal.setText("0");
                        mPickingOrderTotal.setText("0");
                        mPayOrderTotal.setText("0");
                        mSignOrderTotal.setText("0");
                        mReceiveOrderTotal.setText("0");
                    }
                }
            });

            WisdomCityRestClient.execute(myOrdersTotalAPI);
        } else {
            mAllOrderTotal.setText("0");
            mCancelOrderTotal.setText("0");
            mEvelOrderTotal.setText("0");
            mPickingOrderTotal.setText("0");
            mPayOrderTotal.setText("0");
            mSignOrderTotal.setText("0");
            mReceiveOrderTotal.setText("0");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.myorders_on_page_end));
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (driverMainActivity.order_status) {
            case 0:// 全部
                mRadioAll.setChecked(true);
                break;
            case 1:// 待付款
                mRadioObligation.setChecked(true);
                break;
            case 2:// 待配货
                mRadioDistribution.setChecked(true);
                break;
            case 3:// 待签收
                mRadioSign.setChecked(true);
                break;
            case 4:// 待评价
                mRadioEvaluated.setChecked(true);
                break;
            case 5:// 已取消
                mRadioCanceled.setChecked(true);
                break;

            default:
                break;
        }

        // checkReddot
        mcheckReddotHandler.postDelayed(mcheckReddotRunnable, 1000 * 5);

        MobclickAgent.onPageStart(getResources().getString(R.string.myorders_on_page_start));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContent.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remove checkReddot
        mcheckReddotHandler.removeCallbacks(mcheckReddotRunnable);
    }

}
