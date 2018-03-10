package com.android.flashbackmusicv000; /**
 * Created by MobileComputerWizard on 3/6/2018.
 */

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.flashbackmusicv000.friend.Friend;
import com.android.flashbackmusicv000.friend.NameGenerator;
import com.android.flashbackmusicv000.friend.NonFriend;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
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

    @Rule
    public ActivityTestRule<ForAccessingResourcesActivity> testActivity = new ActivityTestRule<ForAccessingResourcesActivity>(ForAccessingResourcesActivity.class);
    @Before
    public void setUp(){

        //ClassLoader classLoader = this.getClass().getClassLoader();
        //URL resource = classLoader.getResource("C:\\Users\\MobileComputerWizard\\Desktop\\cse-110-team-project-team-35\\app\\src\\test\\res\\namesdb.csv");
        //file = new File("C:\\Users\\MobileComputerWizard\\Desktop\\cse-110-team-project-team-35\\app\\src\\test\\res\\namesdb.txt");
        file = new File("C:\\Users\\MobileComputerWizard\\Desktop\\friend\\namesdb.txt");
        //assertTrue(file != null);
        /*
        ng = NameGenerator.getInstance(file);

        friend1 = new Friend("Abdi");
        friend2 = new Friend("Mukhtar");
        friend3 = new Friend("Abdi");
        fiend1 = new NonFriend(ng);
        fiend2 = new NonFriend(ng);

        String name = "";
        while(ng.getRandomName() != null){

            fiends.add(new NonFriend(ng));

        }
        */
    }

    @Test
    public void fileTest(){

        assertTrue(file != null);
    }
    //testing objects, the constructor in effect.
    /*
    @Test
    public void testPrintObjects() {
        assertEquals("Abdi",friend1);
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
    public void testCompleteInEquality() {
        // are they all unique????

        ArrayList<String> names = new ArrayList<String>();
        String name;

        for(NonFriend fiend : fiends){

            assertFalse(fiends.contains(fiend));
            name = ""+fiend;
            assertFalse(names.contains(name));
            names.add(name);
        }
    }*/
}
