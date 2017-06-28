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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private EditText fname, lname, uname, mail, mobile, phone, password;
    private CircleImageView pic;
    public static final String DEFAULT = "";
    private String apikey = "", TAG = AddUserActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private Spinner gender, userType;
    String userTypeString = "1", genderString = "male";
    List<String> genderList = new ArrayList<String>();
    List<String> userTypeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
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

        fname = (EditText) findViewById(R.id.firstName);
        lname = (EditText) findViewById(R.id.lastName);
        uname = (EditText) findViewById(R.id.userUname);
        gender = (Spinner) findViewById(R.id.gender);
        userType = (Spinner) findViewById(R.id.userType);
        mail = (EditText) findViewById(R.id.userEmail);
        mobile = (EditText) findViewById(R.id.userMobile);
        phone = (EditText) findViewById(R.id.userPhone);
        password = (EditText) findViewById(R.id.userPassword);

        // Spinner click listener
        gender.setOnItemSelectedListener(AddUserActivity.this);
        userType.setOnItemSelectedListener(AddUserActivity.this);

        genderList.add("Male");
        genderList.add("Female");

        userTypeList.add("Admin");
        userTypeList.add("User");

        // Creating adapter for gender
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        // Creating adapter for user types
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, userTypeList);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(userTypeAdapter);

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fabAddUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = Constants.USERS_ADD_URL +
                        "?fname=" + fname.getText().toString() +
                        "&lname=" + lname.getText().toString()  +
                        "&uname=" + uname.getText().toString()  +
                        "&pass=" + password.getText().toString() +
                        "&gen=" + genderString +
                        "&email=" + mail.getText().toString() +
                        "&phone=" + phone.getText().toString() +
                        "&mobile=" + mobile.getText().toString() +
                        "&utype=" + userTypeString +
                        "&apikey=" + apikey +
                        "&img=http://www.provo2.com/health-fitness/wp-content/uploads/2010/11/default-avatar.jpg";
                Log.d("final url", finalUrl);
                //Make call to Async
                new addUser().execute(finalUrl);

            }
        });
    }

    private class addUser extends AsyncTask<String, String, String> {
        String status = "";
        String msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddUserActivity.this);
            pDialog.setMessage("Adding user...");
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
                            Intent intent = new Intent(AddUserActivity.this, UsersActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddUserActivity.this, UsersActivity.class);
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
                        Intent intent = new Intent(AddUserActivity.this, UsersActivity.class);
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
            if ("success".equalsIgnoreCase(status) || "ok".equalsIgnoreCase(status)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddUserActivity.this, UsersActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if ("Admin".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) userTypeString = "2";
        else if ("User".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) userTypeString = "3";
        else if ("Male".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) genderString = "male";
        else if ("Female".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) genderString = "female";

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
