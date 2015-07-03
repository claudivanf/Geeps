package android.geeps.activities;

import android.app.Dialog;
import android.geeps.R;
import android.geeps.firebase.FirebaseCliente;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private FirebaseCliente fbCliente;
    private GoogleMap googleMap;
    private CameraUpdate center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * O inicializador do google maps
     */
    private void initOfGoogleMaps() {

        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // focus na minha localização
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (center == null && fbCliente.isRastreable) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    center = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    googleMap.animateCamera(center, 1000, null);
                }
            }
        });
    }

    public void initFirebase() {
        Bundle bundle = getIntent().getExtras();
        String entregador_id = bundle.getString("entregador_id");
        // comeca a capturar a localização e setar o marcador do entregador no mapa
        if (entregador_id != null) {
            fbCliente = new FirebaseCliente();
            fbCliente.init(this, googleMap, entregador_id);
        }
    }

    @Override
    public void onBackPressed() {
        fbCliente.offConnection();
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initFirebase();
        initOfGoogleMaps();
    }
}
