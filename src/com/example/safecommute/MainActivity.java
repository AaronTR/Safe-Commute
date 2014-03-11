package com.example.safecommute;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//setContentView(R.layout.table_layout);
		setContentView(R.layout.activity_main);	
		scalingImages(); // adds tags to all images
		scalingTexts();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void scalingTexts(){
		
		Display display = getWindowManager().getDefaultDisplay(); 
		int screenWidth = display.getWidth();  // screen width
		int screenHeight = display.getHeight();  // screen height
		int textHeight = screenHeight/20; // new text height
		
		
		
		// Assigning table rows
		TableRow appstextrow = (TableRow)findViewById(R.id.appstextrow); 
		TableRow passengertextrow = (TableRow)findViewById(R.id.passengertextrow); 
		TableRow emergencytextrow = (TableRow)findViewById(R.id.emergencytextrow); 
		
		
		// Layout parameters for text views
		TableRow.LayoutParams textParams = new TableRow.LayoutParams(
				screenWidth, textHeight); // 
		textParams.span = 3;
		
		// Format and add "Passenger Unlock" text
		TextView passengertext = new TextView(this);
		passengertext.setText(R.string.passenger_unlock);
		passengertext.setLayoutParams(textParams);
		passengertext.setGravity(Gravity.CENTER);
		passengertext.setTextColor(Color.WHITE);
		passengertext.setTextSize(16 * getResources().getDisplayMetrics().density);		
		passengertextrow.addView(passengertext);
		
		// Format and add "Available Applications" text
		TextView appstext = new TextView(this);
		appstext.setText(R.string.available_apps);
		appstext.setLayoutParams(textParams);
		appstext.setGravity(Gravity.CENTER);
		appstext.setTextColor(Color.WHITE);
		appstext.setTextSize(16 * getResources().getDisplayMetrics().density);		
		appstextrow.addView(appstext);
		
		// Format and add "Emergency" text
		TextView emergencytext = new TextView(this);
		emergencytext.setText(R.string.emergency_contacts);
		emergencytext.setLayoutParams(textParams);
		emergencytext.setGravity(Gravity.CENTER);
		emergencytext.setTextColor(Color.WHITE);
		emergencytext.setTextSize(16 * getResources().getDisplayMetrics().density);		
		emergencytextrow.addView(emergencytext);		
	}
	
	
	public void scalingImages() {
		
		/* DYNAMICALLY SIZE IMAGES, BASED ON SCREEN SIZE */
		
		// Obtain screen size
		Display display = getWindowManager().getDefaultDisplay(); 
		int screenWidth = display.getWidth();  // screen width
		int screenHeight = display.getHeight();  // screen height			
	
		// Assigning table rows
		final TableRow titlerow = (TableRow)findViewById(R.id.titlerow);
		final TableRow appsimagerow = (TableRow)findViewById(R.id.appsimagerow);
		final TableRow passengerimagerow = (TableRow)findViewById(R.id.passengerimagerow); 
		final TableRow emergencyimagerow = (TableRow)findViewById(R.id.emergencyimagerow); 
		appsimagerow.setClickable(true);
		passengerimagerow.setClickable(true);
		emergencyimagerow.setClickable(true);
		
		// New Dimensions
		int imageHeight = screenHeight/5;
		int textHeight = screenHeight/20;
		int titleHeight = screenHeight/5;
		int titleWidth = screenWidth/4;
		
		// tag id Position
		int position = 0;
		
		// Setting parameters, from above dimensions
		TableRow.LayoutParams titleParams = new TableRow.LayoutParams(
			    titleWidth, titleHeight);
		titleParams.span = 3;
		TableRow.LayoutParams imageParams = new TableRow.LayoutParams(
			    imageHeight, imageHeight);

		// Adding logo
		ImageView title = new ImageView(this);
		title.setImageResource(R.drawable.logo);		
		title.setLayoutParams(titleParams);
		title.setScaleType(ImageView.ScaleType.FIT_CENTER);		
		titlerow.addView(title);
		
		// Adding images to Available Apps row		
		final ImageView settingsimage = new ImageView(this);
		settingsimage.setImageResource(R.drawable.settings);
		settingsimage.setLayoutParams(imageParams);
		settingsimage.setTag(position);
		appsimagerow.addView(settingsimage);
		settingsimage.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, ""+settingsimage.getTag(),
                        Toast.LENGTH_SHORT).show();
                showFragment(settingsimage, (Integer) settingsimage.getTag());
			}
		});	
		position++;
		
		final ImageView musicimage = new ImageView(this);
		musicimage.setImageResource(R.drawable.music);
		musicimage.setLayoutParams(imageParams);
		appsimagerow.addView(musicimage);
		musicimage.setTag(position);
		musicimage.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, ""+ musicimage.getTag(),
                        Toast.LENGTH_SHORT).show();
                showFragment(musicimage, (Integer) musicimage.getTag());
            }
		});	
		position++;
		
		final ImageView mapsimage = new ImageView(this);
		mapsimage.setImageResource(R.drawable.map);
		mapsimage.setLayoutParams(imageParams);
		mapsimage.setTag(position);
		appsimagerow.addView(mapsimage);
		mapsimage.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, ""+mapsimage.getTag(),
                        Toast.LENGTH_SHORT).show();
                showFragment(mapsimage, (Integer) mapsimage.getTag());
            }
		});	
		position++;
		
		// Adding images to Passenger Unlock row		
		final ImageView cameraimage = new ImageView(this);
		cameraimage.setImageResource(R.drawable.camera);
		cameraimage.setLayoutParams(imageParams);
		passengerimagerow.addView(cameraimage);
		cameraimage.setTag(position);
		cameraimage.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, ""+cameraimage.getTag(),
                        Toast.LENGTH_SHORT).show();
                showFragment(mapsimage, (Integer) mapsimage.getTag());
            }
		});	
		position++;
		
		final ImageView bluetoothimage = new ImageView(this);
		bluetoothimage.setImageResource(R.drawable.bluetooth);
		bluetoothimage.setLayoutParams(imageParams);
		bluetoothimage.setTag(position);
		passengerimagerow.addView(bluetoothimage);
		bluetoothimage.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, ""+bluetoothimage.getTag(),
                        Toast.LENGTH_SHORT).show();
                showFragment(bluetoothimage, (Integer) bluetoothimage.getTag());
            }
		});
		position++;

		// Adding image to Emergency row		
		final ImageView emergencyimage = new ImageView(this);
		emergencyimage.setImageResource(R.drawable.emergency);
		emergencyimage.setLayoutParams(imageParams);
		emergencyimage.setTag(position);
		emergencyimagerow.addView(emergencyimage);
		emergencyimage.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, ""+emergencyimage.getTag(),
                        Toast.LENGTH_SHORT).show();
                
                showFragment(emergencyimage, (Integer) emergencyimage.getTag());
            }
		});	
				
	}
	
    void showFragment(ImageView pic, int positionIndex) {
    		// We need to launch a new activity to display
 			// the dialog fragment with selected text.

 			// Create an intent for starting the DetailsActivity
 			Intent intent = new Intent(this, DetailsActivity.class);

 			// explicitly set the activity context and class
 			// associated with the intent (context, class)
 			//intent.setClass(MainActivity.CONTEXT_IGNORE_SECURITY, MainActivity.DetailsActivity.class);
 			
 			// pass the current position
 			intent.putExtra("index", positionIndex);
 			intent.putExtra("title", pic.toString());

 			startActivity(intent);
    }

	
	public static class DetailsActivity extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (savedInstanceState == null) {
				// During initial setup, plug in the details fragment.

				// create fragment
				DisplayClicked dispClick = new DisplayClicked();

				// get and set the position input by user (i.e., "index")
				// which is the construction arguments for this fragment
				dispClick.setArguments(getIntent().getExtras());

				//
				getFragmentManager().beginTransaction()
				.add(android.R.id.content, dispClick).commit();
			}
		}
}

}
