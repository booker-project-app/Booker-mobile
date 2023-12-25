package com.example.bookingapp.fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.model.Accommodation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private MapView mapView;

    public double latitude;
    public double longitude;

    public MapFragment(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static MapFragment newInstance(double latitude, double longitude) {
        MapFragment fragment = new MapFragment(latitude, longitude);
        Bundle args = new Bundle();
        args.putDouble("lat", latitude);
        args.putDouble("lon", longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            latitude = args.getDouble("lat");
            longitude = args.getDouble("lon");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        Log.d("MyTag", "Nije usao");
        mapView.getMapAsync(googleMap -> {
            Log.d("MyTag", "Usao je!!!");
            LatLng place = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title("Address"));
            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(place, 12));
        });

        return view;
    }

}