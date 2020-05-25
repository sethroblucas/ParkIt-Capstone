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

public class SetAvailableBackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    SetAvailableBackgroundWorker(Context ctx) {
        context = ctx;
    }
    //START BACKGROUND WORKER METHOD
    @Override
    protected String doInBackground(String... params) {
        //SET PARAMETERS PASSED FROM PREVIOUS METHOD
        String type = "unclaim";
        String unclaim_url = "https://cgi.soic.indiana.edu/~team56/team-56/makeSpotAvailable.php";
        String spotID = params[0];
        if (type.equals("unclaim")) {


            try {
            //SET URL FOR makeSpotAvailable.php AND START HTTP CONNECTION
                URL url = new URL(unclaim_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                //INITIATE WRITER AND SET ENCODING TYPE
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //ENCODE SPOT ID TO SEND TO PHP FILE
                String post_data = URLEncoder.encode("spotID", "UTF-8") + "=" + URLEncoder.encode(spotID, "UTF-8");
                //WRITE, FLUSH, CLOSE WRITER
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                //CLOSE OUTPUT STREAM
                outputStream.close();
                //START INPUT STREAM READER FROM HTTP CONNECTION
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                //GET THE RESULT FROM PHP FILE
                while((line = bufferedReader.readLine()) != null){
                    result +=line;
                }
                //CLOSE INPUTS AND DISCONNECT FROM URL
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //RETURN THE RESULTS
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

