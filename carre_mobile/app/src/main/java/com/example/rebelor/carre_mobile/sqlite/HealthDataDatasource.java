package com.example.rebelor.carre_mobile.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rebelor.carre_mobile.sqlite.type.HealthData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class HealthDataDatasource {

    //Hardcoded health data to observables
    public Map<String, String> HealthDataMapping = new HashMap<>();

    public static String[] HealthDataVector = new String[]{};

    // Database fields
    private SQLiteDatabase database;
    //private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.HEALTH_DATA_ID,
            MySQLiteHelper.HEALTH_DATA_SEX,
            MySQLiteHelper.HEALTH_DATA_AGE,
            MySQLiteHelper.HEALTH_DATA_SMOKER,
            MySQLiteHelper.HEALTH_DATA_PHYSICAL_ACTIVITY,
            MySQLiteHelper.HEALTH_DATA_CHRONIC_KIDNEY,
            MySQLiteHelper.HEALTH_DATA_HEIGHT,
            MySQLiteHelper.HEALTH_DATA_MASS,
            MySQLiteHelper.HEALTH_DATA_HYPERTENSION,
            MySQLiteHelper.HEALTH_DATA_DIABETES,
            MySQLiteHelper.HEALTH_DATA_BMI,
            MySQLiteHelper.HEALTH_DATA_TIMESTAMP
    };

    public HealthDataDatasource(SQLiteDatabase data) {
        database = data;
        HealthDataMapping.put("sex", "OB_64");
        HealthDataMapping.put("age", "OB_7");
        HealthDataMapping.put("smoker", "OB_65");
        HealthDataMapping.put("physical_activity", "OB_58");
        HealthDataMapping.put("chronic_kidney", "OB_21");
        HealthDataMapping.put("height", "OB_88");
        HealthDataMapping.put("mass", "OB_84");
        HealthDataMapping.put("hypertension", "OB_42");
        HealthDataMapping.put("diabetes", "OB_27");
        HealthDataMapping.put("bmi", "OB_17");

    }

    public HealthData addHealthData(HealthData hd){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.HEALTH_DATA_SEX, hd.getSex());
        values.put(MySQLiteHelper.HEALTH_DATA_AGE, hd.getAge());
        values.put(MySQLiteHelper.HEALTH_DATA_SMOKER, hd.getSmoker());
        values.put(MySQLiteHelper.HEALTH_DATA_PHYSICAL_ACTIVITY, hd.getPhysicalActivity());
        values.put(MySQLiteHelper.HEALTH_DATA_CHRONIC_KIDNEY, hd.getChronicKidney());
        values.put(MySQLiteHelper.HEALTH_DATA_HEIGHT, hd.getHeight());
        values.put(MySQLiteHelper.HEALTH_DATA_MASS, hd.getMass());
        values.put(MySQLiteHelper.HEALTH_DATA_HYPERTENSION, hd.getHypertension());
        values.put(MySQLiteHelper.HEALTH_DATA_DIABETES, hd.getDiabetes());
        values.put(MySQLiteHelper.HEALTH_DATA_BMI, hd.getBmi());
        values.put(MySQLiteHelper.HEALTH_DATA_TIMESTAMP, hd.getTimestamp());

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HEALTH_DATA, allColumns,
                MySQLiteHelper.HEALTH_DATA_ID + " = " + hd.getId(), null, null, null, null);
        HealthData newHealthData = null;
        long insertId = -1;
        if(cursor.getCount() == 1){
            insertId = database.replace(MySQLiteHelper.TABLE_HEALTH_DATA, null, values);
        }else{
            insertId = database.insert(MySQLiteHelper.TABLE_HEALTH_DATA, null, values);
        }
        cursor.close();

        if(insertId != -1){

            //IS FASTER FROM PREVIOUS
            newHealthData = new HealthData();
            newHealthData.setId(insertId);
            newHealthData.setSex(hd.getSex());
            newHealthData.setAge(hd.getAge());
            newHealthData.setSmoker(hd.getSmoker());
            newHealthData.setPhysicalActivity(hd.getPhysicalActivity());
            newHealthData.setChronicKidney(hd.getChronicKidney());
            newHealthData.setHeight(hd.getHeight());
            newHealthData.setMass(hd.getMass());
            newHealthData.setHypertension(hd.getHypertension());
            newHealthData.setDiabetes(hd.getDiabetes());
            newHealthData.setBmi(hd.getBmi());
            newHealthData.setTimestamp(hd.getTimestamp());
        }

        return newHealthData;
    }

    public List<HealthData> getAllHealthData(){
        List<HealthData> health_data = new ArrayList<HealthData>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HEALTH_DATA, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HealthData hd = cursorToHealthData(cursor);
            health_data.add(hd);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return health_data;
    }

    public HealthData getLastHealthData(){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_HEALTH_DATA, allColumns, null, null,
                null, null, null);
        cursor.moveToLast();
        if (cursor.isNull(0) != true) {
            HealthData hd = cursorToHealthData(cursor);
            cursor.close();
            return hd;
        } else {
            Log.d(TAG, "Health Data Empty");
            cursor.close();
            return new HealthData();
        }
    }

    public void deleteAllHealthData(){
        database.delete(MySQLiteHelper.TABLE_HEALTH_DATA, null, null);
        Log.d(TAG, "TABLE_HEALTH_DATA deleted");
    }

    public HealthData cursorToHealthData(Cursor cursor){
        HealthData health_data = new HealthData();
        health_data.setId(cursor.getInt(0));
        health_data.setSex(cursor.getString(1));
        health_data.setAge(cursor.getInt(2));
        health_data.setSmoker(cursor.getString(3));
        health_data.setPhysicalActivity(cursor.getString(4));
        health_data.setChronicKidney(cursor.getString(5));
        health_data.setHeight(cursor.getDouble(6));
        health_data.setMass(cursor.getDouble(7));
        health_data.setHypertension(cursor.getString(8));
        health_data.setDiabetes(cursor.getString(9));
        health_data.setBmi(cursor.getDouble(10));
        health_data.setTimestamp(cursor.getLong(11));
        return health_data;
    }


     int compare(String o1, String o2) {
         String oo1, oo2;
         oo1 = o1.replaceAll("\\D", "");
         oo2 = o2.replaceAll("\\D", "");

         return Integer.parseInt(oo1)-Integer.parseInt(oo2);
    }

    public String[] HealthDataVector(Map map){

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HEALTH_DATA, allColumns, null, null,
                null, null, null);
        cursor.moveToLast();
        if (cursor.isNull(0) != true) {
            String[] columnNames = cursor.getColumnNames();
            List<String> tmpVector = new ArrayList<>();

            // Create Health Data Vector
            for (String str : columnNames) {
                if (HealthDataMapping.containsKey(str)) {
                    tmpVector.add(HealthDataMapping.get(str));
                    tmpVector.add(cursor.getString(cursor.getColumnIndex(str)));
                }
            }

            //if no map is given then calculates health vector based on current phr
            if (map != null){
                for (int i = 0; i < tmpVector.size(); i++){
                    if (map.containsKey(tmpVector.get(i).toString())){
                        tmpVector.set(i+1, map.get(tmpVector.get(i).toString()).toString());
                    }
                }
            }
            
            String[] HealthDataVector = tmpVector.toArray(new String[0]);
            cursor.close();

            //Sort HealthDataVector
            String tmp1, tmp2;
            for (int k =0; k < HealthDataVector.length-1; k+=2) {
                for (int i = 0; i < HealthDataVector.length - 2; i += 2) {
                    if (compare((HealthDataVector[i]), HealthDataVector[i+2]) < 0){
                        tmp1 = HealthDataVector[i];
                        HealthDataVector[i] = HealthDataVector[i + 2];
                        HealthDataVector[i + 2] = tmp1;
                        tmp2 = HealthDataVector[i + 1];
                        HealthDataVector[i + 1] = HealthDataVector[i + 3];
                        HealthDataVector[i + 3] = tmp2;
                    }
                }
            }

            return HealthDataVector;
        } else {
            Log.d(TAG, "Health Data Empty");
            cursor.close();
            return null;
        }

    }

}
