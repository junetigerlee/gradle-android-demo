
package cn.com.incito.driver.fragments.orders;

import cn.com.incito.driver.BNavigatorActivity;
import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverApplication;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.UI.detailDialog.AgentStarLevelDialog;
import cn.com.incito.driver.UI.detailDialog.ComfirmDialog;
import cn.com.incito.driver.UI.detailDialog.EvaluateDialog;
import cn.com.incito.driver.UI.detailDialog.OrderSignDialog;
import cn.com.incito.driver.UI.detailDialog.PaymentOrderDialog;
import cn.com.incito.driver.UI.detailDialog.RefuseOrderDialog;
import cn.com.incito.driver.UI.detailDialog.SignInfoDialog;
import cn.com.incito.driver.fragments.PageListAbstractFragment;
import cn.com.incito.driver.listeners.ComfirmCallback;
import cn.com.incito.driver.listeners.OperateCallback;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ModelAgent;
import cn.com.incito.driver.models.ModelGoods;
import cn.com.incito.driver.models.ModelOrder;
import cn.com.incito.driver.models.ModelResponse;
import cn.com.incito.driver.models.ModelShipper;
import cn.com.incito.driver.models.orders.MyOrdersAllModel;
import cn.com.incito.driver.net.apis.CanvassingOrderAPI;
import cn.com.incito.driver.net.apis.CreditLevelAPI;
import cn.com.incito.driver.net.apis.CreditLevelAPI.CreditLevelAPIResponse;
import cn.com.incito.driver.net.apis.WisdomCityRestClientParameterImpl;
import cn.com.incito.driver.net.apis.orders.DeleteOrderAPI;
import cn.com.incito.driver.net.apis.orders.GetSignInfoAPI;
import cn.com.incito.driver.net.apis.orders.OrderCommentDetailAPI;
import cn.com.incito.driver.net.apis.orders.OrderCommentDetailAPI.OrderCommentDetailAPIResponse;
import cn.com.incito.driver.net.apis.orders.ReceiveOrderAPI;
import cn.com.incito.driver.net.apis.orders.RefuseOrderAPI;
import cn.com.incito.driver.util.GetResourcesUtil;
import cn.com.incito.driver.util.ParseUtil;
import cn.com.incito.wisdom.sdk.adapter.PageCursor;
import cn.com.incito.wisdom.sdk.adapter.PageCursorAdapter;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.assist.FailReason;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.IntentUtils;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.refreshable.RefreshableListView;
import cn.com.incito.wisdom.uicomp.widget.slideexpandable.SlideExpandableListWithIconAdapter;
import cn.com.incito.wisdom.uicomp.widget.textview.MarqueeTextView;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

/**
 * 我的订单的 抽象类
 * 
 * @description 我的订单的 抽象类
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public abstract class AbstractMyOrderFragment<T extends View, U extends ModelResponse> extends
        PageListAbstractFragment<ListView, ModelResponse> {
    protected MyOrdersFragment fragment;

    private DriverMainActivity driverMainActivity;

    private DriverApplication driverApplication;

    public int mTimeRange;

    public RadioGroup mTabs;

    // private RadioButton mInside,mOutside;
    private static final int DEFUALT_ONE_PAGE_COUNT = 20;

    /**
     * 星级评价 starimg 星级图片 credit 整体得分 creditcount 成交笔数
     */
    private String starimg = "";

    private String credit = "";

    private String creditcount = "";

    public LayoutInflater mInflater;

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _RESULT_NO_DATA = "";
        mPageSize = DEFUALT_ONE_PAGE_COUNT;
        mInflater = inflater;
        mContext = getActivity();
        fragment = (MyOrdersFragment) getParentFragment();
        View view = inflater.inflate(R.layout.fragment_my_orders_common, null);
        mTimeRange = Constants.ORDER_WITHIN_THREE_MONTHS;
        mPullToRefreshWidget = (RefreshableListView) view.findViewById(R.id.listView);
        mPullToRefreshWidget.setMode(mRefreshMode);
        mPullToRefreshWidget.setOnRefreshListener(this);
        ListView listView = mPullToRefreshWidget.getRefreshableView();
        mAdapter = new MyOrderAdapter(getActivity(), null, false);
        // listView.setAdapter(new SlideExpandableListAdapter(mAdapter,
        // R.id.expandable_toggle_button, R.id.expandable));

        listView.setAdapter(new SlideExpandableListWithIconAdapter(mAdapter, R.id.expandable_toggle_button,
                R.id.expandable, R.id.image_expand, R.drawable.item_expanded_close, R.drawable.item_expanded_open));

        // TODO
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

            }
        });

        // set listview empty view
        View emptyView = mInflater.inflate(R.layout.empty_list, null);
        listView.setEmptyView(emptyView);
        // if (null != mTabs) {
        // mTabs.setOnCheckedChangeListener(null);
        // }
        // mTabs = (RadioGroup) view.findViewById(R.id.radiobutton_date_group);
        // mTabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(RadioGroup group, int checkedId) {
        // switch (checkedId) {
        // case R.id.in_three_months:
        // mTimeRange = Constants.ORDER_WITHIN_THREE_MONTHS;
        // // fragment.queryTotal(mTimeRange);
        // loadData(true, false);
        // break;
        // case R.id.three_months_away:
        // mTimeRange = Constants.ORDER_THREE_MONTHS_AWAY;
        // // fragment.queryTotal(mTimeRange);
        // loadData(true, false);
        // break;
        // default:
        // break;
        // }
        //
        // }
        // });

        // Button target = (Button) view.findViewById(R.id.do_goods);
        // final BadgeView badge = new BadgeView(mContext, target);
        // badge.setText("1");
        // badge.show();
        return view;
    }

    /**
     * 我的订单-列表适配器
     * 
     * @author qiujiaheng 2014年3月5日
     */
    public class MyOrderAdapter extends PageCursorAdapter implements OnClickListener {
        private LayoutInflater mInflater;

        public MyOrderAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public void bindView(View view, Context context, PageCursor cursor, int position) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();

            /******** init start *************/
            viewHolder.listItem.setBackgroundResource(R.drawable.list_item_bg);

            // viewHolder.weight.setVisibility(View.VISIBLE);
            // viewHolder.textWeight.setVisibility(View.VISIBLE);
            //
            // viewHolder.volume.setVisibility(View.VISIBLE);
            // viewHolder.textVolume.setVisibility(View.VISIBLE);
            //
            // viewHolder.count.setVisibility(View.VISIBLE);
            // viewHolder.textCount.setVisibility(View.VISIBLE);

            viewHolder.fareLable.setVisibility(View.VISIBLE);
            viewHolder.fare.setVisibility(View.VISIBLE);
            viewHolder.price_tv.setVisibility(View.VISIBLE);
            viewHolder.infoFareLable.setVisibility(View.VISIBLE);
            viewHolder.infoFare.setVisibility(View.VISIBLE);
            viewHolder.price2_tv.setVisibility(View.VISIBLE);
            // 回单要求
            viewHolder.backstatus.setVisibility(View.VISIBLE);
            // 结算方式
            viewHolder.drawback.setVisibility(View.VISIBLE);
            // 声明价值
            viewHolder.goodsDeclaredValue.setVisibility(View.VISIBLE);
            // 货代信息
            viewHolder.agentInfo.setVisibility(View.VISIBLE);

            /******** init end *************/

            viewHolder.position = cursor.getPosition();
            String orderJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_ORDER));
            String goodsJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_GOODS));
            String consigneJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_CONSIGNE));
            String shipperJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_SHIPPER));
            String agentJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_AGENT));
            Gson gson = new Gson();
            ModelOrder modelOrder = null;
            ModelGoods modelGoods = null;
            ModelShipper modelShipper = null;
            ModelShipper modelConsigne = null;
            ModelAgent modelAgent = null;

            if (null != orderJson) {// 订单对象不为null
                modelOrder = gson.fromJson(orderJson, ModelOrder.class);
                viewHolder.orderno.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_order_no),
                        modelOrder.getOrderno()));
                viewHolder.generateTime.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_order_generatetime),
                        modelOrder.getGeneratetime()));
                setOrderStatus(viewHolder, modelOrder);

                switch (Integer.parseInt(modelOrder.getGoodscustom())) {
                    case Constants.GOODS_CUSTOM_PUBLIC:// 公共
                        viewHolder.listItem.setBackgroundResource(R.drawable.list_item_bg);
                        break;
                    case Constants.GOODS_CUSTOM_PRIVATE:// 自有
                        viewHolder.listItem.setBackgroundResource(R.drawable.list_item_blue_bg);
                        break;
                    default:
                        break;
                }

            }
            if (null != goodsJson) {// 货源对象
                modelGoods = gson.fromJson(goodsJson, ModelGoods.class);

                viewHolder.route.setText(modelGoods.getOriginalprovince() + modelGoods.getOriginalcity()
                        + modelGoods.getOriginalregion() + "→" + modelGoods.getReceiptprovince()
                        + modelGoods.getReceiptcity() + modelGoods.getReceiptregion());

                // viewHolder.shipProvince.setText(modelGoods
                // .getOriginalprovince());
                // viewHolder.shipCity.setText(modelGoods.getOriginalcity());
                // viewHolder.shipCounty.setText(modelGoods.getOriginalregion());
                // viewHolder.receiptProvince.setText(modelGoods
                // .getReceiptprovince());
                // viewHolder.receiptCity.setText(modelGoods.getReceiptcity());
                // viewHolder.receiptCounty.setText(modelGoods.getReceiptregion());
                if (!TextUtils.isEmpty(modelOrder.getWeight())) {
                    viewHolder.weight
                            .setText(ParseUtil.parseDoubleToString(Double.parseDouble(modelOrder.getWeight())));
                    viewHolder.weight.setVisibility(View.VISIBLE);
                    viewHolder.textWeight.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.weight.setVisibility(View.GONE);
                    viewHolder.textWeight.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(modelOrder.getVolume())) {
                    viewHolder.volume
                            .setText(ParseUtil.parseDoubleToString(Double.parseDouble(modelOrder.getVolume())));
                    viewHolder.volume.setVisibility(View.VISIBLE);
                    viewHolder.textVolume.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.volume.setVisibility(View.GONE);
                    viewHolder.textVolume.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(modelGoods.getCount())) {
                    viewHolder.count.setText(modelGoods.getCount());
                    viewHolder.count.setVisibility(View.VISIBLE);
                    viewHolder.textCount.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.count.setVisibility(View.GONE);
                    viewHolder.textCount.setVisibility(View.GONE);
                }

                // if (TextUtils.isEmpty(modelGoods.getFare())) {
                // viewHolder.fare.setText("面议");
                // viewHolder.price_tv.setVisibility(View.GONE);
                // } else {
                // viewHolder.fare.setText(modelGoods.getFare());
                // }
                // if (TextUtils.isEmpty(modelGoods.getInfofare())) {
                // viewHolder.infoFare.setText("面议");
                // viewHolder.price2_tv.setVisibility(View.GONE);
                // } else {
                // viewHolder.infoFare.setText(modelGoods.getInfofare());
                // }

                switch (Integer.parseInt(modelOrder.getGoodscustom())) {
                    case Constants.GOODS_CUSTOM_PUBLIC:// 公共
                        if (TextUtils.isEmpty(modelGoods.getFare())) {
                            viewHolder.fare.setText(mContext.getResources()
                                    .getString(R.string.myorders_order_item_fare));
                            viewHolder.price_tv.setVisibility(View.GONE);
                        } else {
                            viewHolder.fare.setText(modelGoods.getFare());
                        }
                        if (TextUtils.isEmpty(modelGoods.getInfofare())) {
                            viewHolder.infoFare.setText(mContext.getResources().getString(
                                    R.string.myorders_order_item_fare));
                            viewHolder.price2_tv.setVisibility(View.GONE);
                        } else {
                            viewHolder.infoFare.setText(modelGoods.getInfofare());
                        }
                        // 回单要求
                        if (!TextUtils.isEmpty(modelGoods.getBackstatus())) {
                            viewHolder.backstatus.setText(mContext.getResources().getString(
                                    R.string.myorders_order_item_backstatus)
                                    + setDrawBack(Integer.parseInt(modelGoods.getBackstatus())));
                        }
                        // 结算方式
                        if (!TextUtils.isEmpty(modelGoods.getPaystatus())) {
                            viewHolder.drawback.setText(mContext.getResources().getString(
                                    R.string.myorders_order_item_paystatus)
                                    + setPayStatus(Integer.parseInt(modelGoods.getPaystatus())));
                            if (Integer.parseInt(modelGoods.getPaystatus()) == 0) {// 如果结算方式为货到付款，无回单方式
                                viewHolder.backstatus.setVisibility(View.INVISIBLE);
                            } else {
                                viewHolder.backstatus.setVisibility(View.VISIBLE);
                            }
                        }
                        // 声明价值
                        if (!TextUtils.isEmpty(modelGoods.getDeclaredvalue())) {
                            viewHolder.goodsDeclaredValue.setText(String.format(
                                    mContext.getResources().getString(
                                            R.string.myorders_list_item_detail_goods_declaredvalue),
                                    modelGoods.getDeclaredvalue()));
                        } else {
                            viewHolder.goodsDeclaredValue.setText(String.format(
                                    mContext.getResources().getString(
                                            R.string.myorders_list_item_detail_goods_declaredvalue), mContext
                                            .getResources().getString(R.string.myorders_order_item_empty)));
                        }

                        break;
                    case Constants.GOODS_CUSTOM_PRIVATE:// 自有
                        viewHolder.fareLable.setVisibility(View.GONE);
                        viewHolder.fare.setVisibility(View.GONE);
                        viewHolder.price_tv.setVisibility(View.GONE);

                        viewHolder.infoFareLable.setVisibility(View.GONE);
                        viewHolder.infoFare.setVisibility(View.GONE);
                        viewHolder.price2_tv.setVisibility(View.GONE);

                        // 回单要求
                        viewHolder.backstatus.setVisibility(View.GONE);
                        // 结算方式
                        viewHolder.drawback.setVisibility(View.GONE);
                        // 声明价值
                        viewHolder.goodsDeclaredValue.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

                viewHolder.memo.setText(Html.fromHtml(modelGoods.getMemo()).toString());
                viewHolder.goodsNo.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_goods_no),
                        modelGoods.getGoodsno()));
                if (modelGoods.getGoodstype() != null) {
                    switch (Integer.parseInt(modelGoods.getGoodstype())) {
                        case Constants.GOODS_TYPE_NORMAL:
                            viewHolder.goodsType.setText(String.format(
                                    mContext.getResources().getString(R.string.myorders_list_item_detail_goods_type),
                                    mContext.getResources().getString(R.string.myorders_order_item_goodstype_nolimit)));
                            break;
                        case Constants.GOODS_TYPE_HEAVY_CARGO:
                            viewHolder.goodsType.setText(String.format(
                                    mContext.getResources().getString(R.string.myorders_list_item_detail_goods_type),
                                    mContext.getResources().getString(R.string.myorders_order_item_goodstype_heavy)));
                            break;
                        case Constants.GOODS_TYPE_LIGHT_CARGO:
                            viewHolder.goodsType.setText(String.format(
                                    mContext.getResources().getString(R.string.myorders_list_item_detail_goods_type),
                                    mContext.getResources().getString(R.string.myorders_order_item_goodstype_light)));
                            break;
                        case Constants.GOODS_TYPE_DEVICES:
                            viewHolder.goodsType.setText(String.format(
                                    mContext.getResources().getString(R.string.myorders_list_item_detail_goods_type),
                                    mContext.getResources().getString(R.string.myorders_order_item_goodstype_device)));
                            break;
                        default:
                            break;
                    }
                }
                viewHolder.goodsName.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_goods_name),
                        modelGoods.getGoodsname()));
                if (!TextUtils.isEmpty(modelGoods.getVolume())) {
                    viewHolder.goodsVolume.setText(String.format(
                            mContext.getResources().getString(R.string.myorders_list_item_detail_goods_volume),
                            ParseUtil.parseDoubleToString(Double.parseDouble(modelGoods.getVolume()))));
                } else {
                    viewHolder.goodsVolume.setText(String.format(
                            mContext.getResources().getString(R.string.myorders_list_item_detail_goods_volume),
                            mContext.getResources().getString(R.string.myorders_order_item_empty)));
                }
                if (!TextUtils.isEmpty(modelGoods.getCount())) {
                    viewHolder.goodsCount.setText(String.format(
                            mContext.getResources().getString(R.string.myorders_list_item_detail_goods_count),
                            modelGoods.getCount()));
                } else {
                    viewHolder.goodsCount.setText(String.format(
                            mContext.getResources().getString(R.string.myorders_list_item_detail_goods_count), mContext
                                    .getResources().getString(R.string.myorders_order_item_empty)));
                }

            }
            if (null != consigneJson) {// 收货人对象
                modelConsigne = gson.fromJson(consigneJson, ModelShipper.class);
                viewHolder.consigneLayout.setVisibility(View.VISIBLE);
                viewHolder.consigneName.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_name),
                        modelConsigne.getName()));
                viewHolder.consigneTel.setText(modelConsigne.getMobile());
                viewHolder.consigneTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                viewHolder.consigneTel.setClickable(true);
                viewHolder.consigneTel.setFocusable(false);
                viewHolder.consigneTel.setFocusableInTouchMode(false);
                viewHolder.consigneTel.setTag(viewHolder.position);
                viewHolder.consigneTel.setOnClickListener(this);
                viewHolder.consigneCompany.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_company),
                        modelConsigne.getCompany()));
                viewHolder.consigneAddress.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_address),
                        (modelConsigne.getProvince() == null ? "" : modelConsigne.getProvince())
                                + (modelConsigne.getCity() == null ? "" : modelConsigne.getCity())
                                + (modelConsigne.getCountry() == null ? "" : modelConsigne.getCountry())
                                + modelConsigne.getAddress()));
            } else {
                viewHolder.consigneLayout.setVisibility(View.GONE);
            }
            if (null != shipperJson) {// 发货人对象
                modelShipper = gson.fromJson(shipperJson, ModelShipper.class);
                viewHolder.shipperLayout.setVisibility(View.VISIBLE);
                viewHolder.shipperName.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_name),
                        modelShipper.getName()));
                viewHolder.shipperTel.setText(modelShipper.getMobile());
                viewHolder.shipperTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                viewHolder.shipperTel.setClickable(true);
                viewHolder.shipperTel.setFocusable(false);
                viewHolder.shipperTel.setFocusableInTouchMode(false);
                viewHolder.shipperTel.setTag(viewHolder.position);
                viewHolder.shipperTel.setOnClickListener(this);
                viewHolder.shipperCompany.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_company),
                        modelShipper.getCompany()));
                viewHolder.shipperAddress.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_address),
                        (modelShipper.getProvince() == null ? "" : modelShipper.getProvince())
                                + (modelShipper.getCity() == null ? "" : modelShipper.getCity())
                                + (modelShipper.getCountry() == null ? "" : modelShipper.getCountry())
                                + modelShipper.getAddress()));
            } else {
                viewHolder.shipperLayout.setVisibility(View.GONE);
            }

            switch (Integer.parseInt(modelOrder.getGoodscustom())) {
                case Constants.GOODS_CUSTOM_PUBLIC:// 公共
                    if (null != agentJson) {// 货代对象
                        modelAgent = gson.fromJson(agentJson, ModelAgent.class);
                        ImageLoader.getInstance().displayImage(
                                WisdomCityRestClientParameterImpl.getUrl() + modelAgent.getPhoto(),
                                viewHolder.agentPhoto, new ImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        viewHolder.agentPhoto.setBackgroundResource(R.drawable.headportrait);
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
                        viewHolder.agentNo.setText(modelAgent.getAgentno());
                        viewHolder.agentCompany.setText(modelAgent.getCompany());
                        viewHolder.agentAdd.setText(modelAgent.getAddress());
                        viewHolder.agentName.setText(modelAgent.getName());
                        viewHolder.agentTel.setText(modelAgent.getTel());
                        viewHolder.agentTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
                        viewHolder.starImage.setImageResource(GetResourcesUtil.getDrawableIdentifier(mContext,
                                modelAgent.getStarImg()));
                        viewHolder.starImage.setClickable(true);
                        viewHolder.starImage.setFocusable(false);
                        viewHolder.starImage.setFocusableInTouchMode(false);
                        viewHolder.starImage.setTag(viewHolder.position);
                        viewHolder.starImage.setOnClickListener(this);

                    }
                    viewHolder.agentTel.setClickable(true);
                    viewHolder.agentTel.setFocusable(false);
                    viewHolder.agentTel.setFocusableInTouchMode(false);
                    viewHolder.agentTel.setTag(viewHolder.position);
                    viewHolder.agentTel.setOnClickListener(this);

                    break;
                case Constants.GOODS_CUSTOM_PRIVATE:// 自有
                    viewHolder.agentInfo.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }

            viewHolder.localAgentBtn.setFocusable(false);
            viewHolder.localAgentBtn.setFocusableInTouchMode(false);
            viewHolder.localAgentBtn.setTag(viewHolder.position);
            viewHolder.localAgentBtn.setOnClickListener(this);

            viewHolder.localOwnerBtn.setFocusable(false);
            viewHolder.localOwnerBtn.setFocusableInTouchMode(false);
            viewHolder.localOwnerBtn.setTag(viewHolder.position);
            viewHolder.localOwnerBtn.setOnClickListener(this);

            viewHolder.payBtn.setFocusable(false);
            viewHolder.payBtn.setFocusableInTouchMode(false);
            viewHolder.payBtn.setTag(viewHolder.position);
            viewHolder.payBtn.setOnClickListener(this);

            viewHolder.confirmAllocationBtn.setFocusable(false);
            viewHolder.confirmAllocationBtn.setFocusableInTouchMode(false);
            viewHolder.confirmAllocationBtn.setTag(viewHolder.position);
            viewHolder.confirmAllocationBtn.setOnClickListener(this);

            viewHolder.navigationBtn.setFocusable(false);
            viewHolder.navigationBtn.setFocusableInTouchMode(false);
            viewHolder.navigationBtn.setTag(viewHolder.position);
            viewHolder.navigationBtn.setOnClickListener(this);

            viewHolder.signBtn.setFocusable(false);
            viewHolder.signBtn.setFocusableInTouchMode(false);
            viewHolder.signBtn.setTag(viewHolder.position);
            viewHolder.signBtn.setOnClickListener(this);

            viewHolder.signInfoBtn.setFocusable(false);
            viewHolder.signInfoBtn.setFocusableInTouchMode(false);
            viewHolder.signInfoBtn.setTag(viewHolder.position);
            viewHolder.signInfoBtn.setOnClickListener(this);

            viewHolder.evaluateBtn.setFocusable(false);
            viewHolder.evaluateBtn.setFocusableInTouchMode(false);
            viewHolder.evaluateBtn.setTag(viewHolder.position);
            viewHolder.evaluateBtn.setOnClickListener(this);

            viewHolder.evaluateInfoBtn.setFocusable(false);
            viewHolder.evaluateInfoBtn.setFocusableInTouchMode(false);
            viewHolder.evaluateInfoBtn.setTag(viewHolder.position);
            viewHolder.evaluateInfoBtn.setOnClickListener(this);

            viewHolder.drawbackBtn.setFocusable(false);
            viewHolder.drawbackBtn.setFocusableInTouchMode(false);
            viewHolder.drawbackBtn.setTag(viewHolder.position);
            viewHolder.drawbackBtn.setOnClickListener(this);

            viewHolder.deleteBtn.setFocusable(false);
            viewHolder.deleteBtn.setFocusableInTouchMode(false);
            viewHolder.deleteBtn.setTag(viewHolder.position);
            viewHolder.deleteBtn.setOnClickListener(this);

            viewHolder.receiveBtn.setFocusable(false);
            viewHolder.receiveBtn.setFocusableInTouchMode(false);
            viewHolder.receiveBtn.setTag(viewHolder.position);
            viewHolder.receiveBtn.setOnClickListener(this);

            viewHolder.refuseBtn.setFocusable(false);
            viewHolder.refuseBtn.setFocusableInTouchMode(false);
            viewHolder.refuseBtn.setTag(viewHolder.position);
            viewHolder.refuseBtn.setOnClickListener(this);

        }

        @Override
        public View newView(Context context, PageCursor cursor, ViewGroup parent) {
            View convertView = mInflater.inflate(R.layout.fragment_my_orders_list_item, null);
            ViewHolder.findAndCacheViews(convertView);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            if (!NetworkUtils.isNetworkAvaliable(mContext)) {
                showToast(R.string.errcode_network_unavailable);
                return;
            }
            int position = (Integer) v.getTag();
            final PageCursor cursor = (PageCursor) mAdapter.getItem(position);
            String orderJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_ORDER));
            String goodJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_GOODS));
            String shipperJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_SHIPPER));
            String consigneJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_CONSIGNE));
            String agentJson = cursor.getString(cursor.getColumnIndex(MyOrdersAllModel.COLUMN_ORDERS_AGENT));
            Gson gson = new Gson();
            ModelOrder modelOrder = gson.fromJson(orderJson, ModelOrder.class);
            ModelGoods modelGoods = gson.fromJson(goodJson, ModelGoods.class);
            // 发货人信息
            ModelShipper modelShipper = gson.fromJson(shipperJson, ModelShipper.class);
            // 收货人信息
            ModelShipper modelConsigne = gson.fromJson(consigneJson, ModelShipper.class);
            ModelAgent modelAgent = gson.fromJson(agentJson, ModelAgent.class);

            switch (v.getId()) {
                case R.id.btn_local_agent:// 定位货代
                    // localAgent(modelAgent.getLocation());
                    localAgent(modelAgent);
                    break;
                case R.id.btn_local_owner:// 定位货主
                    // TODO
                    // Toast.makeText(mContext, "定位货主", Toast.LENGTH_SHORT).show();
                    goodsOwner(modelShipper);
                    break;
                case R.id.btn_pay:// 付信息费按钮
                    payment(modelOrder.getId(), modelGoods.getInfofare());
                    break;
                case R.id.btn_confirm_allocation:// 确认配货按钮
                    confirmAllocation(modelOrder.getId());
                    break;
                case R.id.btn_navigation:// 导航
                    // TODO
                    // Toast.makeText(mContext, "导航", Toast.LENGTH_SHORT).show();
                    startNavigation(modelConsigne);
                    break;
                case R.id.btn_sign:// 签收
                    sign(modelOrder, modelGoods, modelShipper, modelConsigne);
                    break;
                case R.id.btn_signinfo:// 签收信息
                    signInfo(modelOrder);
                    break;
                case R.id.btn_evaluate:// 评价
                    evaluateAgent(modelOrder, modelAgent);
                    break;
                case R.id.btn_evaluate_info:// 评价信息
                    evaluateInfo(modelOrder, modelAgent);
                    break;
                case R.id.tv_agent_phonenumber:// 拨打货代电话
                    mContext.startActivity(IntentUtils.getCallItent(modelAgent.getTel()));
                    break;
                case R.id.tv_shipper_tel:// 发货人电话
                    mContext.startActivity(IntentUtils.getCallItent(modelShipper.getMobile()));
                    break;
                case R.id.tv_consigne_tel:// 收货人电话
                    mContext.startActivity(IntentUtils.getCallItent(modelConsigne.getMobile()));
                    break;
                case R.id.iv_star_image:// 显示司机的星级
                    showAgentCredit(modelAgent);
                    break;
                case R.id.btn_delete_order:// 删除订单
                    deleteOrder(modelOrder.getId());
                    break;
                case R.id.btn_receive_order:// 接受订单
                    receiveOrder(modelOrder.getId());
                    break;
                case R.id.btn_refuse_order:// 拒绝订单
                    refuseOrder(modelOrder.getId());
                    break;
                default:
                    break;
            }

        }

    }

    private static class ViewHolder {
        public static ViewHolder findAndCacheViews(View convertView) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.listItem = (LinearLayout) convertView.findViewById(R.id.list_item);
            viewHolder.route = (MarqueeTextView) convertView.findViewById(R.id.tv_route);
            viewHolder.route.setInnerFocus(true);
            viewHolder.weight = (MarqueeTextView) convertView.findViewById(R.id.tv_weight);
            viewHolder.weight.setInnerFocus(true);
            viewHolder.count = (MarqueeTextView) convertView.findViewById(R.id.tv_count);
            viewHolder.count.setInnerFocus(true);
            viewHolder.volume = (MarqueeTextView) convertView.findViewById(R.id.tv_volume);
            viewHolder.volume.setInnerFocus(true);
            viewHolder.fareLable = (TextView) convertView.findViewById(R.id.tv_fare_lable);
            viewHolder.fare = (TextView) convertView.findViewById(R.id.tv_fare);
            viewHolder.infoFareLable = (TextView) convertView.findViewById(R.id.tv_infofare_lable);
            viewHolder.infoFare = (TextView) convertView.findViewById(R.id.tv_infofare);
            viewHolder.memo = (TextView) convertView.findViewById(R.id.tv_memo);
            viewHolder.orderno = (TextView) convertView.findViewById(R.id.tv_orderno);
            viewHolder.generateTime = (TextView) convertView.findViewById(R.id.tv_order_generatetime);
            viewHolder.orderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
            viewHolder.goodsNo = (TextView) convertView.findViewById(R.id.tv_goodsno);
            viewHolder.goodsVolume = (TextView) convertView.findViewById(R.id.tv_goods_volume);
            viewHolder.goodsType = (TextView) convertView.findViewById(R.id.tv_goodstype);
            viewHolder.goodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);

            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            viewHolder.goodsDeclaredValue = (TextView) convertView.findViewById(R.id.tv_declaredvalue);
            viewHolder.agentInfo = (LinearLayout) convertView.findViewById(R.id.llyt_agent_info);
            viewHolder.agentPhoto = (ImageView) convertView.findViewById(R.id.head_portrait);
            viewHolder.agentNo = (TextView) convertView.findViewById(R.id.tv_agent_no);
            viewHolder.agentCompany = (TextView) convertView.findViewById(R.id.tv_agent_company);
            viewHolder.agentAdd = (TextView) convertView.findViewById(R.id.tv_agent_address);
            viewHolder.agentName = (TextView) convertView.findViewById(R.id.tv_agent_contact);
            viewHolder.agentTel = (TextView) convertView.findViewById(R.id.tv_agent_phonenumber);

            viewHolder.consigneLayout = convertView.findViewById(R.id.llyt_consigne);
            viewHolder.shipperLayout = convertView.findViewById(R.id.llyt_shipper);
            viewHolder.shipperName = (TextView) convertView.findViewById(R.id.tv_shipper_name);
            viewHolder.shipperTel = (TextView) convertView.findViewById(R.id.tv_shipper_tel);
            viewHolder.shipperCompany = (TextView) convertView.findViewById(R.id.tv_shipper_company);
            viewHolder.shipperAddress = (TextView) convertView.findViewById(R.id.tv_shipper_address);

            viewHolder.consigneName = (TextView) convertView.findViewById(R.id.tv_consigne_name);
            viewHolder.consigneTel = (TextView) convertView.findViewById(R.id.tv_consigne_tel);
            viewHolder.consigneCompany = (TextView) convertView.findViewById(R.id.tv_consigne_company);
            viewHolder.consigneAddress = (TextView) convertView.findViewById(R.id.tv_consigne_address);

            viewHolder.localAgentBtn = (Button) convertView.findViewById(R.id.btn_local_agent);
            viewHolder.localOwnerBtn = (Button) convertView.findViewById(R.id.btn_local_owner);
            viewHolder.payBtn = (Button) convertView.findViewById(R.id.btn_pay);
            viewHolder.confirmAllocationBtn = (Button) convertView.findViewById(R.id.btn_confirm_allocation);
            viewHolder.navigationBtn = (Button) convertView.findViewById(R.id.btn_navigation);
            viewHolder.signBtn = (Button) convertView.findViewById(R.id.btn_sign);
            viewHolder.signInfoBtn = (Button) convertView.findViewById(R.id.btn_signinfo);
            viewHolder.evaluateBtn = (Button) convertView.findViewById(R.id.btn_evaluate);
            viewHolder.evaluateInfoBtn = (Button) convertView.findViewById(R.id.btn_evaluate_info);
            viewHolder.drawbackBtn = (Button) convertView.findViewById(R.id.btn_drawback);
            viewHolder.starImage = (ImageView) convertView.findViewById(R.id.iv_star_image);
            viewHolder.drawback = (TextView) convertView.findViewById(R.id.tv_drawback);
            viewHolder.backstatus = (TextView) convertView.findViewById(R.id.tv_backstatus);
            viewHolder.price_tv = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.price2_tv = (TextView) convertView.findViewById(R.id.tv_price2);
            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.btn_delete_order);
            viewHolder.receiveBtn = (Button) convertView.findViewById(R.id.btn_receive_order);
            viewHolder.refuseBtn = (Button) convertView.findViewById(R.id.btn_refuse_order);

            viewHolder.textWeight = (TextView) convertView.findViewById(R.id.tv_weight_text);
            viewHolder.textCount = (TextView) convertView.findViewById(R.id.tv_count_text);
            viewHolder.textVolume = (TextView) convertView.findViewById(R.id.tv_volume_text);
            convertView.setTag(viewHolder);
            return viewHolder;
        }

        public LinearLayout listItem;

        public TextView price_tv;

        public TextView price2_tv;

        public TextView textWeight;

        public TextView textCount;

        public TextView textVolume;

        public TextView drawback;// 结算方式

        public TextView backstatus;// 回单要求

        public MarqueeTextView route;// 收发货地

        // public TextView shipProvince;
        // public TextView shipCity;
        // public TextView shipCounty;
        // public TextView receiptProvince;
        // public TextView receiptCity;
        // public TextView receiptCounty;
        public MarqueeTextView weight;

        public MarqueeTextView count;

        public MarqueeTextView volume;

        public TextView fareLable;

        public TextView infoFareLable;

        public TextView fare;

        public TextView infoFare;

        public TextView memo;

        public TextView orderno;

        public TextView generateTime;

        public TextView orderStatus;

        public TextView goodsNo;

        public TextView goodsVolume;

        public TextView goodsType;

        public TextView goodsCount;

        public TextView goodsName;

        public TextView goodsDeclaredValue;

        public LinearLayout agentInfo;

        public ImageView agentPhoto;

        public TextView agentNo;

        public TextView agentTel;

        public TextView agentCompany;

        public TextView agentAdd;

        public TextView agentName;

        public View consigneLayout, shipperLayout;

        public TextView shipperName;

        public TextView shipperTel;

        public TextView shipperCompany;

        public TextView shipperAddress;

        public TextView consigneName;

        public TextView consigneTel;

        public TextView consigneCompany;

        public TextView consigneAddress;

        public Button localAgentBtn, localOwnerBtn, payBtn, confirmAllocationBtn, navigationBtn, signBtn, signInfoBtn,
                evaluateBtn, evaluateInfoBtn, drawbackBtn, deleteBtn, receiveBtn, refuseBtn;

        public ImageView starImage;

        int position;
    }

    protected Context mContext;

    private OrderSignDialog orderSignDialog;

    private EvaluateDialog evaluateDialog;

    private EvaluateDialog evaluateInfoDialog;

    private PaymentOrderDialog paymentOrderDialog;

    private SignInfoDialog signInfoDialog;

    /**
     * 确认配货接口
     * 
     * @param orderId
     */
    public void confirmAllocation(final String orderId) {

        ComfirmDialog dialog = new ComfirmDialog(getActivity(), 0, new ComfirmCallback() {

            @Override
            public boolean onOperate(int position) {
                if (NetworkUtils.isNetworkAvaliable(getActivity())) {
                    showDialog();
                    String leavelocation = null;

                    if (null != driverApplication.bdLocation) {
                        Log.d(TAG, "driverApplication.getLatitude() : " + driverApplication.bdLocation.getLatitude());
                        Log.d(TAG, "bdLocation.getLongitude() : " + driverApplication.bdLocation.getLongitude());
                        leavelocation = driverApplication.bdLocation.getLatitude() + ","
                                + driverApplication.bdLocation.getLongitude();
                    }
                    CanvassingOrderAPI canvassingOrderAPI = new CanvassingOrderAPI(orderId, leavelocation,
                            driverApplication.bdLocation.getCity());
                    new WisdomCityHttpResponseHandler(canvassingOrderAPI, new APIFinishCallback() {

                        @Override
                        public void OnRemoteApiFinish(BasicResponse response) {
                            dismissDialog();
                            if (mContext == null || ((Activity) mContext).isFinishing()) {
                                return;
                            }
                            if (response.status == BasicResponse.SUCCESS) {
                                showToast(R.string.confirmAllocation_ok);
                                loadData(true, false);
                                // fragment.queryTotal(mTimeRange);
                            } else {
                                showToast(R.string.confirmAllocation_failed);
                            }
                        }
                    });
                    WisdomCityRestClient.execute(canvassingOrderAPI);
                } else {
                    showToast(R.string.errcode_network_unavailable);
                }
                return true;
            }
        }, mContext.getResources().getString(R.string.myorders_order_confirm_allocation));
        dialog.show();

        // showDialog();
        // String leavelocation = null;
        //
        // // if (null != locData) {
        // // Log.d(TAG, "locData.latitude : " + locData.latitude);
        // // Log.d(TAG, "locData.longitude : " + locData.longitude);
        // // leavelocation = locData.latitude + "," + locData.longitude;
        // // }
        // // CanvassingOrderAPI canvassingOrderAPI = new
        // // CanvassingOrderAPI(orderId,
        // // leavelocation, localcity);
        // if (null != driverApplication.bdLocation) {
        // Log.d(TAG, "driverApplication.getLatitude() : "
        // + driverApplication.bdLocation.getLatitude());
        // Log.d(TAG, "bdLocation.getLongitude() : "
        // + driverApplication.bdLocation.getLongitude());
        // leavelocation = driverApplication.bdLocation.getLatitude() + ","
        // + driverApplication.bdLocation.getLongitude();
        // }
        // CanvassingOrderAPI canvassingOrderAPI = new
        // CanvassingOrderAPI(orderId,
        // leavelocation, driverApplication.bdLocation.getCity());
        // new WisdomCityHttpResponseHandler(canvassingOrderAPI,
        // new APIFinishCallback() {
        //
        // @Override
        // public void OnRemoteApiFinish(BasicResponse response) {
        // dismissDialog();
        // if (mContext == null
        // || ((Activity) mContext).isFinishing()) {
        // return;
        // }
        // if (response.status == BasicResponse.SUCCESS) {
        // showToast(R.string.confirmAllocation_ok);
        // loadData(true, false);
        // fragment.queryTotal(mTimeRange);
        // } else {
        // showToast(R.string.confirmAllocation_failed);
        // }
        // }
        // });
        // WisdomCityRestClient.execute(canvassingOrderAPI);
    }

    /**
     * 展示货代的星级
     * 
     * @param modelAgent
     */
    public void showAgentCredit(final ModelAgent modelAgent) {
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            showToast(R.string.errcode_network_unavailable);
            return;
        }
        // String starimg = "";
        if (null != modelAgent.getStarImg() && !TextUtils.isEmpty(modelAgent.getStarImg())) {
            starimg = modelAgent.getStarImg();
        } else {
            starimg = "creditrating_star0";
        }
        // String credit = "0";// 得分
        if (null != modelAgent.getCredit() && !TextUtils.isEmpty(modelAgent.getCredit())) {
            credit = modelAgent.getCredit();
        }
        // String creditcount = "0";// 成交笔数
        if (null != modelAgent.getCreditcount() && !TextUtils.isEmpty(modelAgent.getCreditcount())) {
            creditcount = modelAgent.getCreditcount();
        }
        showDialog();
        CreditLevelAPI creditlevelapi = new CreditLevelAPI(getActivity(), modelAgent.getId());
        new WisdomCityHttpResponseHandler(creditlevelapi, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                dismissDialog();
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                if (response.status == BasicResponse.SUCCESS) {

                    CreditLevelAPI.CreditLevelAPIResponse res = (CreditLevelAPIResponse) response;
                    AgentStarLevelDialog agentStarLevelDialog = new AgentStarLevelDialog(getActivity(),
                            R.style.CustomerDialog_new, starimg, credit, res.starlevel.getDesc(),
                            res.starlevel.getQuality(), creditcount);
                    agentStarLevelDialog.show();
                } else {
                    Toast.makeText(getActivity(), response.msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        WisdomCityRestClient.execute(creditlevelapi);
    }

    /**
     * 定位货代
     */
    public void localAgent(ModelAgent modelAgent) {

        if (null != driverApplication.bdLocation && null != modelAgent) {
            if (!TextUtils.isEmpty(modelAgent.getCity()) && !TextUtils.isEmpty(modelAgent.getCountry())
                    && !TextUtils.isEmpty(modelAgent.getAddress())) {
                geoCoder(modelAgent.getCity(), modelAgent.getCountry() + modelAgent.getAddress());
            } else {
                Toast.makeText(mContext,
                        mContext.getResources().getString(R.string.myorders_order_location_agent_fail),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.myorders_order_location_fail),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 定位货代
     */
    public void localAgent(String agentGps) {

        if (null != driverApplication.bdLocation && null != agentGps && !TextUtils.isEmpty(agentGps)) {

            Log.d(TAG, "driverApplication.getLatitude() : " + driverApplication.bdLocation.getLatitude());
            Log.d(TAG, "driverApplication.getLongitude() : " + driverApplication.bdLocation.getLongitude());

            String[] agentGpss = agentGps.split(",");
            if (null != agentGpss && agentGpss.length == 2 && !TextUtils.isEmpty(agentGpss[0])
                    && !TextUtils.isEmpty(agentGpss[1])) {
                double desLocationLat = Double.valueOf(agentGpss[0]);
                double desLocationLon = Double.valueOf(agentGpss[1]);
                double srcLocationLat = driverApplication.bdLocation.getLatitude();
                double srcLocationLon = driverApplication.bdLocation.getLongitude();

                Log.d("NET_LOG", "src (" + srcLocationLat + "," + srcLocationLon + "), (" + desLocationLat + ","
                        + desLocationLon + ")");
                // BaiduNaviManager.getInstance().launchNavigator(getActivity(),
                // srcLocationLat, srcLocationLon, "起点", desLocationLat,
                // desLocationLon, "终点",
                // NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
                // true, // 真实导航
                // BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
                // new OnStartNavigationListener() { // 跳转监听
                //
                // @Override
                // public void onJumpToNavigator(Bundle configParams) {
                // Intent intent = new Intent(getActivity(),
                // BNavigatorActivity.class);
                // intent.putExtras(configParams);
                // startActivity(intent);
                // }
                //
                // @Override
                // public void onJumpToDownloader() {
                // }
                // });

                BNaviPoint startPoint = new BNaviPoint(srcLocationLon, srcLocationLat, "起点",
                        BNaviPoint.CoordinateType.BD09_MC);
                BNaviPoint endPoint = new BNaviPoint(desLocationLon, desLocationLat, "终点",
                        BNaviPoint.CoordinateType.BD09_MC);

                BaiduNaviManager.getInstance().launchNavigator(getActivity(), startPoint, endPoint,
                        NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
                        true, // 真实导航
                        BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //
                        new OnStartNavigationListener() { // 跳转监听

                            @Override
                            public void onJumpToNavigator(Bundle configParams) {
                                Intent intent = new Intent(getActivity(), BNavigatorActivity.class);
                                intent.putExtras(configParams);
                                startActivity(intent);
                            }

                            @Override
                            public void onJumpToDownloader() {
                            }
                        });
            }

            // Intent intent = new Intent(mContext, BaiduMapActivity.class);
            // intent.putExtra("currLocation_lat",
            // driverMainActivity.bdLocation.getLatitude());
            // intent.putExtra("currLocation_lon",
            // driverMainActivity.bdLocation.getLongitude());
            // intent.putExtra("agent_lat", Double.valueOf(agentGpss[0]));
            // intent.putExtra("agent_lon", Double.valueOf(agentGpss[1]));
            // mContext.startActivity(intent);

        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.myorders_order_location_fail),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 定位货主
     */
    public void goodsOwner(ModelShipper modelShipper) {
        // TODO
        if (null != driverApplication.bdLocation && null != modelShipper) {

            if (TextUtils.isEmpty(modelShipper.getCountry())) {
                geoCoder(modelShipper.getCity(),
                        modelShipper.getProvince() + modelShipper.getCity() + modelShipper.getAddress());
            } else {
                geoCoder(modelShipper.getCity(), modelShipper.getCountry() + modelShipper.getAddress());
            }

        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.myorders_order_gps_fail),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 开始导航
     */
    public void startNavigation(ModelShipper modelConsigne) {
        // TODO
        if (null != driverApplication.bdLocation && null != modelConsigne) {

            if (TextUtils.isEmpty(modelConsigne.getCountry())) {
                geoCoder(modelConsigne.getCity(),
                        modelConsigne.getProvince() + modelConsigne.getCity() + modelConsigne.getAddress());
            } else {
                geoCoder(modelConsigne.getCity(), modelConsigne.getCountry() + modelConsigne.getAddress());
            }

        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.myorders_order_gps_fail),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void geoCoder(String city, String geoCodeKey) {
        mSearch.geocode(new GeoCodeOption().city(city).address(geoCodeKey));

    }

    OnGetGeoCoderResultListener getGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
                Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
                return;
            }
            // 获取地理编码结果
            // String strInfo = String.format("纬度：%f 经度：%f",
            // result.getLocation().latitude,
            // result.getLocation().longitude);
            // Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();

            double desLocationLat = result.getLocation().latitude;
            double desLocationLon = result.getLocation().longitude;
            double srcLocationLat = driverApplication.bdLocation.getLatitude();
            double srcLocationLon = driverApplication.bdLocation.getLongitude();

            Log.d("NET_LOG", "src (" + srcLocationLat + "," + srcLocationLon + "), (" + desLocationLat + ","
                    + desLocationLon + ")");

            BNaviPoint startPoint = new BNaviPoint(srcLocationLon, srcLocationLat, "起点",
                    BNaviPoint.CoordinateType.BD09_MC);
            BNaviPoint endPoint = new BNaviPoint(desLocationLon, desLocationLat, "终点",
                    BNaviPoint.CoordinateType.BD09_MC);

            BaiduNaviManager.getInstance().launchNavigator(getActivity(), startPoint, endPoint,
                    NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
                    true, // 真实导航
                    BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //
                    new OnStartNavigationListener() { // 跳转监听

                        @Override
                        public void onJumpToNavigator(Bundle configParams) {
                            Intent intent = new Intent(getActivity(), BNavigatorActivity.class);
                            intent.putExtras(configParams);
                            startActivity(intent);
                        }

                        @Override
                        public void onJumpToDownloader() {
                        }
                    });

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
                Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            }
            // 获取反向地理编码结果
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (orderSignDialog != null) {
            orderSignDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(getGeoCoderResultListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        driverMainActivity = (DriverMainActivity) getActivity();
        driverApplication = (DriverApplication) driverMainActivity.getApplication();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 评价货代
     * 
     * @param modelOrder
     * @param modelAgent
     */
    protected void evaluateAgent(ModelOrder modelOrder, ModelAgent modelAgent) {
        if (evaluateDialog != null && evaluateDialog.isShowing()) {
            return;
        } else {
            evaluateDialog = new EvaluateDialog(mContext, R.style.CustomerDialog, modelOrder.getId(),
                    modelAgent.getId());
            evaluateDialog.setCallback(new OperateCallback() {

                @Override
                public void onOperate(int resId, Map<String, Object> params) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onOperateFinished() {
                    loadData(true, false);
                    // fragment.queryTotal(mTimeRange);
                }

                @Override
                public void onReservFinished() {
                    // TODO Auto-generated method stub

                }
            });
            evaluateDialog.show();
        }
    }

    /**
     * 查看评价信息
     * 
     * @param modelOrder
     * @param modelAgent
     */
    protected void evaluateInfo(final ModelOrder modelOrder, final ModelAgent modelAgent) {
        showDialog();
        OrderCommentDetailAPI commentDetailAPI = new OrderCommentDetailAPI(modelOrder.getId(),
                GlobalModel.getInst().mLoginModel.getCarId(), modelAgent.getId(),
                OrderCommentDetailAPI.FLAG_DRIVER_LOOK);
        new WisdomCityHttpResponseHandler(commentDetailAPI, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                dismissDialog();
                if (mContext == null || ((Activity) mContext).isFinishing()) {
                    return;
                }
                if (response.status == BasicResponse.SUCCESS) {
                    OrderCommentDetailAPI.OrderCommentDetailAPIResponse reps = (OrderCommentDetailAPIResponse) response;
                    if ((evaluateInfoDialog != null && evaluateInfoDialog.isShowing())) {
                        return;
                    } else {
                        evaluateInfoDialog = new EvaluateDialog(mContext, R.style.CustomerDialog, modelOrder.getId(),
                                modelAgent.getId(), reps.mCommentMap.get("carcomment"), reps.mCommentMap
                                        .get("agentcomment"), EvaluateDialog.FLAG_EVALUATE_INFO);
                        evaluateInfoDialog.show();
                    }
                } else {
                    showToast(R.string.get_signinfo_failed);
                }
            }
        });
        WisdomCityRestClient.execute(commentDetailAPI);
    }

    /**
     * 付款
     */
    protected void payment(String orderId, String infoValue) {
        if (paymentOrderDialog != null && paymentOrderDialog.isShowing()) {
            return;
        } else {
            paymentOrderDialog = new PaymentOrderDialog(mContext, R.style.CustomerDialog, orderId, infoValue);

            paymentOrderDialog.setCallback(new OperateCallback() {

                @Override
                public void onOperate(int resId, Map<String, Object> params) {

                }

                @Override
                public void onOperateFinished() {
                    loadData(true, false);
                    // fragment.queryTotal(mTimeRange);
                }

                @Override
                public void onReservFinished() {
                    // TODO Auto-generated method stub

                }
            });
            paymentOrderDialog.show();
        }
    }

    protected void setOrderStatus(ViewHolder viewHolder, ModelOrder modelOrder) {
        viewHolder.deleteBtn.setVisibility(View.GONE);
        viewHolder.localAgentBtn.setVisibility(View.GONE);
        viewHolder.localOwnerBtn.setVisibility(View.GONE);
        viewHolder.payBtn.setVisibility(View.GONE);
        viewHolder.confirmAllocationBtn.setVisibility(View.GONE);
        viewHolder.navigationBtn.setVisibility(View.GONE);
        viewHolder.signBtn.setVisibility(View.GONE);
        viewHolder.signInfoBtn.setVisibility(View.GONE);
        viewHolder.evaluateBtn.setVisibility(View.GONE);
        viewHolder.evaluateInfoBtn.setVisibility(View.GONE);
        viewHolder.receiveBtn.setVisibility(View.GONE);
        viewHolder.refuseBtn.setVisibility(View.GONE);

        switch (Integer.parseInt(modelOrder.getStatus())) {
            case Constants.ORDER_STATUS_APPOINT:
                viewHolder.orderStatus.setText(mContext.getResources()
                        .getString(R.string.myorders_order_status_appoint));
                viewHolder.receiveBtn.setVisibility(View.VISIBLE);
                viewHolder.refuseBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_NEW:
                viewHolder.orderStatus.setText(mContext.getResources().getString(R.string.myorders_order_status_new));
                viewHolder.localAgentBtn.setVisibility(View.VISIBLE);
                viewHolder.payBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_PAIED:
                viewHolder.orderStatus.setText(mContext.getResources().getString(R.string.myorders_order_status_paied));
                viewHolder.localAgentBtn.setVisibility(View.VISIBLE);
                break;
            // modify by zys,增加订单取消状态
            case Constants.ORDER_STATUS_CANCELED:
                viewHolder.orderStatus.setText(mContext.getResources().getString(
                        R.string.myorders_order_status_canceled));
                // viewHolder.deleteBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_CONFIRMRECEIPT:// 货代确认收款
                switch (Integer.parseInt(modelOrder.getGoodscustom())) {
                    case Constants.GOODS_CUSTOM_PUBLIC:
                        viewHolder.orderStatus.setText(mContext.getResources().getString(
                                R.string.myorders_order_status_confirmreceipt_public));
                        break;
                    case Constants.GOODS_CUSTOM_PRIVATE:
                        viewHolder.orderStatus.setText(mContext.getResources().getString(
                                R.string.myorders_order_status_confirmreceipt_private));
                        break;
                    default:
                        break;
                }
                viewHolder.localOwnerBtn.setVisibility(View.VISIBLE);
                viewHolder.confirmAllocationBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_CANVASSING:
                viewHolder.orderStatus.setText(mContext.getResources().getString(
                        R.string.myorders_order_status_canvassing));
                viewHolder.navigationBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_EXECUTING:
                viewHolder.orderStatus.setText(mContext.getResources().getString(
                        R.string.myorders_order_status_executing));
                viewHolder.navigationBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_ARRIVED:
                viewHolder.orderStatus.setText(mContext.getResources()
                        .getString(R.string.myorders_order_status_arrived));
                viewHolder.navigationBtn.setVisibility(View.VISIBLE);
                viewHolder.signBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_SIGNED:
                viewHolder.orderStatus
                        .setText(mContext.getResources().getString(R.string.myorders_order_status_signed));
                viewHolder.signInfoBtn.setVisibility(View.VISIBLE);
                // viewHolder.evaluateBtn.setVisibility(View.VISIBLE);
                // switch (Integer.parseInt(modelOrder.getGoodscustom())) {
                // case Constants.GOODS_CUSTOM_PUBLIC:
                // viewHolder.evaluateBtn.setVisibility(View.VISIBLE);
                // break;
                // case Constants.GOODS_CUSTOM_PRIVATE:
                // break;
                // default:
                // break;
                // }

                break;
            case Constants.ORDER_STATUS_REFUSE_SIGNED:
                viewHolder.orderStatus.setText(mContext.getResources().getString(
                        R.string.myorders_order_status_refuse_signed));
                viewHolder.signInfoBtn.setVisibility(View.VISIBLE);
                // viewHolder.evaluateBtn.setVisibility(View.VISIBLE);
                // switch (Integer.parseInt(modelOrder.getGoodscustom())) {
                // case Constants.GOODS_CUSTOM_PUBLIC:
                // viewHolder.evaluateBtn.setVisibility(View.VISIBLE);
                // break;
                // case Constants.GOODS_CUSTOM_PRIVATE:
                // break;
                // default:
                // break;
                // }
                break;
            case Constants.ORDER_STATUS_DRIVER_EVALUATED:
                viewHolder.orderStatus.setText(mContext.getResources().getString(
                        R.string.myorders_order_status_driver_evaluated));
                viewHolder.signInfoBtn.setVisibility(View.VISIBLE);
                // viewHolder.evaluateInfoBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_AGENT_EVALUATED:
                viewHolder.orderStatus.setText(mContext.getResources().getString(
                        R.string.myorders_order_status_agent_evaluated));
                viewHolder.signInfoBtn.setVisibility(View.VISIBLE);
                // viewHolder.evaluateBtn.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_OVER:
                viewHolder.orderStatus.setText(mContext.getResources().getString(R.string.myorders_order_status_over));
                // viewHolder.deleteBtn.setVisibility(View.VISIBLE);
                viewHolder.signInfoBtn.setVisibility(View.VISIBLE);
                // viewHolder.evaluateInfoBtn.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 删除订单
     * 
     * @param id
     */
    public void deleteOrder(final String id) {
        ComfirmDialog dialog = new ComfirmDialog(mContext, 0, new ComfirmCallback() {

            @Override
            public boolean onOperate(int position) {
                if (NetworkUtils.isNetworkAvaliable(getActivity())) {
                    showDialog();
                    DeleteOrderAPI api = new DeleteOrderAPI(id);
                    new WisdomCityHttpResponseHandler(api, new APIFinishCallback() {

                        @Override
                        public void OnRemoteApiFinish(BasicResponse response) {
                            dismissDialog();
                            if (getActivity() == null || getActivity().isFinishing()) {
                                return;
                            }
                            if (response.status == BasicResponse.SUCCESS) {
                                showToast(R.string.delete_item_ok);
                                loadData(true, false);
                                // fragment.queryTotal(mTimeRange);
                            } else {
                                showToast(R.string.delete_item_failed);
                            }
                        }
                    });
                    WisdomCityRestClient.execute(api);
                } else {
                    showToast(R.string.errcode_network_unavailable);
                }
                return true;
            }
        }, mContext.getResources().getString(R.string.myorders_order_delete_order));
        dialog.show();
    }

    /**
     * 查看签收信息
     * 
     * @param modelOrder
     */
    protected void signInfo(ModelOrder modelOrder) {
        showDialog();
        // TODO
        GetSignInfoAPI getSignInfoAPI = new GetSignInfoAPI(modelOrder.getId());
        new WisdomCityHttpResponseHandler(getSignInfoAPI, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                dismissDialog();
                if (mContext == null || ((Activity) mContext).isFinishing()) {
                    return;
                }
                if (response.status == BasicResponse.SUCCESS) {

                    signInfoDialog = new SignInfoDialog(mContext, R.style.CustomerDialog,
                            ((GetSignInfoAPI.GetSignInfoAPIResponse) response).mSignInfo);
                    signInfoDialog.show();
                } else {
                    showToast(R.string.get_signinfo_failed);
                }
            }
        });

        WisdomCityRestClient.execute(getSignInfoAPI);
    }

    /**
     * 结算方式
     * 
     * @param index
     * @return
     */
    public String setPayStatus(int index) {
        String str = "";
        switch (index) {
            case 0:
                str = mContext.getResources().getString(R.string.myorders_order_item_paystatus_delivery);
                break;
            case 1:
                str = mContext.getResources().getString(R.string.myorders_order_item_paystatus_backorder);
                break;
            default:
                str = mContext.getResources().getString(R.string.myorders_order_item_empty);
                break;
        }
        return str;
    }

    /**
     * 回单方式
     * 
     * @param index
     * @return
     */
    public String setDrawBack(int index) {
        String str = "";
        switch (index) {
            case 0:
                str = mContext.getResources().getString(R.string.myorders_order_item_backstatus_normal);
                break;
            case 1:
                str = mContext.getResources().getString(R.string.myorders_order_item_backstatus_fax);
                break;
            case 2:
                str = mContext.getResources().getString(R.string.myorders_order_item_backstatus_scan);
                break;
            default:
                str = mContext.getResources().getString(R.string.myorders_order_item_empty);
                break;
        }
        return str;
    }

    /**
     * 签收订单功能
     * 
     * @param modelOrder
     * @param modelGoods
     * @param modelShipper
     * @param modelConsigne
     */
    private void sign(ModelOrder modelOrder, ModelGoods modelGoods, ModelShipper modelShipper,
            ModelShipper modelConsigne) {
        if (orderSignDialog != null && orderSignDialog.isShowing()) {
            return;
        } else {
            orderSignDialog = new OrderSignDialog(mContext, R.style.CustomerDialog, modelOrder, modelGoods,
                    modelShipper, modelConsigne);
            orderSignDialog.setCallback(new OperateCallback() {

                @Override
                public void onOperate(int resId, Map<String, Object> params) {

                }

                @Override
                public void onOperateFinished() {
                    loadData(true, false);
                    // fragment.queryTotal(mTimeRange);
                }

                @Override
                public void onReservFinished() {

                }
            });
            orderSignDialog.show();
        }
    }

    /**
     * 接受订单
     */
    private void receiveOrder(final String id) {
        ComfirmDialog dialog = new ComfirmDialog(getActivity(), 0, new ComfirmCallback() {

            @Override
            public boolean onOperate(int position) {
                if (NetworkUtils.isNetworkAvaliable(getActivity())) {
                    showDialog();
                    ReceiveOrderAPI api = new ReceiveOrderAPI(id);
                    new WisdomCityHttpResponseHandler(api, new APIFinishCallback() {

                        @Override
                        public void OnRemoteApiFinish(BasicResponse response) {
                            dismissDialog();
                            if (getActivity() == null || getActivity().isFinishing()) {
                                return;
                            }
                            if (response.status == BasicResponse.SUCCESS) {
                                showToast(R.string.receive_ok);
                                loadData(true, false);
                                // fragment.queryTotal(mTimeRange);
                            } else {
                                showToast(R.string.receive_failed);
                            }
                        }
                    });
                    WisdomCityRestClient.execute(api);
                } else {
                    showToast(R.string.errcode_network_unavailable);
                }
                return true;
            }
        }, mContext.getResources().getString(R.string.myorders_order_receive_order));
        dialog.show();
    }

    /**
     * 拒绝订单
     */
    private void refuseOrder(final String id) {
        // ComfirmDialog dialog = new ComfirmDialog(getActivity(), 0,
        // new ComfirmCallback() {
        //
        // @Override
        // public boolean onOperate(int position) {
        // if (NetworkUtils.isNetworkAvaliable(getActivity())) {
        // showDialog();
        // RefuseOrderAPI refuseOrderAPI = new RefuseOrderAPI(
        // id);
        // new WisdomCityHttpResponseHandler(refuseOrderAPI,
        // new APIFinishCallback() {
        //
        // @Override
        // public void OnRemoteApiFinish(
        // BasicResponse response) {
        // dismissDialog();
        // if (getActivity() == null
        // || getActivity()
        // .isFinishing()) {
        // return;
        // }
        // if (response.status == BasicResponse.SUCCESS) {
        // showToast(R.string.refuse_ok);
        // loadData(true, false);
        // // fragment.queryTotal(mTimeRange);
        // } else {
        // showToast(R.string.refuse_failed);
        // }
        // }
        // });
        // WisdomCityRestClient.execute(refuseOrderAPI);
        // } else {
        // showToast(R.string.errcode_network_unavailable);
        // }
        // return true;
        // }
        // }, mContext.getResources().getString(
        // R.string.myorders_order_refuse_order));
        // dialog.show();

        RefuseOrderDialog dialog = new RefuseOrderDialog(getActivity(), 0, new OperateCallback() {

            @Override
            public void onReservFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onOperateFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onOperate(int resId, Map<String, Object> params) {
                if (NetworkUtils.isNetworkAvaliable(getActivity())) {
                    showDialog();
                    String mRefusalreason = (String) params.get(Constants.REFUSE_REASON_REFUSALREASON);
                    String mRefusalmemo = (String) params.get(Constants.REFUSE_REASON_REFUSALMEMO);
                    RefuseOrderAPI refuseOrderAPI = new RefuseOrderAPI(id, mRefusalreason, mRefusalmemo);
                    new WisdomCityHttpResponseHandler(refuseOrderAPI, new APIFinishCallback() {

                        @Override
                        public void OnRemoteApiFinish(BasicResponse response) {
                            dismissDialog();
                            if (getActivity() == null || getActivity().isFinishing()) {
                                return;
                            }
                            if (response.status == BasicResponse.SUCCESS) {
                                showToast(R.string.refuse_ok);
                                loadData(true, false);
                            } else {
                                showToast(R.string.refuse_failed);
                            }
                        }
                    });
                    WisdomCityRestClient.execute(refuseOrderAPI);
                } else {
                    showToast(R.string.errcode_network_unavailable);
                }
            }
        }, "");
        dialog.show();
    }

    @Override
    protected void showEmptyPage(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadData(true, false);
    }

    @Override
    protected void showToast(int isid) {
        Toast.makeText(getActivity(), isid, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    protected void loadData(boolean update, boolean loadmore) {
        // TODO Auto-generated method stub

    }

}
