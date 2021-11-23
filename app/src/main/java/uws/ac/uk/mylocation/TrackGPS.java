package uws.ac.uk.mylocation;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * For education purposes only.
 * Developed by,
 * Scott Paterson - B00350157
 * @ UWS - paisley
 * orig dev date = 15/11/2021
 */

public class TrackGPS extends Service implements LocationListener {

    private final Context ctxt; //ref current activity
    boolean checkGPS = false; //GPS availability status
    boolean checkNetwork = false; //check if WiFi location available
    boolean canGetLocation = false; //true if GPS or WiFi are available

    Location loc; //current location
    protected LocationManager locationManager;
    double latitude;
    double longitude;

    private static final long MINDISTANCE = 10; //min dis required to update loc
    private static final long MINDELAY = 1000 * 60; //min time between updates

    public TrackGPS (Context ctxt){
        this.ctxt = ctxt;
        getLocation();
    }

    private Location getLocation() {
        try{
            locationManager = (LocationManager) ctxt.getSystemService(LOCATION_SERVICE);
            //get gps status
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //get network status
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!checkGPS && !checkNetwork) {
                Toast.makeText(ctxt, "No Service provider available", Toast.LENGTH_SHORT).show();
            } else {
                canGetLocation = true;
                //get provider, prefer to use netwirk if both are available
                String provider = checkNetwork ? LocationManager.NETWORK_PROVIDER : LocationManager.GPS_PROVIDER;
                try{
                    locationManager.requestLocationUpdates(provider, MINDELAY, MINDISTANCE, this);
                    if (locationManager !=null){
                        loc = locationManager.getLastKnownLocation(provider);
                        if(loc != null){
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                }catch (SecurityException e) {
                    Toast.makeText(ctxt, "No permission to access provider", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return loc;
    }

    public void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctxt);
        dialog.setTitle("GPS Disabled");
        dialog.setMessage("Do you want to turn on GPS?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //what to do if user clicks yes
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctxt.startActivity(intent);
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public void stopGPS(){
        if (locationManager != null){
            try{
                locationManager.removeUpdates(TrackGPS.this);
            } catch(SecurityException e){
                Toast.makeText(ctxt, "No permission to access GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public double getLatitude(){
        if (loc != null) return loc.getLatitude();
        return latitude;
    }

    public double getLongitude(){
        if (loc != null) return loc.getLongitude();
        return longitude;
    }

    public boolean canGetLocation() { return this.canGetLocation;}


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onProviderDisabled( String provider) {

    }

    @Override
    public void onProviderEnabled( String provider) {

    }
}
