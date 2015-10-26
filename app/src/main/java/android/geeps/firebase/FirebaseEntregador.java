package android.geeps.firebase;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

/**
 * Será responsável por enviar ao firebase a atual localização do entregador,
 * ou seja, será utilizado pelo service de entregador {@link android.geeps.services.EntregadorService}
 */
public class FirebaseEntregador {

    private Firebase roomRef;

    private static final String FIREBASE_URL = "https://brilliant-fire-3813.firebaseio.com/";

    /**
     * Inicializador da conexão websocket utilizando o firebase
     */
    public void init(Context context, final String room) {
        Firebase.setAndroidContext(context);
        Firebase myFirebaseRef = new Firebase(FIREBASE_URL);

        // cria a room
        roomRef = myFirebaseRef.child(room);
    }

    /**
     * Itá atualizar a minha posicao atual no banco de dados do Firebase
     *
     * @param location
     */
    public void refreshMyPosition(Location location) {
        roomRef.child("ENTREGADOR").setValue(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void offConnection() {
        roomRef.goOffline();
    }
}
