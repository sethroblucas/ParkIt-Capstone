package com.example.park_it_project_v1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    String userID;
    TextView ID;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    Button back;

    //INITIATE BACK BUTTON ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        back  = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BACK BUTTON INTENTS USER BACK TO SUPPORT PAGE
                Intent intent = new Intent(view.getContext(), SupportActivity.class);
                view.getContext().startActivity(intent);
                userID = ID.getText().toString();
                //GET CURRENT USER ID AND INTENT TO SUPPORT PAGE
                Intent intent4 = new Intent(FAQActivity.this,SupportActivity.class);
                intent4.putExtra("user_id",userID);
                startActivity(intent4);
            }
        });
        //GET CURRENT USER ID
        ID = findViewById(R.id.textViewSetID);
        Intent intent1 = getIntent();
        userID = intent1.getStringExtra("user_id");
        ID.setText(userID);
        
        //CREATE EXPANDABLE LIST VIEW
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        //GET LIST VIEW DATA FROM ExpandableListDataPump JAVA CLASS AND SET DATA TO LIST
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            //BUILT IN METHODS FOR EXPANDABLE LIST VIEW
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });
    }

}