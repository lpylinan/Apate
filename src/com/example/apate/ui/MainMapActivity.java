package com.example.apate.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.example.apate.R;
import com.example.apate.base.BaseActivity;

public class MainMapActivity extends BaseActivity implements OnClickListener,
		InfoWindowAdapter, AMapLocationListener, OnCameraChangeListener,
		OnMapLoadedListener, OnInfoWindowClickListener {

	public MapView mapView;
	private AMap aMap;

	private static final LatLng DEFAULT_LATLNG = new LatLng(39.915184,
			116.403945);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);

		initMapView(savedInstanceState);
	}

	private void initMapView(Bundle savedInstanceState) {
		AMapOptions baiduMapOptions = new AMapOptions();
		baiduMapOptions.camera(new CameraPosition(DEFAULT_LATLNG,
				14.141077041625977f, 0, 0));
		mapView = new MapView(this, baiduMapOptions);
		mapView.onCreate(savedInstanceState);// 必须要写
		((LinearLayout) findViewById(R.id.map_layout)).addView(mapView);
		aMap = mapView.getMap();
		aMap.setOnMapLoadedListener(this);
		// aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnInfoWindowClickListener(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.getUiSettings().setZoomControlsEnabled(false);

		aMap.setOnMarkerClickListener(markerClick);// 设置点击marker事件监听器

	}

	private OnMarkerClickListener markerClick = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {


			return true;
		}

	};

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
