package com.android.flashbackmusicv000.utility;

import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by MobileComputerWizard on 3/14/2018.
 */

public class Globals {

    public static boolean isOn;
    public static Activity currentActivity;
    public static Context currentContext;

    public static void setContext(){

        currentContext = currentActivity.getApplicationContext();

    }

    public static class SwitchListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // do something, the isChecked will be
            //Switch sw = (Switch) buttonView;

            if(isChecked){

                Toast.makeText(currentContext, "vibe mode is on", Toast.LENGTH_SHORT).show();
                isOn = true;


            } else {

                Toast.makeText(currentContext, "vibe mode is off", Toast.LENGTH_SHORT).show();
                isOn = false;

            }

        }
    }
}
