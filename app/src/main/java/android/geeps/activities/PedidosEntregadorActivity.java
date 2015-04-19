package android.geeps.activities;

import android.app.Activity;
import android.content.Intent;
import android.geeps.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class PedidosEntregadorActivity extends Activity {

    ListView pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_entregador);

        pedidos = (ListView) findViewById(R.id.pedidos_entregador);
        createAdapter();
        listaPedidosListener();

    }

    private void createAdapter() {
        String[] arrayPedidos = {"Pedido 1  \n" +
                "R: João Batista 457, B: Liberdade"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,  arrayPedidos);
        pedidos.setAdapter(adapter);
    }

    private void listaPedidosListener() {
        pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getApplicationContext(), MapsActivity.class);
                Bundle params = new Bundle();
                params.putString("user_type", "ENTREGADOR");
                myIntent.putExtras(params);
                startActivity(myIntent);
            }
        });
    }
}
