package com.example.apate.application;

import android.util.SparseArray;

import com.example.apate.util.BaseActivityCloseListener;

public class ApateApp extends android.app.Application {
	private static ApateApp instance;

	public SparseArray<SparseArray<BaseActivityCloseListener>> closeMap;
	public static final String ACTION_CLOSE_ALL_ACTIVITY = "com.citycowry.app.action_close_all_activity";

	public static String cookieStr = "";

	public void onCreate() {
		super.onCreate();
		initInstance();
	}

	public void initInstance() {
		closeMap = new SparseArray<SparseArray<BaseActivityCloseListener>>();
		instance = this;
	}

	public static ApateApp getInstance() {
		return instance;
	}

	public synchronized void putClosePath(int key,
			BaseActivityCloseListener listener) {
		if (closeMap.indexOfKey(key) < 0) {
			SparseArray<BaseActivityCloseListener> sa = new SparseArray<BaseActivityCloseListener>();
			sa.put(sa.size(), listener);
			closeMap.put(key, sa);
		} else {
			SparseArray<BaseActivityCloseListener> sa = closeMap.get(key);
			if (sa.indexOfValue(listener) < 0) {
				sa.put(sa.size(), listener);
			}
		}
	}

	public synchronized void popClosePath(boolean finish, int key) {
		if (closeMap.indexOfKey(key) >= 0) {
			SparseArray<BaseActivityCloseListener> sa = closeMap.get(key);
			if (finish) {
				int s = sa.size();
				for (int i = 0; i < s; i++) {
					BaseActivityCloseListener bl = sa.get(sa.keyAt(i));
					bl.onFinish();
				}
			}
			sa.clear();
			closeMap.remove(key);
		}
	}

}
