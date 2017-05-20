package arshan.com.e_medicine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import arshan.com.e_medicine.Adapters.DrawerAdapter;
import arshan.com.e_medicine.Models.DrawerPojo;
import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerFragment extends Fragment implements DrawerAdapter.ClickListener{

    private RecyclerView recyclerView;
    private DrawerAdapter myAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private LinearLayout profile;
    private TextView profileName;
    private CircleImageView profilePhoto;
    public static final String DEFAULT = "";
    String fname = "", lname = "", profilePic = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        fname = sharedPreferences.getString("fname", DEFAULT);
        lname = sharedPreferences.getString("lname", DEFAULT);
        profilePic = sharedPreferences.getString("profilePic", DEFAULT);

        profileName = (TextView) layout.findViewById(R.id.profileName);
        profilePhoto = (CircleImageView) layout.findViewById(R.id.profilePic);

        profileName.setText(fname+" "+lname);
        Glide.with(NavigationDrawerFragment.this).load(profilePic).into(profilePhoto);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        profile = (LinearLayout) layout.findViewById(R.id.containerDrawerImage);
        myAdapter = new DrawerAdapter(getActivity(), getData());
        myAdapter.setClickListener(this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                Log.d("profile","profile updating");
            }
        });
        return layout;
    }

    public static List<DrawerPojo> getData() {
        List<DrawerPojo> data = new ArrayList<>();
        int[] icons = {R.drawable.home, R.drawable.calculator, R.drawable.settings, R.drawable.about, R.drawable.contact, R.drawable.faq};
        String[] titles = {"Home", "Calculator", "Settings", "About us", "Contact us","FAQs"};
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            DrawerPojo current = new DrawerPojo();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
            //3rd type navigation drawer(fade action bar)
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //((MainActivity) getActivity()).onDrawerSlide(slideOffset);
                /*if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }*/
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();

            }
        });
    }

    @Override
    public void itemClicked(View view, int position) {
        if (position == 0) {
            mDrawerLayout.closeDrawers();
        } else if (position == 1) {
            Log.d("SDK",android.os.Build.VERSION.SDK);
            if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 15){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(getActivity(), "Opening Calculator", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
            ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();
            final PackageManager pm = getContext().getPackageManager();
            List<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                if( pi.packageName.toString().toLowerCase().contains("calcul")){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("appName", pi.applicationInfo.loadLabel(pm));
                    map.put("packageName", pi.packageName);
                    items.add(map);
                }
            }
            if(items.size()>=1){
                String packageName = (String) items.get(0).get("packageName");
                Intent i = pm.getLaunchIntentForPackage(packageName);
                if (i != null)
                    Toast.makeText(getActivity(), "Opening Calculator", Toast.LENGTH_LONG).show();
                    startActivity(i);
            }
            else{
                Toast.makeText(getActivity(), "Calculator not found", Toast.LENGTH_LONG).show();
            }
        } else if (position == 2) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            //Toast.makeText(getActivity(), "Settings", Toast.LENGTH_LONG).show();
        } else if (position == 3) {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
            //Toast.makeText(getActivity(), "About us", Toast.LENGTH_LONG).show();
        } else if (position == 4) {
            startActivity(new Intent(getActivity(), ContactUsActivity.class));
            //Toast.makeText(getActivity(), "Contact us", Toast.LENGTH_LONG).show();
        } else if (position == 5) {
            startActivity(new Intent(getActivity(), FaqActivity.class));
            //Toast.makeText(getActivity(), "FAQs", Toast.LENGTH_LONG).show();
        }
    }
    }

