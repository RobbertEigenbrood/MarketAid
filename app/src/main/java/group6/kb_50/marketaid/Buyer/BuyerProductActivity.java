package group6.kb_50.marketaid.Buyer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
import android.app.AlertDialog; //This one instead
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import group6.kb_50.marketaid.GPSWrapper;
import group6.kb_50.marketaid.MainScreenActivity;
import group6.kb_50.marketaid.R;
import group6.kb_50.marketaid.Seller.SettingsActivityFragment;


/* The Buyer product Detail Screen */

public class BuyerProductActivity extends AppCompatActivity
        implements CommentFragment.OnFragmentInteractionListener {
    private String ID;
    SwipeRefreshLayout swipeRefreshLayout;


    /* Don't use GPS until the user requests to. Also, leave it as a global variable so we can remove it (updates) in the onPause() */
    GPSWrapper mLocation = null;
    GPSWrapper tempLocation = null;
    boolean onLocationFirst = true;    //Makes sure the GPSWrapper is instantiated only once in case of no connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_activity_product);

        /* Display the ProgressBar */
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBuyerProduct);
        progressBar.setVisibility(View.VISIBLE);

        /* Hide the TextViews (and show the progressbar) until data is received from server */
        final TextView productTitle = (TextView) findViewById(R.id.TitleProductText);
        productTitle.setVisibility(TextView.GONE);
        final TextView productDescription = (TextView) findViewById(R.id.DescriptionProductText);
        productDescription.setVisibility(TextView.GONE);

        handleIntent();

        /* If the information is obtained from the server, hide the ProgressBar and show the TextViews */
        progressBar.setVisibility(View.GONE);
        productTitle.setVisibility(TextView.VISIBLE);
        productDescription.setVisibility(TextView.VISIBLE);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment fragment = new CommentFragment();

        Bundle bundle =  new Bundle();
        bundle.putString("ID",ID);
        fragment.setArguments(bundle);

        ft.replace(R.id.commentframelayout, fragment);
        ft.addToBackStack(null);
        ft.commit();


    }


    public void handleIntent() {
        Intent i = getIntent();
        ID = i.getStringExtra("ID");

        final TextView nametv = (TextView) findViewById(R.id.TitleProductText);
        final TextView descriptiontv = (TextView) findViewById(R.id.DescriptionProductText);
        final ParseImageView imageview = (ParseImageView) findViewById(R.id.view);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        query.fromLocalDatastore();
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                String name = object.get("Name").toString();
                String description = object.get("Description").toString();
                ParseFile file = object.getParseFile("Image");

                nametv.setText(name);
                descriptiontv.setText(description);
                imageview.setParseFile(file);
                imageview.loadInBackground();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buyer_product, menu);
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

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public void onClickFindLocation(View view) {

        if(isAirplaneModeOn(this)){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setIcon(R.drawable.airplane)
                    .setTitle("Airplane Mode")
                    .setMessage("The phone is in airplane mode.")
                    .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    })
                    .setNegativeButton(getString(R.string.noConnectionMapsCancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Log.e("GPS", "User clicked/cancelled this AlertDialog");
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e("GPS", "User cancelled this AlertDialog");
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            return;
        }

        /* Go to or show location of seller */
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
         /* Check for internet connection in order to load Maps properly */
        checkConnection();
        /* Create only one instance of GPSWrapper in case the button is pressed again */
        if(mLocation == null) {
            mLocation = new GPSWrapper(this);
        }
        mLocation.removeUpdates();
        // Retrieve the object by id
        //TODO: waarom willen we de locatie online (van de server) ophalen? Hierdoor wordt Maps niet geladen wanneer er geen internet is!

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            /* Notify the user */

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setIcon(R.drawable.location_logo)
                    .setTitle(getString(R.string.locationsettings))
                    .setMessage(getString(R.string.locationtext))
                    .setPositiveButton(getString(R.string.gotosettings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                Toast.makeText(getBaseContext(), getString(R.string.location_enabled), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.noConnectionMapsCancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Log.e("GPS", "User clicked/cancelled this AlertDialog");
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.e("GPS", "User cancelled this AlertDialog");
                    }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        }
        /* Check again, as the user might not have enabled Location settings */
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            //Settings where not enabled: "cancel" the click of this button
            return;
        }

        /* Wait in a Thread until a Location has been found */
        Toast.makeText(this, getString(R.string.obtaining_location), Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            public void run() {
                int count = 1;
                Looper.prepare();//Gave Runtime Exception when not implemented
                while (!mLocation.hasALock()) {
                    mLocation.getCurrentLocation();
                    // Wait until GPS lock
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("GPS", "BuyerProductRun: No lock found after " + ( count>1 ? " seconds" : " second"));
                    count++;
                }
                /* GPS lock found! */
                Log.e("GPS", "BuyerProductRun: Lock has been found after " + count + " seconds");
                Log.e("GPS", "BuyerProductRun: Location is " + mLocation.getLatLong());

                /* Save the Latitude and Longitude in global variables so we can use them later on */
                mLocation.getCurrentLatDouble();
                mLocation.getCurrentLongDouble();

                query.getInBackground(ID, new GetCallback<ParseObject>() {
                    public void done(ParseObject p, ParseException e) {
                        if (e == null) {

                            p.getParseUser("Seller").fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject seller, ParseException e) {
                                    ParseGeoPoint sellerGeoPoint = (ParseGeoPoint) seller.get("LatLong");
                                    double ownLat = mLocation.getCurrentLatDouble();
                                    double ownLong = mLocation.getCurrentLongDouble();

                                    double sellerLat = sellerGeoPoint.getLatitude();
                                    double sellerLong = sellerGeoPoint.getLongitude();
                                    Uri googlemapsUri = Uri.parse("http://maps.google.com/maps?saddr=" + ownLat + "," + ownLong + "&daddr=" + sellerLat + "," + sellerLong);
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, googlemapsUri);
                                    startActivity(intent);

                                    /* Dismiss the GPSWrapper object (which also clears the Location icon in Taskbar) */
                                    mLocation.removeUpdates();
                                }
                            });
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    public void onClickCommentButton(View v){
                Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                i.putExtra("Product",ID);
                startActivity(i);
    }

    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            NoConnectionMapsDialogFragment dialog = new NoConnectionMapsDialogFragment();
            dialog.show(getSupportFragmentManager(),"No Connection");
            Log.e("Connection", "No internet connection!");
            return false;
        }
        return true;
    }

    public static class NoConnectionMapsDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.noConnectionMapsText))
                    .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton(getString(R.string.noConnectionMapsCancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        /* Remove the GPS location when the user leaves this activity */
        if(mLocation != null) {
            mLocation.removeUpdates();
        } else{ //Object will be null when user hasn't used Set GPS button */
            Log.e("GPS", "mLocation was null when trying to remove!");
        }

    }
}