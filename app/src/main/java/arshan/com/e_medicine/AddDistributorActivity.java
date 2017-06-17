package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddDistributorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText name, uname, mail, mobile, phone, password;
    private CircleImageView pic;
    public static final String DEFAULT = "";
    private String apikey = "", TAG = DistributorsActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_distributor);
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

        name = (EditText) findViewById(R.id.distName);
        uname = (EditText) findViewById(R.id.distUname);
        mail = (EditText) findViewById(R.id.distEmail);
        mobile = (EditText) findViewById(R.id.distMobile);
        phone = (EditText) findViewById(R.id.distPhone);
        password = (EditText) findViewById(R.id.distPassword);

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fabAddDist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = Constants.ADD_DISTRIBUTOR_URL + "?name=" + name.getText().toString() + "&email=" + mail.getText().toString() +
                        "&username=" + uname.getText().toString() + "&password=" + password.getText().toString() + "&number=" + mobile.getText().toString()
                        + "&phone=" + phone.getText().toString() + "&apikey=" + apikey;
                Log.d("final url", finalUrl);
                //Make call to Async
                new addDistributor().execute(finalUrl);

            }
        });
    }

    private class addDistributor extends AsyncTask<String, String, String> {
        String status = "";
        String msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddDistributorActivity.this);
            pDialog.setMessage("Adding Distributor...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... f_url) {
            HttpHandler sh = new HttpHandler();

            String cookie="";
            /*SharedPreferences sharedPreferencesCookie = getSharedPreferences("CookieData", Context.MODE_PRIVATE);
            cookie = sharedPreferencesCookie.getString("cookieString", "");
            if (null == cookie || cookie.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(),"Cookie empty", Toast.LENGTH_LONG).show();
            }*/
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(f_url[0],cookie);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("msg");
                    Log.d("status",status);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddDistributorActivity.this, DistributorsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddDistributorActivity.this, DistributorsActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong. Please try again",
                                Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(AddDistributorActivity.this, DistributorsActivity.class);
                        startActivity(intent);
                        finish();
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
            if ("ok".equalsIgnoreCase(status)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddDistributorActivity.this, DistributorsActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            /**
             * Updating parsed JSON data into ListView
             * */
        }
    }
}

