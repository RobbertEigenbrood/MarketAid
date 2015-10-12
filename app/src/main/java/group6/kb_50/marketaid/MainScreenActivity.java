package group6.kb_50.marketaid;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;

import java.util.List;

import group6.kb_50.marketaid.Buyer.BuyerMainActivity;
import group6.kb_50.marketaid.Seller.SellerLoginActivity;
import group6.kb_50.marketaid.Seller.SellerMainActivity;
/*
import android.view.Menu;
import android.view.MenuItem;
*/

public class MainScreenActivity extends AppCompatActivity {

    static boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment fragment = new MainFragment();

        ft.setCustomAnimations(R.anim.fadein,R.anim.fadeout);

        ft.replace(R.id.framelayout, fragment);
        ft.addToBackStack(null);
        ft.commit();


        // Enable Local Datastore.
        if(first) {
            ParseObject.registerSubclass(Product.class);
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "eWtFfVxalDS39OF2hA8k2R3hTy8l125jU2fn4Mnx", "6q0qhKaUUDd1p2nUtCXUlOFwGqreFdoROVT7QQ2a");
            storeDatabase();
            first = false;
        }
    }

    public void ToSellerMain(View view) {
        ParseUser user = ParseUser.getCurrentUser();
        if(user != null) {
            startActivity(new Intent(this, SellerMainActivity.class));
        }
        else
            startActivity(new Intent(this,SellerLoginActivity.class));


    }

    public void ToBuyerMain(View view) {
        startActivity(new Intent(this, BuyerMainActivity.class));
    }












    @Override
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

            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void storeDatabase(){

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.findInBackground(new FindCallback<Product>() {
            public void done(List<Product> productList, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(productList);
                    Toast.makeText(getApplicationContext(), "Saved successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
