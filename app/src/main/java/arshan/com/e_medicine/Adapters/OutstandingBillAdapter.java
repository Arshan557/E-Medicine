package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Models.PurchasesPojo;
import arshan.com.e_medicine.R;
import arshan.com.e_medicine.ViewSettledPurchaseActivity;
import arshan.com.e_medicine.Views.CustomProgressDialog;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class OutstandingBillAdapter extends RecyclerView.Adapter<OutstandingBillAdapter.OutstandingBillViewHolder> {
    private List<PurchasesPojo> outstandingBillList = new ArrayList<>();;
    private OutstandingBillClickListener outstandingBillClickListener;
    private CustomProgressDialog customProgressDialog;
    PurchasesPojo purchasesPojo;
    private Context context;
    OutstandingBillViewHolder holder;

    private static final String TAG = "OutstandingBillAdapter";

    public class OutstandingBillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView invoice;
        Typeface cat_names_font = Typeface.createFromAsset(context.getAssets(), "categorynamefont.otf");

        public OutstandingBillViewHolder(View view) {
            super(view);
            invoice = (TextView) view.findViewById(R.id.settled_invoice);

            invoice.setTypeface(cat_names_font);

            view.setOnClickListener(this);
            invoice.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PurchasesPojo purchasesPojo = outstandingBillList.get(getPosition());
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
            context.startActivity(i);

            if (outstandingBillClickListener != null) {
                outstandingBillClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public OutstandingBillAdapter(Context context, List<PurchasesPojo> outstandingBillList) {
        this.context=context;
        this.outstandingBillList = outstandingBillList;
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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outstanding_bill_row, parent, false);

        return new OutstandingBillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutstandingBillViewHolder holder, int position) {
        final PurchasesPojo purchasesPojo = outstandingBillList.get(position);
        //Log.d("size",""+purchasesPojo.getInvoiceNumber());
        this.holder = holder;
        holder.invoice.setText(purchasesPojo.getInvoiceNumber());
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
