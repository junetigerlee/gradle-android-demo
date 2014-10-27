
package cn.com.incito.driver.UI.detailDialog;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.OperateCallback;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomerRadioGroup;
import cn.com.incito.wisdom.uicomp.widget.radiogroup.CustomerRadioGroup.OnCheckedChangeListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 拒单理由对话框
 * 
 * @description 拒单理由对话框
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class RefuseOrderDialog extends Dialog {

    private Context context;

    private Button bt_yes;

    private Button bt_no;

    private TextView confirm_text;

    private String contentText;

    private OperateCallback operateCallback;

    private int position = 0;

    private CustomerRadioGroup resufeOrderGroup;

    private EditText etOtherInfo;

    private String refusalReason = "";

    private String refusalMemo = "";

    private int mCheckedId = R.id.rbtn_order_reason_maintenance;

    public RefuseOrderDialog(Context context, int position, OperateCallback operateCallback, String contentText) {
        super(context, R.style.CustomerDialog);
        this.context = context;
        this.position = position;
        this.operateCallback = operateCallback;
        this.contentText = contentText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_refuse_order_reason, null);
        setContentView(view);
        confirm_text = (TextView) view.findViewById(R.id.tv_confirm_text);
        // confirm_text.setText(contentText);
        resufeOrderGroup = (CustomerRadioGroup) view.findViewById(R.id.rgb_refuse_order_reason);
        etOtherInfo = (EditText) view.findViewById(R.id.et_other_info);
        etOtherInfo.setEnabled(false);
        etOtherInfo.setFocusable(false);
        etOtherInfo.setFocusableInTouchMode(false);
        resufeOrderGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CustomerRadioGroup group, int checkedId) {
                onCheck(checkedId);
                mCheckedId = checkedId;

            }
        });
        resufeOrderGroup.check(R.id.rbtn_order_reason_maintenance);
        bt_yes = (Button) view.findViewById(R.id.btn_yes);
        bt_no = (Button) view.findViewById(R.id.btn_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onCheck(mCheckedId);
                refusalMemo = etOtherInfo.getText().toString();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(Constants.REFUSE_REASON_REFUSALREASON, refusalReason);
                params.put(Constants.REFUSE_REASON_REFUSALMEMO, refusalMemo);
                operateCallback.onOperate(position, params);
                RefuseOrderDialog.this.dismiss();
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RefuseOrderDialog.this.dismiss();
            }
        });
    }

    private void onCheck(int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_order_reason_maintenance:
                refusalReason = Constants.REFUSE_ORDER_REASON_MAINTENANCE;
                etOtherInfo.setVisibility(View.INVISIBLE);
                etOtherInfo.setText("");
                break;
            case R.id.rbtn_order_reason_sick:
                refusalReason = Constants.REFUSE_ORDER_REASON_SICK;
                etOtherInfo.setVisibility(View.INVISIBLE);
                etOtherInfo.setText("");
                break;
            case R.id.rbtn_order_reason_unreasonable:
                refusalReason = Constants.REFUSE_ORDER_REASON_UNREASONABLE;
                etOtherInfo.setVisibility(View.INVISIBLE);
                etOtherInfo.setText("");
                break;
            case R.id.rbtn_order_reason_unfamiliar:
                refusalReason = Constants.REFUSE_ORDER_REASON_UNFAMILIAR;
                etOtherInfo.setVisibility(View.INVISIBLE);
                etOtherInfo.setText("");
                break;
            case R.id.rbtn_order_reason_other:
                refusalReason = Constants.REFUSE_ORDER_REASON_OTHER;
                etOtherInfo.setVisibility(View.VISIBLE);
                etOtherInfo.setEnabled(true);
                etOtherInfo.setFocusable(true);
                etOtherInfo.setFocusableInTouchMode(true);
                break;

            default:
                break;
        }
    }
}
