package com.example.park_it_project_v1;

import java.net.HttpURLConnection;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

public class Connector {

    public static HttpURLConnection connection(String urlAddress) {
        try
        {
            URL url=new URL(urlAddress);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            //SET PROPERTIES
            con.setRequestMethod("POST");
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            con.setDoInput(true);
            con.setDoOutput(true);

            //RETURN
            return con;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
