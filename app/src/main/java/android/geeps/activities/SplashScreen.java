package android.geeps.activities;

/**
 * Created by Túlio on 18/04/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.geeps.R;
import android.geeps.util.StoredData;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private static final int SPLASHTIMEOUT = 2000;
    private StoredData sp;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        sp = new StoredData(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!sp.checkDataStored()) {
                    Intent i = new Intent(SplashScreen.this, RegistryActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, ActBarActivity.class);
                    startActivity(i);
                    finish();
                }



                //Intent i = new Intent(SplashScreen.this, ClienteEntregadorActivity.class);
//                Intent i = new Intent(SplashScreen.this, RegistryActivity.class);
//                Intent i = new Intent(SplashScreen.this, DemoActivity.class);
//                Intent i = new Intent(SplashScreen.this, ActBarActivity.class);
                //startActivity(i);
                //finish();
            }
        }, SplashScreen.SPLASHTIMEOUT);
    }

}