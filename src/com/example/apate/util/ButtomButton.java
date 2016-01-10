package com.example.apate.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apate.R;



public class ButtomButton extends RelativeLayout{
	
	public static final int TAG_START_BUTTON = 1;
	public static final int TAG_END_BUTTON = 2;
	public static final int TAG_PATH_BUTTON = 3;
	public static final int TAG_RUN_BUTTON =4;
	
	
	private Context context;
	private ImageView image;
	private TextView text;
	
	
	public ButtomButton(Context context) {
		super(context);
		this.context = context;
		initializeLayout();
	}

    public ButtomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
		initializeLayout();
    }

    public ButtomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
		initializeLayout();
    }
	
	
	private void initializeLayout() {
		LayoutInflater.from(context).inflate(R.layout.buttom_button, this, true);
		image = (ImageView) findViewById(R.id.image);
		text = (TextView) findViewById(R.id.text);
		
		String tag = (String)getTag();
		
		switch(Integer.valueOf(tag)) {
			case TAG_START_BUTTON:
				text.setText("起始位置");
				image.setImageResource(R.drawable.icon_start);
				break;
			case TAG_END_BUTTON:
				text.setText("终点位置");
				image.setImageResource(R.drawable.icon_end);
				break;
			case TAG_PATH_BUTTON:
				text.setText("路径规划");
				image.setImageResource(R.drawable.icon_path);
				break;
			case TAG_RUN_BUTTON:
				text.setText("开始");
				break;
		}
	}
	
	public void setView(int imageId, String content) {
		if(imageId >0) {
			image.setBackground(context.getResources().getDrawable(imageId));
		}
		if(content != null) {
			text.setText(content);
		}
	}
	
	
	
	
	
}
