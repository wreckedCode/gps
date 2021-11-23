package uws.ac.uk.mylocation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * For education purposes only.
 * Developed by,
 * Scott Paterson - B00350157
 * @ UWS - paisley
 * orig dev date = 15/11/2021
 */
public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;
    private TrackGPS gps;
    double longitude;
    double latitude;
    private final static int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(mPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{mPermission,},REQUEST_CODE_PERMISSION);
                return;
            }
            else
            {
            }
        }
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new TrackGPS(MainActivity.this);
                if (gps.canGetLocation()){
                    editText = (EditText) findViewById(R.id.editText);
                    editText.setText("Latitude: "+gps.getLatitude()+"; Longitude: "+gps.getLongitude());
                }else{
                    gps.showAlert();
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        gps.stopGPS();
    }


}