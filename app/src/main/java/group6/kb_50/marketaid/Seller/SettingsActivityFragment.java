package group6.kb_50.marketaid.Seller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import group6.kb_50.marketaid.GPSWrapper;
import group6.kb_50.marketaid.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends Fragment {
    final static String PREF_NAME = "Preferences";
    View view;
    double mLat = 0.0,
           mLong = 0.0;

    GPSWrapper mLocation = null;

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.seller_fragment_settings, container, false);
        // Restore preferences
        final SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);

        TextView tv = (TextView) view.findViewById(R.id.textView8);
        tv.setText("You are logged in as: " + ParseUser.getCurrentUser().getUsername());

        Switch switch1 = (Switch) view.findViewById(R.id.switch1);
        switch1.setChecked(ParseUser.getCurrentUser().getBoolean("Present"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParseUser.getCurrentUser().put("Present",isChecked);
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        //TODO: maybe implement saving GPS location at database in Thread? This way, it will only be saved when a Lock has been found
        /* The "Set GPS" button */
        Button button = (Button) view.findViewById(R.id.buttonGPS);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (ParseUser.getCurrentUser() == null) {
                    //No user logged in: better safe than sorry
                    Toast.makeText(getActivity(), "No user is logged in!", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setIcon(R.drawable.location_logo)
                    .setTitle(getString(R.string.locationsettings))
                    .setMessage(getActivity().getResources().getString(R.string.locationtext))
                    .setPositiveButton(getActivity().getString(R.string.gotosettings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getActivity().startActivity(myIntent);
                            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                Toast.makeText(getActivity(), "Location settings where enabled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.setNegativeButton(getActivity().getString(R.string.noConnectionMapsCancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Log.e("GPS", "User clicked/cancelled this AlertDialog");
                        }
                    });
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e("GPS", "User cancelled this AlertDialog");
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.e("GPS", "User dismissed this AlertDialog");
                        }
                    });
                    dialog.show();

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

                 /* Create only one instance of GPSWrapper in case the button is pressed again */
                if(mLocation == null) {
                    mLocation = new GPSWrapper(getActivity());
                    Toast.makeText(getActivity(), R.string.saving_location, Toast.LENGTH_SHORT).show();
                }
                mLocation.removeUpdates();

                /* Wait in a Thread until a Location has been found */
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
                            Log.e("GPS", "SettingsRun: No lock found after " + ( count>1 ? " seconds" : " second"));
                            count++;
                        }
                        /* GPS lock found! */
                        Log.e("GPS", "SettingsRun: Lock has been found after " + count + " seconds");
                        Log.e("GPS", "SettingsRun: Location is " + mLocation.getLatLong());

                        /* Save the Latitude and Longitude in global variables so we can use them later on */
                        ParseGeoPoint geoPoint = new ParseGeoPoint();
                        mLat = mLocation.getCurrentLatDouble();
                        geoPoint.setLatitude(mLat);
                        mLong = mLocation.getCurrentLongDouble();
                        geoPoint.setLongitude(mLong);

                        ParseUser.getCurrentUser().put("LatLong", geoPoint);

                        Toast.makeText(getActivity(), "Location of " + ParseUser.getCurrentUser().getUsername() + " has been set", Toast.LENGTH_SHORT).show();
                        //TODO: check for internet connection here and inform user if not
                        ParseUser.getCurrentUser().saveInBackground();
                        /* Dismiss the GPSWrapper object (which also clears the Location icon in Taskbar) */
                        mLocation.removeUpdates();
                    }
                }).start();
            }
        });

        return view;
    }

    /* Use getLocation() for logging/debugging */
    private String getLocation(){
        GPSWrapper mLocation = new GPSWrapper(getActivity());
        String s = mLocation.getLatLong();
        mLocation.removeUpdates();
        return s;
    }

    @Override
    public  void onStop(){
        super.onStop();
        //TODO Save all the necessary items on the server

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Switch switch1 = (Switch) view.findViewById(R.id.switch1);
        editor.putBoolean("present", switch1.isSelected());
        //editor.commit();
        editor.putFloat("Latitude", (float) mLat);
        editor.putFloat("Longitude", (float) mLong);
        //editor.commit();
        editor.apply();
    }

}