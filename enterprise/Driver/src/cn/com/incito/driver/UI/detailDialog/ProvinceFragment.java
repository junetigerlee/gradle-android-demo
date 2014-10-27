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
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import cn.com.incito.driver.R;

@SuppressLint("ValidFragment")
public class ProvinceFragment extends Fragment implements OnClickListener {

    public int[] buttons = { R.id.beijing_btn, R.id.shanghai_btn,
            R.id.tianjin_btn, R.id.chongqing_btn, R.id.anhui_btn,
            R.id.fujian_btn, R.id.gansu_btn, R.id.guangdong_btn,
            R.id.guangxi_btn, R.id.guizhou_btn, R.id.aomen_btn, R.id.hebei_btn,
            R.id.jilin_btn, R.id.heilongjiang_btn, R.id.hubei_btn,
            R.id.jiangsu_btn, R.id.jiangxi_btn, R.id.henan_btn, R.id.hunan_btn,
            R.id.shanxi_btn, R.id.sanxi_btn, R.id.shandong_btn,
            R.id.neimenggu_btn, R.id.sichuan_btn, R.id.qinghai_btn,
            R.id.liaoning_btn, R.id.ningxia_btn, R.id.zhejiang_btn,
            R.id.yunnan_btn, R.id.xizang_btn, R.id.xinjiang_btn,
            R.id.taiwan_btn, R.id.xianggang_btn };

    public static final int PROVINCE = 0;
    public Handler mHandler;
    public Context mContext;

    public static final int[] municipalities = { R.id.beijing_btn, R.id.shanghai_btn,
            R.id.tianjin_btn, R.id.chongqing_btn, R.id.aomen_btn,
            R.id.taiwan_btn, R.id.xianggang_btn };
    
    public ProvinceFragment() {
        super();
    }

    public ProvinceFragment(Context context, Handler handler) {
        mHandler = handler;
        mContext = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test1", "ProvinceFragment onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("test1", "ProvinceFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_province, null);
        ViewHolder holder = new ViewHolder(buttons, view);
        for (int i = 0; i < holder.list.size(); i++) {
            ((Button) holder.list.get(i)).setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("test1", "ProvinceFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("test1", "ProvinceFragment onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("test1", "ProvinceFragment onSaveInstanceState");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == id) {
                if (((PCCFragmentActivity) mContext).addStr(((Button) v)
                        .getText().toString(), PROVINCE,id)) {
                    Message message = new Message();
                    message.arg1 = i;//index
                    message.arg2=id;//
                    message.what = PROVINCE;
                    mHandler.sendMessage(message);
                }
            }
        }
    }

    static class ViewHolder {
        List<Button> list = new ArrayList<Button>();

        public ViewHolder(int[] id, View view) {
            // TODO Auto-generated constructor stub
            for (int i = 0; i < id.length; i++) {
                Button btn = (android.widget.Button) view.findViewById(id[i]);
                list.add(btn);
            }
        }
    }

}