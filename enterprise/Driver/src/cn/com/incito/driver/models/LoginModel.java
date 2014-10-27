
package cn.com.incito.driver.models;

import cn.com.incito.driver.provider.DataProvider;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * LoginModel
 * 
 * @description LoginModel
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class LoginModel {
    private static final String IS_LOGGINED_IN_KEY = "is_logged_in";

    private static final String DEVICE_PUSH_TOKEN = "device_push_token";

    private static final String DISPLAY_PARTNER_KEY = "display_partner_key";

    private static final String LATEST_LOGIN_UID = "latest_login_uid";

    public static final String LOGIN_MSG = "login_message";

    private static final String LOGIN_CAR_INFO = "carinfo";

    private static final String LOGIN_MSG_CARID = "id";// 就是carid

    private static final String LOGIN_CAR_LEFTWEIGHT = "leftweight";

    private final Context mContext;

    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private boolean mDisplayPartner;

    private int mUid = 0;

    private int mLatestLoginUid = 0;

    public LoginModel(Context context, SharedPreferences sp) {
        super();
        this.mContext = context;
        mPrefs = sp;
        mUid = WisdomCityRestClient.getUid();
        mLatestLoginUid = mPrefs.getInt(LATEST_LOGIN_UID, 0);
        if (mUid != 0 && mLatestLoginUid == 0) {
            mLatestLoginUid = mUid;
            Editor e = mPrefs.edit();
            e.putInt(LATEST_LOGIN_UID, mLatestLoginUid);
            e.commit();
        }
        mIsLoggedIn = mPrefs.getBoolean(IS_LOGGINED_IN_KEY, mUid != 0);
        mDisplayPartner = mPrefs.getBoolean(DISPLAY_PARTNER_KEY, false);
    }

    public final boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public final int getUid() {
        return mUid;
    }

    public final int getLatestLoginUid() {
        return mLatestLoginUid;
    }

    public void setPushDeviceToken(String token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(DEVICE_PUSH_TOKEN, token);
        editor.commit();
    }

    public String getPushDeviceToken() {
        return mPrefs.getString(DEVICE_PUSH_TOKEN, "INVALID TOKEN");
    }

    public final boolean isDisplayPartner() {
        return mDisplayPartner;
    }

    public void setLoginMsg(String strMsg) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(LOGIN_MSG, strMsg);
        editor.commit();
    }

    public String getCarId() {
        String logininfo = mPrefs.getString(LOGIN_MSG, "");
        String carId = null;
        if (!TextUtils.isEmpty(logininfo)) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                JSONObject jsonObj2 = jsonObj.getJSONObject(LOGIN_CAR_INFO);
                carId = jsonObj2.getString(LOGIN_MSG_CARID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return carId;
    }

    public double getLeftload() {
        String logininfo = mPrefs.getString(LOGIN_MSG, "");
        double leftweight = 0.0;
        if (!TextUtils.isEmpty(logininfo)) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                JSONObject jsonObj2 = jsonObj.getJSONObject(LOGIN_CAR_INFO);
                leftweight = jsonObj2.getDouble(LOGIN_CAR_LEFTWEIGHT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return leftweight;
    }

    public double getTotalLoad() {
        String logininfo = mPrefs.getString(LOGIN_MSG, "");
        double totalload = 0.0;
        if (!TextUtils.isEmpty(logininfo)) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                JSONObject jsonObj2 = jsonObj.getJSONObject(LOGIN_CAR_INFO);
                totalload = jsonObj2.getDouble("totalload");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return totalload;
    }

    public String getCarCity() {
        String logininfo = mPrefs.getString(LOGIN_MSG, "");
        String carcity = null;
        if (!TextUtils.isEmpty(logininfo)) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                JSONObject jsonObj2 = jsonObj.getJSONObject(LOGIN_CAR_INFO);
                carcity = jsonObj2.getString("carcity");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return carcity;
    }

    public String getTargetCity() {
        String logininfo = mPrefs.getString(LOGIN_MSG, "");
        String targetcity = null;
        if (!TextUtils.isEmpty(logininfo)) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                JSONObject jsonObj2 = jsonObj.getJSONObject(LOGIN_CAR_INFO);
                targetcity = jsonObj2.getString("targetcity");
                if (!TextUtils.isEmpty(targetcity) && targetcity.equals("全国")) {
                    targetcity = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return targetcity;
    }

    public boolean setLoggedIn(boolean isloggedin) {
        if (isloggedin != mIsLoggedIn) {
            if (!isloggedin) {
                WisdomCityRestClient.clearCookie();
                mContext.getContentResolver().delete(DataProvider.CLEAR_USER_CACHE_DATA_URI, null, null);
                mUid = 0;
                mDisplayPartner = false;
                Editor editor = mPrefs.edit();
                editor.putBoolean(DISPLAY_PARTNER_KEY, false);
                editor.commit();
            }
            mUid = WisdomCityRestClient.getUid();
            Editor e = mPrefs.edit();
            e.putBoolean(IS_LOGGINED_IN_KEY, isloggedin);
            if (isloggedin) {
                mLatestLoginUid = mUid;
                e.putInt(LATEST_LOGIN_UID, mLatestLoginUid);
            }
            this.mIsLoggedIn = isloggedin;
            boolean result = e.commit();
            return result;
        } else {
            return false;
        }
    }

}
