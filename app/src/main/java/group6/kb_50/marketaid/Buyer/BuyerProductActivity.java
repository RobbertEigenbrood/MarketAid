package group6.kb_50.marketaid.Buyer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import group6.kb_50.marketaid.GPSWrapper;
import group6.kb_50.marketaid.MainScreenActivity;
import group6.kb_50.marketaid.R;
import group6.kb_50.marketaid.Seller.SettingsActivityFragment;


/* The Buyer product Detail Screen */

public class BuyerProductActivity extends AppCompatActivity
        implements CommentFragment.OnFragmentInteractionListener {
    private String ID;


    /* Don't use GPS until the user requests to. Also, leave it as a global variable so we can remove it (updates) in the onPause() */
    GPSWrapper mLocation = null;
    boolean onLocationFirst = true;    //Makes sure the GPSWrapper is instantiated only once in case of no connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_product);

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickFindLocation(View view) {
        /* Go to or show location of seller */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        mLocation = new GPSWrapper(this);
         /* Check for internet connection in order to load Maps properly */
        checkConnection();
        // Retrieve the object by id
        //TODO: waarom willen we data online (van de server) ophalen? Hierdoor wordt Maps niet geladen wanneer er geen internet is!
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            public void done(ParseObject p, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.

                    p.getParseUser("Seller").fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject seller, ParseException e) {
                            ParseGeoPoint sellerGeoPoint = (ParseGeoPoint) seller.get("LatLong");

                            double ownLat = mLocation.getCurrentLatDouble();
                            double ownLong = mLocation.getCurrentLongDouble();

                            double sellerLat = sellerGeoPoint.getLatitude();
                            double sellerLong = sellerGeoPoint.getLongitude();

                            Uri googlemapsUri = Uri.parse("http://maps.google.com/maps?saddr=" + ownLat + "," + ownLong + "&daddr=" + sellerLat + "," + sellerLong);
                            final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, googlemapsUri);

                            /* Check GPS status */
                            if (mLocation.isGPSON()) {
                                /* Do we have a location already? */
                                if (!mLocation.hasALock()) {    //(GPS is slow or older device)
                                    Toast.makeText(getBaseContext(), "Waiting for GPS location", Toast.LENGTH_SHORT).show();

                                    /* Wait in Thread until GPS lock is found. Check every second. */
                                    new Thread(new Runnable() {
                                        public void run() {
                                            int count = 1;
                                            while (!mLocation.hasALock()) {
                                                // Wait until GPS lock
                                                try{
                                                    Thread.sleep(1000);
                                                }catch (Exception e){e.printStackTrace();}
                                                Log.e("GPS", "No lock found after " + count + "seconds");
                                                count++;
                                            }
                                            /* GPS lock found! */
                                            Log.e("GPS", "Lock has been found after " + count + "seconds");
                                            startActivity(intent);
                                            mLocation.removeUpdates();
                                        }
                                    }).start();

                                } else{ // We already have a location (GPS is fast or newer device)
                                    startActivity(intent);
                                    mLocation.removeUpdates();
                                }
                            } else {    //Location settings are off
                                Log.e("GPS", "GPS is off!");
                                DialogFragment newFragment = new SettingsActivityFragment.GPSWarningDialogFragment();
                                newFragment.show(getSupportFragmentManager(), "GPSonDialog");
                                mLocation.removeUpdates();
                                return;
                            }
                        }
                    });
                }
            }
        });
        //mLocation.removeUpdates(); //Moved to the onPause method in case the user wants to wait for a GPS lock
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
                    .setPositiveButton(getString(R.string.noConnectionMapsOk), new DialogInterface.OnClickListener() {
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
        if(mLocation != null) {
            mLocation.removeUpdates();
        } else{ //Object will be null when user hasn't used Set GPS button */
            Log.e("GPS", "mLocation was null when trying to remove!");
        }
    }
}