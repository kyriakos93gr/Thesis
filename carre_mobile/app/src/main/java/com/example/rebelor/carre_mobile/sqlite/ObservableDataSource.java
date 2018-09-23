package com.example.rebelor.carre_mobile.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rebelor.carre_mobile.sqlite.type.Observable;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class ObservableDataSource {

    // Database fields
    private SQLiteDatabase database;

    private String[] allColumns = { MySQLiteHelper.OBSERVABLES_ID,
            MySQLiteHelper.OBSERVABLES_OBSERVABLE,
            MySQLiteHelper.OBSERVABLES_NAME,
            MySQLiteHelper.OBSERVABLES_MEASUREMENT_TYPE,
            MySQLiteHelper.OBSERVABLES_MODIFIABLE,
    };

    public ObservableDataSource(SQLiteDatabase data) {
        database = data;
    }

    public Observable addObservable(Observable ob) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.OBSERVABLES_OBSERVABLE, ob.getObservable());
        values.put(MySQLiteHelper.OBSERVABLES_NAME, ob.getName());
        values.put(MySQLiteHelper.OBSERVABLES_MEASUREMENT_TYPE, ob.getMeasurement());
        values.put(MySQLiteHelper.OBSERVABLES_MODIFIABLE, ob.getModifiable());


        Cursor cursor = database.query(MySQLiteHelper.TABLE_OBSERVABLES, allColumns,
                null, null, null, null, null);
        Observable newObservable = null;
        long insertId = -1;
        if (cursor.getCount() == 1) {
            insertId = database.replace(MySQLiteHelper.TABLE_OBSERVABLES, null, values);
        } else {
            insertId = database.insert(MySQLiteHelper.TABLE_OBSERVABLES, null, values);
        }
        cursor.close();

        if (insertId != -1) {

            //IS FASTER FROM PREVIOUS
            newObservable = new Observable();
//            newObservable.setId(insertId);
            newObservable.setObservable(ob.getObservable());
            newObservable.setName(ob.getName());
            newObservable.setMeasurement(ob.getMeasurement());
            newObservable.setModifiable(ob.getModifiable());
        }
        return newObservable;

    }

    public List<Observable> getAllObservables(){
        List<Observable> observables = new ArrayList<Observable>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_OBSERVABLES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Observable ob = cursorToObservable(cursor);
            observables.add(ob);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return observables;
    }

    public void deleteAllObservables(){
        database.delete(MySQLiteHelper.TABLE_OBSERVABLES, null, null);
        Log.d(TAG, "TABLE_OBSERVABLES deleted");
    }

    public Observable cursorToObservable(Cursor cursor){
        Observable observable = new Observable();
        observable.setId(cursor.getLong(0));
        observable.setObservable(cursor.getString(1));
        observable.setName(cursor.getString(2));
        observable.setMeasurement(cursor.getString(3));
        observable.setModifiable(cursor.getString(4));

        return observable;
    }
}
