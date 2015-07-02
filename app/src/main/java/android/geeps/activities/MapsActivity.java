package android.geeps.activities;

import android.app.Dialog;
import android.geeps.R;
import android.geeps.util.GPSTracker;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

    private GPSTracker gps;

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

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            gps = new GPSTracker(this);
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng coordinate = new LatLng(latitude, longitude);

            MarkerOptions mo = new MarkerOptions();
            mo.position(coordinate);
            mo.title(gps.getAdress());
            mo.anchor(0.5f, 0.5f);

            //TODO Colocar ícone que preste no mapa!
            mo.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_grayscale));
            googleMap.addMarker(mo).setVisible(true);

            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(coordinate,
                    13);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom, 2000, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
