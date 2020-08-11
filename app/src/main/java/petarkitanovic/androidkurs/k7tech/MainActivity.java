package petarkitanovic.androidkurs.k7tech;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import petarkitanovic.androidkurs.k7tech.adapter.WeatherAdapter;
import petarkitanovic.androidkurs.k7tech.models.Hourly;
import petarkitanovic.androidkurs.k7tech.models.TodaysWeather;
import petarkitanovic.androidkurs.k7tech.service2.MyApiEndpointInterface;
import petarkitanovic.androidkurs.k7tech.service2.MyService;
import petarkitanovic.androidkurs.k7tech.service2.MyServiceContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION = 1;

    private RelativeLayout relativeLayout;


    private ConstraintLayout weather_layout;
    private ProgressBar loading;


    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    WeatherAdapter weatherAdapter;

    Double locationLat;
    Double locationLon;

    List<Hourly> hourlyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.root_view);
        weather_layout = findViewById(R.id.weather_layout);
        recyclerView = findViewById(R.id.recyclerView);

        loading = findViewById(R.id.loading);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            locationLat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            locationLon = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            getWeatherData();
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void getWeatherData() {
        MyService.apiInterface().getWeatherData(String.valueOf(locationLat),
                String.valueOf(locationLon),
                "metric",
                "minutely,daily",
                MyServiceContract.APIKEY)
                .enqueue(new Callback<TodaysWeather>() {
                    @Override
                    public void onResponse(@NonNull Call<TodaysWeather> call, @NonNull Response<TodaysWeather> response) {
                        if (response.code() == 200) {
                            try {

                                if (response.body() != null) {
                                    hourlyList = response.body().getHourly();
                                    Log.d("Lista", hourlyList.size() + " ");
                                    linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);

                                    weatherAdapter = new WeatherAdapter(MainActivity.this, hourlyList);

                                    recyclerView.setAdapter(weatherAdapter);

                                    weather_layout.setVisibility(View.VISIBLE);
                                    loading.setVisibility(View.GONE);

                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TodaysWeather> call, @NonNull Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

}