package com.example.pushuttletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
// import android.support.annotation.NonNull;
// import android.support.v4.app.ActivityCompat;
// import android.supportx.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentLocationMarker;
    private Location currentLocation;
    private boolean firstTimeFlag = true;
    private Button button1;
    private String selectedItemString;
    private String selectItemIdSt;
    private int selectedItemId;
    private TextView textAddress;

    private final View.OnClickListener clickListener = view -> {
        if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null)
            animateCamera(currentLocation);
    };

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            showMarker(currentLocation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(MapActivity.this);
        findViewById(R.id.currentLocationImageButton).setOnClickListener(clickListener);
        //Intent intent = getIntent();
        Intent intent = getIntent();
        selectedItemString = intent.getStringExtra("itemString");
        Toast.makeText(
                MapActivity.this,
                "Selected " + selectedItemString,
                Toast.LENGTH_SHORT
        ).show();
        selectItemIdSt = intent.getStringExtra("itemId");
        Log.i("MapACtivity",selectedItemString + " ! " + selectItemIdSt);
        // selectedItemId  = Integer.parseInt(intent.getStringExtra("itemId"));

        textAddress = (TextView) findViewById(R.id.textAddress);

        textAddress.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

// This is to prevent user to click on the map under the distance text.

            }

        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permission denied by uses", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
    }

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(13).build();
    }

    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Location locationA  = new Location("Location Current");
        Location locationB  = new Location("Location Bus Driver");
        LatLng latLngBus ;

        locationA.setLatitude(latLng.latitude);
        locationA.setLongitude(latLng.longitude);

        switch (Integer.parseInt(selectItemIdSt)) {
            case R.id.Punewcampus:
                    latLngBus = new LatLng(31.5033,74.2781);
                googleMap.addMarker(new MarkerOptions().position(latLngBus).title("Driver 1").snippet("Kareem Block").draggable(true));
                locationB.setLatitude(latLngBus.latitude);
                locationB.setLongitude(latLngBus.longitude);
                //LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                if (currentLocationMarker == null) {
                    currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                    // calculatedistance function call here
                    textAddress.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB) / 1000) + "km - Time: 1hr approx");
                    //float results[] = new float[10];
                    // Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 31.5033, 74.2781, results);
                    // Toast.makeText(MapActivity.this,"Distance:" +results,Toast.LENGTH_SHORT).show();
                }

                else {
                    MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                    // calculatedistance function call here

                  //  Bundle extras = getIntent().getExtras();
                    //int selectedItemId = extras.getInt("Item Id");
                    //int selectedItemString = extras.getInt("Item String");
                }
                // do your code
                break;

            case R.id.Puoldcampus:
                latLngBus = new LatLng(31.5153,74.3074);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(31.5153,74.3074)).title("Driver 2").snippet("Wahdat Colony").draggable(true));

                if (currentLocationMarker == null) {
                    currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                    // calculatedistance function call here

                    textAddress.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB) / 1000) + "km - Time: 40 min approx");

                    //float results[] = new float[10];
                    // Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 31.5033, 74.2781, results);
                    // Toast.makeText(MapActivity.this,"Distance:" +results,Toast.LENGTH_SHORT).show();
                }

                else {
                    MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                    // calculatedistance function call here

                  //  Bundle extras = getIntent().getExtras();
                    //int selectedItemId = extras.getInt("Item Id");
                    //int selectedItemString = extras.getInt("Item String");
                }
                // do your code
                break;
            case R.id.Thokar:
                // do your code
                latLngBus = new LatLng(31.4697,74.2728);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(31.4697,74.2728)).title("Driver 3").snippet("Johar Town").draggable(true));

                if (currentLocationMarker == null) {
                    currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                    // calculatedistance function call here

                    textAddress.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB) / 1000) + "km - Time: 45 min approx");

                    //float results[] = new float[10];
                    // Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 31.5033, 74.2781, results);
                    // Toast.makeText(MapActivity.this,"Distance:" +results,Toast.LENGTH_SHORT).show();
                }

                else {
                    MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                    // calculatedistance function call here

                   // Bundle extras = getIntent().getExtras();
                    //int selectedItemId = extras.getInt("Item Id");
                    //int selectedItemString = extras.getInt("Item String");
                }
                // do your code
                break;
            default:
                latLngBus = new LatLng(31.5033,74.2781);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(31.5033,74.2781)).title("Driver 1").snippet("Kareem Block").draggable(true));
                //LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                if (currentLocationMarker == null) {
                    currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                    // calculatedistance function call here

                    textAddress.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB) / 1000) + "km - Time: 1 hr approx");

                    //float results[] = new float[10];
                    // Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 31.5033, 74.2781, results);
                    // Toast.makeText(MapActivity.this,"Distance:" +results,Toast.LENGTH_SHORT).show();
                }

                else {
                    MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                    // calculatedistance function call here

                   // Bundle extras = getIntent().getExtras();
                    //int selectedItemId = extras.getInt("Item Id");
                    //int selectedItemString = extras.getInt("Item String");
                }
                break;
        }

       /* if(selectedItemId == "" )
        {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(31.5033,74.2781)).title("Driver 1").snippet("Kareem Block").draggable(true));
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            if (currentLocationMarker == null) {
                currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                // calculatedistance function call here
                //float results[] = new float[10];
               // Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 31.5033, 74.2781, results);
               // Toast.makeText(MapActivity.this,"Distance:" +results,Toast.LENGTH_SHORT).show();
            }

            else {
                MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                // calculatedistance function call here

                Bundle extras = getIntent().getExtras();
                //int selectedItemId = extras.getInt("Item Id");
                //int selectedItemString = extras.getInt("Item String");
                }
        }
        else if (selectedItemId == 2 )
        {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(31.5153,74.3074)).title("Driver 2").snippet("Wahdat Colony").draggable(true));
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            if (currentLocationMarker == null)
                currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                // calculatedistance function call here
            else {
                MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                // calculatedistance function call here

                //Bundle extras = getIntent().getExtras();
                //int selectedItemId = extras.getInt("Item Id");
                //int selectedItemString = extras.getInt("Item String");
            }
        }
        else {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(31.4697,74.2728)).title("Driver 3").snippet("Johar Town").draggable(true));
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            if (currentLocationMarker == null)
                currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("Student").snippet("Current Location").draggable(true));
                // calculatedistance function call here
            else {
                MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
                // calculatedistance function call here

                //Bundle extras = getIntent().getExtras();
                //int selectedItemId = extras.getInt("Item Id");
                //int selectedItemString = extras.getInt("Item String");
            }
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient = null;
        googleMap = null;
    }
}