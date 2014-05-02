package safecommute.main;
import safecommute.movement.GPSService;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;



public class DumClass extends Activity{
	private Intent LocIntent = null;
	public static GPSService loc = null;
	public static Service newService;
	
	@Override 
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Toast.makeText(this, "DUMMY WORKS", Toast.LENGTH_SHORT).show();
		
		LocIntent = new Intent(this, GPSService.class);
		startService(LocIntent);
		loc = new GPSService(this);
		
		finish();
	}
	

}
