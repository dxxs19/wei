// IOnNewPersonArrivedListener.aidl
package com.wei.ipc;
import com.wei.ipc.Person;

// Declare any non-default types here with import statements

interface IOnNewPersonArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    void onNewPersonArrived(in Person newPerson);
}
