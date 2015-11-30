package android.geeps.activities;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.geeps.R;
import android.geeps.adapters.UserOrderAdapter;
import android.geeps.dialogs.ConnectionMissingDialog;
import android.geeps.dialogs.GPSMissingDialog;
import android.geeps.http.HTTPPedidos;
import android.geeps.http.HTTPPedidosEntregador;
import android.geeps.models.UserOrder;
import android.geeps.util.SPManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ActBarActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Settings Clicked",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(),voice_text,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Vai ser chamado depois do oncreate, ou quando estiver em foreground.
     */
    @Override
    protected void onResume () {
        ConnectionMissingDialog checks = new ConnectionMissingDialog(this);
        super.onResume();
        if(checks.checkInternet()) {
//            Fragment main = new PedidosListFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.main_content, main);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        ConnectionMissingDialog checks = new ConnectionMissingDialog(this);
        checks.createDialog("Deseja sair do Geeps?", "Sair", null);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CLIENTE";
                case 1:
                    return "ENTREGADOR";

            }
            return null;
        }
    }

    public static class PlaceholderFragment extends android.support.v4.app.Fragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static List<UserOrder> mItems;
        private static List<UserOrder> mItemsEntregador;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_action_bar, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.itens_list);

            final int ABACLIENTE = 1;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == ABACLIENTE){
                SPManager spManager = new SPManager(getActivity());
                // initialize the items list
                mItems = new ArrayList<UserOrder>();
                Resources resources = getResources();

                HTTPPedidos httpPedidos = new HTTPPedidos();
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
                listView.setAdapter(new UserOrderAdapter(getActivity(), mItems));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ConnectionMissingDialog connection = new ConnectionMissingDialog(getActivity());
                        GPSMissingDialog gps = new GPSMissingDialog(getActivity());
                        if(connection.checkInternet() && gps.checkGPSConnection()) {
                            if(mItems.get(position).title.equals("EM ANDAMENTO")){
                                String entregador_id = mItems.get(position).entregador_id;
                                Bundle bundle = new Bundle();
                                bundle.putString("entregador_id", entregador_id);
                                Intent intent = new Intent(getActivity(), MapsActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Pedido já concluído ou não saiu para entrega", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }else{
                SPManager spManager = new SPManager(getActivity());
                // initialize the items list
                mItemsEntregador = new ArrayList<UserOrder>();
                Resources resources = getResources();

                HTTPPedidosEntregador httpPedidos = new HTTPPedidosEntregador();
                JSONArray arrayPedidos = httpPedidos.getPedidos(spManager.getPhone());
                for (int i = 0; i < arrayPedidos.length(); i++) {
                    try {
                        mItemsEntregador.add(new UserOrder(resources.getDrawable(R.drawable.icon),
                                arrayPedidos.getJSONObject(i).getString("status")
                                , arrayPedidos.getJSONObject(i).getString("empresa_nome"),
                                arrayPedidos.getJSONObject(i).getString("entregador_id")));
                    } catch (JSONException e) {
                        Log.d("JSON PARSER ERROR", e.getMessage());
                    }
                }

                // initialize and set the list adapter
                listView.setAdapter(new UserOrderAdapter(getActivity(), mItemsEntregador));
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        ConnectionMissingDialog connection = new ConnectionMissingDialog(getActivity());
//                        GPSMissingDialog gps = new GPSMissingDialog(getActivity());
//                        if(connection.checkInternet() && gps.checkGPSConnection()) {
//                            if(mItems.get(position).title.equals("EM ANDAMENTO")){
//                                String entregador_id = mItems.get(position).entregador_id;
//                                Bundle bundle = new Bundle();
//                                bundle.putString("entregador_id", entregador_id);
//                                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(getActivity().getApplicationContext(), "Pedido já concluído ou não saiu para entrega", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                });
            }

            return rootView;
        }
    }
}
