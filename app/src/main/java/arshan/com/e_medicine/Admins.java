package arshan.com.e_medicine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Admins extends Fragment {
    String[] skillTitle;
    GridView skillList;
    int[] images = {R.drawable.about, R.drawable.calculator, R.drawable.defaultpic,
            R.drawable.distributors, R.drawable.categories, R.drawable.feedback, R.drawable.faq};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admins, container, false);
        skillTitle = getResources().getStringArray(R.array.skillTitles);
        skillList = (GridView) view.findViewById(R.id.skillList);
        skillList.setAdapter(new MyAdminsAdapter(getActivity(), skillTitle, images));
        return view;
    }
}

class MyAdminsAdapter extends ArrayAdapter<String> {
    Context context;
    int[] images;
    String[] titleArray;

    public MyAdminsAdapter(Context context, String[] titles, int[] imgs) {
        super(context, R.layout.admins_row,R.id.skillTitle,titles);
        this.context = context;
        this.images = imgs;
        this.titleArray = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyAdminsViewHolder holder = null;
        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.admins_row, parent, false);
            holder = new MyAdminsViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (MyAdminsViewHolder) row.getTag();
        }
        holder.myTitles.setText(titleArray[position]);
        return row;
    }
}

class MyAdminsViewHolder {
    ImageView myImage;
    TextView myTitles;

    public MyAdminsViewHolder(View row){
        myImage = (ImageView) row.findViewById(R.id.skillImage);
        myTitles = (TextView) row.findViewById(R.id.skillTitle);
    }

}
