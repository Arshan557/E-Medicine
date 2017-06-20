package arshan.com.e_medicine;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.outstanding,
            R.drawable.settled2,
            R.drawable.unsettled2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        viewPager = (ViewPager) findViewById(R.id.purchaseViewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.purchaseTabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    //Tabs with icons
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OutstandingBill(), "Outstanding");
        adapter.addFragment(new PurchaseSettled(), "Settled");
        adapter.addFragment(new PurchaseUnsettled(), "Unsettled");
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_purchase, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        MenuItem addBill = menu.findItem(R.id.action_addbill);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        addBill.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(PurchaseActivity.this, AddPurchaseActivity.class);
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
        });*/
        return true;
    }
}
