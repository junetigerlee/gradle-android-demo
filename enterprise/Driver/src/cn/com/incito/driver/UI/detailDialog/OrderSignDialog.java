
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.DriverApplication;
import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.OperateCallback;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ModelGoods;
import cn.com.incito.driver.models.ModelOrder;
import cn.com.incito.driver.models.ModelShipper;
import cn.com.incito.driver.net.apis.UploadPicAPI;
import cn.com.incito.driver.net.apis.UploadPicAPI.UploadPicAPIResponse;
import cn.com.incito.driver.net.apis.WisdomCityRestClientParameterImpl;
import cn.com.incito.driver.net.apis.orders.SignAndRefuseOrderAPI;
import cn.com.incito.wisdom.sdk.image.display.BitmapDisplayerImpl;
import cn.com.incito.wisdom.sdk.image.display.DisplayAnim;
import cn.com.incito.wisdom.sdk.image.display.DisplayShape;
import cn.com.incito.wisdom.sdk.image.loader.DisplayImageOptions;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.assist.FailReason;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.FetchImageUtils;
import cn.com.incito.wisdom.sdk.utils.FetchImageUtils.OnPickFinishedCallback;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomRadioGroup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 订单签收
 * 
 * @description 订单签收
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class OrderSignDialog extends Dialog implements OnClickListener {

    private Context mContext;

    private OperateCallback mCallback;

    private String mPhoto;// 记录上传完以后的图片url地址

    private ModelOrder mOrder;

    private ModelShipper mShipper, mConsigne;

    private ModelGoods mGoods;

    private ViewStub mSignStub, mRefuseStub;

    private Button mSignBtn, mRefuseBtn;

    private TextView mOrderNoTV, mGoodsNameTV, mGoodsWeightTV, mGoodsCountTV, mShipperNameTV, mShipperTelTV,
            mShipperCompanyTV, mShipperAddTv, mConsigneNameTV, mConsigneTelTV, mConsigneCompanyTV, mConsigneAddTV;

    private View mOperateLayout;

    /** 签收照片 */
    private ImageView mSignPhoto;

    public DisplayImageOptions mImageDisplayOptions;

    private FetchImageUtils mImageUtil;

    private DriverMainActivity driverMainActivity;

    private DriverApplication driverApplication;

    public void setCallback(OperateCallback mCallback) {
        this.mCallback = mCallback;
    }

    public OrderSignDialog(Context context) {
        super(context);
    }

    public OrderSignDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public OrderSignDialog(Context context, int theme, ModelOrder order, ModelGoods goods, ModelShipper shipper,
            ModelShipper consigne) {
        super(context, theme);
        this.mContext = context;
        mOrder = order;
        mGoods = goods;
        mShipper = shipper;
        mConsigne = consigne;

        BitmapDisplayerImpl displayer = new BitmapDisplayerImpl(DisplayShape.DEFAULT, DisplayAnim.FADE_IN);
        mImageDisplayOptions = new DisplayImageOptions.Builder().displayer(displayer)
                .showStubImage(R.drawable.default_photo).showImageForEmptyUri(R.drawable.default_photo).cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public OrderSignDialog(Context context, int theme, String orderId) {
        super(context, theme);
        this.mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_sign);
        setCanceledOnTouchOutside(true);
        initView();

        driverMainActivity = (DriverMainActivity) mContext;
        driverApplication = (DriverApplication) driverMainActivity.getApplication();
    }

    private void initView() {
        // 订单、货物信息
        mOrderNoTV = (TextView) findViewById(R.id.orderno);
        mGoodsNameTV = (TextView) findViewById(R.id.goods_name);
        mGoodsWeightTV = (TextView) findViewById(R.id.weight);
        mGoodsCountTV = (TextView) findViewById(R.id.count);
        mOrderNoTV.setText(String.format(
                mContext.getResources().getString(R.string.myorders_list_item_detail_order_no), mOrder.getOrderno()));
        mGoodsNameTV
                .setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_goods_name),
                        mGoods.getGoodsname()));

        switch (Integer.parseInt(mGoods.getGoodstype())) {
            case Constants.GOODS_TYPE_HEAVY_CARGO:
                mGoodsWeightTV.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_goods_weight),
                        mGoods.getWeight()));
                break;
            case Constants.GOODS_TYPE_LIGHT_CARGO:
                mGoodsWeightTV.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_goods_volume),
                        mGoods.getVolume()));
                break;
            case Constants.GOODS_TYPE_DEVICES:
                mGoodsWeightTV.setText(String.format(
                        mContext.getResources().getString(R.string.myorders_list_item_detail_goods_weight),
                        mGoods.getWeight()));
                break;
            default:
                break;
        }

        mGoodsCountTV.setText(String.format(
                mContext.getResources().getString(R.string.myorders_list_item_detail_goods_count), mGoods.getCount()));
        // 发货人信息
        mShipperNameTV = (TextView) findViewById(R.id.shipper_name);
        mShipperTelTV = (TextView) findViewById(R.id.shipper_tel);
        mShipperCompanyTV = (TextView) findViewById(R.id.shipper_company);
        mShipperAddTv = (TextView) findViewById(R.id.shipper_address);
        if (null != mShipper) {
            mShipperNameTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_name),
                    mShipper.getName()));
            mShipperTelTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_tel),
                    mShipper.getMobile()));
            mShipperCompanyTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_company),
                    mShipper.getCompany()));
            mShipperAddTv.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_address),
                    (mShipper.getProvince() == null ? "" : mShipper.getProvince())
                            + (mShipper.getCity() == null ? "" : mShipper.getCity())
                            + (mShipper.getCountry() == null ? "" : mShipper.getCountry()) + mShipper.getAddress()));
        } else {
            mShipperNameTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_name), ""));
            mShipperTelTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_tel), ""));
            mShipperCompanyTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_company), ""));
            mShipperAddTv.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_shipper_address), ""));
        }
        // 收货人信息
        mConsigneNameTV = (TextView) findViewById(R.id.consigne_name);
        mConsigneTelTV = (TextView) findViewById(R.id.consigne_tel);
        mConsigneCompanyTV = (TextView) findViewById(R.id.consigne_company);
        mConsigneAddTV = (TextView) findViewById(R.id.consigne_address);
        if (null != mConsigne) {
            mConsigneNameTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_name),
                    mConsigne.getName()));
            mConsigneTelTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_tel),
                    mConsigne.getMobile()));
            mConsigneCompanyTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_company),
                    mConsigne.getCompany()));
            mConsigneAddTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_address),
                    (mConsigne.getProvince() == null ? "" : mConsigne.getProvince())
                            + (mConsigne.getCity() == null ? "" : mConsigne.getCity())
                            + (mConsigne.getCountry() == null ? "" : mConsigne.getCountry()) + mConsigne.getAddress()));
        } else {
            mConsigneNameTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_name), ""));
            mConsigneTelTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_tel), ""));
            mConsigneCompanyTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_company), ""));
            mConsigneAddTV.setText(String.format(
                    mContext.getResources().getString(R.string.myorders_list_item_detail_receipter_address), ""));
        }
        mSignBtn = (Button) findViewById(R.id.sign_btn);
        mRefuseBtn = (Button) findViewById(R.id.refuse_btn);
        mSignBtn.setOnClickListener(this);
        mRefuseBtn.setOnClickListener(this);
        mSignStub = (ViewStub) findViewById(R.id.order_sign_viewstub);
        mRefuseStub = (ViewStub) findViewById(R.id.order_refuse_sign_viewstub);
        mOperateLayout = findViewById(R.id.operate_layout);
    }

    /**
     * 设置签收信息图片
     * 
     * @param photoUri
     */
    public void setSignPhotoView(String photoUri) {
        ImageLoader.getInstance().loadImage(WisdomCityRestClientParameterImpl.getUrl() + photoUri,
                GlobalModel.getInst().mDisplayOptions, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                        mSignPhoto.setImageBitmap(loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (!NetworkUtils.isNetworkAvaliable(mContext)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.errcode_network_unavailable),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.sign_btn:
                // TODO
                mOperateLayout.setVisibility(View.GONE);
                View view = mSignStub.inflate();
                mSignPhoto = (ImageView) view.findViewById(R.id.photo);
                Button signShootBtn = (Button) view.findViewById(R.id.signlayout_shoot_btn);
                Button signLayoutConfirmBtn = (Button) view.findViewById(R.id.signlayout_confirm_btn);
                signShootBtn.setOnClickListener(this);
                signLayoutConfirmBtn.setOnClickListener(this);

                break;
            case R.id.refuse_btn:
                mOperateLayout.setVisibility(View.GONE);
                View refuseStub = mRefuseStub.inflate();
                Button refuseConfirmBtn = (Button) refuseStub.findViewById(R.id.refuselayout_confirm_btn);
                Button refuseCancelBtn = (Button) refuseStub.findViewById(R.id.refuselayout_cancel_btn);
                refuseConfirmBtn.setOnClickListener(this);
                refuseCancelBtn.setOnClickListener(this);
                CustomRadioGroup refuseInfoGroup = (CustomRadioGroup) refuseStub.findViewById(R.id.refuse_info_group);
                final EditText otherEt = (EditText) refuseStub.findViewById(R.id.other_info_edittext);
                RadioButton breButton = (RadioButton) refuseStub.findViewById(R.id.goods_break);
                refuseInfoGroup.setOnCheckedChangeListener(new CustomRadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CustomRadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.goods_break:
                            case R.id.goods_lose:
                            case R.id.goods_return:
                            case R.id.goods_delay:
                            case R.id.goods_chaos:
                                otherEt.setEnabled(false);
                                otherEt.setFocusable(false);
                                otherEt.setError(null);
                                break;
                            case R.id.other:
                                otherEt.setEnabled(true);
                                otherEt.setFocusable(true);
                                otherEt.setFocusableInTouchMode(true);
                                otherEt.requestFocus();
                                otherEt.setError(null);
                                break;
                            default:
                                break;
                        }
                    }
                }

                );
                breButton.setChecked(true);
                refuseConfirmBtn.setTag(R.id.other_info_edittext, otherEt);
                refuseConfirmBtn.setTag(R.id.refuse_info_group, refuseInfoGroup);

                break;
            case R.id.signlayout_shoot_btn:// 拍照功能
                showPhotoSelector();
                break;
            case R.id.signlayout_confirm_btn:// 确认签收功能
                signConfirm();

                break;
            case R.id.refuselayout_confirm_btn:// 拒签的确认按钮
                refuseLayoutConfirm(v);
                break;
            case R.id.refuselayout_cancel_btn:// 拒签的取消按钮
                this.dismiss();
                break;

            default:
                break;
        }
    }

    private String getLocation() {
        String location = "";
        if (null != driverApplication.bdLocation) {
            location = driverApplication.bdLocation.getLatitude() + "," + driverApplication.bdLocation.getLongitude();
        }

        return location;
    }

    private String getAddstr() {
        String address = "";
        if (null != driverApplication.bdLocation) {

            if (null != driverApplication.bdLocation.getProvince()) {
                address += driverApplication.bdLocation.getProvince();
            } else {
                address += "";
            }

            if (null != driverApplication.bdLocation.getCity()) {
                address += driverApplication.bdLocation.getCity();
            } else {
                address += "";
            }

            if (null != driverApplication.bdLocation.getDistrict()) {
                address += driverApplication.bdLocation.getDistrict();
            } else {
                address += "";
            }

            if (null != driverApplication.bdLocation.getStreet()) {
                address += driverApplication.bdLocation.getStreet();
            } else {
                address += "";
            }

        }
        return address;
    }

    /**
     * 签收确认功能
     */
    protected void signConfirm() {

        // ((DriverMainActivity) mContext).showLoadingDialog();
        driverMainActivity.showLoadingDialog();

        SignAndRefuseOrderAPI andRefuseOrderAPI = new SignAndRefuseOrderAPI(mOrder.getId(), String.valueOf("0"),
                mPhoto, getLocation(), getAddstr(), "", "");
        new WisdomCityHttpResponseHandler(andRefuseOrderAPI, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                ((DriverMainActivity) mContext).dismissLoadingDialog();
                if (((Activity) mContext) == null || ((Activity) mContext).isFinishing()) {
                    return;
                }
                if (response.status == BasicResponse.SUCCESS) {
                    Toast.makeText(mContext, R.string.confirmSignGoods_ok, Toast.LENGTH_SHORT).show();
                    mCallback.onOperateFinished();
                    dismiss();
                } else {
                    Toast.makeText(mContext, R.string.confirmSignGoods_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        WisdomCityRestClient.execute(andRefuseOrderAPI);
    }

    /**
     * 拒签的确认功能
     * 
     * @param v
     */
    protected void refuseLayoutConfirm(View v) {
        final EditText otherEtTag = (EditText) v.getTag(R.id.other_info_edittext);
        CustomRadioGroup refuseInfoGroupTag = (CustomRadioGroup) v.getTag(R.id.refuse_info_group);
        String memo = null;// 拒签原因-其他描述
        String refusal = null;// 拒签原因
        int index = -1;
        for (int i = 0; i < refuseInfoGroupTag.getChildCount() - 1; i++) {
            if (((RadioButton) refuseInfoGroupTag.getChildAt(i)).isChecked()) {
                index = i;
                break;
            }
        }
        if (index == -1) {// 如果用户选择的是“其他”
            if (TextUtils.isEmpty(otherEtTag.getText().toString())) {
                otherEtTag.requestFocus();
                otherEtTag.setError("请完成信息填写");
                return;
            }
            memo = otherEtTag.getText().toString();
            refusal = "5";// 其他
        } else {
            memo = "";
            refusal = String.valueOf(index);
        }
        // ((DriverMainActivity) mContext).showLoadingDialog();
        driverMainActivity.showLoadingDialog();

        SignAndRefuseOrderAPI andRefuseOrderAPI = new SignAndRefuseOrderAPI(mOrder.getId(), String.valueOf("1"), "",
                getLocation(), getAddstr(), memo, refusal);
        new WisdomCityHttpResponseHandler(andRefuseOrderAPI, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                ((DriverMainActivity) mContext).dismissLoadingDialog();
                if (((Activity) mContext) == null || ((Activity) mContext).isFinishing()) {
                    return;
                }
                if (response.status == BasicResponse.SUCCESS) {
                    Toast.makeText(mContext, R.string.refuseSignGoods_ok, Toast.LENGTH_SHORT).show();
                    mCallback.onOperateFinished();
                    dismiss();
                } else {
                    Toast.makeText(mContext, R.string.refuseSignGoods_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        WisdomCityRestClient.execute(andRefuseOrderAPI);
    }

    /**
     * 弹出菜单选择如何上传照片
     */
    public void showPhotoSelector() {
        String[] choices = new String[1];
        choices[0] = mContext.getResources().getString(R.string.edit_profile_photo_selector_camera);
        // choices[1] = mContext.getResources().getString(
        // R.string.edit_profile_photo_selector_gallery);
        Context context = new ContextThemeWrapper(mContext, R.style.Theme_CustomDialog_Window);
        final ListAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, choices);
        if (mImageUtil == null) {
            mImageUtil = new FetchImageUtils((Activity) mContext);
        }
        // modify by zys,for bug3412,call photographer directly
        // final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setSingleChoiceItems(adapter, -1,
        // new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // switch (which) {
        // case 0:
        mImageUtil.doTakePhoto(mPickCallback);
        // break;
        // // case 1:
        // // mImageUtil.doPickPhotoFromGallery(mPickCallback);
        // // break;
        // }
        // }
        // });
        // Dialog alertDialog = builder.create();
        // alertDialog.show();
    }

    private OnPickFinishedCallback mPickCallback = new OnPickFinishedCallback() {
        @Override
        public void onPickSuccessed(Bitmap bm) {
            if (bm != null) {
                uploadPic(bm);
            } else {
                Toast.makeText(mContext, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPickFailed() {
            Toast.makeText(mContext, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPickCancel() {
        }
    };

    /**
     * 上传图片到服务器
     * 
     * @param bitmap
     */
    public void uploadPic(final Bitmap bitmap) {
        ((DriverMainActivity) mContext).showLoadingDialog();
        UploadPicAPI api = new UploadPicAPI(bitmap);
        new WisdomCityHttpResponseHandler(api, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                if (response.status == BasicResponse.SUCCESS) {
                    UploadPicAPIResponse r = (UploadPicAPIResponse) response;
                    mPhoto = r.mPhoto;
                    ImageLoader.getInstance().loadImage(WisdomCityRestClientParameterImpl.getUrl() + mPhoto,
                            GlobalModel.getInst().mDisplayOptions, new ImageLoadingListener() {

                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                                    mSignPhoto.setImageBitmap(loadedImage);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
                                }
                            });
                    ((DriverMainActivity) mContext).dismissLoadingDialog();
                } else {
                    Toast.makeText(mContext, R.string.edit_photo_upload_faild, Toast.LENGTH_SHORT).show();
                    ((DriverMainActivity) mContext).dismissLoadingDialog();
                }
            }
        });
        WisdomCityRestClient.execute(api);
    }

    /**
     * 照片获取成功以后
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mImageUtil != null) {
            mImageUtil.onActivityResult(requestCode, resultCode, data);
        }
    }
}
