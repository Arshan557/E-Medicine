package arshan.com.e_medicine;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Adapters.PurchaseSettledAdapter;
import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.Database.SQLiteDatabaseHandler;
import arshan.com.e_medicine.Models.PurchasesPojo;
import arshan.com.e_medicine.Network.HttpHandler;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class PurchaseSettled extends Fragment {
    private PurchaseSettledAdapter purchaseSettledAdapter;
    private RecyclerView recyclerView;
    private TextView noSettled;
    private List<PurchasesPojo> purchasesPojoList = new ArrayList<>();
    private String TAG = PurchaseSettled.class.getSimpleName(), apikey="";
    public static final String DEFAULT = "";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.purchase_settled, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.settled_recycle);
        noSettled = (TextView) view.findViewById(R.id.no_settled);

        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (null != sharedPreferences) {
            apikey = sharedPreferences.getString("apikey", DEFAULT);
        }

        /*String firstTimeFlag;
        spGetFirstTime = getContext().getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
        firstTimeFlag = spGetFirstTime.getString("PurchaseFirstTimeFlag", "");
        Log.d("PurchaseFirstTimeFlag", firstTimeFlag);
        if (!"N".equalsIgnoreCase(firstTimeFlag)) {
            spGetFirstTime = getContext().getSharedPreferences("FirstTimeFlag", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spGetFirstTime.edit();
            editor.putString("PurchaseFirstTimeFlag", "N");
            editor.commit();

            String finalDistUrl = Constants.PURCHASE_LIST_URL+"?apikey="+apikey;
            Log.d("final url",finalDistUrl);
            purchasesPojoList.clear();
            //Make call to Async
            new getSettledPurchases().execute(finalUrl);
        } else {*/
            // Reading all purchases
            Log.d("Reading: ", "Reading all purchases..");
            List<PurchasesPojo> purchases = db.getAllPurchases();
            purchasesPojoList.clear();
            if (null != purchases) {
                for (int i = 0; i <= purchases.size() - 1; i++) {
                    String log = "IsSettled: " + purchases.get(i).getIsSettled() + " ,Invoice: " + purchases.get(i).getInvoiceNumber();
                    Log.d("settled:purchases: ", log);
                    if ("1".equalsIgnoreCase(purchases.get(i).getIsSettled())) {
                        try {
                            PurchasesPojo purchasesPojo = new PurchasesPojo(purchases.get(i).getDistributorName(), purchases.get(i).getId(), purchases.get(i).getCompanyid(), purchases.get(i).getBillDate(), purchases.get(i).getInvoiceNumber(),
                                    purchases.get(i).getDistributorId(), purchases.get(i).getAmount(), purchases.get(i).getPaymentDate(), purchases.get(i).getPaymentMode(), purchases.get(i).getChequeNumber(),
                                    purchases.get(i).getBankName(), purchases.get(i).getCreatedBy(), purchases.get(i).getCreatedOn(), purchases.get(i).getModifiedBy(), purchases.get(i).getModifiedOn(), purchases.get(i).getIsSettled());
                            purchasesPojoList.add(purchasesPojo);
                        } catch (Exception e) {
                            Log.d("Exception", "" + e.getMessage());
                        }
                    }
                }
            }

        if (purchasesPojoList.isEmpty()) {
            noSettled.setVisibility(View.VISIBLE);
        } else {
            noSettled.setVisibility(View.GONE);
        }

        //Recycle view starts
        purchaseSettledAdapter = new PurchaseSettledAdapter(getContext(), purchasesPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(purchaseSettledAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_purchase_settled);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String finalUrl = Constants.PURCHASE_LIST_URL+"?apikey="+apikey;
                Log.d("final url",finalUrl);
                //Make call to Async
                new getPurchasesList().execute(finalUrl);
            }
        });

        return view;
    }

    private class getPurchasesList extends AsyncTask<String, String, String> {
        String status, msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                        JSONArray category = jsonObj.getJSONArray("purchase");
                        // looping through All News
                        purchasesPojoList.clear();
                        for (int i = 0; i < category.length(); i++) {
                            JSONObject c = category.getJSONObject(i);

                            String DistributorName = c.getString("DistributorName");
                            String id = c.getString("id");
                            String companyId = c.getString("companyId");
                            String BillDate = c.getString("BillDate");
                            String InvoiceNumber = c.getString("InvoiceNumber");
                            String DistributorId = c.getString("DistributorId");
                            String Amount = c.getString("Amount");
                            String PaymentDate = c.getString("PaymentDate");
                            String PaymentMode = c.getString("PaymentMode");
                            String ChequeNumber = c.getString("ChequeNumber");
                            String BankName = c.getString("BankName");
                            String createdBy = c.getString("createdBy");
                            String createdOn = c.getString("createdOn");
                            String modifiedBy = c.getString("modifiedBy");
                            String modifiedOn = c.getString("modifiedOn");
                            String isSettled = c.getString("isSettled");

                            PurchasesPojo purchasesPojo = new PurchasesPojo(DistributorName, id, companyId, BillDate, InvoiceNumber, DistributorId, Amount,
                                    PaymentDate, PaymentMode, ChequeNumber, BankName, createdBy, createdOn, modifiedBy, modifiedOn, isSettled);
                            purchasesPojoList.add(purchasesPojo);

                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getContext());
                            db.addPurchase(new PurchasesPojo(DistributorName, id, companyId, BillDate, InvoiceNumber, DistributorId, Amount,
                                    PaymentDate, PaymentMode, ChequeNumber, BankName, createdBy, createdOn, modifiedBy, modifiedOn, isSettled));
                        }
                    } else {
                        msg = jsonObj.getString("msg");
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (purchasesPojoList.isEmpty()) {
                noSettled.setVisibility(View.VISIBLE);
            } else {
                noSettled.setVisibility(View.GONE);
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (null != msg  && !"ok".equalsIgnoreCase(status)) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
            /**
             * Updating parsed JSON data into ListView
             * */
            //purchaseSettledAdapter.notifyDataSetChanged();
        }
    }
}

