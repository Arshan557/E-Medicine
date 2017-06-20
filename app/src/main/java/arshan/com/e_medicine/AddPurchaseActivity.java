package arshan.com.e_medicine;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;

public class AddPurchaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private EditText amount, invoice, date;
    private ImageView dateIcon;
    private Button btnAddBill;
    private Spinner distList;
    String distributorId = "";
    public static final String DEFAULT = "";
    private String apikey = "", TAG = AddPurchaseActivity.class.getSimpleName();
    private CustomProgressDialog customProgressDialog;
    private DatePicker datePicker;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    static final int DATE_PICKER_ID = 1111;
    List<String> distributorList = new ArrayList<String>();
    Map<String, String> distributorMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);
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

        amount = (EditText) findViewById(R.id.purchaseAmount);
        invoice = (EditText) findViewById(R.id.purchaseInvoice);
        date = (EditText) findViewById(R.id.purchaseDate);
        distList = (Spinner) findViewById(R.id.spinnerDistributor);
        dateIcon = (ImageView) findViewById(R.id.dateIcon);
        btnAddBill = (Button) findViewById(R.id.btn_addbill);

        // Spinner click listener
        distList.setOnItemSelectedListener(AddPurchaseActivity.this);

        distributorList.clear();
        distributorList.add("Choose distributor");
        //Get active distributors
        String finalUrl = Constants.PURCHASE_PRE_ADD_URL+"?&apikey="+apikey;
        Log.d("final url",finalUrl);
        new getActiveDistributors().execute(finalUrl);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, distributorList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        distList.setAdapter(dataAdapter);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(showDate(year, month+1, day));

        dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
            }
        });

        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add purchase bill
                String finalUrl = Constants.PURCHASE_ADD_URL+"?&apikey="+apikey+"&amt="+amount.getText().toString()+"&dist="+distributorId+
                        "&invoice="+invoice.getText().toString()+"&billdate="+date.getText().toString();
                Log.d("final url",finalUrl);
                new addPurchaseBill().execute(finalUrl);
            }
        });
    }

    private class addPurchaseBill extends AsyncTask<String, String, String> {
        String status,msg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(AddPurchaseActivity.this);
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
                            Toast.makeText(AddPurchaseActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(AddPurchaseActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                    startActivity(i);
                    finish();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddPurchaseActivity.this,
                                "Something went wrong. Please try again",
                                Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                        startActivity(i);
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
            customProgressDialog.cancel();
            if (null != msg) {
                Toast.makeText(AddPurchaseActivity.this, msg, Toast.LENGTH_LONG).show();
                invoice.setText("");
                amount.setText("");
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                date.setText(showDate(year, month+1, day));
            }
        }
    }

    private class getActiveDistributors extends AsyncTask<String, String, String> {
        String status,msg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(AddPurchaseActivity.this);
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
                    if ("ok".equalsIgnoreCase(status)) {
                        // Getting JSON Array node
                        JSONArray category = jsonObj.getJSONArray("distributors");
                        // looping through All News
                        for (int i = 0; i < category.length(); i++) {
                            JSONObject c = category.getJSONObject(i);

                            String id = c.getString("id");
                            String name = c.getString("name");
                            distributorList.add(name);
                            distributorMap.put(name, id);

                        }
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                    Log.d("status",status);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddPurchaseActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(AddPurchaseActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                    startActivity(i);
                    finish();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddPurchaseActivity.this,
                                "Something went wrong. Please try again",
                                Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                        startActivity(i);
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
            customProgressDialog.cancel();
            if (null != msg) {
                Toast.makeText(AddPurchaseActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private StringBuilder showDate(int year, int month, int day) {
        String mnth = "00", dy = "00";
        //date.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        if (month < 10) {
            mnth = "0" + String.valueOf(month);
            month = Integer.parseInt(mnth);
        } else {
            mnth = String.valueOf(month);
        }
        if (day < 10) {
            dy = "0" + month;
            day = Integer.parseInt(dy);
        } else {
            dy = String.valueOf(day);
        }
        return (new StringBuilder().append(year).append("-")
                .append(mnth).append("-").append(dy));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String mnth = "00", dy = "00";
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            if (month < 10) {
                mnth = "0" + String.valueOf(month);
                month = Integer.parseInt(mnth);
            } else {
                mnth = String.valueOf(month);
            }
            if (day < 10) {
                dy = "0" + month;
                day = Integer.parseInt(dy);
            } else {
                dy = String.valueOf(day);
            }
            // Show selected date
            date.setText(new StringBuilder().append(year)
                    .append("-").append(mnth).append("-").append(dy));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        distributorId = distributorMap.get(adapterView.getItemAtPosition(i).toString());
        // Showing selected spinner item
        //Toast.makeText(AddPurchaseActivity.this, "Selected: " + distributorId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
