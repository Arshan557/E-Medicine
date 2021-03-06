package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.FeedbackAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.FeedbackPojo;
import arshan.com.e_medicine.Network.HttpHandler;

public class FeedbackActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private List<FeedbackPojo> feedbackPojoList = new ArrayList<>();
    private FeedbackAdapter feedbackAdapter;
    public static final int progress_bar_type = 0;
    private String TAG = MainActivity.class.getSimpleName(), apikey, DEFAULT = "";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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

        recyclerView = (RecyclerView) findViewById(R.id.feedback_recycle);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.feedbackCoordinate);

        String finalUrl = Constants.FEEDBACK_URL+"?apikey="+apikey;
        Log.d("final url",finalUrl);
        //Make call to Async
        new GetFeedbacks().execute(finalUrl);

        //Recycle view starts
        feedbackAdapter = new FeedbackAdapter(getApplicationContext(), feedbackPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(feedbackAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
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
                        if (newText.isEmpty()){
                            feedbackAdapter = new FeedbackAdapter(FeedbackActivity.this, feedbackPojoList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(feedbackAdapter);
                        }
                        feedbackAdapter.getFilter().filter(newText);
                        recyclerView.invalidate();
                        return true;
                    }
                });
        return true;
    }

    private class GetFeedbacks extends AsyncTask<String, String, String> {
        String status, msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FeedbackActivity.this);
            pDialog.setMessage("Loading Feedbacks...");
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
                    Log.d("status",status);
                    if ("ok".equalsIgnoreCase(status)) {
                        // Getting JSON Array node
                        JSONArray feedbacks = jsonObj.getJSONArray("feeds");

                        // looping through All News
                        for (int i = 0; i < feedbacks.length(); i++) {
                            JSONObject c = feedbacks.getJSONObject(i);

                            String name = c.getString("Name");
                            String mail = c.getString("Email");
                            String comments = c.getString("Comments");
                            String created = c.getString("CreatedDate");

                            Log.d("response", name + "," + mail + "," + comments + "," + created + "::");

                            FeedbackPojo feedbackPojo = new FeedbackPojo(name, mail, comments, created);
                            feedbackPojoList.add(feedbackPojo);

                        }
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong. Please try again",
                                    Toast.LENGTH_LONG)
                                    .show();
                            Intent i = new Intent(FeedbackActivity.this, Home.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong. Please try again",
                            Toast.LENGTH_LONG)
                            .show();
                    Intent i = new Intent(FeedbackActivity.this, Home.class);
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
                        Intent i = new Intent(FeedbackActivity.this, Home.class);
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (null != msg && !"".equalsIgnoreCase(msg)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FeedbackActivity.this, Home.class);
                startActivity(intent);
                finish();
            }

            feedbackAdapter.notifyDataSetChanged();
        }

    }
}
