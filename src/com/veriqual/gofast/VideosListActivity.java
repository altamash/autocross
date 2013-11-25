package com.veriqual.gofast;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.veriqual.gofast.model.Comparison;
import com.veriqual.gofast.model.ComparisonsList;

public class VideosListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videos_list);

		ListView compList = (ListView) findViewById(R.id.comparisonList);
		
		final ArrayList<String> comparisonlist = new ArrayList<String>();
		
		for (Comparison comparison : ComparisonsList.getInstance().getComparisons()) {
			comparisonlist.add(comparison.getName());
		}
		final ArrayAdapter<String> sessionAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, comparisonlist);
		compList.setAdapter(sessionAdapter);
		compList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

}
