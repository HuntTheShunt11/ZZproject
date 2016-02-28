package com.zz3f5.maxime.bikedashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zz3f5.maxime.bikedashboard.gpsLocation.gpsNetworkLocation;
import com.zz3f5.maxime.bikedashboard.gpsLocation.locationListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient gApiClient;
    private GoogleMap googleMap;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private int counter = 1;
    private Polyline line;
    ArrayList<LatLng> pointList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Avant de commencer une ballade");
        builder.setMessage("Utilisez-vous un dongle OBDII sur votre machine?");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked yes button
                //creation d'une connection bluetooth au dongle

            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked no button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            LatLng sydney = new LatLng(-34, 151);

            /*Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(sydney).title("TutorialsPoint"));*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, "maps ok, on passe à la localisation gps", duration);
        toast.show();

        /*pointList = new ArrayList<>();
        if (gApiClient == null) {
            gApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds*/

        gpsNetworkLocation gps = new gpsNetworkLocation(this);
        locationListener listener = new locationListener(this, gps, googleMap);
        gps.start(listener);

        Toast toast1 = Toast.makeText(this, "gpsListener lancé", duration);
        toast1.show();


    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStop() {
       // gApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*Action viewAction = Action.newAction(
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
        client.disconnect();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
       // gApiClient.connect();
        super.onStart();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*client.connect();
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
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onPause(){
        /*if (gApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(gApiClient, this);
            gApiClient.disconnect();
        }*/
        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(gApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(gApiClient, locationRequest, this);
        }
        else {
            handleNewLocation(location);
        }


    }

    private void handleNewLocation(Location location) {
        counter ++;
        LatLng position = new LatLng(location.getLatitude(),location.getLongitude());
        pointList.add(position);
        if(counter == 1) {
            Marker mP = googleMap.addMarker(new MarkerOptions().
                    position(position).title("départ"));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        if(counter > 1) {
            if(counter > 2){
                line.remove();
            }
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < pointList.size(); i++) {
                LatLng point = pointList.get(i);
                options.add(point);
            }
            line = googleMap.addPolyline(options);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Main Activity", "Location services connection failed with code " + connectionResult.getErrorCode());
            int duration = Toast.LENGTH_LONG;
            Toast toast3 = Toast.makeText(this, "Location services connection failed with code " + connectionResult.getErrorCode(), duration);
            toast3.show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}
