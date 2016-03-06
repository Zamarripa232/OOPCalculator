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
        EditText copayDeducView = (EditText) findViewById(R.id.copayDeduc);

        // Pull TextViews for display purposes
        TextView splitOneView = (TextView) findViewById(R.id.splitOne);
        TextView splitTwoView = (TextView) findViewById(R.id.splitTwo);

        // Turn into numbers
        BigDecimal currentOOP = new BigDecimal(Double.parseDouble(currentOOPView.getText().toString()));
        BigDecimal maxOOP = new BigDecimal(Double.parseDouble(maxOOPView.getText().toString()));
        BigDecimal coins = new BigDecimal(Double.parseDouble(coinsView.getText().toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal memberCoins = new BigDecimal(1.0).subtract(coins).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal allow = new BigDecimal(Double.parseDouble(allowView.getText().toString()));
        BigDecimal copayDeduc = new BigDecimal(Double.parseDouble(copayDeducView.getText().toString()));

        // More Accurate Math-er?
        BigDecimal amtToOOP = maxOOP.subtract(currentOOP).setScale(2,BigDecimal.ROUND_HALF_UP);
        String resultOne;
        String resultTwo;

        // Check to see if the copay/deductible by themselves will hit the OOP maximum perfectly
        if (amtToOOP.subtract(copayDeduc).setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0")) == 0){
            resultOne = "First Line: " + copayDeduc.toString();
            resultTwo = "Second Line: " + allow.subtract(copayDeduc).setScale(2,BigDecimal.ROUND_HALF_UP);

        // Checks to see if part of the copay/deductible will hit OOP Maximum
        } else if(amtToOOP.subtract(copayDeduc).setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0")) < 0){
            resultOne = "First Line: " + amtToOOP.toString();
            resultTwo = "Second Line: " + allow.subtract(amtToOOP).setScale(2,BigDecimal.ROUND_HALF_UP);

        // Does the new coins split math...
        } else {
            BigDecimal newCoinsSplit = amtToOOP.subtract(copayDeduc).setScale(2,BigDecimal.ROUND_HALF_UP);
            newCoinsSplit = newCoinsSplit.divide(memberCoins, BigDecimal.ROUND_HALF_UP);
            newCoinsSplit = newCoinsSplit.add(copayDeduc).setScale(2,BigDecimal.ROUND_HALF_UP);

            // Checks to see if it doesn't even hit the OOP Maximum here
            if (newCoinsSplit.compareTo(allow) == 1) {
                resultOne = getString(R.string.noOOP);
                resultTwo = "";

            // Checks to see if the OOP is met *perfectly* and therefore requires no split
            } else if (newCoinsSplit.compareTo(allow) == 0) {
                resultOne = getString(R.string.oopMetPerfectly);
                resultTwo = "";

            // If neither of those two things are true, it will then display the split
            } else {
                resultOne = "First Line: " + newCoinsSplit.toString();
                resultTwo = "Second Line: " + allow.subtract(newCoinsSplit).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
        }

        // Set the text in the view
        splitOneView.setText(resultOne);
        splitTwoView.setText(resultTwo);

    }
}
