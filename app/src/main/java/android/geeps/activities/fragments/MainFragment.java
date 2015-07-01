package android.geeps.activities.fragments;

import android.app.ListFragment;
import android.content.res.Resources;
import android.geeps.R;
import android.geeps.adapters.UserOrderAdapter;
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

public class MainFragment extends ListFragment {

    private List<UserOrder> mItems;        // ListView items list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SPManager spManager = new SPManager(getActivity());
        // initialize the items list
        mItems = new ArrayList<UserOrder>();
        Resources resources = getResources();

        HTTPPedidos httpPedidos = new HTTPPedidos();
        JSONArray arrayPedidos = httpPedidos.getPedidos(spManager.getPhone());

        for (int i = 0; i < arrayPedidos.length(); i++) {
            try {
                Log.d("JSON CONTENT", arrayPedidos.get(i).toString());
                mItems.add(new UserOrder(resources.getDrawable(R.drawable.icon),
                        arrayPedidos.getJSONObject(i).getString("status")
                        , arrayPedidos.getJSONObject(i).getString("empresa_nome")));
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
        // remove the dividers from the ListView of the ListFragment
        //getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        UserOrder item = mItems.get(position);
        // TODO chamar o map activity and conectar com o FirebaseCliente e criar a sala a partir do id do pedido
    }

}