package arshan.com.e_medicine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import arshan.com.e_medicine.EditProductActivity;
import arshan.com.e_medicine.Models.DistributorPojo;
import arshan.com.e_medicine.Models.ProductsPojo;
import arshan.com.e_medicine.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arshan on 29-May-2017.
 */
public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorViewHolder> {

    private List<DistributorPojo> distributorsList = new ArrayList<>();;
    private DistributorsClickListener distributorsClickListener;
    private Context context;

    public class DistributorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView distributorName;
        public CircleImageView distributorPic;
        public Switch  simpleSwitch;

        public DistributorViewHolder(View view) {
            super(view);
            distributorName = (TextView) view.findViewById(R.id.distributorName);
            distributorPic = (CircleImageView) view.findViewById(R.id.distributorPic);
            simpleSwitch = (Switch) view.findViewById(R.id.activeSwitch);

            view.setOnClickListener(this);
            distributorName.setOnClickListener(this);
            distributorPic.setOnClickListener(this);
            simpleSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final DistributorPojo distributorPojo = distributorsList.get(getPosition());
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
