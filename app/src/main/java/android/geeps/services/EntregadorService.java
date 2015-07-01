package android.geeps.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.geeps.firebase.FirebaseEntregador;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Service criado para poder pegar a localização do entregador, mesmo em background.
 */
public class EntregadorService extends Service {

    private MyLocationListener mylocationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mylocationListener = new MyLocationListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mylocationListener.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyLocationListener implements LocationListener,
                    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        private static final int INTERVALO_ENTRE_REQUESTS = 2000; // 2 ssegundo
        private static final int INTERVALO_ENTRE_REQUESTS_FAST = 1000; // 1 ssegundo

        GoogleApiClient mGoogleApiClient;
        LocationRequest mLocationRequest;
        Context context;

        public MyLocationListener(Context context) {
            this.context = context;
            createLocationRequest();
            buildGoogleApiClient();
        }

        public void start() {
            mGoogleApiClient.connect();
        }

        protected void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVALO_ENTRE_REQUESTS);
            mLocationRequest.setFastestInterval(INTERVALO_ENTRE_REQUESTS_FAST);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        protected void startLocationUpdates() {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

        @Override
        public void onLocationChanged(Location location) {
            FirebaseEntregador fbEntregador = new FirebaseEntregador();
            //fbEntregador.init(this, .toArray()[0]);
            Log.d("LOCATION LAT", String.valueOf(location.getLatitude()));
            Log.d("LOCATION LNG", String.valueOf(location.getLongitude()));
        }

        @Override
        public void onConnected(Bundle bundle) {
            Log.d("CONNECTED :", "está conectado");
            startLocationUpdates();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d("CONNECT SUSPEND :", "n está conectado");
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d("CONNECT FAILED :", "n está conectado");
        }

    }

}