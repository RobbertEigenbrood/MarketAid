package group6.kb_50.marketaid.Seller;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
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
        switch1.setChecked(ParseUser.getCurrentUser().getBoolean("Present"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParseUser.getCurrentUser().put("Present",isChecked);
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
        editor.putBoolean("present",switch1.isSelected());
        //editor.commit();
        editor.putFloat("Latitude", (float) mLat);
        editor.putFloat("Longitude", (float) mLong);
        //editor.commit();
        editor.apply();

    }

    public void onClickSettingsLogin(View view){

    }

}