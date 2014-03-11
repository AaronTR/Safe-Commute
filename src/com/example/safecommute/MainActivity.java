//Kru
//Aaron
//Mark
//Jakob

package com.example.safecommute;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
	
	public void scalingImages(){
		
		/* DYNAMICALLY SIZE IMAGES, BASED ON SCREEN SIZE */
		
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
		
		
		//Adapter container = new ImageAdapter();
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
		ImageView settingsimage = new ImageView(this);
		settingsimage.setImageResource(R.drawable.settings);
		settingsimage.setLayoutParams(imageParams);
		appsimagerow.addView(settingsimage);
		settingsimage.setTag(position);
		position++;
		
		ImageView musicimage = new ImageView(this);
		musicimage.setImageResource(R.drawable.music);
		musicimage.setLayoutParams(imageParams);
		appsimagerow.addView(musicimage);
		musicimage.setTag(position);
		position++;
		
		ImageView mapsimage = new ImageView(this);
		mapsimage.setImageResource(R.drawable.map);
		mapsimage.setLayoutParams(imageParams);
		appsimagerow.addView(mapsimage);
		mapsimage.setTag(position);
		position++;
		
		// Adding images to Passenger Unlock row		
		ImageView cameraimage = new ImageView(this);
		cameraimage.setImageResource(R.drawable.camera);
		cameraimage.setLayoutParams(imageParams);
		passengerimagerow.addView(cameraimage);
		cameraimage.setTag(position);
		position++;
		
		ImageView bluetoothimage = new ImageView(this);
		bluetoothimage.setImageResource(R.drawable.bluetooth);
		bluetoothimage.setLayoutParams(imageParams);
		passengerimagerow.addView(bluetoothimage);
		bluetoothimage.setTag(position);
		position++;

		// Adding image to Emergency row		
		ImageView emergencyimage = new ImageView(this);
		emergencyimage.setImageResource(R.drawable.emergency);
		emergencyimage.setLayoutParams(imageParams);
		emergencyimagerow.addView(emergencyimage);
		emergencyimage.setTag(position);
		position++;
		
	}
	
	/*public View getView(int position, View view, ViewGroup parent) {
	    ImageView imageView;
	    imageView = (ImageView) view;
	    imageView.setTag(position);
	    return imageView;
	}*/

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

	        ImageView imageView;
	        //Variable i, here, is from a for loop.
	        imageView = (ImageView)v.findViewWithTag(position);
	        //I get a NullPointerException at the next line, "Log.d"
	        Log.d("View 1", imageView.toString());
	        //If I get rid of the "Log.d" line above, 
	        //the NullPointerException occurs on the next line
	        imageView.setBackgroundColor(Color.BLUE);
	        imageView = (ImageView)v.findViewWithTag(position);

	        Log.d("View 2", imageView.toString());
	        imageView.setBackgroundColor(Color.BLUE);

	    };
	};

	
	public class ImageAdapter extends BaseAdapter {
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.getCount();
		}
	
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return this.getItem(arg0);
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return this.getItemId(position);
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getView(position, convertView, parent);
		}
	
	};

}
