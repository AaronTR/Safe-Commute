package safecommute.movement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;
import safecommute.main.*;

public class StartAtBootReceiver extends BroadcastReceiver{	
	public static GPSService loc = null;

	@Override
    public void onReceive(Context sContext, Intent intent) {
    	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(sContext, notification);
		r.play();
		Toast.makeText(sContext, "Hello World!", Toast.LENGTH_SHORT).show();
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent dumIntent = new Intent(sContext, MarkIsADummyLikeThisActivity.class);
            dumIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sContext.startActivity(dumIntent);
        }
    }
}