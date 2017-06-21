package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Models.DistNameAmountMapPojo;
import arshan.com.e_medicine.Models.PurchasesPojo;
import arshan.com.e_medicine.R;
import arshan.com.e_medicine.Views.CustomProgressDialog;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class OutstandingBillAdapter extends RecyclerView.Adapter<OutstandingBillAdapter.OutstandingBillViewHolder> {
    private List<PurchasesPojo> outstandingBillList = new ArrayList<>();
    private OutstandingBillClickListener outstandingBillClickListener;
    private CustomProgressDialog customProgressDialog;
    PurchasesPojo purchasesPojo;
    DistNameAmountMapPojo distNameAmountMapPojo;
    private Context context;
    private List<DistNameAmountMapPojo> distNameAmountMapPojosList = new ArrayList<>();
    OutstandingBillViewHolder holder;

    private static final String TAG = "OutstandingBillAdapter";

    public class OutstandingBillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView distributor, amount;
        Typeface cat_names_font = Typeface.createFromAsset(context.getAssets(), "categorynamefont.otf");

        public OutstandingBillViewHolder(View view) {
            super(view);
            distributor = (TextView) view.findViewById(R.id.outstanding_distributor);
            amount = (TextView) view.findViewById(R.id.outstanding_amount);

            distributor.setTypeface(cat_names_font);

            view.setOnClickListener(this);
            distributor.setOnClickListener(this);
            amount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*PurchasesPojo purchasesPojo = outstandingBillList.get(getPosition());
            Intent i = new Intent(context, ViewSettledPurchaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", purchasesPojo.getId());
            bundle.putString("companyId", purchasesPojo.getCompanyid());
            bundle.putString("BillDate", purchasesPojo.getBillDate());
            bundle.putString("InvoiceNumber", purchasesPojo.getInvoiceNumber());
            bundle.putString("DistributorId", purchasesPojo.getDistributorId());
            bundle.putString("Amount", purchasesPojo.getAmount());
            bundle.putString("PaymentDate", purchasesPojo.getPaymentDate());
            bundle.putString("PaymentMode", purchasesPojo.getPaymentMode());
            bundle.putString("ChequeNumber", purchasesPojo.getChequeNumber());
            bundle.putString("BankName", purchasesPojo.getBankName());
            bundle.putString("createdBy", purchasesPojo.getCreatedBy());
            bundle.putString("createdOn", purchasesPojo.getCreatedOn());
            bundle.putString("modifiedBy", purchasesPojo.getModifiedBy());
            bundle.putString("modifiedOn", purchasesPojo.getModifiedOn());
            bundle.putString("isSettled", purchasesPojo.getIsSettled());
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/

            if (outstandingBillClickListener != null) {
                outstandingBillClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public OutstandingBillAdapter(Context context, List<DistNameAmountMapPojo> distNameAmountMapPojosList) {
        this.context=context;
        this.outstandingBillList = outstandingBillList;
        this.distNameAmountMapPojosList = distNameAmountMapPojosList;
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
                final ArrayList<PurchasesPojo> results = new ArrayList<PurchasesPojo>();
                if (constraint != null) {
                    if (outstandingBillList != null && outstandingBillList.size() > 0) {
                        for (final PurchasesPojo purchase : outstandingBillList) {
                            if (purchase.getInvoiceNumber().toLowerCase().contains(constraint.toString()))
                                results.add(purchase);
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
                outstandingBillList = (ArrayList<PurchasesPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setClickListener(OutstandingBillClickListener outstandingBillClickListener) {
        this.outstandingBillClickListener = outstandingBillClickListener;
    }

    @Override
    public OutstandingBillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outstanding_bill_row, parent, false);

        return new OutstandingBillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutstandingBillViewHolder holder, int position) {
        //final PurchasesPojo purchasesPojo = outstandingBillList.get(position);
        distNameAmountMapPojo = distNameAmountMapPojosList.get(position);
        Log.d("size",""+distNameAmountMapPojo.getAmount());

        this.holder = holder;
        holder.distributor.setText(distNameAmountMapPojo.getName());
        holder.amount.setText(distNameAmountMapPojo.getAmount());

    }

    @Override
    public int getItemCount() {
        //Log.d("size",""+settledPurchaseList.size());
        return outstandingBillList.size();
    }

    public interface OutstandingBillClickListener {
        public void itemClicked(View view, int position);
    }
}
