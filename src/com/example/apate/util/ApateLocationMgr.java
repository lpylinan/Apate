package com.example.apate.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.route.DrivePath;

public class ApateLocationMgr {

    private Context context;

    private String mMockProviderName = LocationManager.GPS_PROVIDER;

    public static boolean isRuning = false;

    public static LatLng startLatLng;
    public static LatLng endLatLng;
    public static DrivePath drivePath;

    public static Thread refreshThread;

    private LocationManager locationManager;

    public ApateLocationMgr(Context context) {
        this.context = context;
        inilocation();
    }
    
    private void inilocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(mMockProviderName, false, true, false, false, true, true,
                true, 0, 5);
        locationManager.setTestProviderEnabled(mMockProviderName, true);
    }

    private Thread getThread() {
        if (refreshThread == null) {

        }
        return refreshThread;
    }

    public void setLocation(LatLng mLatLng) {
        Location location = new Location(mMockProviderName);
        location.setTime(System.currentTimeMillis());
        location.setLatitude(mLatLng.latitude);
        location.setLongitude(mLatLng.longitude);
        location.setAltitude(2.0f);
        location.setAccuracy(100);
        if(Build.VERSION.SDK_INT > 16){  
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());  
        }   
        locationManager.setTestProviderLocation(mMockProviderName, location);
    }

}
