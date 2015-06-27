package android.geeps.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Manager para gerenciar transações com o Shared Preferences.
 */
public class SPManager {
    private static final String MY_PREFERENCES = "MyPrefs" ;

    private static final String NAME = "nameKey";
    private static final String PHONE = "phoneKey";
    private static final String COUNTRY_CODE = "countryCodeKey";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_REG_ID = "registration_id";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    private String SENDER_ID = "926181984442";
    private Activity activity;
    private SharedPreferences sharedpreferences;
    private GoogleCloudMessaging gcm;

    static final String TAG = "GCM Demo";

    public SPManager(Activity activity){
        this.activity = activity;
        sharedpreferences = activity.getSharedPreferences(MY_PREFERENCES, activity.getApplicationContext().MODE_PRIVATE);

        if (!sharedpreferences.contains(PROPERTY_APP_VERSION)) {
            storeAnyDataSP(PROPERTY_APP_VERSION, getAppVersionFromContext(activity));
        }

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(activity);

            if (getRegId().isEmpty()) {
                // recebe o regId do google cloud e salva no shared preferences
                getRegIdFromGoogleCloud();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void getRegIdFromGoogleCloud() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(activity.getApplicationContext());
                    }
                    String regId = gcm.register(SENDER_ID);
                    storeAnyDataSP(PROPERTY_REG_ID, regId);
                    msg = "Device registered, registration ID= " + regId;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    //sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.
//                    Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    // Persist the regID - no need to register again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Toast.makeText(activity.getApplicationContext(), "Error: "+msg, Toast.LENGTH_LONG).show();
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

    public boolean checkDataStored(){
        return !getName().isEmpty() && !getPhone().isEmpty() && !getCountryCode().isEmpty();
    }

    public void storeAnyDataSP(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void storeAnyDataSP(String key, int value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private int getAppVersionFromContext(Context context) {
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public String getRegId() {
        if (!sharedpreferences.contains(PROPERTY_REG_ID))
            return "";
        return sharedpreferences.getString(PROPERTY_REG_ID, "");
    }

    public String getName() {
        if (!sharedpreferences.contains(NAME))
            return "";
        return sharedpreferences.getString(NAME, "");
    }

    public String getPhone() {
        if (!sharedpreferences.contains(PHONE))
            return "";
        return sharedpreferences.getString(PHONE, "");
    }

    public String getCountryCode() {
        if (!sharedpreferences.contains(COUNTRY_CODE))
            return "";
        return sharedpreferences.getString(COUNTRY_CODE, "");
    }

    public int getAppVersion() {
        return sharedpreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
    }

    public void saveData(String name, String phone, String countryCode){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(NAME, name);
        editor.putString(PHONE, phone);
        editor.putString(COUNTRY_CODE, countryCode);
        editor.putString(PROPERTY_REG_ID, getRegId());
        editor.putInt(PROPERTY_APP_VERSION, getAppVersion());
        editor.commit();
    }
}