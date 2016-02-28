package com.zz3f5.maxime.bikedashboard.gpsLocation;

import android.location.Location;
/**
 * Created by Maxime on 28/02/2016.
 */

public interface LocationTracker {
    public interface LocationUpdateListener{
        public void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime);
    }

    public void start();
    public void start(LocationUpdateListener update);

    public void stop();

    public boolean hasLocation();

    public boolean hasPossiblyStaleLocation();

    public Location getLocation();

    public Location getPossiblyStaleLocation();

}
