package arshan.com.e_medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;


public class MainActivity extends AppCompatActivity {
    String rememberFlag, DEFAULT = "", email, password;
    SharedPreferences sharedPreferences = null;
    private CustomProgressDialog customProgressDialog;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Getting data from Shared preferences
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        rememberFlag = sharedPreferences.getString("rememberFlag", DEFAULT);
        email = sharedPreferences.getString("email", DEFAULT);
        password = sharedPreferences.getString("password", DEFAULT);
        if ("Y".equalsIgnoreCase(rememberFlag)) {
            loginAgain();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void loginAgain() {
        if (!("".equalsIgnoreCase(email) && "".equalsIgnoreCase(password))) {
            String finalUrl = Constants.AUTH_URL+"?uname="+email+"&password="+password;
            Log.d("final url",finalUrl);
            new AuthenticateAgain().execute(finalUrl);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private class AuthenticateAgain extends AsyncTask<String, String, String> {
        String status = "", msg = "";
        String fname,lname,apikey,profilePic,phone,gender = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(MainActivity.this);
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
                        editor.putString("rememberFlag", "Y");
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
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again" + e.getMessage(), Toast.LENGTH_LONG).show();
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
            customProgressDialog.cancel();
            Intent i = new Intent(MainActivity.this, Home.class);
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
        }

    }
}


