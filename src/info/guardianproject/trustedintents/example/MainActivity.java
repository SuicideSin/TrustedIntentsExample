
package info.guardianproject.trustedintents.example;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import info.guardianproject.GuardianProjectRSA1024;
import info.guardianproject.trustedintents.TrustedIntents;

import java.security.cert.CertificateException;

public class MainActivity extends ActionBarActivity {

    private static TrustedIntents trustedIntents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        trustedIntents = TrustedIntents.get(this);
        trustedIntents.addTrustedSigner(new GuardianProjectRSA1024());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final Activity activity = getActivity();
            Button sendSecretsToGPG = (Button) activity.findViewById(R.id.send_secrets_to_gpg);
            sendSecretsToGPG.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setClassName("info.guardianproject.gpg",
                                "info.guardianproject.gpg.MainActivity");
                        trustedIntents.startActivity(activity, intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } catch (CertificateException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
