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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.UsersPojo;
import arshan.com.e_medicine.Network.HttpHandler;

public class UsersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String TAG = UsersActivity.class.getSimpleName(), apikey="";
    private ProgressDialog pDialog;
    public static final String DEFAULT = "";
    private List<UsersPojo> usersPojoList = new ArrayList<>();
    SharedPreferences spGetFirstTime;
    private int[] tabIcons = {
            R.drawable.admin,
            R.drawable.user
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
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
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(UsersActivity.this);

        String firstTimeFlag;
        spGetFirstTime = getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
        firstTimeFlag = spGetFirstTime.getString("UsersFirstTimeFlag", "");
        Log.d("UsersFirstTimeFlag", firstTimeFlag);
        if (!"N".equalsIgnoreCase(firstTimeFlag)) {
            String finalUrl = Constants.USERS_LIST_URL + "?apikey=" + apikey;
            Log.d("final url", finalUrl);
            usersPojoList.clear();
            //Make call to Async
            new getUsersList().execute(finalUrl);
        } else {
            viewPager = (ViewPager) findViewById(R.id.usersViewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.usersTabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
        }
    }

    //Tabs with icons
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Admins(), "Admins");
        adapter.addFragment(new Users(), "Users");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class getUsersList extends AsyncTask<String, String, String> {
        String status,msg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UsersActivity.this);
            pDialog.setMessage("Loading Users...");
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
                    if ("ok".equalsIgnoreCase(status)) {
                        // Getting JSON Array node
                        JSONArray category = jsonObj.getJSONArray("users");
                        // looping through All News
                        for (int i = 0; i < category.length(); i++) {
                            JSONObject c = category.getJSONObject(i);

                            String id = c.getString("id");
                            String fname = c.getString("fname");
                            String lname = c.getString("lname");
                            String uname = c.getString("uname");
                            String password = c.getString("password");
                            String gender = c.getString("gender");
                            String email = c.getString("email");
                            String mobile = c.getString("mobile");
                            String phone = c.getString("phone");
                            String usertype = c.getString("usertype");
                            String apikey = c.getString("apikey");
                            String addressId = c.getString("addressId");
                            String profilePic = c.getString("profilePic");
                            String companyid = c.getString("companyid");
                            String createdBy = c.getString("createdBy");
                            String createdOn = c.getString("createdOn");
                            String modifiedBy = c.getString("modifiedBy");
                            String modifiedOn = c.getString("modifiedOn");
                            String isActive = c.getString("isActive");

                            Log.d("UserActivity:response", fname + "," + profilePic + "," + usertype);
                            URL url = null;

                            String picURL = "http://www.provo2.com/health-fitness/wp-content/uploads/2010/11/default-avatar.jpg";
                            url = new URL(picURL);

                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            /*UsersPojo usersPojo = new UsersPojo(id, fname, lname, uname, password, gender, email,
                                    mobile, phone,usertype, apikey, addressId, profilePic, companyid, createdBy, createdOn, modifiedBy, modifiedOn, isActive, bmp);
                            usersPojoList.add(usersPojo);*/

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(UsersActivity.this);
                            db.addUser(new UsersPojo(id, fname, lname, uname, password, gender, email,
                                    mobile, phone, usertype, apikey, addressId, profilePic, companyid, createdBy, createdOn, modifiedBy, modifiedOn, isActive, stream.toByteArray()));

                        }
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                    Log.d("status",status);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UsersActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(UsersActivity.this, Home.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(UsersActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(UsersActivity.this, Home.class);
                    startActivity(i);
                    finish();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsersActivity.this,
                                "Something went wrong. Please try again",
                                Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(UsersActivity.this, Home.class);
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
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (null != msg && !"ok".equalsIgnoreCase(status)) {
                Toast.makeText(UsersActivity.this, msg, Toast.LENGTH_LONG).show();
                Intent i = new Intent(UsersActivity.this, Home.class);
                startActivity(i);
                finish();
            } else if ("ok".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status)) {
                spGetFirstTime = getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spGetFirstTime.edit();
                editor.putString("UsersFirstTimeFlag", "N");
                editor.commit();

                viewPager = (ViewPager) findViewById(R.id.usersViewpager);
                setupViewPager(viewPager);

                tabLayout = (TabLayout) findViewById(R.id.usersTabs);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        MenuItem addUser = menu.findItem(R.id.action_adduser);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        addUser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(UsersActivity.this, AddUserActivity.class);
                startActivity(i);
                return false;
            }
        });

        /*searchView.setOnQueryTextListener(
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
                            categoriesAdapter = new CategoriesAdapter(PurchaseActivity.this, );
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(categoriesAdapter);
                        }
                        categoriesAdapter.getFilter().filter(newText);
                        recyclerView.invalidate();
                        return true;
                    }
                });*/

        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String finalUrl = Constants.USERS_LIST_URL + "?apikey=" + apikey;
                Log.d("final url", finalUrl);
                usersPojoList.clear();
                //Make call to Async
                new getUsersList().execute(finalUrl);
                return false;
            }
        });
        return true;
    }
}
