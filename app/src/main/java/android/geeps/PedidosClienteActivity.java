package android.geeps;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class PedidosClienteActivity extends Activity {

    ListView pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);

        pedidos = (ListView) findViewById(R.id.pedidos_cliente);
        createAdapter();
        listaPedidosListener();
    }

    private void createAdapter() {
        String[] arrayPedidos = {"Pedido 1 - \n        Entregador Roberto Carlos"};

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
                params.putString("user_type", "CLIENTE");
                myIntent.putExtras(params);
                startActivity(myIntent);
            }
        });
    }

}
