package com.example.rebelor.carre_mobile.sqlite;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rebelor.carre_mobile.sqlite.type.RiskElement;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RiskElementDataSource {
    // Database fields
    private SQLiteDatabase database;
    //private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.RISK_ELEMENTS_ID,
            MySQLiteHelper.RISK_ELEMENTS_RISK_ELEMENT,
            MySQLiteHelper.RISK_ELEMENTS_NAME,
            MySQLiteHelper.RISK_ELEMENTS_AUTHOR,
            MySQLiteHelper.RISK_ELEMENTS_OBSERVABLE,
            MySQLiteHelper.RISK_ELEMENTS_MODIFIABLE};

    public RiskElementDataSource(SQLiteDatabase data) {
        database = data;
    }

    public RiskElement addRiskElement(RiskElement rl) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.RISK_ELEMENTS_RISK_ELEMENT, rl.getRiskelement());
        values.put(MySQLiteHelper.RISK_ELEMENTS_NAME, rl.getName());
        values.put(MySQLiteHelper.RISK_ELEMENTS_AUTHOR, rl.getAuthor());
        values.put(MySQLiteHelper.RISK_ELEMENTS_OBSERVABLE, rl.getObservable());
        values.put(MySQLiteHelper.RISK_ELEMENTS_MODIFIABLE, rl.getModifiable());


        Cursor cursor = database.query(MySQLiteHelper.TABLE_RISK_ELEMENTS, allColumns,
                MySQLiteHelper.RISK_ELEMENTS_ID + " = " + rl.getId(), null, null, null, null);
        RiskElement newRiskElement = null;
        long insertId = -1;
        if (cursor.getCount() == 1) {
            insertId = database.replace(MySQLiteHelper.TABLE_RISK_ELEMENTS, null, values);
        } else {
            insertId = database.insert(MySQLiteHelper.TABLE_RISK_ELEMENTS, null, values);

        }
        cursor.close();

        if (insertId != -1) {

            //IS FASTER FROM PREVIOUS
            newRiskElement = new RiskElement();
//            newRiskElement.setId(insertId);
            newRiskElement.setRiskelement(rl.getRiskelement());
            newRiskElement.setName(rl.getName());
            newRiskElement.setAuthor(rl.getAuthor());
            newRiskElement.setObservable(rl.getObservable());
            newRiskElement.setModifiable(rl.getModifiable());
        }
        return newRiskElement;

    }

    public List<RiskElement> getAllRiskElements(){
        List<RiskElement> risk_elements = new ArrayList<RiskElement>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_RISK_ELEMENTS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RiskElement rl= cursorToRiskElement(cursor);
            risk_elements.add(rl);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return risk_elements;
    }

    public void deleteAllRiskElements(){
        database.delete(MySQLiteHelper.TABLE_RISK_ELEMENTS, null, null);
        Log.d(TAG, "TABLE_RISK_ELEMENTS deleted");
    }

    public RiskElement cursorToRiskElement(Cursor cursor){
        RiskElement risk_element = new RiskElement();
        risk_element.setId(cursor.getInt(0));
        risk_element.setRiskelement(cursor.getString(1));
        risk_element.setName(cursor.getString(2));
        risk_element.setAuthor(cursor.getString(3));
        risk_element.setObservable(cursor.getString(4));
        risk_element.setModifiable(cursor.getString(5));
        return risk_element;
    }


}