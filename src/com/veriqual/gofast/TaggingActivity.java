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
import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.veriqual.gofast.dialog.TagDialog;
import com.veriqual.gofast.model.Tagging;
import com.veriqual.gofast.model.Video;
import com.veriqual.gofast.utilites.Utilities;

public class TaggingActivity extends Activity implements TagDialog.TagDialogListener {
//	http://vimeo.com/5937483/download?t=1380622550&v=5043551&s=bff330857f646c387da8103aed5ee65b
//	http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8
//	http://vimeo.com/5313987/download?t=1380623488&v=5800982&s=5fd7d894420e9fe94256ed4c4ecb827e

	private VideoView mFirstVideo;
	private VideoView currentVideo;
	private String cvName;
	int duration;
	int currentPosition;
//	final int CALIBRATE = -100;
	MediaPlayer mp;
	int fps;
	Video firstVideo; 
	Video secondVideo;
	TagDialog dialog;
	TextView lapName;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.tagging);
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		    StrictMode.setThreadPolicy(policy);
//		}
		firstVideo = new Video(Video.FIRSTVIDEO);
		setupVideoView((VideoView) findViewById(R.id.view),
				"android.resource://" + getPackageName() + "/" + R.raw.v8_turbo_480x270, firstVideo);
		secondVideo = new Video(Video.SECONDVIDEO);
		setupVideoView((VideoView) findViewById(R.id.view2),
				"http://vimeo.com/5313987/download?t=1380623488&v=5800982&s=5fd7d894420e9fe94256ed4c4ecb827e", secondVideo);

		Button button = (Button) findViewById(R.id.pause);
		button.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				currentPosition = mFirstVideo.getCurrentPosition();
				mFirstVideo.pause();
				return false;
			}
		});
		
		lapName = (TextView) findViewById(R.id.lapName);

		Button seek = (Button) findViewById(R.id.seek);
		seek.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mFirstVideo.start();
				mFirstVideo.seekTo(currentPosition);
				return false;
			}
		});
	}
	
	private void setupVideoView(final VideoView videoView, String url, final Video video) {
		videoView.setVideoURI(Uri.parse(url));
		videoView.setMediaController(new MediaController(this));
		videoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(currentVideo != null) currentVideo.pause();
				currentVideo = videoView;
				cvName = video.getVideoOrder();
				lapName.setText("Selected Lap: " + cvName);
				return false;
			}
		});
		video.setUrl(url);
		video.setTagging(new Tagging());
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
	
	public void dialog(View v) {
		if (cvName == null) return; 
		dialog = new TagDialog();
		Bundle args = new Bundle();
		args.putString("msg", Utilities.generateTagMsg(cvName, firstVideo, secondVideo));
		if(Utilities.isHideInput(cvName, firstVideo, secondVideo)) args.putString("hideinput", "");
		dialog.setArguments(args);
        dialog.show(getFragmentManager(), "TagDialog");
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		String tag = Utilities.getTag(cvName, firstVideo, secondVideo);
		if(tag == null) {
			tag = ((TagDialog)dialog).getTg();
		}
		if (tag == null) return;
		if (cvName.equals(firstVideo.getVideoOrder())) {
			firstVideo.getTagging().addTag(tag, currentVideo.getCurrentPosition());
		} else {
			secondVideo.getTagging().addTag(tag, currentVideo.getCurrentPosition());
		}
//		System.out.println(tag);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

}