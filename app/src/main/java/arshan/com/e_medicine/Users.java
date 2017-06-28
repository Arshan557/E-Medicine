package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.UsersAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Models.UsersPojo;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.Views.CustomProgressDialog;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class Users extends Fragment {
    private UsersAdapter usersAdapter;
    private RecyclerView recyclerView;
    private List<UsersPojo> usersPojoList = new ArrayList<>();
    private String TAG = Users.class.getSimpleName(), apikey="";
    private ProgressDialog pDialog;
    public static final String DEFAULT = "";
    SharedPreferences spGetFirstTime;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomProgressDialog customProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.users, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.users_recycle);

        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (null != sharedPreferences) {
            apikey = sharedPreferences.getString("apikey", DEFAULT);
        }

        /*com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) view.findViewById(R.id.fabUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddUserActivity.class);
                startActivity(intent);
            }
        });*/

        /*String firstTimeFlag;
        spGetFirstTime = getContext().getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
        firstTimeFlag = spGetFirstTime.getString("UsersFirstTimeFlag", "");
        Log.d("UsersFirstTimeFlag", firstTimeFlag);
        if (!"N".equalsIgnoreCase(firstTimeFlag)) {
            String finalUrl = Constants.USERS_LIST_URL+"?apikey="+apikey;
            Log.d("final url",finalUrl);
            usersPojoList.clear();
            //Make call to Async
            new getUsersList().execute(finalUrl);
        } else {*/
            // Reading all users
            Log.d("Reading: ", "Reading all users..");
                List<UsersPojo> user = db.getAllUsers();
                if (null != user) {
                    usersPojoList.clear();
                    for (int i = 0; i <= user.size() - 1; i++) {
                        String log = "Fname: " + user.get(i).getFname() + " ,Lname: " + user.get(i).getLname() + "type: " + user.get(i).getUsertype();
                        Log.d("users: ", log);
                        try {
                            if ("3".equalsIgnoreCase(user.get(i).getUsertype())) {
                                //Log.d("userType: ", user.get(i).getUsertype());
                                Bitmap bitmap = BitmapFactory.decodeByteArray(user.get(i).getImageByteArray(), 0, user.get(i).getImageByteArray().length);

                                UsersPojo usersPojo = new UsersPojo(user.get(i).getId(), user.get(i).getFname(), user.get(i).getLname(), user.get(i).getUname(),
                                        user.get(i).getPassword(), user.get(i).getGender(), user.get(i).getEmail(), user.get(i).getMobile(), user.get(i).getPhone(),
                                        user.get(i).getUsertype(), user.get(i).getApikey(), user.get(i).getAddressId(), user.get(i).getProfilePic(), user.get(i).getCompanyid(), user.get(i).getCreatedBy(),
                                        user.get(i).getCreatedOn(), user.get(i).getModifiedBy(), user.get(i).getModifiedOn(), user.get(i).getIsActive(), bitmap);
                                usersPojoList.add(usersPojo);
                            }
                        } catch (Exception e) {
                            Log.d("Exception", "" + e.getMessage());
                        }
                    }
                }

        //Recycle view starts
        usersAdapter = new UsersAdapter(getContext(), usersPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(usersAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_users);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String finalUrl = Constants.USERS_LIST_URL+"?apikey="+apikey;
                Log.d("final url",finalUrl);
                usersPojoList.clear();
                //Make call to Async
                new getUsersList().execute(finalUrl);
            }
        });

        return view;
    }

    private class getUsersList extends AsyncTask<String, String, String> {
        String status,msg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //customProgressDialog = CustomProgressDialog.show(getContext());
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

                            Log.d("Users:response", fname + "," + profilePic + "," + usertype);
                            URL url = null;

                            String picURL = "http://www.provo2.com/health-fitness/wp-content/uploads/2010/11/default-avatar.jpg";
                            url = new URL(picURL);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            UsersPojo usersPojo = new UsersPojo(id, fname, lname, uname, password, gender, email,
                                    mobile, phone, usertype, apikey, addressId, profilePic, companyid, createdBy, createdOn, modifiedBy, modifiedOn, isActive, bmp);
                            usersPojoList.add(usersPojo);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getContext());
                            db.addUser(new UsersPojo(id, fname, lname, uname, password, gender, email,
                                    mobile, phone, usertype, apikey, addressId, profilePic, companyid, createdBy, createdOn, modifiedBy, modifiedOn, isActive, stream.toByteArray()));

                        }
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                    Log.d("status",status);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getContext(), Home.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getContext(), Home.class);
                    startActivity(i);
                    getActivity().finish();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Something went wrong. Please try again",
                                Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(getContext(), Home.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            //customProgressDialog.cancel();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            /*if ("ok".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status)){
                spGetFirstTime = getContext().getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spGetFirstTime.edit();
                editor.putString("UsersFirstTimeFlag", "N");
                editor.commit();
            }*/
            if (null != msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}

