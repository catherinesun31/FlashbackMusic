//import com.android.flashbackmusicv000.friend.input.namesDB;
package com.android.flashbackmusicv000.friend;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

//import data.*;

/**
 * Created by MobileComputerWizard on 3/5/2018.
 */

public final class NameGenerator {

    //private Context mContext;
    private File file;
    private ArrayList<String> names;
    //singleton pattern for this class.
    private static boolean initialised;
    private NameGenerator ng;

    //private URL url;
    //private URLConnection webConnection;
    private BufferedReader buffReadInput;
    private InputStream is;

    //JUST REMEMBER TO PASS THE CONTEXT HERE FOR ACCESS TO THE CSV FILE INSIDE ASSETS, THAT WE GET FROM ASSET MANAGER.

    public NameGenerator(InputStream i) {

        this.names = new ArrayList<String>();
        this.is = i;

        try {

            setInput();
            readFile();

        }catch (FileNotFoundException fnf){

                fnf.printStackTrace();

        } catch (IOException ioEx) {

            ioEx.printStackTrace();

        }

        initialised = true;
    }

    private void setInput()throws IOException{

        buffReadInput = new BufferedReader(new InputStreamReader(is));

    }

    private void readFile()throws IOException{

        String inputLine = "";

        names = new ArrayList<String>();
        while ((inputLine = buffReadInput.readLine()) != null) {

            //This also does not work.....
            this.names.add(inputLine);

        }

        buffReadInput.close();

    }

    public String getRandomName() throws NullPointerException{

        if(names.isEmpty()){
            return null;
        }

        int randomInteger = (int)(Math.random() * (names.size()));
        String name = names.get(randomInteger);
        names.remove(randomInteger);
        return name;

    }
    /*
    public static NameGenerator getInstance(){

        return ng;

    }

    public static NameGenerator getInstance(File f){

        if(!initialised){

            initialised = true;

            ng = new NameGenerator(f);

            return ng;

        }

        return ng;

    }*/
}
