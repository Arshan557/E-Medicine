package arshan.com.e_medicine;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewSettledPurchaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView dName, invoiceNumText, invoiceNum, billDateText, billDate, amountText, amount,
    paymentModeText, paymentMode, chequeNumText, chequeNum, paymentDateText, paymentDate, bankText, bank;
    private LinearLayout linearCheque, linearBank;
    Bundle data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_settled_purchase);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        data = getIntent().getExtras();
        if (null != data) {
            Log.d("data",data.getString("InvoiceNumber"));
        }
        getSupportActionBar().setTitle(data.getString("InvoiceNumber"));

        Typeface cat_names_font = Typeface.createFromAsset(getAssets(), "categorynamefont.otf");
        Typeface nav_items_font = Typeface.createFromAsset(getAssets(), "nav_items.ttf");
        Typeface nav_subitems_font = Typeface.createFromAsset(getAssets(), "nav_sub_item_font.otf");

        dName = (TextView) findViewById(R.id.dName);
        /*invoiceNumText = (TextView) findViewById(R.id.invoiceNumText);
        invoiceNum = (TextView) findViewById(R.id.invoiceNum);*/
        billDateText = (TextView) findViewById(R.id.billDateText);
        billDate = (TextView) findViewById(R.id.billDate);
        amountText = (TextView) findViewById(R.id.amountText);
        amount = (TextView) findViewById(R.id.amount);
        paymentModeText = (TextView) findViewById(R.id.paymentModeText);
        paymentMode = (TextView) findViewById(R.id.paymentMode);
        chequeNumText = (TextView) findViewById(R.id.chequeNumText);
        chequeNum = (TextView) findViewById(R.id.chequeNum);
        paymentDateText = (TextView) findViewById(R.id.paymentDateText);
        paymentDate = (TextView) findViewById(R.id.paymentDate);
        bankText = (TextView) findViewById(R.id.bankText);
        bank = (TextView) findViewById(R.id.bank);
        linearCheque = (LinearLayout) findViewById(R.id.linearChequeNum);
        linearBank = (LinearLayout) findViewById(R.id.linearBank);

        dName.setTypeface(nav_subitems_font);
        //invoiceNumText.setTypeface(nav_subitems_font);
        billDateText.setTypeface(nav_subitems_font);
        amountText.setTypeface(nav_subitems_font);
        paymentModeText.setTypeface(nav_subitems_font);
        chequeNumText.setTypeface(nav_subitems_font);
        paymentDateText.setTypeface(nav_subitems_font);
        bankText.setTypeface(nav_subitems_font);
        //invoiceNum.setTypeface(cat_names_font);
        billDate.setTypeface(cat_names_font);
        amount.setTypeface(cat_names_font);
        paymentMode.setTypeface(cat_names_font);
        chequeNum.setTypeface(cat_names_font);
        paymentDate.setTypeface(cat_names_font);
        bank.setTypeface(cat_names_font);

        if (!"null".equalsIgnoreCase(data.getString("DistributorId")))
            dName.setText(data.getString("DistributorId"));
        /*if (null != data.getString("InvoiceNumber") && !"null".equalsIgnoreCase(data.getString("InvoiceNumber")))
            invoiceNum.setText(data.getString("InvoiceNumber"));*/
        if (null != data.getString("BillDate") && !"null".equalsIgnoreCase(data.getString("BillDate")))
            billDate.setText(data.getString("BillDate"));
        if (null != data.getString("Amount") && !"null".equalsIgnoreCase(data.getString("Amount")))
            amount.setText(data.getString("Amount"));
        if (null != data.getString("PaymentMode") && !"null".equalsIgnoreCase(data.getString("PaymentMode")))
            paymentMode.setText(data.getString("PaymentMode"));
        if (null != data.getString("ChequeNumber") && !"null".equalsIgnoreCase(data.getString("ChequeNumber")))
            chequeNum.setText(data.getString("ChequeNumber"));
        if (null != data.getString("PaymentDate") && !"null".equalsIgnoreCase(data.getString("PaymentDate")))
            paymentDate.setText(data.getString("PaymentDate"));
        if (null != data.getString("BankName") && !"null".equalsIgnoreCase(data.getString("BankName")))
            bank.setText(data.getString("BankName"));

        if (!"cheque".equalsIgnoreCase(data.getString("PaymentMode"))) {
            linearCheque.setVisibility(View.GONE);
            linearBank.setVisibility(View.GONE);
        }
    }
}
