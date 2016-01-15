package com.tsdi.mcs.controls;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MyOrientationListener implements SensorEventListener {

	private Context					context;
	private SensorManager			mSensorManager;
	private Sensor					accelerometer;						// 加速度传感器
	private Sensor					magnetic;							// 地磁场传感器
	private float[]					accelerometerValues	= new float[3];
	private float[]					magneticFieldValues	= new float[3];

	private float					lastX;

	private OnOrientationListener	onOrientationListener;

	public MyOrientationListener(Context context) {
		this.context = context;
	}

	// 开始
	public void start() {
		// 获得传感器管理器
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			// 初始化加速度传感器
			accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			// 初始化地磁场传感器
			magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			if (accelerometer != null)
				mSensorManager.registerListener(this, accelerometer, Sensor.TYPE_ACCELEROMETER);
			if (magnetic != null)
				mSensorManager.registerListener(this, magnetic, Sensor.TYPE_MAGNETIC_FIELD);
		}

	}

	// 停止检测
	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			accelerometerValues = event.values;
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			magneticFieldValues = event.values;
		}
		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
		SensorManager.getOrientation(R, values);
		float x = (float) Math.toDegrees(values[0]);
		if (Math.abs(x - lastX) > 1.0) {
			Log.i("方向", "" + x);
			if (x < 0)
				x = x + 360;
			onOrientationListener.onOrientationChanged(x);
		}
		lastX = x;
	}

	public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
		this.onOrientationListener = onOrientationListener;
	}

	public interface OnOrientationListener {
		void onOrientationChanged(float x);
	}

}
