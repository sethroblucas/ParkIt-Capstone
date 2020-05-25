package com.example.park_it_project_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

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

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //Set Variables
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private GoogleApiClient googleApiClient;
    private Button button, button_veh;
    String userID;
    TextView ID;
    TextView userName_drawer;
    TextView email_drawer;
    View headerView;
    ImageView userImage;


    //Method for page loads
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Get ID's from XML
        toolbar = findViewById(R.id.toolbar);

        TextView title = findViewById(R.id.custom_title);
        title.setText("Settings");

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        button = (Button)findViewById(R.id.edit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAccount();
            }
        });

        button_veh = (Button)findViewById(R.id.edit_vehicle_btn);
        button_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditVehicle();
            }
        });

        button = (Button)findViewById(R.id.FAQ_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportFAQ();
            }
        });

        button = (Button)findViewById(R.id.about_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                About();
            }
        });

        ID = findViewById(R.id.textViewSetID);

        Intent intent1 = getIntent();
        userID = intent1.getStringExtra("user_id");
        ID.setText(userID);

        headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        userName_drawer = headerView.findViewById(R.id.userNamee);
        email_drawer = headerView.findViewById(R.id.userEmaill);
        userImage = headerView.findViewById(R.id.imageView);


        //Adding Google profile picture to drawer
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri personImage = account.getPhotoUrl();
        Picasso.with(this).load(personImage).into(userImage);
        getDataDrawer();

    }


    //Gets data to the drawer header
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
                        Toast.makeText(SettingsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    //Gets data to the drawer header
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


    //Method for siging out user
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


    //Method for edit account click
    public void EditAccount() {

        Intent intent = new Intent(this,EditAccountActivity.class);
        startActivity(intent);

        String userID = ID.getText().toString();
        Intent intent1 = new Intent(this,EditAccountActivity.class);
        intent1.putExtra("user_id",userID);
        startActivity(intent1);
    }


    //Method for edit vehicle click
    public void EditVehicle() {

        Intent intent2 = new Intent(this,EditVehicleActivity.class);
        startActivity(intent2);

        String userID = ID.getText().toString();
        Intent intent3 = new Intent(this,EditVehicleActivity.class);
        intent3.putExtra("user_id",userID);
        startActivity(intent3);

    }


    //Method for support/faq click
    public void SupportFAQ() {

        Intent intent = new Intent(this,SupportActivity.class);
        startActivity(intent);

        String userID = ID.getText().toString();
        Intent intent1 = new Intent(this,SupportActivity.class);
        intent1.putExtra("user_id",userID);
        startActivity(intent1);
    }


    //Method for about click
    public void About() {

        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);

        String userID = ID.getText().toString();
        Intent intent1 = new Intent(this,AboutActivity.class);
        intent1.putExtra("user_id",userID);
        startActivity(intent1);
    }


    //Method for item click in navigation drawer
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
