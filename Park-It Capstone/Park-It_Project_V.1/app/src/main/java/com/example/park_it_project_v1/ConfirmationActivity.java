package com.example.park_it_project_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {


    String userID;
    TextView ID;
    TextView date;
    String TOTAL;
    String payID;
    String stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        date = findViewById(R.id.paymentdate);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        date.setText(currentDate);

        ID = findViewById(R.id.textViewSetID);

        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("user_id");
        ID.setText(userID);

        //Getting Intent
        Intent intent = getIntent();


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount+" USD");
        TOTAL = textViewAmount.getText().toString();
        payID = textViewId.getText().toString();
        stat = textViewStatus.getText().toString();
    }


    //Method for paypal confirm
    public void paypalConfirm(View view) {
        String str_paypalConf = payID;
        String str_cost = TOTAL;
        String str_date = date.getText().toString();
        String str_userID = ID.getText().toString();
        String str_stat = stat;
        String type = "transaction";

        Log.d("Str_paypalID", str_paypalConf);
        Log.d("Str_Cost", str_cost);
        Log.d("Str_Date", str_date);
        Log.d("Str_Stat",str_stat);



        //Passes data to background worker

        transactionBackgroundWorker transactionBackgroundWorker = new transactionBackgroundWorker(ConfirmationActivity.this);
        transactionBackgroundWorker.execute(type, str_paypalConf, str_cost, str_date, str_userID, str_stat);

        Intent intent = new Intent(ConfirmationActivity.this,MapActivity.class);
        startActivity(intent);


        //Passes string to next intent
        userID = ID.getText().toString();
        Intent intent0 = new Intent(this,MapActivity.class);
        intent0.putExtra("user_id",userID);
        startActivity(intent0);
    }
}