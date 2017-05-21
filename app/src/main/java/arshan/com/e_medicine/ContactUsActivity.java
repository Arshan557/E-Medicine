package arshan.com.e_medicine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText name, mail, subject, message;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        name = (EditText) findViewById(R.id.name);
        mail = (EditText) findViewById(R.id.email);
        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.msg);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty() && !mail.getText().toString().isEmpty()
                        && !subject.getText().toString().isEmpty()  && !message.getText().toString().isEmpty() ) {

                    /*new Thread(new Runnable() {
                        public void run() {
                            try {
                                GMailSender sender = new GMailSender("ravi.sharma@oodlestechnologies.com",
                                        "Can't disclose, enter your password and your email");
                                sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/image.jpg");
                                sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",
                                        "ravi.sharma@oodlestechnologies.com",
                                        "ravisharmabpit@gmail.com");
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();*/
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("message/rfc822");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mani.chari91@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"vsudarhan57@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, String.valueOf(subject.getText()));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(message.getText()));
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Choose mail to contact us"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ContactUsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(ContactUsActivity.this, "Your query has been submitted. we'll get back to you soon", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactUsActivity.this, "Please fill all details", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
