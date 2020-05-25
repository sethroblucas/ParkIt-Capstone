package com.example.park_it_project_v1;

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener{
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    private GoogleMap mMap;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private GoogleApiClient googleApiClient;
    String userID;
    SeekBar mySeekBar;
    TextView ID;
    TextView address;
    TextView description;
    TextView cost;
    TextView userName_drawer;
    TextView email_drawer;
    View headerView;
    ImageView userImage;
    Integer claimTime;
    String claimTimestr;
    String addressFinal;
    String descriptionFinal;
    String costFinal;
    String ed1, ed2, ed3;
    PlacesClient placesClient;
    final Context context = this;
    //INITIATE GOOGLE SIGN IN AND REQUEST USER DATA
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestEmail()
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //CALL METHOD TO GET USER CURRENT LOCATION
        fetchlastlocation();

        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.custom_title);
        title.setText("Park-it");
        ID = findViewById(R.id.textViewID);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        cost = findViewById(R.id.Cost);

        ed1= "NAME PLACEHOLDER";
        ed2= "CLAIMED SPOT";
        ed3= "User has claimed a spot!";

        //SET PROJECT GOOGLE API KEY
        String apikey = "AIzaSyCB4thCIP-ASnWrk4Fzl7qSsCZNxg6Bmoo";
        //CHECK FOR "PLACES" FUNCTIONALITY IN API
        if (!Places.isInitialized()) {
            //SET "PLACES" FUNCTIONALITY IF NOT INITIALIZED
            Places.initialize(getApplicationContext(), apikey);
        }
        //SET PLACES CLIENT CONTEXT
        placesClient = Places.createClient(this);
        //INITIATE MAP FRAGMENT SUPPORT
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //SEND FIELDS TO "PLACES" CLIENT
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        //SET OnClick LISTENER FOR MAP FRAGMENT
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //GET LAT LNG FROM SELECTED PLACE ON MAP AND SET CAMERA VIEW
                final LatLng latLng = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19.0f));
            }
            @Override
            public void onError(@NonNull Status status) {
            }
        });
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //GET CURRENT USER ID
        Intent intent1 = getIntent();
        userID = intent1.getStringExtra("user_id");
        ID.setText(userID);
        headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        userName_drawer = headerView.findViewById(R.id.userNamee);
        email_drawer = headerView.findViewById(R.id.userEmaill);
        userImage = headerView.findViewById(R.id.imageView);
        //CALL METHOD TO PLACE MARKERS ON MAP FROM DATABASE
        getDataDrawer();

        //GET USER DATA FROM GOOGLE API FOR DRAWER
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri personImage = account.getPhotoUrl();
        Picasso.with(this).load(personImage).into(userImage);

    }

//The fetchlastlocation method gets the last reported location of the device and changes to camera
//view to that new location.

    private void fetchlastlocation() {
        //CHECK APP PERMISSIONS FOR INTERNET ACCESS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        //GET LAST KNOWN LOCATION AS LAT LNG DOUBLE
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    //SET CURRENT LOCATION LAT LNG
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mySeekBar = findViewById(R.id.seekbar);

        //METHOD TO POST SPOTS TO MAP
        getData();

        //SET CURRENT LOCATION AS NEW LAT LNG VARIABLE
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        //CREATE MARKER AT CURRENT LOCATION AND SET STYLE
        final Marker User = mMap.addMarker(new MarkerOptions().position(latLng).title("You Are Here").draggable(false));
        User.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //UPDATE CAMERA TO CURRENT LOCATION
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(19.0f);

        //OnClick LISTENER FOR MAP LOCATIONS
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //CHECK IF THE MARKER IS THE CURRENT LOCATION MARKER
                if (marker.equals(User)){
                    return false;
                }
                //OPEN CARD VIEW WHEN USER CLICKS MARKER
                else{
                    marker.hideInfoWindow();
                    final CardView spotCard = findViewById(R.id.spotCard);

                    //SEEK BAR SO USER CAN SET TIME TO PARK
                    mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //UPDATE CARD VIEW WHEN USER INTERACTS WITH SEEK BAR
                            claimTime = progress+1;
                            claimTimestr = String.valueOf(claimTime);
                            TextView t= findViewById(R.id.seekbarProgress);
                            t.setText(String.valueOf(progress));
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                    //INITIATE BUTTONS ON CARD VIEW
                    final Button closeCard = findViewById(R.id.closeCard);
                    final Button claimSpot = findViewById(R.id.claimSpot);
                    spotCard.setVisibility(View.VISIBLE);
                    closeCard.setVisibility(View.VISIBLE);
                    claimSpot.setVisibility(View.VISIBLE);

                    //CALL METHOD TO FILL CARD VIEW WITH CORRECT SPOT DATA FROM DATABASE
                    getCardData(marker);

                    closeCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //CLOSE CARD VIEW ON CLICK
                            spotCard.setVisibility(View.INVISIBLE);
                            closeCard.setVisibility(View.INVISIBLE);
                            claimSpot.setVisibility(View.INVISIBLE);
                            marker.hideInfoWindow();
                        }
                    });
                    //ON CLICK LISTENER FOR CLAIMING SPOT
                    claimSpot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //CHECK THAT USER HAS SET AMOUNT OF TIME TO CLAIM
                            if (claimTime != null) {
                            //BUILD ALERT DAILOG TO CONFIRM SPOT CLAIM
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Confirm Session?");
                                builder.setIcon(R.drawable.alertlogo);
                                claimTime = claimTime - 1;
                                builder.setMessage("\n"+"Please confirm the following details:"+"\n"+"\n"+"Location: "+address.getText()+"\n"+ "\n"+"Allotted time: "+claimTime+" minutes"
                                +"\n"+"\n"+"Note: Your parking session will begin as soon as you confirm!");

                                //DAILOG BUTTON LISTENER FOR CONFIRMING A SPOT CLAIM
                                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        //GET SPOT DATA OF SPOT BEING CLAIMED
                                        addressFinal = address.getText().toString();
                                        descriptionFinal = description.getText().toString();
                                        String spotID = marker.getSnippet();

                                        //SEND SPOT ID TO BACKGROUND WORKER TO TAKE IT OFF THE MAP
                                        claimBackground(spotID);

                                        //GET FINAL COST AND ADD INTENT EXTRAS FOR ACTIVE PARKING ACTIVITY
                                        costFinal = cost.getText().toString();
                                        Intent intent1 = new Intent(MapActivity.this, activeParking.class);
                                        intent1.putExtra("user_id", userID).putExtra("availability",claimTimestr).putExtra("address", addressFinal).putExtra("description", descriptionFinal).putExtra("cost", costFinal).putExtra("spotID", spotID);
                                        startActivity(intent1);
                                        dialog.dismiss();
                                    }
                                });
                            //DAILOG BUTTON LISTENER FOR CANCELING A SPOT CLAIM
                                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        claimTime = claimTime + 1;
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            //TELL USER TO ENTER AN AMOUNT OF TIME TO CLAIM FOR
                            } else {
                                Toast.makeText(MapActivity.this, "Please select amount of time", Toast.LENGTH_SHORT).show();
                            }
                        }});
                    return false;
                }}
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_CODE:
                //CHECKS THAT THERE ARE LAST CURRENT LOCATION RESULTS AND FETCHES IF TRUE
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    fetchlastlocation();
                }
                break;
        }
    }

//The claimBackground method sends the spot ID to the claimSpotWorker so that
//availability on that spot will be set to unavailable.

    private void claimBackground(String spotID){
        //SET TYPE AND EXECUTE BACKGROUND WORKER
        String type = "claim";
        ClaimSpotWorker claimSpotWorker = new ClaimSpotWorker(this);
        claimSpotWorker.execute(type, spotID);
        Log.d("spotID_TEST", spotID);
    }

    public void signout() {
        //USE GOOGLE API CLIENT TO SIGN THE CURRENT USER OUT
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //GET ID OF DRAWER ITEM
        int id = item.getItemId();
        //SWITCH TO ITEM CASE
        switch (id) {
            case R.id.home:
                //START INTENT OF CASE FROM DRAWER MENU AND INTENT USER DATA
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
                Intent intent5 = new Intent(this,SettingsActivity.class);
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

//The getData method grabs a list of latlng objects from the database to populate
//the map with available parking spots that have been entered.

    private void getData() {
        //GET URL PATH FOR PHP FILE
        String url = Config5.DATA_URL2;

        //START STRING REQUEST FROM URL PATH
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //SEND JSON OBJECT RESPONSE TO showJSON METHOD
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //LISTEN FOR JSON OBJECT ERROR
                        Toast.makeText(MapActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        //START REQUEST QUEUE AND ADD URL STRING
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//The getCardData method grabs the data from the spots table in the database that corresponds
//with a clicked on spot marker. It then sends the JSON object to another method to be displayed on the card view.


    private void getCardData(Marker marker) {
    //GET SPOT ID FROM THE MARKER THAT WAS CLICKED ON AND SET URL PATH FOR PHP FILE
        final String clickID = marker.getSnippet();
        String url = Config5.DATA_URL_card;
        //START STRING REQUEST WITH URL PATH
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //SEND JSON OBJECT TO saveJSON METHOD WITH THE SPOT ID
                saveJSON(clickID,response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //LISTEN FOR JSON ERROR
                        Toast.makeText(MapActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        //START REQUEST QUEUE AND ADD URL PATH
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//The get DataDrawer method grabs the currently signed in user data for the drawer menu.

    private void getDataDrawer() {

        //GET USER ID AND ADD IT TO URL PATH FOR PHP FILE
        String userIDd = ID.getText().toString();
        String url = Config5.DATA_URL5 + userIDd;

        //START STRING REQUEST AND ADD URL PATH
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //SEND JSON OBJECT TO showJSONDrawer method
                showJSONDrawer(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //LISTEN FOR JSON ERROR
                        Toast.makeText(MapActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        //START REQUEST QUEUE AND ADD URL PATH
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//The showJSONDrawer method fills the drawer menu with the currently signed in user's information.


    private void showJSONDrawer(String response) {
        //CREATE ARRAY LIST FOR JSON OBJECT
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            //GET JSON ARRAY FROM PHP FILE
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
            //SET VALUES FROM JSON OBJECT TO CORRECT TEXT VIEWS
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

//The saveJSON method is passed the spot ID and grabs the data associated with the
//spot to fill the card view when a spot is clicked on.

    private void saveJSON(String clickID, String response) {

        try {
        //SET JSON ARRAY FOR THE RESPONSE OBJECT
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
            //GET STRING VALUES FROM JSON OBJECT
                JSONObject jo = result.getJSONObject(i);
                String spotID= jo.getString(Config5.KEY_SPOTID);
                String street = jo.getString(Config5.KEY_STREET);
                String city =jo.getString(Config5.KEY_CITY);
                String state =jo.getString(Config5.KEY_STATE);
                String zip =jo.getString(Config5.KEY_ZIP);
                String descriptionStr =jo.getString(Config5.KEY_DESCRIPTION);
                String costStr =jo.getString(Config5.KEY_COST);
                String addressStr = street+" "+city+" "+state+" "+zip;

                //CHECK THAT THE PHP GOT THE CORRECT SPOT INFORMATION AND SET TEXT VIEWS
                if ( clickID.matches(spotID)) {
                    address.setText(addressStr);
                    description.setText(descriptionStr);
                    cost.setText(costStr);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//The showJson method is passed the JSON object from the getData method and
//it is a list of spots with their ID's and lat lng values.

    private void showJSON(String response) {
        try {
            //SET JSON ARRAY AND GET RESULT
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                //GET STRING VALUES OF JSON RESULT
                JSONObject jo = result.getJSONObject(i);
                String lat = jo.getString(Config5.KEY_LAT);
                String lon = jo.getString(Config5.KEY_LON);
                String spotID = jo.getString(Config5.KEY_SPOTID);
                //CONVERT LAT AND LNG VALUES TO DOUBLE AND SET NEW LatLng VARIABLE
                Double dlat = Double.valueOf(lat);
                Double dlon = Double.valueOf(lon);
                LatLng mLatLng = new LatLng(dlat,dlon);
                //CREATE MARKERS FOR ALL ITEMS IN THE LIST AND ADD SPOT ID AS A SNIPPET
                MarkerOptions options = new MarkerOptions().position(mLatLng).title("Open Spot").snippet(spotID).draggable(false);
                //MAKE SPOT MARKERS THE PARK-IT LOGO
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.alertlogo_marker));
                mMap.addMarker(options);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//The spotSelect method intents the user to the activity where they can add a spot
//to the map by entering their address.

    public void spotSelect (View view){
        //ON CLICK INTENT TO THE SpotDetails ACTIVITY
        Intent f= new Intent(this, SpotDetails.class);
        startActivity(f);
        //INTENT CURRENT USER ID DATA TO SpotDetails ACTIVITY
        userID = ID.getText().toString();
        Intent intent0 = new Intent(this,SpotDetails.class);
        intent0.putExtra("user_id",userID);
        startActivity(intent0);
    }

    @Override
    protected void onStart() {
        //INITIATE GOOGLE SIGN IN API AND REQUEST USER INFO
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
