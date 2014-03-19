package safecommute.bluetooth;

import java.util.Set;

import com.example.safecommute.R;
import com.example.safecommute.R.id;
import com.example.safecommute.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_DISCOVERABLE_BT = 1;
	BluetoothAdapter mBluetoothAdapter = null;
	BroadcastReceiver btReceiver = null;
	ListView pairedBtList;
	ListView inRangeBtList;
	ArrayAdapter<String> pairedBtArrayAdapter;
	ArrayAdapter<String> inRangeBtArrayAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView out = (TextView) findViewById(R.id.out);
		final Button turnOnButton = (Button) findViewById(R.id.turnOnButton);
		final Button discoverButton = (Button) findViewById(R.id.discoverButton);
		final Button turnOffButton = (Button) findViewById(R.id.turnOffButton);
		final Button scanDevicesButton = (Button) findViewById(R.id.scanDevicesButton);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		pairedBtList = (ListView) findViewById(R.id.pairedList);
		pairedBtArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, 0);
		pairedBtList.setAdapter(pairedBtArrayAdapter);
		
		inRangeBtList = (ListView) findViewById(R.id.deviceList);
		inRangeBtArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, 0);
		inRangeBtList.setAdapter(inRangeBtArrayAdapter);
		
		// check if the device supports Bluetooth
		if (mBluetoothAdapter == null) {
			// if not display so
			out.append("\tDevice not supported");
		} else {
			// if it does display and check for paired devices
			out.append("\tDevice supported");
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
					.getBondedDevices();
			// if the size > 0 there are paired devices
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					// add to an array adapter to show in a ListView
					pairedBtArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
			}
		}

		turnOnButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled()) {
					startBluetooth();
					displayToast("STARTING BLUETOOTH");
				}
			}
		});

		discoverButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isDiscovering()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					startActivityForResult(enableBtIntent,
							REQUEST_DISCOVERABLE_BT);
					displayToast("DEVICE DISCOVERABLE");
				}
			}
		});
		turnOffButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBluetoothAdapter.isEnabled()) {
					mBluetoothAdapter.disable();
					displayToast("TURNING OFF BLUETOOTH");
				}
			}

		});
		scanDevicesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled()) {
					startBluetooth();
				}
				mBluetoothAdapter.startDiscovery();
				displayToast("SEARCHING FOR DEVICES");
				
				btReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
				    String action = intent.getAction();

				    if (BluetoothDevice.ACTION_FOUND.equals(action)) 
				    {
				        // Get the BluetoothDevice object from the Intent
				        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				        // Add the name and address to an array adapter to show in a ListView
				       inRangeBtArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				    }
				  }
				};

				IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
				registerReceiver(btReceiver, filter);
				
			}
		});
	}

	public void displayToast(String s) {
		CharSequence text = s;
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void startBluetooth() {
		if (!(mBluetoothAdapter == null)) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			displayToast("DEVICE NOT SUPPORTED");
		}
	}

}
