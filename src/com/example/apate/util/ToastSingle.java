package com.example.apate.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apate.R;

 
public class ToastSingle extends Toast {

	/* 静态toast */
	private static ToastSingle tost;
	/* 加载xml文件使用 */
	private LayoutInflater inflater;
	/* toast显示的View */
	private View view;
	/* toast中显示文字的控件 */
	private TextView text;

	private ToastSingle(Context context) {
		super(context);
	}

	public static ToastSingle getToast(Context context) {
		if (tost == null) {
			tost = new ToastSingle(context);
			tost.inflater = LayoutInflater.from(context);
			tost.view = tost.inflater.inflate(R.layout.toast_layout, null);
			tost.setView(tost.view);
			tost.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, context.getResources().getDimensionPixelOffset(R.dimen.dd_dimen_100px));
			tost.text = (TextView) tost.view.findViewById(R.id.TextViewInfo);
			tost.setDuration(Toast.LENGTH_SHORT);
		}
		return tost;
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void setText(CharSequence s) {
		tost.text.setText(s);
	}
}
