package android.geeps.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.geeps.R;
import android.geeps.core.User;
import android.geeps.util.HTTPGet;
import android.geeps.util.StoredData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RegistryActivity extends Activity {

    private Button souCliente;

    private User user;

    private StoredData sp;

    private EditText name, phone;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        sp = new StoredData(this);

        HTTPGet http = new HTTPGet();
        String a = http.requestJson();

        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();


        if (sp.checkDataStored()) {
            Intent i = new Intent(RegistryActivity.this, ActBarActivity.class);
            startActivity(i);
            finish();
        }

        this.name = (EditText) findViewById(R.id.user_name);
        this.name.setText(sp.getName());

        this.phone = (EditText) findViewById(R.id.user_phone);
        this.phone.setText(sp.getPhone());

        souCliente = (Button) findViewById(R.id.btn_registry);
        fillSpinnerCoutries();
        clienteListener();
    }

    private void clienteListener() {
        souCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isConnected()) {
                if (hasConnection(getApplicationContext())) {
                    if(validEditText()) {
                        getSharedPrefs().saveData(name.getText().toString(), phone.getText().toString(), countryCode);
                        Intent myIntent = new Intent(getApplicationContext(), ActBarActivity.class);
                        startActivity(myIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Preencha os dados corretamente", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Verifique sua conexão de internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validEditText(){
        if(name.getText().toString().isEmpty()) return false;
        if(phone.getText().toString().isEmpty()) return false;
        if(countryCode.isEmpty()) return false;
        return true;
    }

    private void fillSpinnerCoutries() {
        Resources r = getApplicationContext().getResources();
        TypedArray countrieCodes = r.obtainTypedArray(R.array.countries);

        List<String> country = new ArrayList<String>();
        List<String> code = new ArrayList<String>();

        for (int i = 0; i < countrieCodes.length(); i++) {
            int id = countrieCodes.getResourceId(i, 0);
            code.add(r.getStringArray(id)[0]);
            country.add(r.getStringArray(id)[1]);
        }

        countrieCodes.recycle();

        final List<String> fCode = code;
        final List<String> fCountry = country;

        final Spinner p = (Spinner) findViewById(R.id.countries);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);
        p.setAdapter(dataAdapter);
        p.setSelection(2);
//        countryCode = "" + 2;
        countryCode = dataAdapter.getItem(2);
        p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selectedCountry = (String) p.getSelectedItem();
                int selectedPosition = fCountry.indexOf(selectedCountry);
                countryCode = fCode.get(selectedPosition);
                // Here is your corresponding country code
                System.out.println("### countryCode: " + countryCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * Check internet connection.
     *
     * @return the connection.
     */
    public final boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Verifies Internet Connection
     *
     * @param Context
     * @return Boolean True if has connection
     */
    static boolean hasConnection(Context c) {
        boolean connectedWifi = false;
        boolean connectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networks = cm.getAllNetworkInfo();
        for (NetworkInfo ni : networks) {
            if ("WIFI".equalsIgnoreCase(ni.getTypeName()) && ni.isConnected()) {
                connectedWifi = true;
            }
            if ("MOBILE".equalsIgnoreCase(ni.getTypeName()) && ni.isConnected()) {
                connectedMobile = true;
            }
        }
        return connectedWifi || connectedMobile;
    }

    public StoredData getSharedPrefs() {
        return sp;
    }

}
