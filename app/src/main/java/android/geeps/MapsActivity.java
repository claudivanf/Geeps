package android.geeps;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Firebase myFirebaseRef;
    private Firebase roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initOfGoogleMaps();
        initOfFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    /**
     * O inicializador do google maps
     */
    private void initOfGoogleMaps() {
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            // registra um listener para quando a localização mudar
            GoogleMap.OnMyLocationChangeListener locationListener = new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    Log.d("LOCATION CHANGE", "Latitude: " + location.getLatitude() +
                            " Longitude: " + location.getLongitude());
                    refreshMyPosition(location);
                    setUpMap(location);
                }
            };

            if (location != null) {
                //PLACE THE INITIAL MARKER
            }
            googleMap.setOnMyLocationChangeListener(locationListener);
            //locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
        }
    }

    /**
     * Inicializador da conexão websocket utilizando o firebase
     */
    private void initOfFirebase() {
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://brilliant-fire-3813.firebaseio.com/");

        // cria a room
        roomRef = myFirebaseRef.child("room1");

        // add um listener para quando um valor mudar no B do firebase
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // TODO pegar os valores de latitude e longitude e setar no mapa
                Log.d("ONCHANGE", snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ONCANCELED", firebaseError.getMessage());
            }
        });
    }

    /**
     * Itá atualizar a minha posicao atual no banco de dados do Firebase
     *
     * @param location
     */
    private void refreshMyPosition(Location location) {
        Map<String, LatLng> users = new HashMap<>();
        users.put("eu", new LatLng(location.getLatitude(), location.getLongitude()));
        roomRef.setValue(users);
    }

    /**
     * Adiciona os Markers relativo a mim e ao entregador
     *
     * @param location
     */
    private void setUpMap(Location location) {
        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(location.getLatitude() + 0.004, location.getLongitude() + 0.004)).title("Marker"));
    }
}
