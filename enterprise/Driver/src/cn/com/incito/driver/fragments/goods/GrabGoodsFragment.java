
package cn.com.incito.driver.fragments.goods;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.net.apis.goods.GoodsGrabTotalAPI;
import cn.com.incito.driver.net.apis.goods.GoodsGrabTotalAPI.GoodsGrabTotalAPIResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomRadioGroup;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomRadioGroup.OnCheckedChangeListener;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * 抢货源
 * 
 * @description 抢货源
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GrabGoodsFragment extends Fragment {
    public static final String TAG = GrabGoodsFragment.class.getSimpleName();

    public Button mAvailableTotal, mHasAvailableTotal;// 总数

    private CustomRadioGroup mTabs;

    private RadioButton mRadioAvailable, mRadioHasAvailable;

    private Fragment mContent;

    private DriverMainActivity driverMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        driverMainActivity = (DriverMainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_goods_grab, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mAvailableTotal = (Button) view.findViewById(R.id.btn_available_total);
        mHasAvailableTotal = (Button) view.findViewById(R.id.btn_has_available_total);

        mTabs = (CustomRadioGroup) view.findViewById(R.id.crg_tabs);
        mRadioAvailable = (RadioButton) view.findViewById(R.id.rbtn_available);
        mRadioHasAvailable = (RadioButton) view.findViewById(R.id.rbtn_has_available);
        mTabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CustomRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_available:// 全部
                        switchContent(Constants.GRAB_TAG_GOODS_AVAILABLE);
                        break;
                    case R.id.rbtn_has_available:// 待接单
                        switchContent(Constants.GRAB_TAG_GOODS_HAS_AVAILABLE);
                        break;

                    default:
                        break;
                }
            }
        });

        view.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                driverMainActivity.toggleToMyOrder();
            }
        });
    }

    /**
     * 切换tab
     * 
     * @param tagName
     */
    protected void switchContent(String tagName) {
        int index = -1;
        for (int i = 0; i < Constants.GOODS_GRAB_TAGS.length; i++) {
            if (Constants.GOODS_GRAB_TAGS[i].equals(tagName)) {
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
        Fragment fragment = getChildFragmentManager().findFragmentByTag(Constants.GOODS_GRAB_TAGS[position]);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (mContent != null) {
            ft.detach(mContent);
        }
        if (fragment == null) {
            fragment = Fragment.instantiate(getActivity(), Constants.GOODS_GRAB_CLASSES[position].getName());
            ft.add(R.id.content_frame, fragment, Constants.GOODS_GRAB_TAGS[position]);
        } else {
            ft.attach(fragment);
        }
        mContent = fragment;
        ft.commit();
        getChildFragmentManager().executePendingTransactions();

    }

    /**
     * 查询可抢货源和已抢货源总数
     * 
     * @description 查询可抢货源和已抢货源总数
     * @author lizhan
     * @createDate 2014年10月15日
     */
    public void queryTotal() {
        if (NetworkUtils.isNetworkAvaliable(getActivity())) {

            GoodsGrabTotalAPI goodsGrabTotalAPI = new GoodsGrabTotalAPI(GlobalModel.getInst().mLoginModel.getCarId());
            new WisdomCityHttpResponseHandler(goodsGrabTotalAPI, new APIFinishCallback() {

                @Override
                public void OnRemoteApiFinish(BasicResponse response) {

                    if (response.status == BasicResponse.SUCCESS) {
                        GoodsGrabTotalAPIResponse goodsGrabTotalAPIResponse = (GoodsGrabTotalAPIResponse) response;

                        Constants.GOODGRAB_AVAILABLEGOODS_TOTAL = goodsGrabTotalAPIResponse.mGoodsGrabTotal
                                .getAvailableGoods();

                        mAvailableTotal.setText(goodsGrabTotalAPIResponse.mGoodsGrabTotal.getAvailableGoods());
                        mHasAvailableTotal.setText(goodsGrabTotalAPIResponse.mGoodsGrabTotal.getHasAvailableGoods());
                        Constants.GOODGRAB_SUCCGRABNUM = goodsGrabTotalAPIResponse.mGoodsGrabTotal.getSuccGrabNum();
                        Constants.GOODGRAB_FAILGRABNUM = goodsGrabTotalAPIResponse.mGoodsGrabTotal.getFailGrabNum();

                    } else {
                        mAvailableTotal.setText("0");
                        mHasAvailableTotal.setText("0");
                    }
                }
            });

            WisdomCityRestClient.execute(goodsGrabTotalAPI);
        } else {
            mAvailableTotal.setText("0");
            mHasAvailableTotal.setText("0");

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getResources().getString(R.string.grabgoods_goods_manage));
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getActivity().getIntent();
        String panelNo = intent.getStringExtra("panelNo");
        if (null != panelNo) {
            // 01 打开pad可抢货源页面 02 打开pad已抢货源页面
            if (panelNo.equals("01")) {
                switchContent(Constants.GRAB_TAG_GOODS_AVAILABLE);
                GoodsAvailableFragment goodsAvailableFragment = (GoodsAvailableFragment) mContent;
                goodsAvailableFragment.loadData(true, false);
                // queryTotal();
                mRadioAvailable.setChecked(true);
                intent.removeExtra("panelNo");
            } else if (panelNo.equals("02")) {
                switchContent(Constants.GRAB_TAG_GOODS_HAS_AVAILABLE);
                GoodsHasAvailableFragment hasAvailableFragment = (GoodsHasAvailableFragment) mContent;
                hasAvailableFragment.loadData(true, false);
                // queryTotal();
                mRadioHasAvailable.setChecked(true);
                intent.removeExtra("panelNo");
            }
        } else {

            mRadioAvailable.setChecked(true);

        }

        MobclickAgent.onPageStart(getActivity().getResources().getString(R.string.grabgoods_goods_manage));
    }
}
