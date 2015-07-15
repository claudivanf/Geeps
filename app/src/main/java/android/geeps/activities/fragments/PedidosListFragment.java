package android.geeps.activities.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.geeps.R;
import android.geeps.activities.MapsActivity;
import android.geeps.adapters.UserOrderAdapter;
import android.geeps.dialogs.ConnectionMissingDialog;
import android.geeps.dialogs.GPSMissingDialog;
import android.geeps.http.HTTPPedidos;
import android.geeps.models.UserOrder;
import android.geeps.util.SPManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PedidosListFragment extends ListFragment {

    private List<UserOrder> mItems;        // ListView items list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SPManager spManager = new SPManager(getActivity());
        // initialize the items list
        mItems = new ArrayList<UserOrder>();
        Resources resources = getResources();

        HTTPPedidos httpPedidos = new HTTPPedidos();
        System.out.println("######### "+httpPedidos);
        JSONArray arrayPedidos = httpPedidos.getPedidos(spManager.getPhone());
        for (int i = 0; i < arrayPedidos.length(); i++) {
            try {
                mItems.add(new UserOrder(resources.getDrawable(R.drawable.icon),
                        arrayPedidos.getJSONObject(i).getString("status")
                        , arrayPedidos.getJSONObject(i).getString("empresa_nome"),
                        arrayPedidos.getJSONObject(i).getString("entregador_id")));
            } catch (JSONException e) {
                Log.d("JSON PARSER ERROR", e.getMessage());
            }
        }

        // initialize and set the list adapter
        setListAdapter(new UserOrderAdapter(getActivity(), mItems));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ConnectionMissingDialog connection = new ConnectionMissingDialog(getActivity());
        GPSMissingDialog gps = new GPSMissingDialog(getActivity());
        if(connection.checkInternet() && gps.checkGPSConnection()) {
            String entregador_id = mItems.get(position).entregador_id;
            Bundle bundle = new Bundle();
            bundle.putString("entregador_id", entregador_id);
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}