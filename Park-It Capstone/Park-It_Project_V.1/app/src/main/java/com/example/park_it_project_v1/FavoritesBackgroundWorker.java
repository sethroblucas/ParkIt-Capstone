package com.example.park_it_project_v1;

// IMPORTING MODULES
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

// STARTING THE CLASS
public class FavoritesBackgroundWorker extends AsyncTask<String, String, String> {
    Context context;
    FavoritesBackgroundWorker(Context ctx) {
        context = ctx;
    }


        // DO IN BACKGROUND METHOD
        @Override
        protected String doInBackground(String... params) {
        // DEFINING STRINGS
            String spot_url = "https://cgi.soic.indiana.edu/~team56/team-56/favoritesInsert.php";
            String type = params[0];
            String address = params[1];
            String description = params[2];
            String cost = params[3];
            String date = params[4];
            String ID = params[5];
            if (type.equals("favorites")) {
                try
                {

                    // URL CONNECTION
                    URL url = new URL(spot_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") +
                            "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8")  +
                            "&" + URLEncoder.encode("totalCost", "UTF-8") + "=" + URLEncoder.encode(cost, "UTF-8")  +
                            "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") +
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


        // EXECUTE METHOD
        @Override
        protected void onPreExecute() {


        }

        // POST EXECTUTE METHOD
        @Override
        protected void onPostExecute(String result) {

        }

        // ON PROGRESS METHOD
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

