package com.veriqual.gofast;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
	private final String TAG = "SPLASH";
	TimerTask switchActivity;
	Timer t = new Timer();
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		switchActivity = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						moveToHomeScreen();
					}

				});

			}
		};

		t.schedule(switchActivity, 3000);
	}
	
	private void moveToHomeScreen() {
//		if (Utilities.getUserKey(Constants.USERID, Splash.this) == null) {
			Intent baseIntent = new Intent(Splash.this, MainActivity.class);
			startActivity(baseIntent);
			this.finish();
//		} else {
//			Intent baseIntent = new Intent(Splash.this, TicketList.class);
//			
//			ArrayList<String> extra_text = new ArrayList<String>();
//			extra_text.add(Utilities.getUserKey(Constants.USERID, Splash.this));
//			baseIntent.putStringArrayListExtra(Constants.USERID, extra_text);
//			baseIntent.putExtra(Constants.USERID,
//					Utilities.getUserKey(Constants.USERID, Splash.this));
//			Log.d(TAG, Utilities.getUserKey(Constants.USERID, Splash.this));
//			startActivity(baseIntent);
//			this.finish();

//		}
	}

}
