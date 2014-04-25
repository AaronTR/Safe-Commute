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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableRow;

public class Maps extends Activity implements LocationListener{
	LocationManager locationManager;	
	Location location;
	String provider;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        
        
        // Add logo to the top
        Display display = getWindowManager().getDefaultDisplay(); 
		int screenWidth = display.getWidth();  // screen width
		int screenHeight = display.getHeight();  // screen height	
		int titleHeight = screenHeight/5;
		int titleWidth = screenWidth/4;
		
		

		ImageView titlerow = (ImageView)findViewById(R.id.logo);
		titlerow.setImageResource(R.drawable.logo);
		titlerow.getLayoutParams().height = titleHeight;
		titlerow.getLayoutParams().width = titleWidth;
		titlerow.setScaleType(ScaleType.FIT_XY);
		
		/*ImageView imageView = (ImageView)findViewById(R.id.logo);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		bitmap = Bitmap.createScaledBitmap(bitmap, titleWidth, titleHeight, true);
		imageView.setImageBitmap(bitmap);*/
	


        
        
        
        
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