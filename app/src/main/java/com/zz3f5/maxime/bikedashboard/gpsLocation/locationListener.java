package com.zz3f5.maxime.bikedashboard.gpsLocation;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Maxime on 28/02/2016.
 */
public class locationListener implements LocationTracker.LocationUpdateListener{

        gpsNetworkLocation gps;
        Context cont;
        GoogleMap map;
        ArrayList<LatLng> pointList;
        Polyline line;

        int counter = 0;
        String gpsLocationOrNot;
        String longi;
        String lat;
        String alt;
        String acc;

        public locationListener(Context c, gpsNetworkLocation g, GoogleMap m, ArrayList<LatLng> l, Polyline li){
            cont = c;
            gps = g;
            map = m;
            pointList = l;
            line = li;
        }


        @Override
        public void onUpdate(Location oldLoc, long oldTime, Location newLoc,
        long newTime) {
            counter++;
            if (gps.hasLocation()) {
                gpsLocationOrNot="GPS has location ( " + counter + " )";
            } else {
                gpsLocationOrNot="GPS has no location ( " + counter+ " )";
            }

            if (gps.hasPossiblyStaleLocation()) {
                gpsLocationOrNot = gpsLocationOrNot + " plus stale location";
            }

            Double longitude = newLoc.getLongitude();
            Double latitude = newLoc.getLatitude();
            Double height = newLoc.getAltitude();
            Float accuracy = newLoc.getAccuracy();

            longi = "Longitude = " + longitude.toString();
            lat = "Latitude  = " + latitude.toString();
            alt = "Altitude  = " + height.toString();
            acc = "Accuracy  = " + accuracy.toString();

            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(cont, gpsLocationOrNot, duration);
            toast.show();

            String info = longi + " " + lat;
            Toast toast1 = Toast.makeText(cont, info, duration);
            toast1.show();

            info = alt + " " + acc;
            Toast toast2 = Toast.makeText(cont,info,duration);
            toast2.show();

            LatLng position = new LatLng(latitude, longitude);

            pointList.add(position);

            if(counter == 1) {
                Marker mP = map.addMarker(new MarkerOptions().
                        position(position).title("Départ"));
            }

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

            if(counter > 1) {
                if(counter > 2){
                    line.remove();
                }
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                for (int i = 0; i < pointList.size(); i++) {
                    LatLng point = pointList.get(i);
                    options.add(point);
                }
                line = map.addPolyline(options);
            }

        }

}
