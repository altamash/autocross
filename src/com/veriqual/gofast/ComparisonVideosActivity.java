package com.veriqual.gofast;

import javax.xml.transform.Result;

import com.veriqual.gofast.model.Comparison;
import com.veriqual.gofast.model.Tagging;
import com.veriqual.gofast.model.Video;
import com.veriqual.gofast.utilites.Utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class ComparisonVideosActivity extends Activity {
	
	Comparison comparison;
//	Video firstVideo; 
//	Video secondVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comparison_videos);
		
		int index = getIntent().getExtras().getInt("index");
		comparison = Utilities.getComparisonsList(this).getComparisons().get(index);
		
		setupVideoView((VideoView) findViewById(R.id.v),
				comparison.getFirstVideo().getUrl());
		setupVideoView((VideoView) findViewById(R.id.v2),
				comparison.getSecondVideo().getUrl());
	}
	
	private void setupVideoView(final VideoView videoView, final String url) {
//		new AsyncTask<Void, Void, Void>() {
//
//			@Override
//			protected Void doInBackground(Void... params) {
				videoView.setVideoURI(Uri.parse(url));
				videoView.setMediaController(new MediaController(ComparisonVideosActivity.this));
//				return null;
//			}
//			
//		}.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comparison_videos, menu);
		return true;
	}

}
