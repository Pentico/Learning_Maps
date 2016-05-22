package com.example.alfie.learning_maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Finding the physical location of the device
 */

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(location_context);
        testProvider();

        btn = (Button) findViewById(R.id.btn_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }




    public void testProvider() {

        TextView tv = (TextView) findViewById(R.id.myTextView);
        StringBuilder stringBuilder = new StringBuilder("Enabled Provider");

        List<String> providers = locationManager.getProviders(true);

        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

            stringBuilder.append("\n").append(provider).append(": ");
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null ){
                double lat = location.getLatitude();
                double lng = location.getAltitude();
                stringBuilder.append(lat).append(", ").append(lng);
            }else{
                stringBuilder.append("No location");
            }
        }

        tv.setText(stringBuilder);
    }
}
