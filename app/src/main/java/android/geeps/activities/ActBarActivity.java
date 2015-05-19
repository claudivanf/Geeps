package android.geeps.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.geeps.R;
import android.geeps.core.User;
import android.geeps.util.MainFragment;
import android.geeps.util.StoredData;
import android.geeps.util.TextFragment;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class ActBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment main = new MainFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, main);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

        StoredData sd = new StoredData(this);
        User user = new User(sd.getPhone(), sd.getCountryCode(), sd.getName());

        TextView name = (TextView) findViewById(R.id.textViewName);
        name.setText(user.getName());
        TextView phone = (TextView) findViewById(R.id.textViewPhone);
        phone.setText(user.getPhone());

//        Toast.makeText(getApplicationContext(), "RegId: " + sd.getRegId(), Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
//            case R.id.action_call:
//                Intent dialer= new Intent(Intent.ACTION_DIAL);
//                startActivity(dialer);
//                return true;
//            case R.id.action_speech:
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                startActivityForResult(intent, 1234);
//
//                return true;
//            case R.id.action_done:
//
//                Bundle args = new Bundle();
//                args.putString("Menu", "You pressed done button.");
//                Fragment detail = new TextFragment();
//                detail.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.content_frame, detail).commit();
//
//                return true;
//            case R.id.action_contacts:
//                Toast.makeText(getApplicationContext(),"Contacts Clicked",Toast.LENGTH_SHORT).show();
//                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Settings Clicked",Toast.LENGTH_SHORT).show();
                return true;
//            case R.id.action_status:
//                Toast.makeText(getApplicationContext(),"Status Clicked",Toast.LENGTH_SHORT).show();
//                return true;
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
}
