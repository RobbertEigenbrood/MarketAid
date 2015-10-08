package group6.kb_50.marketaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import group6.kb_50.marketaid.Buyer.BuyerMainActivity;
import group6.kb_50.marketaid.Seller.SellerLoginActivity;
import group6.kb_50.marketaid.Seller.SellerMainActivity;
/*
import android.view.Menu;
import android.view.MenuItem;
*/

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Product.class);
        Parse.initialize(this, "eWtFfVxalDS39OF2hA8k2R3hTy8l125jU2fn4Mnx", "6q0qhKaUUDd1p2nUtCXUlOFwGqreFdoROVT7QQ2a");
    }

    public void ToSellerMain(View view) {
        ParseUser user = new ParseUser();
        if(user != null) {
            startActivity(new Intent(this, SellerMainActivity.class));
        }
        else
            startActivity(new Intent(this,SellerLoginActivity.class));


    }

    public void ToBuyerMain(View view) {
        startActivity(new Intent(this, BuyerMainActivity.class));
    }

    public void ToMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }











    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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
    }*/
}
