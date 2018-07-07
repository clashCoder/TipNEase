package com.github.clashcoder.tipnease;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import com.github.clashcoder.tipnease.databinding.ActivityMainBinding;

import org.apache.commons.lang3.math.NumberUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private double billTotal;
    private double tipPercentage;
    private double tipTotal;
    private double grandTotal;
    private long numPeople;
    private double totalPerPerson;
    private double tipPerPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences(TipUtils.SHARED_PREF_FILE, 0);
        prefEditor = sharedPreferences.edit();

        initializeValues();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        bindValues();

        addTextListeners();
        prefEditor.commit();
    }

    private void addTextListeners() {



        binding.billEditMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String billTotalStr = editable.toString();

                //float prevBill = sharedPreferences.getFloat(TipUtils.BILL_TOTAL_KEY_MAIN, 0.00f);

                if (!NumberUtils.isParsable(billTotalStr)) {
                    billTotal = 0.0f;
                } else {
                    billTotal = Double.valueOf(billTotalStr);
                }

                prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY_MAIN, (float) billTotal);
                prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY, (float) billTotal);
                prefEditor.commit();

                grandTotal = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalWithTip(billTotal, tipPercentage), 2);
                tipTotal = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipTotal(billTotal, tipPercentage), 2);
                totalPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalPerPerson(billTotal, tipPercentage, numPeople), 2);
                tipPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipPerPerson(billTotal, tipPercentage, numPeople), 2);

                binding.totalMain.setText(Double.toString(grandTotal));
                binding.tipTotalMain.setText(Double.toString(tipTotal));
                binding.totalPerPersonMain.setText(Double.toString(totalPerPerson));
                binding.tipPerPersonMain.setText(Double.toString(tipPerPerson));
            }
        });

        binding.tipPctEditMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String tipPercentageStr = editable.toString();


                if (!NumberUtils.isParsable(tipPercentageStr)) {
                    tipPercentage = 0.00f;
                } else {
                    tipPercentage = Double.valueOf(tipPercentageStr);
                }

                prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY_MAIN, (float) tipPercentage);
                prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY, (float) tipPercentage);
                prefEditor.commit();

                grandTotal = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalWithTip(billTotal, tipPercentage), 2);
                tipTotal = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipTotal(billTotal, tipPercentage), 2);
                totalPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalPerPerson(billTotal, tipPercentage, numPeople), 2);
                tipPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipPerPerson(billTotal, tipPercentage, numPeople), 2);

                binding.totalMain.setText(Double.toString(grandTotal));
                binding.tipTotalMain.setText(Double.toString(tipTotal));
                binding.totalPerPersonMain.setText(Double.toString(totalPerPerson));
                binding.tipPerPersonMain.setText(Double.toString(tipPerPerson));
            }
        });

        binding.numPeopleEditMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String numPeopleStr = editable.toString();

                if (!NumberUtils.isParsable(numPeopleStr)) {
                    numPeople = 1;
                } else {
                    numPeople = Long.valueOf(editable.toString());
                }

                prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY_MAIN, (float) numPeople);
                prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY, (float) numPeople);
                prefEditor.commit();

                totalPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalPerPerson(billTotal, tipPercentage, numPeople), 2);
                tipPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipPerPerson(billTotal, tipPercentage, numPeople), 2);

                binding.totalPerPersonMain.setText(Double.toString(totalPerPerson));
                binding.tipPerPersonMain.setText(Double.toString(tipPerPerson));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeValues();
        bindValues();
    }

    public void initializeValues() {
        billTotal = TipUtils.roundToTwoDecPlaces((double)sharedPreferences.getFloat(TipUtils.BILL_TOTAL_KEY_MAIN, 0.00f), 2);
        tipPercentage = TipUtils.roundToTwoDecPlaces((double) sharedPreferences.getFloat(TipUtils.TIP_PERCENTAGE_KEY_MAIN, 0.00f), 2);
        numPeople = (long) sharedPreferences.getFloat(TipUtils.NUM_PEOPLE_KEY_MAIN, 1.00f);

        tipTotal = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipTotal(billTotal, tipPercentage), 2);
        grandTotal = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalWithTip(billTotal, tipPercentage), 2);
        totalPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTotalPerPerson(billTotal, tipPercentage, numPeople), 2);
        tipPerPerson = TipUtils.roundToTwoDecPlaces(TipUtils.calculateTipPerPerson(billTotal, tipPercentage, numPeople), 2);
    }

    public void bindValues() {
        binding.billEditMain.setText(String.format("%.2f", billTotal));
        binding.tipPctEditMain.setText(String.format("%.2f", tipPercentage));
        binding.numPeopleEditMain.setText(String.valueOf(numPeople));

        binding.tipTotalMain.setText(String.format("%.2f", tipTotal));
        binding.totalMain.setText(String.format("%.2f", grandTotal));
        binding.totalPerPersonMain.setText(String.format("%.2f", totalPerPerson));
        binding.tipPerPersonMain.setText(String.format("%.2f", tipPerPerson));
    }
}
