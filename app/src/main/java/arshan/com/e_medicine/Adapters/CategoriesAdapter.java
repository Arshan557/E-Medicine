package arshan.com.e_medicine.Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.DailogEditCategory;
import arshan.com.e_medicine.Models.CategoriesPojo;
import arshan.com.e_medicine.R;
import arshan.com.e_medicine.Views.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arshan on 15-Jun-2017.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<CategoriesPojo> categoriesList = new ArrayList<>();;
    private CategoriesClickListener categoriesClickListener;
    private CustomProgressDialog customProgressDialog;
    CategoriesPojo categoriesPojo;
    private Context context;
    CategoryViewHolder holder;

    private static final String TAG = "CategoriesAdapter";

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView categoryName;
        public CircleImageView categoryPic;
        private String apikey;

        public CategoryViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryPic = (CircleImageView) view.findViewById(R.id.categoryPic);

            // Getting data from Shared preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            if (null != sharedPreferences) {
                apikey = sharedPreferences.getString("apikey", "");
            }

            view.setOnClickListener(this);
            categoryName.setOnClickListener(this);
            categoryPic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CategoriesPojo categoriesPojo = categoriesList.get(getPosition());
            /*Intent i = new Intent(context, DailogEditCategory.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", categoriesPojo.getId());
            bundle.putString("companyId", categoriesPojo.getCompanyId());
            bundle.putString("name", categoriesPojo.getName());
            bundle.putString("createdBy", categoriesPojo.getCreatedBy());
            bundle.putString("createdOn", categoriesPojo.getCreatedOn());
            bundle.putString("modifiedBy", categoriesPojo.getModifiedBy());
            bundle.putString("modifiedOn", categoriesPojo.getModifiedOn());
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/

            Bundle bundle = new Bundle();
            bundle.putString("id", categoriesPojo.getId());
            bundle.putString("name", categoriesPojo.getName());
            FragmentManager manager = ((Activity)context).getFragmentManager();
            DailogEditCategory dailogFragment = new DailogEditCategory();
            dailogFragment.setArguments(bundle);
            dailogFragment.show(manager,"dailogFrag");

            if (categoriesClickListener != null) {
                categoriesClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public CategoriesAdapter(Context context, List<CategoriesPojo> categoriesList) {
        this.context=context;
        this.categoriesList = categoriesList;
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
                final ArrayList<CategoriesPojo> results = new ArrayList<CategoriesPojo>();
                if (constraint != null) {
                    if (categoriesList != null && categoriesList.size() > 0) {
                        for (final CategoriesPojo cat : categoriesList) {
                            if (cat.getName().toLowerCase().contains(constraint.toString()))
                                results.add(cat);
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
                categoriesList = (ArrayList<CategoriesPojo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setClickListener(CategoriesClickListener categoriesClickListener) {
        this.categoriesClickListener = categoriesClickListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_categories, parent, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final CategoriesPojo category = categoriesList.get(position);
        this.holder = holder;
        holder.categoryName.setText(category.getName());
        holder.categoryPic.setImageBitmap(category.getPic());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public interface CategoriesClickListener {
        public void itemClicked(View view, int position);
    }
}
