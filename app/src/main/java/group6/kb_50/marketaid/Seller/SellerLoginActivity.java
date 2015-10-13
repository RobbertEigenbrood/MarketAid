package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import group6.kb_50.marketaid.R;

public class SellerLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        /* From Stackoverflow.com: "listener-for-done-button-on-edittext" */
        final EditText username = (EditText)findViewById(R.id.editTextusername);
        final EditText pass = (EditText)findViewById(R.id.editTextpassword);

        /* If the user pressed "Enter" or "Gereed", log in immediately */
        username.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClickLogin( new View(getBaseContext()) );
                    return true;
                }
                return false;
            }
        });
        pass.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClickLogin( new View(getBaseContext()) );
                    return true;
                }
                return false;
            }
        });
    }

    public void onClickLogin(View v){

        final EditText username = (EditText)findViewById(R.id.editTextusername);
        final EditText pass = (EditText)findViewById(R.id.editTextpassword);

        ParseUser user = new ParseUser();
        ParseUser.logInInBackground(username.getText().toString(), pass.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Toast.makeText(getBaseContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("settingsusername", username.getText().toString()).commit();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("settingspassword", pass.getText().toString()).commit();

                    Intent i = new Intent(getBaseContext(), SellerMainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(getBaseContext(), getString(R.string.wrongLgin),Toast.LENGTH_SHORT).show();
                }
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

    public void onClickCreateUser(View v) {
        startActivity(new Intent(this, SellerCreateAccountActivity.class));
    }
}
