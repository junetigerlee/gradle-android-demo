
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.R;
import cn.com.incito.driver.util.GetResourcesUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 货代星级显示dialog
 * 
 * @description 货代星级显示dialog
 * @author zhanyushuang
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class AgentStarLevelDialog extends Dialog implements OnClickListener {
    private Context mContext;

    private ImageView mStarLevelImg;

    private TextView mStarLevelScoreTv, mGoodsDescriptionTv, mServiceScoreTv, mDealNumTv;

    private ImageButton mCloseBtn;

    private String mStarImg, mStarLevelScore, mGoodsDescription, mServiceScore, mDealNum;

    public AgentStarLevelDialog(Context context) {
        super(context);
    }

    public AgentStarLevelDialog(Context context, int theme) {
        super(context, theme);
    }

    public AgentStarLevelDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * @param context
     * @param theme
     * @param starImg starlevel`s name
     * @param starLevelScore 星级的分数
     * @param goodsDescription 货源描述的分数
     * @param serviceScore 服务质量的分数
     * @param dealNum 成交笔数
     */
    public AgentStarLevelDialog(Context context, int theme, String starImg, String starLevelScore,
            String goodsDescription, String serviceScore, String dealNum) {
        this(context, theme);
        mContext = context;
        mStarImg = starImg;
        mStarLevelScore = starLevelScore;
        mGoodsDescription = goodsDescription;
        mServiceScore = serviceScore;
        mDealNum = dealNum;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_star_level_agent);
        setCanceledOnTouchOutside(true);
        mStarLevelImg = (ImageView) findViewById(R.id.star_img);
        mStarLevelScoreTv = (TextView) findViewById(R.id.star_level_score);
        mGoodsDescriptionTv = (TextView) findViewById(R.id.goods_description);
        mServiceScoreTv = (TextView) findViewById(R.id.service_score);
        mDealNumTv = (TextView) findViewById(R.id.deal_num);
        mCloseBtn = (ImageButton) findViewById(R.id.close_btn);
        mCloseBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_btn) {
            this.dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
        mStarLevelImg.setImageResource(GetResourcesUtil.getDrawableIdentifier(mContext, mStarImg));
        // mStarLevelScoreTv.setText(String.format(mContext.getResources()
        // .getString(R.string.star_level_score), mStarLevelScore));
        // mGoodsDescriptionTv.setText(String.format(mContext.getResources()
        // .getString(R.string.star_level_goodsdiscription_score),
        // mGoodsDescription));
        // mServiceScoreTv.setText(String.format(mContext.getResources()
        // .getString(R.string.star_level_service_score), mServiceScore));
        // mDealNumTv.setText(String
        // .format(mContext.getResources().getString(
        // R.string.star_level_deal_num), mDealNum));
        mStarLevelScoreTv.setText(format(R.string.star_level_score, 0, mStarLevelScore, true));
        mGoodsDescriptionTv.setText(format(R.string.star_level_goodsdiscription_score, 5, mGoodsDescription, true));
        mServiceScoreTv.setText(format(R.string.star_level_service_score, 5, mServiceScore, true));
        mDealNumTv.setText(format(R.string.star_level_deal_num, 4, mDealNum, true));

    }

    public SpannableString format(int id, int start, String str, boolean color) {
        SpannableString spanText = null;
        String strs = String.format(mContext.getResources().getString(id), str);
        if (color) {
            spanText = new SpannableString(strs);
            float[] HSV = new float[3];
            Color.RGBToHSV(255, 85, 0, HSV);
            spanText.setSpan(new ForegroundColorSpan(Color.HSVToColor(HSV)), start,// 0xFF5500
                    start + str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            spanText = new SpannableString(strs);
        }
        return spanText;
    }
}
