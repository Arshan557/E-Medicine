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

public class EditUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    Bundle data = null;
    public static final String DEFAULT = "N/A";
    private String TAG = EditUserActivity.class.getSimpleName(), apikey="";
    private EditText user_name, user_uname, user_mail, user_mobile, user_phone;
    private CircleImageView user_pic;
    private CustomProgressDialog customProgressDialog;
    com.shamanland.fab.FloatingActionButton fabSave;
    com.shamanland.fab.FloatingActionButton fabEdit;
    com.shamanland.fab.FloatingActionButton fabCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
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
            Log.d("data",data.getString("fname") + "," + data.getString("email")+","+data.getString("uname"));
        }
        getSupportActionBar().setTitle(data.getString("fname"));

        user_name = (EditText) findViewById(R.id.user_name);
        user_uname = (EditText) findViewById(R.id.user_uname);
        user_mail = (EditText) findViewById(R.id.user_email);
        user_mobile = (EditText) findViewById(R.id.user_mobile);
        user_phone = (EditText) findViewById(R.id.user_phone);
        fabSave = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.user_save);
        fabEdit = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.user_edit);
        fabCall = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.user_call);
        user_pic = (CircleImageView) findViewById(R.id.user_pic);

        if (null != data.getString("fname")) user_name.setText(data.getString("fname"));
        else user_name.setText(data.getString(DEFAULT));
        if (null != data.getString("uname")) user_uname.setText(data.getString("uname"));
        else user_uname.setText(data.getString(DEFAULT));
        if (null != data.getString("email")) user_mail.setText(data.getString("email"));
        else user_mail.setText(data.getString(DEFAULT));
        if (null != data.getString("mobile")) user_mobile.setText(data.getString("mobile"));
        else user_mobile.setText(data.getString(DEFAULT));
        if (null != data.getString("phone")) user_phone.setText(data.getString("phone"));
        else user_phone.setText(data.getString(DEFAULT));
        if (null != data.getString("profilePic"))
            Glide.with(EditUserActivity.this).load(data.getString("profilePic")).into(user_pic);
        else
            user_pic.setImageResource(R.drawable.defaultpic);

        user_name.setEnabled(false);
        user_uname.setEnabled(false);
        user_mobile.setEnabled(false);
        user_mail.setEnabled(false);
        user_pic.setEnabled(false);
        user_phone.setEnabled(false);
        fabSave.setVisibility(View.GONE);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name.setEnabled(true);
                user_uname.setEnabled(false);
                user_mobile.setEnabled(true);
                user_phone.setEnabled(true);
                user_mail.setEnabled(false);
                user_pic.setEnabled(true);
                fabEdit.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name.setEnabled(false);
                user_uname.setEnabled(false);
                user_mobile.setEnabled(false);
                user_phone.setEnabled(false);
                user_mail.setEnabled(false);
                user_pic.setEnabled(false);
                fabEdit.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.GONE);

                if (!(user_name.getText().toString().equalsIgnoreCase(data.getString("fname"))
                        && user_uname.getText().toString().equalsIgnoreCase(data.getString("uname"))
                        && user_mobile.getText().toString().equalsIgnoreCase(data.getString("mobile"))
                        && user_phone.getText().toString().equalsIgnoreCase(data.getString("phone"))
                        && user_mail.getText().toString().equalsIgnoreCase(data.getString("email")))) {

                    String finalUrl = Constants.EDIT_DISTRIBUTOR_URL+"?name="+user_name.getText().toString()+"&mobile="+user_mobile.getText().toString()
                            +"&phone="+user_phone.getText().toString()+"&apikey="+apikey+"&id="+data.getString("id");
                    Log.d("final url",finalUrl);
                    //Make call to Async
                    new editUser().execute(finalUrl);
                } else {
                    Toast.makeText(EditUserActivity.this,"Nothing has changed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                //builder.setTitle("Sample Alert");
                builder.setMessage("Do you want to make call to "+user_name.getText().toString()+"?");
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
                                Toast.makeText(EditUserActivity.this,"Calling "+user_name.getText().toString(),Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91"+user_mobile.getText().toString()));
                                try {
                                    startActivity(in);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getApplicationContext(), "Sorry! Unable to call", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();
            }
        });
    }

    private class editUser extends AsyncTask<String, String, String> {
        String status,msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(EditUserActivity.this);
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
                            Intent i = new Intent(EditUserActivity.this, UsersActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EditUserActivity.this, UsersActivity.class);
                    startActivity(i);
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
                        Intent i = new Intent(EditUserActivity.this, UsersActivity.class);
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
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            Intent i = new Intent(EditUserActivity.this, UsersActivity.class);
            startActivity(i);
            finish();


        }
    }
}
