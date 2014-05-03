package safecommute.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class EmergencyCall extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caller);
         
        View callButton = findViewById(R.id.call);
        callButton.setOnClickListener(this);
    }

	@Override
	 public void onClick(View v) {
		  // TODO Auto-generated method stub
		  switch(v.getId())
		  {
		   case R.id.call:
		    phoneCall();
		    break;
		   default:
		    break;
		  }
	}
	
	void phoneCall () {
		 String phoneCallUri = "tel:3143974107"; // mark tabor's phone number (looking at you ladies)
	     Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
	     phoneCallIntent.setData(Uri.parse(phoneCallUri));
	     startActivity(phoneCallIntent);
	}

}
