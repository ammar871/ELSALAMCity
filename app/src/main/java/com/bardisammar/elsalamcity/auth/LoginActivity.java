package com.bardisammar.elsalamcity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.auth.pojo.Users;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityLoginBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ActivityLoginBinding binding;
    private GoogleApiClient googleApiClient;

    //location
    private Location mlastlocation;
    private LocationRequest mlocationrequest;


    //for facebook
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 2;
    Users users;
    SharedEditor sharedEditor;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        pd = new ProgressDialog(this);
        Get_hash_key();
        //
        Thread mThread = new Thread() {
            @Override
            public void run() {
                if (isLoggedIn() || isSignedIn()) {

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
               // pd.dismiss();
            }
        };
        mThread.start();

        FacebookSdk.sdkInitialize(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        sharedEditor = new SharedEditor(this);


        //LOGIN WITH GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        binding.rvGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValue()) {

                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, RC_SIGN_IN);
                }

            }
        });

        loginManferFace();

        //LOGIN WITH FACEBOOK
        binding.rvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValue()) {

                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));

                }
            }
        });


    }

    private boolean isValue() {
        if (binding.editTextMobile.getText().toString().isEmpty() || binding.editTextMobile.length() < 11) {
            binding.editTextMobile.setError("من فضلك أدخل رقم الهاتف ");
            return false;
        } else if (binding.editAddress.getText().toString().isEmpty()) {

            binding.editAddress.setError("من فضلك أدخل عنوانك");
            return false;

        } else {
            return true;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private boolean isSignedIn() {

        return GoogleSignIn.getLastSignedInAccount(this) != null;

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        return accessToken != null;
    }

    private void loginManferFace() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("resulte", loginResult.getAccessToken().getUserId() + "");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name");
                        request.setParameters(parameters);
                        request.executeAsync();


//                        getUserProfile(loginResult.getAccessToken());


                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        handleSignInResult(result);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();

             //   sharedEditor.saveData(users);
                Commen.print("namegoogle", personName);

            }

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            Commen.checkLogin = false;
        } else {


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("anannn",connectionResult.getErrorMessage()+"");
    }
    public void Get_hash_key() {
// coneverte Key Hash google play
        byte[] sha1 = {
                (byte) 0xcf, 0x37, 0x1c, (byte)0x2c, (byte)0xcc, (byte) 0xea, 0x27, (byte)0xb8, (byte)0x02, (byte)0x44, (byte)0xcd, 0x66,
                0x35, 0x37, 0x64, (byte)0x7f, (byte) 0xab, (byte) 0xce, (byte)0x9d, (byte)0x57
        };
       Log.d("keyhashG", Base64.encodeToString(sha1, Base64.NO_WRAP));
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.elsalmcity.elsalamcity", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.d("hashkeyandroid", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.d("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}