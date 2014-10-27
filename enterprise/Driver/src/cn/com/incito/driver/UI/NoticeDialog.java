
package cn.com.incito.driver.UI;

import cn.com.incito.driver.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeDialog extends Dialog implements android.view.View.OnClickListener {
    public static final int SUCCESS_FLAG = 0;

    public static final int FAILED_FLAG = 1;

    private TextView promptContent;

    private TextView prompt_tag;

    String msg;

    Context activity;

    private ImageView promptImage;

    private int mFlag = -1;

    private android.view.View.OnClickListener mComfirnListener;

    private android.view.View.OnClickListener mInfoListener;

    private ImageButton mClosebtn;

    public android.view.View.OnClickListener getComfirnListener() {
        return mComfirnListener;
    }

    public void setComfirnListener(android.view.View.OnClickListener mComfirnListener) {
        this.mComfirnListener = mComfirnListener;
    }

    public android.view.View.OnClickListener getInfoListener() {
        return mInfoListener;
    }

    public void setInfoListener(android.view.View.OnClickListener mInfoListener) {
        this.mInfoListener = mInfoListener;
    }

    public NoticeDialog(Context context) {
        super(context);
    }

    public NoticeDialog(Context context, int theme, String msg) {
        super(context, theme);
        this.activity = context;
        this.msg = msg;
        this.setCanceledOnTouchOutside(true);
    }

    public NoticeDialog(Context context, int theme, String msg, int flag) {
        super(context, theme);
        this.activity = context;
        this.msg = msg;
        this.setCanceledOnTouchOutside(true);
        mFlag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);
        promptImage = (ImageView) findViewById(R.id.prompt_image);
        promptContent = (TextView) findViewById(R.id.prompt_content);
        promptContent.setText(msg);
        promptContent.setText(msg);

        if (mFlag != -1) {
            if (mFlag == SUCCESS_FLAG) {
                promptImage.setBackgroundResource(R.drawable.success_logo);
            } else {
                promptImage.setBackgroundResource(R.drawable.warning_logo);
            }
        }
        mClosebtn = (ImageButton) findViewById(R.id.close_btn);
        mClosebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NoticeDialog.this.dismiss();
            }
        });
    }

    /**
     * 设置提示框的title
     * 
     * @param title
     */
    public void setTitle(String title) {
        prompt_tag.setText(title);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        // case R.id.prompt_btn_comfirn:
        // this.dismiss();
        // break;
        // case R.id.prompt_btn_info:
        // mComfirnListener.onClick(arg0);
        // break;
            default:
                break;
        }
    }

}
