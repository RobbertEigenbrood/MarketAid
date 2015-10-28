package group6.kb_50.marketaid.Seller;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.parse.ParseUser;

import group6.kb_50.marketaid.R;

public class SellerMainActivity extends AppCompatActivity
        implements SellerNavigationFragment.NavigationDrawerCallbacks {


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private SellerNavigationFragment mSellerNavigationFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    SharedPreferences prefs = null;
    final static String PREF_NAME = "Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_empty);
        if(savedInstanceState==null) {

            mSellerNavigationFragment = (SellerNavigationFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer1);
            mTitle = getTitle();

            // Set up the drawer.
            mSellerNavigationFragment.setUp(
                    R.id.navigation_drawer1,
                    (DrawerLayout) findViewById(R.id.drawer_layout1));

        }

        prefs = getSharedPreferences("group6.kb_50.marketaid", MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(prefs.getBoolean("firstrun", true)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            /* Load the Main Fragment first... */
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new SellerMainFragment())
                    .commit();
            /* ...then load the FirstRunFragment  */
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new SellerFirstRunFragment())
                    .commit();
            prefs.edit().putBoolean("firstrun", false).apply();

            /* Set Shared boolean "product saved" to false at first time use */
            SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("boolProductSaved", false);
            editor.apply();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, boolean fromSavedInstanceState) {

        Fragment fragment = null;

        switch(position)
        {
            case 0:
                fragment = new SellerMainFragment();
                break;
            case 1:
                fragment = new SellerAddProductFragment();
                break;
            case 2:
                fragment = new SettingsActivityFragment();
                break;
            case 3 :
                onLogout();
                break;
        }
        if(!fromSavedInstanceState) {
            if (fragment != null) {
                // update the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }catch (NullPointerException e){

        }
    }

    private void onLogout(){
        DialogFragment newFragment = new FireMissilesDialogFragment();
        newFragment.show(getSupportFragmentManager(), "missiles");

    }

    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.logouttext))
                    .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ParseUser.logOut();
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}