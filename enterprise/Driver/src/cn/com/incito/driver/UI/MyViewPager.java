package cn.com.incito.driver.UI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private boolean isScrollable;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}

	}

}
