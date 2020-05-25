package com.example.park_it_project_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;



import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.widget.Button;

//Class for Parking History Activity
public class ParkingHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	//Set variables
    private static final int REQUEST_CODE=101;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private GoogleApiClient googleApiClient;
    String userID;
    TextView ID;
    TextView userName_drawer;
    TextView email_drawer;
    View headerView;
    ImageView userImage;
    Button favorites;
    HashMap<String, String> spots = new HashMap<>();
    ListView listview;
    Button favoriteButton;
    String TOTAL;
    TextView DateHistory;
    TextView addressHistory;
    TextView descriptionHistory;
    TextView CostHistory;
    final Context context = this;


	//Method for when page is loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);

			//Get IDs from XML
        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.custom_title);
        title.setText("Parking History");
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

			//Toggles for Drawer
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

			//Get IDs from XML
        listview = findViewById(R.id.listViewHistory);
        addressHistory = findViewById(R.id.addressHistory);
        descriptionHistory = findViewById(R.id.descriptionHistory);
        DateHistory = findViewById(R.id.DateHistory);
        CostHistory = findViewById(R.id.costHistory);


			//Get string from previous Intent
        Intent intent0 = getIntent();
        TOTAL = intent0.getStringExtra("payment");

			//Get IDs for drawer header
        ID = findViewById(R.id.textViewSetID);
        headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        userName_drawer = headerView.findViewById(R.id.userNamee);
        email_drawer = headerView.findViewById(R.id.userEmaill);
        userImage = headerView.findViewById(R.id.imageView);

			//Adds Google profile picture to drawer
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri personImage = account.getPhotoUrl();
        Picasso.with(this).load(personImage).into(userImage);

     





			//Gets string from previous intent and sets ID text
        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("user_id");
        ID.setText(userID);

			//Run methods
        getDataDrawer();
        getData();


        //Listener for list view click
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(ParkingHistoryActivity.this);
                builder1.setTitle("Add Spot to Favorites?");
                builder1.setIcon(R.drawable.ic_favorite_red_24dp);
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

									//Set variables for background work
                                String address = ((TextView) view.findViewById(R.id.addressHistory)).getText().toString();
                                String description = ((TextView) view.findViewById(R.id.descriptionHistory)).getText().toString();
                                String cost = ((TextView) view.findViewById(R.id.costHistory)).getText().toString();
                                String date = ((TextView) view.findViewById(R.id.DateHistory)).getText().toString();


											//Set variables for background worker
                                String str_address = address;
                                String str_description = description;
                                String str_cost = cost;
                                String str_date = date;
                                String str_userID = ID.getText().toString();
                                String type = "favorites";

											//Sends variables to favorites background worker 
                                FavoritesBackgroundWorker favoritesBackgroundWorker = new FavoritesBackgroundWorker(ParkingHistoryActivity.this);
                                favoritesBackgroundWorker.execute(type, str_address, str_description, str_cost, str_date, str_userID);
                                dialog.cancel();

                                Toast.makeText(ParkingHistoryActivity.this,"Spot added to Favorites!",Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder1.setNegativeButton(
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = builder1.create();
                alertDialog.show();


            }
        });




        favorites = findViewById(R.id.fav_btn);
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFavorites();
            }
        });

   }





	//Method for Favorites button click
    private void goToFavorites(){
        Intent intent = new Intent(ParkingHistoryActivity.this,Favorites.class);
        startActivity(intent);

        String userID = ID.getText().toString();
        Intent intent1 = new Intent(ParkingHistoryActivity.this,Favorites.class);
        intent1.putExtra("user_id",userID);
        startActivity(intent1);
    }



	//Method for user signout clicked
    public void signout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

	//Method for getting data from database via php/JSON
    private void getData() {

        String userID = ID.getText().toString();
        String url = Config5.DATA_URL7 + userID;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingHistoryActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


	//Method for getting data via php/JSON
    public void showJSON(String response) {
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String address = jo.getString(Config5.KEY_ADDRESS);
                String description = jo.getString(Config5.KEY_DESCRIPTION);
                String date = jo.getString(Config5.KEY_DATE);
                String cost = jo.getString(Config5.KEY_COST2);



                spots = new HashMap<>();
                spots.put(Config5.KEY_ADDRESS, address);
                spots.put(Config5.KEY_DESCRIPTION, description);
                spots.put(Config5.KEY_DATE, "Total: $"+date);
                spots.put(Config5.KEY_COST3, cost);


                list.add(spots);



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ParkingHistoryActivity.this, list, R.layout.mylist_history,
                new String[]{Config5.KEY_ADDRESS, Config5.KEY_DESCRIPTION, Config5.KEY_DATE, Config5.KEY_COST3},
                new int[]{R.id.addressHistory, R.id.descriptionHistory, R.id.costHistory, R.id.DateHistory});

        listview.setAdapter(adapter);

    }




	//Gets data for drawer via php/json
    private void getDataDrawer() {


        String userIDd = ID.getText().toString();
        String url = Config5.DATA_URL5 + userIDd;




        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONDrawer(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingHistoryActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


	//Gets data for drawer via php/json
    private void showJSONDrawer(String response) {
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String user = jo.getString(Config5.KEY_USERNAME);
                String email = jo.getString(Config5.KEY_EMAIL);


                userName_drawer.setText(user);
                email_drawer.setText(email);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


	//Method for navigation drawer items clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.home:
                Intent a= new Intent(this, MapActivity.class);
                startActivity(a);

                userID = ID.getText().toString();
                Intent intent0 = new Intent(this,MapActivity.class);
                intent0.putExtra("user_id",userID);
                startActivity(intent0);
                break;

            case R.id.history:
                Intent b= new Intent(this, ParkingHistoryActivity.class);
                startActivity(b);

                userID = ID.getText().toString();
                Intent intent1 = new Intent(this,ParkingHistoryActivity.class);
                intent1.putExtra("user_id",userID);
                startActivity(intent1);
                break;

            case R.id.spot:
                Intent d= new Intent(this, MySpotActivity.class);
                startActivity(d);

                String userID = ID.getText().toString();
                Intent intent2 = new Intent(this,MySpotActivity.class);
                intent2.putExtra("user_id",userID);
                startActivity(intent2);
                break;

            case R.id.about:
                Intent e= new Intent(this, AboutActivity.class);
                startActivity(e);

                userID = ID.getText().toString();
                Intent intent3 = new Intent(this,AboutActivity.class);
                intent3.putExtra("user_id",userID);
                startActivity(intent3);

                break;

            case R.id.support:
                Intent f= new Intent(this, SupportActivity.class);
                startActivity(f);

                userID = ID.getText().toString();
                Intent intent4 = new Intent(this,SupportActivity.class);
                intent4.putExtra("user_id",userID);
                startActivity(intent4);
                break;

            case R.id.settings:
                Intent g= new Intent(this, SettingsActivity.class);
                startActivity(g);

                userID = ID.getText().toString();
                Intent intent5= new Intent(this,SettingsActivity.class);
                intent5.putExtra("user_id",userID);
                startActivity(intent5);
                break;

            case R.id.social:
                Uri uri = Uri.parse("https://www.instagram.com/park_it_app/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException z) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/park_it_app/")));
                }
                break;


            case R.id.logout:
                signout();
                break;

        }



        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }
}
