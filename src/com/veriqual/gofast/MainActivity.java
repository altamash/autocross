package com.veriqual.gofast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void createComparison(View v) {
		Intent intent = new Intent(this, TaggingActivity.class);
		startActivity(intent);
	}

	public void list(View v) {
		Intent intent = new Intent(this, ComparisonListActivity.class);
		startActivity(intent);
	}
	
	public void listVideos(View v) {
		Intent intent = new Intent(this, VideosListActivity.class);
		startActivity(intent);
	}

	public void exit(View v) {
		finish();
		System.exit(0);
	}

}
