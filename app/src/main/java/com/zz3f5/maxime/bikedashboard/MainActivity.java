package com.zz3f5.maxime.bikedashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zz3f5.maxime.bikedashboard.gpsLocation.gpsNetworkLocation;
import com.zz3f5.maxime.bikedashboard.gpsLocation.gpxExport;
import com.zz3f5.maxime.bikedashboard.gpsLocation.locationListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleMap googleMap;
    private ArrayList<LatLng> pointList;
    private Polyline line;
    private gpsNetworkLocation gps;
    private locationListener listener;
    private boolean tripEnded = false;



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

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, "maps ok, on passe à la localisation gps", duration);
        toast.show();

        pointList = new ArrayList<>();//initialisé ici pour éviter bug null sur le onRestart
        /*gps = new gpsNetworkLocation(this);
        listener = new locationListener(this, gps, googleMap, pointList, line);*/
        //gps.start(listener);


    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if(pointList.size()!=0){
            Marker mP = googleMap.addMarker(new MarkerOptions().
                    position(pointList.get(0)).title("Départ").icon(BitmapDescriptorFactory.fromResource(R.drawable.dd_start)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointList.get(0), 15));

            if(pointList.size() > 1) {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                for (int i = 0; i < pointList.size(); i++) {
                    LatLng point = pointList.get(i);
                    options.add(point);
                }
                line = googleMap.addPolyline(options);

                if(tripEnded){
                    googleMap.addMarker(new MarkerOptions().
                            position(pointList.get(pointList.size()-1)).title("Arrivée").icon(BitmapDescriptorFactory.fromResource(R.drawable.dd_end)));
                }
            }

        }


    }

    @Override
    protected void onStop() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu); //on defini le menu de l'application avec le fichier xml correspondant (action_bar)
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //si l'action de départ de trip a ete selectionnee
            case R.id.action_tripStart:
                pointList = new ArrayList<>();
                gps = new gpsNetworkLocation(this);
                listener = new locationListener(this, gps, googleMap, pointList, line);
                gps.start(listener);
                Toast toast = Toast.makeText(this, "clic start, gps listener ok", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.action_tripStop:
                Toast toast2 = Toast.makeText(this, "clic stop", Toast.LENGTH_LONG);
                toast2.show();
                if(pointList.size()!=0){
                    Marker mP = googleMap.addMarker(new MarkerOptions().
                            position(pointList.get(0)).title("Départ").icon(BitmapDescriptorFactory.fromResource(R.drawable.dd_start)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointList.get(0), 15));

                    if(pointList.size() > 1) {
                        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                        for (int i = 0; i < pointList.size(); i++) {
                            LatLng point = pointList.get(i);
                            options.add(point);
                        }
                        line = googleMap.addPolyline(options);
                        googleMap.addMarker(new MarkerOptions().
                                position(pointList.get(pointList.size()-1)).title("Arrivée").icon(BitmapDescriptorFactory.fromResource(R.drawable.dd_end)));
                    }
                    tripEnded = true;
                }
                gps.stop();
                endOfTripMsg();
                break;
            default:
                break;
        }

        return true;
    }

    public void endOfTripMsg(){
        AlertDialog.Builder endBuilder = new AlertDialog.Builder(MainActivity.this);
        endBuilder.setTitle("Fin de trajet");
        endBuilder.setMessage("Voulez-vous partager votre trajet par email?");

        endBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked yes button
                // on cree le fichier d'export gpx et on lance l'application email

                String rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
                File fileDirectory = new File(rootPath + "/saved_images");
                fileDirectory.mkdirs();

                Random generator = new Random();
                int nb = 10;
                nb = generator.nextInt(nb);

                String filename = "exportGpxMoto-" + nb + ".gpx";
                File file = new File(fileDirectory, filename);
                if (file.exists())
                    file.delete();
                try {
                    gpxExport.writeToFile(file,pointList);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),file.getAbsolutePath(),Toast.LENGTH_LONG).show();
                //creation du mail
                Uri uri = Uri.parse("file://" + file.getAbsolutePath());

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
                intent.putExtra(Intent.EXTRA_TEXT, "body text");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Envoi d'un email"));


            }
        });
        endBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked no button
            }
        });
        AlertDialog dialog = endBuilder.create();
        dialog.show();
    }

}
