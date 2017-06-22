package arshan.com.e_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText name, uname, mail, mobile, phone, password;
    private CircleImageView pic;
    public static final String DEFAULT = "";
    private String apikey = "", TAG = AddUserActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // Getting data from Shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (null != sharedPreferences) {
            apikey = sharedPreferences.getString("apikey", DEFAULT);
        }

        name = (EditText) findViewById(R.id.userName);
        uname = (EditText) findViewById(R.id.userUname);
        mail = (EditText) findViewById(R.id.userEmail);
        mobile = (EditText) findViewById(R.id.userMobile);
        phone = (EditText) findViewById(R.id.userPhone);
        password = (EditText) findViewById(R.id.userPassword);

        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fabAddUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String finalUrl = Constants.ADD_DISTRIBUTOR_URL + "?name=" + name.getText().toString() + "&email=" + mail.getText().toString() +
                        "&username=" + uname.getText().toString() + "&password=" + password.getText().toString() + "&number=" + mobile.getText().toString()
                        + "&phone=" + phone.getText().toString() + "&apikey=" + apikey+"&image=http://www.provo2.com/health-fitness/wp-content/uploads/2010/11/default-avatar.jpg";
                Log.d("final url", finalUrl);
                //Make call to Async
                new addDistributor().execute(finalUrl);*/

            }
        });
    }
}
