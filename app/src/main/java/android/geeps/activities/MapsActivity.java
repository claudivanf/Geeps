package android.geeps.activities;

import android.app.Dialog;
import android.geeps.R;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initOfGoogleMaps();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * O inicializador do google maps
     */
    private void initOfGoogleMaps() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
        }
    }
}
