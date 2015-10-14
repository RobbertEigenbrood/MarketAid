package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import group6.kb_50.marketaid.R;

public class SellerCreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_create_account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void onClickCreate(View v)
    {
        EditText user = (EditText)findViewById(R.id.editTextusername);
        String userstring = user.getText().toString();
        /* Check if last character is a space due to auto-fill functionality */
        if( userstring.length() > 0 ) {
            int charAtDelete = userstring.length() - 1; //At end of string
            char c = userstring.charAt(charAtDelete);
            if( c ==  ' '){ //Space at the end of the Username? Delete.
                userstring = userstring.substring(0, userstring.length() - 1);
            }
        }
        EditText password = (EditText)findViewById(R.id.editTextpassword);
        EditText email = (EditText)findViewById(R.id.editTextemail);
        ParseUser parseUser = new ParseUser();
        try{
            parseUser.setUsername(userstring);
            parseUser.setPassword(password.getText().toString());
            parseUser.setEmail(email.getText().toString());
        }
        catch (Exception e)
        {
            Log.e("Creating new account", e.toString());
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getBaseContext(), "New User Created", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), "Error creating account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logInNewUser(user.getText().toString(), password.getText().toString());
        Intent i = new Intent(getBaseContext(), SellerMainActivity.class);
        startActivity(i);
        finish();
    }

    public void logInNewUser(String username,String password){

            final String usern = username;
            final String passw = password;

            ParseUser.logInInBackground(usern, passw, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        Toast.makeText(getBaseContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("settingsusername", usern).commit();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("settingspassword", passw).commit();

                    }
                }
            });
        }
    public void onClickBack(View v)
    {
        finish();
    }
}
