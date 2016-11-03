// IMyAidlInterface.aidl
package com.wei.ipc;
import com.wei.ipc.Person;
import com.wei.ipc.IOnNewPersonArrivedListener;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    List<com.wei.ipc.Person> getPersonList();
    void addPerson(in com.wei.ipc.Person person);
    void registerListener(IOnNewPersonArrivedListener listener);
    void unregisterListener(IOnNewPersonArrivedListener listener);
}
