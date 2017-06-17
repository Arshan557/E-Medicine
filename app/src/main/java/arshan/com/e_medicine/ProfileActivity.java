package arshan.com.e_medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText fname, lname, mail, mbl, phn;
    private CircleImageView pic;
    String firstname, lastname, profilePic, email, mobile, phone, apiKey, id = "";
    public static final String DEFAULT = "N/A";
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static final int GALLERY = 1;
    private CustomProgressDialog customProgressDialog;
    private String TAG = DistributorsActivity.class.getSimpleName();
    com.shamanland.fab.FloatingActionButton fabSave;
    com.shamanland.fab.FloatingActionButton fabEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
            firstname = sharedPreferences.getString("fname", DEFAULT);
            lastname = sharedPreferences.getString("lname", DEFAULT);
            profilePic = sharedPreferences.getString("profilePic", DEFAULT);
            email = sharedPreferences.getString("email", DEFAULT);
            mobile = sharedPreferences.getString("mobile", DEFAULT);
            phone = sharedPreferences.getString("phone", DEFAULT);
            apiKey = sharedPreferences.getString("apikey", DEFAULT);
            id = sharedPreferences.getString("id", DEFAULT);
        }

        fname = (EditText) findViewById(R.id.first_name);
        lname = (EditText) findViewById(R.id.last_name);
        mail = (EditText) findViewById(R.id.email);
        mbl = (EditText) findViewById(R.id.mobile);
        phn = (EditText) findViewById(R.id.phone);
        fabSave = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.save);
        fabEdit = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.edit);
        pic = (CircleImageView) findViewById(R.id.pic);

        fname.setText(firstname);
        lname.setText(lastname);
        mail.setText(email);
        mbl.setText(mobile);
        phn.setText(phone);
        if (null != profilePic)
            Glide.with(ProfileActivity.this).load(profilePic).into(pic);
        else
            pic.setImageResource(R.drawable.defaultpic);

        fname.setEnabled(false);
        lname.setEnabled(false);
        mbl.setEnabled(false);
        phn.setEnabled(false);
        mail.setEnabled(false);
        pic.setEnabled(false);
        fabSave.setVisibility(View.GONE);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname.setEnabled(true);
                lname.setEnabled(true);
                mbl.setEnabled(true);
                phn.setEnabled(true);
                mail.setEnabled(false);
                pic.setEnabled(true);
                fabEdit.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname.setEnabled(false);
                lname.setEnabled(false);
                mbl.setEnabled(false);
                phn.setEnabled(false);
                mail.setEnabled(false);
                pic.setEnabled(false);
                fabEdit.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.GONE);

                if (!(firstname.equalsIgnoreCase(fname.getText().toString())
                        && lastname.equalsIgnoreCase(lname.getText().toString())
                        && mobile.equalsIgnoreCase(mbl.getText().toString())
                        && phone.equalsIgnoreCase(phn.getText().toString()))) {
                    String finalUrl = Constants.PROFILE_EDIT_URL+"?fname="+fname.getText().toString()+"&lname="+lname.getText().toString()
                            +"&phone="+phn.getText().toString()+"&mobile="+mbl.getText().toString()+"&id="+id+"&apikey="+apiKey;
                    Log.d("final url", finalUrl);
                    //Make call to Async
                    new editProfile().execute(finalUrl);
                } else {
                    Toast.makeText(ProfileActivity.this,"No changes found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            Uri mImageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    pic.setImageBitmap(Image);
                Toast.makeText(ProfileActivity.this,"Profile pic has been changed",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.d("FileNotFoundException",e.getLocalizedMessage());
            } catch (IOException e) {
                Log.d("IOException",e.getLocalizedMessage());
            }
        }
    }

    private class editProfile extends AsyncTask<String, String, String> {
        String status,msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(ProfileActivity.this);
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
        }
    }
}
