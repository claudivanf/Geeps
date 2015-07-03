package android.geeps.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.geeps.util.GeepsNotification;
import android.geeps.util.SPManager;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.HashSet;
import java.util.Set;

/**
 * Service do GCM, faz com que receba mensagens do gcm em background.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("PEDIDO_NOTIFICATION");

        if(message == null){
            message = data.getString("ENTREGADOR_NOTIFICATION");
            String entregadorId = data.getString("ENTREGADOR_ID");

            // comeca a pegar a localização em tempo real
            Intent intent = new Intent(this, EntregadorService.class);
            Bundle b = new Bundle();
            b.putString("entregador_id", entregadorId);
            intent.putExtras(b);
            startService(intent);    // começa a pegar a localização do entregador

            GeepsNotification.sendNotification(this, message, 2);

        } else {
            GeepsNotification.sendNotification(this, message, 1);
            // TODO arrumar um jeito de recarregar a tela de listar pedidos
        }
    }
}
