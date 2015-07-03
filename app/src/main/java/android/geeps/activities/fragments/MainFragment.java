package android.geeps.activities.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.geeps.R;
import android.geeps.activities.MapsActivity;
import android.geeps.adapters.UserOrderAdapter;
import android.geeps.http.HTTPPedidos;
import android.geeps.models.UserOrder;
import android.geeps.util.SPManager;
import android.location.LocationManager;
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
    private LocationManager mLocationManager;

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
    }

    public boolean checkGPSIsEnabled(){
        if(this.mLocationManager == null){
            this.mLocationManager = (LocationManager) getActivity().getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder
                .setMessage("GPS está desligado. Ligue para continuar.")
                .setCancelable(false)
                .setPositiveButton("Liberar GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                getActivity().startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        UserOrder item = mItems.get(position);

        // TODO chamar o map activity and conectar com o FirebaseCliente e criar a sala a partir do id do pedido

        if (checkGPSIsEnabled()){
            Intent i = new Intent(getActivity(), MapsActivity.class);
            startActivity(i);
        } else {
            showSettingsAlert();
        }

    }

}