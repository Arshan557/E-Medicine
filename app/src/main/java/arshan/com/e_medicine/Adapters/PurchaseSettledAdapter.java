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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Models.PurchasesPojo;
import arshan.com.e_medicine.R;
import arshan.com.e_medicine.ViewSettledPurchaseActivity;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class PurchaseSettledAdapter extends RecyclerView.Adapter<PurchaseSettledAdapter.PurchaseSettledViewHolder> {
    private List<PurchasesPojo> settledPurchaseList = new ArrayList<>();;
    private SettledPurchaseClickListener settledPurchaseClickListener;
    private Context context;
    PurchaseSettledViewHolder holder;

    private static final String TAG = "PurchaseSettledAdapter";

    public class PurchaseSettledViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView invoice, amount;
        LinearLayout linearLayout;
        Typeface cat_names_font = Typeface.createFromAsset(context.getAssets(), "categorynamefont.otf");

        public PurchaseSettledViewHolder(View view) {
            super(view);
            invoice = (TextView) view.findViewById(R.id.settled_invoice);
            amount = (TextView) view.findViewById(R.id.settled_amount);
            linearLayout = (LinearLayout) view.findViewById(R.id.mainLinear);

            invoice.setTypeface(cat_names_font);

            view.setOnClickListener(this);
            invoice.setOnClickListener(this);
            amount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PurchasesPojo purchasesPojo = settledPurchaseList.get(getPosition());
            Intent i = new Intent(context, ViewSettledPurchaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("DistributorName", purchasesPojo.getDistributorName());
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

            if (settledPurchaseClickListener != null) {
                settledPurchaseClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public PurchaseSettledAdapter(Context context, List<PurchasesPojo> settledPurchaseList) {
        this.context=context;
        this.settledPurchaseList = settledPurchaseList;
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
                    if (settledPurchaseList != null && settledPurchaseList.size() > 0) {
                        for (final PurchasesPojo purchase : settledPurchaseList) {
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
                settledPurchaseList = (ArrayList<PurchasesPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setClickListener(SettledPurchaseClickListener settledPurchaseClickListener) {
        this.settledPurchaseClickListener = settledPurchaseClickListener;
    }

    @Override
    public PurchaseSettledViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_settled_row, parent, false);

        return new PurchaseSettledViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PurchaseSettledViewHolder holder, int position) {
        final PurchasesPojo purchasesPojo = settledPurchaseList.get(position);
            this.holder = holder;
            holder.invoice.setText(purchasesPojo.getInvoiceNumber());
            holder.amount.setText(purchasesPojo.getAmount());
    }

    @Override
    public int getItemCount() {
        //Log.d("size",""+settledPurchaseList.size());
        return settledPurchaseList.size();
    }

    public interface SettledPurchaseClickListener {
        public void itemClicked(View view, int position);
    }
}
