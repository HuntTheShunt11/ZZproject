package com.zz3f5.maxime.bikedashboard;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zz3f5.maxime.bikedashboard.blueConnection.ConnectBThread;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public  BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public List<BluetoothDevice> foundDevices = new Vector<BluetoothDevice>();
    public ArrayAdapter<String> pairedDevicesArray = null;


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
                Log.d("bluetoothAdapter", "bluetooth discovery launched");
                Toast toast = Toast.makeText(context, "Bluetooth device searching...", Toast.LENGTH_LONG);
                toast.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
                Log.d("bluetoothAdapter", "bluetooth discovery finished");
                Toast toast = Toast.makeText(context, "Bluetooth device search finished", Toast.LENGTH_LONG);
                toast.show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                Log.d("bluetoothAdapter", "bluetooth device found");
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                foundDevices.add(device);
                Toast toast = Toast.makeText(context, "Found device " + device.getName(), Toast.LENGTH_LONG);
                toast.show();
                Log.d("bluetooth", "bluetooth device name " + device.getName());
            } else if (BluetoothDevice.ACTION_UUID.equals(action)) {
                Log.d("bluetooth", "on a trouvé uuid");
                /*BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName()=="MAXIME-PC") {
                    Parcelable[] uuids = new Parcelable[];
                    uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                    for (Parcelable ep : uuids) {
                        Log.d("UUID records : ", ep.toString());
                    }
                }*/
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra =null;
                uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                for (int i=0; i<uuidExtra.length; i++) {
                    Log.d("ACTION_UUID","Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayAdapter knownDevices = null;
        Button bluetoothB = (Button) findViewById(R.id.bluTooButton);
        bluetoothB.setOnClickListener(myhandler1);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //onClick();

            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getApplicationContext(), "Bluetooth non existant sur cet appareil", duration).show(); //marche pas
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }

            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }

            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            //filter.addAction(BluetoothDevice.ACTION_UUID);

            registerReceiver(mReceiver, filter);
            /*Log.d("bluetooth", "bluetooth discovery next");
            mBluetoothAdapter.startDiscovery();
            Log.d("bluetooth", "bluetooth discovery finished");*/

            final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            //foundDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            pairedDevicesArray = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    pairedDevicesArray.add(device.getName());
                    Log.d("pairedDevices", "paired Device found " + device.getName());
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose a bluetooth device");
            builder.setAdapter(pairedDevicesArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                    String itemClicked = pairedDevicesArray.getItem(which);
                    Toast toast = Toast.makeText(getApplicationContext(), "user clicked " + itemClicked, Toast.LENGTH_LONG);
                    toast.show();

                    Iterator it = pairedDevices.iterator();
                    while (it.hasNext()){
                        BluetoothDevice device = (BluetoothDevice)it.next();
                        Log.d("Connect", device.getName());
                        Log.d("Connect", itemClicked);
                        String deviceName = device.getName();
                        if(deviceName.equals(itemClicked)){
                            Log.d("Connect", "on passe là");
                            ConnectBThread connectBThread = new ConnectBThread(device, getApplicationContext());
                            Log.d("Connect", "ConnectBThread OK");
                            connectBThread.run();
                        }
                    }

                }
            });
            builder.show();

            //final CharSequence colors[] = new CharSequence[] {"red", "green", "blue", "black"};


                /*if (foundDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : foundDevices) {
                        // Add the name and address to an array adapter to show in a ListView
                        knownDevices.add(device.getName() + "\n" + device.getAddress());
                    }
                    ListView listBlue = (ListView) findViewById(R.id.listViewBlue);
                    listBlue.setAdapter(knownDevices);
                    listBlue.setVisibility(View.VISIBLE);
                }*/
            Log.d("bluetooth", "suite à la decouverte des objets");

           /* if(foundDevices.size() > 0){
                foundDevices.get(0).fetchUuidsWithSdp();
                IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_UUID);
                registerReceiver(mReceiver, filter2);
                Log.d("bluetooth", "on a trouvé des devices");
                //ConnectBThread connectBThread = new ConnectBThread(foundDevices.get(0));
                Log.d("Connect", "ConnectBThread OK");
                //connectBThread.run();
            }*/





        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("bluetooth", "activation acceptee par l'util");
        } else {
            Log.d("bluetooth", "activation refusee par l'util");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.zz3f5.maxime.bikedashboard/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.disable(); //à enlever en fin de projet, peut arrêter d'autres applis qui utilisent le bluetooth
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.zz3f5.maxime.bikedashboard/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}
