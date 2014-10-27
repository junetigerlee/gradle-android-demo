
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.ComfirmCallback;
import cn.com.incito.wisdom.sdk.utils.IntentUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 忘记密码弹出框
 * 
 * @description 忘记密码弹出框
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class ForgetPasswordServicesDialog extends Dialog {

    private Context context;

    private Button bt_yes;

    private Button bt_no;

    private ComfirmCallback callback;

    private int position = 0;

    public ForgetPasswordServicesDialog(Context context, ComfirmCallback callback) {
        super(context, R.style.CustomerDialog);
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_forgetpassword_services, null);
        setContentView(view);
        view.findViewById(R.id.iv_images_services_tel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                context.startActivity(IntentUtils.getCallItent(Constants.SERVICES_SUPPORT_TEL));
            }

        });
        view.findViewById(R.id.tv_services_tel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                context.startActivity(IntentUtils.getCallItent(Constants.SERVICES_SUPPORT_TEL));
            }

        });
        bt_yes = (Button) view.findViewById(R.id.btn_yes);
        bt_no = (Button) view.findViewById(R.id.btn_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != callback) {

                    callback.onOperate(position);
                }
                ForgetPasswordServicesDialog.this.dismiss();
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ForgetPasswordServicesDialog.this.dismiss();
            }
        });
    }
}
