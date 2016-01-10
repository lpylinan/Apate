package com.example.apate.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.apate.R;
import com.example.apate.base.BaseActivity;
import com.example.apate.util.ButtomButton;
import com.example.apate.util.LocationMgr;

public class MainMapActivity extends BaseActivity implements OnClickListener,
		InfoWindowAdapter, AMapLocationListener, OnCameraChangeListener,
		OnMapLoadedListener, OnInfoWindowClickListener{

	private LocationMgr locationMgr;
	
	public MapView mapView;
	private AMap aMap;
	
	private Marker centerMarker;
	private MarkerOptions centerMarkerOptions;
	private Marker startMarker;
	private Marker endMarker;
	
	
	//初始化定位
	public AMapLocationClient mLocationClient;
	private AMapLocationClientOption mLocationOption = null;

	private static final LatLng DEFAULT_LATLNG = new LatLng(39.915184,
			116.403945);
	
	//底部Button
	private ButtomButton startButton;
	private ButtomButton endButton;
	private ButtomButton pathButton;
	private ButtomButton runButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);
		initView();
		locationMgr = new LocationMgr(context);
		mLocationClient = new AMapLocationClient(getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(this);
		
		initMapView(savedInstanceState);
	}
	
	private void initView() {
		startButton = (ButtomButton) findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
		endButton = (ButtomButton) findViewById(R.id.endButton);
		endButton.setOnClickListener(this);
		pathButton = (ButtomButton) findViewById(R.id.pathButton);
		pathButton.setOnClickListener(this);
		runButton = (ButtomButton) findViewById(R.id.runButton);
		runButton.setOnClickListener(this);
	}

	private void initMapView(Bundle savedInstanceState) {
		AMapOptions aMapOptions = new AMapOptions();
		aMapOptions.camera(new CameraPosition(DEFAULT_LATLNG,
				14.141077041625977f, 0, 0));
		mapView = new MapView(this, aMapOptions);
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
		initCenter();
	}

	private OnMarkerClickListener markerClick = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {
			return true;
		}

	};

	@Override
	public void onMapLoaded() {
		refreshCenter(aMap.getCameraPosition().target);
		aMap.setOnCameraChangeListener(this);
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
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			if(amapLocation.getErrorCode() == 0){
			//定位成功回调信息，设置相关消息
			amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
			amapLocation.getLatitude();//获取纬度
			amapLocation.getLongitude();//获取经度
			amapLocation.getAccuracy();//获取精度信息
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(amapLocation.getTime());
			df.format(date);//定位时间
			amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
			amapLocation.getCountry();//国家信息
			amapLocation.getProvince();//省信息
			amapLocation.getCity();//城市信息
			amapLocation.getDistrict();//城区信息
			amapLocation.getRoad();//街道信息
			amapLocation.getCityCode();//城市编码
			amapLocation.getAdCode();//地区编码
			} else {
				//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError","location Error, ErrCode:"+ amapLocation.getErrorCode() + ", errInfo:"+amapLocation.getErrorInfo());
			}
		}
	}

	@Override
	public View getInfoContents(Marker arg0) {
		View windowLayout = View.inflate(this, R.layout.window_layout, null);
		TextView location_name = (TextView) windowLayout.findViewById(R.id.location_name);
		return windowLayout;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.startButton:
				setStartLocation(aMap.getCameraPosition().target);
				break;
			case R.id.endButton:
				setEndLocation(aMap.getCameraPosition().target);
				break;
			case R.id.pathButton:
				break;
			case R.id.runButton:
				break;
		}

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
		mLocationClient.stopLocation();
		mLocationClient.onDestroy();
	}
	
	private void initCenter() {
		if (centerMarker == null) {
			centerMarkerOptions = new MarkerOptions();
			centerMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
			centerMarkerOptions.title("固定弹出");
			centerMarker = aMap.addMarker(centerMarkerOptions);
		}
	}

	/**
	 * 刷新中心位置
	 */
	private void refreshCenter(LatLng latlng) {
		Point point = aMap.getProjection().toScreenLocation(latlng);
		centerMarker.setPosition(latlng);
		centerMarker.setPositionByPixels(point.x, point.y);
	}
	
	private void setStartLocation(LatLng latLng) {
		if(latLng == null) {
			return;
		}
		locationMgr.startLatLng = latLng;
		if(startMarker == null) {
			MarkerOptions options = new MarkerOptions();
			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_start_big));
			startMarker = aMap.addMarker(options);
		}
		startMarker.setPosition(latLng);
		startMarker.showInfoWindow();
		Log.d("lpy", "set startLocation lat:" + locationMgr.startLatLng.latitude + "  log:" + locationMgr.startLatLng.longitude);
	}
	
	private void setEndLocation(LatLng latLng) {
		if(latLng == null) {
			return;
		}
		locationMgr.endLatLng = latLng;
		if(endMarker == null) {
			MarkerOptions options = new MarkerOptions();
			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end_big));
			endMarker = aMap.addMarker(options);
		}
		endMarker.setPosition(latLng);
		endMarker.showInfoWindow();
		Log.d("lpy", "set endLocation lat:" + locationMgr.startLatLng.latitude + "  log:" + locationMgr.startLatLng.longitude);
	}

}
