package com.example.rebelor.carre_mobile;

import org.json.JSONArray;
import org.json.JSONException;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rebelor.carre_mobile.fragments.AbouFragment;
import com.example.rebelor.carre_mobile.sqlite.*;

import com.example.rebelor.carre_mobile.sqlite.type.Observable;
import com.example.rebelor.carre_mobile.sqlite.type.RiskElement;
import com.example.rebelor.carre_mobile.sqlite.type.RiskEvidence;

import java.math.BigDecimal;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

//TODO: App crashes whe there is no internet


public class GetCarreDatabase {

    private static JSONArray carre_json = null;
    private static Context context;
    static SQLiteDatabase db;

    private final String sparql_risk_evidences_public = "https://devices.duth.carre-project.eu/sparql?default-graph-uri=http%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic&query=PREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3EPREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3EPREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3EPREFIX+%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Fsensors.owl%23%3EPREFIX+risk%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Frisk.owl%23%3EPREFIX+carreManufacturer%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fmanufacturers%2F%3EPREFIX+carreUsers%3A+%3Chttps%3A%2F%2Fcarre.kmi.open.ac.uk%2Fusers%2F%3ESELECT+DISTINCT+%3Frisk_evidence+%3Fcondition+%3Fconfidence_interval_min+%3Fconfidence_interval_max+%3Frisk_evidence_ratio_value+%3Frisk_evidence_ratio_type+%3Frisk_factor+%3Fhas_risk_factor_source+%3Fhas_risk_factor_target+%3Fhas_risk_factor_association_type+FROM+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic%3E+WHERE+%7B++%0D%0A++%3Frisk_evidence+a+risk%3Arisk_evidence+%3B++++%0D%0A+++risk%3Ahas_risk_factor+%3Frisk_factor%3B%0D%0A++++risk%3Ahas_risk_evidence_ratio_type+%3Frisk_evidence_ratio_type%3B+%0D%0A+++++++++risk%3Ahas_risk_evidence_ratio_value+%3Frisk_evidence_ratio_value.++++++%0D%0A+++++++++OPTIONAL+%7B+%3Frisk_evidence+risk%3Ahas_confidence_interval_max+%3Fconfidence_interval_max%3B+%0D%0A+++++++++++++risk%3Ahas_confidence_interval_min+%3Fconfidence_interval_min.+%7D++++++%0D%0A+++++++++++++%3Frisk_evidence+risk%3Ahas_risk_evidence_observable+%3Fob+%3B+%0D%0A+++++++++++++risk%3Ahas_observable_condition+%3Fcondition+.++++%0D%0A+++++++++++++%23details+for+risk+factor++++%0D%0A+++++++++++++%3Frisk_factor+risk%3Ahas_risk_factor_association_type+%3Fhas_risk_factor_association_type%3B++++%0D%0A+++++++++++++risk%3Ahas_risk_factor_source+%3Fhas_risk_factor_source%3B++++%0D%0A+++++++++++++risk%3Ahas_risk_factor_target+%3Fhas_risk_factor_target.++++++%7D&should-sponge=&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on";
    private final String sparql_risk_elements_public = "https://devices.duth.carre-project.eu/sparql?default-graph-uri=http%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic&query=PREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3EPREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3EPREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3EPREFIX+%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Fsensors.owl%23%3EPREFIX+risk%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Frisk.owl%23%3EPREFIX+carreManufacturer%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fmanufacturers%2F%3EPREFIX+carreUsers%3A+%3Chttps%3A%2F%2Fcarre.kmi.open.ac.uk%2Fusers%2F%3E%0D%0ASELECT+DISTINCT+*+FROM+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic%3E+%0D%0AWHERE+%7B++%0D%0A++%7B++++%3Frisk_element+a+risk%3Arisk_element%3Brisk%3Ahas_risk_element_name+%3Fname.+++++++++++++++%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_author+%3Fauthor%7D++++++++%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_risk_element_observable+%3Fobservable%7D++++++++%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_risk_element_modifiable_status+%3Fmodifiable%7D+%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_educational_material+%3Feducational%7D+++%0D%0AFILTER+%28lang%28%3Fname%29%3D%27en%27%29%7D%7D&should-sponge=&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on";
    private final String sparql_observables_public = "https://devices.duth.carre-project.eu/sparql?default-graph-uri=http%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic&query=PREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3EPREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3EPREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3EPREFIX+%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Fsensors.owl%23%3EPREFIX+risk%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Frisk.owl%23%3EPREFIX+carreManufacturer%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fmanufacturers%2F%3EPREFIX+carreUsers%3A+%3Chttps%3A%2F%2Fcarre.kmi.open.ac.uk%2Fusers%2F%3E%0D%0ASELECT+DISTINCT+*+FROM+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fcarreriskdata%3E+%0D%0AWHERE+%7B++%0D%0A++%7B++++%3Fobservable+a+risk%3Aobservable%3Brisk%3Ahas_observable_name+%3Fname%3B%0D%0Arisk%3Ahas_observable_measurement_type+%3Fmeasurement%3B+risk%3Ahas_observable_modifiable_status+%3Fmodifiable.++++++++++%0D%0AFILTER+%28lang%28%3Fname%29%3D%27en%27%29%7D%7D&should-sponge=&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on";

    private final String sparql_risk_evidences = "https://devices.duth.carre-project.eu/sparql?default-graph-uri=http%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic&query=PREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3EPREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3EPREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3EPREFIX+%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Fsensors.owl%23%3EPREFIX+risk%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Frisk.owl%23%3EPREFIX+carreManufacturer%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fmanufacturers%2F%3EPREFIX+carreUsers%3A+%3Chttps%3A%2F%2Fcarre.kmi.open.ac.uk%2Fusers%2F%3ESELECT+DISTINCT+%3Frisk_evidence+%3Fcondition+%3Fconfidence_interval_min+%3Fconfidence_interval_max+%3Frisk_evidence_ratio_value+%3Frisk_evidence_ratio_type+%3Frisk_factor+%3Fhas_risk_factor_source+%3Fhas_risk_factor_target+%3Fhas_risk_factor_association_type+FROM+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fcarreriskdata%3E+WHERE+%7B++%0D%0A++%3Frisk_evidence+a+risk%3Arisk_evidence+%3B++++%0D%0A+++risk%3Ahas_risk_factor+%3Frisk_factor%3B%0D%0A++++risk%3Ahas_risk_evidence_ratio_type+%3Frisk_evidence_ratio_type%3B+%0D%0A+++++++++risk%3Ahas_risk_evidence_ratio_value+%3Frisk_evidence_ratio_value.++++++%0D%0A+++++++++OPTIONAL+%7B+%3Frisk_evidence+risk%3Ahas_confidence_interval_max+%3Fconfidence_interval_max%3B+%0D%0A+++++++++++++risk%3Ahas_confidence_interval_min+%3Fconfidence_interval_min.+%7D++++++%0D%0A+++++++++++++%3Frisk_evidence+risk%3Ahas_risk_evidence_observable+%3Fob+%3B+%0D%0A+++++++++++++risk%3Ahas_observable_condition+%3Fcondition+.++++%0D%0A+++++++++++++%23details+for+risk+factor++++%0D%0A+++++++++++++%3Frisk_factor+risk%3Ahas_risk_factor_association_type+%3Fhas_risk_factor_association_type%3B++++%0D%0A+++++++++++++risk%3Ahas_risk_factor_source+%3Fhas_risk_factor_source%3B++++%0D%0A+++++++++++++risk%3Ahas_risk_factor_target+%3Fhas_risk_factor_target.++++++%7D&should-sponge=&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on";
    private final String sparql_risk_elements = "https://devices.duth.carre-project.eu/sparql?default-graph-uri=http%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic&query=PREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3EPREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3EPREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3EPREFIX+%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Fsensors.owl%23%3EPREFIX+risk%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Frisk.owl%23%3EPREFIX+carreManufacturer%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fmanufacturers%2F%3EPREFIX+carreUsers%3A+%3Chttps%3A%2F%2Fcarre.kmi.open.ac.uk%2Fusers%2F%3E%0D%0ASELECT+DISTINCT+*+FROM+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fcarreriskdata%3E+%0D%0AWHERE+%7B++%0D%0A++%7B++++%3Frisk_element+a+risk%3Arisk_element%3Brisk%3Ahas_risk_element_name+%3Fname.+++++++++++++++%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_author+%3Fauthor%7D++++++++%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_risk_element_observable+%3Fobservable%7D++++++++%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_risk_element_modifiable_status+%3Fmodifiable%7D+%0D%0AOPTIONAL+%7B%3Frisk_element+risk%3Ahas_educational_material+%3Feducational%7D+++%0D%0AFILTER+%28lang%28%3Fname%29%3D%27en%27%29%7D%7D&should-sponge=&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on";
    private final String sparql_observables = "https://devices.duth.carre-project.eu/sparql?default-graph-uri=http%3A%2F%2Fcarre.kmi.open.ac.uk%2Fpublic&query=PREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3EPREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3EPREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3EPREFIX+%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Fsensors.owl%23%3EPREFIX+risk%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fontology%2Frisk.owl%23%3EPREFIX+carreManufacturer%3A+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fmanufacturers%2F%3EPREFIX+carreUsers%3A+%3Chttps%3A%2F%2Fcarre.kmi.open.ac.uk%2Fusers%2F%3E%0D%0ASELECT+DISTINCT+*+FROM+%3Chttp%3A%2F%2Fcarre.kmi.open.ac.uk%2Fcarreriskdata%3E+%0D%0AWHERE+%7B++%0D%0A++%7B++++%3Fobservable+a+risk%3Aobservable%3Brisk%3Ahas_observable_name+%3Fname%3B%0D%0Arisk%3Ahas_observable_measurement_type+%3Fmeasurement.++++++++++%0D%0AFILTER+%28lang%28%3Fname%29%3D%27en%27%29%7D%7D&should-sponge=&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on";

    private static int dbUpdated = 0;

    public GetCarreDatabase(){};
    public GetCarreDatabase(SQLiteDatabase data, Context context) {

        db = data;
        this.context = context;
    }

    public void fetchDatabase(){

        new GetJSONAsyncTask().executeOnExecutor(THREAD_POOL_EXECUTOR, sparql_risk_evidences_public);
        new GetJSONAsyncTask().executeOnExecutor(THREAD_POOL_EXECUTOR, sparql_risk_elements_public);
        new GetJSONAsyncTask().executeOnExecutor(THREAD_POOL_EXECUTOR, sparql_observables_public);
    }


    void dataDownloaded(JSONArray carre_json){
        new UpdateDatabaseAsyncTask().executeOnExecutor(THREAD_POOL_EXECUTOR, carre_json);
    }

    private class UpdateDatabaseAsyncTask extends AsyncTask<JSONArray, Void, String>  {

        @Override
        protected String doInBackground(JSONArray... carre_json){
            String str = null;
            try {
                //Add risk evidences in database
                if (carre_json[0].getJSONObject(0).has("risk_evidence")){
                    new RiskEvidenceDataSource(db).deleteAllRiskEvidences();
                    RiskEvidence rv = new RiskEvidence();
                    for (int i = 0; i < carre_json[0].length(); i++) {
                        str = "RV_" + carre_json[0].getJSONObject(i).getJSONObject("risk_evidence").getString("value").replaceAll("\\D+","");
                        rv.setRiskEvidence(str);
                        rv.setCondition(carre_json[0].getJSONObject(i).getJSONObject("condition").getString("value"));
                        rv.setConfidenceIntervalMin( (carre_json[0].getJSONObject(i).has("confidence_interval_min")) ? BigDecimal.valueOf(carre_json[0].getJSONObject(i).getJSONObject("confidence_interval_min").getDouble("value")).floatValue() : 0);
                        rv.setConfidenceIntervalMax( (carre_json[0].getJSONObject(i).has("confidence_interval_max")) ? BigDecimal.valueOf(carre_json[0].getJSONObject(i).getJSONObject("confidence_interval_max").getDouble("value")).floatValue() : 0);
                        rv.setRatioValue(BigDecimal.valueOf(carre_json[0].getJSONObject(i).getJSONObject("risk_evidence_ratio_value").getDouble("value")).floatValue());
                        rv.setRatioType(carre_json[0].getJSONObject(i).getJSONObject("risk_evidence_ratio_type").getString("value"));
                        str = "RF_" + carre_json[0].getJSONObject(i).getJSONObject("risk_factor").getString("value").replaceAll("\\D+","");
                        rv.setRiskFactor(str);
                        str = "RL_" + carre_json[0].getJSONObject(i).getJSONObject("has_risk_factor_source").getString("value").replaceAll("\\D+","");
                        rv.setRiskFactorSource(str);
                        str = "RL_" + carre_json[0].getJSONObject(i).getJSONObject("has_risk_factor_target").getString("value").replaceAll("\\D+","");
                        rv.setRiskFactorTarget(str);
                        String[] strs = carre_json[0].getJSONObject(i).getJSONObject("has_risk_factor_association_type").getString("value").split("association_type_");
                        rv.setRiskFactorAssociationType(strs[1].replaceAll("_", " "));
                        new RiskEvidenceDataSource(db).addRiskEvidence(rv);
                    }
                    dbUpdated += 1;

                 //Add risk elements in database
                } else if (carre_json[0].getJSONObject(0).has("risk_element")){
                    new RiskElementDataSource(db).deleteAllRiskElements();
                    RiskElement rl = new RiskElement();
                    for (int i = 0; i < carre_json[0].length(); i++) {
                        str = "RL_" + carre_json[0].getJSONObject(i).getJSONObject("risk_element").getString("value").replaceAll("\\D+","");
                        rl.setRiskelement(str);
                        rl.setName(carre_json[0].getJSONObject(i).getJSONObject("name").getString("value"));
                        rl.setAuthor(carre_json[0].getJSONObject(i).getJSONObject("author").getString("value"));
                        str = "OB_" + carre_json[0].getJSONObject(i).getJSONObject("observable").getString("value").replaceAll("\\D+","");
                        rl.setObservable(str);
                        rl.setModifiable(carre_json[0].getJSONObject(i).getJSONObject("modifiable").getString("value"));

                        new RiskElementDataSource(db).addRiskElement(rl);
                    }
                    dbUpdated += 1;

                 //Add observables in database
                } else if (carre_json[0].getJSONObject(0).has("observable")){
                    new ObservableDataSource(db).deleteAllObservables();
                    Observable ob = new Observable();
                    for (int i = 0; i < carre_json[0].length(); i++) {
                        str = "OB_" + carre_json[0].getJSONObject(i).getJSONObject("observable").getString("value").replaceAll("\\D+","");
                        ob.setObservable(str);
                        ob.setName(carre_json[0].getJSONObject(i).getJSONObject("name").getString("value"));
                        str = "ME_" + carre_json[0].getJSONObject(i).getJSONObject("observable").getString("value").replaceAll("\\D+","");
                        ob.setMeasurement(str);
                        ob.setModifiable(carre_json[0].getJSONObject(i).getJSONObject("modifiable").getString("value"));
                        new ObservableDataSource(db).addObservable(ob);
                    }
                    dbUpdated += 1;

                } else{
                    Log.e("Alert", "No datatype found in UpdateDatabaseAsyncTask");
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (dbUpdated == 3){
//                MainActivity.showToast(context, "Database Updated");
                dbUpdated = 0;
                AbouFragment.dismissProgress();
            }
        }
    }

}
