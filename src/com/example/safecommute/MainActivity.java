// Kru Shah
//Aaron
//Mark
//Jakob
package com.example.safecommute;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	        going to be the fragment container for the main view
		setContentView(R.layout.activity_main); // main program
		//setContentView(R.layout.alpha_clickable_title); // Jakob Test
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	

	//test
}
