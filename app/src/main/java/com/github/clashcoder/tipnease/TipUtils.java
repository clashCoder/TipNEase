package com.github.clashcoder.tipnease;

/**
 * Created by itachiuchiha on 6/13/18.
 */

public class TipUtils {

    public static final String SHARED_PREF_FILE = "com.github.clashcoder.tipnease";

    public static final String BILL_TOTAL_KEY_MAIN = "bill_total_main";
    public static final String TIP_PERCENTAGE_KEY_MAIN = "tip_percentage_main";
    public static final String TIP_TOTAL_KEY_MAIN = "tip_total_main";
    public static final String TOTAL_WITH_TIP_KEY_MAIN = "total_with_tip_main";
    public static final String NUM_PEOPLE_KEY_MAIN = "num_people_main";
    public static final String TOTAL_PER_PERSON_KEY_MAIN = "total_per_person_main";
    public static final String TIP_PER_PERSON_KEY_MAIN = "tip_per_person_main";

    public static final String BILL_TOTAL_KEY = "bill_total";
    public static final String TIP_PERCENTAGE_KEY = "tip_percentage";
    public static final String TIP_TOTAL_KEY = "tip_total";
    public static final String TOTAL_WITH_TIP_KEY = "total_with_tip";
    public static final String NUM_PEOPLE_KEY = "num_people";
    public static final String TOTAL_PER_PERSON_KEY = "total_per_person";
    public static final String TIP_PER_PERSON_KEY = "tip_per_person";


    public static float round(float value, int scale) {

        int pow = 10;

        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }

        float tmp = value * pow;
        float tmpSub = tmp - (int) tmp;

        return ( (float) ( (int) ( value >= 0 ? (tmpSub >= 0.5f ? tmp + 1 : tmp) :
                (tmpSub >= -0.5f ? tmp : tmp - 1)))) / pow;
    }

    public static double roundToTwoDecPlaces(double val, int scale) {
        return Math.round(val * Math.pow(10, scale)) / Math.pow(10, scale);
    }


    public static double round3(double val) {
        double roundOff = Math.round(val * 100.0) / 100.0;

        return roundOff;
    }

    public static double calculateTipTotal(double billTotal, double tipPercentage) {

        double tip = tipPercentage / 100;
        return billTotal * tip;
    }

    public static double calculateTotalWithTip(double billTotal, double tipPercentage) {

        double tipTotal = calculateTipTotal(billTotal, tipPercentage);

        return billTotal + tipTotal;
    }

    public static double calculateTotalPerPerson(double billTotal, double tipPercentage, int numPeople) {

        if (numPeople == 0)
            return 0;

        double total = calculateTotalWithTip(billTotal, tipPercentage);

        return roundToTwoDecPlaces(total / numPeople, 2);
    }

    public static double calculateTipPerPerson(double billTotal, double tipPercentage, int numPeople) {

        if (numPeople == 0)
            return 0;

        double tip = calculateTipTotal(billTotal, tipPercentage);

        return roundToTwoDecPlaces(tip / numPeople, 2);
    }
}
