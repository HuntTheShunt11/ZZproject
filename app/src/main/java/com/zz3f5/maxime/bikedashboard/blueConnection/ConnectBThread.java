package com.zz3f5.maxime.bikedashboard.blueConnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Maxime on 03/02/2016.
 */
public class ConnectBThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Context mmContext;

    public ConnectBThread(BluetoothDevice device, Context appContext) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmContext = appContext;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID = UUID connection serie
            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            /*if(mmDevice.getUuids()!= null){
                MY_UUID.fromString(mmDevice.getUuids().toString());
            }
            //MY_UUID = device.fetchUuidsWithSdp();*/
            ParcelUuid UUIDs[] = mmDevice.getUuids();

            for (ParcelUuid u : UUIDs) {
                Log.d("UUID found", u.getUuid().toString());
            }
            //MY_UUID = UUIDs[0].getUuid();

            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mmAdapter.cancelDiscovery();
        Log.d("bluetooth socket", "enter in run");
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            Log.e("bluetooth socket", "connection failed!");
            Log.e("bluetooth socket", connectException.getMessage());
            Toast toast = Toast.makeText(mmContext, "Echec de la connexion Bluetooth", Toast.LENGTH_LONG);
            toast.show();
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        Log.d("bluetooth socket", "connection OK!");
        Toast toast = Toast.makeText(mmContext, "Connexion Bluetooth réussie", Toast.LENGTH_LONG);
        toast.show();
        // Do work to manage the connection (in a separate thread)
       // manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public BluetoothSocket getSocket(){
        return mmSocket;
    }


}
