package android.geeps.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.geeps.util.GeepsNotification;
import android.geeps.util.SPManager;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Set;

/**
 * Service do GCM, faz com que receba mensagens do gcm em background.
 */
public class MyGcmListenerService extends GcmListenerService {

    static SharedPreferences sharedpreferences;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("PEDIDO_NOTIFICATION");
        sharedpreferences = getApplication().getSharedPreferences(SPManager.MY_PREFERENCES, this.getApplicationContext().MODE_PRIVATE);

        if(message == null){
            // comeca a pegar a localização em tempo real
            Intent intent = new Intent(this, EntregadorService.class);
            startService(intent);
            message = data.getString("ENTREGADOR_NOTIFICATION");
            String pedidoId = data.getString("PEDIDO_ID");
            storePedidoSP(pedidoId);
        } else {
            // TODO arrumar um jeito de recarregar a tela de listar pedidos
        }

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        GeepsNotification.sendNotification(this, message);
    }


    public void storePedidoSP(String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> pedidos = getPedidos();
        pedidos.add(value);
        editor.putStringSet(SPManager.PEDIDOS, pedidos);
        editor.commit();
    }


    public static Set<String> getPedidos() {
        if (!sharedpreferences.contains(SPManager.PEDIDOS))
            return null;
        return sharedpreferences.getStringSet(SPManager.PEDIDOS, null);
    }
}
