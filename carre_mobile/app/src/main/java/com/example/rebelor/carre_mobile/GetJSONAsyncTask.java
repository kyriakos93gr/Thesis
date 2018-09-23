package com.example.rebelor.carre_mobile;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rebelor.carre_mobile.GetCarreDatabase;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class GetJSONAsyncTask extends AsyncTask<String, JSONObject, JSONArray> {

    @Override
    public JSONArray doInBackground(String... URL) {

        JSONArray Jarray = new JSONArray();

            try {
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(45, TimeUnit.SECONDS);
                client.setReadTimeout(45, TimeUnit.SECONDS);
                client.setWriteTimeout(45, TimeUnit.SECONDS);
                Request request = new Request.Builder()
                        .url(URL[0])
                        .build();
                Response responses = null;
                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();
                JSONObject result = new JSONObject(jsonData);
                JSONObject Jobject = result.getJSONObject("results");
                Jarray.put(Jobject.getJSONArray("bindings"));
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return Jarray;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
//        try {
//            Log.d(TAG,(Jarray.getJSONObject(0).getJSONObject("id").get("value").toString()));
//            Log.d(TAG,(result.get(0).toString()));//.getJSONObject("id").get("value").toString()));
//            Log.d(TAG,(Jarray.get(1).toString()));//.getJSONObject("id").get("value").toString()));
//            Log.d(TAG,(Jarray.get(2).toString()));//.getJSONObject("id").get("value").toString()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        try {
            new GetCarreDatabase().dataDownloaded(result.getJSONArray(0));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}