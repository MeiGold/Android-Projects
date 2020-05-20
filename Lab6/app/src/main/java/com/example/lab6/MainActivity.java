package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMyCoords;
    private TextView textViewMyAddress;
    private TextView textViewMyDistance;
    private Button but;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Geocoder geocoder;
    List<Address> addressList;
    double d_latitude;
    double d_longitude;
    boolean des_set;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMyCoords = findViewById(R.id.textViewMyCoords);
        textViewMyAddress = findViewById(R.id.textViewMyAddress);
        textViewMyDistance = findViewById(R.id.textViewMyDistance);
        but = findViewById(R.id.buttonSetDest);
        geocoder = new Geocoder(this, Locale.getDefault());
        des_set=false;


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                    textViewMyCoords.setText("(" + location.getLatitude() + "," + location.getLongitude()+")");
                    textViewMyAddress.setText(addressList.get(0).getAddressLine(0));


                    if (des_set)
                    {
                        textViewMyDistance.setText(Double.toString(distanceInmBetweenEarthCoordinates(d_latitude,d_longitude,location.getLatitude(),location.getLongitude()))+"m");
                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET}, 54);
                return;
            }
            else
            {
                try {
                    locationManager.requestLocationUpdates("gps", 500, 0, locationListener);
                }
                catch (SecurityException ex)
                {
                }
            }
        }
        else
        {
            try {
                locationManager.requestLocationUpdates("gps", 500, 0, locationListener);
            }
            catch (SecurityException ex)
            {
            }
        }

        ConfigureNextButtonInfo();
    }

    public void setInfoActivity(View view) {
        Intent e = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(e);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 54:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    try {
                        locationManager.requestLocationUpdates("gps", 500, 0, locationListener);
                    }
                    catch (SecurityException ex)
                    {
                    }
                return;
        }
    }


    private void ConfigureNextButtonInfo()
    {
        Button nextButton = (Button) findViewById(R.id.buttonSetDest);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, GeocodeClass.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("lat")) {
                d_latitude=data.getExtras().getDouble("lat");
            }
            if (data.hasExtra("long")) {
                d_longitude=data.getExtras().getDouble("long");
            }
            des_set=true;
        }
    }

    private double DegToRad(double deg){
        return deg * Math.PI / 180;
    }


    private double distanceInmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;

        double dLat = DegToRad(lat2-lat1);
        double dLon = DegToRad(lon2-lon1);

        lat1 = DegToRad(lat1);
        lat2 = DegToRad(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }
}
