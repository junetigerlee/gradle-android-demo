
package cn.com.incito.driver.provider;

import cn.com.incito.driver.DriverMainActivity;
import cn.com.incito.driver.dao.DaoMaster;
import cn.com.incito.driver.dao.DaoMaster.DevOpenHelper;
import cn.com.incito.driver.dao.DaoSession;
import cn.com.incito.driver.models.AbstractModel;
import cn.com.incito.driver.models.SQLParamToServerAPIParam;
import cn.com.incito.driver.models.goods.GoodsAvailableModel;
import cn.com.incito.driver.models.goods.GoodsHasAvailableModel;
import cn.com.incito.driver.models.orders.MyOrdersAllModel;
import cn.com.incito.driver.models.orders.MyOrdersCanceledModel;
import cn.com.incito.driver.models.orders.MyOrdersDistributionModel;
import cn.com.incito.driver.models.orders.MyOrdersEvaluatedModel;
import cn.com.incito.driver.models.orders.MyOrdersObligationModel;
import cn.com.incito.driver.models.orders.MyOrdersReceivedModel;
import cn.com.incito.driver.models.orders.MyOrdersReimburseModel;
import cn.com.incito.driver.models.orders.MyOrdersSignModel;
import cn.com.incito.wisdom.sdk.utils.ReflectionUtils;

import de.greenrobot.dao.AbstractDaoMaster;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 内容提供者
 * 
 * @description 内容提供者
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class DataProvider extends ContentProvider {
    public static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "driver_cache.db";

    private static final String AUTHORITY = "cn.com.incito.driver";

    private static final String SCHEME = "content://";

    private static final String MODEL_DEFAULT_URL_FIELD_NAME = "sDefaultUrl";

    private static final String REFRESH_METHOD_NAME = "refresh";

    private static final String QUERY_METHOD_NAME = "query";

    private static final String UPDATE_METHOD_NAME = "update";

    private static final String DELETE_METHOD_NAME = "delete";

    private static final String LOAD_MORE_URI_SUFFIX = "/loadmore";

    private static final String ONLY_LOCAL_URI_SUFFIX = "/onlylocal";

    private static final String ONLY_NETWORK_URI_SUFFIX = "/onlynetwork";

    private static final String LOAD_MORE_NETWORK_URI_SUFFIX = "/loadmorenetwork";

    /** 清除缓存 */
    private static final String CLEAR_EXCLUDE_LIST = "clearExcludeList";

    public static final Uri CLEAR_EXCLUDE_LIST_URI = Uri.parse(SCHEME + AUTHORITY + "/" + CLEAR_EXCLUDE_LIST);

    /** 清除用户缓存 */
    private static final String CLEAR_USER_CACHE_DATA = "clearUserCacheData";

    public static final Uri CLEAR_USER_CACHE_DATA_URI = Uri.parse(SCHEME + AUTHORITY + "/" + CLEAR_USER_CACHE_DATA);

    private static final int CLEAR_EXCLUDE_LIST_ID = 1;

    private static final int CLEAR_USER_CACHE_DATA_ID = 2;

    /**
     * A UriMatcher instance
     */
    private static final UriMatcher sUriMatcher;

    private static final SparseArrayCompat<ClassEntity> sModelClassMap;

    private static int sUriMatchedId = 1;

    private static class ClassEntity {

        public ClassEntity(Class<? extends AbstractModel> clazz, boolean loadMore, int fetchApproach) {
            super();
            this.mClazz = clazz;
            this.mLoadMore = loadMore;
            this.mFetchDataApproach = fetchApproach;
        }

        Class<? extends AbstractModel> mClazz;

        boolean mLoadMore;

        int mFetchDataApproach;
    }

    /**
     * A block that instantiates and sets static objects
     */
    static {
        sModelClassMap = new SparseArrayCompat<ClassEntity>();
        /*
         * Creates and initializes the URI matcher
         */
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AUTHORITY, CLEAR_EXCLUDE_LIST, sUriMatchedId++);
        sUriMatcher.addURI(AUTHORITY, CLEAR_USER_CACHE_DATA, sUriMatchedId++);

        // addUri(MyOrdersModel.class);
        addUri(MyOrdersAllModel.class);
        addUri(MyOrdersObligationModel.class);
        addUri(MyOrdersDistributionModel.class);
        addUri(MyOrdersSignModel.class);
        addUri(MyOrdersEvaluatedModel.class);
        addUri(MyOrdersCanceledModel.class);
        addUri(MyOrdersReimburseModel.class);
        addUri(MyOrdersReceivedModel.class);

        addUri(GoodsAvailableModel.class);// 可抢货源
        addUri(GoodsHasAvailableModel.class);// 已抢货源
    }

    protected SQLiteDatabase mDB;

    protected DevOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new DaoMaster.DevOpenHelper(getContext(), DATABASE_NAME, null);
        mDB = mHelper.getWritableDatabase();
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        Log.e("wisdomcity",
                " Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)");
        int matchedId = sUriMatcher.match(uri);
        if (sModelClassMap.get(matchedId) != null) {
            ClassEntity entity = sModelClassMap.get(matchedId);
            Object[] constructArgs = new Object[] {
                    getContext(), mDB, new DaoMaster(mDB)
            };
            final Class<?>[] constructArgsClass = new Class[3];
            constructArgsClass[0] = Context.class;
            constructArgsClass[1] = SQLiteDatabase.class;
            constructArgsClass[2] = AbstractDaoMaster.class;
            Object[] methodArgs = new Object[] {
                    entity.mLoadMore, entity.mFetchDataApproach, projection, selection, selectionArgs, sortOrder
            };
            final int length = methodArgs.length;
            final Class<?>[] methodArgsClass = new Class[length];
            String[] arary = new String[] {};
            methodArgsClass[0] = boolean.class;
            methodArgsClass[1] = int.class;
            methodArgsClass[2] = arary.getClass();
            methodArgsClass[3] = String.class;
            methodArgsClass[4] = arary.getClass();
            methodArgsClass[5] = String.class;
            try {
                cursor = (Cursor) ReflectionUtils.invokeMethod(entity.mClazz.getName(), constructArgsClass,
                        constructArgs, QUERY_METHOD_NAME, methodArgsClass, methodArgs);
                if (cursor != null) {
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException();
            }
        }
        return cursor;
    }

    // 网上获取新数据
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Map<String, Object> keyValues = SQLParamToServerAPIParam.parseParams(selection, selectionArgs);

        int matchedId = sUriMatcher.match(uri);
        if (sModelClassMap.get(matchedId) != null) {
            ClassEntity entity = sModelClassMap.get(matchedId);

            Object[] constructArgs = new Object[] {
                    getContext(), mDB, new DaoMaster(mDB)
            };
            final Class<?>[] constructArgsClass = new Class[3];
            constructArgsClass[0] = Context.class;
            constructArgsClass[1] = SQLiteDatabase.class;
            constructArgsClass[2] = AbstractDaoMaster.class;

            if (entity.mFetchDataApproach == AbstractModel.FETCH_DATA_APPROACH_ONLY_LOCAL) {
                Object[] methodArgs = new Object[] {
                        values, selection, selectionArgs
                };
                final int length = methodArgs.length;
                final Class<?>[] methodArgsClass = new Class[length];
                String[] arary = new String[] {};
                methodArgsClass[0] = ContentValues.class;
                methodArgsClass[1] = String.class;
                methodArgsClass[2] = arary.getClass();
                try {
                    ReflectionUtils.invokeMethod(entity.mClazz.getName(), constructArgsClass, constructArgs,
                            UPDATE_METHOD_NAME, methodArgsClass, methodArgs);
                    if (uri.getPathSegments() != null && !uri.getPathSegments().isEmpty()) {
                        Uri notifyuri = Uri.parse(SCHEME + AUTHORITY);
                        notifyuri = Uri.withAppendedPath(notifyuri, uri.getPathSegments().get(0));
                        getContext().getContentResolver().notifyChange(notifyuri, null);
                    }
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException();
                } catch (NullPointerException e) {
                }
            } else {
                Object[] methodArgs = new Object[] {
                        entity.mLoadMore, keyValues
                };
                final int length = methodArgs.length;
                final Class<?>[] methodArgsClass = new Class[length];
                methodArgsClass[0] = boolean.class;
                methodArgsClass[1] = Map.class;
                try {
                    ReflectionUtils.invokeMethod(entity.mClazz.getName(), constructArgsClass, constructArgs,
                            REFRESH_METHOD_NAME, methodArgsClass, methodArgs);
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException();
                }
            }
        }
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int matchedId = sUriMatcher.match(uri);
        switch (sUriMatcher.match(uri)) {
            case CLEAR_EXCLUDE_LIST_ID:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DaoMaster master = new DaoMaster(mDB);
                            DaoSession session = master.newSession();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(
                                new Intent(DriverMainActivity.INTERNAL_BROADCAST_RECEIVER_CACHE_CLEARED));
                    }
                }).start();
                break;
            case CLEAR_USER_CACHE_DATA_ID:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DaoMaster master = new DaoMaster(mDB);
                            DaoSession session = master.newSession();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            default:
                if (sModelClassMap.get(matchedId) != null) {
                    ClassEntity entity = sModelClassMap.get(matchedId);

                    Object[] constructArgs = new Object[] {
                            getContext(), mDB, new DaoMaster(mDB)
                    };
                    final Class<?>[] constructArgsClass = new Class[3];
                    constructArgsClass[0] = Context.class;
                    constructArgsClass[1] = SQLiteDatabase.class;
                    constructArgsClass[2] = AbstractDaoMaster.class;

                    Object[] methodArgs = new Object[] {
                            selection, selectionArgs
                    };
                    final int length = methodArgs.length;
                    final Class<?>[] methodArgsClass = new Class[length];
                    String[] arary = new String[] {};
                    methodArgsClass[0] = String.class;
                    methodArgsClass[1] = arary.getClass();
                    try {
                        ReflectionUtils.invokeMethod(entity.mClazz.getName(), constructArgsClass, constructArgs,
                                DELETE_METHOD_NAME, methodArgsClass, methodArgs);
                        if (uri.getPathSegments() != null && !uri.getPathSegments().isEmpty()) {
                            Uri notifyuri = Uri.parse(SCHEME + AUTHORITY);
                            notifyuri = Uri.withAppendedPath(notifyuri, uri.getPathSegments().get(0));
                            getContext().getContentResolver().notifyChange(notifyuri, null);
                        }
                    } catch (InvocationTargetException e) {
                        throw new IllegalArgumentException();
                    }
                }
        }
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    private static void addUri(Class<? extends AbstractModel> clazz) {
        String uri = (String) ReflectionUtils.getStaticProperty(clazz.getName(), MODEL_DEFAULT_URL_FIELD_NAME);
        if (!TextUtils.isEmpty(uri)) {
            // loadmore = false, prefer cache
            sUriMatcher.addURI(AUTHORITY, uri, sUriMatchedId);
            sModelClassMap.put(sUriMatchedId++, new ClassEntity(clazz, false,
                    AbstractModel.FETCH_DATA_APPROACH_FIRST_LOCAL_THEN_NETWORK));

            // loadmore = true, prefer cache
            sUriMatcher.addURI(AUTHORITY, uri + LOAD_MORE_URI_SUFFIX, sUriMatchedId);
            sModelClassMap.put(sUriMatchedId++, new ClassEntity(clazz, true,
                    AbstractModel.FETCH_DATA_APPROACH_FIRST_LOCAL_THEN_NETWORK));

            // only local(loadmore must be false)
            sUriMatcher.addURI(AUTHORITY, uri + ONLY_LOCAL_URI_SUFFIX, sUriMatchedId);
            sModelClassMap.put(sUriMatchedId++, new ClassEntity(clazz, false,
                    AbstractModel.FETCH_DATA_APPROACH_ONLY_LOCAL));

            // only network (loadmore = false)
            sUriMatcher.addURI(AUTHORITY, uri + ONLY_NETWORK_URI_SUFFIX, sUriMatchedId);
            sModelClassMap.put(sUriMatchedId++, new ClassEntity(clazz, false,
                    AbstractModel.FETCH_DATA_APPROACH_ONLY_NETWORK));

            // only network, loadmore
            sUriMatcher.addURI(AUTHORITY, uri + LOAD_MORE_NETWORK_URI_SUFFIX, sUriMatchedId);
            sModelClassMap.put(sUriMatchedId++, new ClassEntity(clazz, true,
                    AbstractModel.FETCH_DATA_APPROACH_ONLY_NETWORK));
        }
    }

    public static Uri getUri(Class<? extends AbstractModel> clazz, boolean loadMore) {
        return getUri(clazz, loadMore, AbstractModel.FETCH_DATA_APPROACH_FIRST_LOCAL_THEN_NETWORK);
    }

    public static Uri getUri(Class<? extends AbstractModel> clazz, boolean loadMore, int fetchApproach) {
        if (loadMore && fetchApproach == AbstractModel.FETCH_DATA_APPROACH_ONLY_LOCAL) {
            throw new IllegalArgumentException();
        }
        String uri = (String) ReflectionUtils.getStaticProperty(clazz.getName(), MODEL_DEFAULT_URL_FIELD_NAME);
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(uri)) {
            buffer.append(SCHEME);
            buffer.append(AUTHORITY);
            buffer.append("/");
            buffer.append(uri);
            if (AbstractModel.FETCH_DATA_APPROACH_FIRST_LOCAL_THEN_NETWORK == fetchApproach) {
                if (loadMore) {
                    buffer.append(LOAD_MORE_URI_SUFFIX);
                }
            } else if (AbstractModel.FETCH_DATA_APPROACH_ONLY_NETWORK == fetchApproach) {
                if (loadMore) {
                    buffer.append(LOAD_MORE_NETWORK_URI_SUFFIX);
                } else {
                    buffer.append(ONLY_NETWORK_URI_SUFFIX);
                }
            } else if (AbstractModel.FETCH_DATA_APPROACH_ONLY_LOCAL == fetchApproach) {
                buffer.append(ONLY_LOCAL_URI_SUFFIX);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return Uri.parse(buffer.toString());
    }
}
