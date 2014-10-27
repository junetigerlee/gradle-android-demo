/**
 * 
 */

package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.OperateCallback;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.models.ModelSignInfo;
import cn.com.incito.driver.net.apis.WisdomCityRestClientParameterImpl;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.assist.FailReason;
import cn.com.incito.wisdom.sdk.image.loader.assist.ImageLoadingListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 签收信息查看对话框
 * 
 * @description 签收信息查看对话框
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class SignInfoDialog extends Dialog implements OnClickListener {

    private Context mContext;

    private ModelSignInfo mSignInfo;

    private OperateCallback mCallback;

    private View mConfirmLayout, mRefuseLayout;

    private TextView mSignDate, mRefuseDate, mRefuseMemo;

    private ImageView mSignPhoto;

    private ImageButton mImageButton;

    public void setCallback(OperateCallback mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * @param context
     */
    public SignInfoDialog(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * @param context
     * @param theme
     */
    public SignInfoDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public SignInfoDialog(Context context, int theme, ModelSignInfo signInfo) {
        this(context, theme);
        mSignInfo = signInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signinfo);
        initView();
    }

    private void initView() {
        mConfirmLayout = findViewById(R.id.confirm_layout);
        mRefuseLayout = findViewById(R.id.refuse_layout);
        mSignDate = (TextView) findViewById(R.id.sign_date);
        mRefuseDate = (TextView) findViewById(R.id.refuse_sign_date);
        mRefuseMemo = (TextView) findViewById(R.id.refuse_sign_memo);
        mSignPhoto = (ImageView) findViewById(R.id.signphoto);
        mImageButton = (ImageButton) findViewById(R.id.close_btn);
        mImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SignInfoDialog.this.dismiss();
            }
        });
        if ("0".equals(mSignInfo.getType())) {// 签收
            mConfirmLayout.setVisibility(View.VISIBLE);
            mRefuseLayout.setVisibility(View.GONE);
            mSignDate.setText(String.format(mContext.getResources().getString(R.string.sign_date),
                    mSignInfo.getSigntime()));

            ImageLoader.getInstance().loadImage(WisdomCityRestClientParameterImpl.getUrl() + mSignInfo.getPhoto(),
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
        } else {// 拒签
            mRefuseLayout.setVisibility(View.VISIBLE);
            mConfirmLayout.setVisibility(View.GONE);
            mRefuseDate.setText(String.format(mContext.getResources().getString(R.string.refuse_date),
                    mSignInfo.getSigntime()));
            mRefuseMemo.setText(String.format(mContext.getResources().getString(R.string.refuse_memo),
                    getRefuseSignMemo(mSignInfo.getRefusal(), mSignInfo.getMemo())));
        }
    }

    /**
     * 获取拒签理由
     * 
     * @param memo
     * @return
     */
    private String getRefuseSignMemo(String refusal, String memo) {
        String result = null;
        try {
            int memoCase = Integer.parseInt(refusal);
            switch (memoCase) {
                case 0:
                    result = "货物破损";
                    break;
                case 1:
                    result = "货物丢失";
                    break;
                case 2:
                    result = "退换货";
                    break;
                case 3:
                    result = "延误送货";
                    break;
                case 4:
                    result = "货物混包";
                    break;
                case 5:
                    result = memo;
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return refusal;
        }
        return result;
    }

    @Override
    public void onClick(View arg0) {
    }
}
