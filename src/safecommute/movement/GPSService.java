package safecommute.movement;

import safecommute.main.*;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GPSService extends Service implements LocationListener {

	private final Context mContext;

	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean canGetLocation = false;

	private Location location;

	double latitude;
	double longitude;

	private static final long MIN_UPDATE_DISTANCE = 100; // meters
	private static final long MIN_UPDATE_TIME = 1000*60; // milliseconds

	protected LocationManager locationManager;
	
	private TextView mDistance;
    private Handler mHandler;
    private boolean mDistanceAdd;
    private float totalDistance;
    private Location firstLocation;

    private static final int UPDATE_DISTANCE = 3;

	private boolean DistanceRunning = false;

    private long time1 = 0;
    private SensorManager mSensorManager; // = getSystemService(Context.SENSOR_SERVICE);
    private Sensor mSensor;
    private boolean useAcc = false;
    private double maximum;
    
    private boolean mGeocoderAvailable;

	private LocationManager mLocationManager;


    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate();
        /**
        // The isPresent() helper method is only available on Gingerbread or above.
        mGeocoderAvailable =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();

        // Get a reference to the LocationManager object.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Toast.makeText(mContext, "Starting Setup", Toast.LENGTH_SHORT).show();
        */
        //mSensorManager.registerListener(accelerationListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        
        setup();
        
    }
    
    public void setup() {
        Location gpsLocation = null;
        Location networkLocation = null;
        mLocationManager.removeUpdates(listener);
        if(!mDistanceAdd)

        // Get fine location updates only.        
/**        if (mUseFine) {
            // Request updates from just the fine (gps) provider.
            gpsLocation = requestUpdatesFromProvider(
                    LocationManager.GPS_PROVIDER, R.string.not_support_gps);
        } 
        else if (mUseBoth) {
            // Get coarse and fine location updates.
*/            // Request upFdates from both fine (gps) and coarse (network) providers.
            gpsLocation = requestUpdatesFromProvider(
                    LocationManager.GPS_PROVIDER, R.string.wifi_only);
            networkLocation = requestUpdatesFromProvider(
                    LocationManager.NETWORK_PROVIDER, R.string.not_support_network);
            
            useAcc = true;
            maximum = 0;
            
            mSensorManager.registerListener(accelerationListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    		Toast.makeText(mContext, "Service Succesfully Started, shake phone to start app.", Toast.LENGTH_SHORT).show();

    }

    private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, 10*1000, 80, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        }
        return location;
    }
    
	public GPSService() {
		this.mContext = null;
		onCreate();
	}

	public GPSService(Context context) {
		this.mContext = context;

        // The isPresent() helper method is only available on Gingerbread or above.
        mGeocoderAvailable =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();

        // Get a reference to the LocationManager object.
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        
        setup();

	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				//if GPS is off, get location from Network Provider
				if (isNetworkEnabled && !isGPSEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME,
							MIN_UPDATE_DISTANCE, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get location using GPS Services
				else {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME,
								MIN_UPDATE_DISTANCE, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Calling this function will stop the app using GPS
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSService.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Function to check GPS/Network enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
/**
	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		long time2 = System.currentTimeMillis();
    	if (mDistanceAdd)
    		updateDistance(location, firstLocation, time1, time2);
        firstLocation = location;
        time1 = time2;
	}
*/
    private void updateDistance(Location location1, Location location2, long timeStart, long timeFinish){
    	Toast.makeText(mContext, "Updating Distance...", Toast.LENGTH_SHORT).show();
    	totalDistance = location1.distanceTo(location2);
    	float timeDiff = (timeFinish-timeStart)/(1000);
    	float velocity = totalDistance/timeDiff;
    	
    	if(velocity > 1){
//    	if(velocity > 6.7056){
    		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    		Ringtone r = RingtoneManager.getRingtone(mContext, notification);
    		r.play();
    		
    		
    		Toast.makeText(mContext, "YOU ARE GOING " + velocity + " m/s!!!", Toast.LENGTH_LONG).show();
			stopDistanceAdder(mDistance);
			//startActivity(mainActivity)
			//at top include package that mainactivity is in
			//instantiate an object: MainActiviy main = new MainActivity();
    	}
    	else{
    		Toast.makeText(mContext, "time: " + timeDiff + " seconds", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void stopDistanceAdder(View v){
    	mDistanceAdd = false;
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

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public SensorEventListener accelerationListener = new SensorEventListener() {
	
    public void onSensorChanged(SensorEvent event){
  	  if(useAcc){
	    	  double total_acc = Math.abs(Math.sqrt((double)(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2])));
	    	  maximum = Math.max(total_acc, maximum);
	    	  
	    	  if (total_acc > 3){
	    		  Toast.makeText(mContext, "Bumpin' " + mDistanceAdd, Toast.LENGTH_SHORT).show();
	    		  startDistanceAdder(mDistance);
	    		  Intent intent = new Intent(mContext, safecommute.main.LockScreen.class);
	    		  intent.setAction(Intent.ACTION_VIEW);
	    		  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		  intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    		  mContext.startActivity(intent);	    		  
	    	  }
  	  }
  	}

	//@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		//DO NOTHING!!!
	}
	
	 
    };
	
	public void onClickSensorRead(View poop){
		useAcc = true;
      maximum = 0;
      
      mSensorManager.registerListener(accelerationListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

	}

public void startDistanceAdder(View v){
if (!DistanceRunning){
		Toast.makeText(mContext, "startDistanceAdder begin!", Toast.LENGTH_SHORT).show();
		useAcc = false;
		DistanceRunning = true;
    	mDistanceAdd = true;
    	totalDistance = 0;
    	Location gps;
    	Location netloc;
/**	    	if (mUseFine){
    		gps = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
    		firstLocation = gps;}
    	else if (mUseBoth){	*/
    			gps = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
    			netloc = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
    			if (gps != null){ 
    				Toast.makeText(mContext, "GPS: " + gps.getLatitude() + ", " + gps.getLongitude(), Toast.LENGTH_SHORT).show();
                    firstLocation = gps;
    			}
                else if (netloc != null){
    				Toast.makeText(mContext, "Network: " + netloc.getLatitude() + ", " + netloc.getLongitude(), Toast.LENGTH_SHORT).show();
                    firstLocation = netloc;
                }
                else
            		Toast.makeText(mContext, "LOCATING SATELLITES", Toast.LENGTH_LONG).show();

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


@Override
public void onLocationChanged(Location location) {
	// A new location update is received.  Do something useful with it.  Update the UI with
    // the location update.
	/**LOOK HERE I'M IMPORTANT!!!!!!!!!!!!!!!!!!!*/
	long time2 = System.currentTimeMillis();
	if (mDistanceAdd)
		updateDistance(location, firstLocation, time1, time2);
    firstLocation = location;
    time1 = time2;}

}

