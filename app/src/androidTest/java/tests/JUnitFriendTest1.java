package tests; /**
 * Created by MobileComputerWizard on 3/6/2018.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.flashbackmusicv000.ForAccessingResourcesActivity;
import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.friend.Friend;
import com.android.flashbackmusicv000.friend.NameGenerator;
import com.android.flashbackmusicv000.friend.NonFriend;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)

public class JUnitFriendTest1 {

    private Friend friend1;
    private Friend friend2;
    private Friend friend3;

    private NonFriend fiend1;
    private NonFriend fiend2;

    private File file;
    private ArrayList<NonFriend> fiends;
    private NameGenerator ng;
    private ArrayList<String> names;

    private InputStream is;
    @Rule
    public ActivityTestRule<ForAccessingResourcesActivity> testActivity = new ActivityTestRule<ForAccessingResourcesActivity>(ForAccessingResourcesActivity.class);
    @Before
    public void setUp(){

        Activity ac = testActivity.getActivity();

        Resources resources = ac.getResources();

        is = resources.openRawResource(R.raw.namesdb);

        //ClassLoader classLoader = ac.getClass().getClassLoader();
        //file = new File(classLoader.getResource("..\\res\\namesdb.csv").getFile());

        //File f = ac.getResources().getIdentifier("namesdb",
                //"raw", ac.getPackageName());
        //resources.openRawResource(R.raw.namesdb);
        //String path = "..\\res\\namesdb.csv";
        //String path = "../res/namesdb.csv";
        //String path = "src/androidTest/java/res/namesdb.csv";
        //String path = "src\\androidTest\\java\\res\\namesdb.csv";
        //ClassLoader classLoader = getClass().getClassLoader();
        //file = new File(classLoader.getResource("..\\res\\namesdb.csv").getFile());

        //file = testActivity.getResource(R.raw.namesdb);
        //file = new File("..\\res\\namesdb.csv");
        //
        //file testActivity.getClass().getResource("somefile").getFile();


        ng = new NameGenerator(is);

        ng.getRandomName();

        friend1 = new Friend("Abdi");
        friend2 = new Friend("Mukhtar");
        friend3 = new Friend("Abdi");
        fiend1 = new NonFriend(ng);
        fiend2 = new NonFriend(ng);

        NonFriend fiendX = null;
        fiends = new ArrayList<NonFriend>();

        int count = 0;

        //these two tests take far too long.
        /*
            try {

            while ((fiendX = new NonFriend(ng)) != null) {


                fiends.add(fiendX);
                System.out.println(count);
                ++count;
            }




        } catch(NullPointerException npe){



        }*/
        /*
        while ((fiendX = new NonFriend(ng)) != null) {

            fiends.add(fiendX);
            System.out.println(count);
            ++count;
        }
        */

        String name = null;
        names = new ArrayList<String>();

        while((name = ng.getRandomName())!= null){

            names.add(name);

        }
    }

    /*
    @Test
    public void checkFile(){

        //assertTrue(file.exists());
    }*/

    //testing objects, the constructor in effect.

    @Test
    public void testPrintObjects() {

        assertEquals("Abdi",""+friend1);
        //random name, cannot know the equality, but should not be empty
        assertNotEquals("",""+fiend1);
        assertNotEquals(null,""+fiend1);
        //System.out.println(friend2);
        //System.out.println(fiend);
    }


    @Test
    public void testObjectInEquality(){
        //different names
        assertNotEquals(friend1.hashCode(),fiend1.hashCode());
        //same names
        assertNotEquals(friend1.hashCode(),friend2.hashCode());

    }


    @Test
    public void testNames() {
        // are they all unique????
        String currentName;
        ArrayList<String> copyNames = new ArrayList<String>();
        for(String s : names){

            assertFalse(copyNames.contains(s));
            copyNames.add(s);

        }
   }
}
