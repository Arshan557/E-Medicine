package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Constants.Constants;
import arshan.com.e_medicine.EditDistributorActivity;
import arshan.com.e_medicine.Models.DistributorPojo;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.R;
import arshan.com.e_medicine.Views.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arshan on 29-May-2017.
 */
public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorViewHolder> {

    private List<DistributorPojo> distributorsList = new ArrayList<>();;
    private DistributorsClickListener distributorsClickListener;
    private CustomProgressDialog customProgressDialog;
    DistributorPojo distributorPojo;
    private Context context;
    DistributorViewHolder holder;

    private static final String TAG = "DistributorAdapter";

    public class DistributorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView distributorName;
        public CircleImageView distributorPic;
        public Switch  simpleSwitch;
        private String apikey;
        //com.shamanland.fab.FloatingActionButton fabCallOutSide;

        public DistributorViewHolder(View view) {
            super(view);
            distributorName = (TextView) view.findViewById(R.id.distributorName);
            distributorPic = (CircleImageView) view.findViewById(R.id.distributorPic);
            simpleSwitch = (Switch) view.findViewById(R.id.activeSwitch);
            //fabCallOutSide = (com.shamanland.fab.FloatingActionButton) view.findViewById(R.id.dist_call_outside);

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
                    final View v = view;
                   distributorPojo = distributorsList.get(getPosition());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    //builder.setTitle("Sample Alert");
                    if ("1".equalsIgnoreCase(distributorPojo.getActive())) builder.setMessage("Do you want to deactivate "+distributorPojo.getName()+"?");
                    else builder.setMessage("Do you want to activate "+distributorPojo.getName()+"?");

                    builder.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getContext(),"No is clicked",Toast.LENGTH_LONG).show();
                                    if (simpleSwitch.isChecked()) simpleSwitch.setChecked(false);
                                    else simpleSwitch.setChecked(true);
                                }
                            });
                    builder.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //Toast.makeText(getContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                    Log.d("switch",distributorPojo.getActive()+","+distributorPojo.getId()+","+apikey);
                                    if ("1".equalsIgnoreCase(distributorPojo.getActive()))  distributorPojo.setActive("0");
                                    else distributorPojo.setActive("1");
                                    String finalUrl = Constants.CHANGE_ACTIVATION_URL+"?DID="+distributorPojo.getId()+"&status="+distributorPojo.getActive()+"&apikey="+apikey;
                                    Log.d("final url",finalUrl);
                                    new changeActivationStatus(v).execute(finalUrl);
                                }
                            });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            if (simpleSwitch.isChecked()) simpleSwitch.setChecked(false);
                            else simpleSwitch.setChecked(true);
                        }
                    });
                    builder.show();

                }
            });

            /*fabCallOutSide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View v = view;
                    distributorPojo = distributorsList.get(getPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    //builder.setTitle("Sample Alert");
                    builder.setMessage("Do you want to make call to "+distributorPojo.getName()+"?");
                    builder.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getContext(),"No is clicked",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                    Toast.makeText(v.getContext(),"Calling "+distributorPojo.getName(),Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91"+distributorPojo.getMobile()));
                                    try {
                                        v.getContext().startActivity(in);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(v.getContext(), "your Activity is not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    builder.show();
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            DistributorPojo distributorPojo = distributorsList.get(getPosition());
            Intent i = new Intent(context, EditDistributorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", distributorPojo.getId());
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
            this.holder = holder;
            holder.distributorName.setText(distributor.getName());
            holder.distributorPic.setImageBitmap(distributor.getPic());
            if ("1".equalsIgnoreCase(distributor.getActive()))
                holder.simpleSwitch.setChecked(true);
            else
                holder.simpleSwitch.setChecked(false);
    }

    private class changeActivationStatus extends AsyncTask<String, String, String> {
        String status = "", msg = "Error occured";
        private WeakReference vRef;
        View v;

        public changeActivationStatus (View v) {
            vRef = new WeakReference(v);
            this.v = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog = CustomProgressDialog.show(v.getContext());

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
                    status = jsonObj.getString("status");
                    Log.d("status",status);
                    msg = jsonObj.getString("msg");
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
            customProgressDialog.cancel();
            if ("ok".equalsIgnoreCase(status)) {
                if ("1".equalsIgnoreCase(distributorPojo.getActive())) Toast.makeText(context, distributorPojo.getName()+" activated", Toast.LENGTH_LONG).show();
                else Toast.makeText(context, distributorPojo.getName()+" deactivated", Toast.LENGTH_LONG).show();
            } else if ("error".equalsIgnoreCase(status)) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                if (holder.simpleSwitch.isChecked()) holder.simpleSwitch.setChecked(false);
                else holder.simpleSwitch.setChecked(true);
            }
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
