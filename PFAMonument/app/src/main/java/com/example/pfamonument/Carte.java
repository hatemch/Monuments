package com.example.pfamonument;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Carte extends Fragment implements OnMapReadyCallback {

    GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng musee = new LatLng(35.822069, 10.635394);
        LatLng plage = new LatLng(35.835963, 10.638065);
        LatLng medina = new LatLng(35.824681, 10.637995);
        LatLng ribat = new LatLng(35.827956, 10.638716);
        LatLng kobbah = new LatLng(35.825308, 10.638233);
        map.addMarker(new MarkerOptions().position(musee).title("Musée archéologique de Sousse"));
        map.addMarker(new MarkerOptions().position(plage).title("Bou Jaafar Beach"));
        map.addMarker(new MarkerOptions().position(medina).title("Médina de Sousse"));
        map.addMarker(new MarkerOptions().position(ribat).title("RIBAT"));
        map.addMarker(new MarkerOptions().position(kobbah).title("Le musée des Traditions populaires à la Kobba"));
        map.moveCamera(CameraUpdateFactory.newLatLng(musee));
        map.moveCamera(CameraUpdateFactory.newLatLng(plage));
        map.moveCamera(CameraUpdateFactory.newLatLng(medina));
        map.moveCamera(CameraUpdateFactory.newLatLng(ribat));
        map.moveCamera(CameraUpdateFactory.newLatLng(kobbah));
    }
}
