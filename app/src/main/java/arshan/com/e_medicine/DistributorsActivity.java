package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.DistributorAdapter;
import arshan.com.e_medicine.Adapters.FeedbackAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.DistributorPojo;
import arshan.com.e_medicine.Models.FeedbackPojo;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Scanner.IntentIntegrator;

public class DistributorsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DistributorAdapter distributorAdapter;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private List<DistributorPojo> distributorPojoList = new ArrayList<>();
    public static final int progress_bar_type = 0;
    private String TAG = DistributorsActivity.class.getSimpleName(), apikey="";
    private ProgressDialog pDialog;
    public static final String DEFAULT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributors);
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

        recyclerView = (RecyclerView) findViewById(R.id.distributor_recycle);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.distributorCoordinate);

        //Recycle view starts
        distributorAdapter = new DistributorAdapter(getApplicationContext(), distributorPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(distributorAdapter);

        String finalUrl = Constants.DISTRIBUTORS_URL+"?apikey="+apikey;
        Log.d("final url",finalUrl);
        //Make call to Async
        new getDistributors().execute(finalUrl);
    }

    private class getDistributors extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DistributorsActivity.this);
            pDialog.setMessage("Loading Distributors...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... f_url) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(f_url[0]);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String status = jsonObj.getString("status");
                    Log.d("status",status);
                    if ("ok".equalsIgnoreCase(status)) {
                        // Getting JSON Array node
                        JSONArray distributors = jsonObj.getJSONArray("distributors");
                        // looping through All News
                        for (int i = 0; i < distributors.length(); i++) {
                            JSONObject c = distributors.getJSONObject(i);

                            String id = c.getString("id");
                            String companyid = c.getString("companyid");
                            String name = c.getString("name");
                            String email = c.getString("email");
                            String uname = c.getString("uname");
                            String password = c.getString("password");
                            String mobile = c.getString("mobile");
                            String isActive = c.getString("isActive");
                            String picURL = c.getString("picURL");
                            Log.d("response", name + "," + picURL );

                            URL url = new URL(picURL);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            DistributorPojo distributorsPojo = new DistributorPojo(name, bmp, isActive);
                            distributorPojoList.add(distributorsPojo);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "MalformedURLException " + e.getMessage());
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            distributorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_distributor, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.w("myApp", "onQueryTextSubmit::"+query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.w("myApp", "onQueryTextChange::"+newText);
                        distributorAdapter.getFilter().filter(newText);
                        recyclerView.invalidate();
                        return true;
                    }
                });
        return true;
    }
}
