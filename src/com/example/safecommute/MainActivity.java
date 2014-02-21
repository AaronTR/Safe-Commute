// Kru Shah
//Aaron
//Mark
//Jakob
package com.example.safecommute;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.widget.TableRow;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// going to be the fragment container for the main view
		setContentView(R.layout.activity_main); // main program
		//setContentView(R.layout.alpha_clickable_title); // Jakob Test
		
		// Resizing table rows, based on screen size
		Display display = getWindowManager().getDefaultDisplay(); 
		int screenWidth = display.getWidth();  // screen width
		int screenHeight = display.getHeight();  // screen height	
		
		// Dynamically adding rows in the Table
		TableRow titlerow = (TableRow)findViewById(R.id.titlerow); 
		titlerow.setMinimumHeight(screenHeight/4);
		
		TableRow appstextrow = (TableRow)findViewById(R.id.appstextrow); 
		appstextrow.setMinimumHeight(screenHeight/20);
		
		TableRow appsimagerow = (TableRow)findViewById(R.id.appsimagerow); 
		appsimagerow.setMinimumHeight(screenHeight/5);
		
		TableRow passengertextrow = (TableRow)findViewById(R.id.passengertextrow); 
		passengertextrow.setMinimumHeight(screenHeight/20);
		
		TableRow passengerimagerow = (TableRow)findViewById(R.id.passengerimagerow); 
		passengerimagerow.setMinimumHeight(screenHeight/5);
		
		TableRow emergencytextrow = (TableRow)findViewById(R.id.emergencytextrow); 
		emergencytextrow.setMinimumHeight(screenHeight/20);
		
		TableRow emergencyimagerow = (TableRow)findViewById(R.id.emergencyimagerow); 
		emergencyimagerow.setMinimumHeight(screenHeight/5); 
		
		
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

	//test
}
