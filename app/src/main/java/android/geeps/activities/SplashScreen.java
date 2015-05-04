package android.geeps.activities;

/**
 * Created by Túlio on 18/04/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.geeps.R;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private static final int SPLASHTIMEOUT = 3000;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent i = new Intent(SplashScreen.this, ClienteEntregadorActivity.class);
                Intent i = new Intent(SplashScreen.this, RegistryActivity.class);
//                Intent i = new Intent(SplashScreen.this, DemoActivity.class);
//                Intent i = new Intent(SplashScreen.this, ActBarActivity.class);
                startActivity(i);
                finish();
            }
        }, SplashScreen.SPLASHTIMEOUT);
    }
}