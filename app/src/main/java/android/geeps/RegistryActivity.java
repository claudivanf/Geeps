package android.geeps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class RegistryActivity extends Activity {

    private Button souCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        souCliente = (Button) findViewById(R.id.btn_registry);

        clienteListener();
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

}
