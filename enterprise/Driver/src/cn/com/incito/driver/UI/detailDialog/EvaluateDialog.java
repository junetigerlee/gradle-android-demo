/**
 * 
 */

package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.R;
import cn.com.incito.driver.UI.NoticeDialog;
import cn.com.incito.driver.dao.Comment;
import cn.com.incito.driver.listeners.OperateCallback;
import cn.com.incito.driver.net.apis.orders.EvaluateAPI;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 评价对话框
 * 
 * @description 评价对话框
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class EvaluateDialog extends Dialog implements OnClickListener {

    private Context mContext;

    /** 评价 */
    public final static int FLAG_EVALUATE = 0;

    /** 查看评价信息 */
    public final static int FLAG_EVALUATE_INFO = 1;

    // 评价相关文本对象
    private TextView mDescription_grade, mGoodsSrouceDesc, mService_grade, mAgentServiceDesc, mComment_txt;

    private RatingBar mDescriptionRatingBar, mServiceRatingBar;

    private EditText mComment_edit;

    // 对方给的评价
    private TextView mDeliverSpeedGrade, mDeliverSpeedDesc, mServiceMannerGrade, mServiceDesc, mReleaseDateGrade,
            mDeliverDateDesc, mReleaseQualityGrade, mDeliverQualityDesc, mCommentRemoteTxt;

    private RatingBar mDeliverSpeedRatingBar, mServiceManRatingBar, mReleaseDateRatingBar, mReleasequalityRatingBar;

    private int evaluateValue = 0;

    private Button mCommentBtn, mCancelBtn;

    private ImageButton mCloseBtn;

    private NoticeDialog mNoticeDialog;// 弹出dialog提示用户，按钮的操作交互

    private String orderId;

    private String orderNo;

    private String agentId;

    private OperateCallback mCallback;

    private Comment mCarcomment, mAgentcomment;

    private int mFlag = FLAG_EVALUATE;

    private TextView agentEvaluateTitle;

    public void setCallback(OperateCallback mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * @param context
     */
    public EvaluateDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public EvaluateDialog(Context context, int theme) {
        super(context, theme);
    }

    public EvaluateDialog(Context context, int theme, String orderId, String agentId) {
        super(context, theme);
        this.mContext = context;
        this.orderId = orderId;
        this.agentId = agentId;
    }

    public EvaluateDialog(Context context, int theme, String orderId, String agentId, Comment carComment,
            Comment agentComment, int flag) {
        this(context, theme, orderId, agentId);
        mCarcomment = carComment;
        mAgentcomment = agentComment;
        mFlag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_evaluate_driver);
        initView();
    }

    private void initView() {
        RelativeLayout dialogLayout = (RelativeLayout) findViewById(R.id.dialog_layout);
        agentEvaluateTitle = (TextView) findViewById(R.id.driver_evaluate_title);
        if (mFlag == FLAG_EVALUATE_INFO) {
            agentEvaluateTitle.setText("我给货代的评价");
        }
        mDescription_grade = (TextView) findViewById(R.id.description_grade);
        mGoodsSrouceDesc = (TextView) findViewById(R.id.goodsource_desc);
        mService_grade = (TextView) findViewById(R.id.service_grade);
        mAgentServiceDesc = (TextView) findViewById(R.id.agent_service_desc);
        mDescriptionRatingBar = (RatingBar) findViewById(R.id.description_ratingBar);
        mDescriptionRatingBar.setStepSize(1);
        mServiceRatingBar = (RatingBar) findViewById(R.id.service_ratingBar);
        mServiceRatingBar.setStepSize(1);

        TextView commentLocalTx = (TextView) findViewById(R.id.comment_local_txt);
        View operateLayout = findViewById(R.id.operate_linear);
        mComment_edit = (EditText) findViewById(R.id.comment_et);
        mCommentBtn = (Button) findViewById(R.id.btn_comment);
        mCommentBtn.setOnClickListener(this);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel);
        mCancelBtn.setOnClickListener(this);
        mCloseBtn = (ImageButton) findViewById(R.id.close_btn);
        mCloseBtn.setOnClickListener(this);
        mDescriptionRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar arg0, float progress, boolean arg2) {
                // if (progress > 0) {
                mDescription_grade.setVisibility(View.VISIBLE);
                // } else {
                // mDescription_grade.setVisibility(View.GONE);
                // }
                if (progress < 1) {
                    mDescription_grade.setText("1.0分");
                    mDescriptionRatingBar.setRating(1);
                } else {
                    mDescription_grade.setText(Float.toString(progress) + "分");
                }

                String[] mArray = mContext.getResources().getStringArray(R.array.deliverSpeed_desc);
                if (arg0.getRating() > 1) {
                    mGoodsSrouceDesc.setText(mArray[Float.valueOf(arg0.getRating()).intValue() - 1]);
                } else {
                    mGoodsSrouceDesc.setText(mArray[0]);
                }
            }
        });
        mServiceRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar arg0, float progress, boolean arg2) {
                // if (progress > 0) {
                mService_grade.setVisibility(View.VISIBLE);
                // } else {
                // mService_grade.setVisibility(View.GONE);
                // }
                if (progress < 1) {
                    mService_grade.setText("1.0分");
                    mServiceRatingBar.setRating(1);
                } else {
                    mService_grade.setText(Float.toString(progress) + "分");
                }

                String[] mArray = mContext.getResources().getStringArray(R.array.agentService_desc);
                if (arg0.getRating() > 1) {
                    mAgentServiceDesc.setText(mArray[Float.valueOf(arg0.getRating()).intValue() - 1]);
                } else {
                    mAgentServiceDesc.setText(mArray[0]);
                }

            }
        });

        View divideLine = findViewById(R.id.divide_line);
        View theOtherEvaluateLayout = findViewById(R.id.the_other_evaluate_layout);
        View noEvaluateTx = findViewById(R.id.no_evaluate_tx);
        View evaluateLayout = findViewById(R.id.evaluate_layout);

        if (mFlag == FLAG_EVALUATE) {
            LayoutParams layoutPaarams = new LayoutParams(LayoutParams.WRAP_CONTENT, 550);
            dialogLayout.setLayoutParams(layoutPaarams);

            commentLocalTx.setVisibility(View.GONE);
            mComment_edit.setVisibility(View.VISIBLE);
            operateLayout.setVisibility(View.VISIBLE);
            divideLine.setVisibility(View.GONE);
            theOtherEvaluateLayout.setVisibility(View.GONE);
        } else {
            commentLocalTx.setVisibility(View.VISIBLE);
            operateLayout.setVisibility(View.GONE);
            mComment_edit.setVisibility(View.GONE);
            divideLine.setVisibility(View.VISIBLE);
            theOtherEvaluateLayout.setVisibility(View.VISIBLE);

            RatingBar[] ratingBars = new RatingBar[] {
                    mDescriptionRatingBar, mServiceRatingBar
            };
            String level = mAgentcomment.getLevel();
            String[] levels = level.split(",");
            setRatingBars(ratingBars, levels);
            if (!TextUtils.isEmpty(mAgentcomment.getContent())) {
                commentLocalTx.setText(String.format(mContext.getResources().getString(R.string.label_evaluate_memo),
                        mAgentcomment.getContent()));
            } else {//未填写评价内容
                commentLocalTx.setText(String.format(mContext.getResources().getString(R.string.label_evaluate_memo),
                        "无"));
            }

            // 对方评价的相关控件
            // TODO 根据服务器信息，如果对方没有评价，则此布局提示对方并未评价
            if (mCarcomment != null) {// 对方评价了
                noEvaluateTx.setVisibility(View.GONE);
                evaluateLayout.setVisibility(View.VISIBLE);
                mDeliverSpeedGrade = (TextView) findViewById(R.id.deliver_speed_grade);
                mDeliverSpeedDesc = (TextView) findViewById(R.id.deliver_desc);
                mServiceMannerGrade = (TextView) findViewById(R.id.service_manner_grade);
                mServiceDesc = (TextView) findViewById(R.id.service_desc);
                mReleaseDateGrade = (TextView) findViewById(R.id.release_date_grade);
                mDeliverDateDesc = (TextView) findViewById(R.id.deliver_date_desc);
                mReleaseQualityGrade = (TextView) findViewById(R.id.release_quality_grade);
                mDeliverQualityDesc = (TextView) findViewById(R.id.deliver_quality_desc);
                mCommentRemoteTxt = (TextView) findViewById(R.id.comment_remote_txt);
                mDeliverSpeedRatingBar = (RatingBar) findViewById(R.id.deliver_speed_ratingBar);
                mServiceManRatingBar = (RatingBar) findViewById(R.id.service_manner_ratingBar);
                mReleaseDateRatingBar = (RatingBar) findViewById(R.id.release_date_ratingBar);
                mReleasequalityRatingBar = (RatingBar) findViewById(R.id.release_quality_ratingBar);

                mDeliverSpeedRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar arg0, float progress, boolean arg2) {
                        // if (progress > 0) {
                        mDeliverSpeedGrade.setVisibility(View.VISIBLE);
                        // } else {
                        // mDeliverSpeedGrade.setVisibility(View.GONE);
                        // }
                        mDeliverSpeedGrade.setText(Float.toString(progress) + "分");

                        String[] mArray = mContext.getResources().getStringArray(R.array.deliverSpeed_desc);
                        if (arg0.getRating() > 1) {
                            mDeliverSpeedDesc.setText(mArray[Float.valueOf(arg0.getRating()).intValue() - 1]);
                        } else {
                            mDeliverSpeedDesc.setText(mArray[0]);
                        }
                    }
                });
                mServiceManRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar arg0, float progress, boolean arg2) {
                        // if (progress > 0) {
                        mServiceMannerGrade.setVisibility(View.VISIBLE);
                        // } else {
                        // mServiceMannerGrade.setVisibility(View.GONE);
                        // }
                        mServiceMannerGrade.setText(Float.toString(progress) + "分");

                        String[] mArray = mContext.getResources().getStringArray(R.array.service_desc);
                        if (arg0.getRating() > 1) {
                            mServiceDesc.setText(mArray[Float.valueOf(arg0.getRating()).intValue() - 1]);
                        } else {
                            mServiceDesc.setText(mArray[0]);
                        }

                    }
                });
                mReleaseDateRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar arg0, float progress, boolean arg2) {
                        // if (progress > 0) {
                        mReleaseDateGrade.setVisibility(View.VISIBLE);
                        // } else {
                        // mReleaseDateGrade.setVisibility(View.GONE);
                        // }
                        mReleaseDateGrade.setText(Float.toString(progress) + "分");

                        String[] mArray = mContext.getResources().getStringArray(R.array.deliverDate_desc);

                        if (arg0.getRating() > 1) {
                            mDeliverDateDesc.setText(mArray[Float.valueOf(arg0.getRating()).intValue() - 1]);
                        } else {
                            mDeliverDateDesc.setText(mArray[0]);
                        }

                    }
                });
                mReleasequalityRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar arg0, float progress, boolean arg2) {
                        // if (progress > 0) {
                        mReleaseQualityGrade.setVisibility(View.VISIBLE);
                        // } else {
                        // mReleaseQualityGrade.setVisibility(View.GONE);
                        // }
                        mReleaseQualityGrade.setText(Float.toString(progress) + "分");

                        String[] mArray = mContext.getResources().getStringArray(R.array.deliverQuality_desc);
                        if (arg0.getRating() > 1) {
                            mDeliverQualityDesc.setText(mArray[Float.valueOf(arg0.getRating()).intValue() - 1]);
                        } else {

                            mDeliverQualityDesc.setText(mArray[0]);
                        }

                    }
                });
                RatingBar[] agentRatingBars = new RatingBar[] {
                        mDeliverSpeedRatingBar, mServiceManRatingBar, mReleaseDateRatingBar, mReleasequalityRatingBar
                };
                String agentLevel = mCarcomment.getLevel();
                String[] agentLevels = agentLevel.split(",");
                setRatingBars(agentRatingBars, agentLevels);
                TextView agentEvaluatTx = (TextView) findViewById(R.id.comment_remote_txt);
                if (null != mCarcomment.getContent() && !TextUtils.isEmpty(mCarcomment.getContent())) {
                    agentEvaluatTx.setText(String.format(mContext.getResources()
                            .getString(R.string.label_evaluate_memo), mCarcomment.getContent()));
                } else {// 未填写评价内容，显示为无
                    // agentEvaluatTx.setText(String.format(
                    // mContext.getResources().getString(
                    // R.string.label_evaluate_memo),
                    // "这个家伙很懒，什么都没有留下！"));
                    agentEvaluatTx.setText(String.format(mContext.getResources()
                            .getString(R.string.label_evaluate_memo), "无"));
                }

            } else {// 货代没有对司机评价，则显示“未评价”
                noEvaluateTx.setVisibility(View.VISIBLE);
                evaluateLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置评分
     * 
     * @param ratingBars
     * @param levels
     */
    protected void setRatingBars(RatingBar[] ratingBars, String[] levels) {
        for (int i = 0; i < levels.length; i++) {
            ratingBars[i].setRating(Float.valueOf(levels[i]));
            ratingBars[i].setIsIndicator(true);
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_comment:

                Comment comment = new Comment();
                comment.setStatus(String.valueOf(evaluateValue));
                comment.setContent(mComment_edit.getText().toString());
                comment.setLevel(mDescriptionRatingBar.getRating() + "," + mServiceRatingBar.getRating());
                EvaluateAPI evaluateapi = new EvaluateAPI(orderId, agentId, comment);
                new WisdomCityHttpResponseHandler(evaluateapi, new APIFinishCallback() {
                    @Override
                    public void OnRemoteApiFinish(BasicResponse response) {
                        dismiss();
                        if (mContext == null) {
                            return;
                        }
                        if (response.status == BasicResponse.SUCCESS) {

                            mNoticeDialog = new NoticeDialog(mContext, R.style.CustomerDialog, "评价成功",
                                    NoticeDialog.SUCCESS_FLAG);
                            mNoticeDialog.show();
                            if (mCallback != null)
                                mCallback.onOperateFinished();
                        } else {
                            mNoticeDialog = new NoticeDialog(mContext, R.style.CustomerDialog, response.msg,
                                    NoticeDialog.FAILED_FLAG);
                            mNoticeDialog.show();
                        }
                    }
                });
                WisdomCityRestClient.execute(evaluateapi);

                break;
            case R.id.btn_cancel:
            case R.id.close_btn:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}
