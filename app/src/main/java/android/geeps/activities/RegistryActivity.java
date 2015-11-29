package android.geeps.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.geeps.R;
import android.geeps.dialogs.ConnectionMissingDialog;
import android.geeps.http.HTTPCheckUser;
import android.geeps.http.HTTPPostUser;
import android.geeps.util.PhoneEditText;
import android.geeps.util.SPManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegistryActivity extends Activity {

    private Button registerButton;
    private SPManager spManager;

    private EditText name;
    private PhoneEditText phone;
    private String countryCode;
    private HTTPCheckUser checkUser;
    private ConnectionMissingDialog check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        this.check = new ConnectionMissingDialog(this);

        spManager = new SPManager(this);

        this.name = (EditText) findViewById(R.id.user_name);
        this.phone = (PhoneEditText) findViewById(R.id.user_phone);
        this.registerButton = (Button) findViewById(R.id.btn_registry);

        fillSpinnerCoutries();
        clienteListener();
    }

    private void clienteListener() {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check.hasConnection()) {
                    if (validRegisterFields()) {

                        checkUser = new HTTPCheckUser();

                        if (!checkUser.check(formatPhoneToBD(phone.getText().toString()))) { //Usuário já cadastrado; Checar dados para preencher campos do spManager.
                            JSONObject jo = checkUser.getJsonClient();
                            try {
                                if (jo.getString("regId").equals(spManager.getRegId())) { //É o mesmo usuário (de acordo com o regId). Atualizar dados no celular.
                                    spManager.saveData(name.getText().toString(), formatPhoneToBD(phone.getText().toString()), countryCode);
                                    Toast.makeText(getApplicationContext(), "Usuário atualizado", Toast.LENGTH_LONG).show();
                                    Intent myIntent = new Intent(getApplicationContext(), ActBarActivity.class);
                                    startActivity(myIntent);
                                    finish();
                                } else if (jo.getString("regId") == null) {
                                    //TODO Atualizar dados no servidor quando o usuário não tiver ainda regId;
                                } else { //Não é o mesmo usuário
                                    Toast.makeText(getApplicationContext(), "Usuário já cadastrado", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Erro interno, tente novamente", Toast.LENGTH_LONG).show();
                            }
                        } else { //Novo usuário
                            HTTPPostUser postUser = new HTTPPostUser();
                            spManager.saveData(name.getText().toString(), formatPhoneToBD(phone.getText().toString()), countryCode);

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
                }
            }
        });
    }

    private boolean validRegisterFields() {
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

    private String formatPhoneToBD(String unformatedPhone){
        String formatPhone = unformatedPhone.replaceAll("[()\\s-]+", "");
        return formatPhone;
    }
}