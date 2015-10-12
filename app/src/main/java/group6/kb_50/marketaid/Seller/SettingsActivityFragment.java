package group6.kb_50.marketaid.Seller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Restore preferences
        final SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);

        TextView tv = (TextView) view.findViewById(R.id.textView8);
        tv.setText("You are logged in as: " + ParseUser.getCurrentUser().getUsername());

        Switch switch1 = (Switch) view.findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getActivity(), "Checked!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Unchecked!", Toast.LENGTH_SHORT).show();
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        /* The "Set GPS" button */
        Button button = (Button) view.findViewById(R.id.buttonGPS);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ParseUser.getCurrentUser() != null) {
                    GPSWrapper mLocation = new GPSWrapper(getActivity());
                    ParseGeoPoint geoPoint = new ParseGeoPoint();

                    /* Let's hope we have a fix. If not, maybe wait in Thread for a fix? */

                    Log.e("GPS", getLocation());
                    if(!mLocation.isGPSON()){
                        Log.e("GPS", "GPS is off!"); DialogFragment newFragment = new GPSWarningDialogFragment();
                        newFragment.show(getFragmentManager(), "GPSonDialog");
                        return;
                    }

                    /* Save the Latitude and Longitude in global variable so we can use the later on */
                    mLat = mLocation.getCurrentLatDouble();
                    geoPoint.setLatitude(mLat);
                    mLong = mLocation.getCurrentLongDouble();
                    geoPoint.setLongitude(mLong);

                    ParseUser.getCurrentUser().put("LatLong", geoPoint);

                    Toast.makeText(getActivity(), "Location of " + ParseUser.getCurrentUser().getUsername() + " has been set", Toast.LENGTH_SHORT).show();
                    ParseUser.getCurrentUser().saveInBackground();
                } else {
                    Toast.makeText(getActivity(), "No user is logged in!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /* Use getLocation() for logging/debugging */
    private String getLocation(){
        GPSWrapper mLocation = new GPSWrapper(getActivity());
        return mLocation.getLatLong();
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

    public void onClickSettingsLogin(View view){

    }

    /* Show this dialog when the GPS is off */
    public static class GPSWarningDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.gpswarning))
                    .setCancelable(false) // Doesn't seem to ve working though
                    .setIcon(R.drawable.location_logo)
                    .setTitle(getString(R.string.locationsettings))
                    .setPositiveButton(getString(R.string.gotosettings), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(viewIntent);
                        }
                    })
                    .setNegativeButton(getString(R.string.logoutcancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}