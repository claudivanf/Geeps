package android.geeps.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.geeps.R;
import android.geeps.util.HTTPCheckUser;
import android.geeps.util.HTTPPostUser;
import android.geeps.util.SPManager;
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

    private Button registerButton;
    private SPManager spManager;

    private EditText name, phone;
    private String countryCode;
    private HTTPCheckUser checkUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        spManager = new SPManager(this);

        this.name = (EditText) findViewById(R.id.user_name);
        this.phone = (EditText) findViewById(R.id.user_phone);
        this.registerButton = (Button) findViewById(R.id.btn_registry);

        fillSpinnerCoutries();
        clienteListener();
    }

    private void clienteListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConnection(getApplicationContext())) {
                    if (validRegisterFields()) {

                        checkUser = new HTTPCheckUser();

                        if (!checkUser.check(phone.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Usuário já cadastrado", Toast.LENGTH_LONG).show();
                        }else{
                            HTTPPostUser postUser = new HTTPPostUser();
                            spManager.saveData(name.getText().toString(), phone.getText().toString(), countryCode);

                            String response = postUser.registryUser(
                                    spManager.getName(),
                                    spManager.getPhone(),
                                    spManager.getCountryCode(),
                                    spManager.getRegId());

                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(getApplicationContext(), ActBarActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Preencha os dados corretamente", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Verifique sua conexão de internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validRegisterFields(){
        return !name.getText().toString().isEmpty() &&
                !phone.getText().toString().isEmpty() &&
                !countryCode.isEmpty();
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
     * Verifies Internet Connection
     *
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
}