package group6.kb_50.marketaid.Seller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import group6.kb_50.marketaid.R;

public class SellerCreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_create_account);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCreate(View v)
    {
        EditText user = (EditText)findViewById(R.id.editTextusername);
        EditText password = (EditText)findViewById(R.id.editTextpassword);
        EditText email = (EditText)findViewById(R.id.editTextemail);
        ParseUser parseUser = new ParseUser();
        try{
        parseUser.setUsername(user.getText().toString());
        parseUser.setPassword(password.getText().toString());
        parseUser.setEmail(email.getText().toString());}
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getBaseContext(), "New User Created", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(),"Error creating account",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void onClickBack(View v)
    {
        finish();
    }
}
