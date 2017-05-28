package arshan.com.e_medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import arshan.com.e_medicine.Constants.Constants;
import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerFragment extends Fragment{

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private LinearLayout profile, calcLinear, contactUsLinear, changePwdLinear, rateLinear, aboutUsLinear, logoutLinear, aboutGroup;
    private TextView profileName, tc, contactDev, faq, help;
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
        if (null != sharedPreferences) {
            fname = sharedPreferences.getString("fname", DEFAULT);
            lname = sharedPreferences.getString("lname", DEFAULT);
            profilePic = sharedPreferences.getString("profilePic", DEFAULT);
        }

        profileName = (TextView) layout.findViewById(R.id.profileName);
        profilePhoto = (CircleImageView) layout.findViewById(R.id.profilePic);

        profileName.setText(fname+" "+lname);
        if (null != profilePic) {
            Glide.with(NavigationDrawerFragment.this).load(profilePic).into(profilePhoto);
        }

        //recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        profile = (LinearLayout) layout.findViewById(R.id.containerDrawerImage);
        calcLinear = (LinearLayout) layout.findViewById(R.id.calcLinear);
        contactUsLinear = (LinearLayout) layout.findViewById(R.id.contactUsLinear);
        changePwdLinear = (LinearLayout) layout.findViewById(R.id.changePwdLinear);
        rateLinear = (LinearLayout) layout.findViewById(R.id.rateLinear);
        aboutUsLinear = (LinearLayout) layout.findViewById(R.id.aboutUsLinear);
        logoutLinear = (LinearLayout) layout.findViewById(R.id.logoutLinear);
        aboutGroup = (LinearLayout) layout.findViewById(R.id.aboutGroup);
        faq =  (TextView) layout.findViewById(R.id.faq);
        tc =  (TextView) layout.findViewById(R.id.tc);
        contactDev =  (TextView) layout.findViewById(R.id.contactDev);
        help =  (TextView) layout.findViewById(R.id.help);

        aboutGroup.setVisibility(View.GONE);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                Log.d("profile","profile updating");
            }
        });
        calcLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                Log.d("SDK",android.os.Build.VERSION.SDK);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    /*intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(new ComponentName("com.android.calculator2", "com.android.calculator2.Calculator"));*/
                    startActivity(intent);
            }
        });
        contactUsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(getActivity(), ContactUsActivity.class));
            }
        });
        changePwdLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                mDrawerLayout.closeDrawers();
            }
        });
        rateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_LONG).show();
            }
        });
        aboutUsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aboutGroup.getVisibility() == View.GONE) {
                    aboutGroup.setVisibility(View.VISIBLE);
                } else {
                    aboutGroup.setVisibility(View.GONE);
                }
                /*mDrawerLayout.closeDrawers();
                startActivity(new Intent(getActivity(), AboutUsActivity.class));*/
            }
        });
        logoutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                SharedPreferences preferences =getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                i.putExtra("postUrl", Constants.FAQ_URL);
                i.putExtra("label", "FAQs");
                startActivity(i);
            }
        });
        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                i.putExtra("postUrl", Constants.ABOUT_URL);
                i.putExtra("label", "Terms & Conditions");
                startActivity(i);
            }
        });
        return layout;
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
}

