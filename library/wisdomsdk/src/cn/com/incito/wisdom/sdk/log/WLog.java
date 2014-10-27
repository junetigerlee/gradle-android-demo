package cn.com.incito.wisdom.sdk.log;

import java.util.Set;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class WLog {

	private static boolean isCustomLogEnable = false;
	private static boolean isErrorLogEnable = true;
	private static boolean isDebugLogEnable = false;
	private static boolean isInfoLogEnable = false;
	private static boolean isVerboseLogEnable = false;
	private static boolean isWarnLogEnable = false;
	
	private WLog() {
	}
	
	public static void setFullLogLevel() {
		isCustomLogEnable = true;
		isErrorLogEnable = true;
		isDebugLogEnable = true;
		isInfoLogEnable = true;
		isVerboseLogEnable = true;
		isWarnLogEnable = true;
	}
	
	public static void setDebugLogLevel() {
		isCustomLogEnable = true;
		isErrorLogEnable = true;
		isDebugLogEnable = true;
		isInfoLogEnable = false;
		isVerboseLogEnable = false;
		isWarnLogEnable = true;
	}
	
	public static void setReleaseLogLevel() {
		isCustomLogEnable = false;
		isErrorLogEnable = true;
		isDebugLogEnable = false;
		isInfoLogEnable = false;
		isVerboseLogEnable = false;
		isWarnLogEnable = false;
	}
	
	public static void setProductionLogLevel() {
		isCustomLogEnable = false;
		isErrorLogEnable = false;
		isDebugLogEnable = false;
		isInfoLogEnable = false;
		isVerboseLogEnable = false;
		isWarnLogEnable = false;
	}
	
	public static boolean isCustomLogEnable() {
		return isCustomLogEnable;
	}
	
	public static boolean isErrorLogEnable() {
		return isErrorLogEnable;
	}
	
	public static boolean isDebugLogEnable() {
		return isDebugLogEnable;
	}
	
	public static boolean isInfoLogEnable() {
		return isInfoLogEnable;
	}
	
	public static boolean isVerboseLogEnable() {
		return isVerboseLogEnable;
	}
	
	public static boolean isWarnLogEnable() {
		return isWarnLogEnable;
	}
	
	public static void setErrorLogEnable(boolean isEnable) {
		isErrorLogEnable = isEnable;
	}
	
	public static void setDebugLogEnable(boolean isEnable) {
		isDebugLogEnable = isEnable;
	}
	
	public static void setInfoLogEnable(boolean isEnable) {
		isInfoLogEnable = isEnable;
	}
	
	public static void setVerboseLogEnable(boolean isEnable) {
		isVerboseLogEnable = isEnable;
	}
	
	public static void setWarnLogEnable(boolean isEnable) {
		isWarnLogEnable = isEnable;
	}
	
	public static void setCustomLogEnable(boolean isEnable) {
		isCustomLogEnable = isEnable;
	}
	
	public static void e(Class<?> clazz, String msg) {
		if(isErrorLogEnable) {
			Log.e(clazz.getSimpleName(), msg);
		}
	}
	
	public static void e(Class<?> clazz, String msg, Throwable t) {
		if(isErrorLogEnable) {
			Log.e(clazz.getSimpleName(), msg, t);
		}
	}
	
	public static void d(Class<?> clazz, String msg) {
		if(isDebugLogEnable) {
			Log.d(clazz.getSimpleName(), msg);
		}
	}
	
	public static void d(Class<?> clazz, String msg, Throwable t) {
		if(isDebugLogEnable) {
			Log.d(clazz.getSimpleName(), msg, t);
		}
	}
	
	public static void i(Class<?> clazz, String msg) {
		if(isInfoLogEnable) {
			Log.i(clazz.getSimpleName(), msg);
		}
	}
	
	public static void i(Class<?> clazz, String msg, Throwable t) {
		if(isInfoLogEnable) {
			Log.i(clazz.getSimpleName(), msg, t);
		}
	}
	
	public static void v(Class<?> clazz, String msg) {
		if(isVerboseLogEnable) {
			Log.v(clazz.getSimpleName(), msg);
		}
	}
	
	public static void v(Class<?> clazz, String msg, Throwable t) {
		if(isVerboseLogEnable) {
			Log.v(clazz.getSimpleName(), msg, t);
		}
	}
	
	public static void w(Class<?> clazz, String msg) {
		if(isWarnLogEnable) {
			Log.w(clazz.getSimpleName(), msg);
		}
	}
	
	public static void w(Class<?> clazz, String msg, Throwable t) {
		if(isWarnLogEnable) {
			Log.w(clazz.getSimpleName(), msg, t);
		}
	}

	public static boolean isLoggable(Class<?> tag, int level) {
		return Log.isLoggable(tag.getSimpleName(), level);
	}
	
	public static void logCursor(Class<?> tag, Cursor cursor) {
		if (isCustomLogEnable) {
			if (cursor != null) {
				int count = cursor.getColumnCount();
				if (count == 0) {
					Log.e(tag.getSimpleName(), "The cursor is empty");
				}
				StringBuilder sb = new StringBuilder("Cursor Column");
				for (int i = 0; i < count; i++) {
					sb.append(cursor.getColumnName(i) + "\t");
				}
				Log.i(tag.getSimpleName(), sb.toString());

				if (cursor.moveToFirst()) {
					do {
						sb.delete(0, sb.length());
						sb.append("Cursor Data)");
						for (int i = 0; i < count; i++) {
							sb.append(cursor.getString(i) + "\t");
						}
						Log.i(tag.getSimpleName(), sb.toString());
					} while (cursor.moveToNext());
				}
			}else{
				Log.e(tag.getSimpleName(), "The cursor is null");
			}
		}
	}

	public static void logBundle(Bundle b) {
		if (isCustomLogEnable) {
			if (b != null) {
				Set<String> set = b.keySet();
				if (set.size() > 0) {
					for (String key : set) {
						Object obj = (Object) b.get(key);
						if (obj != null) {
							Log.e("Bundle extra", "key = " + key + " value = " + obj);
						}
					}
				}
			}
		}
	}

	@TargetApi(4)
	public static void logIntent(Intent it) {
		if (isCustomLogEnable) {
			if (it != null) {
				Log.e("Intent", "Package " + it.getPackage());
				Log.e("Intent", "Action " + it.getAction());
				Log.e("Intent", "Flag " + it.getFlags());
				Bundle payload = it.getExtras();
				logBundle(payload);
			}
		}
	}
	
	public static void logStackTrace(Exception e) {
		e.printStackTrace();
	}
	
	public static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}
}
