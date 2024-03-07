package com.example.findme.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String CoodToDir(LatLng pos , Context context) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        addresses = geocoder.getFromLocation(pos.latitude, pos.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return String.format(address+", "+city+", "+state);
    }

    public static LatLng BuscadorUbicacion(Context context, String lugar) throws IOException{
        Geocoder geo = new Geocoder(context);
        LatLng latLng;
        int maxResultados = 1;
        List<Address> adress = geo.getFromLocationName(lugar, maxResultados);
        if (adress.isEmpty()){
            latLng = null;
        }else {
            latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
        }

        return latLng;
    }

}
