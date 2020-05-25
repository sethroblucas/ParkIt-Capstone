package com.example.park_it_project_v1;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

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

public class transactionBackgroundWorker extends AsyncTask<String, String, String> {
    Context context;
    transactionBackgroundWorker(Context ctx) {
        context = ctx;
    }

    // Send transaction data to the database
    @Override
    protected String doInBackground(String... params) {
        //SET TYPE AND URL PATH
        String trans_url = "https://cgi.soic.indiana.edu/~team56/team-56/transactionInsert.php";
        //Set params to string
        String type = params[0];
        String paypalID = params[1];
        String cost = params[2];
        String date = params[3];
        String ID = params[4];
        String status = params[5];
        if (type.equals("transaction")) {
            try
            {
                // formats/encodes data to be sent to the database
                URL url = new URL(trans_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("paypalID", "UTF-8") + "=" + URLEncoder.encode(paypalID, "UTF-8") +
                        "&" + URLEncoder.encode("totalCost", "UTF-8") + "=" + URLEncoder.encode(cost, "UTF-8")  +
                        "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") +
                        "&" + URLEncoder.encode("mainID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8")+
                        "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
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



    @Override
    protected void onPreExecute() {


    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
