package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddProductActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText productName, barCode, mfd, price, quantity, measure, stock, maxDiscount, expDate, batch, color;
    private CircleImageView pic;
    public static final String DEFAULT = "";
    private String apikey = "", TAG = AddProductActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
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

        productName = (EditText) findViewById(R.id.prodName);
        barCode = (EditText) findViewById(R.id.barCode);
        mfd = (EditText) findViewById(R.id.mfd);
        price = (EditText) findViewById(R.id.mrp);
        quantity = (EditText) findViewById(R.id.quantity);
        measure = (EditText) findViewById(R.id.measure);
        stock = (EditText) findViewById(R.id.stock);
        maxDiscount = (EditText) findViewById(R.id.maxD);
        expDate = (EditText) findViewById(R.id.expDate);
        batch = (EditText) findViewById(R.id.batch);
        color = (EditText) findViewById(R.id.color);

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fabAddProduct);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(productName.getText().toString().isEmpty() || barCode.getText().toString().isEmpty() || mfd.getText().toString().isEmpty()
                        || price.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || measure.getText().toString().isEmpty()
                || stock.getText().toString().isEmpty() || maxDiscount.getText().toString().isEmpty() || expDate.getText().toString().isEmpty()
                        || batch.getText().toString().isEmpty() || color.getText().toString().isEmpty())) {


                    String finalUrl = Constants.PRODUCT_ADD_URL
                            + "?name=" + productName.getText().toString()
                            + "&barcode=" + barCode.getText().toString()
                            + "&MFD=" + mfd.getText().toString()
                            + "&MRP=" + price.getText().toString()
                            + "&qty=" + quantity.getText().toString()
                            + "&measure=" + measure.getText().toString()
                            + "&stock=" + stock.getText().toString()
                            + "&MaxD=" + maxDiscount.getText().toString()
                            + "&EXP=" + expDate.getText().toString()
                            + "&batch=" + batch.getText().toString()
                            + "&img=http://www.privaledge.net/pricing-strategy/wp-content/uploads/2013/09/13986273-new-product.jpg"
                            + "&color=" + color.getText().toString()
                            + "&apikey=" + apikey
                            + "&i=1";
                    Log.d("final url", finalUrl);
                    //Make call to Async
                    new addProduct().execute(finalUrl);
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all the details", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class addProduct extends AsyncTask<String, String, String> {
        String status;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddProductActivity.this);
            pDialog.setMessage("Adding Product...");
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
                            Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
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
                        Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
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
            if ("success".equalsIgnoreCase(status)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
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
