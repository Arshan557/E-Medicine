package arshan.com.e_medicine;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDistributorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText dist_name, dist_uname, dist_mail, dist_mobile;
    private Button dist_edit, dist_save;
    private CircleImageView dist_pic;
    public static final String DEFAULT = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_distributor);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Bundle data = getIntent().getExtras();
        if (null != data) {
            Log.d("data",data.getString("name") + "," + data.getString("email")+","+data.getString("uname")
                    +","+data.getString("mobile")+","+data.getString("phone")+","+data.getString("picUrl"));
        }
        getSupportActionBar().setTitle(data.getString("name"));

        dist_name = (EditText) findViewById(R.id.dist_name);
        dist_uname = (EditText) findViewById(R.id.dist_uname);
        dist_mail = (EditText) findViewById(R.id.dist_email);
        dist_mobile = (EditText) findViewById(R.id.dist_mobile);
        dist_edit = (Button) findViewById(R.id.dist_edit);
        dist_save = (Button) findViewById(R.id.dist_save);
        dist_pic = (CircleImageView) findViewById(R.id.dist_pic);

        if (null != data.getString("name")) dist_name.setText(data.getString("name"));
        else dist_name.setText(data.getString(DEFAULT));
        if (null != data.getString("uname")) dist_uname.setText(data.getString("uname"));
        else dist_uname.setText(data.getString(DEFAULT));
        if (null != data.getString("email")) dist_mail.setText(data.getString("email"));
        else dist_mail.setText(data.getString(DEFAULT));
        if (null != data.getString("mobile")) dist_mobile.setText(data.getString("mobile"));
        else dist_mobile.setText(data.getString(DEFAULT));
        if (null != data.getString("picUrl"))
            Glide.with(EditDistributorActivity.this).load(data.getString("picUrl")).into(dist_pic);
        else
            dist_pic.setImageResource(R.drawable.defaultpic);

        dist_name.setEnabled(false);
        dist_uname.setEnabled(false);
        dist_mobile.setEnabled(false);
        dist_mail.setEnabled(false);
        dist_pic.setEnabled(false);
        dist_save.setVisibility(View.GONE);

        dist_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dist_name.setEnabled(true);
                dist_uname.setEnabled(true);
                dist_mobile.setEnabled(true);
                dist_mail.setEnabled(true);
                dist_pic.setEnabled(true);
                dist_edit.setVisibility(View.GONE);
                dist_save.setVisibility(View.VISIBLE);
            }
        });

        dist_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditDistributorActivity.this,"Saving details",Toast.LENGTH_SHORT).show();
                dist_name.setEnabled(false);
                dist_uname.setEnabled(false);
                dist_mobile.setEnabled(false);
                dist_mail.setEnabled(false);
                dist_pic.setEnabled(false);
                dist_edit.setVisibility(View.VISIBLE);
                dist_save.setVisibility(View.GONE);
            }
        });
    }
}
