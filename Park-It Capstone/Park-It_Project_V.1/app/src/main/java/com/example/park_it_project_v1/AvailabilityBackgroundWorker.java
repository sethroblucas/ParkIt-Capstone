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

public class AvailabilityBackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    AvailabilityBackgroundWorker(Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        //SET TYPE AND URL PATH
        String type = "Availability";
        String claim_url = "https://cgi.soic.indiana.edu/~team56/team-56/spotClaim.php";
        //GET SPOT ID PARAMETER AND MAKE A STRING
        String spotID = params[1];
        if (type.equals("Availability")) {
            try {
                //CONVERT STRING TO URL VARIABLE AND START HTTP CONNECTION
                URL url = new URL(claim_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                //CREATE WRITER FOR POST REQUEST DATA
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("spotID", "UTF-8") + "=" + URLEncoder.encode(spotID, "UTF-8");
                //WRITE ENCODED DATA TO URL
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //GET INPUT STREAM FROM URL
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                //READ RESULT AND CREATE A STRING
                String result = "";
                String line;
                while((line = bufferedReader.readLine()) != null){
                    result +=line;
                }
                //CLOSE READER/INPUT STREAM AND DISCONNECT FROM URL
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
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");

    }
    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        //alertDialog.show();
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

