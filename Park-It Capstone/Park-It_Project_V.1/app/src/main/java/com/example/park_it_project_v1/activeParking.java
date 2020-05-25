package com.example.park_it_project_v1;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.util.Date;
import java.util.Locale;

public class activeParking extends AppCompatActivity {



    //Setting Values
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    NavigationView navigationView;
    TextView seekbarProgress;
    TextView seekbarProgress2;
    SeekBar addSeekbar;
    String userID;
    String claimTime;
    String address;
    String description;
    String cost;
    TextView ID;
    Integer timerCount;
    TextView timer;
    ImageView userImage;
    CountDownTimer countDownTimer;
    CountDownTimer addTimeTimer;
    long allotedTime;
    Button endButton;
    Button addTimeBtn;
    TextView addressActives;
    TextView descriptionActives;
    TextView costActives;
    TextView date;
    Button add;
    Button close;
    double costPerSecond;
    String costPerSec;
    double totalCost;
    String TOTAL;
    Context context;
    String spotID;
    String ed1, ed2, ed3, ed4, ed5, ed6;
    Integer Addtime;
    String addTimestr;




    //Runs when page is started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_parking);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        timerCount = 0;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        CardView addtimecard = findViewById(R.id.timeCard);

        Intent intent2 = getIntent();
        address = intent2.getStringExtra("address");
        description = intent2.getStringExtra("description");
        cost = intent2.getStringExtra("cost");
        userID = intent2.getStringExtra("user_id");
        claimTime = intent2.getStringExtra("availability");
        spotID = intent2.getStringExtra("spotID");

        //Gets XML IDs
        ID = findViewById(R.id.textViewSetID);
        timer = findViewById(R.id.timer);
        addressActives = findViewById(R.id.addressActive);
        descriptionActives = findViewById(R.id.descriptionActive);
        costActives = findViewById(R.id.costActive);
        seekbarProgress = findViewById(R.id.seekbarProgress);
        seekbarProgress2 = findViewById(R.id.seekbarProgress2);
        date = findViewById(R.id.date);

        add = findViewById(R.id.add_time);
        close = findViewById(R.id.close);



        ed4= "NAME PLACEHOLDER";
        ed5= "SESSION ENDED";
        ed6= "Your parking session has ended!";


        //Sets texts for text views
        addressActives.setText(address);
        descriptionActives.setText('"'+description+'"');
        costActives.setText("");

        double result = Double.parseDouble(cost);

        costPerSecond = result/3600;


        costPerSec = String.valueOf(costPerSecond);

        ID.setText(userID);
        date.setText(currentDate);

        totalCost = 0.0;

        startTimer();

        context = this;

        Log.d("Address", addressActives.getText().toString());
        Log.d("Description", descriptionActives.getText().toString());
        Log.d("Cost", costActives.getText().toString());



        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.custom_title);
        title.setText("Current Parking Session:");



        endButton = (Button) findViewById(R.id.end_btn);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    EndParking();


            }
        });



        //On click listener for adding time button click
        addTimeBtn = findViewById(R.id.add_btn);
        addTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtimecard.setVisibility(View.VISIBLE);
            }
        });

        addSeekbar = findViewById(R.id.ActiveSeekbar);
        addSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Addtime = progress + 1;
                addTimestr = (String.valueOf(Addtime));
                seekbarProgress.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void onCloseClick (View view) {
        CardView addtimecard = findViewById(R.id.timeCard);
        addtimecard.setVisibility(View.INVISIBLE);

    }

    public void onClickAdd (View view) {
        CardView addtimecard = findViewById(R.id.timeCard);
        addSeekbar = findViewById(R.id.ActiveSeekbar);
        addSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Addtime = progress + 1;
                addTimestr = (String.valueOf(Addtime));
                seekbarProgress.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timerCount += 1;
        if (timerCount == 1){
            countDownTimer.cancel();
            newTimer(); }
        if (timerCount >= 2){
            addTimeTimer.cancel();
            newTimer();
        }
        Toast.makeText(getApplicationContext(),String.valueOf(Addtime-1)+" minutes added!",Toast.LENGTH_SHORT).show();
        addtimecard.setVisibility(View.INVISIBLE);
    }





    //Starts a timer when user claims spot
    public void startTimer() {
        int i = Integer.parseInt(claimTime);
        allotedTime = (i - 1) * 60000;
        TOTAL = String.format(Locale.getDefault(), "%.2f", (double) totalCost);
        countDownTimer = new CountDownTimer(allotedTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                allotedTime = millisUntilFinished;
                updateCountdownText();
                totalCost += costPerSecond;
                TOTAL =  String.format(Locale.getDefault(), "%.2f", (double) totalCost);
            }

            @Override
            public void onFinish() {
                String tittle1=ed4;
                String subject1=ed5;
                String body1=ed6;
                EndParkingWorker endParkingWorker = new EndParkingWorker(activeParking.this);
                endParkingWorker.execute(spotID);
                TOTAL = String.format(Locale.getDefault(), "%.2f", (double) totalCost);

                Intent intent0 = new Intent(activeParking.this,PayPalActivity.class);
                intent0.putExtra("payment",TOTAL).putExtra("user_id", userID);
                startActivity(intent0);


                if(addTimeTimer != null){
                    addTimeTimer.cancel();
                    timer.setText("Finished");
                }
                else{
                    countDownTimer.cancel();
                    timer.setText("Finished");
                }


                String str_address = addressActives.getText().toString();
                String str_description = descriptionActives.getText().toString();
                String str_cost = TOTAL;
                String str_date = date.getText().toString();
                String str_userID = ID.getText().toString();
                String type = "active";

                Log.d("Str_Address", str_address);
                Log.d("Str_Description", str_description);
                Log.d("Str_Cost", str_cost);
                Log.d("Str_Date", str_date);



                ActiveParkingBackgroundWorker activeParkingBackgroundWorker = new ActiveParkingBackgroundWorker(activeParking.this);
                activeParkingBackgroundWorker.execute(type, str_address, str_description, str_cost, str_date, str_userID);
            }


        }.start();




    }


    //Timer if a user adds time to parking spot
    private void newTimer() {
        countDownTimer.cancel();
        int i = Addtime;
        allotedTime += (i - 1) * 60000;
        TOTAL = String.format(Locale.getDefault(), "%.2f", (double) totalCost);
        addTimeTimer = new CountDownTimer(allotedTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                allotedTime = millisUntilFinished;
                updateCountdownText();
                totalCost += costPerSecond;
                TOTAL =  String.format(Locale.getDefault(), "%.2f", (double) totalCost);
            }

            @Override
            public void onFinish() {
                String tittle1=ed4;
                String subject1=ed5;
                String body1=ed6;
                EndParkingWorker endParkingWorker = new EndParkingWorker(activeParking.this);
                endParkingWorker.execute(spotID);
                TOTAL = String.format(Locale.getDefault(), "%.2f", (double) totalCost);

                Intent intent0 = new Intent(activeParking.this,PayPalActivity.class);
                intent0.putExtra("payment",TOTAL).putExtra("user_id", userID);
                startActivity(intent0);



                if(addTimeTimer != null){
                    addTimeTimer.cancel();
                    timer.setText("Finished");
                }
                else{
                    countDownTimer.cancel();
                    timer.setText("Finished");
                }




                //Setting values for Background worker
                String str_address = addressActives.getText().toString();
                String str_description = descriptionActives.getText().toString();
                String str_cost = TOTAL;
                String str_date = date.getText().toString();
                String str_userID = ID.getText().toString();
                String type = "active";

                Log.d("Str_Address", str_address);
                Log.d("Str_Description", str_description);
                Log.d("Str_Cost", str_cost);
                Log.d("Str_Date", str_date);
                Log.d("Str_userID", str_userID);



                //Sends values to background worker
                ActiveParkingBackgroundWorker activeParkingBackgroundWorker = new ActiveParkingBackgroundWorker(activeParking.this);
                activeParkingBackgroundWorker.execute(type, str_address, str_description, str_cost, str_date, str_userID);


            }
        }.start();
    }



    //Running total for cost
    private void updateCountdownText() {


        int minutes = (int) (allotedTime / 1000) / 60;
        int seconds = (int) (allotedTime / 1000) % 60;
        costActives.setText("Total: $"+TOTAL);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText("Time Left: " + timeLeftFormatted);

    }




    //Method for when user ends parking
    public void EndParking() {
        String tittle1=ed4;
        String subject1=ed5;
        String body1=ed6;
        String str_spotID = spotID;
        EndParkingWorker endParkingWorker = new EndParkingWorker(activeParking.this);
        endParkingWorker.execute(spotID);
        TOTAL = String.format(Locale.getDefault(), "%.2f", (double) totalCost);


        Intent intent0 = new Intent(activeParking.this,PayPalActivity.class);
        intent0.putExtra("payment",TOTAL).putExtra("user_id", userID);
        startActivity(intent0);


        if(addTimeTimer != null){
            addTimeTimer.cancel();
            timer.setText("Finished");
        }
        else{
            countDownTimer.cancel();
            timer.setText("Finished");
        }



        //Setting values for Background worker
        String str_address = addressActives.getText().toString();
        String str_description = descriptionActives.getText().toString();
        String str_cost = TOTAL;
        String str_date = date.getText().toString();
        String str_userID = ID.getText().toString();
        String type = "active";

        Log.d("Str_Address", str_address);
        Log.d("Str_Description", str_description);
        Log.d("Str_Cost", str_cost);
        Log.d("Str_Date", str_date);




        //Sends values to background worker
        ActiveParkingBackgroundWorker activeParkingBackgroundWorker = new ActiveParkingBackgroundWorker(this);
        activeParkingBackgroundWorker.execute(type, str_address, str_description, str_cost, str_date, str_userID);


    }
    }
