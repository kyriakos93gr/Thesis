package com.example.rebelor.carre_mobile.sqlite;

import com.example.rebelor.carre_mobile.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

//risk evidence RV
//risk factor RF
//risk element RL
//observables OB

public class MySQLiteHelper extends SQLiteOpenHelper{

    private static MySQLiteHelper mInstance = null;
    private static SQLiteDatabase database = null;

    private static final String DATABASE_NAME = "CollectedPersonalData.db";
    private static final int DATABASE_VERSION = 1;

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////

    public static final String TABLE_RISK_EVIDENCES = "risk_evidences";
    public static final String RISK_EVIDENCES_ID = "_id";
    public static final String RISK_EVIDENCES_RISK_EVIDENCE = "risk_evidence";
    public static final String RISK_EVIDENCES_CONDITON = "condition";
    public static final String RISK_EVIDENCES_CONFIDENCE_INTERVAL_MIN = "confidence_interval_min";
    public static final String RISK_EVIDENCES_CONFIDENCE_INTERVAL_MAX = "confidence_interval_max";
    public static final String RISK_EVIDENCES_RATIO_VALUE = "ratio_value";
    public static final String RISK_EVIDENCES_RATIO_TYPE = "ratio_type";
    public static final String RISK_EVIDENCES_RISK_FACTOR = "risk_factor";
    public static final String RISK_EVIDENCES_RISK_FACTOR_SOURCE = "risk_factor_source";
    public static final String RISK_EVIDENCES_RISK_FACTOR_TARGET = "risk_factor_target";
    public static final String RISK_EVIDENCES_RISK_FACTOR_ASSOCIATION_TYPE = "risk_factor_association_type";


    private static final String CREATE_TABLE_RISK_EVIDENCES = "create table " + TABLE_RISK_EVIDENCES + "("
            + RISK_EVIDENCES_ID + " integer primary key asc, "
            + RISK_EVIDENCES_RISK_EVIDENCE + " text not null, "
            + RISK_EVIDENCES_CONDITON + " text not null, "
            + RISK_EVIDENCES_CONFIDENCE_INTERVAL_MIN + " numeric, "
            + RISK_EVIDENCES_CONFIDENCE_INTERVAL_MAX + " numeric, "
            + RISK_EVIDENCES_RATIO_VALUE + " numeric not null, "
            + RISK_EVIDENCES_RATIO_TYPE + " text not null, "
            + RISK_EVIDENCES_RISK_FACTOR + " text not null, "
            + RISK_EVIDENCES_RISK_FACTOR_SOURCE + " text not null, "
            + RISK_EVIDENCES_RISK_FACTOR_TARGET + " text, "
            + RISK_EVIDENCES_RISK_FACTOR_ASSOCIATION_TYPE + " text not null "
            + ");";

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////

    public static final String TABLE_RISK_ELEMENTS = "risk_elements";
    public static final String RISK_ELEMENTS_ID = "_id";
    public static final String RISK_ELEMENTS_RISK_ELEMENT = "risk_element";
    public static final String RISK_ELEMENTS_NAME = "name";
    public static final String RISK_ELEMENTS_AUTHOR = "author";
    public static final String RISK_ELEMENTS_OBSERVABLE = "observable";
    public static final String RISK_ELEMENTS_MODIFIABLE = "modifiable";
//    public static final String RISK_ELEMENTS_EDUCATIONAL = "educational";


    private static final String CREATE_TABLE_RISK_ELEMENTS = "create table " + TABLE_RISK_ELEMENTS + "("
            + RISK_ELEMENTS_ID + " integer primary key asc, "
            + RISK_ELEMENTS_RISK_ELEMENT + " text not null, "
            + RISK_ELEMENTS_NAME + " text not null, "
            + RISK_ELEMENTS_AUTHOR + " text not null, "
            + RISK_ELEMENTS_OBSERVABLE + " integer not null, "
            + RISK_ELEMENTS_MODIFIABLE + " text not null "
//            + RISK_ELEMENTS_EDUCATIONAL + " text not null "
            + ");";

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////

    public static final String TABLE_OBSERVABLES = "observables";
    public static final String OBSERVABLES_ID = "_id";
    public static final String OBSERVABLES_OBSERVABLE = "observable";
    public static final String OBSERVABLES_NAME = "name";
    public static final String OBSERVABLES_MEASUREMENT_TYPE = "measurement";
    public static final String OBSERVABLES_MODIFIABLE = "modifiable";


    private static final String CREATE_TABLE_OBSERVABLES = "create table " + TABLE_OBSERVABLES + "("
            + OBSERVABLES_ID + " integer primary key asc, "
            + OBSERVABLES_OBSERVABLE + " text not null, "
            + OBSERVABLES_NAME + " text not null, "
            + OBSERVABLES_MEASUREMENT_TYPE + " text not null, "
            + OBSERVABLES_MODIFIABLE + " text not null "
            + ");";

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////

    public static final String TABLE_HEALTH_DATA = "health_data";
    public static final String HEALTH_DATA_ID = "_id";
    public static final String HEALTH_DATA_SEX = "sex";
    public static final String HEALTH_DATA_AGE = "age";
    public static final String HEALTH_DATA_SMOKER = "smoker";
    public static final String HEALTH_DATA_PHYSICAL_ACTIVITY = "physical_activity";
    public static final String HEALTH_DATA_CHRONIC_KIDNEY = "chronic_kidney";
    public static final String HEALTH_DATA_HEIGHT = "height";
    public static final String HEALTH_DATA_MASS = "mass";
    public static final String HEALTH_DATA_HYPERTENSION = "hypertension";
    public static final String HEALTH_DATA_DIABETES = "diabetes";
    public static final String HEALTH_DATA_BMI = "bmi";
    public static final String HEALTH_DATA_TIMESTAMP = "timestamp";


    private static final String CREATE_TABLE_HEALTH_DATA = "create table " + TABLE_HEALTH_DATA + "("
            + HEALTH_DATA_ID + " integer primary key default 0, "
            + HEALTH_DATA_SEX + " text not null default male, "
            + HEALTH_DATA_AGE + " integer not null default 0, "
            + HEALTH_DATA_SMOKER + " text not null default never, "
            + HEALTH_DATA_PHYSICAL_ACTIVITY + " text not null default no, "
            + HEALTH_DATA_CHRONIC_KIDNEY + " text not null default no, "
            + HEALTH_DATA_HEIGHT + " numeric not null default 0, "
            + HEALTH_DATA_MASS + " numeric not null default 0, "
            + HEALTH_DATA_HYPERTENSION + " string not null default no, "
            + HEALTH_DATA_DIABETES + " string not null default no, "
            + HEALTH_DATA_BMI + " numeric not null default no, "
            + HEALTH_DATA_TIMESTAMP + " integer not null default 0 "
            + ");";

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////

    private static final String INSERT_INTO_HEALTH_DATA = "insert into " + TABLE_HEALTH_DATA +
            " default values;";

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mInstance = this;
    }

    public static MySQLiteHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new MySQLiteHelper(ctx);
            openConnecion();
        }
        return mInstance;
    }

    private static void openConnecion(){
        if ( database == null ){
            database = mInstance.getWritableDatabase();
        }
    }

    public synchronized void closeConnecion() {
        if(mInstance!=null){
            mInstance.close();
            database.close();
            mInstance = null;
            database = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL(CREATE_TABLE_COMMENTS);
        database.execSQL(CREATE_TABLE_RISK_EVIDENCES);
        database.execSQL(CREATE_TABLE_RISK_ELEMENTS);
        database.execSQL(CREATE_TABLE_OBSERVABLES);
        database.execSQL(CREATE_TABLE_HEALTH_DATA);
        database.execSQL(INSERT_INTO_HEALTH_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if(oldVersion == 1 && newVersion == 2){
        //	Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will add the table '"+TABLE_SENSORS+"'");
        //	database.execSQL(CREATE_TABLE_SENSORS);
        //}else{
        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RISK_EVIDENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RISK_ELEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVABLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH_DATA);

        onCreate(db);
        //}
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void exportDB(Context ctx) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            //File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //String currentDBPath = "//data//" + "<package name>" + "//databases//" + "<db name>";
                Date d = new Date();
                String backupDBPath = "//"+ctx.getString(R.string.app_name)+"//"+this.getDatabaseName().replace(".db", "")
                        +"."+(new SimpleDateFormat("yyyy-MM-dd")).format(d)+"_"+d.getHours()+"."+
                        d.getMinutes()+"."+d.getSeconds()+".db";

                File currentDB = ctx.getDatabasePath(this.getDatabaseName()); //new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(ctx, "Backup Successful!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Backup Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkEmpty(SQLiteDatabase db){

        String count_observables = "SELECT count(*) FROM " + TABLE_OBSERVABLES;
        Cursor mcursor = db.rawQuery(count_observables, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0)
            return false;
        else
            return true;
    }

    public int countRiskFactors(SQLiteDatabase db){

        String count_risk_factors = "Select count (*) from (Select DISTINCT " +
                RISK_EVIDENCES_RISK_FACTOR_SOURCE + ", " + RISK_EVIDENCES_RISK_FACTOR_TARGET + ", "
                + RISK_EVIDENCES_RISK_FACTOR_ASSOCIATION_TYPE + " from " + TABLE_RISK_EVIDENCES +
                ") AS XXX;" ;

        Cursor mcursor = db.rawQuery(count_risk_factors, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0);
    }
}
