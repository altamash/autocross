package com.veriqual.gofast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.veriqual.gofast.dialog.TagDialog;
import com.veriqual.gofast.model.Comparison;
import com.veriqual.gofast.model.ComparisonsList;
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
	ComparisonsList comparisonsList;
	String name;
	Comparison comparison;
	EditText input;
	private boolean firstClicked;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.tagging);
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		    StrictMode.setThreadPolicy(policy);
//		}
//		comparisonsList = Utilities.getComparisonsList(this);
		ComparisonsList.getInstance().setComparisons(Utilities.getComparisonsList(this).getComparisons());
		firstVideo = new Video(Video.FIRSTVIDEO);
//		setupVideoView((VideoView) findViewById(R.id.view),
//				"android.resource://" + getPackageName() + "/" + R.raw.v8_turbo_480x270, 
//				firstVideo);
		secondVideo = new Video(Video.SECONDVIDEO);
		comparison = new Comparison(firstVideo, secondVideo);
		comparisonsList = ComparisonsList.getInstance();
		comparisonsList.getComparisons().add(comparison);
//		setupVideoView((VideoView) findViewById(R.id.view2),
//				"http://vimeo.com/5313987/download?t=1380623488&v=5800982&s=5fd7d894420e9fe94256ed4c4ecb827e", secondVideo);
		
		addItemsOnSpinner();
		
		addFrameListener();

		lapName = (TextView) findViewById(R.id.lapName);

//		Button seek = (Button) findViewById(R.id.seek);
//		seek.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				mFirstVideo.start();
//				mFirstVideo.seekTo(currentPosition);
//				return false;
//			}
//		});
	}
	
	private void setupVideoView(final VideoView videoView, final String url, final Video video) {
		videoView.setVideoURI(Uri.parse(url));
		videoView.setMediaController(new MediaController(this));
		videoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(currentVideo != null) {
					currentVideo.pause();
					currentVideo.setBackgroundColor(Color.parseColor("#66666666"));
				}
				currentVideo = videoView;
				currentVideo.setBackgroundColor(Color.parseColor("#00000000"));
				cvName = video.getVideoOrder();
				lapName.setText("Selected Lap: " + cvName);
				return false;
			}
		});
		video.setUrl(url);
		video.setTagging(new Tagging());
//		comparisonsList.getComparisonsList()
		((Button) TaggingActivity.this.findViewById(R.id.tag)).setEnabled(true);
	}
	
	public void loadFirst(View v) {
		firstClicked = true;
		Intent intent = new Intent(TaggingActivity.this, FolderActivity.class);
		startActivityForResult(intent, 1);
		
//		((Button) findViewById(R.id.viewBtn)).setVisibility(Button.GONE);
//		((VideoView) findViewById(R.id.view)).setVisibility(Button.VISIBLE);
////		firstVideo = new Video(Video.FIRSTVIDEO);
//		setupVideoView((VideoView) findViewById(R.id.view),
//				"android.resource://" + getPackageName() + "/" + R.raw.v8_turbo_480x270, 
//				firstVideo);
	}
	
	public void loadSecond(View v) {
		Intent intent = new Intent(TaggingActivity.this, FolderActivity.class);
		startActivityForResult(intent, 1);
		
//		((Button) findViewById(R.id.view2Btn)).setVisibility(Button.GONE);
//		((VideoView) findViewById(R.id.view2)).setVisibility(Button.VISIBLE);	
////		secondVideo = new Video(Video.SECONDVIDEO);
//		setupVideoView((VideoView) findViewById(R.id.view2),
//				"http://vimeo.com/5313987/download?t=1380623488&v=5800982&s=5fd7d894420e9fe94256ed4c4ecb827e", secondVideo);
	}
	
	public void moveFramesBack(View v) {
		currentVideo.seekTo(currentVideo.getCurrentPosition() - (1000/25*fps));
	}
	
	public void moveFramesForward(View v) {
		currentVideo.seekTo(currentVideo.getCurrentPosition() + (1000/25*fps));
	}
	
	public void dialog(View v) {
		if (cvName == null || firstVideo.getUrl() == null || secondVideo.getUrl() == null) return; 
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
	
	public void view(View v) {
		Intent intent = new Intent(TaggingActivity.this, ComparisonVideosActivity.class);
		intent.putExtra("index", comparisonsList.getComparisons().size() - 1);
		startActivity(intent);
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
					up = false;
					new AsyncTask<Void, Void, Result>() {

						@Override
						protected Result doInBackground(Void... params) {
							while(!up) {
								currentVideo.seekTo(currentVideo.getCurrentPosition() + ((1000/25)*fps));
							}							
							return null;
						}
					}.execute();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					up = true;
				}
				
				return true;
			}
		});
		TextView left = (TextView) findViewById(R.id.left);
		left.setOnTouchListener(new OnTouchListener() {
			boolean up;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					up = false;
					new AsyncTask<Void, Void, Result>() {

						@Override
						protected Result doInBackground(Void... params) {
							while(!up) {
								currentVideo.seekTo(currentVideo.getCurrentPosition() - ((1000/25)*fps));
							}							
							return null;
						}
					}.execute();
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
				firstVideo.setOffset(currentPosition);
			}
			firstVideo.setOffset(firstVideo.getOffset() + Utilities.getLastTagValue(firstVideo.getTagging()));
			firstVideo.getTagging().addTag(tag, (long) (currentPosition - firstVideo.getOffset()));
		} else {
			if (secondVideo.getTagging().getTags().isEmpty()) {
				secondVideo.setStartOffset(currentPosition);
				secondVideo.setOffset(currentPosition);
			}
			secondVideo.setOffset(secondVideo.getOffset() + Utilities.getLastTagValue(secondVideo.getTagging()));
			secondVideo.getTagging().addTag(tag, (long) (currentPosition - secondVideo.getOffset()));

			if(tag.equals(Tagging.FINISHTAG)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				input = new EditText(this);
				input.setText(comparison.getName());
				input.setSelection(input.getText().toString().length());
				builder.setMessage("Save comparison?")
					.setView(input)
					.setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
			}
			
			((Button) TaggingActivity.this.findViewById(R.id.save)).setEnabled(true);
			((Button) TaggingActivity.this.findViewById(R.id.name)).setEnabled(true);
			((Button) TaggingActivity.this.findViewById(R.id.compare)).setEnabled(true);
			((Button) TaggingActivity.this.findViewById(R.id.video)).setEnabled(true);
		}
		
		
	}
	
	public void saveComparison(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		input = new EditText(this);
		input.setText(comparison.getName());
		input.setSelection(input.getText().toString().length());
		builder.setMessage("Save comparison")
			.setView(input)	
			.setPositiveButton("Save", dialogClickListener)
		    .setNegativeButton("Cancel", dialogClickListener).show();
	}
	
	public void nameComparison(View v) {
//		comparison.setName(cvName);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		input = new EditText(this);
		input.setText(comparison.getName());
		input.setSelection(input.getText().toString().length());
		builder.setMessage("Name comparison")
			.setView(input)	
			.setPositiveButton("Ok", nameClickListener)
		    .setNegativeButton("Cancel", nameClickListener).show();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
	}
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	        	comparison.setName(input.getText().toString());
	        	try {
					Utilities.saveComparison(TaggingActivity.this, ComparisonsList.getInstance());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
//	        	((Button) TaggingActivity.this.findViewById(R.id.save)).setVisibility(Button.VISIBLE);
	            break;
	        }
	    }
	};
	
	DialogInterface.OnClickListener nameClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	        	comparison.setName(input.getText().toString());
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	        	((Button) TaggingActivity.this.findViewById(R.id.save)).setVisibility(Button.VISIBLE);
	            break;
	        }
	    }
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				
				if (firstClicked) {
					((Button) findViewById(R.id.viewBtn)).setVisibility(Button.GONE);
					((VideoView) findViewById(R.id.view)).setVisibility(Button.VISIBLE);
					setupVideoView((VideoView) findViewById(R.id.view),
							path, 
							firstVideo);
					firstClicked = false;
				} else {
					((Button) findViewById(R.id.view2Btn)).setVisibility(Button.GONE);
					((VideoView) findViewById(R.id.view2)).setVisibility(Button.VISIBLE);	
					setupVideoView((VideoView) findViewById(R.id.view2),
							path, secondVideo);
				}
				
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

}