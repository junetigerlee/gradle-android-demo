
package cn.com.incito.driver.models;

import cn.com.incito.wisdom.sdk.event.EventBus;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

/**
 * AbstractModel
 * 
 * @description AbstractModel
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public abstract class AbstractModel {
    /** 只从本地获取数据 */
    public static final int FETCH_DATA_APPROACH_ONLY_LOCAL = 1;

    /** 只从网络获取数据 */
    public static final int FETCH_DATA_APPROACH_ONLY_NETWORK = 2;

    /** 先从本地获取数据再从网络获取数据 */
    public static final int FETCH_DATA_APPROACH_FIRST_LOCAL_THEN_NETWORK = 3;

    public final static String COLUMN_ID = "_id";

    public final static String COLUMN_CREATE_TIME = "createtime";

    protected static final EventBus sEventBus;
    static {
        sEventBus = EventBus.getDefault();
    }

    protected SQLiteDatabase mDatabase;

    protected AbstractDaoMaster mDaoMaster;

    protected AbstractDaoSession mDaoSession;

    protected Context mContext;

    public AbstractModel(Context context, SQLiteDatabase db, AbstractDaoMaster daoMaster) {
        super();
        mContext = context;
        mDatabase = db;
        mDaoMaster = daoMaster;
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 直接拉取网络数据
     * 
     * @param loadMore 是否加载更多
     * @param keyValues 参数
     * @return
     */
    public abstract int refresh(boolean loadMore, Map<String, Object> keyValues);

    /**
     * 查询数据
     * 
     * @param loadMore 是否加载更多
     * @param fetchDataApproach 获取数据的方法
     * @param projection
     * @param selection 查询语句条件
     * @param selectionArgs 查询语句条件参数
     * @param sortOrder 查询语句命令
     * @return
     */
    public abstract Cursor query(boolean loadMore, int fetchDataApproach, String[] projection, String selection,
            String[] selectionArgs, String sortOrder);

    /**
     * 更新数据
     * 
     * @param values
     * @param selection 条件语句
     * @param selectionArgs 条件语句参数
     * @return
     */
    public abstract int update(ContentValues values, String selection, String[] selectionArgs);

    /**
     * 删除数据
     * 
     * @param selection 条件语句
     * @param selectionArgs 条件语句参数
     * @return
     */
    public abstract int delete(String selection, String[] selectionArgs);

    /** 判断数据是否超时 */
    protected boolean isDataExpired(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(COLUMN_CREATE_TIME);
            long lastInsertTime = cursor.getLong(columnIndex);
            return (System.currentTimeMillis() - lastInsertTime) > getCacheExpiredTime() ? true : false;
        }
        return true;
    }

    /** 获取缓存数据过期时间 */
    protected abstract ModelResponse getProviderResponse();

    /** 获取缓存数据过期时间 */
    protected abstract long getCacheExpiredTime();

    /**
     * 从网络加载数据
     * 
     * @param loadMore 是否加载更多
     * @param params 参数
     */
    protected abstract void fetchFromNetwork(boolean loadMore, Map<String, Object> params);

    /**
     * 从本地加载数据
     * 
     * @param projection 要查询的字段
     * @param selection 条件语句
     * @param selectionArgs 条件语句参数
     * @param sortOrder 命令
     * @return
     */
    protected abstract Cursor fetchFromDisk(String[] projection, String selection, String[] selectionArgs,
            String sortOrder);
}
