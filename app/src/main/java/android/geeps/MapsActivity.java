package android.geeps;

import android.app.Dialog;
import android.content.Intent;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Firebase myFirebaseRef;
    private Firebase roomRef;
    private String userType;

    private Marker markerMe;
    private Marker markerEntregador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // ver se é um cliente ou entregador que esta logado
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params!=null) {
            userType= params.getString("user_type");
        }

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
                JSONObject jsonobj = null;
                try {
                    jsonobj = new JSONObject(snapshot.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("CHILD CLIENTE", String.valueOf(users.keySet().toString()));
                try {
                    if (jsonobj.getJSONObject("room1").getJSONObject("CLIENTE") != null ) {
                        double lat = (double) jsonobj.getJSONObject("room1").
                                getJSONObject("CLIENTE").getDouble("latitude");
                        double lng = (double) jsonobj.getJSONObject("room1").
                                getJSONObject("CLIENTE").getDouble("longitude");
                        if (markerMe == null) {
                            Log.d("MARKERME", "NULL");
                            addMarcadorCliente(lat, lng);
                        } else {
                            Log.d("MARKERME", "NOT NULL");
                            markerMe.setPosition(new LatLng(lat,lng));
                        }

                    }
                    if (jsonobj.getJSONObject("room1").getJSONObject("ENTREGADOR") != null ) {
                        double lat = (double) jsonobj.getJSONObject("room1").
                                getJSONObject("ENTREGADOR").getDouble("latitude");
                        double lng = (double) jsonobj.getJSONObject("room1").
                                getJSONObject("ENTREGADOR").getDouble("longitude");
                        if (markerEntregador == null) {
                            addMarcadorEntregador(lat, lng);
                        } else {
                            markerEntregador.setPosition(new LatLng(lat,lng));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        roomRef.child(userType).setValue(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    /**
     * Adiciona os Markers relativo a mim
     */
    private void addMarcadorCliente(double lat, double lng) {

        markerMe = googleMap.addMarker(new MarkerOptions().position(
                new LatLng(lat, lng)).title("Eu").icon(
                BitmapDescriptorFactory.fromResource(R.drawable.me)));
    }

    /**
     * Adiciona os Markers relativo ao entregador
     */
    private void addMarcadorEntregador(double lat, double lng) {

        markerEntregador = googleMap.addMarker(new MarkerOptions().position(
                new LatLng(lat, lng)).title("Entregador").icon(
                BitmapDescriptorFactory.fromResource(R.drawable.delivery)));
    }
}
