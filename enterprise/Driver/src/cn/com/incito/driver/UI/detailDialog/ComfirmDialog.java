
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.ComfirmCallback;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 确认对话框
 * 
 * @description 确认对话框
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class ComfirmDialog extends Dialog {

    private Context context;

    private Button bt_yes;

    private Button bt_no;

    private TextView confirm_text;

    private String contentText;

    private ComfirmCallback callback;

    private int position = 0;

    public ComfirmDialog(Context context, int position, ComfirmCallback callback, String contentText) {
        super(context, R.style.CustomerDialog);
        this.context = context;
        this.position = position;
        this.callback = callback;
        this.contentText = contentText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_comfirm, null);
        setContentView(view);
        confirm_text = (TextView) view.findViewById(R.id.tv_confirm_text);
        confirm_text.setText(contentText);
        bt_yes = (Button) view.findViewById(R.id.btn_yes);
        bt_no = (Button) view.findViewById(R.id.btn_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onOperate(position);
                ComfirmDialog.this.dismiss();
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ComfirmDialog.this.dismiss();
            }
        });
    }
}
