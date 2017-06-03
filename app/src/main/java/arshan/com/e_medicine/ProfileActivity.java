package arshan.com.e_medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText fname, lname, mail, mobile;
    private Button edit, save;
    private CircleImageView pic;
    String firstname, lastname, profilePic, email, phone = "";
    public static final String DEFAULT = "N/A";
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static final int GALLERY = 1;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
            firstname = sharedPreferences.getString("fname", DEFAULT);
            lastname = sharedPreferences.getString("lname", DEFAULT);
            profilePic = sharedPreferences.getString("profilePic", DEFAULT);
            email = sharedPreferences.getString("email", DEFAULT);
            phone = sharedPreferences.getString("phone", DEFAULT);
        }

        fname = (EditText) findViewById(R.id.first_name);
        lname = (EditText) findViewById(R.id.last_name);
        mail = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        edit = (Button) findViewById(R.id.edit);
        save = (Button) findViewById(R.id.save);
        pic = (CircleImageView) findViewById(R.id.pic);

        fname.setText(firstname);
        lname.setText(lastname);
        mail.setText(email);
        mobile.setText(phone);
        if (null != profilePic)
            Glide.with(ProfileActivity.this).load(profilePic).into(pic);
        else
            pic.setImageResource(R.drawable.defaultpic);

        fname.setEnabled(false);
        lname.setEnabled(false);
        mobile.setEnabled(false);
        mail.setEnabled(false);
        pic.setEnabled(false);
        save.setVisibility(View.GONE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname.setEnabled(true);
                lname.setEnabled(true);
                mobile.setEnabled(true);
                mail.setEnabled(true);
                pic.setEnabled(true);
                edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this,"Saving profile",Toast.LENGTH_SHORT).show();
                fname.setEnabled(false);
                lname.setEnabled(false);
                mobile.setEnabled(false);
                mail.setEnabled(false);
                pic.setEnabled(false);
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            Uri mImageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    pic.setImageBitmap(Image);
                Toast.makeText(ProfileActivity.this,"Profile pic has been changed",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.d("FileNotFoundException",e.getLocalizedMessage());
            } catch (IOException e) {
                Log.d("IOException",e.getLocalizedMessage());
            }
        }
    }
}
