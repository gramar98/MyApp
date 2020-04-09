package com.example.lab5;

import android.os.Parcel;
import android.os.Parcelable;

public class Repo implements Parcelable {
    public String fullName;
    public String description;

    public Repo(){}
    public Repo(Parcel in) {
        fullName = in.readString();
        description = in.readString();
    }

    @Override
    public String toString() {
        return "Repo{" +
                "fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(description);
    }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {
        @Override
        public Repo createFromParcel(Parcel in) {
            return new Repo(in);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };
}
