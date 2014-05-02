package safecommute.main;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class Maps extends Activity implements LocationListener{
	LocationManager locationManager;	
	Location location;
	String provider;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        
        /* Resize logo */
        Display display = getWindowManager().getDefaultDisplay(); 
		int screenWidth = display.getWidth();  // screen width
		int screenHeight = display.getHeight();  // screen height			
		int titleHeight = screenHeight/5;
		int titleWidth = screenWidth/4;
	
		// Assigning table rows
		final TableRow titlerow = (TableRow)findViewById(R.id.titlerow);
		
		TableRow.LayoutParams titleParams = new TableRow.LayoutParams(
			    titleWidth, titleHeight);
		titleParams.span = 3;
		
		// Adding logo
		ImageView title = new ImageView(this);
		title.setImageResource(R.drawable.logo);		
		title.setLayoutParams(titleParams);
		title.setScaleType(ImageView.ScaleType.FIT_CENTER);		
		titlerow.addView(title);
		
	


        
        
        
        
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        
        // Check first satellite location then Internet and then Sim Network
        provider = locationManager.getBestProvider(c, false);
        location = locationManager.getLastKnownLocation(provider);
        double lat = 0;
        double lng = 0;
        if (location != null)
        {
            lng = location.getLongitude();
            lat = location.getLatitude();
        }

        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        LatLng curr = new LatLng(lat, lng); // current lattitude and longitude

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 13));

        map.addMarker(new MarkerOptions()
                .position(curr));
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		
		double lng = location.getLongitude();
        double lat = location.getLatitude();
        
        LatLng curr = new LatLng(lat, lng); // current lattitude and longitude
        
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 13));

        map.addMarker(new MarkerOptions()
                .position(curr));
	}
}