package arshan.com.e_medicine;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.ProductsAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.ProductsPojo;
import arshan.com.e_medicine.Models.ProductsSQLite;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Scanner.IntentIntegrator;
import arshan.com.e_medicine.Scanner.IntentResult;
import arshan.com.e_medicine.Views.CustomProgressDialog;

public class ProductsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private List<ProductsPojo> productsPojoList = new ArrayList<>();
    private ProductsAdapter productsAdapter;
    public static final int progress_bar_type = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private CustomProgressDialog customProgressDialog;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting products
        //Log.d("Insert: ", "Inserting ..");
        //db.addProduct(new ProductsSQLite("3-cnC1Ot", "3-1XRoRH", "8901314010728", "Colgate Strong Teeth Super Shakti Dental Cream", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
       /* db.addProduct(new ProductsSQLite("5-cnC1Oo", "3-1XRoRH", "8901314010728", "Maxfreh", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("6-cnC1Oo", "3-1XRoRH", "8901314010728", "Active salt", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("7-cnC1Oo", "3-1XRoRH", "8901314010728", "Pepsodent", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("8-cnC1Oo", "3-1XRoRH", "8901314010728", "Dabur", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("9-cnC1Oo", "3-1XRoRH", "8901314010728", "Haldiram", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("10-cnC1Oo", "3-1XRoRH", "8901314010728", "Red MI", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("11-cnC1Oo", "3-1XRoRH", "8901314010728", "ASUS", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("12-cnC1Oo", "3-1XRoRH", "8901314010728", "DELL", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
        db.addProduct(new ProductsSQLite("13-cnC1Oo", "3-1XRoRH", "8901314010728", "Anand Bhavan", "2017-02-01", "2018-04-30", "5", "10", "100", "code1", "http:\\/\\/www.ranchibazaar.com\\/1017-thickbox_default\\/colgate-strong-teeth-toothpaste-500-g.jpg"));
*/
        try {
            String imgUrl = "http://www.ranchibazaar.com/1017-thickbox_default/colgate-strong-teeth-toothpaste-500-g.jpg";
            URL url = new URL(imgUrl);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            Log.d("Exception", "" + e.getLocalizedMessage());
        }

        // Reading all products
        Log.d("Reading: ", "Reading all contacts..");
        List<ProductsSQLite> products = db.getAllProducts();

        for (int i=products.size()-1; i >= 0; i--) {
            String log = "Id: "+products.get(i).getId()+" ,companyid: " + products.get(i).getCompanyid() + " ,barcode: " + products.get(i).getBarcode() + " ,itemname: " + products.get(i).getItemname() + " ,mfgdate: " + products.get(i).getMfgdate()
                    + " ,expdate: " + products.get(i).getExpdate() + " ,maxdiscount: " + products.get(i).getMaxdiscount() + " ,qty: " + products.get(i).getQty() + " ,mrp: " + products.get(i).getMrp() + " ,batch: " + products.get(i).getBatch() + " ,productimage: " + products.get(i).getProductimage();
            Log.d("product: ", log);
            try {
                /*URL url = new URL(products.get(i).getProductimage());
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());*/
                ProductsPojo productsPojo = new ProductsPojo(products.get(i).getItemname(), products.get(i).getMfgdate(), bmp,
                        products.get(i).getQty(), products.get(i).getMrp());
                productsPojoList.add(productsPojo);
            } catch (Exception e) {
                Log.d("Exception", ""+e.getMessage());
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.products_recycle);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.productsCoordinate);

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
                    .make(coordinatorLayout, "Plz enable WiFi/Mobile data", Snackbar.LENGTH_LONG)
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

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        //Make call to Async
        //new GetProducts().execute(Constants.PRODUCTS_URL);

        //Recycle view starts
        productsAdapter = new ProductsAdapter(getApplicationContext(), productsPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsAdapter);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(ProductsActivity.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ProductsActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ProductsAdapter productsAdapter = (ProductsAdapter)recyclerView.getAdapter();
                if (true && productsAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ProductsAdapter adapter = (ProductsAdapter)recyclerView.getAdapter();
                boolean undoOn = true;
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Toast.makeText(getApplicationContext(),"Data:"+scanContent+","+scanFormat, Toast.LENGTH_SHORT).show();
            Log.d("bar code results",scanContent+"....."+scanFormat);

            new GetProducts().execute(Constants.BARCODE_SEARCH_URL+"?barcode="+scanContent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            Log.d("no data","No scan data received");
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem barCode = menu.findItem(R.id.action_barcode);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.w("myApp", "onQueryTextSubmit::"+query);
                        new GetProducts().execute(Constants.PRODUCTNAME_SEARCH_URL+"?name="+query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.w("myApp", "onQueryTextChange::"+newText);
                        /*productsAdapter.getFilter().filter(newText);
                        recyclerView.invalidate();*/
                        return true;
                    }
                });

        barCode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),"Scanning...",Toast.LENGTH_LONG).show();
                IntentIntegrator scanIntegrator = new IntentIntegrator(ProductsActivity.this);
                scanIntegrator.initiateScan();
                return false;
            }
        });
        return true;
    }

    private class GetProducts extends AsyncTask<String, String, String> {

        String status, msg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            customProgressDialog = CustomProgressDialog.show(ProductsActivity.this);
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
                        JSONArray products = jsonObj.getJSONArray("product");
                        // looping through All News
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            String id = c.getString("id");
                            String companyid = c.getString("companyid");
                            String barcode = c.getString("barcode");
                            String itemname = c.getString("itemname");
                            String mfgdate = c.getString("mfgdate");
                            String expdate = c.getString("expdate");
                            String maxdiscount = c.getString("maxdiscount");
                            String qty = c.getString("qty");
                            String mrp = c.getString("mrp");
                            String batch = c.getString("batch");
                            String productimage = c.getString("productimage");

                            Log.d("productimage",productimage);
                            URL url = new URL(productimage);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            productsPojoList.clear();
                            ProductsPojo productsPojo = new ProductsPojo(itemname, mfgdate, bmp, qty, mrp);
                            productsPojoList.add(productsPojo);

                            //In
                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ProductsActivity.this);
                            db.addProduct(new ProductsSQLite(id, companyid, barcode, itemname, mfgdate, expdate, maxdiscount, qty, mrp, batch, productimage));

                        }
                    } else {
                        msg = jsonObj.getString("msg");
                        Log.d("status",status);
                        productsPojoList.clear();
                        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ProductsActivity.this);
                        List<ProductsSQLite> products = db.getAllProducts();
                        for (ProductsSQLite pr : products) {
                            ProductsPojo productsPojo = new ProductsPojo(pr.getItemname(), pr.getMfgdate(), bmp, pr.getQty(), pr.getMrp());
                            productsPojoList.add(productsPojo);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong. Try again" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ProductsActivity.this, Home.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ProductsActivity.this, Home.class);
                    startActivity(i);
                    finish();
                } catch (IOException e) {
                    Log.e(TAG, "IOException.. " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ProductsActivity.this, Home.class);
                    startActivity(i);
                    finish();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ProductsActivity.this, Home.class);
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
            if (null != msg)
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            /**
             * Updating parsed JSON data into ListView
             * */

            productsAdapter.notifyDataSetChanged();
        }

    }
}
