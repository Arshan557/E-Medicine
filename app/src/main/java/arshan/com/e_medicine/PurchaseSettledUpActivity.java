package arshan.com.e_medicine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PurchaseSettledUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private EditText chequeNum, paymentDate, otherBank;
    private ImageView dateIcon;
    private Button btnSettledUp;
    private Spinner paymentListSpinner, bankListSpinner;
    private DatePicker datePicker;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    static final int DATE_PICKER_ID = 1111;
    List<String> paymentList = new ArrayList<String>();
    List<String> bankList = new ArrayList<String>();
    ArrayList<String> invoice_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_settled_up);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        invoice_list = getIntent().getStringArrayListExtra("invoice_list");
        Log.d("invoice_list",""+invoice_list);

        chequeNum = (EditText) findViewById(R.id.cheque_num);
        paymentDate = (EditText) findViewById(R.id.paymentDate);
        otherBank = (EditText) findViewById(R.id.otherBank);
        dateIcon = (ImageView) findViewById(R.id.dateIcon);
        btnSettledUp = (Button) findViewById(R.id.btn_settledUp);
        paymentListSpinner = (Spinner) findViewById(R.id.spinnerPaymentList);
        bankListSpinner = (Spinner) findViewById(R.id.spinnerBank);

        // Spinner click listener
        paymentListSpinner.setOnItemSelectedListener(PurchaseSettledUpActivity.this);
        bankListSpinner.setOnItemSelectedListener(PurchaseSettledUpActivity.this);

        paymentList.add("Select payment mode");
        paymentList.add("Cash");
        paymentList.add("Cheque");

        bankList.add("Choose bank");
        bankList.add("SBI");
        bankList.add("HDFC");
        bankList.add("Kotak");
        bankList.add("ICICI");
        bankList.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> bankListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, bankList);
        bankListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankListSpinner.setAdapter(bankListAdapter);

        ArrayAdapter<String> paymentListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, paymentList);
        paymentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentListSpinner.setAdapter(paymentListAdapter);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        paymentDate.setText(showDate(year, month+1, day));

        dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
            }
        });
    }

    private StringBuilder showDate(int year, int month, int day) {
        String mnth = "00", dy = "00";
        //date.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        if (month < 10) {
            mnth = "0" + String.valueOf(month);
            month = Integer.parseInt(mnth);
        } else {
            mnth = String.valueOf(month);
        }
        if (day < 10) {
            dy = "0" + month;
            day = Integer.parseInt(dy);
        } else {
            dy = String.valueOf(day);
        }
        return (new StringBuilder().append(year).append("-")
                .append(mnth).append("-").append(dy));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String mnth = "00", dy = "00";
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            if (month < 10) {
                mnth = "0" + String.valueOf(month);
                month = Integer.parseInt(mnth);
            } else {
                mnth = String.valueOf(month);
            }
            if (day < 10) {
                dy = "0" + month;
                day = Integer.parseInt(dy);
            } else {
                dy = String.valueOf(day);
            }
            // Show selected date
            paymentDate.setText(new StringBuilder().append(year)
                    .append("-").append(mnth).append("-").append(dy));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        //Toast.makeText(PurchaseSettledUpActivity.this, "Selected: " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
        if ("other".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) {
            otherBank.setVisibility(View.VISIBLE);
        } else {
            otherBank.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
