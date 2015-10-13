package group6.kb_50.marketaid;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
        Fragment fragment = new MainScreenFragment();

        ft.setCustomAnimations(R.anim.fadein,R.anim.fadeout);

        ft.replace(R.id.framelayout, fragment);
        ft.addToBackStack(null);
        ft.commit();

        // Enable Local Datastore.
        if(first) {
            ParseObject.registerSubclass(Product.class);
            ParseObject.registerSubclass(Comment.class);
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "eWtFfVxalDS39OF2hA8k2R3hTy8l125jU2fn4Mnx", "6q0qhKaUUDd1p2nUtCXUlOFwGqreFdoROVT7QQ2a");
            checkConnection();
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
/*        if (id == R.id.action_settings) {

            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }*/
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

                }
            }
        });
    }

    private void checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            NoConnectionDialogFragment dialog = new NoConnectionDialogFragment();
            dialog.show(getSupportFragmentManager(),"No Connection");
        }
    }

    public static class NoConnectionDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.noConnectionText))
                    .setPositiveButton(getString(R.string.noConnectionOk), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton(getString(R.string.noConnectionCancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
