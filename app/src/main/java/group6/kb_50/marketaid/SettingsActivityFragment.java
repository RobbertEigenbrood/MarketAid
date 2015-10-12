package group6.kb_50.marketaid;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends Fragment {
    final static String PREF_NAME = "Preferences";
    View view;

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view  = inflater.inflate(R.layout.fragment_settings, container, false);
        // Restore preferences
        SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);

        String username =  settings.getString("settingsusername", "Error");
        EditText editText =(EditText)view.findViewById(R.id.settingsusernameedit);
        editText.setText(username);

        Switch switch1 = (Switch) view.findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked) {
                    Toast.makeText(getActivity(),"Checked!",Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(getActivity(),"Unchecked!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button button = (Button)view.findViewById(R.id.buttonGPS);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                if(ParseUser.getCurrentUser() != null) {
                    //TODO Get GPS location and save in in two differend doubles, long and lat
                    GPSWrapper mLocation = new GPSWrapper(getActivity());
                    ParseGeoPoint geoPoint = new ParseGeoPoint();
                    geoPoint.setLatitude(mLocation.getCurrentLatDouble());
                    geoPoint.setLongitude(mLocation.getCurrentLongDouble());

                    ParseUser.getCurrentUser().put("LatLong", geoPoint);

                    Toast.makeText(getActivity(), "Lat putted = " + mLocation.getCurrentLatitude(), Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity(),"Location of " + ParseUser.getCurrentUser().getUsername() + " has been set", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "No user is logged in!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }


    @Override
    public  void onStop(){
        super.onStop();
        //TODO Save all the necessary items on the server

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        EditText editText =(EditText)view.findViewById(R.id.settingsusernameedit);
        editor.putString("settingsusername", editText.getText().toString());
        editor.commit();

    }

    public void setGPS(View v){
        //TODO Get GPS and store in Parse
    }

}
