package arshan.com.e_medicine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Database.SQLiteDatabaseHandler;
import arshan.com.e_medicine.Models.DistributorPojo;
import arshan.com.e_medicine.Models.DistributorsSQLite;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;

public class PurchaseSettledUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private EditText chequeNum, paymentDate, otherBank;
    private ImageView dateIcon;
    private Button btnSettledUp;
    private Spinner paymentListSpinner, bankListSpinner;
    private DatePicker datePicker;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    public static final String DEFAULT = "", TAG = "PurchaseSettledUp";
    private CustomProgressDialog customProgressDialog;
    private String apikey = "", bankListString = "HDFC", paymentListString = "Cash";
    static final int DATE_PICKER_ID = 1111;
    List<String> paymentList = new ArrayList<String>();
    List<String> bankList = new ArrayList<String>();
    ArrayList<String> invoice_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_settled_up);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        invoice_list = getIntent().getStringArrayListExtra("invoice_list");
        Log.d("invoice_list",""+invoice_list);

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (null != sharedPreferences) {
            apikey = sharedPreferences.getString("apikey", DEFAULT);
        }

        chequeNum = (EditText) findViewById(R.id.cheque_num);
        paymentDate = (EditText) findViewById(R.id.paymentDate);
        otherBank = (EditText) findViewById(R.id.otherBank);
        dateIcon = (ImageView) findViewById(R.id.dateIcon);
        btnSettledUp = (Button) findViewById(R.id.btn_settledUp);
        paymentListSpinner = (Spinner) findViewById(R.id.spinnerPaymentList);
        bankListSpinner = (Spinner) findViewById(R.id.spinnerBank);

        // Spinner click listener
        paymentListSpinner.setOnItemSelectedListener(PurchaseSettledUpActivity.this);
        bankListSpinner.setOnItemSelectedListener(PurchaseSettledUpActivity.this);

        paymentList.add("Select payment mode");
        paymentList.add("Cash");
        paymentList.add("Cheque");

        bankList.add("Choose bank");
        bankList.add("SBI");
        bankList.add("HDFC");
        bankList.add("Kotak");
        bankList.add("ICICI");
        bankList.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> bankListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, bankList);
        bankListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankListSpinner.setAdapter(bankListAdapter);

        ArrayAdapter<String> paymentListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, paymentList);
        paymentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentListSpinner.setAdapter(paymentListAdapter);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        paymentDate.setText(showDate(year, month+1, day));

        dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
            }
        });

        if (null != invoice_list && null != apikey) {
            final StringBuilder invList = new StringBuilder();
            for (int i = 0; i < invoice_list.size(); i++) {
                invList.append("'").append(invoice_list.get(i)).append("'").append(",");
                if ( i == invoice_list.size()-1) {
                    invList.deleteCharAt(invList.length()-1);
                }
            }
            btnSettledUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String finalUrl = Constants.PURCHASE_SETTLE_URL
                            + "?apikey=" + apikey
                            + "&ids=" + invList
                            + "&payment=" + paymentListString
                            + "&checkno=" + chequeNum.getText().toString()
                            + "&bank=" + bankListString
                            + "&paymentDate=" + paymentDate.getText().toString()
                            + "&i="+"1";
                    Log.d("final url", finalUrl);
                    //Make call to Async
                    new settlePurchases().execute(finalUrl);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            Intent i = new Intent(PurchaseSettledUpActivity.this, PurchaseActivity.class);
            startActivity(i);
            finish();
        }
    }

    private class settlePurchases extends AsyncTask<String, String, String> {
        String status, msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(PurchaseSettledUpActivity.this);
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
                    Log.d("status",status);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(PurchaseSettledUpActivity.this, PurchaseActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(PurchaseSettledUpActivity.this, PurchaseActivity.class);
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
            if (customProgressDialog.isShowing()) customProgressDialog.cancel();
            if (null != msg && "ok".equalsIgnoreCase(status)) {
                Toast.makeText(PurchaseSettledUpActivity.this, ""+msg, Toast.LENGTH_LONG).show();
            } else if ((!"ok".equalsIgnoreCase(status) || null == status) && null != msg) {
                Toast.makeText(PurchaseSettledUpActivity.this, msg, Toast.LENGTH_LONG).show();
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
        return (new StringBuilder().append(year).append("/")
                .append(mnth).append("/").append(dy));
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
            paymentDate.setText(new StringBuilder().append(year)
                    .append("/").append(mnth).append("/").append(dy));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        //Toast.makeText(PurchaseSettledUpActivity.this, "Selected: " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
        if ("Cash".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) paymentListString = "Cash";
        else if ("Cheque".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) paymentListString = "Cheque";
        else if ("SBI".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) bankListString = "SBI";
        else if ("HDFC".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) bankListString = "HDFC";
        else if ("Kotak".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) bankListString = "Kotak";
        else if ("ICICI".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) bankListString = "ICICI";
        else if ("Other".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) bankListString = "Other";

        if ("other".equalsIgnoreCase(bankListString)) {
            otherBank.setVisibility(View.VISIBLE);
        } else {
            otherBank.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
