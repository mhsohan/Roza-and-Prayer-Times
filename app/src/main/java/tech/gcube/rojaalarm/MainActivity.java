package tech.gcube.rojaalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public double longitude, latitude;
    LocationManager locationManager;
    LocationListener locationListener;



    private TextView txtPrayerTimes;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //txtPrayerTimes = (TextView) findViewById(R.id.txtPrayerTimes);

        final Button buttonFazr = findViewById(R.id.buttonFazr);
        final Button buttonSunRise = findViewById(R.id.buttonSunrise);
        final Button buttonZohr = findViewById(R.id.buttonZohr);
        final Button buttonAsor = findViewById(R.id.buttonAsor);
        final Button buttonSunSet = findViewById(R.id.buttonSunset);
        final Button buttonMagrib = findViewById(R.id.buttonMagrib);
        final Button buttonEsha = findViewById(R.id.buttonEsha);

        //LocationManager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //LocationListener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Log.i("onLocation Longitude: ",Double.toString(longitude));
                Log.i("onLocation Latitude: ",Double.toString(latitude));

               // Intent intent = getIntent();
               // finish();
               // startActivity(intent);
               // myUpdateOperation();
                //txtPrayerTimes.setText(null);



                double timezone = (Calendar.getInstance().getTimeZone()
                        .getOffset(Calendar.getInstance().getTimeInMillis()))
                        / (1000 * 60 * 60);
                PrayTime prayers = new PrayTime();

                prayers.setTimeFormat(prayers.Time12);
                prayers.setCalcMethod(prayers.Makkah);
                prayers.setAsrJuristic(prayers.Shafii);
                prayers.setAdjustHighLats(prayers.AngleBased);
                int[] offsets = { 0, 0, 0, 0, 0, 0, 0 }; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
                prayers.tune(offsets);

                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);

                ArrayList prayerTimes = prayers.getPrayerTimes(cal, latitude,
                        longitude, timezone);
                ArrayList prayerNames = prayers.getTimeNames();

               /* for (int i = 0; i < prayerTimes.size(); i++) {
                    txtPrayerTimes.append("\n" + prayerNames.get(i) + " - "
                            + prayerTimes.get(i));
                }*/


                buttonFazr.setText(prayerNames.get(0)+"              "+prayerTimes.get(0));
                buttonSunRise.setText(prayerNames.get(1)+"        "+prayerTimes.get(1));
                buttonZohr.setText(prayerNames.get(2)+"           "+prayerTimes.get(2));
                buttonAsor.setText(prayerNames.get(3)+"                 "+prayerTimes.get(3));
                buttonSunSet.setText(prayerNames.get(4)+"          "+prayerTimes.get(4));
                buttonMagrib.setText(prayerNames.get(5)+"        "+prayerTimes.get(5));
                buttonEsha.setText(prayerNames.get(6)+"                 "+prayerTimes.get(6));


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        //if device is running SDK<23
        if (Build.VERSION.SDK_INT < 23) {
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
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

        //THE DATA COMES FROM HERE ON CREATE AND AFTER GETTING PERMISSIONS

        // Retrive lat, lng using location API
        //double latitude = 23.810332;
        //double longitude = 90.412518;
        Log.i("Latitude: ",Double.toString(latitude));
        Log.i("Longitude: ",Double.toString(longitude));


    }
}
