package com.example.pushuttletracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.*;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class TrackActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (final Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                             //   LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                               // googleMap.addMarker(MarkerOptions.position(LatLng).title("MY location").snippet("Kochi").draggable(true));

                           //     MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");

                            //    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 50));

                             //   googleMap.addMarker(markerOptions).showInfoWindow();
                              //  LatLng Pakistan = new LatLng(31, 71);
                             //   googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Pakistan"));

                             //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                            }
                        });
                    }
                }
            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(TrackActivity.this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        client = LocationServices.getFusedLocationProviderClient(TrackActivity.this);
    //    client = LocationServices.getFusedLocationProviderClient(TrackActivity.this);

        if (ActivityCompat.checkSelfPermission(TrackActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(TrackActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getCurrentLocation() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();

         task.addOnSuccessListener(new OnSuccessListener<Location>() {
             @Override
             public void onSuccess(final Location location) {

                 if (location != null){

                     mapFragment.getMapAsync(new OnMapReadyCallback() {
                         @Override
                         public void onMapReady(GoogleMap googleMap) {
                             LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                             googleMap.addMarker(new MarkerOptions().position(new LatLng(31.5698,74.3120)).title("MY location").snippet("Anarkali").draggable(true));
                             googleMap.addMarker(new MarkerOptions().position(new LatLng(31.5313,74.3183)).title("Bus is here").snippet("Ichra").draggable(true));
                             //MarkerOptions  markerOptions = new MarkerOptions().position(latLng).title("You are here");

                             googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,75));

                             //googleMap.addMarker(markerOptions).showInfoWindow();
                             //LatLng Pakistan = new LatLng(31,71);
                             LatLng Lahore = new LatLng(31.5204,74.3587);
                             googleMap.addMarker(new MarkerOptions().position(Lahore).title("Marker in Lahore"));
                             //googleMap.addMarker(new MarkerOptions().position(Pakistan).title("Marker in Pakistan"));


                             googleMap.moveCamera(CameraUpdateFactory.newLatLng(Lahore));

                         }

                     });
                 }

             }
         });
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }else {
            Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
