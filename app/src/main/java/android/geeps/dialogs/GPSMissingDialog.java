package android.geeps.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Claudivan on 03/07/2015.
 */
public class GPSMissingDialog extends DialogFragment {

    private Activity act;

    public GPSMissingDialog(Activity act){
        this.act = act;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder
                .setMessage("GPS está desligado. Ative-o para continuar.")
                .setCancelable(false)
                .setPositiveButton("Ativar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                getActivity().startActivity(callGPSSettingIntent);
                            }
                        })
                .setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return alertDialogBuilder.create();
    }

    public boolean checkGPSConnection(){
        LocationManager locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsIsEnabled){
            createDialog("GPS desativado", "Ligar", android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        }
        return gpsIsEnabled;
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
}
