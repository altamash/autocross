package com.veriqual.gofast;

import java.io.IOException;

import com.veriqual.gofast.model.ComparisonsList;
import com.veriqual.gofast.utilites.Utilities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		Intent intent = new Intent(this, ListActivity.class);
		startActivity(intent);
	}

}
