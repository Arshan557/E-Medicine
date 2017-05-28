package arshan.com.e_medicine;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;

import arshan.com.e_medicine.Views.CustomProgressDialog;

public class AboutUsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    private String postUrl = "", label = "About";
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            postUrl= extras.getString("postUrl");
            label= extras.getString("label");
        }
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(label);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Log.d("postUrl",postUrl);
        customProgressDialog = CustomProgressDialog.show(AboutUsActivity.this);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);
        customProgressDialog.cancel();
    }
}
