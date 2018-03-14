package com.android.flashbackmusicv000;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Chelsea on 3/14/18.
 *
 * Mocking class to set up fake location for location testing.
 */

public class MockLocation{
        String providerName = "flp";
        Context ctx;

        public MockLocation(String name, Context ctx) {

            this.ctx = ctx;

            LocationManager lm = (LocationManager) ctx.getSystemService(
                    Context.LOCATION_SERVICE);
            lm.addTestProvider(providerName, false, false, false, false, false,
                    true, true, 0, 5);
            lm.setTestProviderEnabled(providerName, true);
        }

        public void pushLocation(double lat, double lon) {
            LocationManager lm = (LocationManager) ctx.getSystemService(
                    Context.LOCATION_SERVICE);

            Location mockLocation = new Location(providerName);
            mockLocation.setLatitude(lat);
            mockLocation.setLongitude(lon);
            mockLocation.setAltitude(0);
            mockLocation.setTime(System.currentTimeMillis());
            lm.setTestProviderLocation(providerName, mockLocation);
        }

        public void shutdown() {
            LocationManager lm = (LocationManager) ctx.getSystemService(
                    Context.LOCATION_SERVICE);
            lm.removeTestProvider(providerName);
        }

}
