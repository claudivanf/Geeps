package android.geeps.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionMissingDialog {

    private Activity act;

    public ConnectionMissingDialog(Activity act){
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

    public boolean checkInternet(){
        ConnectivityManager cm = (ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected= activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            createDialog("Sem conexão à internet", "Ligar", android.provider.Settings.ACTION_WIFI_SETTINGS);
        }
        return isConnected;
    }

    /**
     * Verifies Internet Connection
     *
     * @return Boolean True if has connection
     */
    public boolean hasConnection() {
        boolean connectedWifi = false;
        boolean connectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) act.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networks = cm.getAllNetworkInfo();
        for (NetworkInfo ni : networks) {
            if ("WIFI".equalsIgnoreCase(ni.getTypeName()) && ni.isConnected()) {
                connectedWifi = true;
            }
            if ("MOBILE".equalsIgnoreCase(ni.getTypeName()) && ni.isConnected()) {
                connectedMobile = true;
            }
        }
        if(!connectedWifi && !connectedMobile){
            createDialog("Sem conexão à internet", "Ligar", android.provider.Settings.ACTION_WIFI_SETTINGS);
        }
        return connectedWifi || connectedMobile;
    }
}
