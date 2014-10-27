/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.incito.driver.UI.detailDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import cn.com.incito.driver.R;


@SuppressLint("ValidFragment")
public class CityFragment extends Fragment {

    // 市，自治区集合
    public int[] cityArray = { R.array.beijin_province_item,
            R.array.shanghai_province_item, R.array.tianjin_province_item,
            R.array.chongqing_province_item, R.array.anhui_province_item,
            R.array.fujian_province_item, R.array.gansu_province_item,
            R.array.guangdong_province_item, R.array.guangxi_province_item,
            R.array.guizhou_province_item, R.array.aomen_province_item,
            R.array.heibei_province_item, R.array.jilin_province_item,
            R.array.heilongjiang_province_item, R.array.hubei_province_item,
            R.array.jiangsu_province_item, R.array.jiangxi_province_item,
            R.array.henan_province_item, R.array.hunan_province_item,
            R.array.sanxi_province_item, R.array.shanxi_province_item,
            R.array.shandong_province_item, R.array.neimenggu_province_item,
            R.array.sichuan_province_item, R.array.qinghai_province_item,
            R.array.liaoning_province_item, R.array.ningxia_province_item,
            R.array.zhejiang_province_item, R.array.yunnan_province_item,
            R.array.xizang_province_item, R.array.xinjiang_province_item,
            R.array.taiwan_province_item, R.array.hongkong_province_item };

    public Handler mHandler;
    public Context mContext;
    public int index;
    public static final int CITY = 1;
    public GridView gridView;

    public CityFragment() {
        super();
    }

    public CityFragment(Context context, Handler handler) {
        // TODO Auto-generated constructor stub
        mHandler = handler;
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_country, null);
        gridView = (GridView) view.findViewById(R.id.gridView);
        setAdapter();
        return view;
    }

    public void setAdapter() {
        gridView.setAdapter(new MyPagerAdapter());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void clearDatas() {
        if (gridView != null && gridView.getAdapter() != null) {
            ((MyPagerAdapter) gridView.getAdapter()).clearDatas();
        }
    }

    public class MyPagerAdapter extends BaseAdapter {

        public List<String> list;

        public MyPagerAdapter() {
            // TODO Auto-generated constructor stub
            String[] strs = getResources().getStringArray(cityArray[index]);
            list = new ArrayList<String>(Arrays.asList(strs));
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_city_country_grid_item, parent, false);
                holder = new ViewHolder();
                holder.button = (Button) convertView.findViewById(R.id.btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.button.setText(list.get(position));
            holder.button
                    .setOnClickListener(new mClickListener(index, position));
            return convertView;
        }

        public void clearDatas() {
            if (list != null) {
                list.clear();
                notifyDataSetChanged();
            }
        }
    }

    static class ViewHolder {
        Button button;
    }

    class mClickListener implements OnClickListener {

        public int mprovince;
        public int mcity;

        public mClickListener(int province, int city) {
            // TODO Auto-generated constructor stub
            mprovince = province;
            mcity = city;
        }

        @Override
        public void onClick(View v) {
            if (((PCCFragmentActivity) mContext).addStr(((Button) v).getText()
                    .toString(), CITY, 0)) {
                Message message = new Message();
                message.arg1 = mprovince;
                message.arg2 = mcity;
                message.what = CITY;
                mHandler.sendMessage(message);
            }
        }

    }

}