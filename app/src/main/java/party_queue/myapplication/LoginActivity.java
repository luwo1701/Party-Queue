package party_queue.myapplication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.party_queue_1243.party_queue.PartyQueue;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesAccountRequest;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesAccountResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    Long USER_ID;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    Button _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        USER_ID = 0L;

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        PartyQueueApiMessagesAccountRequest loginInfo = new PartyQueueApiMessagesAccountRequest();
        loginInfo.setEmail(email);
        loginInfo.setUsername(password);
        PartyQueue.Builder builder = new PartyQueue.Builder(
                                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        builder.setApplicationName("party_queue_1243");
        PartyQueue service = builder.build();

        // TODO: Implement your own authentication logic here.
        Thread t = new Thread(new myrunnable(service, loginInfo));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e){}

        if (USER_ID == null || USER_ID == 0L) onLoginFailed();
        else onLoginSuccess();
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        *//*PartyQueue.Builder builder = new PartyQueue.Builder(
                                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
                        builder.setApplicationName("party_queue_1243");



                        PartyQueue service = builder.build();
                        PartyQueueApiMessagesAccountRequest loginInfo = new PartyQueueApiMessagesAccountRequest();
                        loginInfo.setEmail(email);
                        loginInfo.setUsername();
*//*
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Log.d("LoginActivity", "OnActivityResult");
                Bundle extras = data.getExtras();
                if (extras != null) {
                    USER_ID = extras.getLong("USER_ID");
                }

                Log.d("LoginActivity", "Signup Returned user id = "+USER_ID);
                onLoginSuccess();

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(this, SpotifyActivity.class);
        Intent intent = new Intent(this, MenuActivity.class);

        intent.putExtra("USER_ID", USER_ID);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    class myrunnable implements Runnable {
        PartyQueue service;
        PartyQueueApiMessagesAccountRequest loginInfo;

        public myrunnable(PartyQueue s, PartyQueueApiMessagesAccountRequest l) {
            service = s;
            loginInfo = l;
        }

        public void run () {
            PartyQueueApiMessagesAccountResponse r;
            try {
                r = service.partyqueue().login(loginInfo).execute();
                USER_ID = r.getId();
                Log.d("SignupActivity", "ID = " + USER_ID);
                Log.d("SignupActivity", "Username = " + r.getUsername());
                Log.d("SignupActivity", "Email = " + r.getEmail());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
