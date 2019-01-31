package com.example.game;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class map_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mapMarker;

    public double latitude = 46.536843;
    public double longitude = 1.652443;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  latitude= randomLatLng(-90,90);
      //  longitude = randomLatLng(-180,180);
        LatLng ici = new LatLng(latitude,longitude );
        mMap.addMarker(new MarkerOptions().position(ici).title("Vous êtes ici"+" Latitude:"+latitude+" | Longitude: "+longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ici));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
    public  void addMarker(View v){
        //mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        latitude= randomLatLng(-90,90);
        longitude = randomLatLng(-180,180);
        LatLng ici = new LatLng(latitude,longitude );
        mMap.addMarker(new MarkerOptions().position(ici).title("Vous êtes ici"+" Latitude:"+latitude+" | Longitude: "+longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ici));
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public double randomLatLng(double x,double y){
        Random r = new Random();
        Double l = x + r.nextDouble()*(y-x +1);
        return l;
    }
}
