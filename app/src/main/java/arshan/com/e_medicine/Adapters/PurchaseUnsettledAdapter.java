package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arshan.com.e_medicine.Models.PurchasesPojo;
import arshan.com.e_medicine.R;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class PurchaseUnsettledAdapter extends RecyclerView.Adapter<PurchaseUnsettledAdapter.PurchaseUnSettledViewHolder> {
    private List<PurchasesPojo> unsettledPurchaseList = new ArrayList<>();;
    private UnsettledPurchaseClickListener unsettledPurchaseClickListener;
    private Context context;
    PurchasesPojo purchasesPojo;
    PurchaseUnSettledViewHolder holder;
    Set<String> invoiceList = new HashSet<>();

    private static final String TAG = "PurchaseUnsettledAdapter";

    public class PurchaseUnSettledViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView invoice, amount;
        public CheckBox checkBox;
        LinearLayout linearLayout;
        Typeface cat_names_font = Typeface.createFromAsset(context.getAssets(), "categorynamefont.otf");

        public PurchaseUnSettledViewHolder(View view) {
            super(view);
            invoice = (TextView) view.findViewById(R.id.unsettled_invoice);
            amount = (TextView) view.findViewById(R.id.unsettled_amount);
            checkBox = (CheckBox) view.findViewById(R.id.unsettled_checkbox);
            linearLayout = (LinearLayout) view.findViewById(R.id.mainLinear);

            invoice.setTypeface(cat_names_font);
            SharedPreferences preferencesInvoiceList = context.getSharedPreferences("invoiceList", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editorInvoiceList = preferencesInvoiceList.edit();

            view.setOnClickListener(this);
            invoice.setOnClickListener(this);
            amount.setOnClickListener(this);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    purchasesPojo = unsettledPurchaseList.get(getPosition());
                    if (checkBox.isChecked()) {
                        invoiceList.add(purchasesPojo.getInvoiceNumber());

                    } else {
                        invoiceList.remove(purchasesPojo.getInvoiceNumber());
                    }
                    Log.d("getInvoiceList",""+invoiceList);
                    editorInvoiceList.clear();
                    editorInvoiceList.commit();
                    editorInvoiceList.putStringSet("invoiceList", invoiceList);
                    editorInvoiceList.commit();
                }
            });
            //Log.d("getInvoiceList",""+purchasesPojo.getInvoiceList());
        }

        @Override
        public void onClick(View v) {
            /*PurchasesPojo purchasesPojo = settledPurchaseList.get(getPosition());
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

            if (unsettledPurchaseClickListener != null) {
                unsettledPurchaseClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public PurchaseUnsettledAdapter(Context context, List<PurchasesPojo> unsettledPurchaseList) {
        this.context=context;
        this.unsettledPurchaseList = unsettledPurchaseList;
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
                    if (unsettledPurchaseList != null && unsettledPurchaseList.size() > 0) {
                        for (final PurchasesPojo purchase : unsettledPurchaseList) {
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
                unsettledPurchaseList = (ArrayList<PurchasesPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setClickListener(UnsettledPurchaseClickListener unsettledPurchaseClickListener) {
        this.unsettledPurchaseClickListener = unsettledPurchaseClickListener;
    }

    @Override
    public PurchaseUnSettledViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_unsettled_row, parent, false);

        return new PurchaseUnSettledViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PurchaseUnSettledViewHolder holder, int position) {
        final PurchasesPojo purchasesPojo = unsettledPurchaseList.get(position);
            this.holder = holder;
            holder.invoice.setText(purchasesPojo.getInvoiceNumber());
            holder.amount.setText(purchasesPojo.getAmount());
    }

    @Override
    public int getItemCount() {
        return unsettledPurchaseList.size();
    }

    public interface UnsettledPurchaseClickListener {
        public void itemClicked(View view, int position);
    }
}
