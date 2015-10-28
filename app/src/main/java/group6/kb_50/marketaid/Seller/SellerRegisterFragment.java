package group6.kb_50.marketaid.Seller;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.parse.SignUpCallback;

import group6.kb_50.marketaid.R;

public class SellerRegisterFragment extends Fragment implements View.OnClickListener{

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_seller_register, container, false);

        final Button createButton = (Button)view.findViewById(R.id.RegisterCreateButton);
        createButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        
        final EditText user = (EditText) view.findViewById(R.id.RegisterUserEdit);
        final EditText password = (EditText) view.findViewById(R.id.RegisterPasswordEdit);
        final EditText email = (EditText) view.findViewById(R.id.RegisterEmailEdit);
        /* If the user pressed "Enter" or "Gereed", Go to password field */
        user.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    password.requestFocus();
                    return true;
                }
                return false;
            }
        });
        password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    email.requestFocus();
                    return true;
                }
                return false;
            }
        });
        email.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClick(view.findViewById(R.id.RegisterCreateButton));
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.RegisterCreateButton ) {
            final EditText user = (EditText) view.findViewById(R.id.RegisterUserEdit);
            String userstring = user.getText().toString();
            //Check if last character is a space due to auto-fill functionality
            if (userstring.length() > 0) {
                int charAtDelete = userstring.length() - 1; //At end of string
                char c = userstring.charAt(charAtDelete);
                if (c == ' ') { //Space at the end of the Username? Delete.
                    userstring = userstring.substring(0, userstring.length() - 1);
                }
            }
            final EditText password = (EditText) view.findViewById(R.id.RegisterPasswordEdit);
            final EditText email = (EditText) view.findViewById(R.id.RegisterEmailEdit);
            ParseUser parseUser = new ParseUser();
            try {
                parseUser.setUsername(userstring);
                parseUser.setPassword(password.getText().toString());
                parseUser.setEmail(email.getText().toString());
            } catch (Exception e) {
                Log.e("Creating new account", e.toString());
                //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity().getBaseContext(), "New User Created", Toast.LENGTH_SHORT).show();
                        logInNewUser(user.getText().toString(), password.getText().toString());
                        Intent i = new Intent(getActivity().getBaseContext(), SellerMainActivity.class);
                        startActivity(i);
                        getFragmentManager().popBackStack();
                    } else {
                        if (e.getCode() == 202) {
                            Toast.makeText(getActivity().getBaseContext(), "Username already exists.", Toast.LENGTH_SHORT).show();
                        } else if (e.getCode() == 203) {
                            Toast.makeText(getActivity().getBaseContext(), "Email already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Something went wrong when creating the account.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public void logInNewUser(String username,String password){

        final String usern = username;
        final String passw = password;

        ParseUser.logInInBackground(usern, passw, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Toast.makeText(getActivity().getBaseContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("settingsusername", usern).commit();
                    PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("settingspassword", passw).commit();
                }
            }
        });
    }

}
