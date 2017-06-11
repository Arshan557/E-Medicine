package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private Toolbar toolbar;
    private String currentPwd, newPwd, reNewPwd, apikey;
    private WebView webView;
    private ProgressDialog pDialog;
    public static final String DEFAULT = "";

    @Bind(R.id.input_current_password) EditText _currentPasswordText;
    @Bind(R.id.input_new_password) EditText _newPasswordText;
    @Bind(R.id.input_reEnternewPassword) EditText _reNewPasswordText;
    @Bind(R.id.btn_changepwd) Button _changePwdButton;
    @Bind(R.id.parentRelative) RelativeLayout _parentRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (null != sharedPreferences) {
            apikey = sharedPreferences.getString("apikey", DEFAULT);
        }

        /*webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false)*/;

        ButterKnife.bind(this);
        _changePwdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    String finalUrl = Constants.CHANGE_PWD_URL+"?old="+currentPwd+"&new="+newPwd+"&apikey="+apikey;
                    Log.d("final url",finalUrl);
                    new changePwd().execute(finalUrl);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        currentPwd = _currentPasswordText.getText().toString();
        newPwd = _newPasswordText.getText().toString();
        reNewPwd = _reNewPasswordText.getText().toString();

        if (currentPwd.isEmpty() || currentPwd.length() < 4 || currentPwd.length() > 10) {
            _currentPasswordText.setError("Should be 4 to 10 alphanumeric characters");
            valid = false;
        } else {
            _currentPasswordText.setError(null);
        }

        if (newPwd.isEmpty() || newPwd.length() < 4 || newPwd.length() > 10) {
            _newPasswordText.setError("Should be 4 to 10 alphanumeric characters");
            valid = false;
        } else {
            _newPasswordText.setError(null);
        }
        if (reNewPwd.isEmpty() || reNewPwd.length() < 4 || reNewPwd.length() > 10) {
            _reNewPasswordText.setError("Should be 4 to 10 alphanumeric characters");
            valid = false;
        } else {
            _reNewPasswordText.setError(null);
        }
        if (!reNewPwd.matches(newPwd)) {
            _newPasswordText.setError("Not matching");
            valid = false;
        } else {
            _newPasswordText.setError(null);
        }
        return valid;
    }

    private class changePwd extends AsyncTask<String, String, String> {
        String msg = "Error occured";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SettingsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... f_url) {
            HttpHandler sh = new HttpHandler();

            String cookie;
            SharedPreferences sharedPreferencesCookie = getSharedPreferences("CookieData", Context.MODE_PRIVATE);
            cookie = sharedPreferencesCookie.getString("cookieString", "");
            if (null == cookie || cookie.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(),"Cookie empty", Toast.LENGTH_LONG).show();
            }
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(f_url[0],cookie);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String status = jsonObj.getString("status");
                    Log.d("status",status);
                    msg = jsonObj.getString("msg");
                    if ("ok".equalsIgnoreCase(status)) {
                        SharedPreferences preferences =getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        /*Snackbar snackbar = Snackbar.make(_parentRelative, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        finish();*/

                    } else if ("error".equalsIgnoreCase(status)) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar.make(_parentRelative, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "MalformedURLException " + e.getMessage());
                    if (null != msg) {
                        Snackbar snackbar = Snackbar.make(_parentRelative, msg, Snackbar.LENGTH_LONG);
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
            Snackbar snackbar = Snackbar.make(_parentRelative, msg+" Login Again", Snackbar.LENGTH_LONG);
            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                                finish();
                            }
                        }, 2000);

        }

    }
}
