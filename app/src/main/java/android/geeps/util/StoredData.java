package android.geeps.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by Túlio on 02/05/2015.
 */
public class StoredData {
    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String Name = "nameKey";
    private static final String Phone = "phoneKey";

    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static String regId = "";
    private int appVersion;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "926181984442";

    private EditText name;
    private EditText phone;
    private Activity act;

    private SharedPreferences sharedpreferences;

    GoogleCloudMessaging gcm;
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Demo";

    public StoredData(EditText edName, EditText edPhone, Activity act){
        this.name = edName;
        this.phone = edPhone;
        this.act = act;
        sharedpreferences = act.getSharedPreferences(MyPREFERENCES, act.getApplicationContext().MODE_PRIVATE);

        if (sharedpreferences.contains(Name))
        {
            name.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Phone))
        {
            phone.setText(sharedpreferences.getString(Phone, ""));
        }
        if (sharedpreferences.contains(PROPERTY_REG_ID))
        {
            regId = sharedpreferences.getString(PROPERTY_REG_ID, "");
        }

        if (sharedpreferences.contains(PROPERTY_APP_VERSION))
        {
            appVersion = sharedpreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        } else{
            appVersion = getAppVersion(act);
        }

        if (checkPlayServices()) {
            System.out.println("##### linha 67");
            gcm = GoogleCloudMessaging.getInstance(act);

            Toast.makeText(act.getApplicationContext(), "RegId: "+regId, Toast.LENGTH_LONG).show();
            if (regId.isEmpty()) {
                System.out.println("##### linha 71");
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }


    }

    public boolean checkValues(){
        if (name.getText() != null && name.getText().length() == 0){
            return false;
        }
        if(phone.getText() != null && phone.getText().length() == 0){
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(act.getApplicationContext());
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID= " + regId;


                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.
//                    Toast.makeText(act.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    // Persist the regID - no need to register again.
                    storeData();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Toast.makeText(act.getApplicationContext(), "Error: "+msg, Toast.LENGTH_LONG).show();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }


    public void storeData() {
        String n  = name.getText().toString();
        String ph  = phone.getText().toString();

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(Phone, ph);
        editor.putString(PROPERTY_REG_ID, getRegId());
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static String getRegId() {
        return regId;
    }

    public static void setRegId(String regId) {
        StoredData.regId = regId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(act);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, act,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                act.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

}
