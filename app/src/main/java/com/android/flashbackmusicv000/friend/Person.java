package com.android.flashbackmusicv000.friend;

/**
 * Created by MobileComputerWizard on 3/5/2018.
 */
//change to builder pattern when we have to do lots of things with this class. Probably will become this design at some point.

public abstract class Person{

    protected String name;

    public Person(String name){

        this.name = name;

    }

    public Person(){

    }


    /*just to note here that I am overriding the to String method inherited by the
    * object class.This would usually print out the memory location if you call
    * System.out.println(friend); where friend is an object of the Person type.
    */

    @Override
    public String toString(){

        return name;

    }

}
