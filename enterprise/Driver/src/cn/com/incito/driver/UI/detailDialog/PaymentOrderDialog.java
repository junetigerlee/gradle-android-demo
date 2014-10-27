
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.OperateCallback;
import cn.com.incito.driver.net.apis.PayMoneyForCarAPI;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 支付方式
 * 
 * @description 支付方式
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class PaymentOrderDialog extends Dialog implements OnClickListener {

    private Context mContext;

    private Button mPaymentBtn;

    private Button mCancelBtn;

    private TextView mPaymentMoney;

    private RadioGroup mPaymentRadioGroup;

    private RadioGroup mBankGroup;

    private RadioButton mOnlinePaymentRadio, mOffinePaymentRadio, mCcb_radiobutton, mCmbc_radiobutton,
            mAgricultural_radiobutton;

    private View mOnLinePayLayout;

    private String orderId;

    private String paymentMoney;

    private CheckBox mAlipayCheckBox;

    private OperateCallback mCallback;

    public void setCallback(OperateCallback mCallback) {
        this.mCallback = mCallback;
    }

    public PaymentOrderDialog(Context context, int theme, String orderId, String paymentMoney) {
        super(context, theme);
        this.mContext = context;
        this.orderId = orderId;
        this.paymentMoney = paymentMoney;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment_order);
        initView();
    }

    private void initView() {
        mPaymentMoney = (TextView) findViewById(R.id.payment_money);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.alipay_checkbox);
        mPaymentRadioGroup = (RadioGroup) findViewById(R.id.paymentRadioGroup);
        mOnlinePaymentRadio = (RadioButton) findViewById(R.id.onlinePaymentRadio);
        mOffinePaymentRadio = (RadioButton) findViewById(R.id.offlinePaymentRadio);

        mCcb_radiobutton = (RadioButton) findViewById(R.id.ccb_radiobutton);
        mCmbc_radiobutton = (RadioButton) findViewById(R.id.cmbc_radiobutton);
        mAgricultural_radiobutton = (RadioButton) findViewById(R.id.agricultural_radiobutton);

        mBankGroup = (RadioGroup) findViewById(R.id.bank_group);
        mOnLinePayLayout = findViewById(R.id.online_payment_layout);
        mPaymentRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.onlinePaymentRadio:
                        setCheckStatus(true);
                        break;
                    case R.id.offlinePaymentRadio:
                        setCheckStatus(false);
                        break;
                    default:
                        break;
                }
            }

        });
        mOffinePaymentRadio.setChecked(true);
        mBankGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean checked = false;
                switch (checkedId) {
                    case R.id.ccb_radiobutton:
                        checked = mCcb_radiobutton.isChecked();
                        break;
                    case R.id.cmbc_radiobutton:
                        checked = mCmbc_radiobutton.isChecked();
                        break;
                    case R.id.agricultural_radiobutton:
                        checked = mAgricultural_radiobutton.isChecked();
                        break;
                    default:
                        break;
                }
                if (checked) {
                    mAlipayCheckBox.setChecked(false);
                }
            }
        });
        mAlipayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBankGroup.clearCheck();
                }
            }
        });

        mPaymentBtn = (Button) findViewById(R.id.payment_btn);
        mPaymentBtn.setOnClickListener(this);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel);
        mCancelBtn.setOnClickListener(this);

    }

    @Override
    public void show() {
        super.show();
        mPaymentMoney.setText(paymentMoney);
    }

    public String getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(String paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 设置银行支付方式与阿里支付方式按钮状态
     */
    protected void setCheckStatus(boolean enable) {
        for (int i = 0; i < mBankGroup.getChildCount(); i++) {
            mBankGroup.getChildAt(i).setEnabled(enable);
        }
        mAlipayCheckBox.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_btn:

                switch (mPaymentRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.onlinePaymentRadio:
                        // this.dismiss();
                        // PaymentOnlineDialog paymentOnlineDialog = new
                        // PaymentOnlineDialog(
                        // mContext, R.style.CustomerDialog, orderId, paymentMoney);
                        // paymentOnlineDialog.setCallback(new OperateCallback() {
                        //
                        // @Override
                        // public void onReservFinished() {
                        // // TODO Auto-generated method stub
                        //
                        // }
                        //
                        // @Override
                        // public void onOperateFinished() {
                        // mCallback.onOperateFinished();
                        // }
                        //
                        // @Override
                        // public void onOperate(int resId, Map<String, Object> params)
                        // {
                        // // TODO Auto-generated method stub
                        //
                        // }
                        // });
                        // paymentOnlineDialog.show();
                        // break;
                    case R.id.offlinePaymentRadio:
                        // this.dismiss();
                        // PaymentOfflineDialog paymentOfflineDialog = new
                        // PaymentOfflineDialog(
                        // mContext, R.style.CustomerDialog, orderId, paymentMoney);
                        // paymentOfflineDialog.setCallback(new OperateCallback() {
                        //
                        // @Override
                        // public void onReservFinished() {
                        // // TODO Auto-generated method stub
                        //
                        // }
                        //
                        // @Override
                        // public void onOperateFinished() {
                        // mCallback.onOperateFinished();
                        // }
                        //
                        // @Override
                        // public void onOperate(int resId, Map<String, Object> params)
                        // {
                        // // TODO Auto-generated method stub
                        //
                        // }
                        // });
                        // paymentOfflineDialog.show();
                        ((DriverMainActivity) mContext).showLoadingDialog();
                        // PaymentOrdersAPI paymentOrdersAPI = new
                        // PaymentOrdersAPI(orderId);
                        PayMoneyForCarAPI payMoneyForCarAPI = new PayMoneyForCarAPI(orderId);
                        new WisdomCityHttpResponseHandler(payMoneyForCarAPI, new APIFinishCallback() {

                            @Override
                            public void OnRemoteApiFinish(BasicResponse response) {
                                ((DriverMainActivity) mContext).dismissLoadingDialog();
                                if (mContext == null || ((Activity) mContext).isFinishing()) {
                                    return;
                                }
                                if (response.status == BasicResponse.SUCCESS) {
                                    Toast.makeText(mContext, R.string.payment_ok, Toast.LENGTH_SHORT).show();

                                    dismiss();
                                    mCallback.onOperateFinished();
                                } else {
                                    Toast.makeText(mContext, R.string.payment_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        WisdomCityRestClient.execute(payMoneyForCarAPI);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_cancel:
                this.dismiss();
                break;

            default:
                break;
        }
    }

}
