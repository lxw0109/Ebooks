package com.example.compasstest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private SensorManager sensorManager;

	private ImageView compassImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		compassImg = (ImageView) findViewById(R.id.compass_img);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor magneticSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accelerometerSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(listener, magneticSensor,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(listener, accelerometerSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sensorManager != null) {
			sensorManager.unregisterListener(listener);
		}
	}

	private SensorEventListener listener = new SensorEventListener() {

		float[] accelerometerValues = new float[3];

		float[] magneticValues = new float[3];

		private float lastRotateDegree;

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 判断当前是加速度传感器还是地磁传感器
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				// 注意赋值时要调用clone()方法
				accelerometerValues = event.values.clone();
			} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				// 注意赋值时要调用clone()方法
				magneticValues = event.values.clone();
			}
			float[] values = new float[3];
			float[] R = new float[9];
			SensorManager.getRotationMatrix(R, null, accelerometerValues,
					magneticValues);
			SensorManager.getOrientation(R, values);
			float rotateDegree = -(float) Math.toDegrees(values[0]);
			if (Math.abs(rotateDegree - lastRotateDegree) > 1) {
				RotateAnimation animation = new RotateAnimation(
						lastRotateDegree, rotateDegree,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setFillAfter(true);
				compassImg.startAnimation(animation);
				lastRotateDegree = rotateDegree;
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

	};

}
