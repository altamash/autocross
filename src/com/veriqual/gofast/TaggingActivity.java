package com.veriqual.gofast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class TaggingActivity extends Activity {
//	http://vimeo.com/5937483/download?t=1380622550&v=5043551&s=bff330857f646c387da8103aed5ee65b
//	http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8
//	http://vimeo.com/5313987/download?t=1380623488&v=5800982&s=5fd7d894420e9fe94256ed4c4ecb827e

	private io.vov.vitamio.widget.VideoView mFirstVideo;
	private io.vov.vitamio.widget.VideoView mSecondVideo;
	private io.vov.vitamio.widget.VideoView currentVideo;
	int duration;
	long currentPosition;
	int secondCurrentPosition;
//	final int CALIBRATE = -100;
	MediaPlayer mp;
	int fps;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.tagging);
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		    StrictMode.setThreadPolicy(policy);
//		}
		setupVideoView((io.vov.vitamio.widget.VideoView) findViewById(R.id.view),
				"android.resource://" + getPackageName() + "/" + R.raw.v8_turbo_480x270);
		setupVideoView((io.vov.vitamio.widget.VideoView) findViewById(R.id.view2),
				"http://vimeo.com/5313987/download?t=1380623488&v=5800982&s=5fd7d894420e9fe94256ed4c4ecb827e");

		Button button = (Button) findViewById(R.id.pause);
		button.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				currentPosition = mFirstVideo.getCurrentPosition();
				mFirstVideo.pause();
				return false;
			}
		});

		Button seek = (Button) findViewById(R.id.seek);
		seek.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mFirstVideo.start();
				mFirstVideo.seekTo(currentPosition);
				return false;
			}
		});
		
//		mFirstVideo.setOnPreparedListener(new OnPreparedListener() {
//		    @Override
//		    public void onPrepared(MediaPlayer mp) {
//
//		        mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
//		            @Override
//		            public void onSeekComplete(MediaPlayer mp) {
//		            	mFirstVideo.start();
//		            }
//		        });
//
//		    }
//		});

		
//		mFirstVideo.seekTo(0);

//		String PATH =Environment.getExternalStorageDirectory().toString()+"/load";
//		Log.v("LOG_TAG", "PATH: " + PATH);
//
//		File file = new File(PATH);
//		file.mkdirs();
//		File outputFile = new File(file,"file1");
//		downloadFile("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8", outputFile);
		
//		DownloadFromUrl("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8", "file1");
	}
	
	private void setupVideoView(final io.vov.vitamio.widget.VideoView videoView, String url) {
		videoView.setVideoURI(Uri.parse(url));
		videoView.setMediaController(new io.vov.vitamio.widget.MediaController(this));
		videoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(currentVideo != null) currentVideo.pause();
				currentVideo = videoView;
				return false;
			}
		});
	}
	
	public void setScroll10Fps(View v) {
		fps = 5;		
	}
	
	public void setScroll20Fps(View v) {
		fps = 10;		
	}
	
	public void setScroll30Fps(View v) {
		fps = 15;		
	}
	
	public void setScroll40Fps(View v) {
		fps = 20;		
	}
	
	public void moveFramesBack(View v) {
		currentVideo.seekTo(currentVideo.getCurrentPosition() - (1000/25*fps));
	}
	
	public void moveFramesForward(View v) {
		currentVideo.seekTo(currentVideo.getCurrentPosition() + (1000/25*fps));
	}
	
	private static void downloadFile(String url, File outputFile) {
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			int contentLength = conn.getContentLength();

			DataInputStream stream = new DataInputStream(u.openStream());

			byte[] buffer = new byte[contentLength];
			stream.readFully(buffer);
			stream.close();

			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					outputFile));
			fos.write(buffer);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			return; // swallow a 404
		} catch (IOException e) {
			return; // swallow a 404
		}
	}
	
	public void DownloadFromUrl(String DownloadUrl, String fileName) {

		try {
			File root = android.os.Environment.getExternalStorageDirectory();

			File dir = new File(root.getAbsolutePath() + "/xmls");
			if (dir.exists() == false) {
				dir.mkdirs();
			}

			URL url = new URL(DownloadUrl); // you can write here any link
			File file = new File(dir, fileName);

			long startTime = System.currentTimeMillis();
			Log.d("DownloadManager", "download begining");
			Log.d("DownloadManager", "download url:" + url);
			Log.d("DownloadManager", "downloaded file name:" + fileName);

			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();
			Log.d("DownloadManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("DownloadManager", "Error: " + e);
		}

	}

}