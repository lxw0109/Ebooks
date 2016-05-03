package com.example.sharedpreferencestest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button saveData;
	
	private Button restoreData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		saveData = (Button) findViewById(R.id.save_data);
		restoreData = (Button) findViewById(R.id.restore_data);
		saveData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
				editor.putString("name", "Tom");
				editor.putInt("age", 28);
				editor.putBoolean("married", false);
				editor.commit();
			}
		});
		restoreData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
				String name = pref.getString("name", "");
				int age = pref.getInt("age", 0);
				boolean married = pref.getBoolean("married", false);
				Log.d("MainActivity", "name is " + name);
				Log.d("MainActivity", "age is " + age);
				Log.d("MainActivity", "married is " + married);
			}
		});
	}

}
