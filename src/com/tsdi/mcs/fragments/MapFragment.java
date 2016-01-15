package com.tsdi.mcs.fragments;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tsdi.mcs.R;
import com.tsdi.mcs.controls.MyOrientationListener;
import com.tsdi.mcs.controls.MyOrientationListener.OnOrientationListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MapFragment extends Fragment implements BDLocationListener {
	@ViewInject(R.id.bmapview)
	private MapView					mMapView;
	@ViewInject(R.id.btnplace)
	private Button					btnplace;

	private BaiduMap				mBaiduMap;
	private LocationClient			mLocClient;
	boolean							isFirstLoc	= true;	// 是否首次定位
	private UiSettings				mUiSettings;
	private MyOrientationListener	myOrientationListener;
	private int						mXDirection;
	private double					mCurrentLatitude;
	private double					mCurrentLongitude;
	private float					mCurrentRadius;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_map, container, false);
		ViewUtils.inject(this, root);
		mBaiduMap = mMapView.getMap();
		mUiSettings = mBaiduMap.getUiSettings();
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.FOLLOWING, true, null));
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(this);

		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setOpenGps(true);
		locationClientOption.setCoorType("bd09ll");
		locationClientOption.setScanSpan(1000);
		locationClientOption.setIsNeedAddress(true);
		mLocClient.setLocOption(locationClientOption);

		// 传感器初始化
		myOrientationListener = new MyOrientationListener(getActivity());
		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			@Override
			public void onOrientationChanged(float x) {
				mXDirection = (int) x;
			}
		});
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		SDKInitializer.initialize(getActivity().getApplicationContext());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		if (location == null || mMapView == null) {
			return;
		}
		mCurrentLatitude = location.getLatitude();
		mCurrentLongitude = location.getLongitude();
		mCurrentRadius = location.getRadius();
		MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(mXDirection).latitude(mCurrentLatitude).longitude(mCurrentLongitude).build();
		mBaiduMap.setMyLocationData(locData);
		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(mCurrentLatitude, mCurrentLongitude);
			getCurrentLocation();

			// 构建文字Option对象，用于在地图上添加文字
			OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30).position(ll);
			// 在地图上添加该文字对象并显示
			mBaiduMap.addOverlay(textOption);

			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions overlayOptions = new MarkerOptions().position(ll).icon(bitmap);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(overlayOptions);
		}
	}

	@OnClick(R.id.btnplace)
	public void OnClick(View v) {
		getCurrentLocation();
	}

	private void getCurrentLocation() {
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(mCurrentLatitude, mCurrentLongitude)));
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		myOrientationListener.start();
		mBaiduMap.setMyLocationEnabled(true);
		if (mLocClient.isStarted() == false) {
			mLocClient.start();// 开启定位
		}
	}

	@Override
	public void onDestroy() {
		if (mMapView != null) {
			myOrientationListener.stop();
			mLocClient.stop();
			mBaiduMap.setMyLocationEnabled(false);
			mMapView.onDestroy();
		}
		super.onDestroy();
	}

	@Override
	public void onPause() {
		if (mMapView != null)
			mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		if (mMapView != null)
			mMapView.onResume();
		super.onResume();
	}

}
