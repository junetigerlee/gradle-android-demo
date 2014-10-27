
package cn.com.incito.driver.models;

import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;
import cn.com.incito.wisdom.sdk.net.http.WisdomCitySyncRestClient;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoMaster;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

/**
 * ListAbstractModel
 * 
 * @description ListAbstractModel
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public abstract class ListAbstractModel<Model, Api extends WisdomCityServerAPI, Response extends BasicResponse> extends
        AbstractModel {

    public static final int DEFAULT_PAGE_SIZE = 20;

    public final static String CAR_ID = "22f73ec5d9704a459649c12ac40e1d34";

    public final static String DRIVER_ID = "22f73ec5d9704a459649c12ac40e1125";

    public final static String VCOLUMN_START = "start";

    public final static String VCOLUMN_NUM = "num";

    public final static String VCOLUMN_PAGE_NO = "pageNo";

    public final static String VCOLUMN_PAGE_SIZE = "pageSize";

    public final static String VCOLUMN_CAR_ID = "carid";

    public final static String VCOLUMN_GOODS_STATUS = "goodsstatus";

    public static final String VCOLUMN_MID = "mId";

    public ListAbstractModel(Context context, SQLiteDatabase db, AbstractDaoMaster daoMaster) {
        super(context, db, daoMaster);
    }

    @Override
    public int refresh(boolean loadMore, Map<String, Object> keyValues) {
        ModelResponse resp = getProviderResponse();
        if (NetworkUtils.isNetworkAvaliable(mContext)) {
            fetchFromNetwork(loadMore, keyValues);
        } else {
            resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NETOWRK_NOT_AVALIABLE);
            sEventBus.post(resp);
        }
        return 0;
    }

    /** 查询数据 */
    @SuppressWarnings("unchecked")
    public Cursor query(boolean loadMore, int fetchDataApproach, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        boolean isNetworkAvaliable = NetworkUtils.isNetworkAvaliable(mContext);
        Cursor cursor = null;
        ModelResponse resp = getProviderResponse();
        if (fetchDataApproach == FETCH_DATA_APPROACH_ONLY_NETWORK) {// 只从网络获取数据
            if (isNetworkAvaliable) {// 网络有效
                Map<String, Object> keyValues = SQLParamToServerAPIParam.parseParams(selection, selectionArgs);
                Api api = newAPIInstance(keyValues);
                WisdomCitySyncRestClient client = new WisdomCitySyncRestClient();
                Response response = (Response) client.executeSync(api);
                if (response.status == BasicResponse.SUCCESS) {
                    List<Model> list = getAPIResponse(response);
                    if (list.isEmpty()) {// 数据集合为null
                        if (loadMore) {
                            resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NO_MORE_DATA);
                        } else {
                            resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NO_DATA);
                        }
                    } else {
                        resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_OK);
                        cursor = buildCursor(list, projection);
                    }
                } else {
                    resp.setStatus(response.status);
                    resp.setMsg(response.msg);
                }
            } else {
                resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NETOWRK_NOT_AVALIABLE);
            }
            sEventBus.post(resp);
            return cursor;
        } else {// 如果不是仅仅从网络获取数据的话，则从本地拿数据
            cursor = fetchFromDisk(projection, selection, selectionArgs, sortOrder);
            if (cursor != null && cursor.moveToFirst()) {
                if (fetchDataApproach == FETCH_DATA_APPROACH_ONLY_LOCAL || !isDataExpired(cursor)) {
                    resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_OK);
                } else {
                    if (isNetworkAvaliable) {
                        // data expired, first return back cached data, and
                        // then fetch from network
                        resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NEED_REFRESH_WITH_EXPRIED_DATA);
                    } else {
                        resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_OK);
                    }
                }
                sEventBus.post(resp);
                return cursor;
            }
        }// end of else, not only network
         // 如果本地没数据的话，并且是只从本地拿数据 加载更多的话，则告诉没有更多数据，不是加载更多的话，则告诉没有数据
        if (fetchDataApproach == FETCH_DATA_APPROACH_ONLY_LOCAL) {
            if (loadMore) {
                resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NO_MORE_DATA);
            } else {
                resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NO_DATA);
            }
        } else {// 如果不是只从本地拿数据的话，网络有效的情况下并且不是加载更多的话，则不用过期数据刷新界面，如果是加载更多的话，则加载更多数据
            if (isNetworkAvaliable) {
                if (!loadMore) {
                    resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NEED_REFRESH_WITHOUT_EXPRIED_DATA);
                } else {
                    resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NEED_LOAD_MORE);
                }
            } else {// 通知网络无效
                resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NETOWRK_NOT_AVALIABLE);
            }
        }
        sEventBus.post(resp);
        return cursor;
    }

    @SuppressWarnings("unchecked")
    public int update(ContentValues values, String selection, String[] selectionArgs) {
        int ret = 0;
        ModelResponse resp = getProviderResponse();
        try {
            AbstractDao<Model, Long> dao = (AbstractDao<Model, Long>) mDaoSession.getDao(getDAOModelClassName());
            ret = mDatabase.update(dao.getTablename(), values, selection, selectionArgs);
            resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_RELOAD);
        } catch (SQLException e) {
        }
        sEventBus.post(resp);
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int delete(String whereClause, String[] whereArgs) {
        int ret = 0;
        try {
            AbstractDao<Model, Long> dao = (AbstractDao<Model, Long>) mDaoSession.getDao(getDAOModelClassName());
            ret = mDatabase.delete(dao.getTablename(), whereClause, whereArgs);
        } catch (SQLException e) {
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public int insert(Model model) {
        int ret = 0;
        try {
            AbstractDao<Model, Long> dao = (AbstractDao<Model, Long>) mDaoSession.getDao(getDAOModelClassName());
            dao.insert(model);
        } catch (SQLException e) {
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Cursor fetchFromDisk(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        try {
            AbstractDao<Model, Long> dao = (AbstractDao<Model, Long>) mDaoSession.getDao(getDAOModelClassName());
            cursor = mDatabase.query(dao.getTablename(), projection, selection, selectionArgs, null, null, sortOrder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void fetchFromNetwork(final boolean loadMore, Map<String, Object> params) {
        Api api = newAPIInstance(params);
        new WisdomCityHttpResponseHandler(api, new APIFinishCallback() {

            @Override
            public void OnRemoteApiFinish(BasicResponse response) {
                ModelResponse resp = getProviderResponse();
                if (response.status == BasicResponse.SUCCESS) {
                    resp.setStatus(loadMore ? ModelResponse.REQUEST_RESULT_STATUS_LOAD_MORE_FINISH
                            : ModelResponse.REQUEST_RESULT_STATUS_REFRESH_FINISH);
                    AbstractDao<Model, Long> dao = null;
                    try {
                        dao = (AbstractDao<Model, Long>) mDaoSession.getDao(getDAOModelClassName());
                        Response r = (Response) response;
                        List<Model> list = getAPIResponse(r);
                        if (!loadMore) {
                            dao.deleteAll();
                        }
                        if (list.isEmpty()) {
                            if (loadMore) {
                                resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NO_MORE_DATA);
                            } else {
                                resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_NO_DATA);
                            }
                        } else {
                            dao.insertInTx(list);
                        }
                    } catch (SQLException e) {
                        resp.setStatus(ModelResponse.REQUEST_RESULT_STATUS_DATABSE_IO_ERROR);
                    }
                } else {
                    resp.setStatus(response.status);
                    resp.setMsg(response.msg);
                }
                sEventBus.post(resp);
            }
        });
        WisdomCityRestClient.execute(api);
    }

    protected abstract Class<Model> getDAOModelClassName();

    protected abstract Api newAPIInstance(Map<String, Object> params);

    protected abstract List<Model> getAPIResponse(Response r);

    protected AbstractListCursor<Model> buildCursor(List<Model> list, String[] projection) {
        return null;
    }

    protected static abstract class AbstractListCursor<Model> implements Cursor {
        protected String[] mColoums;

        protected List<Model> mData;

        protected int mCurrPos = -1;

        protected int[] mProjectionMapping;

        public AbstractListCursor(List<Model> list, String[] projection) {
            if (list == null) {
                throw new IllegalArgumentException();
            }
            mColoums = getColumnNameStringArray();
            mData = list;
            mProjectionMapping = new int[projection.length];
            for (int i = 0; i < projection.length; i++) {
                for (int j = 0; j < mColoums.length; j++) {
                    if (mColoums[j].equals(projection[i])) {
                        mProjectionMapping[i] = j;
                        break;
                    }
                }
            }
        }

        private boolean realMove(int position) {
            if (position < 0 || position > mData.size() - 1) {
                return false;
            } else {
                mCurrPos = position;
                return true;
            }
        }

        protected abstract Object realGet(int columnIndex);

        protected abstract String[] getColumnNameStringArray();

        @Override
        public void close() {
            mData = null;
        }

        @Override
        public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        }

        @Override
        @Deprecated
        public void deactivate() {
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            return null;
        }

        @Override
        public int getColumnCount() {
            return mColoums.length;
        }

        @Override
        public int getColumnIndex(String columnName) {
            for (int i = 0; i < mColoums.length; i++) {
                if (mColoums[i].equals(columnName)) {
                    return i;
                }
            }
            return 0;
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            for (int i = 0; i < mColoums.length; i++) {
                if (mColoums[i].equals(columnName)) {
                    return i;
                }
            }
            throw new IllegalArgumentException();
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex < 0 || columnIndex >= mColoums.length) {
                return null;
            }
            return mColoums[columnIndex];
        }

        @Override
        public String[] getColumnNames() {
            return mColoums;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public double getDouble(int columnIndex) {
            Object ret = realGet(columnIndex);
            if (ret instanceof Double) {
                return (Double) ret;
            } else {
                return 0;
            }
        }

        @Override
        public Bundle getExtras() {
            return null;
        }

        @Override
        public float getFloat(int columnIndex) {
            Object ret = realGet(columnIndex);
            if (ret instanceof Float) {
                return (Float) ret;
            } else {
                return 0;
            }
        }

        @Override
        public int getInt(int columnIndex) {
            Object ret = realGet(columnIndex);
            if (ret instanceof Integer) {
                return (Integer) ret;
            } else {
                return 0;
            }
        }

        @Override
        public long getLong(int columnIndex) {
            Object ret = realGet(columnIndex);
            if (ret instanceof Long) {
                return (Long) ret;
            } else {
                return 0;
            }
        }

        @Override
        public int getPosition() {
            return mCurrPos;
        }

        @Override
        public short getShort(int columnIndex) {
            Object ret = realGet(columnIndex);
            if (ret instanceof Short) {
                return (Short) ret;
            } else {
                return 0;
            }
        }

        @Override
        public String getString(int columnIndex) {
            Object ret = realGet(columnIndex);
            if (ret instanceof String) {
                return (String) ret;
            } else {
                return null;
            }
        }

        @Override
        public int getType(int columnIndex) {
            return 0;
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        @Override
        public boolean isAfterLast() {
            return mCurrPos >= mData.size();
        }

        @Override
        public boolean isBeforeFirst() {
            return mCurrPos < 0;
        }

        @Override
        public boolean isClosed() {
            return mData == null;
        }

        @Override
        public boolean isFirst() {
            return mCurrPos == 0;
        }

        @Override
        public boolean isLast() {
            return mCurrPos == mData.size() - 1;
        }

        @Override
        public boolean isNull(int columnIndex) {
            Object ret = realGet(columnIndex);
            return ret == null;
        }

        @Override
        public boolean move(int offset) {
            return realMove(mCurrPos + offset);
        }

        @Override
        public boolean moveToFirst() {
            return realMove(0);
        }

        @Override
        public boolean moveToLast() {
            return realMove(mData.size() - 1);
        }

        @Override
        public boolean moveToNext() {
            return realMove(mCurrPos++);
        }

        @Override
        public boolean moveToPosition(int position) {
            return realMove(position);
        }

        @Override
        public boolean moveToPrevious() {
            return realMove(mCurrPos--);
        }

        @Override
        public void registerContentObserver(ContentObserver observer) {
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        @Deprecated
        public boolean requery() {
            return false;
        }

        @Override
        public Bundle respond(Bundle extras) {
            return null;
        }

        @Override
        public void setNotificationUri(ContentResolver cr, Uri uri) {
        }

        @Override
        public void unregisterContentObserver(ContentObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }
    }
}
