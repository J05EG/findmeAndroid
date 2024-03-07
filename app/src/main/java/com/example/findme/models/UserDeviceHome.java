package com.example.findme.models;

public class UserDeviceHome {

    String identificador;
    double radio,latRadio,longRadio,latUbcacion,longUbicacion;
    public  UserDeviceHome(){

    }

    public UserDeviceHome(String identificador, double radio, double latRadio, double longRadio, double latUbcacion, double longUbicacion) {
        this.identificador = identificador;
        this.radio = radio;
        this.latRadio = latRadio;
        this.longRadio = longRadio;
        this.latUbcacion = latUbcacion;
        this.longUbicacion = longUbicacion;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public double getLatRadio() {
        return latRadio;
    }

    public void setLatRadio(double latRadio) {
        this.latRadio = latRadio;
    }

    public double getLongRadio() {
        return longRadio;
    }

    public void setLongRadio(double longRadio) {
        this.longRadio = longRadio;
    }

    public double getLatUbcacion() {
        return latUbcacion;
    }

    public void setLatUbcacion(double latUbcacion) {
        this.latUbcacion = latUbcacion;
    }

    public double getLongUbicacion() {
        return longUbicacion;
    }

    public void setLongUbicacion(double longUbicacion) {
        this.longUbicacion = longUbicacion;
    }
}
