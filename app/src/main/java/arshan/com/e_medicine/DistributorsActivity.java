package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.DistributorAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.DistributorPojo;
import arshan.com.e_medicine.Models.DistributorsSQLite;
import arshan.com.e_medicine.Network.HttpHandler;

public class DistributorsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DistributorAdapter distributorAdapter;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private List<DistributorPojo> distributorPojoList = new ArrayList<>();
    private String TAG = DistributorsActivity.class.getSimpleName(), apikey="";
    private ProgressDialog pDialog;
    public static final String DEFAULT = "";
    Bitmap bmp;
    SharedPreferences spGetFirstTime;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);

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

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fabDist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DistributorsActivity.this, AddDistributorActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_distributors);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String finalUrl = Constants.DISTRIBUTORS_URL+"?apikey="+apikey;
                Log.d("final url",finalUrl);
                distributorPojoList.clear();
                //Make call to Async
                new getDistributors().execute(finalUrl);
            }
        });

        String firstTimeFlag;
        spGetFirstTime = getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
        firstTimeFlag = spGetFirstTime.getString("DistributorFirstTimeFlag", "");
        Log.d("firstTimeFlag", firstTimeFlag);
        if (!"N".equalsIgnoreCase(firstTimeFlag)) {

            spGetFirstTime = getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spGetFirstTime.edit();
            editor.putString("DistributorFirstTimeFlag", "N");
            editor.commit();

            String finalUrl = Constants.DISTRIBUTORS_URL+"?apikey="+apikey;
            Log.d("final url",finalUrl);
            //Make call to Async
            new getDistributors().execute(finalUrl);
        } else {
            /*try {
                String picURL = "http://www.provo2.com/health-fitness/wp-content/uploads/2010/11/default-avatar.jpg";
                URL url = new URL(picURL);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.d("Exception", "" + e.getMessage());
            }*/

            // Reading all distributors
            Log.d("Reading: ", "Reading all distributors..");
            List<DistributorsSQLite> distributors = db.getAllDistributors();

            for (int i = 0; i <= distributors.size()-1; i++) {
                String log = "Id: "+distributors.get(i).getId()+" ,companyid: " + distributors.get(i).getCompanyid();
                Log.d("product: ", log);
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(distributors.get(i).getImageByteArray() , 0, distributors.get(i).getImageByteArray().length);

                    DistributorPojo distributorPojo = new DistributorPojo(distributors.get(i).getName(), bitmap, distributors.get(i).getIsActive(),
                            distributors.get(i).getId(), distributors.get(i).getCompanyid(), distributors.get(i).getEmail(), distributors.get(i).getUname(), distributors.get(i).getPassword(),
                            distributors.get(i).getMobile(), distributors.get(i).getPhone(), distributors.get(i).getCreatedBy(), distributors.get(i).getModifiedBy(), distributors.get(i).getCreatedOn(), distributors.get(i).getModifiedOn(), distributors.get(i).getPicURL());
                    distributorPojoList.add(distributorPojo);
                } catch (Exception e) {
                    Log.d("Exception", ""+e.getMessage());
                }
            }
        }
    }

    private class getDistributors extends AsyncTask<String, String, String> {
        String status, msg = "";

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
                            String phone = c.getString("phone");
                            String mobile = c.getString("mobile");
                            String isActive = c.getString("isActive");
                            String picURL = c.getString("picURL");
                            String createdBy = c.getString("createdBy");
                            String modifiedBy = c.getString("modifiedBy");
                            String createdOn = c.getString("createdOn");
                            String modifiedOn = c.getString("modifiedOn");

                            //Log.d("response", name + "," + picURL );
                            URL url = null;
                            if (null != picURL && !"".equalsIgnoreCase(picURL)) {
                                url = new URL(picURL);
                            } else {
                                picURL = "http://www.provo2.com/health-fitness/wp-content/uploads/2010/11/default-avatar.jpg";
                                url = new URL(picURL);
                            }
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            DistributorPojo distributorsPojo = new DistributorPojo(name, bmp, isActive, id, companyid, email,
                                    uname, password, mobile, phone, createdBy, modifiedBy, createdOn, modifiedOn, picURL);
                            distributorPojoList.add(distributorsPojo);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(DistributorsActivity.this);
                            db.addDistributor(new DistributorsSQLite(id, companyid, name, email, uname, password, phone, mobile, isActive, picURL, createdBy, modifiedBy, createdOn, modifiedOn, stream.toByteArray()));
                        }
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(DistributorsActivity.this, Home.class);
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
                        Intent i = new Intent(DistributorsActivity.this, Home.class);
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

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (null != msg && !"".equalsIgnoreCase(msg)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent i = new Intent(DistributorsActivity.this, Home.class);
                startActivity(i);
                finish();
            }

            distributorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_distributor, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
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
                            distributorAdapter = new DistributorAdapter(DistributorsActivity.this, distributorPojoList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(distributorAdapter);
                        }
                        distributorAdapter.getFilter().filter(newText);
                        recyclerView.invalidate();
                        return true;
                    }
                });

        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String finalUrl = Constants.DISTRIBUTORS_URL+"?apikey="+apikey;
                Log.d("final url",finalUrl);
                distributorPojoList.clear();
                //Make call to Async
                new getDistributors().execute(finalUrl);
                return false;
            }
        });
        return true;
    }
}
