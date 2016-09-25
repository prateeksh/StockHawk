package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

public class MyStockDetailAcivity extends Activity {

    private final String LOG_TAG = getClass().getSimpleName();
    private final String BASE_URL = "http://query.yahooapis.com/";

    private TextView mCompanyNameTextView;
    private String mCompanyName;

   // private ArrayList<StockItem>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stock_detail_acivity);

        mCompanyNameTextView = (TextView) findViewById(R.id.companyName);

        //final String stockSymbol = getIntent().getStringExtra("symbol");
        mCompanyName = getIntent().getStringExtra("name");
    }
}
