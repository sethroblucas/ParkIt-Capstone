package com.example.park_it_project_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class EditVehicleWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;

    EditVehicleWorker(Context ctx) {
        context = ctx;
    }


    //Background worker for Edit Vehicle activity
    @Override
    protected String doInBackground(String... params) {
        String type = "vehicle";
        String vehicle_url = "https://cgi.soic.indiana.edu/~team56/team-56/saveVehicle.php";
        String str_make = params[1];
        String str_model = params[2];
        String str_year = params[3];
        String str_color = params[4];
        String personID = params[5];
        if (type.equals("vehicle")) {
            try {

                URL url = new URL(vehicle_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("make", "UTF-8") + "=" + URLEncoder.encode(str_make, "UTF-8") +
                        "&" + URLEncoder.encode("model", "UTF-8") + "=" + URLEncoder.encode(str_model, "UTF-8") +
                        "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(str_year, "UTF-8") +
                        "&" + URLEncoder.encode("color", "UTF-8") + "=" + URLEncoder.encode(str_color, "UTF-8") +
                        "&" + URLEncoder.encode("mainID", "UTF-8") + "=" + URLEncoder.encode(personID, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");

    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
