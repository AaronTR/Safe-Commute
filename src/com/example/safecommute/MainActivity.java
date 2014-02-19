// Kru Shah
//Aaron
//Mark
//Jakob
package com.example.safecommute;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// going to be the fragment container for the main view
		setContentView(R.layout.activity_main); // main program
		//setContentView(R.layout.alpha_clickable_title); // Jakob Test
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
