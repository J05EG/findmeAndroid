package com.example.findme.models;

import android.location.Location;

public class LimitArea {
    private  double radio;
    private Location location;

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
