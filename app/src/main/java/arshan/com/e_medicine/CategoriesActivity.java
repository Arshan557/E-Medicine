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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.CategoriesAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.CategoriesPojo;
import arshan.com.e_medicine.Models.CategoriesSQLite;
import arshan.com.e_medicine.Network.HttpHandler;

public class CategoriesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CategoriesAdapter categoriesAdapter;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noCats;
    private List<CategoriesPojo> categoryPojoList = new ArrayList<>();
    private String TAG = CategoriesActivity.class.getSimpleName(), apikey="";
    private ProgressDialog pDialog;
    public static final String DEFAULT = "";
    SharedPreferences spGetFirstTime;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
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

        recyclerView = (RecyclerView) findViewById(R.id.category_recycle);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.distributorCoordinate);
        noCats = (TextView) findViewById(R.id.no_cats);

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fabCat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.FragmentManager manager = getFragmentManager();
                DailogAddCategory dailogFragment = new DailogAddCategory();
                dailogFragment.show(manager,"dailogFrag");
            }
        });

        String firstTimeFlag;
        spGetFirstTime = getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
        firstTimeFlag = spGetFirstTime.getString("CategoryFirstTimeFlag", "");
        Log.d("firstTimeFlag", firstTimeFlag);
        if (!"N".equalsIgnoreCase(firstTimeFlag)) {
            String finalUrl = Constants.CATEGORIES_URL+"?apikey="+apikey;
            Log.d("final url",finalUrl);
            //Make call to Async
            new getCategories().execute(finalUrl);
        } else {
            // Reading all categories
            Log.d("Reading: ", "Reading all categories..");
            List<CategoriesSQLite> categories = db.getAllCategories();
            if (null != categoryPojoList) {
                categoryPojoList.clear();
                for (int i = 0; i <= categories.size() - 1; i++) {
                    String log = "Id: " + categories.get(i).getId() + " ,name: " + categories.get(i).getName();
                    Log.d("category: ", log);
                    try {
                        CategoriesPojo categoriesPojo = new CategoriesPojo(categories.get(i).getId(), categories.get(i).getCompanyid(), categories.get(i).getName(), categories.get(i).getCreatedBy(),
                                categories.get(i).getCreatedOn(), categories.get(i).getModifiedBy(), categories.get(i).getModifiedOn());
                        categoryPojoList.add(categoriesPojo);
                    } catch (Exception e) {
                        Log.d("Exception", "" + e.getMessage());
                    }
                }
            }
        }

        if (categoryPojoList.isEmpty()) {
            noCats.setVisibility(View.VISIBLE);
        }

        //Recycle view starts
        categoriesAdapter = new CategoriesAdapter(CategoriesActivity.this, categoryPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoriesAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_categories);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String finalUrl = Constants.CATEGORIES_URL+"?apikey="+apikey;
                Log.d("final url",finalUrl);
                categoryPojoList.clear();
                //Make call to Async
                new getCategories().execute(finalUrl);
            }
        });
    }

    private class getCategories extends AsyncTask<String, String, String> {
        String status, msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CategoriesActivity.this);
            pDialog.setMessage("Loading Categories...");
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
                        JSONArray category = jsonObj.getJSONArray("category");
                        // looping through All News
                        for (int i = 0; i < category.length(); i++) {
                            JSONObject c = category.getJSONObject(i);

                            String id = c.getString("id");
                            String companyId = c.getString("companyId");
                            String name = c.getString("name");
                            String createdBy = c.getString("createdBy");
                            String createdOn = c.getString("createdOn");
                            String modifiedBy = c.getString("modifiedBy");
                            String modifiedOn = c.getString("modifiedOn");

                            CategoriesPojo categoriesPojo = new CategoriesPojo(id, companyId, name, createdBy, createdOn, modifiedBy, modifiedOn);
                            categoryPojoList.add(categoriesPojo);

                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(CategoriesActivity.this);
                            db.addCategory(new CategoriesSQLite(id, companyId, name, createdBy, createdOn, modifiedBy, modifiedOn));
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
                            Intent intent = new Intent(CategoriesActivity.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Intent intent = new Intent(CategoriesActivity.this, Home.class);
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
                        Intent intent = new Intent(CategoriesActivity.this, Home.class);
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
            if (categoryPojoList.isEmpty()) {
                noCats.setVisibility(View.VISIBLE);
            }
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if ("ok".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status)){
                spGetFirstTime = getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spGetFirstTime.edit();
                editor.putString("CategoryFirstTimeFlag", "N");
                editor.commit();
            }
            if (null != msg && !"".equalsIgnoreCase(msg)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CategoriesActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
            /**
             * Updating parsed JSON data into ListView
             * */

            categoriesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
                            categoriesAdapter = new CategoriesAdapter(CategoriesActivity.this, categoryPojoList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(categoriesAdapter);
                        }
                        categoriesAdapter.getFilter().filter(newText);
                        recyclerView.invalidate();
                        return true;
                    }
                });

        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String finalUrl = Constants.CATEGORIES_URL+"?apikey="+apikey;
                Log.d("final url",finalUrl);
                categoryPojoList.clear();
                //Make call to Async
                new getCategories().execute(finalUrl);
                return false;
            }
        });
        return true;
    }
}
