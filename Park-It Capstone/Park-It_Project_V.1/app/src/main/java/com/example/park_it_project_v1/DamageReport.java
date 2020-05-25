package com.example.park_it_project_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DamageReport extends AppCompatActivity {


	//Set Variables
    String userID;
    TextView ID;
    private TextView mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;


	// Runs when page is loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_damage_report);

			//Get IDs from XML 
        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);

			//Set text of Text View
        mEditTextTo.setText("parkitcapstone2020@gmail.com");

			//Get ID from XML and set on click listener for when button clicked
        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

            //Get ID from XML
            Button buttonCancel = findViewById(R.id.button_cancel);
			//On click listener for cancel button
			  buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DamageReport.this, SupportActivity.class);
                startActivity(intent);

					//Get text of ID and send it to new Intent
                userID = ID.getText().toString();
                Intent intent4 = new Intent(DamageReport.this,SupportActivity.class);
                intent4.putExtra("user_id",userID);
                startActivity(intent4);
            }
        });

        ID = findViewById(R.id.textViewSetID);

        Intent intent1 = getIntent();
        userID = intent1.getStringExtra("user_id");
        ID.setText(userID);
    }

	//Method for sending users emails
    private void sendMail() {

        String recipientList = mEditTextTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        if (subject.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter a subject",Toast.LENGTH_SHORT).show();
        }

        if (message.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter report message",Toast.LENGTH_SHORT).show();
        }

        else {

				//Pass values to new Intent via putExtra
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an email client"));
        }

    }
}
