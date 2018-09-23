package com.example.rebelor.carre_mobile.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rebelor.carre_mobile.sqlite.type.RiskEvidence;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class RiskEvidenceDataSource {

    // Database fields
    private SQLiteDatabase database;
    //private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.RISK_EVIDENCES_ID,
            MySQLiteHelper.RISK_EVIDENCES_RISK_EVIDENCE,
            MySQLiteHelper.RISK_EVIDENCES_CONDITON,
            MySQLiteHelper.RISK_EVIDENCES_CONFIDENCE_INTERVAL_MIN,
            MySQLiteHelper.RISK_EVIDENCES_CONFIDENCE_INTERVAL_MAX,
            MySQLiteHelper.RISK_EVIDENCES_RATIO_VALUE,
            MySQLiteHelper.RISK_EVIDENCES_RATIO_TYPE,
            MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR,
            MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR_SOURCE,
            MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR_TARGET,
            MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR_ASSOCIATION_TYPE};

    public RiskEvidenceDataSource(SQLiteDatabase data) {
        database = data;
    }

    public RiskEvidence addRiskEvidence(RiskEvidence rv){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.RISK_EVIDENCES_RISK_EVIDENCE, rv.getRiskEvidence());
        values.put(MySQLiteHelper.RISK_EVIDENCES_CONDITON, rv.getCondition());
        values.put(MySQLiteHelper.RISK_EVIDENCES_CONFIDENCE_INTERVAL_MIN, rv.getConfidenceIntervalMin());
        values.put(MySQLiteHelper.RISK_EVIDENCES_CONFIDENCE_INTERVAL_MAX, rv.getConfidenceIntervalMax());
        values.put(MySQLiteHelper.RISK_EVIDENCES_RATIO_TYPE, rv.getRatioType());
        values.put(MySQLiteHelper.RISK_EVIDENCES_RATIO_VALUE, rv.getRatioValue());
        values.put(MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR, rv.getRiskFactor());
        values.put(MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR_SOURCE, rv.getRiskFactorSource());
        values.put(MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR_TARGET, rv.getRiskFactorTarget());
        values.put(MySQLiteHelper.RISK_EVIDENCES_RISK_FACTOR_ASSOCIATION_TYPE, rv.getRiskFactorAssociationType());

        Cursor cursor = database.query(MySQLiteHelper.TABLE_RISK_EVIDENCES, allColumns,
                MySQLiteHelper.RISK_EVIDENCES_ID + " = " + rv.getId(), null, null, null, null);
        RiskEvidence newRiskEvidence = null;
        long insertId = -1;
        if(cursor.getCount() == 1){
            insertId = database.replace(MySQLiteHelper.TABLE_RISK_EVIDENCES, null, values);
        }else{
            insertId = database.insert(MySQLiteHelper.TABLE_RISK_EVIDENCES, null, values);
        }
        cursor.close();

        if(insertId != -1){

            //IS FASTER FROM PREVIOUS
            newRiskEvidence = new RiskEvidence();
//            newRiskEvidence.setId(insertId);
            newRiskEvidence.setRiskEvidence(rv.getRiskEvidence());
            newRiskEvidence.setCondition(rv.getCondition());
            newRiskEvidence.setConfidenceIntervalMin(rv.getConfidenceIntervalMin());
            newRiskEvidence.setConfidenceIntervalMax(rv.getConfidenceIntervalMax());
            newRiskEvidence.setRatioType(rv.getRatioType());
            newRiskEvidence.setRatioValue(rv.getRatioValue());
            newRiskEvidence.setRiskFactor(rv.getRiskFactor());
            newRiskEvidence.setRiskFactorSource(rv.getRiskFactorSource());
            newRiskEvidence.setRiskFactorTarget(rv.getRiskFactorTarget());
            newRiskEvidence.setRiskFactorAssociationType(rv.getRiskFactorAssociationType());
        }

        return newRiskEvidence;
    }

    public List<RiskEvidence> getAllRiskEvidences(){
        List<RiskEvidence> risk_evidences = new ArrayList<RiskEvidence>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_RISK_EVIDENCES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RiskEvidence rv= cursorToRiskEvidence(cursor);
            risk_evidences.add(rv);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return risk_evidences;
    }

    public void deleteAllRiskEvidences(){
        database.delete(MySQLiteHelper.TABLE_RISK_EVIDENCES, null, null);
        Log.d(TAG, "TABLE_RISK_EVIDENCES deleted");
    }

    public RiskEvidence cursorToRiskEvidence(Cursor cursor){
        RiskEvidence risk_evidence = new RiskEvidence();
        risk_evidence.setId(cursor.getLong(0));
        risk_evidence.setRiskEvidence(cursor.getString(1));
        risk_evidence.setCondition(cursor.getString(2));
        risk_evidence.setConfidenceIntervalMin(cursor.getFloat(3));
        risk_evidence.setConfidenceIntervalMax(cursor.getFloat(4));
        risk_evidence.setRatioValue(cursor.getFloat(5));
        risk_evidence.setRatioType(cursor.getString(6));
        risk_evidence.setRiskFactor(cursor.getString(7));
        risk_evidence.setRiskFactorSource(cursor.getString(8));
        risk_evidence.setRiskFactorTarget(cursor.getString(9));
        risk_evidence.setRiskFactorAssociationType(cursor.getString(10));
        return risk_evidence;
    }
}
