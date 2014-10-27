
package cn.com.incito.driver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.driver.UI.detailDialog.ForgetPasswordServicesDialog;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.driver.net.apis.LoginAPI;
import cn.com.incito.driver.net.apis.LoginAPI.UserAPILoginResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.utils.Md5Utils;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

import com.umeng.analytics.MobclickAgent;

/**
 * 登录
 * 
 * @description 登录
 * @author lizhan
 * @createDate 2014年10月13日
 * @version 1.0
 */
public class LoginActivity extends BasiActivity implements OnClickListener {

    /**
     * 登录帐号信息
     */
    public static final String LOGIN_ACCOUNT = "login_account";

    /**
     * 帐号
     */
    public static final String LOGIN_USERNAME = "username";

    /**
     * 密码
     */
    public static final String LOGIN_PASSWORD = "password";

    /**
     * 是否登录
     */
    public static final String ISLOGIN = "islogin";

    /**
     * 登录验证key
     */
    public static final String LOGIN_VERIFYKEY = "verifykey";

    /**
     * 是否是企业用户
     */
    public static final String ISCOMPANY = "iscompany";

    private EditText mAccountEt, mPasswordEt;

    private TextView mForgetPasswordTv;

    private Button mLoginBtn, mRegisterBtn;

    private ProgressiveDialog mProgressDialog;

    private SharedPreferences mShare;

    private Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressiveDialog(this);
        mShare = DriverApplication.getInstance().getSharedPreferences();
        initViews();
        // 初始化数据
        initData();
    }

    /**
     * initViews
     * 
     * @description initViews
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void initViews() {
        mAccountEt = (EditText) findViewById(R.id.et_account);
        mPasswordEt = (EditText) findViewById(R.id.et_password);
        mForgetPasswordTv = (TextView) findViewById(R.id.tv_forget_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);

        mForgetPasswordTv.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_password:
                ForgetPasswordServicesDialog servicesDialog = new ForgetPasswordServicesDialog(this, null);
                servicesDialog.setCanceledOnTouchOutside(true);
                servicesDialog.show();
                break;
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化数据
     * 
     * @description 初始化数据
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void initData() {
        String loginAccount = mShare.getString(LoginActivity.LOGIN_ACCOUNT, "");
        try {
            if (!TextUtils.isEmpty(loginAccount)) {
                JSONObject loginInfoJson = new JSONObject(loginAccount);
                mAccountEt.setText(loginInfoJson.getString(LOGIN_USERNAME));
                mPasswordEt.setText(loginInfoJson.getString(LOGIN_PASSWORD));
                // 登录
                if (mShare.getBoolean(ISLOGIN, false)) {
                    startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
                    finish();
                } else {
                    login();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     * 
     * @description 登录
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void login() {
        if (TextUtils.isEmpty(mAccountEt.getText().toString())) {
            mAccountEt.setError(getResources().getString(R.string.login_account_error));
            mAccountEt.requestFocus();
        } else if (TextUtils.isEmpty(mPasswordEt.getText().toString())) {
            mPasswordEt.setError(getResources().getString(R.string.login_password_error));
            mPasswordEt.requestFocus();
        } else {
            JSONObject obj = new JSONObject();
            try {
                obj.put(LOGIN_USERNAME, mAccountEt.getText().toString());
                obj.put(LOGIN_PASSWORD, mPasswordEt.getText().toString());
                obj.put(LOGIN_VERIFYKEY, Md5Utils.md5(mPasswordEt.getText().toString() + Constants.SERVER_NAME));
                obj.put(ISCOMPANY, Constants.ISCOMPANY);
                if (NetworkUtils.isNetworkAvaliable(LoginActivity.this)) {
                    // 开始登录
                    mProgressDialog.setMessage(R.string.loging);
                    mProgressDialog.show();
                    LoginAPI loginapi = new LoginAPI(obj.toString());
                    new WisdomCityHttpResponseHandler(loginapi, new APIFinishCallback() {
                        @Override
                        public void OnRemoteApiFinish(BasicResponse response) {

                            if (response.status == BasicResponse.SUCCESS) {
                                LoginAPI.UserAPILoginResponse res = (UserAPILoginResponse) response;
                                // 缓存服务端返回的信息
                                GlobalModel.getInst().mLoginModel.setLoginMsg(res.mLoginMsg);
                                // 缓存帐号信息
                                accountShare();
                                startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
                                mProgressDialog.dismiss();
                                finish();
                            } else {
                                mProgressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, response.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    WisdomCityRestClient.execute(loginapi);
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, R.string.errcode_network_unavailable, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓存帐号信息
     * 
     * @description 缓存帐号信息
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void accountShare() {
        try {
            JSONObject obj = new JSONObject();
            obj.put(LOGIN_USERNAME, mAccountEt.getText().toString());
            obj.put(LOGIN_PASSWORD, mPasswordEt.getText().toString());
            obj.put(LOGIN_VERIFYKEY, Md5Utils.md5(mPasswordEt.getText().toString() + Constants.SERVER_NAME));
            obj.put(ISCOMPANY, Constants.ISCOMPANY);
            mEditor = mShare.edit();
            mEditor.putString(LOGIN_ACCOUNT, obj.toString());
            mEditor.putBoolean(ISLOGIN, true);
            mEditor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.login_on_page_start));
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPageEnd(getResources().getString(R.string.login_on_page_end));
        MobclickAgent.onPause(this);
    }

}
