package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    // View refs
    protected EditText txtInputCityName;
    protected Button btnGetWeatherAtMyLocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Init refs
        initRefs();
    }

    protected void initRefs() {
        txtInputCityName = findViewById(R.id.txtInputCityName);
        btnGetWeatherAtMyLocation = findViewById(R.id.btnGetWeatherAtMyLocation);
    }

    protected void openWeatherDetailsByCityName(String cityName) {
        if (cityName.length() > 0) {
            Intent intent = new Intent(this, WeatherDetails.class);
            intent.putExtra("CITY_NAME", cityName);
            intent.putExtra("TYPE", "FETCH_BY_CITY_NAME");
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.message_empty_city_name), Toast.LENGTH_LONG).show();
        }
    }

    protected void openWeatherDetailsByLongLat(double longitude, double latitude) {
        if (longitude != 0.0 && latitude != 0.0) {
            Intent intent = new Intent(this, WeatherDetails.class);
            intent.putExtra("LONGITUDE", longitude);
            intent.putExtra("LATITUDE", latitude);
            intent.putExtra("TYPE", "FETCH_BY_LONG_LAT");
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.message_invalid_location), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickBtnOpenWeatherAtMyLocation(View view) {
        // Todo: Start listening to user's location through Location Manager, will need:
        // Todo: 1. location permission specified in android manifest file
        // Todo: 2. ask user's permission run-time before accessing GPS (location is a DANGEROUS PERMISSION)

        // Todo: Update the latitude and longtitude in the UI


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Todo: we dont have permission, so ask it here
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    0
            );
            return;
        }
        btnGetWeatherAtMyLocation.setText(getString(R.string.getting_your_location));
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (!(longitude != 0.0 && latitude != 0.0)) return;

        locationManager.removeUpdates(this);
        btnGetWeatherAtMyLocation.setText(getString(R.string.get_at_my_location));

        openWeatherDetailsByLongLat(longitude, latitude);
    }


    public void onClickBtnSearchWeatherByCityName(View view) {
        String inputCityName = txtInputCityName.getText().toString();
        openWeatherDetailsByCityName(inputCityName);
    }

    public void onClickCitySuggestion1(View view) {
        openWeatherDetailsByCityName(getString(R.string.city_suggestion_1));
    }

    public void onClickCitySuggestion2(View view) {
        openWeatherDetailsByCityName(getString(R.string.city_suggestion_2));
    }

    public void onClickCitySuggestion3(View view) {
        openWeatherDetailsByCityName(getString(R.string.city_suggestion_3));
    }
}