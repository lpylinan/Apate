package com.example.apate.util;

import android.content.Context;

import com.amap.api.maps.model.LatLng;

public class LocationMgr {
	
	private Context context;
	
	public static boolean isRuning = false;
	
	public static LatLng startLatLng;
	public static LatLng endLatLng;
	
	public LocationMgr(Context context) {
		this.context = context;
	}

}
