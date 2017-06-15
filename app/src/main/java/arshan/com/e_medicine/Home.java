package arshan.com.e_medicine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout sales, distributors, categories, purchase, users, products, feedback, mainLinear;
    boolean doubleBackToExitPressedOnce = false;
    NavigationDrawerFragment drawerFragment;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CALL_PHONE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);
        boolean mobileNwInfo = false;

        //Checking internet connection
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            mobileNwInfo = conMgr.getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {
            mobileNwInfo = false;
        }
        if (mobileNwInfo == false) {
            Snackbar snackbar = Snackbar
                    .make(mainLinear, "Plz enable WiFi/Mobile data", Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        }

        boolean hasPermissionToGallery = (ContextCompat.checkSelfPermission(Home.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionTCall = (ContextCompat.checkSelfPermission(Home.this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermissionToGallery) {
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    REQUEST_GALLERY);
        }
        if (!hasPermissionTCall) {
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
        }

        Bundle data = getIntent().getExtras();
        if(null != data) {
            Log.d("data", "" + data.getString("fname") + "," + data.getString("lname") + "," + data.getString("apikey") + "," + data.getString("profilePic"));
        }

        sales = (LinearLayout) findViewById(R.id.sales);
        distributors = (LinearLayout) findViewById(R.id.distributors);
        categories = (LinearLayout) findViewById(R.id.categories);
        purchase = (LinearLayout) findViewById(R.id.purchase);
        users = (LinearLayout) findViewById(R.id.users);
        products = (LinearLayout) findViewById(R.id.products);
        feedback = (LinearLayout) findViewById(R.id.feedback);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, SalesActivity.class));
            }
        });

        distributors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, DistributorsActivity.class));
            }
        });

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, CategoriesActivity.class));
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, PurchaseActivity.class));
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, UsersActivity.class));
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ProductsActivity.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, FeedbackActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(mainLinear, "Plz click BACK button again to exit", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if(id == R.id.action_rate) {
            //  startActivity(new Intent(MainActivity.this, RateMe.class));
            android.app.FragmentManager manager = getFragmentManager();
            RateMe dailogFragment = new RateMe();
            dailogFragment.show(manager,"dailogFrag");
        } else if(id == R.id.action_logout) {
            SharedPreferences preferences =getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(Home.this, LoginActivity.class));
            finish();
        }*/
        return super.onOptionsItemSelected(item);
    }
}
