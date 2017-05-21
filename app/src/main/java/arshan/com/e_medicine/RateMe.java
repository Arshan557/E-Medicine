package arshan.com.e_medicine;

/**
 * Created by Arshan on 11-Oct-2016.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RateMe extends DialogFragment implements View.OnClickListener{
    Button submit,cancel;
    RatingBar ratingBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rate_me,null);
        submit = (Button) view.findViewById(R.id.btnSubmit);
        cancel = (Button) view.findViewById(R.id.btnCancel);
        ratingBar = (RatingBar) view.findViewById(R.id.rb1);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setCancelable(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("",""+v);
        if (v.getId() == R.id.btnSubmit) {
            String rating = String.valueOf(ratingBar.getRating());
            if (rating == "0.0"){
                Toast.makeText(getActivity(),"Rate and submit", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), rating + " submitted", Toast.LENGTH_LONG).show();
                dismiss();
            }
        } else {
            Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_SHORT).show();
            dismiss();

        }
    }
}