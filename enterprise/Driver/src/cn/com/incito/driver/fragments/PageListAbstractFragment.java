
package cn.com.incito.driver.fragments;

import android.content.AsyncQueryHandler;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.incito.driver.R;
import cn.com.incito.driver.models.ModelResponse;
import cn.com.incito.wisdom.sdk.adapter.PageCursor;
import cn.com.incito.wisdom.sdk.adapter.PageCursorAdapter;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;
import cn.com.incito.wisdom.uicomp.widget.refreshable.PullToRefreshBase;
import cn.com.incito.wisdom.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import cn.com.incito.wisdom.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;

/**
 * 列表数据抽象类
 * 
 * @description 列表数据抽象类
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public abstract class PageListAbstractFragment<T extends View, U extends ModelResponse> extends BaseAbstractFragment
        implements OnRefreshListener2<T> {

    protected static final int MODE_REFRESH = 1;

    protected static final int MODE_LOAD_MORE = 2;

    protected AsyncQueryHandler mAsyncQueryHandler;

    protected PageCursorAdapter mAdapter;

    protected int mPageSize;

    protected Mode mRefreshMode = Mode.BOTH;

    protected PullToRefreshBase<T> mPullToRefreshWidget;

    /**
     * strings add by slider. 网络请求数据为空或者没有更多数据
     */
    public String _RESULT_NO_MORE_DATA = "";

    public String _RESULT_NO_DATA = "";

    // protected boolean AUTO_REFRESH_IS_FINISH = false;
    // private Handler mTaskHandler = new Handler();
    // private Runnable runnable = new Runnable() {
    //
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // this.refresh();
    // if (AUTO_REFRESH_IS_FINISH
    // && NetworkUtils.isNetworkAvaliable(getActivity())) {
    // mTaskHandler.postDelayed(this, 1000 * 30);// 定时刷新，间隔30秒
    // AUTO_REFRESH_IS_FINISH = false;
    // }
    // }
    //
    // public void refresh() {
    // loadData(true, false);
    // mPullToRefreshWidget.setMode(Mode.PULL_DOWN_TO_REFRESH);
    // mPullToRefreshWidget.setRefreshing();
    // AUTO_REFRESH_IS_FINISH = true;
    // }
    // };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _RESULT_NO_MORE_DATA = getResources().getString(R.string.no_more_data);
        _RESULT_NO_DATA = getResources().getString(R.string.errcode_network_response_no_data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LOG_TAG, TAG + " onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");

        if (mAdapter != null) {
            mAdapter.changeCursor(null);
            mAdapter = null;
        }

        View view = onCreateFragmentView(inflater, container, savedInstanceState);
        // 标记页面用的，用完删除
        // TextView test_tag = (TextView) view.findViewById(R.id.test_tag);
        // test_tag.setText(this.getClass().getSimpleName());
        mAsyncQueryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                if (mAdapter == null) {
                    return;
                }
                switch (token) {
                    case MODE_REFRESH:
                        OnDataReady(cursor);
                        mAdapter.changeCursor(cursor);
                        break;
                    case MODE_LOAD_MORE:
                        if (cursor != null && cursor.moveToFirst()) {
                            mAdapter.addCursor(cursor);
                        }
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }

        };
        loadData(false, false);
        return view;
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (NetworkUtils.isNetworkAvaliable(getActivity())) {
            // mTaskHandler.postDelayed(runnable, 1000 * 30);
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        // mTaskHandler.removeCallbacks(runnable);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<T> refreshView) {
        loadData(true, false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<T> refreshView) {
        mPullToRefreshWidget.onRefreshComplete();
        loadData(false, true);
    }

    /**
     * 初始化view
     * 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 加载数据
     * 
     * @param update 是否拉取新数据
     * @param loadmore 是否是加载更多
     */
    protected abstract void loadData(boolean update, boolean loadmore);

    /**
     * 根据cursor得到数据上次更新的时间，设置到下拉刷新的view对象里面
     */
    protected void OnDataReady(Cursor cursor) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (cursor != null && cursor.moveToFirst()) {
            mPullToRefreshWidget.setLastUpdatedLabel(DateUtils.formatDateTime(getActivity(),
                    cursor.getLong(cursor.getColumnIndex("createtime")), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
        } else {
            if (NetworkUtils.isNetworkAvaliable(getActivity())) {
                showEmptyPage(getString(R.string.no_data));
            } else {
                showEmptyPage(getString(R.string.errcode_network_unavailable));
            }
        }
    }

    protected void query(boolean update, boolean loadMore, Uri uri, String[] projection, String selection,
            String[] selectionArgs, String orderBy) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (update) {
            getActivity().getContentResolver().update(uri, null, selection, selectionArgs);
        } else {
            mAsyncQueryHandler.startQuery(loadMore ? MODE_LOAD_MORE : MODE_REFRESH, null, uri, projection, selection,
                    selectionArgs, orderBy);
        }
    }

    public void handleProviderResponse(final U resp) {
        if (mAdapter == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                System.out.println("resp.getStatus = " + resp.getStatus());
                switch (resp.getStatus()) {
                    case ModelResponse.REQUEST_RESULT_STATUS_OK:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        break;
                    // arrive end of cache, need get more data from network
                    case ModelResponse.REQUEST_RESULT_STATUS_NEED_LOAD_MORE:
                        mPullToRefreshWidget.setMode(Mode.PULL_UP_TO_REFRESH);
                        mPullToRefreshWidget.setRefreshing();
                        loadData(true, true);
                        break;
                    // data expired or empty, need refresh from network
                    case ModelResponse.REQUEST_RESULT_STATUS_NEED_REFRESH_WITH_EXPRIED_DATA:
                    case ModelResponse.REQUEST_RESULT_STATUS_NEED_REFRESH_WITHOUT_EXPRIED_DATA:
                        mPullToRefreshWidget.setMode(Mode.PULL_DOWN_TO_REFRESH);
                        mPullToRefreshWidget.setRefreshing();
                        loadData(true, false);
                        break;
                    // load more data from network was finished
                    case ModelResponse.REQUEST_RESULT_STATUS_LOAD_MORE_FINISH:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        loadData(false, true);
                        break;
                    // refresh data from network is finish
                    case ModelResponse.REQUEST_RESULT_STATUS_REFRESH_FINISH:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        loadData(false, false);
                        break;
                    // show notification for network is invalid
                    case ModelResponse.REQUEST_RESULT_STATUS_NETOWRK_NOT_AVALIABLE:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        requestResponse(resp.getStatus(), getString(R.string.errcode_network_unavailable));
                        break;
                    // show notification for get no more data from network
                    case ModelResponse.REQUEST_RESULT_STATUS_NO_MORE_DATA:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        requestResponse(resp.getStatus(), _RESULT_NO_MORE_DATA);
                        break;
                    // show notification for get no data from network
                    case ModelResponse.REQUEST_RESULT_STATUS_NO_DATA:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        requestResponse(resp.getStatus(), _RESULT_NO_DATA);
                        PageCursor cursor = mAdapter.getCursor();
                        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
                            // requestResponse(resp.getStatus(),
                            // getString(R.string.errcode_network_response_no_data));
                            mAdapter.changeCursor(null);
                        }
                        break;
                    // show notification for occurring exception while operating
                    // database
                    case ModelResponse.REQUEST_RESULT_STATUS_DATABSE_IO_ERROR:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        requestResponse(resp.getStatus(), getString(R.string.errcode_network_database_io));
                        break;
                    case ModelResponse.REQUEST_RESULT_STATUS_RELOAD:
                        loadData(false, false);
                        break;
                    default:
                        mPullToRefreshWidget.onRefreshComplete();
                        mPullToRefreshWidget.setMode(mRefreshMode);
                        requestResponse(resp.getStatus(), resp.getMsg());
                        break;
                }
            }
        });
    }

    protected abstract void showEmptyPage(String text);

    protected abstract void showToast(int isid);

    /**
     * 假刷新
     * 
     * @param mode
     */
    protected void fakeRefresh(Mode mode) {
        if (mPullToRefreshWidget != null) {
            mPullToRefreshWidget.setMode(mode);
            mPullToRefreshWidget.setRefreshing();
        }
    }

}
