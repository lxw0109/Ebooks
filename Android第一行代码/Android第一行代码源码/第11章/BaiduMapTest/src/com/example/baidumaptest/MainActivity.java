package com.example.baidumaptest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MainActivity extends Activity {

	private BMapManager manager;

	private MapView mapView;

	private LocationManager locationManager;

	private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = new BMapManager(this);
		// API Key需要替换成你自己的
		manager.init("SHVPoTtIpzfonPD3HCkc5sIt", null);
		setContentView(R.layout.activity_main);
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 获取所有可用的位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			// 当没有可用的位置提供器时，弹出Toast提示用户
			Toast.makeText(this, "No location provider to use",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			navigateTo(location);
		}
	}

	private void navigateTo(Location location) {
		MapController controller = mapView.getController();
		// 设置缩放级别
		controller.setZoom(16);
		GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
		// 设置地图中心点
		controller.setCenter(point);
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mapView);
		LocationData locationData = new LocationData();
		// 指定我的位置
		locationData.latitude = location.getLatitude();
		locationData.longitude = location.getLongitude();
		myLocationOverlay.setData(locationData);
		mapView.getOverlays().add(myLocationOverlay);
		// 刷新使新增覆盖物生效
		mapView.refresh();
		PopupOverlay pop = new PopupOverlay(mapView, new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				// 相应图片的点击事件
				Toast.makeText(MainActivity.this,
						"You clicked button " + index, Toast.LENGTH_SHORT)
						.show();
			}
		});
		// 创建一个长度为3的Bitmap数组
		Bitmap[] bitmaps = new Bitmap[3];
		try {
			// 将三张图片读取到内存中
			bitmaps[0] = BitmapFactory.decodeResource(getResources(),
					R.drawable.left);
			bitmaps[1] = BitmapFactory.decodeResource(getResources(),
					R.drawable.middle);
			bitmaps[2] = BitmapFactory.decodeResource(getResources(),
					R.drawable.right);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pop.showPopup(bitmaps, point, 18);
	}

	@Override
	protected void onDestroy() {
		mapView.destroy();
		if (manager != null) {
			manager.destroy();
			manager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		if (manager != null) {
			manager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		if (manager != null) {
			manager.start();
		}
		super.onResume();
	}

}
