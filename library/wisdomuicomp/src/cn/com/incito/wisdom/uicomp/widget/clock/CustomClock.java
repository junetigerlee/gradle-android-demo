package cn.com.incito.wisdom.uicomp.widget.clock;

import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.incito.wisdom.uicomp.R;

/**
 * 自定义时钟控件
 * 
 * @author qiujiaheng
 * 
 *         2013年10月15日
 */
@SuppressLint("NewApi")
public class CustomClock extends LinearLayout {

	private View mClockView;
	private Calendar mTime;
	private String mTimeZone;
	private boolean mAttached;
	@ExportedProperty
	private boolean mHasSeconds;
	private TextView mCalendarView, mHourView, mMinuteView;

	public CustomClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public CustomClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CustomClock(Context context) {
		super(context);
	}

	/**
	 * initview
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		mClockView = inflate(context,
				cn.com.incito.wisdom.uicomp.R.layout.clock_view, null);
		mCalendarView = (TextView) mClockView.findViewById(R.id.calendar);
		mHourView = (TextView) mClockView.findViewById(R.id.hour);
		mMinuteView = (TextView) mClockView.findViewById(R.id.mm);
		addView(mClockView, 0);
		createTime(mTimeZone);
	}

	private final ContentObserver mFormatChangeObserver = new ContentObserver(
			new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			onTimeChanged();
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			onTimeChanged();
		}
	};
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mTimeZone == null
					&& Intent.ACTION_TIMEZONE_CHANGED
							.equals(intent.getAction())) {
				final String timeZone = intent.getStringExtra("time-zone");
				createTime(timeZone);
			}
			onTimeChanged();
		}
	};
	private final Runnable mTicker = new Runnable() {
		public void run() {
			onTimeChanged();

			long now = SystemClock.uptimeMillis();
			long next = now + (1000 - now % 1000);

			getHandler().postAtTime(mTicker, next);
		}
	};

	private void createTime(String timeZone) {
		if (timeZone != null) {
			mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		} else {
			mTime = Calendar.getInstance();
		}
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		if (!mAttached) {
			mAttached = true;

			registerReceiver();
			registerObserver();

			createTime(mTimeZone);

			if (mHasSeconds) {
				mTicker.run();
			} else {
				onTimeChanged();
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		if (mAttached) {
			unregisterReceiver();
			unregisterObserver();

			getHandler().removeCallbacks(mTicker);

			mAttached = false;
		}
	}

	private void registerReceiver() {
		final IntentFilter filter = new IntentFilter();

		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

		getContext().registerReceiver(mIntentReceiver, filter, null,
				getHandler());
	}

	private void registerObserver() {
		final ContentResolver resolver = getContext().getContentResolver();
		resolver.registerContentObserver(Settings.System.CONTENT_URI, true,
				mFormatChangeObserver);
	}

	private void unregisterReceiver() {
		getContext().unregisterReceiver(mIntentReceiver);
	}

	private void unregisterObserver() {
		final ContentResolver resolver = getContext().getContentResolver();
		resolver.unregisterContentObserver(mFormatChangeObserver);
	}

	@SuppressLint("NewApi")
	private void onTimeChanged() {
		mTime.setTimeInMillis(System.currentTimeMillis());
		String time = DateFormat.format("yyyy年MM月dd日 kk:mm", mTime).toString();
		String[] strs = time.split(" ");
		StringBuilder builder = new StringBuilder();
		builder.append(getWeekOfDate());
		builder.append("\n");
		builder.append(strs[0]);
		SpannableString word = new SpannableString(builder);
		word.setSpan(new AbsoluteSizeSpan(15), 0, 3,
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		mCalendarView.setText(word);
		String[] clock = strs[1].split(":");
		mHourView.setText(clock[0]);
		mMinuteView.setText(clock[1]);
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public String getWeekOfDate() {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int w = mTime.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

}
