package party_queue.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    Long USER_ID;

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    /**
     * get the email string entered by the user
     * @return name of user
     */
    public String getnameText(){
        return _nameText.getText().toString();
    }

    /**
     * get the email string entered by the user
     * @return email
     */
    public String getemailText(){
        return _emailText.getText().toString();
    }

    /**
     * get the password entered by the user
     * @return password
     */
    public String getpasswordTest(){
        return _passwordText.getText().toString();
    }

    /**
     * Tell the user an error occured.
     * @param editText the field for the error to be displayed on
     * @param msg the error message
     */
    public void setError(EditText editText, String msg){
        editText.setError(msg);
    }

    /**
     * Signup the user and log them in if they pass validation.
     */
    public void signup() {
        Log.d(TAG, "Signup");



        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        PartyQueue.Builder builder = new PartyQueue.Builder(
                                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
                        builder.setApplicationName("party_queue_1243");



                        PartyQueue service = builder.build();
                        PartyQueueApiMessagesAccountRequest loginInfo = new PartyQueueApiMessagesAccountRequest();
                        loginInfo.setEmail(email);
                        loginInfo.setUsername(name);

                        Thread t = new Thread( new myrunnable(service, loginInfo));
                        t.start();
                        try {
                            t.join();
                            onSignupSuccess();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            onSignupFailed();
                        }


                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    /**
     * tell the user that signup was succesful and finish the activity
     */
    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        resultIntent.putExtra("USER_ID", USER_ID);
        finish();
    }

    /**
     * tell the user that signup failed and renable the signup button
     */
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }


    /**
     * validate the email, name, and password parameters provided by the user
     * @return true if validation passes, false otherwise
     */
    public Boolean validate() {
        Boolean valid = true;

        String name = getnameText();
        String email = getemailText();
        String password = getpasswordTest();

        if (name.isEmpty() || name.length() < 3) {
            setError(_nameText, "at least 3 characters");
            valid = false;
        } else {
            setError(_nameText, null);
        }

        if (email.isEmpty()) {
            //not pattern matching email for following reason: https://davidcel.is/posts/stop-validating-email-addresses-with-regex/
            setError(_emailText, "enter a valid email address");
            valid = false;
        } else {
            setError(_emailText, null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            setError(_passwordText, "between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            setError(_passwordText, null);
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
                r = service.partyqueue().signup(loginInfo).execute();
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