package com.example.park_it_project_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;

public class AllSpotsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView listview;
    AlertDialog.Builder alertDialog;

    String userID;
    TextView userName_drawer;
    TextView email_drawer;
    View headerView;
    TextView ID;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_spots);
        toolbar = findViewById(R.id.toolbar);

        TextView title = findViewById(R.id.custom_title);
        title.setText("All Spots");





        //Get IDs from XML
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        listview = findViewById(R.id.listView);

        ID = findViewById(R.id.textViewSetID);


        //Get string from previous intent
        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("user_id");
        ID.setText(userID);

        headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        userName_drawer = headerView.findViewById(R.id.userNamee);
        email_drawer = headerView.findViewById(R.id.userEmaill);
        userImage = headerView.findViewById(R.id.imageView);


        //Gets google profile image
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri personImage = account.getPhotoUrl();
        Picasso.with(this).load(personImage).into(userImage);
        getData();
        getDataDrawer();


    }


    //Gets data for drawer
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
                        Toast.makeText(AllSpotsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }




    //Works with getDataDrawer() to show drawer
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


    //Gets data for all spots page
    private void getData() {

        String userID = "3";
        String url = Config5.DATA_URL6 + userID;



        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AllSpotsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }





    //Works with getData() to show all spots page
    private void showJSON(String response) {
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String spotID = jo.getString(Config5.KEY_SPOTID);
                String street = jo.getString(Config5.KEY_STREET);
                String city = jo.getString(Config5.KEY_CITY);
                String state = jo.getString(Config5.KEY_STATE);
                String zip = jo.getString(Config5.KEY_ZIP);
                String description = jo.getString(Config5.KEY_DESCRIPTION);
                String cost = jo.getString(Config5.KEY_COST2);



                final HashMap<String, String> spots = new HashMap<>();
                spots.put(Config5.KEY_SPOTID, "Spot #" + spotID);
                spots.put(Config5.KEY_STREET, street);
                spots.put(Config5.KEY_CITY, city + ", " + state + " " + zip);
                spots.put(Config5.KEY_DESCRIPTION, description);
                spots.put(Config5.KEY_COST2, "$" + cost +"/hour");

                list.add(spots);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                AllSpotsActivity.this, list, R.layout.all_list,
                new String[]{Config5.KEY_SPOTID, Config5.KEY_STREET, Config5.KEY_CITY, Config5.KEY_DESCRIPTION, Config5.KEY_COST2,},
                new int[]{R.id.spotid, R.id.street,R.id.city, R.id.description, R.id.cost});

        listview.setAdapter(adapter);


    }




    public void spotSelect (View view){

        Intent f= new Intent(this, SpotDetails.class);
        startActivity(f);

        userID = ID.getText().toString();
        Intent intent0 = new Intent(this,SpotDetails.class);
        intent0.putExtra("user_id",userID);
        startActivity(intent0);


    }

    private static final int REQUEST_CODE=101;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private GoogleApiClient googleApiClient;






    //Method for signing out user
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




    //Navigation options
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

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
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


