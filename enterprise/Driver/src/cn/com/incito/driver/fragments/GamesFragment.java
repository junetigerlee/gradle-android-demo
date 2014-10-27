
package cn.com.incito.driver.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.driver.R;
import cn.com.incito.driver.models.AppInfo;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

/**
 * 娱乐界面
 * 
 * @description 娱乐界面
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GamesFragment extends Fragment {

    private ProgressiveDialog mProgressDialog;

    ArrayList<AppInfo> appList;

    private MyGridAdapter mGridAdapter;

    private GridView mGridview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_games, null);
        mGridview = (GridView) view.findViewById(R.id.grid_view);
        mProgressDialog = new ProgressiveDialog(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appList = new ArrayList<AppInfo>();
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());

            // Only display the non-system app info
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);
            }

        }
        for (int i = 0; i < appList.size(); i++) {
            appList.get(i).print();
        }
        mGridAdapter = new MyGridAdapter();
        mGridview.setAdapter(mGridAdapter);
        mGridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(appList.get(arg2).packageName);
                startActivity(i);
            }
        });

    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appList = null;
    }

    private static class ViewHolder {
        public ImageView mIcon;

        public TextView mAppName;

    }

    public class MyGridAdapter extends BaseAdapter {

        public MyGridAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return appList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return appList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_games_gridview_item, null);
                viewholder = new ViewHolder();
                viewholder.mAppName = (TextView) convertView.findViewById(R.id.appname);
                viewholder.mIcon = (ImageView) convertView.findViewById(R.id.image_app_icon);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.mAppName.setText(appList.get(position).appName);
            viewholder.mIcon.setImageDrawable(appList.get(position).appIcon);

            return convertView;
        }
    }

}
