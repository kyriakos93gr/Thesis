package com.example.rebelor.carre_mobile.fragments;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.rebelor.carre_mobile.CalculateRiskRatios;
import com.example.rebelor.carre_mobile.MyXAxisValueFormatter;
import com.example.rebelor.carre_mobile.R;
import com.example.rebelor.carre_mobile.XYMarkerView;
import com.example.rebelor.carre_mobile.sqlite.HealthDataDatasource;
import com.example.rebelor.carre_mobile.sqlite.MySQLiteHelper;
import com.example.rebelor.carre_mobile.sqlite.RiskElementDataSource;
import com.example.rebelor.carre_mobile.sqlite.type.HealthData;
import com.example.rebelor.carre_mobile.sqlite.type.RiskElement;
import com.example.rebelor.carre_mobile.sqlite.type.RiskEvidence;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RiskEvidenceGraphFragment extends Fragment implements OnChartValueSelectedListener{

    private static MySQLiteHelper dman;
    private static SQLiteDatabase db;

    Spinner riskEvidenceSpinner;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final RiskEvidenceGraphFragment newInstance(String message){

        RiskEvidenceGraphFragment f = new RiskEvidenceGraphFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.activity_risk_evidence_chart, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle){
        super.onActivityCreated(savedInstanceBundle);

        dman = MySQLiteHelper.getInstance(getActivity());
        db = dman.getDatabase();

        populateSpinner(false);

        ImageButton show_graph_button = (ImageButton) getView().findViewById(R.id.show_graph_button);

        show_graph_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submit = riskEvidenceSpinner.getSelectedItem().toString();

                createBarChart(submit);
                setData(submit);


            }
        });



        if (getActivity() != null && riskEvidenceSpinner.getSelectedItem() != null){
            String riskEvidenceName = riskEvidenceSpinner.getSelectedItem().toString();

            createBarChart(riskEvidenceName);
            setData(riskEvidenceName);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

            String riskEvidenceName;

            BarChart chart = (BarChart) getView().findViewById(R.id.risk_evidence_chart);
            chart.invalidate();

            getActivity().setTitle("CARRE: My Personal Risks");

            if (riskEvidenceSpinner.getSelectedItem() != null) {
                populateSpinner(true);
//                riskEvidenceName = riskEvidenceSpinner.getSelectedItem().toString();
////
//                createBarChart(riskEvidenceName);
//                setData(riskEvidenceName);
            }else{
                //create chart that show no health records text

                BarChart emptyChart = (BarChart) getView().findViewById(R.id.risk_evidence_chart);
                emptyChart.setNoDataText("Update Your Health Records");
                emptyChart.invalidate();
            }
        }
    }

    public void createBarChart(String riskEvidenceName){

        BarChart chart = (BarChart) getView().findViewById(R.id.risk_evidence_chart);

        chart.setOnChartValueSelectedListener(this);


        List<RiskEvidence> RVForChart = new ArrayList<>();
        //Get Risk Evidences for user's choice
        for (List<RiskEvidence> rvlist:UserRiskEvidencesSortedList()){
            //Check if risk factor sources equals the given choice
            String tmpRLID = rvlist.get(0).getRiskFactorSource();
            if (findRiskElement(tmpRLID).equals(riskEvidenceName)){
                RVForChart = new ArrayList<>(rvlist);
                break;
            }
        }


        //Create axis values
        String[] XAxisValues = XAxisValues(RVForChart);
        IAxisValueFormatter xAxisFormatter = new MyXAxisValueFormatter(XAxisValues);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(XAxisValues));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(false);

        //Remove Left axis
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //Limit line at 1
        YAxis yAxisLeft = chart.getAxisLeft();
        LimitLine ll = new LimitLine(1f);
        ll.setLineColor(Color.BLACK);
        ll.setLineWidth(4f);
        yAxisLeft.addLimitLine(ll);

        yAxisLeft.setAxisMinimum(0f);

        //Set animation
        chart.animateXY(3000, 3000);

        chart.resetZoom();


        //Marker on click
        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        //Remove Description
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);

        chart.invalidate(); // refresh

    }

    void setData(String riskEvidenceName){

        BarChart chart = (BarChart) getView().findViewById(R.id.risk_evidence_chart);

        //Removes old data
        if (chart.getData() != null) {
            chart.getData().removeDataSet(0);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        }

        List<RiskEvidence> RVForChart = new ArrayList<>();
        //Get Risk Evidences for user's choice
        for (List<RiskEvidence> rvlist:UserRiskEvidencesSortedList()){
            //Check if risk factor sources equals the given choice
            String tmpRLID = rvlist.get(0).getRiskFactorSource();
            if (findRiskElement(tmpRLID).equals(riskEvidenceName)){
                RVForChart = new ArrayList<>(rvlist);
                break;
            }
        }
        List<BarEntry> BarChartValues = BarChartValues(RVForChart);


        BarDataSet set;
        if (chart.getData() != null  && chart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(BarChartValues);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(BarChartValues, riskEvidenceName);

            set.setDrawIcons(false);

            set.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);


            chart.setData(data);
        }
    }

    List<BarEntry> BarChartValues(List<RiskEvidence> RiskEvidences) {

        List<BarEntry> entries = new ArrayList<BarEntry>();

        int i = 0;
        for (RiskEvidence data : RiskEvidences) {
            String ratio_type = data.getRatioType().substring(data.getRatioType().lastIndexOf("type_")+5);
            if (ratio_type.equals("relative_risk")){
                ratio_type = "RR";
            }
            else if (ratio_type.equals("odds_ratio")){
                ratio_type = "OR";
            }
            else if (ratio_type.equals("hazard_ratio")){
                ratio_type = "HR";
            }
            entries.add(new BarEntry(i, data.getRatioValue(), ratio_type));
            i++;
        }
        return entries;
    }

    void populateSpinner(boolean preselection_enable){

        Integer currentSelection = 0;

        riskEvidenceSpinner = (Spinner) getView().findViewById(R.id.risk_evidence_spinner);

        //boolean=true only when users rechooses a risk
        if (preselection_enable)
            currentSelection = riskEvidenceSpinner.getSelectedItemPosition();

        List<String> riskEvidenceCategories = findRiskFactorSources();

        ArrayAdapter<String> riskEvidenceAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, riskEvidenceCategories);

        riskEvidenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        riskEvidenceSpinner.setAdapter(riskEvidenceAdapter);

        riskEvidenceSpinner.setSelection(currentSelection);
    }

    //Find risk evidence sources from UserRiskEvidencesSortedList
    List<String> findRiskFactorSources(){

        List< List<RiskEvidence> > tempSortedList = UserRiskEvidencesSortedList();

        List<String> RVSources = new ArrayList<>();
        String RiskEvidenceID;

        for (List<RiskEvidence> rv:tempSortedList) {
            RiskEvidenceID = rv.get(0).getRiskFactorSource();
            RVSources.add(findRiskElement(RiskEvidenceID));
        }

        return RVSources;
    }

    //Values for Obesity Graph X-Axis
    String[] XAxisValues(List<RiskEvidence> RiskEvidences){

        List<String> tmpVector = new ArrayList<>();
        String RiskEvidenceID;

        for (RiskEvidence data : RiskEvidences) {
            RiskEvidenceID = data.getRiskFactorTarget();
            tmpVector.add(findRiskElement(RiskEvidenceID));
        }

        return tmpVector.toArray(new String[0]);
    }

    //Splits User's Risks to smaller lists based on risk_factor_source
    public List<List<RiskEvidence>> UserRiskEvidencesSortedList(){

        //creates Health Data vector
        new HealthDataDatasource(db).getLastHealthData();
        String[] HealthDataVector = new HealthDataDatasource(db).HealthDataVector(null);

        List<RiskEvidence> UserRiskEvidences = new ArrayList<>(
        new CalculateRiskRatios().CalculateRiskRatios(db, HealthDataVector));

        List< List<RiskEvidence> > tempList = new ArrayList<>();
        List< List<RiskEvidence> > tempSortedList = new ArrayList<>();

        List<RiskEvidence> ObesityRiskEvidences = new ArrayList<RiskEvidence>();
        for (RiskEvidence rv:UserRiskEvidences){
            if (rv.getRiskFactorSource().equals("RL_38"))
                ObesityRiskEvidences.add(rv);
        }

        String RFSource;
        Iterator<RiskEvidence> iter1;

        while (UserRiskEvidences.size() > 0){
            iter1 = UserRiskEvidences.iterator();
            List<RiskEvidence> singleRVList = new ArrayList<>(); //List with risk evidences of same source
            RFSource = UserRiskEvidences.get(0).getRiskFactorSource();
            while(iter1.hasNext()){
                RiskEvidence tmpRV = iter1.next();
                if (tmpRV.getRiskFactorSource().equals(RFSource)) {
                    singleRVList.add(tmpRV);
                    iter1.remove();
                }
            }
            tempList.add(singleRVList);
        }

        //Sort by more risk factor targets
        Iterator<List<RiskEvidence>> iter2;
        while (tempList.size() > 0) {
            iter2 = tempList.iterator();
            int listsize = 0;
            int counter = 0;
            //get biggest
            for (List <RiskEvidence> RVList:tempList){
                if (RVList.size() > listsize) {
                    listsize = RVList.size();
                }
            }
            while(iter2.hasNext()) {
                List<RiskEvidence> tmpRVList = iter2.next();
                if (tmpRVList.size() == listsize){
                    tempSortedList.add(tmpRVList);
                    iter2.remove();
                    break;
                }
            }
        }

        return tempSortedList;
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

    @Override
    public void onNothingSelected() { }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        BarChart chart = (BarChart) getView().findViewById(R.id.risk_evidence_chart);

        if (e == null)
            return;

//        Log.d(TAG, e.toString());
        RectF bounds = mOnValueSelectedRectF;
        chart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = chart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + chart.getLowestVisibleX() + ", high: "
                        + chart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

}
