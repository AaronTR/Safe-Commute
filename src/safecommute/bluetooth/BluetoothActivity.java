package safecommute.bluetooth;

import safecommute.main.*;

import java.util.Set;

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
		setContentView(R.layout.bluetooth_main);

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
				pairedBtArrayAdapter.clear();
				// add to an array adapter to show in a ListView
				for (BluetoothDevice device : pairedDevices) {
					pairedBtArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				pairedBtArrayAdapter.notifyDataSetChanged();
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

						if (BluetoothDevice.ACTION_FOUND.equals(action)) {
							// Get the BluetoothDevice object from the Intent
							BluetoothDevice device = intent
									.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
							String btAddress = (device.getName() + "\n" + device
									.getAddress());
							// -1 position means not in list
							if (inRangeBtArrayAdapter.getPosition(btAddress) == -1) {
								inRangeBtArrayAdapter.add(btAddress);
								inRangeBtArrayAdapter.notifyDataSetChanged();
							}
						}
					}
				};

				IntentFilter filter = new IntentFilter(
						BluetoothDevice.ACTION_FOUND);
				registerReceiver(btReceiver, filter);

			}
		});
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    //savedInstanceState.putAll();
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    //mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
	    //mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
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
