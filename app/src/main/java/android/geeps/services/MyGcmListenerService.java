package android.geeps.services;

import android.content.Intent;
import android.geeps.util.GeepsNotification;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Service do GCM, faz com que receba mensagens do gcm em background.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("ENTREGADOR_NOTIFICATION");
        System.out.println("##################################### ");

        if(message == null){ //CLIENTE
            message = data.getString("PEDIDO_NOTIFICATION");
            String entregadorId = data.getString("ENTREGADOR_ID");
            Log.d("MSG", entregadorId + "");

            // comeca a pegar a localização em tempo real
            Intent intent = new Intent(this, EntregadorService.class);
            Bundle b = new Bundle();
            b.putString("entregador_id", entregadorId);
            intent.putExtras(b);
            startService(intent);    // começa a pegar a localização do entregador

            GeepsNotification.sendNotification(this, message, 2);

        } else { //ENTREGADOR
            String entregadorId = data.getString("ENTREGADOR_ID");
            GeepsNotification.sendNotification(this, message, 2);
            // TODO arrumar um jeito de recarregar a tela de listar pedidos
        }
    }
}
