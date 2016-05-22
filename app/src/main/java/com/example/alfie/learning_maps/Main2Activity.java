package com.example.alfie.learning_maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * find the device current location
 */
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);

        //The Criteria Will let Android choose the best provider available on the device
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);

    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }
    };

    private void updateWithNewLocation(Location location){

        String latLongString;
        TextView myLocationText;
        myLocationText = (TextView)findViewById(R.id.myLocationText);

        String addressString = "No address found";
        if(location != null){

            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "lat:" + lat + "\nLong:" + lng;

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try{
                List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                StringBuilder stringBuilder = new StringBuilder();

                if (addressList.size() > 0){

                    Address address = addressList.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        //appends all the details from the addressLine
                        stringBuilder.append(address.getAddressLine(i)).append("\n");
                    }

                        stringBuilder.append(address.getLocality()).append("\n");
                        stringBuilder.append(address.getPostalCode()).append("\n");
                        stringBuilder.append(address.getCountryName());

                }
                addressString = stringBuilder.toString();
            }catch (IOException e){

            }
        }else{
            latLongString = "No location found";
        }

        myLocationText.setText("Your Current Position is:\n" + latLongString + "\n" + addressString);
    }
}
