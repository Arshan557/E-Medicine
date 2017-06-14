package arshan.com.e_medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CookieActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Bind(R.id.cookie) EditText cookie;
    @Bind(R.id.cookie_submit) Button cookieSubmit;
    @Bind(R.id.getCookie) TextView getCookie;
    private static final String TAG = "CookieActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        String cookie;
        SharedPreferences sharedPreferencesCookie = getSharedPreferences("CookieData", Context.MODE_PRIVATE);
        cookie = sharedPreferencesCookie.getString("cookieString", "");
        if (null != cookie && !cookie.equalsIgnoreCase("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        ButterKnife.bind(this);

        cookieSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeCookie();
            }
        });

        getCookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://health.ultimatefreehost.in/E-H/production/mobile/cookie.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void storeCookie() {
        cookieSubmit.setEnabled(false);

        String cookieString = cookie.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("CookieData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookieString", cookieString);
        editor.commit();

        Intent i = new Intent(CookieActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }
}
