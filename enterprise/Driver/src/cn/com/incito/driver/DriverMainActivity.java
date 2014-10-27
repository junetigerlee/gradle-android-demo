
package cn.com.incito.driver;

import cn.com.incito.driver.dao.DaoMaster;
import cn.com.incito.driver.dao.DaoMaster.DevOpenHelper;
import cn.com.incito.driver.provider.DataProvider;
import cn.com.incito.driver.push.baidu.Utils;
import cn.com.incito.driver.service.LocationUploadService;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.util.verify.BNKeyVerifyListener;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * 主界面activity
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月13日
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class DriverMainActivity extends FragmentActivity {

    final static String TAG = DriverMainActivity.class.getSimpleName();

    public static final String INTERNAL_BROADCAST_RECEIVER_CACHE_CLEARED = "driver.intent.action.cache_cleared";

    private NotificationManager notificationManager = null;

    public RadioGroup mTabs;

    private RadioButton mHome, mBorad, mOrders, mPublish;

    private Fragment mContent;

    private ProgressiveDialog mProgressDialog;

    public int order_status = 0;

    private int position = 0;

    public HashMap<String, String> boardSearch;

    private SharedPreferences mShare;

    // 01 打开pad可抢货源页面 02 打开pad已抢货源页面
    public String pushMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShare = DriverApplication.getInstance().getSharedPreferences();
        // 验证是否登录过
        validateAccountData();
        DriverApplication app = (DriverApplication) this.getApplication();

        setContentView(R.layout.activity_main);
        initViews();
        // start location upload service
        startService(new Intent(this, LocationUploadService.class));
        initBaiduNav();

        // initBaiduPush();

        processExtraData();
    }

    /**
     * 出来接受到云推送的消息
     * 
     * @description
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void processExtraData() {

        Intent intent = getIntent();
        String panelNo = intent.getStringExtra("panelNo");
        if (null != panelNo) {
            toggleToGrabGoods();
        }

    }

    /**
     * 初始化百度云推送
     * 
     * @description 初始化百度云推送
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void initBaiduPush() {

        Utils.logStringCache = Utils.getLogText(getApplicationContext());

        Resources resource = this.getResources();
        String pkgName = this.getPackageName();

        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        // 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
        if (!Utils.hasBind(getApplicationContext())) {
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                    Utils.getMetaValue(this, "api_key"));
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());
        }

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        // CustomPushNotificationBuilder cBuilder = new
        // CustomPushNotificationBuilder(
        // getApplicationContext(), resource.getIdentifier(
        // "notification_custom_builder", "layout", pkgName),
        // resource.getIdentifier("notification_icon", "id", pkgName),
        // resource.getIdentifier("notification_title", "id", pkgName),
        // resource.getIdentifier("notification_text", "id", pkgName));
        // cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        // cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
        // | Notification.DEFAULT_VIBRATE);
        // cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        // cBuilder.setLayoutDrawable(resource.getIdentifier(
        // "simple_notification_icon", "drawable", pkgName));
        // PushManager.setNotificationBuilder(this, 1, cBuilder);
        //

        // 以apikey的方式绑定
        initWithApiKey();
        // 设置标签,以英文逗号隔开
        // setTags();

    }

    /**
     * 以apikey的方式绑定
     * 
     * @description 以apikey的方式绑定
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void initWithApiKey() {
        // Push: 无账号初始化，用api key绑定
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(this, "api_key"));
    }

    /**
     * 设置标签,以英文逗号隔开
     * 
     * @description 设置标签,以英文逗号隔开
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void setTags() {
        // Push: 设置tag调用方式
        List<String> tags = Utils.getTagsList(Constants.BAIDU_PUSH_TAG);
        PushManager.setTags(getApplicationContext(), tags);
    }

    /**
     * 验证是否登录过
     * 
     * @description 验证是否登录过
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void validateAccountData() {
        Log.d(TAG, "validateAccountData()");
        String loginAccount = mShare.getString(LoginActivity.LOGIN_ACCOUNT, "");
        if (!TextUtils.isEmpty(loginAccount)) {
            Log.d(TAG, "has account");
            if (!mShare.getBoolean(LoginActivity.ISLOGIN, false)) {
                Log.d(TAG, "no account");
                startActivity(new Intent(DriverMainActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            Log.d(TAG, "no account");
            startActivity(new Intent(DriverMainActivity.this, LoginActivity.class));
            finish();
        }
    }

    // private final static String ACCESS_KEY = "IzxdEkvOP47XUjcoquyl38X0";
    private boolean mIsEngineInitSuccess = false;

    /**
     * 初始化百度导航
     * 
     * @description 初始化百度导航
     * @author lizhan
     * @createDate 2014年10月15日
     */
    public void initBaiduNav() {
        // 初始化导航引擎
        if (Constants.RELEASE_SERVER) {
            BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener,
                    Constants.BAIDU_LBS_ACCESS_KEY_REALEASE, mKeyVerifyListener);
        } else {
            BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener,
                    Constants.BAIDU_LBS_ACCESS_KEY, mKeyVerifyListener);
        }

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
        public void engineInitSuccess() {
            mIsEngineInitSuccess = true;
        }

        public void engineInitStart() {
        }

        public void engineInitFail() {
            Toast.makeText(DriverMainActivity.this, getResources().getString(R.string.main_baidu_nav_initfail),
                    Toast.LENGTH_SHORT).show();
        }
    };

    private BNKeyVerifyListener mKeyVerifyListener = new BNKeyVerifyListener() {

        @Override
        public void onVerifySucc() {
            // TODO Auto-generated method stub
            // Toast.makeText(WisdomCityMainActivity.this, "key校验成功",
            // Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerifyFailed(int arg0, String arg1) {
            // TODO Auto-generated method stub
            // Toast.makeText(WisdomCityMainActivity.this, "key校验失败",
            // Toast.LENGTH_LONG).show();
        }
    };

    // /**
    // * 从SD卡导入离线地图安装包
    // *
    // * @param view
    // */
    // public void importFromSDCard() {
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    //
    // System.out.println("开始导入");
    // int num = mOffline.scan();
    // System.out.println("导入完成");
    // String msg = "";
    // if (num == 0) {
    // msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
    // } else {
    // msg = String.format("成功导入 %d 个离线包，可以在下载管理查看", num);
    // }
    // System.out.println("msg = " + msg);
    // }
    // }).start();

    // }

    /**
     * initViews
     * 
     * @description initViews
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void initViews() {

        mTabs = (RadioGroup) findViewById(R.id.menus_radiogroup);
        mTabs.setVisibility(View.GONE);

        switchContent(Constants.TAG_MENU_MY_ORDERS);

        // mTabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(RadioGroup group, int checkedId) {
        // switch (checkedId) {
        // case R.id.menu_home:// 首页
        // switchContent(Constants.TAG_MENU_HOME);
        // position = R.id.menu_home;
        // break;
        // case R.id.menu_board:// 信息板
        // // switchContent(Constants.TAG_MENU_BOARD);
        // // position = R.id.menu_board;
        //
        // checkOrders();
        //
        // break;
        // // case R.id.menu_rim:// 周边货源
        // // switchContent(Constants.TAG_MENU_RIM);
        // // break;
        // case R.id.menu_my_orders:// 我的订单
        //
        // checkOrders();
        // // switchContent(Constants.TAG_MENU_MY_ORDERS);
        // position = R.id.menu_my_orders;
        // break;
        // case R.id.menu_publish:// 发布运力
        // switchContent(Constants.TAG_MENU_PUBLISH);
        // position = R.id.menu_publish;
        // break;
        // // case R.id.menu_games:// 娱乐
        // // switchContent(Constants.TAG_MENU_GAMES);
        // // break;
        // // case R.id.menu_navigation:// 全屏导航
        // // // com.baidu.BaiduMap
        // // Intent i = getPackageManager().getLaunchIntentForPackage(
        // // "com.baidu.BaiduMap");
        // // startActivity(i);
        // // break;
        // default:
        // break;
        // }
        // }
        // });
        // mTabs.check(R.id.menu_board);
        //
        // //
        // ((RadioButton)mTabs.findViewById(R.id.menu_navigation)).setEnabled(false);
        // mTabs.findViewById(R.id.menu_navigation).setOnClickListener(
        // new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // // TODO Auto-generated method stub
        // PackageManager packageManager = DriverMainActivity.this
        // .getPackageManager();
        // Intent intent = new Intent();
        // try {
        // intent = packageManager
        // .getLaunchIntentForPackage("com.baidu.navi");
        // } catch (Exception e) {
        // Toast.makeText(DriverMainActivity.this,
        // "请先安装百度导航应用", Toast.LENGTH_SHORT).show();
        // }
        // startActivity(intent);
        // }
        // });
        //
        // mHome = (RadioButton) findViewById(R.id.menu_home);
        // mBorad = (RadioButton) findViewById(R.id.menu_board);
        // mOrders = (RadioButton) findViewById(R.id.menu_my_orders);
        // mPublish = (RadioButton) findViewById(R.id.menu_publish);

        // Button button=(Button) findViewById(R.id.test);
        // final BadgeView badge = new BadgeView(this, button);
        // badge.setText("1");
        // badge.show();
    }

    /**
     * 切换tab
     * 
     * @description 切换tab
     * @author lizhan
     * @createDate 2014年10月15日
     * @param tagName
     */
    protected void switchContent(String tagName) {
        int index = -1;
        for (int i = 0; i < Constants.TAGS.length; i++) {
            if (Constants.TAGS[i].equals(tagName)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return;
        }
        switchContent(index, false);
    }

    private void switchContent(int position, boolean isMain) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TAGS[position]);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mContent != null) {
            ft.detach(mContent);
        }
        if (fragment == null) {
            fragment = Fragment.instantiate(this, Constants.CLASSES[position].getName());
            ft.add(R.id.content_frame, fragment, Constants.TAGS[position]);
        } else {
            ft.attach(fragment);
        }
        mContent = fragment;
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void toggleContent(int resId) {
        position = resId;
        switch (resId) {
            case R.id.payorder_linearLayout:// 待付款
                // order_status = 1;
                mOrders.setChecked(true);
                break;
            case R.id.pickingorder_linearLayout:// 待配货
                // order_status = 2;
                mOrders.setChecked(true);
                break;
            case R.id.signorder_linearLayout:// 待签收
                // order_status = 3;
                mOrders.setChecked(true);
                break;
            case R.id.evelorder_linearLayout:// 待评价
                // order_status = 4;
                mOrders.setChecked(true);
                break;
            case R.id.cancelorder_linearLayout:// 已取消
                // order_status = 5;
                mOrders.setChecked(true);
                break;
            case R.id.publish_info:// 发布运力
                mPublish.setChecked(true);
                break;
            case R.id.agents_info:// 与我交易最多的货代
                mBorad.setChecked(true);
                break;
            case R.id.today_goods:// 今日新增货源
                mBorad.setChecked(true);
                break;
            case R.id.all_goods:// 平台累计货源
                mBorad.setChecked(true);
                break;
            default:
                break;
        }
    }

    /**
     * 
     */
    private void checkOrders() {
        if (position == R.id.payorder_linearLayout) {
            order_status = 1;
        } else if (position == R.id.pickingorder_linearLayout) {
            order_status = 2;
        } else if (position == R.id.signorder_linearLayout) {
            order_status = 3;
        } else if (position == R.id.evelorder_linearLayout) {
            order_status = 4;
        } else if (position == R.id.cancelorder_linearLayout) {
            order_status = 5;
        } else {
            order_status = 0;
        }
        switchContent(Constants.TAG_MENU_MY_ORDERS);

    }

    @Override
    protected void onDestroy() {
        // mLocClient.stop();
        super.onDestroy();
        // stop location upload service
        stopService(new Intent(this, LocationUploadService.class));
        Log.i(TAG, "ondestroy..");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mContent.getClass().getSimpleName().equals("")) {
                return true;
            } else {

                // 退出和切换用户对话框
                showExitDialog();

                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed()....");
        super.onBackPressed();
    }

    /**
     * 在状态栏显示通知
     * 
     * @param tip 通知提醒标题
     * @param title 通知标题
     * @param message 通知内容
     */
    @SuppressWarnings({
        "unused"
    })
    private void showNotification(String tip, String title, String message) {
        wakelockAndLight();
        // 创建一个NotificationManager的引用
        if (null == notificationManager) {
            notificationManager = (NotificationManager) this
                    .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        }

        // 定义Notification的各种属性
        Notification notification = new Notification(R.drawable.ic_launcher, tip, System.currentTimeMillis());
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知清除，经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 5000;

        // 设置通知的事件消息
        CharSequence contentTitle = title; // 通知栏标题
        CharSequence contentText = message; // 通知栏内容
        Intent intent = new Intent(DriverMainActivity.this, DriverMainActivity.class); // 点击该通知后要跳转的Activity

        PendingIntent contentItent = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);

        // 把Notification传递给NotificationManager
        notificationManager.notify(0, notification);

    }

    /**
     * 点亮屏幕，并且解锁
     */
    private void wakelockAndLight() {
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        // 得到键盘锁管理器对象
        KeyguardLock kl = km.newKeyguardLock("unLock");
        // 参数是LogCat里用的Tag
        kl.disableKeyguard(); // 解锁
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);// 获取电源管理器对象
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是LogCat里用的Tag
        wl.acquire();// 点亮屏幕
        wl.release();// 释放
    }

    public void showLoadingDialog() {
        if (isFinishing())
            return;
        if (mProgressDialog == null) {
            ProgressiveDialog d = new ProgressiveDialog(this);
            d.setMessage(R.string.loading);
            mProgressDialog = d;
        }
        mProgressDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContent.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 退出和切换用户对话框
     * 
     * @description 退出和切换用户对话框
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void showExitDialog() {
        Log.i(TAG, "showExitDialog()....");
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.main_exit_dialog_title))
                .setMessage(getResources().getString(R.string.main_exit_dialog_message))
                .setPositiveButton(getResources().getString(R.string.main_exit_dialog_positive_btn),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // 切换用户
                                toggleUser();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.main_exit_dialog_negative_btn),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // 退出
                                exit();
                            }
                        }).show();
    }

    /**
     * 退出
     */
    private void exit() {
        Log.i(TAG, "exit()....");
        Log.i(TAG, "finish()....");
        this.finish();
    }

    /**
     * 切换用户
     * 
     * @description 切换用户
     * @author lizhan
     * @createDate 2014年10月15日
     */
    private void toggleUser() {
        Log.i(TAG, "toggleUser()....");
        // 清除SharedPreferences 产生的数据
        SharedPreferences mSharePre = DriverApplication.getInstance().getSharedPreferences();
        Editor mEditor = mSharePre.edit();
        mEditor.clear();
        mEditor.commit();
        Log.i(TAG, "LoginActivity....");

        // 清空本地数据库数据

        DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), DataProvider.DATABASE_NAME, null);
        SQLiteDatabase mDB = mHelper.getWritableDatabase();
        mHelper.onUpgrade(mDB, mDB.getVersion(), DaoMaster.SCHEMA_VERSION);

        this.finish();
        startActivity(new Intent(DriverMainActivity.this, LoginActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPause(this);

        initBaiduPush();
    }

    /**
     * 切换到我的订单
     * 
     * @description 切换到我的订单
     * @author lizhan
     * @createDate 2014年10月15日
     */
    public void toggleToMyOrder() {
        switchContent(Constants.TAG_MENU_MY_ORDERS);
    }

    /**
     * 切换到抢货源
     * 
     * @description 切换到抢货源
     * @author lizhan
     * @createDate 2014年10月15日
     */
    public void toggleToGrabGoods() {
        switchContent(Constants.TAG_GOODS_GRAG);
    }
}
