package group6.kb_50.marketaid;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import group6.kb_50.marketaid.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("GPS", getLocation());

        /* Hides the ProgressBar */
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    /* As we can't use the GPSWrapper in the Oncreate(), use getLocation() */
    private String getLocation(){
        GPSWrapper mLocation = new GPSWrapper(this);
        return mLocation.getLatLong();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_login, menu);
        return true;
    }

    public void onClickLogin(View v){

        /* Create an instance of a Login Toast so it could be canceled when the Login is fast */
        final Toast tLogin = Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG);
        tLogin.show();

        final EditText username = (EditText)findViewById(R.id.editTextusername);
        final EditText pass = (EditText)findViewById(R.id.editTextpassword);
        final Button loginButton = (Button) findViewById(R.id.buttonLogin);

        /* Hide the EditTexts and the Login Button */
        username.setVisibility(View.GONE);
        pass.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);

        /* Display the ProgressBar */
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /* Obtain location using the GPSWrapper */
        GPSWrapper mLocation = new GPSWrapper(this);
        final ParseGeoPoint parseGeoPoint = new ParseGeoPoint(mLocation.getCurrentLatDouble(), mLocation.getCurrentLongDouble());

        //ParseUser user = new ParseUser();
        ParseUser.logInInBackground(username.getText().toString(), pass.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    tLogin.cancel();
                    Toast.makeText(getBaseContext(), "Logged in as " + parseUser.getUsername(), Toast.LENGTH_SHORT).show();
                    parseUser.put("LatLong", parseGeoPoint);
                    parseUser.saveInBackground();

                    /* Close this activity if we are logged in */
                    finish();

                } else {
                    tLogin.cancel();
                    Toast.makeText(getBaseContext(), "Login failed.\n" +
                            "Please check username and password.", Toast.LENGTH_SHORT).show();
                }

                /* Hide the ProgressBar */
                progressBar.setVisibility(View.GONE);

                /* Display the EditTexts */
                username.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
