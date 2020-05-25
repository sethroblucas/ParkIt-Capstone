package com.example.park_it_project_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MySpotActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView listview;
    AlertDialog.Builder alertDialog;
    String userID;
    TextView userName_drawer;
    TextView email_drawer;
    View headerView;
    TextView ID;
    ImageView userImage;
    HashMap<String, String> spots = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spot);
        TextView title = findViewById(R.id.custom_title);
        toolbar = findViewById(R.id.toolbar);
        title.setText("My Spots");
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        listview = findViewById(R.id.listView);
        ID = findViewById(R.id.textViewSetID);
        //GET USER ID FROM LAST ACTIVITY
        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("user_id");
        ID.setText(userID);

        headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        userName_drawer = headerView.findViewById(R.id.userNamee);
        email_drawer = headerView.findViewById(R.id.userEmaill);
        userImage = headerView.findViewById(R.id.imageView);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri personImage = account.getPhotoUrl();
        Picasso.with(this).load(personImage).into(userImage);

        //CALL ON METHODS FOR FILLING DRAWER AND CARDVIEW
        getData();
        getDataDrawer();

        //OnClick listener for the list of spots that opens an alert dialog which allows you to set avilability
        // as well as delete a spot.

       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               //GET VARIABLES FOR BACKGROUND WORKERS
               String spotID = ((TextView) view.findViewById(R.id.spotid)).getText().toString();
               String street = ((TextView) view.findViewById(R.id.street)).getText().toString();
               String city = ((TextView) view.findViewById(R.id.city)).getText().toString();
               String str_spotID = spotID;

               //BUILD ALERT DIALOG
               AlertDialog.Builder builder1 = new AlertDialog.Builder(MySpotActivity.this);
               builder1.setTitle("Spot Options:");
               builder1.setMessage("\n"+"Choose one of the following options:"+"\n"+"\n"+"- Set spot to 'available'"+"\n"+"\n"+"- Set spot to 'unavailable'"+"\n"+"\n"+"- Delete spot"+"\n");
               builder1.setIcon(R.drawable.ic_settings_black_24dp);
               builder1.setCancelable(true);

               //DIALOG BUTTON LISTENER FOR SETTING SPOT AVAILABLE
               builder1.setPositiveButton(
                       "Available",
                       new DialogInterface.OnClickListener() {
                           @Override
                           //SEND SPOT ID TO BACKGROUND WORKER
                           public void onClick(DialogInterface dialog, int which) {
                               SetAvailableBackgroundWorker setAvailableBackgroundWorker = new SetAvailableBackgroundWorker(MySpotActivity.this);
                               setAvailableBackgroundWorker.execute(str_spotID);
                               Toast.makeText(MySpotActivity.this,"Spot now Available",Toast.LENGTH_SHORT).show();
                           }
                       });
                //DIALOG BUTTON LISTENER FOR SETTING SPOT UNAVAILABLE
               builder1.setNegativeButton(
                       "Unavailable",
                       new DialogInterface.OnClickListener() {
                           //SEND SPOT ID TO BACKGROUND WORKER
                           public void onClick(DialogInterface dialog, int id) {
                               String type = "Availability";
                               AvailabilityBackgroundWorker availabilityBackgroundWorker = new AvailabilityBackgroundWorker(MySpotActivity.this);
                               availabilityBackgroundWorker.execute(type, str_spotID);
                               Toast.makeText(MySpotActivity.this,"Spot now Unavailable",Toast.LENGTH_SHORT).show();
                           }
                       });
               //DIALOG BUTTON LISTENER FOR DELETING A SPOT
               builder1.setNeutralButton(
                       "Delete", new DialogInterface.OnClickListener() {
                            //BUILDS ALERT DIALOG FOR CONFIRMATION
                            public void onClick(DialogInterface dialog, int id) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(MySpotActivity.this);
                                builder2.setTitle("Delete Spot?");
                                builder2.setMessage("\n"+"Delete Spot at " + street + " " + city + "?");
                                builder2.setIcon(R.drawable.ic_delete_24dp);
                                builder2.setCancelable(false);
                            //DIALOG BUTTON LISTENER FOR CONFIRMING SPOT DELETION
                                builder2.setPositiveButton(
                                        "Delete",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            //SEND SPOT ID TO BACKGROUND WORKER
                                                String type = "delete";
                                                DeleteBackgroundWorker deleteBackgroundWorker = new DeleteBackgroundWorker(MySpotActivity.this);
                                                deleteBackgroundWorker.execute(type, str_spotID);

                                                //RE-INTENT TO UPDATE MYSPOT PAGE TO GET RID OF DELETED SPOT AND GRAB CURRENT USER ID
                                                Intent intent = new Intent(MySpotActivity.this, MySpotActivity.class);
                                                startActivity(intent);
                                                String userID = ID.getText().toString();
                                                Intent intent1 = new Intent(MySpotActivity.this, MySpotActivity.class);
                                                intent1.putExtra("user_id", userID);
                                                startActivity(intent1);
                                                Toast.makeText(MySpotActivity.this,"Spot deleted",Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                //DIALOG BUTTON LISTENER FOR CANCELING DELETION
                                builder2.setNegativeButton(
                                        "Cancel",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog1 = builder2.create();
                                alertDialog1.show();
                            }});
               //ACTIVATE ALERT DIALOG AND SHOW IT
               AlertDialog alertDialog = builder1.create();
               alertDialog.show();
               //INITIATE ALERT DIALOG BUTTONS
               Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
               Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
               Button btnNeutral = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
               //SET LAYOUT FOR ALERT DIALOG BOX
               LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
               layoutParams.weight = 10;
               btnPositive.setLayoutParams(layoutParams);
               btnNegative.setLayoutParams(layoutParams);
               btnNeutral.setLayoutParams(layoutParams);
           }
       });
    }

    // tHE getDataDrawer method sets up the string data to send to the StringRequest, it grabs the drawer.php path
    // and adds the currently signed in user's ID. On response it sends the JSON user data to the
    // showJSONDrawer method.

    private void getDataDrawer() {
        //GET CURRENT USER ID AND ADD TO URL
        String userIDd = ID.getText().toString();
        String url = Config5.DATA_URL5 + userIDd;

        //INITIATE STRING REQUEST FOR DRAWER DATA
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //SEND JSON OBJECT TO METHOD
                showJSONDrawer(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TOAST ERROR MESSAGE IF STRING REQUEST FAILS
                        Toast.makeText(MySpotActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        //START REQUEST QUEUE AND ADD URL STRING
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // This method creates an array list for the drawer user JSON object
    // to be converted to a string and set to the respective TextViews.

    private void showJSONDrawer(String response) {
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        //GET JSON OBJECT OR RETURN JSONException
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
            //GET USERNAME AND EMAIL AND SET TextViews
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

    // This method gets a JSON object of any spot in the database that is associated
    // with the currently signed in users account.

    private void getData() {
        //GET CURRENT USER ID AND ADD IT TO URL STRING
        String userID = ID.getText().toString();
        String url = Config5.DATA_URL + userID;
        //START STRING REQUEST
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //SEND JSON OBJECT(S) TO METHOD
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    //LISTEN FOR JSON ERROR
                        Toast.makeText(MySpotActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        //INITIATE REQUEST QUEUE AND ADD STRING URL
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //The showJSON method creates an array list for the json object(s) that is got from the getData
    //method. It then adds them to a list to be displayed in card views.

    public void showJSON(String response) {
        //CREATE ARRAY LIST FOR JSON OBJECT
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            //INITIATE JSONObject
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                //GET VALUES FOR CARD VIEW FROM DATABASE
                JSONObject jo = result.getJSONObject(i);
                String spotID = jo.getString(Config5.KEY_SPOTID);
                String street = jo.getString(Config5.KEY_STREET);
                String city = jo.getString(Config5.KEY_CITY);
                String state = jo.getString(Config5.KEY_STATE);
                String zip = jo.getString(Config5.KEY_ZIP);
                String description = jo.getString(Config5.KEY_DESCRIPTION);
                String cost = jo.getString(Config5.KEY_COST2);
                //ADD STRING VALUES TO A HashMap
                spots = new HashMap<>();
                spots.put(Config5.KEY_SPOTID, spotID);
                spots.put(Config5.KEY_STREET, street);
                spots.put(Config5.KEY_CITY, city + ", " + state + " " + zip);
                spots.put(Config5.KEY_DESCRIPTION, description);
                spots.put(Config5.KEY_COST2, "$" + cost +"/hour");
                //ADD HashMap TO LIST
                list.add(spots);
            }
        } catch (JSONException e) {
            //CATCH JSON ERROR
            e.printStackTrace();
        }
        //INITIATE LIST ADAPTER
        ListAdapter adapter = new SimpleAdapter(
                //ADD STRINGS TO TEXT VIEWS THROUGH LIST ADAPTER
                MySpotActivity.this, list, R.layout.activity_mylist,
                new String[]{Config5.KEY_SPOTID, Config5.KEY_STREET, Config5.KEY_CITY, Config5.KEY_DESCRIPTION, Config5.KEY_COST2,},
                new int[]{R.id.spotid, R.id.street,R.id.city, R.id.description, R.id.cost});
        //SET CARD VIEW LIST ADAPTER
        listview.setAdapter(adapter);
    }

    //The allSpots method intents the user to a page where they can view a list of all the spots
    //available on the app. It grabs the current user drawer info and intents it to the new activity.

    public void allSpots (View view){
        //INTENT TO AllSpotsActivity
        Intent f= new Intent(this, AllSpotsActivity.class);
        startActivity(f);
        //GET USER ID AND INTENT IT TO AllSpotsActivity
        userID = ID.getText().toString();
        Intent intent0 = new Intent(this,AllSpotsActivity.class);
        intent0.putExtra("user_id",userID);
        startActivity(intent0);
    }
    //INITIATE DRAWER OBJECTS
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private GoogleApiClient googleApiClient;

//The signout method uses the Google API built in method to sign the user out.
//It then intents the user back to the sign in page of the app.

    public void signout() {
        //SIGN USER OUT USING GOOGLE API
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //GIVE USER FEEDBACK AND INTENT TO LOGIN PAGE
                Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void spotSelect (View view){

        Intent f= new Intent(this, SpotDetails.class);
        startActivity(f);

        userID = ID.getText().toString();
        Intent intent0 = new Intent(this,SpotDetails.class);
        intent0.putExtra("user_id",userID);
        startActivity(intent0);


    }



    //LISTENER FOR NAVIGATION ITEMS INSIDE THE DRAWER MENU
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //GET DRAWER ITEM ID OF CLICKED ITEM
        int id = item.getItemId();
        //SWITCH TO THE CLICKED ITEM CASE
        switch (id) {
            case R.id.home:
            //INTENT TO ACTIVITY
                Intent a= new Intent(this, MapActivity.class);
                startActivity(a);
            //INTENT USER ID TO NEW ACTIVITY
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
                //ADD URL FOR TEAM INSTAGRAM ACCOUNT
                Uri uri = Uri.parse("https://www.instagram.com/park_it_app/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                //START INTERNET ACTIVITY
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException z) {
                //CATCH ERROR AND RESTART INTENT
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/park_it_app/")));
                }
                break;

            case R.id.logout:
                signout();
                break;
        }
        //SET DRAWER LAYOUT
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//The onStart method builds the Google Api Sign In client and passes it our project's API key.

    @Override
    protected void onStart() {
        //INITIATE GoogleSignInOptions AND SET TO DEFAULT
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //START Google API CLIENT AND PASS API KEY
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }
}


