package com.android.flashbackmusicv000;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by cailintreseder on 2/18/18.
 */

public class IAddressResultReceiver {

    String mAddressOutput;
    String mAreaOutput;
    String mCityOutput;
    String mStateOutput;

    public IAddressResultReceiver(Handler handler) {
        //super(handler);
        Log.i("In: ", "AddressResultReceiver");

    }


    protected void onReceiveResult(int resultCode, Bundle resultData) {
        Log.i("In: ", "AddressResultReceiver.onReceiveResult");

        // Display the address string
        // or an error message sent from the intent service.
        mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);

        mAreaOutput = resultData.getString(FetchAddressIntentService.Constants.LOCATION_DATA_AREA);

        mCityOutput = resultData.getString(FetchAddressIntentService.Constants.LOCATION_DATA_CITY);

        mStateOutput = resultData.getString(FetchAddressIntentService.Constants.LOCATION_DATA_STREET);

    }
}
