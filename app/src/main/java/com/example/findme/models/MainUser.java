package com.example.findme.models;

public class MainUser {
    private String id;
    private String idEmail;
    private String Password;
    private String idPerson;
    private String idMedioPago;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(String idEmail) {
        this.idEmail = idEmail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getIdMedioPago() {
        return idMedioPago;
    }

    public void setIdMedioPago(String idMedioPago) {
        this.idMedioPago = idMedioPago;
    }

    @Override
    public String toString() {
        return "MainUser{" +
                "id='" + id + '\'' +
                ", idEmail='" + idEmail + '\'' +
                ", idPassword='" + Password + '\'' +
                ", idPerson='" + idPerson + '\'' +
                ", idMedioPago='" + idMedioPago + '\'' +
                '}';
    }
}
