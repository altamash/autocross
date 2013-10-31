package com.veriqual.gofast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.veriqual.gofast.dialog.TagDialog;
import com.veriqual.gofast.model.Tagging;
import com.veriqual.gofast.model.Video;
import com.veriqual.gofast.utilites.ListviewAdapter;
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
	Spinner spinner;
	int fvMargin;
	int svMargin;
	int markerCount;
	
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
		
		addItemsOnSpinner();
		
		addFrameListener();

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
		if(Utilities.isHideInput(cvName, firstVideo, secondVideo)) {
			args.putString("hideinput", "");
		} else {
			if (cvName == Video.FIRSTVIDEO) {
				args.putInt("tagCount", firstVideo.getTagging().getTags().size());
			} else {
				args.putInt("tagCount", secondVideo.getTagging().getTags().size());
			}
		}
		args.putInt("icon", Utilities.getTagDlgIcon(cvName, firstVideo, secondVideo));
		args.putBoolean("ok", Utilities.isTagDlgOneButton(cvName, firstVideo, secondVideo));
		dialog.setArguments(args);
        dialog.show(getFragmentManager(), "TagDialog");
	}
	
	public void compare(View v) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom);
		dialog.setTitle("Tag Comparisons...");
		
		ListView lview = (ListView) dialog.findViewById(R.id.listview);
        ListviewAdapter adapter = new ListviewAdapter(this, Utilities.populateList(firstVideo, secondVideo));
        lview.setAdapter(adapter);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public void addItemsOnSpinner() {
		 
		spinner = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("5");
		list.add("10");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, list);
		
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					fps = 1;
				} else if (arg2 == 1) {
					fps = 5;
				} else {
					fps = 10;
				}  
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	  }
	
	private void addFrameListener() {
		TextView right = (TextView) findViewById(R.id.right);
		right.setOnTouchListener(new OnTouchListener() {
			boolean up;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					up = true;	
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					new AsyncTask<Void, Void, Result>() {
//
//						@Override
//						protected Result doInBackground(Void... params) {
//							if(!up) {
								currentVideo.seekTo(currentVideo.getCurrentPosition() + (1000/25*fps));
//							}							
//							return null;
//						}
//					};
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					up = true;
				}
				
				return false;
			}
		});
		TextView left = (TextView) findViewById(R.id.left);
		left.setOnTouchListener(new OnTouchListener() {
			boolean up;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					while(!up) {
						currentVideo.seekTo(currentVideo.getCurrentPosition() - (1000/25*fps));
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					up = true;
				}
				
				return false;
			}
		});
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		String tag = Utilities.getTag(cvName, firstVideo, secondVideo);
		if(tag == null) {
			tag = ((TagDialog)dialog).getTg();
		}
		if (tag == null || tag.isEmpty()) return;
		int currentPosition = currentVideo.getCurrentPosition();
		if (cvName.equals(firstVideo.getVideoOrder())) {
			if (firstVideo.getTagging().getTags().isEmpty()) {
				firstVideo.setStartOffset(currentPosition);
			}
			firstVideo.getTagging().addTag(tag, (long) (currentPosition - firstVideo.getStartOffset()));
		} else {
			if (secondVideo.getTagging().getTags().isEmpty()) {
				secondVideo.setStartOffset(currentPosition);
			}
			secondVideo.getTagging().addTag(tag, (long) (currentPosition - secondVideo.getStartOffset()));
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
	}

}