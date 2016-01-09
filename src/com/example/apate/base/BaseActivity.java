package com.example.apate.base;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.apate.R;
import com.example.apate.application.ApateApp;
import com.example.apate.util.ToastSingle;

public class BaseActivity extends FragmentActivity {
	public FragmentManager manager;
	public FinderCarHandler handler;
	public Context context;

	public ApateApp app;
	private HashSet<String> tagSet = new HashSet<String>();

	private long clickTime;

	public boolean isActive = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		handler = new FinderCarHandler(this);
		manager = getSupportFragmentManager();
		app = ApateApp.getInstance();
	}

	public void showLoading(final boolean show) {
		final FrameLayout fl = (FrameLayout) findViewById(R.id.base_loading);
		if (fl != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (isFinishing()) {
						return;
					}

					fl.removeAllViews();
					if (show) {
						View load = LayoutInflater.from(context).inflate(
								R.layout.base_loading_layout, null);
						fl.addView(load);
					}
				}
			});

		}
	}

	public void showToast(final String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		handler.post(new Runnable() {

			@Override
			public void run() {
				ToastSingle toast = ToastSingle.getToast(context);
				toast.setText(msg);
				toast.show();
			}
		});
	}

	public class FinderCarHandler extends Handler {

		private WeakReference<Activity> activities = null;

		public FinderCarHandler(Activity activity) {
			activities = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (activities.get() == null || activities.get().isFinishing()) {
				return;
			}
		}

	}

	public void collapseSoftInputMethod() {
		try {
			((InputMethodManager) this
					.getSystemService(context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(this.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {

		}
	}

	public void exit() {
		if ((System.currentTimeMillis() - clickTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
					Toast.LENGTH_SHORT).show();
			clickTime = System.currentTimeMillis();
		} else {
			finishAllActivity();
		}
	}

	private void finishAllActivity() {
		Intent intent = new Intent();
		intent.setAction(app.ACTION_CLOSE_ALL_ACTIVITY);
		sendBroadcast(intent);

	}

	public void showFragment(Fragment fragment, String tag) {
		showFragment(fragment, tag, -1);
	}

	public void showFragment(Fragment fragment, String tag, int extraId) {

		if (this.isFinishing() || manager == null) {
			return;
		}
		if (fragment == null || TextUtils.isEmpty(tag)) {
			return;
		}

		Fragment prev = manager.findFragmentByTag(tag);
		if (prev != null) {
			return;
		}
		tagSet.add(tag);
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		if (extraId == -1) {
			fragmentTransaction.add(R.id.base_extra_layout, fragment, tag);
		} else {
			fragmentTransaction.add(extraId, fragment, tag);
		}
		fragmentTransaction.commitAllowingStateLoss();

	}

	/**
	 * 关闭指定tag的Fragment
	 * 
	 * @param tag
	 */
	public boolean closeFragment(final String tag) {
		try {
			if (this.isFinishing() || manager == null) {
				return false;
			}

			if (!manager.popBackStackImmediate(tag,
					FragmentManager.POP_BACK_STACK_INCLUSIVE)) {
				FragmentTransaction ft = manager.beginTransaction();
				Fragment prev = manager.findFragmentByTag(tag);
				if (prev != null) {
					int i = ft.remove(prev).commitAllowingStateLoss();
					return i > 0 ? true : false;
				}
			} else {
				Log.d("closeFragment", "closeFragment popBackStackImmediate");

				return true;
			}

		} catch (Exception e) {
		}

		return false;
	}

	@Override
	public void onBackPressed() {
		if (tagSet.size() > 0) {
			int i = 0;
			boolean hasShow = false;
			HashSet<String> removeList = new HashSet<String>();
			for (String tag : tagSet) {
				Fragment prev = manager.findFragmentByTag(tag);
				if (prev != null && prev.isVisible()) {
					hasShow = true;
					if (i == (tagSet.size() - 1)) {
						removeList.add(tag);
						closeFragment(tag);
						break;
					} else {
						i++;
					}
				} else {
					i++;
					removeList.add(tag);
				}
			}
			if (hasShow) {
				if (removeList.size() > 0) {
					for (String tag : removeList) {
						tagSet.remove(tag);
					}
				} else {
					super.onBackPressed();
					tagSet.clear();
				}
			} else {
				super.onBackPressed();
				tagSet.clear();
			}
		} else {
			super.onBackPressed();
			tagSet.clear();
		}
	}

	public boolean isFragmentShow() {
		if (tagSet.size() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isActive) {
			// 进入前台
			isActive = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isActive) {
			isActive = false;
		}
	}
}
