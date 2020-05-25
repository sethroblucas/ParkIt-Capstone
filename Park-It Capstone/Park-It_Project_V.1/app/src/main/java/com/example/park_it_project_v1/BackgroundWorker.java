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


//Background worker for my spot page
public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }




    @Override
    protected String doInBackground(String... params) {
        String type = "spot";
        String spot_url = "https://cgi.soic.indiana.edu/~team56/team-56/mySpot.php";
        String street = params[1];
        String city = params[2];
        String state = params[3];
        String zip = params[4];
        String description = params[5];
        String lat = params[6];
        String lng = params[7];
        String cost = params[8];
        String ID = params[9];
        if (type.equals("spot")) {
            try
            {

                URL url = new URL(spot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("street", "UTF-8") + "=" + URLEncoder.encode(street, "UTF-8") +
                        "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8")  +
                        "&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8")  +
                        "&" + URLEncoder.encode("zip", "UTF-8") + "=" + URLEncoder.encode(zip, "UTF-8")  +
                        "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8")  +
                        "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") +
                        "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8")  +
                        "&" + URLEncoder.encode("hourlyCost", "UTF-8") + "=" + URLEncoder.encode(cost, "UTF-8") +
                        "&" + URLEncoder.encode("mainID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                while((line = bufferedReader.readLine()) != null){
                    result +=line;
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




    //Methods for pre, post, and progress executions
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
