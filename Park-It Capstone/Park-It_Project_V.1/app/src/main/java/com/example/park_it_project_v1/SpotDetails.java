package com.example.park_it_project_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;

public class SpotDetails extends AppCompatActivity {
    EditText street, city, state, zip, description, cost;
    ImageView userImage;
    String userID;
    TextView ID;

    // Get vars from XML
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_details);

        street = findViewById(R.id.Street);
        city = findViewById(R.id.City);
        state = findViewById(R.id.State);
        zip = findViewById(R.id.Zip);
        description = findViewById(R.id.Description);
        cost = findViewById(R.id.Cost);

        ID = findViewById(R.id.textViewSetID);

        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("user_id");
        ID.setText(userID);

    }

    // Checks to see if all required fields are filed in before allowing a spot to be posted/submitted
    public void onConfirm(View view) {
        if(street.getText().toString().equals("")){
            Toast.makeText(SpotDetails.this, "Please enter a street", Toast.LENGTH_LONG).show();
        }
        else if(city.getText().toString().equals("")){
            Toast.makeText(SpotDetails.this,"Please enter a city", Toast.LENGTH_LONG).show();

        }
        else if(state.getText().toString().equals("")){
            Toast.makeText(SpotDetails.this,"Please enter a state", Toast.LENGTH_LONG).show();
        }
        else if(zip.getText().toString().equals("")){
            Toast.makeText(SpotDetails.this,"Please enter a zip", Toast.LENGTH_LONG).show();
        }
        else if(description.getText().toString().equals("")){
            Toast.makeText(SpotDetails.this,"Please enter a spot description", Toast.LENGTH_LONG).show();
        }
        else if(cost.getText().toString().equals("")){
            Toast.makeText(SpotDetails.this,"Please set cost/hour for spot", Toast.LENGTH_LONG).show();
        }
        else{
            String str_street = street.getText().toString();
            String str_city = city.getText().toString();
            String str_state = state.getText().toString();
            String str_zip = zip.getText().toString();
            String str_description = description.getText().toString();
            String str_cost = cost.getText().toString();
            String str_userID = ID.getText().toString();
            String str_address = str_street +" "+ str_city +", "+str_state +" "+ str_zip;
            String type = "spot";
            Geocoder coder= new Geocoder(this);
            List<Address> address;
            // Sends data to background worker
            try {
                address = coder.getFromLocationName(str_address, 1);
                if (address != null){
                    Address location = address.get(0);
                    Double lat = location.getLatitude();
                    Double lng = location.getLongitude();
                    String str_lat = ""+ lat;
                    String str_lng = ""+ lng;

                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.execute(type, str_street, str_city, str_state, str_zip, str_description, str_lat, str_lng, str_cost, str_userID);

                    Intent f= new Intent(this, MySpotActivity.class);
                    startActivity(f);

                    userID = ID.getText().toString();
                    Intent intent0 = new Intent(this,MySpotActivity.class);
                    intent0.putExtra("user_id",userID);
                    startActivity(intent0);



                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
            }


// Onclick listener for cancel button
    public void onCancel(View view) {

        Intent f= new Intent(this, MySpotActivity.class);
        startActivity(f);

        userID = ID.getText().toString();
        Intent intent0 = new Intent(this,MySpotActivity.class);
        intent0.putExtra("user_id",userID);
        startActivity(intent0);

    }
}
