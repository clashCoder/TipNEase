package com.github.clashcoder.tipnease;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class TipWidget extends AppWidgetProvider {

    private static final String SHARED_PREF_FILE = "com.github.clashcoder.tipnease";
    private static final String COUNT_KEY = "count";

    private static final String INCREMENT_BILL_TOTAL = "com.github.clashcoder.increment_bill";
    private static final String DECREMENT_BILL_TOTAL = "com.github.clashcoder.decrement_bill";
    private static final String INCREMENT_TIP_PERCENTAGE = "com.github.clashcoder.increment_tip_percentage";
    private static final String DECREMENT_TIP_PERCENTAGE = "com.github.clashcoder.decrement_tip_percentage";
    private static final String INCREMENT_NUM_PEOPLE = "com.github.clashcoder.increment_num_people";
    private static final String DECREMENT_NUM_PEOPLE = "com.github.clashcoder.decrement_num_people";
    private static final String REFRESH = "com.github.clashcoder.refresh";

//    private static final String BILL_TOTAL_KEY = "bill_total";
//    private static final String TIP_PERCENTAGE_KEY = "tip_percentage";
//    private static final String TIP_TOTAL_KEY = "tip_total";
//    private static final String TOTAL_WITH_TIP_KEY = "total_with_tip";
//    private static final String NUM_PEOPLE_KEY = "num_people";
//    private static final String TOTAL_PER_PERSON_KEY = "total_per_person";
//    private static final String TIP_PER_PERSON_KEY = "tip_per_person";

    private static final int BILL_TOTAL_INDEX = 0;
    private static final int TIP_PERCENTAGE_INDEX = 1;
    private static final int TIP_TOTAL_INDEX = 2;
    private static final int TOTAL_WITH_TIP_INDEX = 3;
    private static final int NUM_PEOPLE_INDEX = 4;
    private static final int TOTAL_PER_PERSON_INDEX = 5;
    private static final int TIP_PER_PERSON_INDEX = 6;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE, 0);
//        int count = sharedPreferences.getInt(COUNT_KEY, 0);
//
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tip_widget);

        //Map<String, ?> sharedPrefMap = context.getSharedPreferences(SHARED_PREF_FILE, 0).getAll();

        //float bill_total = (float) sharedPrefMap.get(BILL_TOTAL_KEY);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE, 0);

        double billTotal = TipUtils.roundToTwoDecPlaces((double)sharedPreferences.getFloat(TipUtils.BILL_TOTAL_KEY, 0.00f), 2);
        double tipPercentage = TipUtils.roundToTwoDecPlaces((double)sharedPreferences.getFloat(TipUtils.TIP_PERCENTAGE_KEY, 0.00f), 2);
        long numPeople = (long) sharedPreferences.getFloat(TipUtils.NUM_PEOPLE_KEY, 1.00f);

        double tipTotal = TipUtils.calculateTipTotal(billTotal, tipPercentage);
        double totalWithTip = TipUtils.calculateTotalWithTip(billTotal, tipPercentage);

        double totalPerPerson = TipUtils.calculateTotalPerPerson(billTotal, tipPercentage, numPeople);
        double tipPerPerson = TipUtils.calculateTipPerPerson(billTotal, tipPercentage, numPeople);

        views.setTextViewText(R.id.bill_tv, String.format("%.2f", billTotal));
        views.setTextViewText(R.id.tip_pct_tv, String.format("%.2f", tipPercentage));
        views.setTextViewText(R.id.tip_tv, String.format("%.2f", tipTotal));
        views.setTextViewText(R.id.total_tv, String.format("%.2f", totalWithTip));
        views.setTextViewText(R.id.num_people_tv, String.valueOf(numPeople));
        views.setTextViewText(R.id.total_per_person_tv, String.format("%.2f", totalPerPerson));
        views.setTextViewText(R.id.tip_per_person_tv, String.format("%.2f", tipPerPerson));

        setOnClickListeners(context, appWidgetId, views);

        try {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (Exception e) {}



//        views.setTextViewText(R.id.num_people_tv, String.valueOf(count));
//        count++;
//
//        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
//        prefEditor.putInt(COUNT_KEY, count);
//        prefEditor.apply();
//
//        Intent intentUpdate = new Intent(context, TipWidget.class);
//        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        int[] idArray = new int[]{appWidgetId};
//        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.num_people_increment_btn, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE, 0);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        float currentBill = sharedPreferences.getFloat(TipUtils.BILL_TOTAL_KEY, 0.0f);
        float currentTipPct = sharedPreferences.getFloat(TipUtils.TIP_PERCENTAGE_KEY, 0.0f);
        int currentNumPeople = (int) sharedPreferences.getFloat(TipUtils.NUM_PEOPLE_KEY, 1.0f);

        if (intent.getAction().equals(INCREMENT_BILL_TOTAL)) {

            prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY, currentBill + 0.01f);
            prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY_MAIN, currentBill + 0.01f);

        } else if (intent.getAction().equals(DECREMENT_BILL_TOTAL)) {

            if (currentBill >= 0.01f) {
                prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY, currentBill - 0.01f);
                prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY_MAIN, currentBill - 0.01f);
            } else {
                prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY, 0.00f);
                prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY_MAIN, 0.00f);
            }

        } else if (intent.getAction().equals(INCREMENT_TIP_PERCENTAGE)) {

            prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY, currentTipPct + 0.01f);
            prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY_MAIN, currentTipPct + 0.01f);

        } else if (intent.getAction().equals(DECREMENT_TIP_PERCENTAGE)) {

            if (currentTipPct >= 0.01f) {
                prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY, currentTipPct - 0.01f);
                prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY_MAIN, currentTipPct - 0.01f);
            } else {
                prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY, 0.00f);
                prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY_MAIN, 0.00f);
            }

        } else if (intent.getAction().equals(INCREMENT_NUM_PEOPLE)) {

            prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY, currentNumPeople + 1.0f);
            prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY_MAIN, currentNumPeople + 1.0f);

        } else if (intent.getAction().equals(DECREMENT_NUM_PEOPLE)) {

            if (currentNumPeople >= 2.0f) {
                prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY, currentNumPeople - 1.0f);
                prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY_MAIN, currentNumPeople - 1.0f);
            } else {
                prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY, 1.0f);
                prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY_MAIN, 1.0f);
            }
        } else if (intent.getAction().equals(REFRESH)) {
            prefEditor.putFloat(TipUtils.BILL_TOTAL_KEY, currentBill);
            prefEditor.putFloat(TipUtils.TIP_PERCENTAGE_KEY, currentTipPct);
            prefEditor.putFloat(TipUtils.NUM_PEOPLE_KEY, currentNumPeople);
        }

        prefEditor.commit();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, TipWidget.class));

        for (int appWidgetID : appWidgetsIds) {
            updateAppWidget(context, appWidgetManager, appWidgetID);
        }

        super.onReceive(context, intent);

    }

    private static void setPreferenceValues(Context context, Map<String, Float> sharedKeyValues) {
        Set<String> keySet = sharedKeyValues.keySet();
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_FILE, 0);
        SharedPreferences.Editor prefEditor = sp.edit();

        for (String key: keySet) {
            float widgetValue = sharedKeyValues.get(key);
            prefEditor.putFloat(key, widgetValue);
        }

        prefEditor.apply();
    }

    private static ArrayList<String> getPreferenceValues(Context context, Map<String, Float> sharedKeyValues) {
        ArrayList<String> prefValues = new ArrayList<>();

        SharedPreferences sp = context.getSharedPreferences(TipUtils.SHARED_PREF_FILE, 0);

        float billTotal = sp.getFloat(TipUtils.BILL_TOTAL_KEY, 0.0f);
        float tipPercentage = sp.getFloat(TipUtils.TIP_PERCENTAGE_KEY, 0.0f);
        float tipTotal = sp.getFloat(TipUtils.TIP_TOTAL_KEY, 0.0f);
        float totalWithTip = sp.getFloat(TipUtils.TOTAL_WITH_TIP_KEY, 0.0f);
        long numPeople = (long) sp.getFloat(TipUtils.NUM_PEOPLE_KEY, 1.0f);
        float totalPerPerson = sp.getFloat(TipUtils.TOTAL_PER_PERSON_KEY, 0.0f);
        float tipPerPerson = sp.getFloat(TipUtils.TIP_PER_PERSON_KEY, 0.0f);

        prefValues.add(String.valueOf(billTotal));
        prefValues.add(String.valueOf(tipPercentage));
        prefValues.add(String.valueOf(tipTotal));
        prefValues.add(String.valueOf(totalWithTip));
        prefValues.add(String.valueOf(numPeople));
        prefValues.add(String.valueOf(totalPerPerson));
        prefValues.add(String.valueOf(tipPerPerson));

        return prefValues;
    }


    private static void setOnClickListeners(Context context, int appWidgetId, RemoteViews remoteViews) {
        final Intent intent = new Intent(context, TipWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);


        int shifttedAppWidgetId = appWidgetId << 3;

        intent.setAction(INCREMENT_BILL_TOTAL);
        remoteViews.setOnClickPendingIntent(R.id.bill_increment_btn, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 0, intent, 0));

        intent.setAction(DECREMENT_BILL_TOTAL);
        remoteViews.setOnClickPendingIntent(R.id.bill_decrement_btn, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 1, intent, 0));

        intent.setAction(INCREMENT_TIP_PERCENTAGE);
        remoteViews.setOnClickPendingIntent(R.id.tip_pct_increment_btn, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 2, intent, 0));

        intent.setAction(DECREMENT_TIP_PERCENTAGE);
        remoteViews.setOnClickPendingIntent(R.id.tip_pct_decrement_btn, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 3, intent, 0));

        intent.setAction(INCREMENT_NUM_PEOPLE);
        remoteViews.setOnClickPendingIntent(R.id.num_people_increment_btn, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 4, intent, 0));

        intent.setAction(DECREMENT_NUM_PEOPLE);
        remoteViews.setOnClickPendingIntent(R.id.num_people_decrement_btn, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 5, intent, 0));

        intent.setAction(REFRESH);
        remoteViews.setOnClickPendingIntent(R.id.button_refresh, PendingIntent.getBroadcast(context, shifttedAppWidgetId + 6, intent, 0));

    }
}

