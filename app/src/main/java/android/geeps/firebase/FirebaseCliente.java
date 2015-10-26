package android.geeps.firebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.geeps.R;
import android.geeps.dialogs.EntregadorMissingDialog;
import android.util.Log;
import android.widget.Toast;

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
    public boolean isRastreable = true;

    private static final String FIREBASE_URL = "https://brilliant-fire-3813.firebaseio.com/";

    /**
     * Inicializador da conexão websocket utilizando o firebase
     */
    public void init(final Activity context, GoogleMap googleMap, final String room) {
        this.googleMap = googleMap;

        Firebase.setAndroidContext(context);
        Firebase myFirebaseRef = new Firebase(FIREBASE_URL);

        roomRef = myFirebaseRef.child(room);

        // add um listener para quando a posicao do entregador mudar no firebase
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("ONCHANGE", String.valueOf(snapshot.getValue()));
                if (snapshot.getValue() == null) {
                    isRastreable = false;
                    EntregadorMissingDialog dialog = new EntregadorMissingDialog();
                    dialog.show(context.getFragmentManager(), "");
                } else {
                    JSONObject jsonobj = null;
                    try {
                        jsonobj = new JSONObject(snapshot.getValue().toString());
                        // atualiza posição no mapa
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

        if (googleMap != null) {
            markerEntregador = googleMap.addMarker(new MarkerOptions().position(
                    new LatLng(lat, lng)).title("Entregador").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.icon_grayscale_small)));
        }
    }
}
