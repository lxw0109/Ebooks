package com.example.uisizetest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		float xdpi = getResources().getDisplayMetrics().xdpi;
		float ydpi = getResources().getDisplayMetrics().ydpi;
		Log.d("MainActivity", "xdpi is " + xdpi);
		Log.d("MainActivity", "ydpi is " + ydpi);
	}

}