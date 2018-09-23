package com.example.rebelor.carre_mobile.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rebelor.carre_mobile.CalculateRiskRatios;
import com.example.rebelor.carre_mobile.R;
import com.example.rebelor.carre_mobile.sqlite.HealthDataDatasource;
import com.example.rebelor.carre_mobile.sqlite.MySQLiteHelper;
import com.example.rebelor.carre_mobile.sqlite.RiskElementDataSource;
import com.example.rebelor.carre_mobile.sqlite.type.RiskElement;
import com.example.rebelor.carre_mobile.sqlite.type.RiskEvidence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserGoalsFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "EXTRA MESSAGE";

    private static MySQLiteHelper dman;
    private static SQLiteDatabase db;

    Button calculateButton;
    TextView textView;
    Spinner physicalActivitySpinner, smokerSpinner;

    public static final UserGoalsFragment newInstance(String message){
        UserGoalsFragment f = new UserGoalsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);

        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.activity_usergoals, container, false);

        dman = MySQLiteHelper.getInstance(getActivity());
        db = dman.getDatabase();

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){

        if(isVisibleToUser) {
            if (getActivity() != null) {
                getActivity().setTitle("CARRE: User Goals");



//               CreateGoals(physicalActivity, smoker);
                int[] physicalRatios = NewRisks("physical");
                int[] smokerRatios = NewRisks("smoker");

                String physicalTXT = "Changing your physical activity has the following results.\n"
                        + NewRiskDifference(physicalRatios);

                textView = (TextView) getView().findViewById(R.id.physicalActivityTextView);
                textView.setText(physicalTXT);


                String smokerTXT = "";
                //Check if user is a smoker or not
                HealthDataDatasource hdSource = new HealthDataDatasource(db);
                if (hdSource.getLastHealthData().getSmoker().equals("smoker"))
                    smokerTXT = "On the other hand, changing your smoking habit has the following results.\n"
                        + NewRiskDifference(smokerRatios);

                textView = (TextView) getView().findViewById(R.id.smokerTextView);
                textView.setText(smokerTXT);

                String resultTXT = "";
                if (physicalRatios[0] > smokerRatios[0]){
                    resultTXT = "Based on the improved improved risk factors," +
                            " it would be more beneficial if you exercise more.";
                }
                else if (physicalRatios[0] < smokerRatios[0]) {
                    resultTXT = "Based on the improved risk factors," +
                            " it would be more beneficial if you quit smoking.";
                }

                textView = (TextView) getView().findViewById(R.id.resultTextView);
                textView.setText(resultTXT);

            }

        }
    }



    public String NewRiskDifference(int[] observable){
        String newRiskFactors = "Risk Factors improved: " + String.valueOf(observable[0]) +
                "\nRisk Factors that disappeared: " + String.valueOf(observable[1]) +
                "\nRisk Factors that appeared: " + String.valueOf(observable[2]);

        return newRiskFactors;
    }


    public int[] NewRisks(String ObservableToChange){ //int [3] -> improved, old, new

        HealthDataDatasource hdSource = new HealthDataDatasource(db);

        String[] newHealthDataVector, oldHealthDataVector;
        List<RiskEvidence> oldUserRiskEvidences;
        List<RiskEvidence> newUserRiskEvidences;

        String currentPhysical = hdSource.getLastHealthData().getPhysicalActivity();
        String currentSmoker = hdSource.getLastHealthData().getSmoker();

        String newPhysical = "";
        String newSmoker = "";

        String[] physicalArray = new String[]{"no", "low", "moderate", "high"};

        Map<String, String> changedPHR = new HashMap<>();
        // Choose better physical activity
        if (ObservableToChange.equals("physical")) {
            for (int i = 0; i < physicalArray.length; i++) {
                if (physicalArray[i].equals(currentPhysical) && !currentPhysical.equals("high")) {
                    newPhysical = physicalArray[i + 1];
                    break;
                }
            }
            changedPHR.put("OB_58", newPhysical); //physical activity
        }
        // Choose better smoking condition
        if (ObservableToChange.equals("smoker"))
            currentSmoker = "ex-smoker";
        changedPHR.put("OB_65", currentSmoker); //smoking

        oldHealthDataVector = hdSource.HealthDataVector(null);
        newHealthDataVector = hdSource.HealthDataVector(changedPHR);

        oldUserRiskEvidences = new CalculateRiskRatios().CalculateRiskRatios(db, oldHealthDataVector);
        newUserRiskEvidences = new CalculateRiskRatios().CalculateRiskRatios(db, newHealthDataVector);

        //find which list is longer
        List<RiskEvidence> longList;
        List<RiskEvidence> shortList;
        if (oldUserRiskEvidences.size() > newUserRiskEvidences.size()) {
            longList = oldUserRiskEvidences;
            shortList = newUserRiskEvidences;
        }
        else {
            longList = newUserRiskEvidences;
            shortList = oldUserRiskEvidences;
        }

        // --------------------
        // In this section we find new risks and risk factor changes. We pick which list is
        // longer (old risks or new risks) and then substract the shorter from the longer.
        // --------------------

//        textView = (TextView) getView().findViewById(R.id.physicalActivityTextView);
        String txt = "";

        List<RiskEvidence> differentRisks =
                riskEvidenceDifference(longList, shortList);

        // find differences in risk factors and remove them from differentRisks
        List<RiskEvidence> riskFactorChanges = new ArrayList<>();

        List<String> toDelete = new ArrayList<>();

        for (int i = 0; i < differentRisks.size(); i++){
            for (RiskEvidence oldRV:oldUserRiskEvidences){
                if (differentRisks.get(i).getRiskFactorSource().equals(oldRV.getRiskFactorSource()) &&
                        differentRisks.get(i).getRiskFactorTarget().equals(oldRV.getRiskFactorTarget())
                        && !differentRisks.get(i).getRiskEvidence().equals(oldRV.getRiskEvidence())){
                    //add both old and new riskevidences in a list
                    riskFactorChanges.add(oldRV);
                    riskFactorChanges.add(differentRisks.get(i));
                    differentRisks.remove(i);
                    i--;
                    // we need to delete duplicates created from riskEvidenceDifference method
                    // so we create a list to search them later
                    toDelete.add(oldRV.getRiskEvidence());
                    break;
                }
            }
        }

        // loop for deleting duplicates
        for (int i=0; i < differentRisks.size(); i++){
            for (String s:toDelete){
                if (differentRisks.get(i).getRiskEvidence().equals(s)){
                    differentRisks.remove(i);
                    i--;
                }
            }
        }

        // changed risk factors
        int improvedRiskFactors = 0;
        for (int i = 0; i < riskFactorChanges.size(); i = i + 2){

            RiskEvidence oldrv = riskFactorChanges.get(i);
            RiskEvidence newrv = riskFactorChanges.get(i+1);
            String RFSource = findRiskElement(oldrv.getRiskFactorSource());
            String RFTarget = findRiskElement(oldrv.getRiskFactorTarget());

            if (oldrv.getRatioValue() > newrv.getRatioValue())
                improvedRiskFactors++;

            txt += "Risk factor change: " + RFSource + " " + oldrv.getRiskFactorAssociationType()
                    + " " + RFTarget + " changed from " + oldrv.getRatioValue() + " to "
                    + newrv.getRatioValue() + "\n";
        }

        // old(removed) risk factors
        int oldRiskFactors = 0;
        for (int i = 0; i < differentRisks.size(); i++){
            RiskEvidence rv = differentRisks.get(i);
            String RFSource = findRiskElement(rv.getRiskFactorSource());
            String RFTarget = findRiskElement(rv.getRiskFactorTarget());

            for (RiskEvidence oldrv:oldUserRiskEvidences){
                if (oldrv.getRiskEvidence().equals(rv.getRiskEvidence())) {
                    txt += "Old risk (disappeared): " + RFSource + " " + rv.getRiskFactorAssociationType()
                            + " " + RFTarget + " with ratio " + rv.getRatioValue() + " (" +
                            rv.getRiskEvidence() + ")" + "\n";
                    differentRisks.remove(i);
                    i--;
                    oldRiskFactors++;
                    break;
                }
            }
        }

        // new risk factors
        int newRiskFactors = 0;
        for (int i = 0; i < differentRisks.size(); i++){
            RiskEvidence rv = differentRisks.get(i);
            String RFSource = findRiskElement(rv.getRiskFactorSource());
            String RFTarget = findRiskElement(rv.getRiskFactorTarget());

            newRiskFactors++;
            txt += "New risk (emerged): " + RFSource + " " + rv.getRiskFactorAssociationType()
                    + " " + RFTarget + " with ratio " + rv.getRatioValue() + " (" +
                    rv.getRiskEvidence() + ")" + "\n";
        }

        txt = "Risk Factors improved: " + Integer.toString(improvedRiskFactors) +
                "\nOld Risk Factors: " + Integer.toString(oldRiskFactors) +
                "\nNew Risk Factors: " + Integer.toString(newRiskFactors);

        return new int[]{improvedRiskFactors, oldRiskFactors, newRiskFactors};

    }

    public List<RiskEvidence> riskEvidenceDifference(List<RiskEvidence> oldList, List<RiskEvidence> newList){

        //remove common newRisks from oldRisks
        List<RiskEvidence> tmpOldList = new ArrayList<>(oldList);
        List<RiskEvidence> tmpNewList = new ArrayList<>(newList);

        for (int i = 0; i< tmpOldList.size(); i++){
            for (RiskEvidence newrv:newList){
                if (tmpOldList.get(i).getRiskEvidence().equals(newrv.getRiskEvidence())){
                    tmpOldList.remove(i);
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i< tmpNewList.size(); i++){
            for (RiskEvidence oldrv:oldList){
                if (tmpNewList.get(i).getRiskEvidence().equals(oldrv.getRiskEvidence())){
                    tmpNewList.remove(i);
                    i--;
                    break;
                }
            }
        }
//
//        for (int i = 0; i< tmpOldList.size(); i++){
//            for (RiskEvidence rv: newList){
//                if (tmpOldList.get(i).getRiskEvidence().equals(rv.getRiskEvidence())){
//                    tmpOldList.remove(i);
//                    i--;
//                    break;
//                }
//            }
//        }

        tmpOldList.addAll(tmpNewList);
        return tmpOldList;
    }

    String findRiskElement(String id){
        List<RiskElement> riskElements = new RiskElementDataSource(db).getAllRiskElements();

        for(RiskElement rl:riskElements) {
            if (rl.getRiskelement().equals(id)){
                return rl.getName();
            }
        }
        return null;
    }
}
