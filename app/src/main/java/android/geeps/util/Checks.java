package android.geeps.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Checks {
    Activity act;

    public Checks(Activity act){
        this.act = act;
    }

    public void createDialog(String message, String positiveButton, final String action){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setMessage(message)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (action == null) {
                            act.finish();
                        } else {
                            act.startActivity(new Intent(action));
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object
        builder.create();
        builder.show();
    }

    public boolean checkGPSConnection(){
        LocationManager locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsIsEnabled){
            createDialog("GPS desativado", "Ligar", android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        }
        return gpsIsEnabled;
    }

    public boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected= activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            createDialog("Sem conexão à internet", "Ligar", android.provider.Settings.ACTION_WIFI_SETTINGS);
        }
        return isConnected;
    }
}
