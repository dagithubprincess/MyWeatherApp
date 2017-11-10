package bryonyair.example.com.myweatherapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private TextView dateo, min_tempo, max_tempo, place;
    private ImageView image;
    private String className;
    public static int count = 0;
    private LocationManager locationManager;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    DownloadTask asynctask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        className = this.getClass().getName();
        Log.d(className, "onCreate");

        dateo = (TextView) findViewById(R.id.my_date);
        image = (ImageView) findViewById(R.id.my_icon);
        min_tempo = (TextView) findViewById(R.id.my_min_temp);
        max_tempo = (TextView) findViewById(R.id.my_max_temp);
        place = (TextView) findViewById(R.id.my_place);

        asynctask = new DownloadTask(MainActivity.this, dateo, min_tempo, max_tempo, place, image);

        if (checkPermission()) {

        } else {
            requestPermission();
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 100, locationlistener);
    }

    LocationListener locationlistener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(className, "onLocationChanged");

            double lat = location.getLatitude();
            double lon = location.getLongitude();

            if (count == 0) {
                asynctask.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lon) + "&appid=9343def261d6a8ca89d4e40e75cbc9f4&units=metric");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(className, "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(className, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(className, "onProviderDisabled");
        }
    };

    private boolean checkPermission() {
        Log.d(className, "checkPermission");
        //TODO:Check that this permission does get triggerd when false and true
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermission();
            return false;
        }
    }

    private void requestPermission() {
        Log.d(className, "requestPermission");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "GPS Permission needed in order to locate your position", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(className, "onRequestPermissionResult");
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(className, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(className, "onRestart");
        count=0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(className, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(className, "onResume");
        count=0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(className, "onStart");
        count=0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(className, "onStop");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(className, "onConnectionFailed");
        Toast.makeText(this, "Unable to find weather data, check your internet connection", Toast.LENGTH_LONG).show();
    }
}
