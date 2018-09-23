package com.example.rebelor.carre_mobile.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.rebelor.carre_mobile.GetCarreDatabase;
import com.example.rebelor.carre_mobile.R;
import com.example.rebelor.carre_mobile.sqlite.HealthDataDatasource;
import com.example.rebelor.carre_mobile.sqlite.MySQLiteHelper;
import com.example.rebelor.carre_mobile.sqlite.type.HealthData;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyHealthRecordsFragment extends Fragment {

    private static MySQLiteHelper dman;
    private static SQLiteDatabase db;



    Button submitButton, cancelButton;
    RadioGroup sexRadioGroup;
    RadioButton sexRadioButton;
    EditText ageEditText, heightEditText, massEditText;
    Spinner smokerSpinner, physicalActivitySpinner, chronicKidneySpinner;
    CheckBox hypertensionChecked, diabetesChecked;

    //results
    String sex, smoker, physicalActivity, chronicKidney;
    int age;
    Double height, mass, bmi;
    String hypertension, diabetes;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final MyHealthRecordsFragment newInstance(String message){

        MyHealthRecordsFragment f = new MyHealthRecordsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.activity_my_health_records, container, false);


        dman = MySQLiteHelper.getInstance(getActivity());
        db = dman.getDatabase();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle){
        super.onActivityCreated(savedInstanceBundle);

        dman = MySQLiteHelper.getInstance(getActivity());
        db = dman.getDatabase();

        new GetCarreDatabase(db, getActivity()).fetchDatabase();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){

        if(isVisibleToUser) {

            getActivity().setTitle("CARRE: Personal Health Records");
            populateSelections();
            listener();
        }
    }



    public void listener(){

//        HealthData hd = new HealthDataDatasource(db).getLastHealthData();

        sexRadioGroup = (RadioGroup) getView().findViewById(R.id.sexRadioGroup);
        submitButton = (Button) getView().findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get sex
                int selectedSex = sexRadioGroup.getCheckedRadioButtonId();
                sexRadioButton = (RadioButton) getView().findViewById(selectedSex);
                sex = sexRadioButton.getText().toString().toLowerCase();

                //Get age
                ageEditText = (EditText) getView().findViewById(R.id.age);
                age = Integer.parseInt(ageEditText.getText().toString());

                //Get spinners
                smoker = smokerSpinner.getSelectedItem().toString().toLowerCase();
                physicalActivity = physicalActivitySpinner.getSelectedItem().toString().toLowerCase();
                String chrString = chronicKidneySpinner.getSelectedItem().toString().replaceAll("\\s+","");
                chronicKidney = chrString.substring(0, 1).toLowerCase() + chrString.substring(1);


                //Get height and mass
                heightEditText = (EditText) getView().findViewById(R.id.height);
                height = Double.parseDouble(heightEditText.getText().toString());
                massEditText = (EditText) getView().findViewById(R.id.mass);
                mass = Double.parseDouble(massEditText.getText().toString());
                bmi = mass/Math.pow((height/100), 2.0);

                //Get hypertensionand diabetes
                hypertensionChecked = (CheckBox) getView().findViewById(R.id.hypertension);
                diabetesChecked = (CheckBox) getView().findViewById(R.id.diabetes);

                hypertension = hypertensionChecked.isChecked() ? "yes" : "no";
                diabetes = diabetesChecked.isChecked() ? "yes" : "no";

                HealthData hd = new HealthData(sex, age, smoker, physicalActivity, chronicKidney, height, mass, hypertension, diabetes, bmi);
                new HealthDataDatasource(db).addHealthData(hd);

                Log.d(TAG, "Health Records updated");

            }
        });

    }


    public void populateSelections(){

        //Create spinners
        smokerSpinner = (Spinner) getView().findViewById(R.id.smoker);
        physicalActivitySpinner = (Spinner) getView().findViewById(R.id.physicalActivity);
        chronicKidneySpinner = (Spinner) getView().findViewById(R.id.chronicKidneySpinner);

        // Spinner click listener
        smokerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String selected = smokerSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        physicalActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String selected = smokerSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        chronicKidneySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String selected = smokerSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // Spinner Drop down elements
        List<String> smokeCategories = new ArrayList<>();
        smokeCategories.add("Never");
        smokeCategories.add("Smoker");
        smokeCategories.add("Ex-Smoker");

        List<String> physicalActivityCategories = new ArrayList<>();
        physicalActivityCategories.add("No");
        physicalActivityCategories.add("Low");
        physicalActivityCategories.add("Moderate");
        physicalActivityCategories.add("High");

        List<String> chronicKidneyCategories = new ArrayList<>();
        chronicKidneyCategories.add("No");
        chronicKidneyCategories.add("Stage 1");
        chronicKidneyCategories.add("Stage 2");
        chronicKidneyCategories.add("Stage 3A");
        chronicKidneyCategories.add("Stage 3B");
        chronicKidneyCategories.add("Stage 4");
        chronicKidneyCategories.add("Stage 5");

        // Creating adapter for spinner
        ArrayAdapter<String> smokeAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, smokeCategories);
        ArrayAdapter<String> physicalActivityAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, physicalActivityCategories);
        ArrayAdapter<String> chronicKidneyAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, chronicKidneyCategories);


        // Drop down layout style - list view with radio button
        smokeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        physicalActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chronicKidneyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        smokerSpinner.setAdapter(smokeAdapter);
        physicalActivitySpinner.setAdapter(physicalActivityAdapter);
        chronicKidneySpinner.setAdapter(chronicKidneyAdapter);

        HealthData hd = new HealthDataDatasource(db).getLastHealthData();

        //Set sex
        sexRadioGroup = (RadioGroup) getView().findViewById(R.id.sexRadioGroup);
        switch (hd.getSex()){
            case "male":
                ((RadioButton)sexRadioGroup.getChildAt(0)).setChecked(true);
                break;
            case "female":
                ((RadioButton)sexRadioGroup.getChildAt(1)).setChecked(true);
                break;
            case "other":
                ((RadioButton)sexRadioGroup.getChildAt(2)).setChecked(true);
                break;
        }

        //Set age
        ageEditText = (EditText) getView().findViewById(R.id.age);
        ageEditText.setText(String.valueOf(hd.getAge()));

        //Set smoker spinner
        switch (hd.getSmoker()) {
            case "no":
                smokerSpinner.setSelection(0);
                break;
            case "smoker":
                smokerSpinner.setSelection(1);
                break;
            case "ex-smoker":
                smokerSpinner.setSelection(2);
                break;
        }

        //Set physical activity spinner
        switch (hd.getPhysicalActivity()) {
            case "no":
                physicalActivitySpinner.setSelection(0);
                break;
            case "low":
                physicalActivitySpinner.setSelection(1);
                break;
            case "moderate":
                physicalActivitySpinner.setSelection(2);
                break;
            case "high":
                physicalActivitySpinner.setSelection(3);
                break;
        }

        //Set physical activity spinner
        switch (hd.getChronicKidney()) {
            case "no":
                chronicKidneySpinner.setSelection(0);
                break;
            case "stage1":
                chronicKidneySpinner.setSelection(1);
                break;
            case "stage2":
                chronicKidneySpinner.setSelection(2);
                break;
            case "stage3A":
                chronicKidneySpinner.setSelection(3);
                break;
            case "stage3B":
                chronicKidneySpinner.setSelection(4);
                break;
            case "stage4":
                chronicKidneySpinner.setSelection(5);
                break;
            case "stage5":
                chronicKidneySpinner.setSelection(6);
                break;
        }


        //Set height and mass
        heightEditText = (EditText) getView().findViewById(R.id.height);
        heightEditText.setText(String.valueOf(hd.getHeight()));
        massEditText = (EditText) getView().findViewById(R.id.mass);
        massEditText.setText(String.valueOf(hd.getMass()));

        //Set hypertension, diabetes and heart failure
        hypertensionChecked = (CheckBox) getView().findViewById(R.id.hypertension);
        diabetesChecked = (CheckBox) getView().findViewById(R.id.diabetes);


        if (hd.getHypertension().equals("yes")) hypertensionChecked.setChecked(true);
        if (hd.getDiabetes().equals("yes")) diabetesChecked.setChecked(true);

    }

}
