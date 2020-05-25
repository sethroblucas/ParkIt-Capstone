package com.example.park_it_project_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    //Set Variables
    TextView ID;
    TextView name;
    TextView email;
    ImageView userImage;

    String urlAddress= "https://cgi.sice.indiana.edu/~team56/team-56/Users.php", ed1, ed2, ed3;

    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize UI Fields
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        ID = (TextView) findViewById(R.id.ID);
        userImage = findViewById(R.id.userImage);

        SignIn = (SignInButton)findViewById(R.id.login_bn);
        SignIn.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) .requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this) .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        ed1= "NAME PLACEHOLDER";
        ed2= "SIGNED IN";
        ed3= "User has signed into Park-It!";



    }

    public void openActivity2()
    {

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);

        String userID = ID.getText().toString();
        Intent intent1 = new Intent(LoginActivity.this,MapActivity.class);
        intent1.putExtra("user_id",userID);
        startActivity(intent1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.login_bn:
                setSignIn();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    //Method for setting sign in
    private void setSignIn() {

            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, REQ_CODE);
        }


    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personImage = account.getPhotoUrl();


                name.setText(personName);
                email.setText(personEmail);
                ID.setText(personId);






            Sender s = new Sender(LoginActivity.this,urlAddress,ID,name,email);
            s.execute();
            openActivity2();

        }
    }



    private void updateUI(boolean isLogin)
    {

        if (isLogin)
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

}

