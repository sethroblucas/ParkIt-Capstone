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


public class EditAccountWorker extends AsyncTask<String, Void, String> {
    //Set Variables
    Context context;
    AlertDialog alertDialog;

    EditAccountWorker(Context ctx) {
        context = ctx;
    }


    //Background method for Edit Account
    @Override
    protected String doInBackground(String... params) {
        String type = "account";
        String account_url = "https://cgi.soic.indiana.edu/~team56/team-56/saveInfo.php";
        String str_username = params[1];
        String str_first = params[2];
        String str_last = params[3];
        String str_phone = params[4];
        String personID = params[5];
        if (type.equals("account")) {
            try {

                URL url = new URL(account_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(str_username, "UTF-8") +
                        "&" + URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(str_first, "UTF-8") +
                        "&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(str_last, "UTF-8") +
                        "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(str_phone, "UTF-8") +
                        "&" + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(personID, "UTF-8");

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
