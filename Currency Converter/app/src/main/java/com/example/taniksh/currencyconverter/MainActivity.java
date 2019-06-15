package com.example.taniksh.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void convertCurrency(View view){
        Log.i("Info", "Button pressed!");
        EditText editText = (EditText) findViewById(R.id.editText);
        Log.i("info", editText.getText().toString());

        String amountInDollar = editText.getText().toString();
        double amounntInDollarDouble = Double.parseDouble(amountInDollar);
        double amountinRupeesDouble = amounntInDollarDouble*69.88;
        String amountInRupees = String.format("%.2f", amountinRupeesDouble);

        Toast.makeText(this, amountInDollar + "$ to " + amountInRupees + "rupees", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
