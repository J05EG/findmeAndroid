package com.example.findme.models;

public class UserDevice {
    private String idPersonDevice;
     private Person datosPersona;
     private SmartphoneInfo datosMovil;
     private LimitArea limitArea;

    public Person getDatosPersona() {
        return datosPersona;
    }

    public String getIdPersonDevice() {return idPersonDevice; }

    public void setIdPersonDevice(String idPersonDevice) { this.idPersonDevice = idPersonDevice; }

    public void setDatosPersona(Person datosPersona) {
        this.datosPersona = datosPersona;
    }

    public SmartphoneInfo getDatosMovil() {
        return datosMovil;
    }

    public void setDatosMovil(SmartphoneInfo datosMovil) {
        this.datosMovil = datosMovil;
    }

    public LimitArea getLimitArea() { return limitArea; }

    public void setLimitArea(LimitArea limitArea) {
        this.limitArea = limitArea;
    }
}
