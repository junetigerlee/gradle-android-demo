
package cn.com.incito.driver;

import cn.com.incito.driver.fragments.GamesFragment;
import cn.com.incito.driver.fragments.goods.GoodsAvailableFragment;
import cn.com.incito.driver.fragments.goods.GoodsHasAvailableFragment;
import cn.com.incito.driver.fragments.goods.GrabGoodsFragment;
import cn.com.incito.driver.fragments.infocenter.MyInfoFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersAllFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersCanceledFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersDistributionFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersEvaluatedFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersObligationFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersReceivedFragment;
import cn.com.incito.driver.fragments.orders.MyOrdersSignFragment;

/**
 * 全局常量类
 * 
 * @description 全局常量类
 * @author lizhan
 * @createDate 2014年10月13日
 * @version 1.0
 */
public class Constants {
    /** 如果是false的话，则是测试地址 */
    public static final boolean RELEASE_SERVER = false;

    public static final boolean RELEASE_CLIENT = true;

    public static final int API_VERSION = 1;

    /**
     * baidu lbs api key
     */
    public static final String BAIDU_LBS_ACCESS_KEY = "UvqNVGewEwTMd6NyRHEU3G4n";

    /**
     * baidu lbs api key realese
     */
    public static final String BAIDU_LBS_ACCESS_KEY_REALEASE = "Oo65GSGHiA99TGGnsj73RgHy";

    public static final String BAIDU_PUSH_USER_TYPE = "1";// 1 司机，2 货代

    public static String BAIDU_PUSH_USER_TAG = "";

    public static final String BAIDU_PUSH_TAG = "wl-sj";

    public static final String WISDOMCITY_IAMGE_CACHE_SDCARD_PATH = "driver/image/cache";

    public static final String EMPTY_STR = "";

    public static final String SERVER_NAME = "www.isoftstone.com";

    // 测试服务器ip
    // public static final String TEST_SERVER_IP = "192.168.51.140";// 潜海龙
    // public static final String TEST_SERVER_IP = "192.168.51.131";// 翟光涛
    // public static final String TEST_SERVER_IP = "192.168.51.25";// 刘婷婷
    public static final String TEST_SERVER_IP = "192.168.51.44";// 卢芝锐

    // public static final String TEST_SERVER_IP = "192.168.11.105";//
    // public static final String TEST_SERVER_IP = "192.168.11.136";//enterprise
    // 8082
    // public static final String TEST_SERVER_IP = "192.168.11.137";
    // public static final String TEST_SERVER_IP = "192.168.11.151";
    //	public static final String TEST_SERVER_IP = "192.168.11.184";

    /** 正式域名 */
    // public static final String API_SERVER_NAME = "58.51.36.100";
    public static final String API_SERVER_NAME = "wl-app.incito.com.cn";

    // public static final String API_SERVER_NAME = "192.168.11.151";
    // public static final String API_SERVER_NAME = "221.233.60.163";

    public static final String USER_AUTH_TYPE = "1";// 0，货代 1，司机

    public static final String ISCOMPANY = "0";// 0,企业用户 ，1，标准用户

    /** WisdomCityRestClient 访问API是否输出日志 */
    public static final boolean REST_CLIENT_LOG_SENABLE_LOGGING = true;

    /** 全部货源 */
    public final static String GOODS_ALL = "0";

    /** 带搜索条件的货源 */
    public final static String GOODS_SEARCH = "1";

    /** 货代认证 */
    public final static int AGENT_AUTH = 0;

    /** 未认证 */
    public final static int AGENT_NO_AUTH = 1;

    /** 发布运力-去程 */
    public final static int PUBLISH_TYPE_GO = 0;

    /** 发布运力-返程 */
    public final static int PUBLISH_TYPE_BACK = 1;

    /** 发布运力-已发布 */
    public final static int PUBLISH_STATUS_P = 0;

    /** 发布运力-未发布 */
    public final static int PUBLISH_STATUS_N = 1;

    /** 货物类型-不限 */
    public final static int GOODS_TYPE_NORMAL = 0;

    /** 货物类型-重货 */
    public final static int GOODS_TYPE_HEAVY_CARGO = 1;

    /** 货物类型-轻货 */
    public final static int GOODS_TYPE_LIGHT_CARGO = 2;

    /** 货物类型-设备 */
    public final static int GOODS_TYPE_DEVICES = 3;

    /** 结算类型-货到付款 */
    public final static int GOODS_CASHTYPE_CASH_ON_DELIVERY = 0;

    /** 结算类型-回单付款 */
    public final static int GOODS_CASHTYPE_CASH_ON_RECEIPT = 1;

    /** 回单要求-无 */
    public final static int GOODS_BACKORDER_NONE = 0;

    /** 回单要求-原件 */
    public final static int GOODS_BACKORDER_ORIGINAL = 1;

    /** 回单要求-扫描件 */
    public final static int GOODS_BACKORDER_SCANNING_COPY = 2;

    /** 回单要求-传真 */
    public final static int GOODS_BACKORDER_FAX = 3;

    /** 货源状态 新货源 */
    public final static int GOODS_STATUS_NEW = 0;

    /** 货源状态 已预订 */
    public final static int GOODS_STATUS_RESOVED = 1;

    /** 货源状态 已成交 */
    public final static int GOODS_STATUS_COMPLETED = 2;

    /** 货源状态 无响应 */
    public final static int GOODS_STATUS_NO_RESPONSE = 3;

    /** 订单状态_新订单 */
    public final static int ORDERS_STATUS_NEW = 0;

    /** 订单状态_已付款 */
    public final static int ORDERS_STATUS_PAID = 1;

    /** 订单状态_已取消 */
    public final static int ORDERS_STATUS_CANCEL = 2;

    /** 订单状态_已评价 */
    public final static int ORDERS_STATUS_EVALUATED = 3;

    /** 订单状态_付款待确认 */
    public final static int ORDERS_STATUS_PAYMENT = 4;

    /** 订单状态_司机评价 */
    public final static int ORDERS_STATUS_DRIVER_EVALUATED = 5;

    /** 订单状态_货代评价 */
    public final static int ORDERS_STATUS_AGENT_EVALUATED = 6;

    /** 运单状态_待揽货 */
    public final static int TASKS_STATUS_PICK_GOODS = 0;

    /** 运单状态_已揽货 */
    public final static int TASKS_STATUS_ALREADY_PICK_GOODS = 1;

    /** 运单状态_离开揽货地 */
    public final static int TASKS_STATUS_LEAVE_ORIGINCITY = 2;

    /** 运单状态_运输中 */
    public final static int TASKS_STATUS_TRANSPORTING = 3;

    /** 运单状态_到收货地址 */
    public final static int TASKS_STATUS_ARRIVED_RECEIPTCITY = 4;

    /** 运单状态_已签收 */
    public final static int TASKS_STATUS_SIGN_IN = 5;

    /** 运单状态_已拒签 */
    public final static int TASKS_STATUS_REFUSE = 6;

    /** 评价类型——货代评价 */
    public final static int EVALUATE_TYPE_AGENT_EVALUATE = 0;

    /** 评价类型——司机评价 */
    public final static int EVALUATE_TYPE_DRIVER_EVALUATE = 1;

    /**
     * 位置定时上传时间(半小时)
     */
    public static final int LOCATION_UPLOAD_TIME = 1000 * 60 * 30;

    /**
     * 好评
     */
    public static final int GOOD_EVALUATE = 0;

    /**
     * 中评
     */
    public static final int NORMAL_EVALUATE = 1;

    /**
     * 差评
     */
    public static final int BAD_EVALUATE = 2;

    /** 全部订单 */
    public static final int ORDER_TYPE_ALL = 0;

    /** 待付款 */
    public static final int ORDER_TYPE_OBLIGATION = 1;

    /** 待配货 */
    public static final int ORDER_TYPE_DISTRIBUTION = 2;

    /** 待签收 */
    public static final int ORDER_TYPE_SIGN = 3;

    /** 待评价 */
    public static final int ORDER_TYPE_EVALUATE = 4;

    /** 退款订单 */
    public static final int ORDER_TYPE_REIMBURSE = 5;

    /** 拒绝退款 */
    public static final int ORDER_TYPE_REFUSE = 6;

    /** 已取消 */
    public static final int ORDER_TYPE_CANCELED = 7;

    /** 已预订 */
    public static final int ORDER_TYPE_RESERVED = 8;

    /** 待收款 */
    public static final int ORDER_TYPE_RECEIPTS = 9;

    /** 3月内订单 */
    public static final int ORDER_WITHIN_THREE_MONTHS = 0;

    /** 3月以前订单 */
    public static final int ORDER_THREE_MONTHS_AWAY = 1;

    // 我的订单列表里面显示的订单状态

    /** 已指派 */
    public static final int ORDER_STATUS_APPOINT = -1;

    /** 新订单 */
    public static final int ORDER_STATUS_NEW = 0;

    /** 司机已付款 */
    public static final int ORDER_STATUS_PAIED = 1;

    /** 订单取消 */
    public static final int ORDER_STATUS_CANCELED = 2;

    /** 货代已经确定收款 */
    public static final int ORDER_STATUS_CONFIRMRECEIPT = 3;

    /** 已揽货 */
    public static final int ORDER_STATUS_CANVASSING = 4;

    /** 执行中 */
    public static final int ORDER_STATUS_EXECUTING = 5;

    /** 到达目的地 */
    public static final int ORDER_STATUS_ARRIVED = 6;

    /** 司机签收 */
    public static final int ORDER_STATUS_SIGNED = 7;

    /** 拒签 */
    public static final int ORDER_STATUS_REFUSE_SIGNED = 8;

    /** 司机评价 */
    public static final int ORDER_STATUS_DRIVER_EVALUATED = 9;

    /** 货代评价 */
    public static final int ORDER_STATUS_AGENT_EVALUATED = 10;

    /** 订单完成 */
    public static final int ORDER_STATUS_OVER = 11;

    // 我的订单左侧分类
    /** 全部订单 */
    public static final int MYORDER_TYPE_ALL = 0;

    /** 待付款 */
    public static final int MYORDER_TYPE_OBLIGATION = 1;

    /** 待配货 */
    public static final int MYORDER_TYPE_DISTRIBUTION = 2;

    /** 待签收 */
    public static final int MYORDER_TYPE_SIGN = 3;

    /** 待评价 */
    public static final int MYORDER_TYPE_EVALUATE = 4;

    /** 已取消 */
    public static final int MYORDER_TYPE_CANCELED = 5;

    /** 待接受 */
    public static final int MYORDER_TYPE_APPOINT = 6;

    public static final String TAG_MENU_HOME = "menu_home";

    public static final String TAG_MENU_BOARD = "menu_board";

    public static final String TAG_MENU_RIM = "menu_rim";

    public static final String TAG_MENU_MY_ORDERS = "menu_my_orders";

    public static final String TAG_MENU_HOME_MY_TASKS = "menu_my_tasks";

    public static final String TAG_MENU_GAMES = "menu_games";

    public static final String TAG_GOODS_GRAG = "goods_grab";// 抢货源

    public static final String[] TAGS = {
            TAG_MENU_HOME, TAG_MENU_MY_ORDERS, TAG_MENU_GAMES, TAG_GOODS_GRAG
    };

    public static final Class<?>[] CLASSES = {//
            MyInfoFragment.class,//
            MyOrdersFragment.class,//
            GamesFragment.class, GrabGoodsFragment.class
    };//

    /** 我的订单的tabs */
    public static final String TAG_ORDERS_ALL = "myorders_all";

    public static final String TAG_ORDERS_OBLIGATION = "myorders_obligation";

    public static final String TAG_ORDERS_DISTRIBUTION = "myorders_distribution";

    public static final String TAG_ORDERS_SIGN = "myorders_sign";

    public static final String TAG_ORDERS_EVALUATED = "myorders_evaluated";

    public static final String TAG_ORDERS_CANCELED = "myorders_canceled";

    public static final String TAG_ORDERS_REIMBURSE = "myorders_reimburse";

    public static final String TAG_ORDERS_RECEIVED = "myorders_received";

    public static final String[] MYORDERS_TAGS = {
            TAG_ORDERS_ALL, TAG_ORDERS_OBLIGATION, TAG_ORDERS_DISTRIBUTION, TAG_ORDERS_SIGN, TAG_ORDERS_EVALUATED,
            TAG_ORDERS_CANCELED
            // , TAG_ORDERS_REIMBURSE
            , TAG_ORDERS_RECEIVED
    };

    public static final Class<?>[] MYORDERS_CLASSES = {//
            MyOrdersAllFragment.class,//
            MyOrdersObligationFragment.class,//
            MyOrdersDistributionFragment.class,//
            MyOrdersSignFragment.class, MyOrdersEvaluatedFragment.class, MyOrdersCanceledFragment.class //
            // , MyOrdersReimburseFragment.class //
            , MyOrdersReceivedFragment.class
    };

    public static final String PAYMENT_ORDERID = "orderId";

    public static final String PAYMENT_TOTALPRICE = "totalPrice";

    public static boolean ISMYANDPUBLICCAR = false;

    public static final String LOCATION_LATITUDE = "latitude";

    public static final String LOCATION_LONGITUDE = "longitude";

    public static final String LOCATION_CITY = "city";

    public static final String LOCATION_PROVINCE = "province";

    public static final String LOCATION_ADDRESS = "address";

    // 标识是公共货源订单
    public static final int GOODS_CUSTOM_PUBLIC = 0;

    // 自有货源订单
    public static final int GOODS_CUSTOM_PRIVATE = 1;

    public static final String SERVICES_SUPPORT_TEL = "4008603060";

    // 抢货源
    public static final String GRAB_TAG_GOODS_AVAILABLE = "goods_available";

    public static final String GRAB_TAG_GOODS_HAS_AVAILABLE = "goods_has_available";

    public static final String[] GOODS_GRAB_TAGS = {
            GRAB_TAG_GOODS_AVAILABLE, GRAB_TAG_GOODS_HAS_AVAILABLE
    };

    public static final Class<?>[] GOODS_GRAB_CLASSES = {
            GoodsAvailableFragment.class, GoodsHasAvailableFragment.class
    };

    // 已抢货源查询条件状态，
    public static final String GOODSGRAB_STATUS_ALL = "0"; // 0全部

    public static final String GOODSGRAB_STATUS_SUCCESS = "1"; // 1成功

    public static final String GOODSGRAB_STATUS_FAUILURE = "2"; // 2失败

    public static final String GOODSGRAB_STATUS_AUDIT = "3"; // 3审核中

    // 已抢货源返回状态，
    public static final String GOODSGRAB_RESULT_AUDIT = "1"; // 1审核中

    public static final String GOODSGRAB_RESULT_SUCCESS = "2"; // 2成功

    public static final String GOODSGRAB_RESULT_FAUILURE = "3"; // 3失败

    // 可抢货源数量，用于显示红色标识
    public static String GOODGRAB_AVAILABLEGOODS_TOTAL = "0";

    // 已抢货源成功数量
    public static String GOODGRAB_SUCCGRABNUM = "0";

    // 已抢货源失败数量
    public static String GOODGRAB_FAILGRABNUM = "0";

    // 拒单理由
    public static String REFUSE_ORDER_REASON_OTHER = "0";

    public static String REFUSE_ORDER_REASON_MAINTENANCE = "1";

    public static String REFUSE_ORDER_REASON_SICK = "2";

    public static String REFUSE_ORDER_REASON_UNREASONABLE = "3";

    public static String REFUSE_ORDER_REASON_UNFAMILIAR = "4";

    public static String REFUSE_REASON_REFUSALREASON = "refusalreason";

    public static String REFUSE_REASON_REFUSALMEMO = "refusalmemo";

}
