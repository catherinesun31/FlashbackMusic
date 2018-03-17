package tests;

import com.android.flashbackmusicv000.AnonymousUser;
import com.android.flashbackmusicv000.SignedInUser;
import com.android.flashbackmusicv000.User;
import com.android.flashbackmusicv000.UserBuilder;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by Chelsea on 3/16/18.
 *
 * Tests the UserBuilder and the HashMap at the same time, as they are part of the same related
 * scenario
 */

public class UserbuilderAndHashmapTesting {

    /*
    Create two seperate users and check if they are anonymous or signed in users.
     */

    @Test
    public void testUserBuilder(){
        UserBuilder ub = new UserBuilder();
        ub.setID(5);
        User anonUser = ub.build();

        assertEquals(anonUser instanceof AnonymousUser, true);

        UserBuilder ub2 = new UserBuilder();
        ub.setUsername("FakeUsername");
        ub.setEmail("fakeEmail");
        User signedInUser = ub.build();
        assertEquals(signedInUser instanceof SignedInUser, true);
    }

    /*
    create a bunch of anonymous users and check the names hashed to seperate locations
    Tests the entire hashclass works functionally as a whole
     */
    @Test
    public void testHashing(){
        UserBuilder ub = new UserBuilder();
        ub.setID(5);
        User anonUser = ub.build();


        //even given same ID should resolve hash to a different fruithttps://www.dropbox.com/s/fg7gc1tmdl1jre8/transmission-002-the-blackhole.mp3?dl=1
        UserBuilder ub1 = new UserBuilder();
        ub1.setID(5);
        User anonUser1 = ub1.build();

        assertNotSame(anonUser.getUsername(), anonUser1.getUsername());
    }
}
