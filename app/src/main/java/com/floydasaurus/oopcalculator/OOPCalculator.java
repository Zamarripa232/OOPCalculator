package com.floydasaurus.oopcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;


public class OOPCalculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oopcalculator);
    }

    public void onCalculate(View view){
        // Makes keyboard go away on click
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        // Pull EditText Views
        EditText currentOOPView = (EditText) findViewById(R.id.currentOOP);
        EditText maxOOPView = (EditText) findViewById(R.id.maxOOP);
        EditText coinsView = (EditText) findViewById(R.id.coins);
        EditText allowView = (EditText) findViewById(R.id.allow);

        // Pull TextViews for display purposes
        TextView splitOneView = (TextView) findViewById(R.id.splitOne);
        TextView splitTwoView = (TextView) findViewById(R.id.splitTwo);

        // Turn into numbers
        BigDecimal currentOOP = new BigDecimal(Double.parseDouble(currentOOPView.getText().toString()));
        BigDecimal maxOOP = new BigDecimal(Double.parseDouble(maxOOPView.getText().toString()));
        BigDecimal coins = new BigDecimal(Double.parseDouble(coinsView.getText().toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal memberCoins = new BigDecimal(1.0).subtract(coins).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal allow = new BigDecimal(Double.parseDouble(allowView.getText().toString()));
        // More Accurate Math-er?
        BigDecimal newCoinsSplit = maxOOP.subtract(currentOOP).setScale(2,BigDecimal.ROUND_HALF_UP);
        newCoinsSplit = newCoinsSplit.divide(memberCoins,BigDecimal.ROUND_HALF_UP);

        if (newCoinsSplit.compareTo(allow) == 0 || newCoinsSplit.compareTo(allow) == 1) {
            splitOneView.setText(getString(R.string.noOOP));
            splitTwoView.setText("");
        } else {
            String resultOne = newCoinsSplit.toString();
            String resultTwo = allow.subtract(newCoinsSplit).setScale(2, BigDecimal.ROUND_HALF_UP).toString();

            // Set the text in the view
            splitOneView.setText(resultOne);
            splitTwoView.setText(resultTwo);
        }
    }
}
