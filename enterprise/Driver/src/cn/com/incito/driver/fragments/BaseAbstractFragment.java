
package cn.com.incito.driver.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.com.incito.driver.R;
import cn.com.incito.driver.dao.DaoMaster;
import cn.com.incito.driver.dao.DaoMaster.DevOpenHelper;
import cn.com.incito.driver.models.ModelResponse;
import cn.com.incito.driver.provider.DataProvider;
import cn.com.incito.wisdom.sdk.event.EventBus;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

/**
 * 基础抽象类
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public abstract class BaseAbstractFragment extends Fragment {
    public String LOG_TAG = "driver";

    public String TAG = this.getClass().getSimpleName();

    protected SQLiteDatabase mDB;

    protected DevOpenHelper mHelper;

    private ProgressiveDialog mProgressiveDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        mHelper = new DaoMaster.DevOpenHelper(getActivity(), DataProvider.DATABASE_NAME, null);
        mDB = mHelper.getWritableDatabase();
        Log.e(LOG_TAG, TAG + " onCreate(Bundle savedInstanceState)");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(LOG_TAG, TAG + " onActivityCreated(Bundle savedInstanceState)");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(LOG_TAG, TAG + " onDestroyView()");
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Log.e(LOG_TAG, TAG + " onDestroy()");
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
     * 根据请求结果的响应来显示数据
     * 
     * @param status
     * @param text
     */
    protected void requestResponse(int status, String text) {
        if (text == null || text.equals("")) {
            return;
        }
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        switch (status) {
            case ModelResponse.REQUEST_RESULT_STATUS_NO_DATA:
                showEmptyPage(getString(R.string.no_data));
                break;
            case BasicResponse.TIME_OUT:
                showEmptyPage(text);
                break;
            case ModelResponse.REQUEST_RESULT_STATUS_NETOWRK_NOT_AVALIABLE:
                showEmptyPage(getString(R.string.errcode_network_unavailable));
                break;
        }
    }

    /**
     * 刷新页面
     */
    protected abstract void refresh();

    protected abstract void showEmptyPage(String text);

    protected void showDialog() {
        if (mProgressiveDialog == null) {
            mProgressiveDialog = new ProgressiveDialog(getActivity());
        }
        mProgressiveDialog.setMessage(R.string.loading);
        mProgressiveDialog.show();
    }

    protected void dismissDialog() {
        if (mProgressiveDialog != null && mProgressiveDialog.isShowing()) {
            mProgressiveDialog.dismiss();
        }
    }

}
