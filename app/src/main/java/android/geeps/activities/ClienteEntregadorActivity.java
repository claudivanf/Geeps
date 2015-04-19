package android.geeps.activities;

import android.app.Activity;
import android.content.Intent;
import android.geeps.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ClienteEntregadorActivity extends Activity {

    Button souCliente;
    Button souEntregador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_entregador);

        souCliente = (Button) findViewById(R.id.btn_cliente);
        souEntregador = (Button) findViewById(R.id.btn_entregador);

        clienteListener();
        entregadorListener();
    }

    private void clienteListener() {
        souCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), PedidosClienteActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void entregadorListener() {
        souEntregador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), PedidosEntregadorActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
