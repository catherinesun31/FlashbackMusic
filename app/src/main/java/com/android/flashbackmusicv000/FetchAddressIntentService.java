package com.android.flashbackmusicv000;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.LocationResult;

import static android.content.ContentValues.TAG;

public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;
    private static final String TAG = FetchAddressIntentService.class.getSimpleName();
    private static final String ACTION_PROCESS_UPDATES =
            "com.android.flashbackmusicv000.action" + ".PROCESS_UPDATES";

    public FetchAddressIntentService() {
        super(TAG);
        Log.i("In: ", "FetchAddressIntentService");

    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.i("In: ", "FetchAddressIntentService.onHandleIntent");

        String errorMessage = "";

        if (intent != null) {
            mReceiver = intent.getParcelableExtra(FetchAddressIntentService.Constants.RECEIVER);
            if (mReceiver == null) {
                Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
                return;
            }
            // Get the location passed to this service through an extra.
            Location location = intent.getParcelableExtra(
                    Constants.LOCATION_DATA_EXTRA);

            if (location == null) {
                errorMessage = "No location data provided";
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null);
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.US);

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = "Invalid lat/long";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }

            // Handle case where no address was found.
            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "No address found";
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null);
            }
            //SUCCESS!!!
            else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                Log.i(TAG, "Address found");
                deliverResultToReceiver(Constants.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"),
                                addressFragments), address);
            }
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Log.i("In: ", "FetchAddressIntentService.deliverResultToReceiver");

        try {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.RESULT_DATA_KEY, message);
            bundle.putString(Constants.LOCATION_DATA_AREA, address.getSubLocality());

            bundle.putString(Constants.LOCATION_DATA_CITY, address.getLocality());
            bundle.putString(Constants.LOCATION_DATA_STREET, address.getAddressLine(0));
            mReceiver.send(resultCode, bundle);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public final class Constants{
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.google.android.gms.location.sample.locationaddress";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";
    }
}


