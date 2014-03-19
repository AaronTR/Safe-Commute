/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package safecommute.movement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.preference.*;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.math.*;

public class GPS extends FragmentActivity implements SensorEventListener {
    private TextView mDistance;
    private LocationManager mLocationManager;
    private Handler mHandler;
    private boolean mGeocoderAvailable;
//    private boolean mUseFine;
//    private boolean mUseBoth;
    private boolean mDistanceAdd;
    private float totalDistance;
    private Location firstLocation;
    
    private long start;
    private long startAcc;
    private long elapsed;
    private Handler timeHandler = new Handler();
    
    private long secs = 0L;
    private long mins = 0L;
    private long hrs = 0L;
    
    private String seconds;
    private String minutes;
    private String hours;

    // Keys for maintaining UI states after rotation.
    private static final String KEY_FINE = "use_fine";
    private static final String KEY_BOTH = "use_both";
    // UI handler codes.
    private static final int UPDATE_ADDRESS = 1;
    private static final int UPDATE_LATLNG = 2;
    private static final int UPDATE_DISTANCE = 3;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    
    private static final int Refresh = 100;
    
    private boolean stopped = false;
    
    private SensorManager mSensorManager;
    private Sensor mSensor;
    
    private boolean useAcc = false;
    private double maximum;
    private double times;
    private boolean DistanceRunning = false;
    private long time1 = 0;

    /**
     * This sample demonstrates how to incorporate location based services in your app and
     * process location updates.  The app also shows how to convert lat/long coordinates to
     * human-readable addresses.
     */
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_page);
        
        ((TextView)findViewById(R.id.timer)).setText("00:00:00");
        
//        SharedPreferences prefs = getSharedPreferences("winFitPref", 0);

//        mUseFine = prefs.getBoolean("Grain", true);
//        mUseBoth = !mUseFine;             
//        mDistance = (TextView) findViewById(R.id.distance);

        // The isPresent() helper method is only available on Gingerbread or above.
        mGeocoderAvailable =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();

        // Handler for updating text fields on the UI like the lat/long and address.
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_DISTANCE:
                    	mDistance.setText((String) msg.obj);
                    	break;
                }
            }
        };
        // Get a reference to the LocationManager object.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setup();
    }

    // Restores UI states after rotation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //setup();	/** ---	IS THIS MY PROBLEM? onStart() vs. setup()?	---	*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the GPS setting is currently enabled on the device.
        // This verification should be done during onStart() because the system calls this method
        // when the user returns to the activity, which ensures the desired location provider is
        // enabled each time the activity resumes from the stopped state.
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean wifiEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !wifiEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        	WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
        	wifiManager.setWifiEnabled(true);
        	final boolean wifiEnabled1 = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        	((TextView)findViewById(R.id.accelA1)).setText(String.valueOf(wifiEnabled1));
        	//wifiManager.setWifiEnabled(false);
            //new EnableGpsDialogFragment().show(getSupportFragmentManager(), "enableGpsDialog");
        }	
        setup();
    }

    // Method to launch Settings
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    // Stop receiving location updates whenever the Activity becomes invisible.
    @Override
    protected void onStop() {
        super.onStop();
        if (!mDistanceAdd)
        	mLocationManager.removeUpdates(listener);
        mSensorManager.unregisterListener(this);
    }

    // Set up fine and/or coarse location providers depending on whether the fine provider or
    // both providers button is pressed.
    private void setup() {
        Location gpsLocation = null;
        Location networkLocation = null;
        mLocationManager.removeUpdates(listener);
        if(!mDistanceAdd)
        	mDistance.setText("--NOT STARTED--");
        // Get fine location updates only.
        
/**        if (mUseFine) {
            // Request updates from just the fine (gps) provider.
            gpsLocation = requestUpdatesFromProvider(
                    LocationManager.GPS_PROVIDER, R.string.not_support_gps);

        } else if (mUseBoth) {
            // Get coarse and fine location updates.
*/            // Request upFdates from both fine (gps) and coarse (network) providers.
            gpsLocation = requestUpdatesFromProvider(
                    LocationManager.GPS_PROVIDER, R.string.wifi_only);
            networkLocation = requestUpdatesFromProvider(
                    LocationManager.NETWORK_PROVIDER, R.string.not_support_network);
//S        }
    }

    /**
     * Method to register location updates with a desired location provider.  If the requested
     * provider is not available on the device, the app displays a Toast with a message referenced
     * by a resource id.
     *
     * @param provider Name of the requested provider.
     * @param errorResId Resource id for the string message to be displayed if the provider does
     *                   not exist on the device.
     * @return A previously returned {@link android.location.Location} from the requested provider,
     *         if exists.
     */
    private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, 60*1000, 800, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, errorResId, Toast.LENGTH_SHORT).show();
        }
        return location;
    }
    
    private void updateDistance(Location location1, Location location2, long timeStart, long timeFinish){
    	totalDistance = location1.distanceTo(location2);
    	float localDistance;
    	localDistance = totalDistance;
    	float timeDiff = (timeFinish-timeStart)/(1000);
    	float velocity = totalDistance/timeDiff;
    	
    	if(velocity > 6.7056){
    		Message.obtain(mHandler,UPDATE_DISTANCE, "YOU ARE GOING " + velocity + " m/s!!!").sendToTarget();
			stopDistanceAdder(mDistance);
			//startActivity(mainActivity)
			//at top include package that mainactivity is in
			//instantiate an object: MainActiviy main = new MainActivity();
    	}
    	else{
    		Toast.makeText(this, "time: " + timeDiff + " seconds", Toast.LENGTH_LONG).show();
    	}
    }

    private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
        	/**LOOK HERE I'M IMPORTANT!!!!!!!!!!!!!!!!!!!*/
        	long time2 = System.currentTimeMillis();
        	if (mDistanceAdd)
        		updateDistance(location, firstLocation, time1, time2);
            firstLocation = location;
            time1 = time2;
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /** Determines whether one Location reading is better than the current Location fix.
      * Code taken from
      * http://developer.android.com/guide/topics/location/obtaining-user-location.html
      *
      * @param newLocation  The new Location that you want to evaluate
      * @param currentBestLocation  The current Location fix, to which you want to compare the new
      *        one
      * @return The better Location object based on recency and accuracy.
      */
    protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return newLocation;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return newLocation;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return newLocation;
        }
        return currentBestLocation;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    // Dialog to prompt users to enable GPS on the device.
    @SuppressLint("ValidFragment")
	private class EnableGpsDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Enable GPS")
                    .setMessage("GPS provider not enabled. Would you like to enable it?")
                    .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enableLocationSettings();
                        }
                    })
                    .create();
        }
    }

    public void startDistanceAdder(View v){
    if (!DistanceRunning){
    		DistanceRunning = true;
	    	mDistanceAdd = true;
	    	totalDistance = 0;
	    	Location gps;
	    	Location wifi;
/**	    	if (mUseFine){
	    		gps = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
	    		firstLocation = gps;}
	    	else if (mUseBoth){	*/
	    			gps = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
	    			wifi = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
	    			if (gps != null && wifi != null)
	                    firstLocation = getBetterLocation(gps, wifi);
	                else if (gps != null) 
	                    firstLocation = gps;
	                else if (wifi != null) 
	                    firstLocation = wifi;
	                else
	                	mDistance.setText("--LOCATING SATELLITES!--");
//	            }
	    	
	    	if (firstLocation == null)
	    		mDistance.setText("--LOCATING SATELLITES!--");
	
	    	ClickTimerStart(v);    	
    	}
    }
    
    public void stopDistanceAdder(View v){
    	mDistanceAdd = false;
    	useAcc = false;
    	DistanceRunning = false;
    	ClickTimerStop(v);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
    };
    
    private void updateTimer (float time){
    	secs = (long)(time/1000);
    	mins = (long)(time/60000);
    	hrs = (long)(time/3600000);
    	
    	secs = secs % 60;
    	seconds = String.valueOf(secs);
    	if(secs < 10)
    		seconds = "0" + seconds;
    	mins = mins % 60;
    	minutes = String.valueOf(mins);
    	if (mins <10)
    		minutes = "0"+minutes;
    	hours = String.valueOf(hrs);
    	if(hrs < 10)
    		hours = "0" + hours;
    	
    	((TextView)findViewById(R.id.timer)).setText(hours + ":" + minutes + ":" + seconds);
    }
        
    private Runnable startTimer = new Runnable(){
    	public void run() {
    		elapsed = System.currentTimeMillis() - start;
    		updateTimer(elapsed);
    		timeHandler.postDelayed(this, Refresh);
    	}
    };
    
    public void ClickTimerStart (View view){
    		
    	start = System.currentTimeMillis();
    	
    	timeHandler.removeCallbacks(startTimer);
    	timeHandler.postDelayed(startTimer, 0);   
    	}
    
    public void ClickTimerStop (View view){
    	timeHandler.removeCallbacks(startTimer);
    	stopped = true;
    }
    
    public void onSensorChanged(SensorEvent event){
    	  if(useAcc){
	    	  double total_acc = Math.abs(Math.sqrt((double)(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2])));
	    	  times++;
	    	  double avg_acc = total_acc/times;
	    	  maximum = Math.max(total_acc, maximum);
	    	  
	    	  if (total_acc > 3){
	    		  Toast.makeText(this, "Bumpin'", Toast.LENGTH_SHORT).show();
	    		  startDistanceAdder(mDistance);
	    	  }
	    	  ((TextView)findViewById(R.id.accelM1)).setText(String.valueOf(maximum));
	    	  ((TextView)findViewById(R.id.accelA1)).setText(String.valueOf(avg_acc));
    	  }
    	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		//DO NOTHING!!!
	}
	
	public void onClickSensorRead(View poop){
		useAcc = true;
        maximum = 0;
        times = 0;
        
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    
}