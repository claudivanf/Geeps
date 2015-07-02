package android.geeps.firebase;

import android.content.Context;
import android.geeps.R;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Será responsável por criar o event listener do firebase e atualizar o marcador
 * do entregador no Google Maps
 */
public class FirebaseCliente {

    private Marker markerEntregador;
    private GoogleMap googleMap;
    private Firebase roomRef;

    private static final String FIREBASE_URL = "https://brilliant-fire-3813.firebaseio.com/";

    /**
     * Inicializador da conexão websocket utilizando o firebase
     */
    private void init(Context context, GoogleMap googleMap, final String room) {
        this.googleMap = googleMap;

        Firebase.setAndroidContext(context);
        Firebase myFirebaseRef = new Firebase(FIREBASE_URL);

        roomRef = myFirebaseRef.child(room);

        // add um listener para quando um valor mudar no B do firebase
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("ONCHANGE", snapshot.getValue().toString());
                JSONObject jsonobj = null;
                try {
                    jsonobj = new JSONObject(snapshot.getValue().toString());
                } catch (JSONException e) {
                    Log.d("JSON EXCEPTION: ", e.getMessage());
                }

                // atualiza posição no mapa
                try {
                    JSONObject posicao = jsonobj.getJSONObject("ENTREGADOR");
                    double lat = (double) posicao.getDouble("latitude");
                    double lng = (double) posicao.getDouble("longitude");
                    if (markerEntregador == null) {
                        // adiciona o Marker do entregador
                        addMarcadorEntregador(lat, lng);
                    } else {
                        // atualiza o Marker do entregador
                        markerEntregador.setPosition(new LatLng(lat,lng));
                    }

                } catch (JSONException e) {
                    Log.d("JSON Ent. Pos.: ", e.getMessage());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ONCANCELED", firebaseError.getMessage());
            }
        });
    }

    public void offConnection() {
        roomRef.goOffline();
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
