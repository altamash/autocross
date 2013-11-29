package com.veriqual.gofast.utilites;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.SeekBar;

public class ComparisonImpl implements OnBufferingUpdateListener,

OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener,
		SurfaceHolder.Callback {

	Context context;
	private MediaPlayer mMediaPlayer;
	private SurfaceHolder holder;
	private SeekBar seekBar;

	private static final String TAG = "";
	private int mVideoWidth;
	private int mVideoHeight;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private String url;
	private long duration;

	public ComparisonImpl(SurfaceHolder h, MediaPlayer mp, String u, SeekBar s, long d, Context c) {
		holder = h;
		mMediaPlayer = mp;
		url = u;
		seekBar = s;
		duration = d;
		context = c;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
//		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, Uri.parse(url));
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
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//			startVideoPlayback();
//		}

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
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

//	private void startVideoPlayback() {
//		Log.v(TAG, "startVideoPlayback");
//		holder.setFixedSize(mVideoWidth, mVideoHeight);
//		mMediaPlayer.start();
//		if (seekBar == null) return; 
////		seekBar.setMax((int) duration);
//		seekBar.setMax(mMediaPlayer.getDuration());
//		// seekUpdation();
//
//		final Handler mHandler = new Handler();
//		Runnable mRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				if (mMediaPlayer != null) {
//					// int mCurrentPosition = mMediaPlayer.getCurrentPosition()
//					// / 1000;
//					seekBar.setProgress(mMediaPlayer.getCurrentPosition());
//				}
//				mHandler.postDelayed(this, 100);
//			}
//		};
//		mRunnable.run();
//	}
	
	public boolean isReady() {
		return mIsVideoReadyToBePlayed && mIsVideoSizeKnown;
	}

}
