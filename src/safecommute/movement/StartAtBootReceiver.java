package safecommute.movement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import safecommute.main.*;

public class StartAtBootReceiver extends BroadcastReceiver{	
	public static GPSService loc = null;

	@Override
    public void onReceive(Context sContext, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent dumIntent = new Intent(sContext, DumClass.class);
            dumIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sContext.startActivity(dumIntent);
        }
    }
}