package arshan.com.e_medicine;

/**
 * Created by Arshan on 11-Oct-2016.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;

public class DailogAddCategory extends DialogFragment {
    Button add,cancel;
    EditText catName;
    TextView header;
    String DEFAULT = "", apikey = "", msg="";
    private CustomProgressDialog customProgressDialog;
    Context context;
    private String TAG = DistributorsActivity.class.getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailog_add_category,null);
        add = (Button) view.findViewById(R.id.btnAdd);
        cancel = (Button) view.findViewById(R.id.btnCancel);
        catName = (EditText) view.findViewById(R.id.cat_name);
        header = (TextView) view.findViewById(R.id.header);
        context = view.getContext();

        Typeface nav_items_font = Typeface.createFromAsset(getActivity().getAssets(), "nav_items.ttf");
        header.setTypeface(nav_items_font);

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (null != sharedPreferences) {
            apikey = sharedPreferences.getString("apikey", DEFAULT);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != catName.getText().toString() && !"".equalsIgnoreCase(catName.getText().toString())) {
                    String finalUrl = Constants.ADD_CATEGORIES_URL+"?&apikey="+apikey+"&name="+catName.getText().toString();
                    Log.d("final url",finalUrl);
                    new addCategory().execute(finalUrl);
                    dismiss();
                    startActivity(new Intent(getActivity(), CategoriesActivity.class));
                } else {
                    Toast.makeText(getActivity(),"Enter category name",Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        setCancelable(true);
        return view;
    }

    private class addCategory extends AsyncTask<String, String, String> {
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(getActivity());
        }
        @Override
        protected String doInBackground(String... f_url) {
            HttpHandler sh = new HttpHandler();

            String cookie;
            SharedPreferences sharedPreferencesCookie = getActivity().getSharedPreferences("CookieData", Context.MODE_PRIVATE);
            cookie = sharedPreferencesCookie.getString("cookieString", "");
            if (null == cookie || cookie.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(),"Cookie empty", Toast.LENGTH_LONG).show();
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
                    Log.d("msg",msg);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
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
            if (null != msg && !"".equalsIgnoreCase(msg)) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}