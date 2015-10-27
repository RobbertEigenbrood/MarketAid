package group6.kb_50.marketaid.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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

    private static int USERNAME_EXISTS  = 202;
    private static int EMAIL_EXISTS     = 203;

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
        final EditText user = (EditText)findViewById(R.id.editTextusername);
        String userstring = user.getText().toString();
        /* Check if last character is a space due to auto-fill functionality */
        if( userstring.length() > 0 ) {
            int charAtDelete = userstring.length() - 1; //At end of string
            char c = userstring.charAt(charAtDelete);
            if( c ==  ' '){ //Space at the end of the Username? Delete.
                userstring = userstring.substring(0, userstring.length() - 1);
            }
        }
        final EditText password = (EditText)findViewById(R.id.editTextpassword);
        final EditText email = (EditText)findViewById(R.id.editTextemail);
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
                /* Make an AlertDialog in case something went wrong */
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setTitle(R.string.could_not_create_account)
                       .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });

                if (e == null) {
                    logInNewUser(user.getText().toString(), password.getText().toString());
                    Toast.makeText(getBaseContext(), getString(R.string.new_user_created_with_username) + user.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), SellerMainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if(e.getCode() == USERNAME_EXISTS) {
                        //Toast.makeText(getBaseContext(), getString(R.id.username_exists, Toast.LENGTH_SHORT).show();
                         /* "Username already exists. Please choose another username" */
                                builder.setMessage(getString(R.string.username_exists))
                                .create()
                                .show();
                    }
                    else if(e.getCode() == EMAIL_EXISTS) {
                        //Toast.makeText(getBaseContext(), getString(R.id.email_exists), Toast.LENGTH_SHORT).show();
                         /* "Email address already exists. Please choose another username" */
                        builder.setMessage(getString(R.string.email_exists))
                                .create()
                                .show();
                    }
                    else{
                        //Toast.makeText(getBaseContext(), getString(R.id.went_wrong_creating_account, Toast.LENGTH_SHORT).show();
                         /* "Something went wrong when creating account" */
                        builder.setMessage(getString(R.string.went_wrong_creating_account))
                                .create()
                                .show();
                    }
                }
            }
        });

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
