package com.zz3f5.maxime.bikedashboard.gpsLocation;

/**
 * Created by Maxime on 09/03/2016.
 */
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class gpxExport {
    private static final String TAG = gpxExport.class.getName();

    public static void writeToFile(File file, List<LatLng> points) {

        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
        String name = "<name>ballade en moto</name><trkseg>\n";

        String segments = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        for (LatLng l : points) {
            segments += "<trkpt lat=\"" + l.latitude + "\" lon=\"" + l.longitude + "\"></trkpt>\n";
        }

        String footer = "</trkseg></trk></gpx>";

        try {
            FileWriter writer = new FileWriter(file, false);
            writer.append(header);
            writer.append(name);
            writer.append(segments);
            writer.append(footer);
            writer.flush();
            writer.close();
            Log.i(TAG, "Saved " + points.size() + " points.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "Error Writting Path",e);
        }
    }
}
