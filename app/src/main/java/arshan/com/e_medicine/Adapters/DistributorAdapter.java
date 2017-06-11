package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.EditDistributorActivity;
import arshan.com.e_medicine.Models.DistributorPojo;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arshan on 29-May-2017.
 */
public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorViewHolder> {

    private List<DistributorPojo> distributorsList = new ArrayList<>();;
    private DistributorsClickListener distributorsClickListener;
    private Context context;
    private static final String TAG = "DistributorAdapter";

    public class DistributorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView distributorName;
        public CircleImageView distributorPic;
        public Switch  simpleSwitch;
        private String apikey;

        public DistributorViewHolder(View view) {
            super(view);
            distributorName = (TextView) view.findViewById(R.id.distributorName);
            distributorPic = (CircleImageView) view.findViewById(R.id.distributorPic);
            simpleSwitch = (Switch) view.findViewById(R.id.activeSwitch);

            // Getting data from Shared preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            if (null != sharedPreferences) {
                apikey = sharedPreferences.getString("apikey", "");
            }

            view.setOnClickListener(this);
            distributorName.setOnClickListener(this);
            distributorPic.setOnClickListener(this);
            simpleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DistributorPojo distributorPojo = distributorsList.get(getPosition());

                    Log.d("switch",distributorPojo.getActive()+","+distributorPojo.getId()+","+apikey);
                    if ("1".equalsIgnoreCase(distributorPojo.getActive()))  distributorPojo.setActive("0");
                    else distributorPojo.setActive("1");
                    String finalUrl = Constants.CHANGE_ACTIVATION_URL+"?DID="+distributorPojo.getId()+"&status="+distributorPojo.getActive()+"&apikey="+apikey;
                    Log.d("final url",finalUrl);
                    new changeActivationStatus().execute(finalUrl);
                }
            });
        }

        @Override
        public void onClick(View v) {
            DistributorPojo distributorPojo = distributorsList.get(getPosition());
            Intent i = new Intent(context, EditDistributorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", distributorPojo.getName());
            bundle.putString("email", distributorPojo.getEmail());
            bundle.putString("uname", distributorPojo.getUname());
            bundle.putString("mobile", distributorPojo.getMobile());
            bundle.putString("phone", distributorPojo.getPhone());
            bundle.putString("picUrl", distributorPojo.getPicUrl());
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            if (distributorsClickListener != null) {
                distributorsClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public DistributorAdapter(Context context, List<DistributorPojo> distributorsList) {
        this.context=context;
        this.distributorsList = distributorsList;
    }

    /**
     * This method used to filter serach items
     * @return filter
     */
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<DistributorPojo> results = new ArrayList<DistributorPojo>();
                if (distributorsList == null)
                    distributorsList = distributorsList;
                if (constraint != null) {
                    if (distributorsList != null && distributorsList.size() > 0) {
                        for (final DistributorPojo distri : distributorsList) {
                            if (distri.getName().toLowerCase().contains(constraint.toString()))
                                results.add(distri);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                distributorsList = (ArrayList<DistributorPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public DistributorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_distributors, parent, false);

        return new DistributorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DistributorViewHolder holder, int position) {
        final DistributorPojo distributor = distributorsList.get(position);

            holder.distributorName.setText(distributor.getName());
            holder.distributorPic.setImageBitmap(distributor.getPic());
            if ("1".equalsIgnoreCase(distributor.getActive()))
                holder.simpleSwitch.setChecked(true);
            else
                holder.simpleSwitch.setChecked(false);
    }

    private class changeActivationStatus extends AsyncTask<String, String, String> {
        String msg = "Error occured";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            /*pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }
        @Override
        protected String doInBackground(String... f_url) {
            HttpHandler sh = new HttpHandler();

            String cookie;
            SharedPreferences sharedPreferencesCookie = context.getSharedPreferences("CookieData", Context.MODE_PRIVATE);
            cookie = sharedPreferencesCookie.getString("cookieString", "");
            if (null == cookie || cookie.equalsIgnoreCase("")) {
                //Toast.makeText(getApplicationContext(),"Cookie empty", Toast.LENGTH_LONG).show();
            }
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(f_url[0],cookie);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String status = jsonObj.getString("status");
                    Log.d("status",status);
                    msg = jsonObj.getString("msg");
                    if ("ok".equalsIgnoreCase(status)) {
                        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    } else if ("error".equalsIgnoreCase(status)) {
                        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    if (null != msg) {
                        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                //Toast.makeText(context,"Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
        }

    }

    public void setClickListener(DistributorsClickListener distributorsClickListener) {
        this.distributorsClickListener = distributorsClickListener;
    }


    @Override
    public int getItemCount() {
        return distributorsList.size();
    }

    public interface DistributorsClickListener {
        public void itemClicked(View view, int position);
    }
}
