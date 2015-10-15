package group6.kb_50.marketaid;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnKeyListener;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import group6.kb_50.marketaid.Buyer.BuyerMainActivity;
import group6.kb_50.marketaid.Seller.SellerCreateAccountActivity;
import group6.kb_50.marketaid.Seller.SellerLoginActivity;
import group6.kb_50.marketaid.Seller.SellerLoginFragment;
import group6.kb_50.marketaid.Seller.SellerMainActivity;

public class MainScreenActivity extends AppCompatActivity {

    static boolean first = true;
    static int fragnumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        // Enable Local Datastore.
        if(first) {
            ParseObject.registerSubclass(Product.class);
            ParseObject.registerSubclass(Comment.class);
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "eWtFfVxalDS39OF2hA8k2R3hTy8l125jU2fn4Mnx", "6q0qhKaUUDd1p2nUtCXUlOFwGqreFdoROVT7QQ2a");
            ParseInstallation.getCurrentInstallation().saveInBackground();
            checkConnection();
            storeDatabase();
            first = false;
        }
        if (savedInstanceState == null){
            fragnumber = 0;
        }
        ChooseFragment();
    }

    public void ToSellerMain(View view) {
        ParseUser user = ParseUser.getCurrentUser();
        if(user != null) {
            startActivity(new Intent(this, SellerMainActivity.class));
        }
        else{
            fragnumber = 2;
            ChooseFragment();
//            startActivity(new Intent(this, SellerLoginActivity.class));
            /*Fragment fragment = new SellerLoginFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
            transaction.replace(R.id.framelayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();*/
        }
    }

    public void ChooseFragment(){

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment fragment = null;
        switch (fragnumber){
            case 0: fragment = new MainScreenFragment();
                ft.setCustomAnimations(R.anim.fadein, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_right);
                fragnumber = 1;
                break;
            case 1: fragment = new MainScreenFragment();
                break;
            case 2: fragment = new SellerLoginFragment();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_right);
                fragnumber = 3;
                ft.addToBackStack(null);
                break;
            case 3: fragment = new SellerLoginFragment();
                manager.popBackStack();
                ft.setCustomAnimations(0, 0,R.anim.slide_in_right, R.anim.slide_out_right);
                ft.addToBackStack(null);
                break;
        }
        ft.replace(R.id.framelayout, fragment);
        ft.commit();
    }

    public void ToBuyerMain(View view) {
        startActivity(new Intent(this, BuyerMainActivity.class));
    }

    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            switch (fragnumber){
                case 3:
                    fragnumber = 1;
                    break;
            }
            getFragmentManager().popBackStack();
        }
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
