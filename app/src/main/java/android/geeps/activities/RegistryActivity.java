package android.geeps.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.geeps.R;
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

    public static final String PREFS_NAME = "MyPrefsFile";


    private Button souCliente;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";


    EditText name;
    EditText phone;
//    EditText countryCode;



    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = (EditText) findViewById(R.id.user_name);
        phone = (EditText) findViewById(R.id.user_phone);
//        countryCode = (EditText) findViewById(R.id.editTextStreet);


        souCliente = (Button) findViewById(R.id.btn_registry);
        fillSpinnerCoutries();
        clienteListener();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);

        if (sharedpreferences.contains(Name))
        {
            name.setText(sharedpreferences.getString(Name, ""));

        }
        if (sharedpreferences.contains(Phone))
        {
            phone.setText(sharedpreferences.getString(Phone, ""));

        }

    }

    private void clienteListener() {
        souCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n  = name.getText().toString();
                String ph  = phone.getText().toString();

                Editor editor = sharedpreferences.edit();
                editor.putString(Name, n);
                editor.putString(Phone, ph);
                editor.commit();

                Intent myIntent = new Intent(getApplicationContext(), PedidosClienteActivity.class);
                startActivity(myIntent);
            }
        });
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
        p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selectedCountry = (String) p.getSelectedItem();
                int selectedPosition = fCountry.indexOf(selectedCountry);
                String correspondingCode = fCode.get(selectedPosition);
                // Here is your corresponding country code
                System.out.println(correspondingCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
}
