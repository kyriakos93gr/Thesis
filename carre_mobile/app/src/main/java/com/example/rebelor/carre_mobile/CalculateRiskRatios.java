package com.example.rebelor.carre_mobile;



import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.rebelor.carre_mobile.sqlite.HealthDataDatasource;
import com.example.rebelor.carre_mobile.sqlite.MySQLiteHelper;
import com.example.rebelor.carre_mobile.sqlite.RiskElementDataSource;
import com.example.rebelor.carre_mobile.sqlite.RiskEvidenceDataSource;
import com.example.rebelor.carre_mobile.sqlite.type.RiskElement;
import com.example.rebelor.carre_mobile.sqlite.type.RiskEvidence;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class CalculateRiskRatios{


    public List<RiskEvidence> UserRiskEvidences = new ArrayList<RiskEvidence>();


    public List<RiskEvidence> CalculateRiskRatios(SQLiteDatabase db, String[] HealthDataVector){


        //Calls getLastHealthData which initialises the HealthDataVector
        new HealthDataDatasource(db).getLastHealthData();

        RiskEvidenceDataSource getRiskEvidences = new RiskEvidenceDataSource(db);
        RiskElementDataSource getRiskElements = new RiskElementDataSource(db);

        List<RiskEvidence> TotalRiskEvidences = getRiskEvidences.getAllRiskEvidences();
        List<RiskElement> TotalRiskElements = getRiskElements.getAllRiskElements();

        String txtToShow = "";
        String rvSource = "";
        String rvTarget = "";
        UserRiskEvidences.clear();
        for (RiskEvidence rv: TotalRiskEvidences){
            String tmp = "";
            for (int i=0; i < HealthDataVector.length; i++)
                tmp += HealthDataVector[i] + "    ";
//                Log.d(TAG, tmp);
            String condition = rv.getCondition();
//            Log.d(TAG, condition);
            if (RiskEvidenceConditionParser.evaluate(condition, HealthDataVector) == true) {
//                Log.d(TAG, rv.getRiskEvidence() + "  +  "  + tmp);
//                Log.d(TAG, condition);
                for (RiskElement rl : TotalRiskElements) {
//                    Log.d(TAG, rl.getRiskelement() + "     " + rv.getRiskFactorSource());
                    if (Objects.equals(rl.getRiskelement(), (rv.getRiskFactorSource()))) {
                        rvSource = rl.getName();
//                        Log.d(TAG, "SOURCE: " + rl.getName());
                    }
                    if (Objects.equals(rl.getRiskelement(), (rv.getRiskFactorTarget()))){
                        rvTarget = rl.getName();
//                        Log.d(TAG, "TARGET: " + rl.getName());
                    }
                }
                //Add RiskEvidence to the list
                if (!(rv.getRatioValue()==-1))
                    UserRiskEvidences.add(rv);

            }

        }
        return UserRiskEvidences;
    }
}
