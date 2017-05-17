package arshan.com.e_medicine.Adapters;

/**
 * Created by Arshan on 10-Oct-2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Models.FeedbackPojo;
import arshan.com.e_medicine.Models.ProductsPojo;
import arshan.com.e_medicine.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<FeedbackPojo> feedbackList;
    private FeedbackClickListener feedbackClickListener;
    private Context context;


    public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView feedName, feedMail, feedComments, feedCreated;

        public FeedbackViewHolder(View view) {
            super(view);
            feedName = (TextView) view.findViewById(R.id.name_feedback);
            feedMail = (TextView) view.findViewById(R.id.email_feedback);
            feedComments = (TextView) view.findViewById(R.id.comments_feedback);
            feedCreated = (TextView) view.findViewById(R.id.created_feedback);

            feedName.setOnClickListener(this);
            feedMail.setOnClickListener(this);
            feedComments.setOnClickListener(this);
            feedCreated.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (feedbackClickListener != null) {
                feedbackClickListener.itemClicked(v, getPosition());
            }
        }
    }


    public FeedbackAdapter(Context context, List<FeedbackPojo> feedbackList) {
        this.context=context;
        this.feedbackList = feedbackList;
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
                final ArrayList<FeedbackPojo> results = new ArrayList<FeedbackPojo>();
                if (feedbackList == null)
                    feedbackList = feedbackList;
                if (constraint != null) {
                    if (feedbackList != null && feedbackList.size() > 0) {
                        for (final FeedbackPojo feed : feedbackList) {
                            if (feed.getName().toLowerCase().contains(constraint.toString()))
                                results.add(feed);
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
                feedbackList = (ArrayList<FeedbackPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_feedback, parent, false);

        return new FeedbackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        FeedbackPojo feedbackPojo = feedbackList.get(position);
        holder.feedName.setText(feedbackPojo.getName());
        holder.feedMail.setText(feedbackPojo.getMail());
        holder.feedComments.setText(feedbackPojo.getComments());
        holder.feedCreated.setText(feedbackPojo.getCreated());
    }

    public void setClickListener(FeedbackClickListener feedbackClickListener) {
        this.feedbackClickListener = feedbackClickListener;
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public interface FeedbackClickListener {
        public void itemClicked(View view, int position);
    }
}