package com.veriqual.gofast;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import com.veriqual.gofast.model.Comparison;
import com.veriqual.gofast.model.Video;
import com.veriqual.gofast.utilites.ComparisonImpl;
import com.veriqual.gofast.utilites.Utilities;

public class ComparisonVideosActivity extends Activity implements OnBufferingUpdateListener,

OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener,
		SurfaceHolder.Callback{
	
	Comparison comparison;
//	Video firstVideo; 
//	Video secondVideo;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private MediaPlayer mMediaPlayer2;
	private SurfaceView mPreview2;
	private SurfaceHolder holder2;
	private SeekBar seekBar;
	
	private static final String TAG = "";
	private int mVideoWidth;
	private int mVideoHeight;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private int mVideoWidth2;
	private int mVideoHeight2;
	private boolean mIsVideoSizeKnown2 = false;
	private boolean mIsVideoReadyToBePlayed2 = false;
	ComparisonImpl firstComparisonImpl;
	ComparisonImpl secondComparisonImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comparison_videos);
		
		int index = getIntent().getExtras().getInt("index");
		comparison = Utilities.getComparisonsList(this).getComparisons().get(index);
		
//		setupVideoView((VideoView) findViewById(R.id.v),
//				comparison.getFirstVideo().getUrl());
//		setupVideoView((VideoView) findViewById(R.id.v2),
//				comparison.getSecondVideo().getUrl());
		
		Video v1 = comparison.getFirstVideo();
		Video v2 = comparison.getSecondVideo();
		long duration1 = getDuration(v1);
		long duration2 = getDuration(v2);
		long maxDuration = duration1 >= duration2 ? duration1 : duration2 ;
		
		mPreview = (SurfaceView) findViewById(R.id.v);
		holder = mPreview.getHolder();
		mMediaPlayer = new MediaPlayer();
		firstComparisonImpl = new ComparisonImpl(holder, mMediaPlayer, comparison.getFirstVideo().getUrl(), duration1 >= duration2 ? seekBar : null, maxDuration, this);
		holder.addCallback(firstComparisonImpl);
//		holder.addCallback(this);
		mPreview2 = (SurfaceView) findViewById(R.id.v2);
		holder2 = mPreview2.getHolder();
		mMediaPlayer2 = new MediaPlayer();
		secondComparisonImpl = new ComparisonImpl(holder2, mMediaPlayer2, comparison.getSecondVideo().getUrl(), duration1 >= duration2 ? null : seekBar, maxDuration, this);
		holder2.addCallback(secondComparisonImpl);
//		holder2.addCallback(this);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					mMediaPlayer.seekTo(progress);
					mMediaPlayer2.seekTo(progress);
					seekBar.setProgress(progress);
				}
			}
		});
	}
	
	private long getDuration(Video video) {
		long duration = 0;
		for (long l : video.getTagging().getTags().values()) {
			duration += l;
			
		}
		return duration;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comparison_videos, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == this.holder) {
			mMediaPlayer = new MediaPlayer();
			try {
				mMediaPlayer.setDataSource(ComparisonVideosActivity.this,
						Uri.parse(comparison.getFirstVideo().getUrl()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer.setDisplay(holder);
			try {
				mMediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
		} else {
			mMediaPlayer2 = new MediaPlayer();
			try {
				mMediaPlayer2.setDataSource(ComparisonVideosActivity.this,
						Uri.parse(comparison.getSecondVideo().getUrl()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer2.setDisplay(holder);
			try {
				mMediaPlayer2.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer2.setOnBufferingUpdateListener(this);
			mMediaPlayer2.setOnCompletionListener(this);
			mMediaPlayer2.setOnPreparedListener(this);
			mMediaPlayer2.setOnVideoSizeChangedListener(this);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		if (arg0 == mMediaPlayer) {
			mIsVideoSizeKnown = true;
			mVideoWidth = width;
			mVideoHeight = height;
//			if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//				startVideoPlayback();
//			}
		} else {
			mIsVideoSizeKnown2 = true;
			mVideoWidth2 = width;
			mVideoHeight2 = height;
//			if (mIsVideoReadyToBePlayed2 && mIsVideoSizeKnown2) {
//				startVideoPlayback();
//			}
		}
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown && mIsVideoReadyToBePlayed2 && mIsVideoSizeKnown2) {
//			startVideoPlayback();
//		}
		
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared called");
		if (mp == mMediaPlayer) {
			mIsVideoReadyToBePlayed = true;
		} else {
			mIsVideoReadyToBePlayed2 = true;
		}
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown && mIsVideoReadyToBePlayed2 && mIsVideoSizeKnown2) {
//			startVideoPlayback();
//		}
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		
	}
	
	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
//		holder.setFixedSize(mVideoWidth, mVideoHeight/2);
//		holder2.setFixedSize(mVideoWidth2, mVideoHeight2);
		mMediaPlayer.start();
		mMediaPlayer2.start();
		seekBar.setMax(mMediaPlayer.getDuration() > mMediaPlayer2.getDuration() ? mMediaPlayer.getDuration() : mMediaPlayer2.getDuration());
		// seekUpdation();

		final Handler mHandler = new Handler();
		Runnable mRunnable = new Runnable() {

			@Override
			public void run() {
				if (mMediaPlayer != null) {
					// int mCurrentPosition = mMediaPlayer.getCurrentPosition()
					// / 1000;
					seekBar.setProgress(mMediaPlayer.getCurrentPosition());
				}
				mHandler.postDelayed(this, 100);
			}
		};
		mRunnable.run();
	}
	
	public void play(View v) {
		if (firstComparisonImpl.isReady() && secondComparisonImpl.isReady()) {
			startVideoPlayback();
		}
	}

}
