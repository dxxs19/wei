package com.wei.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WEI on 2016/7/26.
 */
public class Person implements Parcelable
{
    public int personId;
    public String personName;
    public boolean isMale;

    public Person(int personId, String personName, boolean isMale)
    {
        this.personId = personId;
        this.personName = personName;
        this.isMale = isMale;
    }

    protected Person(Parcel in) {
        personId = in.readInt();
        personName = in.readString();
        isMale = in.readByte() != 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(personId);
        dest.writeString(personName);
        dest.writeByte((byte) (isMale ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", personName='" + personName + '\'' +
                ", isMale=" + isMale +
                '}';
    }
}
