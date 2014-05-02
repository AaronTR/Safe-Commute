package safecommute.main;
import safecommute.bluetooth.BluetoothActivity;
import safecommute.imagerecognition.CameraActivity;
import safecommute.movement.GPS;
//import safecommute.movement.GPSService;
import safecommute.music.MainMusic;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreen extends Activity {
	
	private Intent LocIntent = null;
	//public static GPSService loc = null;
	public static final int CAMERA_REQUEST_CODE = 24;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.lockscreen_main);	
		scalingImages(); // adds tags to all images
		scalingTexts();	

	}	
	
	public void onBackPressed() { // disable back button 
	    // Do Here what ever you want do on back press;
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
		//passengertext.setText(R.string.passenger_unlock);
		passengertext.setLayoutParams(textParams);
		passengertext.setGravity(Gravity.CENTER);
		passengertext.setTextColor(Color.WHITE);
		passengertext.setTypeface(Typeface.SERIF);
		passengertext.setTextSize(16 * getResources().getDisplayMetrics().density);	
		passengertextrow.addView(passengertext);
		
		// Format and add "Available Applications" text
		TextView appstext = new TextView(this);
		//appstext.setText(R.string.available_apps);
		appstext.setLayoutParams(textParams);
		appstext.setGravity(Gravity.CENTER);
		appstext.setTextColor(Color.WHITE);
		appstext.setTypeface(Typeface.SERIF);
		appstext.setTextSize(16 * getResources().getDisplayMetrics().density);		
		appstextrow.addView(appstext);
		
		// Format and add "Emergency" text
		TextView emergencytext = new TextView(this);
		//emergencytext.setText(R.string.emergency_contacts);
		emergencytext.setLayoutParams(textParams);
		emergencytext.setGravity(Gravity.CENTER);
		emergencytext.setTextColor(Color.WHITE);
		emergencytext.setTypeface(Typeface.SERIF);
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
		settingsimage.setOnClickListener(new OnClickListener() { // Onclick for settings image
			@Override
            public void onClick(View arg0) {
                showMovement(settingsimage, (Integer) settingsimage.getTag());
			}
		});	
		position++;
		
		final ImageView musicimage = new ImageView(this);
		musicimage.setImageResource(R.drawable.music);
		musicimage.setLayoutParams(imageParams);
		appsimagerow.addView(musicimage);
		musicimage.setTag(position);
		musicimage.setOnClickListener(new OnClickListener() { // Onclick for music image
			@Override
            public void onClick(View arg0) {
                showMusic(musicimage, (Integer) musicimage.getTag());
            }
		});	
		position++;
		
		final ImageView mapsimage = new ImageView(this);
		mapsimage.setImageResource(R.drawable.map);
		mapsimage.setLayoutParams(imageParams);
		mapsimage.setTag(position);
		appsimagerow.addView(mapsimage);
		mapsimage.setOnClickListener(new OnClickListener() { // Onclick for maps image
			@Override
            public void onClick(View arg0) {
                showMap(mapsimage, (Integer) mapsimage.getTag());
            }
		});	
		position++;
		
		// Adding images to Passenger Unlock row		
		final ImageView cameraimage = new ImageView(this);
		cameraimage.setImageResource(R.drawable.camera);
		cameraimage.setLayoutParams(imageParams);
		passengerimagerow.addView(cameraimage);
		cameraimage.setTag(position);
		cameraimage.setOnClickListener(new OnClickListener() { // Onclick for camera image
			@Override
            public void onClick(View arg0) {
                showCameraView(cameraimage, (Integer)cameraimage.getTag());
            }
		});	
		position++;
		
		final ImageView bluetoothimage = new ImageView(this);
		bluetoothimage.setImageResource(R.drawable.bluetooth);
		bluetoothimage.setLayoutParams(imageParams);
		bluetoothimage.setTag(position);
		passengerimagerow.addView(bluetoothimage);
		bluetoothimage.setOnClickListener(new OnClickListener() { // Onclick for bluetooth image
			@Override
            public void onClick(View arg0) {
                showBluetooth(bluetoothimage, (Integer) bluetoothimage.getTag());
            }
		});
		position++;

		// Adding image to Emergency row		
		final ImageView emergencyimage = new ImageView(this);
		emergencyimage.setImageResource(R.drawable.emergency);
		emergencyimage.setLayoutParams(imageParams);
		emergencyimage.setTag(position);
		emergencyimagerow.addView(emergencyimage);
		emergencyimage.setOnClickListener(new OnClickListener() { // Onclick for emergency image
			@Override
            public void onClick(View arg0) {                
                showFragment(emergencyimage, (Integer) emergencyimage.getTag());
            }
		});	
				
	}
	
	void showFragment(ImageView pic, int positionIndex) { // Generic
		// We need to launch a new activity to display
			// the dialog fragment with selected text.

			// Create an intent for starting the DetailsActivity
			Intent intent = new Intent(this, DetailsActivity.class);

			// explicitly set the activity context and class
			// associated with the intent (context, class)
			//intent.setClass(LockScreen.CONTEXT_IGNORE_SECURITY, LockScreen.DetailsActivity.class);
			
			// pass the current position
			intent.putExtra("index", positionIndex);
			intent.putExtra("title", pic.toString());

			startActivity(intent);
	}
	
	void showMusic (ImageView pic, int positionIndex) { // Open Music Player
		Intent intent = new Intent(this, MainMusic.class);
		intent.putExtra("index", positionIndex);
		intent.putExtra("title", pic.toString());
		startActivity(intent);	
	}
	
	void showMap(ImageView pic, int positionIndex) { // Open Google Maps
		Intent intent = new Intent(this, Maps.class);
		intent.putExtra("index", positionIndex);
		intent.putExtra("title", pic.toString());
		startActivity(intent);		
	}
	
	void showMovement(ImageView pic, int positionIndex) { // Open Bluetooth
		Intent intent = new Intent(this, GPS.class);
		intent.putExtra("index", positionIndex);
		intent.putExtra("title", pic.toString());
		startActivity(intent);		
	}
	
	void showBluetooth(ImageView pic, int positionIndex) { // Open Bluetooth
		Intent intent = new Intent(this, BluetoothActivity.class);		
		intent.putExtra("index", positionIndex);
		intent.putExtra("title", pic.toString());		
		Toast.makeText(getApplicationContext(), "class name: " + BluetoothActivity.class, Toast.LENGTH_SHORT).show();		
		startActivity(intent);		
	}	
    
    
    void showCameraView(ImageView pic, int positionIndex) {
    	Intent intent = new Intent(this, CameraActivity.class);
    	startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CAMERA_REQUEST_CODE) {
    		if(resultCode == Activity.RESULT_OK) {
    			
    			/* gets whether or not the camera view activity successfully recognized image,
    			 * defaults to false 
    			 */
    			boolean acceptable = data.getBooleanExtra(CameraActivity.EXTRA_LABEL, false);
    			String text = acceptable ? "Image recognized!" : "Image not recognized. Please try again.";
    			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    		}
    	}
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
