package arshan.com.e_medicine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditDistributorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText dist_name, dist_uname, dist_mail, dist_mobile, dist_phone;
    private CircleImageView dist_pic;
    public static final String DEFAULT = "N/A";
    Bundle data = null;
    private CustomProgressDialog customProgressDialog;
    private String TAG = DistributorsActivity.class.getSimpleName(), apikey="";
    com.shamanland.fab.FloatingActionButton fabSave;
    com.shamanland.fab.FloatingActionButton fabEdit;
    com.shamanland.fab.FloatingActionButton fabCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_distributor);
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

        data = getIntent().getExtras();
        if (null != data) {
            Log.d("data",data.getString("name") + "," + data.getString("email")+","+data.getString("uname")
                    +","+data.getString("mobile")+","+data.getString("phone")+","+data.getString("picUrl")+","+data.getString("id"));
        }
        getSupportActionBar().setTitle(data.getString("name"));

        dist_name = (EditText) findViewById(R.id.dist_name);
        dist_uname = (EditText) findViewById(R.id.dist_uname);
        dist_mail = (EditText) findViewById(R.id.dist_email);
        dist_mobile = (EditText) findViewById(R.id.dist_mobile);
        dist_phone = (EditText) findViewById(R.id.dist_phone);
        fabSave = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.dist_save);
        fabEdit = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.dist_edit);
        fabCall = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.dist_call);
        dist_pic = (CircleImageView) findViewById(R.id.dist_pic);

        if (null != data.getString("name")) dist_name.setText(data.getString("name"));
        else dist_name.setText(data.getString(DEFAULT));
        if (null != data.getString("uname")) dist_uname.setText(data.getString("uname"));
        else dist_uname.setText(data.getString(DEFAULT));
        if (null != data.getString("email")) dist_mail.setText(data.getString("email"));
        else dist_mail.setText(data.getString(DEFAULT));
        if (null != data.getString("mobile")) dist_mobile.setText(data.getString("mobile"));
        else dist_mobile.setText(data.getString(DEFAULT));
        if (null != data.getString("phone")) dist_phone.setText(data.getString("phone"));
        else dist_phone.setText(data.getString(DEFAULT));
        if (null != data.getString("picUrl"))
            Glide.with(EditDistributorActivity.this).load(data.getString("picUrl")).into(dist_pic);
        else
            dist_pic.setImageResource(R.drawable.defaultpic);

        dist_name.setEnabled(false);
        dist_uname.setEnabled(false);
        dist_mobile.setEnabled(false);
        dist_mail.setEnabled(false);
        dist_pic.setEnabled(false);
        dist_phone.setEnabled(false);
        fabSave.setVisibility(View.GONE);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dist_name.setEnabled(true);
                dist_uname.setEnabled(false);
                dist_mobile.setEnabled(true);
                dist_phone.setEnabled(true);
                dist_mail.setEnabled(false);
                dist_pic.setEnabled(true);
                fabEdit.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dist_name.setEnabled(false);
                dist_uname.setEnabled(false);
                dist_mobile.setEnabled(false);
                dist_phone.setEnabled(false);
                dist_mail.setEnabled(false);
                dist_pic.setEnabled(false);
                fabEdit.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.GONE);

                if (!(dist_name.getText().toString().equalsIgnoreCase(data.getString("name"))
                        && dist_uname.getText().toString().equalsIgnoreCase(data.getString("uname"))
                        && dist_mobile.getText().toString().equalsIgnoreCase(data.getString("mobile"))
                        && dist_phone.getText().toString().equalsIgnoreCase(data.getString("phone"))
                        && dist_mail.getText().toString().equalsIgnoreCase(data.getString("email")))) {

                    String finalUrl = Constants.EDIT_DISTRIBUTOR_URL+"?name="+dist_name.getText().toString()+"&mobile="+dist_mobile.getText().toString()
                            +"&phone="+dist_phone.getText().toString()+"&apikey="+apikey+"&id="+data.getString("id");
                    Log.d("final url",finalUrl);
                    //Make call to Async
                    new editDistributor().execute(finalUrl);
                } else {
                    Toast.makeText(EditDistributorActivity.this,"Nothing has changed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                //builder.setTitle("Sample Alert");
                builder.setMessage("Do you want to make call to "+dist_name.getText().toString()+"?");
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(),"No is clicked",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDistributorActivity.this,"Calling "+dist_name.getText().toString(),Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91"+dist_mobile.getText().toString()));
                                try {
                                    startActivity(in);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getApplicationContext(), "your Activity is not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();
            }
        });
    }

    private class editDistributor extends AsyncTask<String, String, String> {
        String status,msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(EditDistributorActivity.this);
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
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("msg");
                    Log.d("status",status);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong. Please try again",
                                Toast.LENGTH_LONG)
                                .show();
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
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            Intent i = new Intent(EditDistributorActivity.this, DistributorsActivity.class);
            startActivity(i);


        }
    }
}
