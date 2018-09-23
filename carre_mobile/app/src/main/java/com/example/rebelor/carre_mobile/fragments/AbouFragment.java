package com.example.rebelor.carre_mobile.fragments;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rebelor.carre_mobile.R;
import com.example.rebelor.carre_mobile.sqlite.MySQLiteHelper;
import com.example.rebelor.carre_mobile.sqlite.ObservableDataSource;
import com.example.rebelor.carre_mobile.sqlite.RiskElementDataSource;
import com.example.rebelor.carre_mobile.sqlite.RiskEvidenceDataSource;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class AbouFragment extends Fragment {

    static ProgressDialog progress;
    static boolean enteredProgressDialog;

    private static MySQLiteHelper dman;
    private static SQLiteDatabase db;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final AbouFragment newInstance(String message){

        AbouFragment f = new AbouFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);

        return f;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.activity_about, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle){
        super.onActivityCreated(savedInstanceBundle);

        dman = MySQLiteHelper.getInstance(getActivity());
        db = dman.getDatabase();

        enteredProgressDialog = false;
        if(dman.checkEmpty(db)) {
            progress = ProgressDialog.show(getActivity(), "Updating Database",
                    "Please wait while database is being downloaded \n " +
                            "(Internet connection required)");

            enteredProgressDialog = true;

            progress.setCancelable(false);
            progress.isIndeterminate();
            progress.show();
        }

        setData();
        chartModification();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){

        if(isVisibleToUser) {
            if (getActivity() != null) {
                getActivity().setTitle("CARRE: Database");

                setData();
                chartModification();
            }
        }
    }


    public void chartModification(){
        PieChart piechart = (PieChart) getView().findViewById(R.id.about_chart);

        //Remove Description
        Description desc = new Description();
        desc.setText("");
        piechart.setDescription(desc);

        //Disable rotation
        piechart.setRotationEnabled(false);

        //Use offset because chart goes out of screen
        piechart.setExtraOffsets(10, 0, 10, 0);

        //Animation
        piechart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        //Hole in center
        piechart.setHoleRadius(45f);

        //Set center text
        piechart.setCenterText("Medical\nKnowledge\nStatistics");

        //Remove legend
        Legend l = piechart.getLegend();
        l.setEnabled(false);


        piechart.invalidate();

    }

    public void setData(){
        PieChart piechart = (PieChart) getView().findViewById(R.id.about_chart);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) count_riskEvidences(db), "Risk Evidences"));
        entries.add(new PieEntry((float) count_riskElements(db), "Risk Elements"));
        entries.add(new PieEntry((float) count_observables(db), "Observables"));
        entries.add(new PieEntry((float) count_riskFactors(db), "Risk Factors"));

        PieDataSet set = new PieDataSet(entries, "Database Records");

        //set colors
        set.setColors(ColorTemplate.PASTEL_COLORS);

        //Draw values outside chart
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setValueTextSize(11f);

        PieData data = new PieData(set);
        piechart.setData(data);
        piechart.invalidate();

    }

    public static void dismissProgress(){

        if(enteredProgressDialog) {
            progress.dismiss();

        }
    }

    public int count_riskEvidences(SQLiteDatabase db){
        return new RiskEvidenceDataSource(db).getAllRiskEvidences().size();
    }

    public int count_riskElements(SQLiteDatabase db){
        return new RiskElementDataSource(db).getAllRiskElements().size();
    }

    public int count_observables(SQLiteDatabase db){
        return new ObservableDataSource(db).getAllObservables().size();
    }

    public int count_riskFactors(SQLiteDatabase db){
        return dman.countRiskFactors(db);
    }

}
