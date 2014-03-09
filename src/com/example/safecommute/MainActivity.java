//Kru
//Aaron
//Mark
//Jakob

package com.example.safecommute;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// going to be the fragment container for the main view
		setContentView(R.layout.activity_main); // UI Lock-Screen Layout
		scalingImages(); // Dynamically size images, according to screen size	
		scalingTexts();	
		
		//setContentView(R.layout.alpha_clickable_title); // Jakob's Test	
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.activity_main, menu);
	//		return true;
	//	}
	
	public static class DetailsActivity extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (savedInstanceState == null) {
				// During initial setup, plug in the details fragment.

				// create fragment
				AlphaDisplayClicked alpha = new AlphaDisplayClicked();

				// get and set the position input by user (i.e., "index")
				// which is the construction arguments for this fragment
				alpha.setArguments(getIntent().getExtras());

				//
				getFragmentManager().beginTransaction()
						.add(android.R.id.content, alpha).commit();
			}
		}
	}

	public void scalingImages(){ /* DYNAMICALLY SIZE IMAGES & TEXT, BASED ON SCREEN SIZE */
		
		// Obtain screen size
		Display display = getWindowManager().getDefaultDisplay(); 
		int screenWidth = display.getWidth();  // screen width
		int screenHeight = display.getHeight();  // screen height			
	
		// Assigning table rows
		TableRow titlerow = (TableRow)findViewById(R.id.titlerow);
		TableRow appsimagerow = (TableRow)findViewById(R.id.appsimagerow);
		TableRow passengerimagerow = (TableRow)findViewById(R.id.passengerimagerow); 
		TableRow emergencyimagerow = (TableRow)findViewById(R.id.emergencyimagerow); 
		
		// New Dimensions
		int imageHeight = screenHeight/5;
		int textHeight = screenHeight/20;
		int titleHeight = screenHeight/5;
		int titleWidth = screenWidth/4;
		
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
		ImageView settingsimage = new ImageView(this);
		settingsimage.setImageResource(R.drawable.settings);
		settingsimage.setLayoutParams(imageParams);
		appsimagerow.addView(settingsimage);
		
		ImageView musicimage = new ImageView(this);
		musicimage.setImageResource(R.drawable.music);
		musicimage.setLayoutParams(imageParams);
		appsimagerow.addView(musicimage);
		
		ImageView mapsimage = new ImageView(this);
		mapsimage.setImageResource(R.drawable.map);
		mapsimage.setLayoutParams(imageParams);
		appsimagerow.addView(mapsimage);
		
		
		// Adding images to Passenger Unlock row		
		ImageView cameraimage = new ImageView(this);
		cameraimage.setImageResource(R.drawable.camera);
		cameraimage.setLayoutParams(imageParams);
		passengerimagerow.addView(cameraimage);
		
		ImageView bluetoothimage = new ImageView(this);
		bluetoothimage.setImageResource(R.drawable.bluetooth);
		bluetoothimage.setLayoutParams(imageParams);
		passengerimagerow.addView(bluetoothimage);

		// Adding image to Emergency row		
		ImageView emergencyimage = new ImageView(this);
		emergencyimage.setImageResource(R.drawable.emergency);
		emergencyimage.setLayoutParams(imageParams);
		emergencyimagerow.addView(emergencyimage);				
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
}
