package android.geeps.activities;

/**
 * Created by Túlio on 18/04/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.geeps.R;
import android.geeps.util.SPManager;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private static final int SPLASH_TIMEOUT = 2000;
    private SPManager spManager;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        spManager = new SPManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!spManager.checkDataStored()) {
                    Intent i = new Intent(SplashScreen.this, RegistryActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    startBarActivity();
                }
            }
        }, SplashScreen.SPLASH_TIMEOUT);
    }

    private void startBarActivity() {
        Intent i = new Intent(SplashScreen.this, ActBarActivity.class);
        startActivity(i);
        finish();
    }
}