package com.martinscastanho.marco.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    Boolean isSignIn = true;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        ImageView logoImageView = findViewById(R.id.logoImageView);
        backgroundLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void confirmButtonPressed(View view){

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "A Username and a Password are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isSignIn){
            signIn(username, password);
        }
        else {
            login(username, password);
        }

    }

    public void login(final String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user == null){
                    Log.e("ERROR", "user is null");
                }

                Toast.makeText(MainActivity.this, "" + username + " logged in successfully", Toast.LENGTH_SHORT).show();
            }
        });

        ParseUser.logOut();
    }

    public void signIn(String username, String password){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("SIGN IN", "Success!");
                }
            }
        });

        ParseUser.logOut();
    }

    public void changeLoginSignIn(View view){
        Button confirmButton = findViewById(R.id.confirmationButton);
        TextView textView = (TextView) view;
        if(isSignIn){
            // change to Login
            confirmButton.setText("Login");
            textView.setText("or Sign In");
        }
        else {
            // change to Sign in
            confirmButton.setText("Sign In");
            textView.setText("or Login");
        }
        isSignIn = !isSignIn;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.i("KEY PRESSED", "some key was pressed");
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i("KEY PRESSED", "ENTER was pressed");
            confirmButtonPressed(v);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backgroundLayout || v.getId() == R.id.logoImageView){
            // Dismiss the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
