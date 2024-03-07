package com.example.findme.Utils;


import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validaciones {
    //metodo para validar si es un valor numerico
    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

    //metodo para validar si es un email
    public static boolean isEmail(EditText campo) {

        boolean resultado;
        if (Patterns.EMAIL_ADDRESS.matcher(campo.getText()).matches()) {
            resultado = true;
        } else {
            campo.setError("Correo Invalido");
            campo.requestFocus();
            resultado = false;
        }

        return resultado;
    }

    //metodo para validar si editext esta vacio
    public static boolean Vacio(EditText campo){
        String dato = campo.getText().toString().trim();
        if(TextUtils.isEmpty(dato)){
            campo.setError("Campo Requerido");
            campo.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean VacioTextView(TextView campo){
        String dato = campo.getText().toString().trim();
        if(dato.equals("Fecha de Nacimiento")){
            campo.setError("Campo Requerido");
            campo.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }


    public static boolean largoPassword(EditText campo){
        String dato = campo.getText().toString().trim();
        if(dato.length()<=5){
            campo.setError("Debe ser de 6 o mas caracteres");
            campo.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean samePassword(EditText campo ,EditText campo2){
        String dato = campo.getText().toString().trim();
        String dato2 = campo2.getText().toString().trim();
        if(!dato.equals(dato2)){
            campo2.setError("Contraselas deben ser identicas");
            campo2.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }
    public static Boolean validaRut (EditText campo ) {
        String rut = campo.getText().toString().trim();
        rut = rut.replace(".", "");
        Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$");
        Matcher matcher = pattern.matcher(rut);
        if ( !matcher.matches()) {
            campo.setError("Formato invalido usar (11111111-1)");
            campo.requestFocus();
            return false;
        }
        String[] stringRut = rut.split("-");
        if(stringRut[1].toLowerCase().equals(Validaciones.dv(stringRut[0])) ){
            return  true;
        }else{
            campo.setError("Rut Invalido");
            campo.requestFocus();
            return false;
        }
    }

    public static String dv ( String rut ) {
        Integer M=0,S=1,T=Integer.parseInt(rut);
        for (;T!=0;T=(int) Math.floor(T/=10))

            S=(S+T%10*(9-M++%6))%11;
        return ( S > 0 ) ? String.valueOf(S-1) : "k";
    }


    public static boolean EstaDentroCirculo (Marker marker, Circle circle){
        float[] disResultado = new float[2];
        LatLng pos = marker.getPosition();
        double radtest = circle.getRadius();

        Location.distanceBetween( pos.latitude, pos.longitude,
                circle.getCenter().latitude,
                circle.getCenter().longitude,
                disResultado);

        if(disResultado[0] > radtest){
           return false;

        } else {
            return true;
        }
    }

}
