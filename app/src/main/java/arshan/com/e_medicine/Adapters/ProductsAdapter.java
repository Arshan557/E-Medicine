package arshan.com.e_medicine.Adapters;

/**
 * Created by Arshan on 10-Oct-2016.
 */
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import arshan.com.e_medicine.EditProductActivity;
import arshan.com.e_medicine.Models.ProductsPojo;
import arshan.com.e_medicine.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<ProductsPojo> productsList = new ArrayList<>();;
    private ProductsClickListener productsClickListener;
    private Context context;
    List<ProductsPojo> itemsPendingRemoval = new ArrayList<>();;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<ProductsPojo, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView newTitle, newsDesc, publishedAt;
        public ImageView bmp;
        public Button undoButton;

        public ProductViewHolder(View view) {
            super(view);
            newTitle = (TextView) view.findViewById(R.id.news_title);
            newsDesc = (TextView) view.findViewById(R.id.news_desc);
            bmp = (ImageView) view.findViewById(R.id.news_image);
            publishedAt = (TextView) view.findViewById(R.id.publishedAt);
            undoButton = (Button) view.findViewById(R.id.undo_button);


            view.setOnClickListener(this);
            newTitle.setOnClickListener(this);
            newsDesc.setOnClickListener(this);
            bmp.setOnClickListener(this);
            publishedAt.setOnClickListener(this);
            undoButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final ProductsPojo productsPojo = productsList.get(getPosition());
            Intent i = new Intent(context, EditProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", productsPojo.getTitle());
            bundle.putString("desc", productsPojo.getDesc());
            bundle.putString("mrp", productsPojo.getPublishedAt());
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            if (productsClickListener != null) {
                productsClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public ProductsAdapter(Context context, List<ProductsPojo> productsList) {
        this.context=context;
        this.productsList = productsList;
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
                final ArrayList<ProductsPojo> results = new ArrayList<ProductsPojo>();
                if (productsList == null)
                    productsList = productsList;
                if (constraint != null) {
                    if (productsList != null && productsList.size() > 0) {
                        for (final ProductsPojo prod : productsList) {
                            if (prod.getTitle().toLowerCase().contains(constraint.toString()))
                                results.add(prod);
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
                productsList = (ArrayList<ProductsPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_products, parent, false);

        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final ProductsPojo product = productsList.get(position);

        if (itemsPendingRemoval.contains(product)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.newTitle.setVisibility(View.GONE);
            holder.newsDesc.setVisibility(View.GONE);;
            holder.bmp.setVisibility(View.GONE);
            holder.publishedAt.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(product);
                    pendingRunnables.remove(product);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(product);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(productsList.indexOf(product));
                }
            });
        } else {
            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.newTitle.setText(product.getTitle());
            holder.newsDesc.setText(product.getDesc());
            holder.bmp.setImageBitmap(product.getBmp());
            holder.publishedAt.setText(product.getPublishedAt());
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(null);
        }

    }

    public void setClickListener(ProductsClickListener productsClickListener) {
        this.productsClickListener = productsClickListener;
    }

    public void pendingRemoval(int position) {
        final ProductsPojo item = productsList.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(productsList.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        ProductsPojo item = productsList.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (productsList.contains(item)) {
            productsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        ProductsPojo item = productsList.get(position);
        return itemsPendingRemoval.contains(item);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public interface ProductsClickListener {
        public void itemClicked(View view, int position);
    }
}