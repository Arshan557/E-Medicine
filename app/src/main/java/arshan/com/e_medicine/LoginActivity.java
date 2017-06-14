package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_FORGOT = 1;
    private ProgressDialog pDialog;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.forgot_pwd) TextView _forgotLink;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.parentRelative) RelativeLayout _parentRelative;
    private CheckBox _rememberChecked;

    @Bind(R.id.clearCookie) TextView clearCookie;

    String email = null;
    String password = null;
    boolean isRemembered = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _rememberChecked = (CheckBox) findViewById(R.id.rememberCheck);
        ButterKnife.bind(this);
        boolean mobileNwInfo = false;

        //Checking internet connection
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            mobileNwInfo = conMgr.getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {
            mobileNwInfo = false;
        }
        if (mobileNwInfo == false) {
            final boolean mnwI = mobileNwInfo;
            Snackbar snackbar = Snackbar
                    .make(_parentRelative, "Plz enable WiFi/Mobile data", Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        /*_signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });*/

        _forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        clearCookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("CookieData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(LoginActivity.this, CookieActivity.class));
                finish();
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

        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        isRemembered = _rememberChecked.isChecked();

        onLoginSuccess();

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        pDialog.dismiss();
                    }
                }, 2000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
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
        boolean mobileNwInfo = false;
        try {
            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            mobileNwInfo = conMgr.getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {
            mobileNwInfo = false;
        }
        if (mobileNwInfo == false) {
            final boolean mnwI = mobileNwInfo;
            Snackbar snackbar = Snackbar
                    .make(_parentRelative, "Plz enable WiFi/Mobile data", Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*if(mnwI == true) {
                                Snackbar snackbar1 = Snackbar.make(_parentRelative, "Connected", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(_parentRelative, "Sorry! Not yet connected", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }*/
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);

            snackbar.show();
        } else {
            String finalUrl = Constants.AUTH_URL+"?uname="+email+"&password="+password;
            Log.d("final url",finalUrl);
            new Authenticate().execute(finalUrl);
        }
    }

    private class Authenticate extends AsyncTask<String, String, String> {
        String status = "", msg = "";
        String fname,lname,apikey,profilePic,phone,gender = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Authenticating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... f_url) {
            HttpHandler sh = new HttpHandler();

            String cookie;
            SharedPreferences sharedPreferencesCookie = getSharedPreferences("CookieData", Context.MODE_PRIVATE);
            cookie = sharedPreferencesCookie.getString("cookieString", "");
            Log.d("cookie",cookie);
            if (null == cookie || cookie.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(),"Cookie empty", Toast.LENGTH_LONG).show();
            }
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(f_url[0],cookie);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    status = jsonObj.getString("status");

                    Log.d("status",status);

                    if ("ok".equalsIgnoreCase(status)) {
                        // Getting JSON Array node
                        JSONArray products = jsonObj.getJSONArray("User");
                        // looping through All News
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            fname = c.getString("fname");
                            lname = c.getString("lname");
                            apikey = c.getString("apikey");
                            profilePic = c.getString("profilePic");
                            phone = c.getString("phone");
                            gender = c.getString("gender");
                        }
                        //Shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("fname", fname);
                        editor.putString("lname", lname);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putString("apikey", apikey);
                        editor.putString("profilePic", profilePic);
                        editor.putString("phone", phone);
                        editor.putString("apigenderkey", gender);
                        if (isRemembered) {
                            editor.putString("rememberFlag", "Y");
                            Log.d("isRemembered","true");
                        } else {
                            editor.putString("rememberFlag", "N");
                            Log.d("isRemembered","false");
                        }
                        editor.commit();
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Try again" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "MalformedURLException " + e.getMessage());
                    if (null != msg) {
                        Snackbar snackbar = Snackbar.make(_parentRelative, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(_parentRelative, "Invalid Username/Password", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (null != status && !status.isEmpty() && "ok".equalsIgnoreCase(status)) {
                Intent i = new Intent(LoginActivity.this, Home.class);
                Bundle bundle = new Bundle();
                bundle.putString("fname",fname);
                bundle.putString("lname",lname);
                bundle.putString("apikey",apikey);
                if (!profilePic.equalsIgnoreCase("")) {
                    bundle.putString("profilePic", profilePic);
                }
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else if ("error".equalsIgnoreCase(status)) {
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar.make(_parentRelative, msg, Snackbar.LENGTH_LONG);
                snackbar.show();
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
            }

        }

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Please try again", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Should be 4 to 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
