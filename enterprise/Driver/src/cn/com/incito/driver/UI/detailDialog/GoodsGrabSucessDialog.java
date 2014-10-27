
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.ComfirmCallback;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 抢货源成功Dialog
 * 
 * @description 抢货源成功Dialogs
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsGrabSucessDialog extends Dialog {

    private Context context;

    private Button bt_yes;

    private Button bt_no;

    private ComfirmCallback callback;

    private int position = 0;

    public GoodsGrabSucessDialog(Context context, ComfirmCallback callback) {
        super(context, R.style.CustomerDialog);
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_goodsgrab_success, null);
        setContentView(view);

        bt_yes = (Button) view.findViewById(R.id.btn_yes);
        bt_no = (Button) view.findViewById(R.id.btn_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != callback) {

                    callback.onOperate(position);
                }
                GoodsGrabSucessDialog.this.dismiss();
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoodsGrabSucessDialog.this.dismiss();
            }
        });
    }
}
