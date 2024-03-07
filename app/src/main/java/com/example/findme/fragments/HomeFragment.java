package com.example.findme.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.findme.R;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.Utils.Utils;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.UserDeviceHome;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    LocationManager locationManager;
    private Location location;
    private double latitud, longitud;
    private View mView;
    private MapView mMapView;
    private ConnectionBackend conn = new ConnectionBackend();
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return mView = inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.mapViewFrag);
        if(mMapView != null){
            mMapView.onCreate(getArguments());
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        final LatLng punto;
        if(location != null) {
            List<UserDeviceHome> listaUserDev= conn.ListarUsuariosReceptorHome(ConstantSQLite.ConsultarDatosMainUser(getContext()).getId());
            Bundle paramslocation = getArguments();
            if(paramslocation ==null){
                punto = new LatLng(location.getLatitude(), location.getLongitude());

            }else{
                if(paramslocation.getDouble("latDouble",0)!=0d){
                    punto =new LatLng(paramslocation.getDouble("latDouble",0),paramslocation.getDouble("longDouble",0));
                    LatLng puntoArea = new LatLng(paramslocation.getDouble("latDoubleCenter",0),paramslocation.getDouble("longDoubleCenter",0));
                    googleMap.addMarker(new MarkerOptions()
                            .position(punto).visible(true).title(paramslocation.getString("userName")));
                    googleMap.addCircle(new CircleOptions().center(puntoArea).radius(paramslocation.getDouble("radioCenter",0))
                            .fillColor(R.color.maps).clickable(false));
                }else{
                    punto = new LatLng(location.getLatitude(), location.getLongitude());
                    for (UserDeviceHome user: listaUserDev){
                        if(user.getLatUbcacion() != 0 && user.getLongUbicacion()!=0){
                            LatLng puntouser =new LatLng(user.getLatUbcacion(),user.getLongUbicacion());
                            googleMap.addMarker(new MarkerOptions()
                                    .position(puntouser).visible(true).title(user.getIdentificador()));
                            if(user.getLatRadio()!=0){
                                LatLng puntoArea = new LatLng(user.getLatRadio(),user.getLongRadio());
                                googleMap.addCircle(new CircleOptions().center(puntoArea).radius(user.getRadio())
                                        .fillColor(R.color.maps).clickable(false));
                            }
                        }
                    }
                }
            }

        }else {
            punto = new LatLng(-33.491285, -70.616766);
            Toast.makeText(getContext(), "Encienda Ubicación", Toast.LENGTH_SHORT).show();
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(punto, 18));
        googleMap.getUiSettings().setAllGesturesEnabled(true);

        searchView = mView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String search = searchView.getQuery().toString();
                LatLng lugar = null;
                try {
                    lugar = Utils.BuscadorUbicacion(getContext(), search);
                    if(lugar != null){
                        googleMap.addMarker(new MarkerOptions().position(lugar).title(search));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lugar, 18));
                    }else {
                        Toast.makeText(getContext(), "No se encontró ubicación", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(final LatLng latLng) {
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                Marker center = googleMap.addMarker(new MarkerOptions()
                        .position(latLng).visible(true).draggable(true));
                final Circle[] LimitArea = new Circle[1];
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker center ) {
                        latitud = latLng.latitude;
                        longitud = latLng.longitude;
                        String message = Double.toString(latitud);
                        String message2 = Double.toString(longitud);
                        Toast.makeText(getContext(), message+", "+message2, Toast.LENGTH_LONG).show();

                        LimitArea[0] = googleMap.addCircle(new CircleOptions().center(latLng)
                                .fillColor(R.color.maps));

                    }

                    @Override
                    public void onMarkerDrag(Marker center) {
                        center.setVisible(false);
                        float rad;
                        LatLng p2= center.getPosition();
                        Location pos1 = new Location("");
                        Location pos2 = new Location("");
                        pos1.setLatitude(latLng.latitude);
                        pos1.setLongitude(latLng.longitude);
                        pos2.setLatitude(p2.latitude);
                        pos2.setLongitude(p2.longitude);
                        rad = pos1.distanceTo(pos2);
                        LimitArea[0].setRadius(rad);
                    }

                    @Override
                    public void onMarkerDragEnd(Marker center) {
                        center.setPosition(LimitArea[0].getCenter());
                        center.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_foreground));
                        center.setVisible(true);
                        center.setDraggable(false);
                        LimitArea[0].setClickable(false);
                        googleMap.getUiSettings().setScrollGesturesEnabled(true);
                        //llamar funcion para insetrtar LimitArea
                        if(getArguments() == null){
                            Toast.makeText(getContext(),"No se puede agregar Area",Toast.LENGTH_LONG).show();
                            LimitArea[0].remove();
                            center.remove();
                        }else{
                            center.setTitle(getArguments().getString("userName"));
                            String userDeviceId = getArguments().getString("userDeviceId");
                            try {
                                conn.SetLimitArea(userDeviceId,LimitArea[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LimitArea[0].getCenter(), 18));
                        }
                    }
                });


            }
        });
    }
}