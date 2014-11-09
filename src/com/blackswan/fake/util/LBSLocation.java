package com.blackswan.fake.util;


import android.location.Location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.NearBarberShop;


/**
 * 百度定位API使用类，启动定位，当返回定位结果是停止定位
 * 
 * @author Lu.Jian
 * 
 */
public class LBSLocation {

	private static LBSLocation location = null;
	private static BaseApplication app = null;

	private MyLocationListenner myListener = new MyLocationListenner();
	public LocationClient mLocationClient = null;

	public static LBSLocation getInstance(BaseApplication application) {
		app = application;
		if (location == null) {
			location = new LBSLocation(app);
		}

		return location;
	}

	private LBSLocation(BaseApplication app) {
		mLocationClient = new LocationClient(app);
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
	}

	/**
	 * 开始定位请求，结果在回调中
	 */
	public void startLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		mLocationClient.setLocOption(option);
		mLocationClient.requestLocation();
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			app.currlocation = location;
			mLocationClient.stop();
			
			//根据当前位置，计算列表中每一项的距离
			for (NearBarberShop content : app.getBarbershops()) {

				float results[] = new float[1];
				if (location != null) {
					Location.distanceBetween(location.getLatitude(),
							location.getLongitude(), content.getLatitude(),
							content.getLongitude(), results);
				}
				float distance = results[0]/1000;
				content.setSDistance((Float) (distance == 0.0 ? "" : (float) results[0]));
			}
			//刷新列表
			app.getBarbershopListAdapter().notifyDataSetChanged();
			
		}

		public void onReceivePoi(BDLocation poiLocation) {
			
		}
	}
}
