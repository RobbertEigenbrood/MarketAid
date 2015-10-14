package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import group6.kb_50.marketaid.R;

public class SellerLoginFragment extends Fragment implements View.OnClickListener {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.seller_fragment_login, container, false);

        final Button loginButton = (Button)view.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this);

        final Button registerButton = (Button)view.findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(this);

        /* From Stackoverflow.com: "listener-for-done-button-on-edittext" */
        final EditText username = (EditText)view.findViewById(R.id.editTextusername);
        final EditText pass = (EditText)view.findViewById(R.id.editTextpassword);

        /* If the user pressed "Enter" or "Gereed", log in immediately */
        username.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /* Lekker vies de onClick van de button aanroepen */
                    onClick( loginButton );
                    return true;
                }
                return false;
            }
        });
        pass.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClick( loginButton );
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onClick(View v){
        final EditText username = (EditText)view.findViewById(R.id.editTextusername);
        final EditText pass = (EditText)view.findViewById(R.id.editTextpassword);

        ParseUser user = new ParseUser();

        switch (v.getId()) {
            case R.id.buttonLogin:
                ParseUser.logInInBackground(username.getText().toString(), pass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Toast.makeText(getActivity().getBaseContext(), "Logged in", Toast.LENGTH_SHORT).show();
                            PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("settingsusername", username.getText().toString()).commit();
                            PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("settingspassword", pass.getText().toString()).commit();

                            Intent i = new Intent(getActivity().getBaseContext(), SellerMainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), getString(R.string.wrongLgin), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.buttonRegister:
                startActivity(new Intent(getActivity(), SellerCreateAccountActivity.class));
                //TODO: change the above Activity to a Fragment
                break;
            default:
                break;
        }


    }

    public void onClickCreateUser(View v) {
        startActivity(new Intent(getActivity().getBaseContext(), SellerCreateAccountActivity.class));
    }
}
