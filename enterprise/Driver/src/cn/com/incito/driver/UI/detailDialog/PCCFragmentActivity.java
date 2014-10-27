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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.driver.Constants;
import cn.com.incito.driver.R;
import cn.com.incito.driver.models.ModelSelectedCity;

public class PCCFragmentActivity extends FragmentActivity implements
        OnClickListener {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private List<Fragment> listFragments = new ArrayList<Fragment>();
    private ProvinceFragment provinceFragment;
    private CityFragment cityFragment;
    private CountryFragment countryFragment;

    private TextView textProvince;
    private TextView textCity;
    private TextView textCountry;
    private Button btn_confirm;
    private Button btn_cancle;
    public static final int RSPONSE = -1;
    /** 单项 */
    public static final int FLAG_SINGELSELECTION = 1;
    /** 多项 */
    public static final int FLAG_MULTIPLESELECTION = 2;
    /** 默认单选 */
    private int mFlag = FLAG_SINGELSELECTION;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case ProvinceFragment.PROVINCE:
                
                cityFragment.setIndex(msg.arg1);
                cityFragment.setAdapter();
                pager.setCurrentItem(1);
                
                for (int municipalities : ProvinceFragment.municipalities) {
                    if (municipalities == msg.arg2) {
                        pager.setCurrentItem(2);
                        countryFragment.setIndex_pc(msg.arg1, 0);
                        countryFragment.setAdapter();
                        break;
                    }

                }
                break;
            case CityFragment.CITY:
                countryFragment.setIndex_pc(msg.arg1, msg.arg2);
                countryFragment.setAdapter();
                pager.setCurrentItem(2);
                break;
            default:
                break;
            }
        };
    };

    public FragmentActivity mContext;
    private TextView number;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test1", "onCreate");
        setContentView(R.layout.activity_pcc_main);

        mContext = this;
        Intent i = getIntent();
        mFlag = i.getIntExtra("selectmodel", FLAG_SINGELSELECTION);
        totals = i.getIntExtra("totals", -1);
        totals_error=i.getStringExtra("totals_error");
        number = (TextView) findViewById(R.id.number);
        if (mFlag == FLAG_SINGELSELECTION) {
            number.setText(String.valueOf(1));
        } else {
            totals = totals != -1 ? totals : 5;
            number.setText(String.valueOf(totals));
        }

        if (mFlag == FLAG_MULTIPLESELECTION) {
            selectedCities = (ArrayList<ModelSelectedCity>) i
                    .getSerializableExtra("selectedCities");
        }
        if (selectedCities == null) {
            selectedCities = new ArrayList<ModelSelectedCity>();
        }
        if (selectedCities.size() > 0) {
            mCurrentSelected = selectedCities.size() - 1;
        }

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        textProvince = (TextView) findViewById(R.id.textProvince);
        textCity = (TextView) findViewById(R.id.textCity);
        textCountry = (TextView) findViewById(R.id.textCountry);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_confirm.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        setSelectedBtnsListener();
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        provinceFragment = new ProvinceFragment(mContext, handler);
        cityFragment = new CityFragment(mContext, handler);
        countryFragment = new CountryFragment(mContext, handler);
        listFragments.add(provinceFragment);
        listFragments.add(cityFragment);
        listFragments.add(countryFragment);
        notifySelectedCities();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "省/直辖市", "市", "县", };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }
    }

    /** 已选目标城市集合 */
    private ArrayList<ModelSelectedCity> selectedCities;
    private int mCurrentSelected = -1;
    /** 指示最多可选几个城市 */
    private int totals;
    private String totals_error;

    private boolean validateMunicipalities(int resId) {
        boolean mFlag = false;
        for (int municipalities : ProvinceFragment.municipalities) {
            if (municipalities == resId) {
                mFlag = true;
                break;
            }

        }
        return mFlag;
    }
    
    public boolean addStr(String str, int flag,int resId) {
        boolean result = false;
        switch (flag) {

        case ProvinceFragment.PROVINCE:
            if (mFlag == FLAG_SINGELSELECTION) {
                selectedCities.clear();
                ModelSelectedCity selectedCity = new ModelSelectedCity();
                selectedCity.province = str;
//                selectedCity.city = "";
                if(validateMunicipalities(resId)){
                    selectedCity.city = str;
                }else{
                    selectedCity.city = "";
                }
                selectedCity.country = "";
                selectedCity.provinceResId=resId;
                selectedCities.add(selectedCity);
                result = true;
            } else {
                ModelSelectedCity selectedCity = null;

                if (selectedCities.size() == totals) {
                    if(null!=totals_error&&!TextUtils.isEmpty(totals_error)){
                        Toast.makeText(mContext, totals_error,
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "最多可选"+totals+"个城市，您已经选了"+totals+"个城市！",
                                Toast.LENGTH_SHORT).show();
                    }
                    return result;
                }
                // 如果刚才的选择没有选择2级城市的话
                if (selectedCities.size() > 0
                        && (selectedCities.get(mCurrentSelected).city == null || TextUtils
                                .isEmpty(selectedCities.get(mCurrentSelected).city))) {
                    
                    if(!validateMunicipalities(selectedCities
                                .get(mCurrentSelected).provinceResId)){
                        pager.setCurrentItem(1);
                        Toast.makeText(mContext, "必须选择二级城市", Toast.LENGTH_SHORT)
                                .show();
                        return result;
                    }else{
                        textCountry.setText(str);
                    }
                   
                }
                mCurrentSelected++;
                selectedCity = new ModelSelectedCity();
                selectedCity.province = str;
//                selectedCity.city = "";
                if(validateMunicipalities(resId)){
                    selectedCity.city = str;
                }else{
                    selectedCity.city = "";
                }
                selectedCity.country = "";
                selectedCity.provinceResId=resId;
                selectedCities.add(selectedCity);
                // mCurrentSelected = selectedCities.size() - 1;
                // }
                result = true;
            }
            textCountry.setText("");
            textCity.setText("");
            textProvince.setText(str);
            notifySelectedCities();
            break;
        case CityFragment.CITY:
            if (mFlag == FLAG_SINGELSELECTION) {
                if (selectedCities.size() > 0) {
                    selectedCities.get(0).city = str;
                } else {
                    Toast.makeText(mContext, "请先选择省", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
            } else {
                if (mCurrentSelected == -1) {
                    Toast.makeText(mContext, "请先选择省", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
                
                if (selectedCities.size() == totals&&validateData(selectedCities)) {
                    if(null!=totals_error&&!TextUtils.isEmpty(totals_error)){
                        Toast.makeText(mContext, totals_error,
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "最多可选"+totals+"个城市，您已经选了"+totals+"个城市！",
                                Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
                
                if (selectedCities.get(mCurrentSelected).city != null
                        && !TextUtils.isEmpty(selectedCities
                                .get(mCurrentSelected).city)
                        && !str.equals(selectedCities.get(mCurrentSelected).city)) {
                    ModelSelectedCity selectedCity = new ModelSelectedCity();
                    selectedCity.province = selectedCities
                            .get(mCurrentSelected).province;
                    selectedCity.city = str;
                    selectedCity.country = "";
                    selectedCities.add(selectedCity);
                    mCurrentSelected++;
                } else {
                    selectedCities.get(mCurrentSelected).city = str;
                }
            }
            textCountry.setText("");
            textCity.setText(str);
            result = true;
            notifySelectedCities();
            break;
        case CountryFragment.COUNTRY:
            if (mFlag == FLAG_SINGELSELECTION) {
                if (selectedCities.size() > 0) {
                    selectedCities.get(0).country = str;
                } else {
                    Toast.makeText(mContext, "请先选择市", Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
            } else {
                if (mCurrentSelected == -1) {
                    Toast.makeText(mContext, "请先选择市", Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
                selectedCities.get(mCurrentSelected).country = str;
            }
            textCountry.setText(str);
            result = true;
            notifySelectedCities();
            break;
        default:
            break;
        }
        return result;
    }

    /**
     * 验证已选择城市集合中，城市是否为空
     * @param selectedCities
     * @return
     */
    private boolean validateData(ArrayList<ModelSelectedCity> selectedCities){
        
        for (ModelSelectedCity modelSelectedCity : selectedCities) {
            if(TextUtils.isEmpty(modelSelectedCity.city)){
                return false;
            }
        }
        return true;
        
    }
    
    /**
     * 设置右侧显示被选中的按钮的点击监听事件
     */
    private void setSelectedBtnsListener() {
        LinearLayout selectedLayout = (LinearLayout) findViewById(R.id.selected_cities_linearlayout);
        for (int i = 0; i < selectedLayout.getChildCount(); i++) {
            Button selectedBtn = (Button) selectedLayout.getChildAt(i);
            selectedBtn.setTag(i);
            selectedBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int indext = (Integer) v.getTag();
                    selectedCities.remove(indext);
                    notifySelectedCities();
                    pager.setCurrentItem(0);
                    cityFragment.clearDatas();
                    countryFragment.clearDatas();
                    if (selectedCities.size() > 0) {
                        mCurrentSelected = selectedCities.size() - 1;
                    } else {
                        mCurrentSelected = -1;
                    }
                }
            });
        }
    }

    /**
     * 更新已选城市的按钮显示
     */
    private void notifySelectedCities() {
        LinearLayout selectedLayout = (LinearLayout) findViewById(R.id.selected_cities_linearlayout);
        for (int i = 0; i < selectedLayout.getChildCount(); i++) {
            Button selectedBtn = (Button) selectedLayout.getChildAt(i);
            if (i < selectedCities.size() && selectedCities.get(i) != null) {
                selectedBtn.setVisibility(View.VISIBLE);
                ModelSelectedCity selectedCity = selectedCities.get(i);
                if (selectedCity.country != null
                        && !TextUtils.isEmpty(selectedCity.country)) {
                    selectedBtn.setText(selectedCity.country);
                } else if (selectedCity.city != null
                        && !TextUtils.isEmpty(selectedCity.city)) {
                    selectedBtn.setText(selectedCity.city);
                } else {
                    selectedBtn.setText(selectedCity.province);
                }
            } else {
                selectedBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.btn_confirm:
            // 判断最后的选项是否信息完整
            if (selectedCities.size() > 0) {
                ModelSelectedCity city = selectedCities.get(selectedCities
                        .size() - 1);
                if (city.city == null || TextUtils.isEmpty(city.city)) {
                    pager.setCurrentItem(1);
                    Toast.makeText(mContext, "必须选择二级城市", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }
            // 判断结果里面是否有相同的对象
            boolean isHaveSameCity = false;
            a: for (int i = 0; i < selectedCities.size(); i++) {
                ModelSelectedCity city = selectedCities.get(i);
                for (int j = i + 1; j < selectedCities.size(); j++) {
                    if (city.equals(selectedCities.get(j))) {
                        isHaveSameCity = true;
                        break a;
                    }
                }
            }
            if (isHaveSameCity) {
                Toast.makeText(mContext, "请不要选择相同的城市", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            Intent intent = new Intent();
            // 把返回数据存入Intent
            intent.putExtra("province", textProvince.getText());
            intent.putExtra("city", textCity.getText());
            intent.putExtra("country", textCountry.getText());
            // 设置返回数据
            if (Constants.ISMYANDPUBLICCAR && selectedCities.size() == 1) {
                if ("".equals(selectedCities.get(0).city)
                        || selectedCities.get(0).city == null) {
                    Toast.makeText(mContext, "请至少选择城市", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
            }
            // 将选择的城市的集合返回
            intent.putExtra("selectedCities", selectedCities);
            PCCFragmentActivity.this.setResult(RSPONSE, intent);
            // 关闭Activity
            PCCFragmentActivity.this.finish();
            break;
        case R.id.btn_cancle:
            PCCFragmentActivity.this.finish();
            break;
        default:
            break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test1", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("test1", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test1", "onStop");
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test1", "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("test1", "onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("test1", "onSaveInstanceState(Bundle outState) {");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}