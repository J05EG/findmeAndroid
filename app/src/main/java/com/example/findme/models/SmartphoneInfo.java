package com.example.findme.models;

import android.location.Location;

public class SmartphoneInfo {
    private String smartphoneId,beacondId;
    private Location ubicacinActual;

    public String getSmartphoneId() {
        return smartphoneId;
    }

    public void setSmartphoneId(String smartphoneId) {
        this.smartphoneId = smartphoneId;
    }

    public String getBeacondId() {
        return beacondId;
    }

    public void setBeacondId(String beacondId) {
        this.beacondId = beacondId;
    }

    public Location getUbicacinActual() {
        return ubicacinActual;
    }

    public void setUbicacinActual(Location ubicacinActual) {
        this.ubicacinActual = ubicacinActual;
    }
}
