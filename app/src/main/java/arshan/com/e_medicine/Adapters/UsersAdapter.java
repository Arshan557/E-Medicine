package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import arshan.com.e_medicine.EditUserActivity;
import arshan.com.e_medicine.Models.UsersPojo;
import arshan.com.e_medicine.Network.HttpHandler;
import arshan.com.e_medicine.R;
import arshan.com.e_medicine.Views.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private List<UsersPojo> usersPojoList = new ArrayList<>();;
    private UsersClickListener usersClickListener;
    private CustomProgressDialog customProgressDialog;
    UsersPojo usersPojo;
    private Context context;
    UsersViewHolder holder;

    private static final String TAG = "UsersAdapter";

    public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView userPic;
        TextView userName;
        public Switch simpleSwitch;
        private String apikey;
        Typeface cat_names_font = Typeface.createFromAsset(context.getAssets(), "categorynamefont.otf");

        public UsersViewHolder(View view) {
            super(view);
            userPic = (CircleImageView) view.findViewById(R.id.userPic);
            userName = (TextView) view.findViewById(R.id.userName);
            simpleSwitch = (Switch) view.findViewById(R.id.activeSwitch);

            // Getting data from Shared preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            if (null != sharedPreferences) {
                apikey = sharedPreferences.getString("apikey", "");
            }

            userName.setTypeface(cat_names_font);

            view.setOnClickListener(this);
            userPic.setOnClickListener(this);
            userName.setOnClickListener(this);
            simpleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View v = view;
                    usersPojo = usersPojoList.get(getPosition());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    //builder.setTitle("Sample Alert");
                    if ("1".equalsIgnoreCase(usersPojo.getIsActive())) builder.setMessage("Do you want to deactivate "+usersPojo.getFname()+"?");
                    else builder.setMessage("Do you want to activate "+usersPojo.getFname()+"?");

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
                                    Log.d("switch",usersPojo.getIsActive()+","+usersPojo.getId()+","+apikey);
                                    if ("1".equalsIgnoreCase(usersPojo.getIsActive()))  usersPojo.setIsActive("0");
                                    else usersPojo.setIsActive("1");
                                    String finalUrl = Constants.USERS_ACTIVATION_URL+"?UID="+usersPojo.getId()+"&status="+usersPojo.getIsActive()+"&apikey="+apikey;
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

        }

        @Override
        public void onClick(View v) {
            UsersPojo usersPojo = usersPojoList.get(getPosition());
            Intent i = new Intent(context, EditUserActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", usersPojo.getId());
            bundle.putString("fname", usersPojo.getFname());
            bundle.putString("lname", usersPojo.getLname());
            bundle.putString("uname", usersPojo.getUname());
            bundle.putString("password", usersPojo.getPassword());
            bundle.putString("gender", usersPojo.getGender());
            bundle.putString("email", usersPojo.getEmail());
            bundle.putString("mobile", usersPojo.getMobile());
            bundle.putString("phone", usersPojo.getPhone());
            bundle.putString("usertype", usersPojo.getUsertype());
            bundle.putString("apikey", usersPojo.getApikey());
            bundle.putString("addressId", usersPojo.getAddressId());
            bundle.putString("profilePic", usersPojo.getProfilePic());
            bundle.putString("companyid", usersPojo.getCompanyid());
            bundle.putString("createdBy", usersPojo.getCreatedBy());
            bundle.putString("createdOn", usersPojo.getCreatedOn());
            bundle.putString("modifiedBy", usersPojo.getModifiedBy());
            bundle.putString("modifiedOn", usersPojo.getModifiedOn());
            bundle.putString("isActive", usersPojo.getIsActive());
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            if (usersClickListener != null) {
                usersClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public UsersAdapter(Context context, List<UsersPojo> usersPojoList) {
        this.context=context;
        this.usersPojoList = usersPojoList;
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
                final ArrayList<UsersPojo> results = new ArrayList<UsersPojo>();
                if (constraint != null) {
                    if (usersPojoList != null && usersPojoList.size() > 0) {
                        for (final UsersPojo user : usersPojoList) {
                            if (user.getLname().toLowerCase().contains(constraint.toString()) ||
                                    user.getFname().toLowerCase().contains(constraint.toString()))
                                results.add(user);
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
                usersPojoList = (ArrayList<UsersPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class changeActivationStatus extends AsyncTask<String, String, String> {
        String status, msg;
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
                    msg = jsonObj.getString("msg");
                    Log.d("status and msg",status+"::"+msg);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    customProgressDialog.cancel();
                    Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                    customProgressDialog.cancel();
                    Toast.makeText(context,"Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    if (null != msg) {
                        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                customProgressDialog.cancel();
                Toast.makeText(context,"Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (customProgressDialog.isShowing()) customProgressDialog.cancel();
            if (null != status && "ok".equalsIgnoreCase(status)) {
                if ("1".equalsIgnoreCase(usersPojo.getIsActive())) Toast.makeText(context, usersPojo.getFname()+" activated", Toast.LENGTH_LONG).show();
                else Toast.makeText(context, usersPojo.getFname()+" deactivated", Toast.LENGTH_LONG).show();
            } else if (null != msg && !"ok".equalsIgnoreCase(status)) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                if (holder.simpleSwitch.isChecked()) holder.simpleSwitch.setChecked(false);
                else holder.simpleSwitch.setChecked(true);
            }
        }

    }

    public void setClickListener(UsersClickListener usersClickListener) {
        this.usersClickListener = usersClickListener;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_row, parent, false);

        return new UsersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        final UsersPojo usersPojo = usersPojoList.get(position);
        //Log.d("size",""+purchasesPojo.getInvoiceNumber());
        this.holder = holder;
        holder.userName.setText(usersPojo.getFname()+" "+usersPojo.getLname());
        holder.userPic.setImageBitmap(usersPojo.getPic());
    }

    @Override
    public int getItemCount() {
        //Log.d("size",""+settledPurchaseList.size());
        return usersPojoList.size();
    }

    public interface UsersClickListener {
        public void itemClicked(View view, int position);
    }
}
