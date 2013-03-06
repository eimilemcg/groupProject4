/*
 * Tim O'Shea
 * 
 * © 2013 All rights reserved
 */

package edu.pitt.cs1635.tdo7.prog2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

// MAIN
public class DoodleActivity extends Activity {

	private Button confirmButton;
	private EditText drawingResult;
	private InnerDrawingView innerView;
	private String characterResult;

	// when the activity is launched
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doodle);

		// where the drawing happens
		innerView = (InnerDrawingView) findViewById(R.id.innerDrawingView1);
		innerView.setOnTouchListener(handleTouch);

		// tells the user what they drew
		drawingResult = (EditText) findViewById(R.id.drawResult);
		drawingResult.setEnabled(false);

		// button that talks to the cloud
		confirmButton = (Button) findViewById(R.id.confirmButton);
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingResult.setTextColor(Color.BLACK);
				drawingResult.setText("");
				innerView.endOfChar();
				// connect to cloud
				postData();
			} // end of listener method
		}); // end of anon listener class
	} // end of onCreate

	// show the character drawn to the cloud
	public void postData() {
		// Create a new HttpClient and Post Header
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://cwritepad.appspot.com/reco/usen");

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("key",
					"11773edfd643f813c18d82f56a8104ed"));
			nameValuePairs.add(new BasicNameValuePair("q", innerView
					.getCoordArray()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// execute HTTP post request
			characterResult = httpClient.execute(httpPost,
					new BasicResponseHandler());
			drawingResult.setText(characterResult);
			innerView.clear();
			innerView.resetResultString();
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	} // end of postData

	// when the options menu is selected
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	} // end of onCreateOptionsMenu

	// when a button in the options menu is clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// called when the options menu is clicked
		switch (item.getItemId()) {
		case R.id.drawingPrefs:
			startActivityForResult(new Intent(this, PrefsActivity.class), 1);
			break;
		case R.id.clearButton:
			innerView.clear();
			break;
		} // end of switch
		return true;
	} // end of onOptionsItemSelected

	// new preferences to use
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1:
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);

			int i = Integer.parseInt(sharedPrefs.getString("newWidthOption",
					"5"));
			if (i > 50) {
				i = 50;
			} else if (i < 1) {
				i = 1;
			} // don't want the brush too big

			// get the color of the brush
			int j = Integer.parseInt(sharedPrefs.getString("colorOption",
					"Color.BLACK"));
			switch (j) {
			case 1:
				j = Color.WHITE;
				break;
			case 2:
				j = Color.RED;
				break;
			case 3:
				j = Color.MAGENTA;
				break;
			case 4:
				j = Color.YELLOW;
				break;
			case 5:
				j = Color.GREEN;
				break;
			case 6:
				j = Color.BLUE;
				break;
			case 7:
				j = Color.CYAN;
				break;
			case 8:
				j = Color.BLACK;
				break;
			case 9:
				j = Color.GRAY;
				break;
			} // end of j switch

			// get the color of the background
			int k = Integer.parseInt(sharedPrefs.getString(
					"backgroundOptionsKey", "Color.YELLOW"));
			switch (k) {
			case 1:
				k = Color.WHITE;
				break;
			case 2:
				k = Color.RED;
				break;
			case 3:
				k = Color.MAGENTA;
				break;
			case 4:
				k = Color.YELLOW;
				break;
			case 5:
				k = Color.GREEN;
				break;
			case 6:
				k = Color.BLUE;
				break;
			case 7:
				k = Color.CYAN;
				break;
			case 8:
				k = Color.BLACK;
				break;
			case 9:
				k = Color.GRAY;
				break;
			} // end of j switch

			innerView.updatePrefs(i, j, k);
			break;
		} // end of switch
	} // end of result

	// handle the touching of the inner view
	private OnTouchListener handleTouch = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				innerView.touch_start(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				innerView.touch_move(x, y);
				break;
			case MotionEvent.ACTION_UP:
				innerView.touch_up();
				break;
			}
			return true;
		} // end of ontouch
	}; // end of listener class
} // end of activity
