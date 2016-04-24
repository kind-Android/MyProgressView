package com.example.viewcursor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	private ProgressView mProgressView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressView = (ProgressView) findViewById(R.id.progress);
		mProgressView.setMaxProgress(60*1000);
//		mProgressView.startRecode();
		findViewById(R.id.start).setOnClickListener(this);
		findViewById(R.id.pause).setOnClickListener(this);
		findViewById(R.id.stop).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			mProgressView.startRecode();
			break;
		case R.id.stop:
			mProgressView.stopRecode();
			break;
		case R.id.pause:
			mProgressView.pauseRecode();
		default:
			break;
		}
		
	}
}
