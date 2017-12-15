package techease.com.postcard.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import techease.com.postcard.R;

public class Login extends AppCompatActivity {

    TextView textView;
    Typeface typeface;
    Button btnEmail,Fb;
    LoginButton Fbloginbtn;
    GoogleSignInOptions gso;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FrameLayout frameLayout;
    String provider_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("com.postcard", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Declaration
//     gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
        frameLayout=(FrameLayout)findViewById(R.id.loginContainer);
        textView=(TextView)findViewById(R.id.tvpostcard);
        typeface=Typeface.createFromAsset(getAssets(),"billabong.ttf");
        btnEmail=(Button)findViewById(R.id.btnContinueWithEmail);
//        SignInButton signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.sign_in_button:
//                        signIn();
//                        break;
//                    // ...
//                }
//            }
//        });
        callbackManager = CallbackManager.Factory.create();
        Fbloginbtn=(LoginButton)findViewById(R.id.btnFB);
        Fb=(Button)findViewById(R.id.btnFbLogin);
        Fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==Fb)
                {
                    Fbloginbtn.performClick();
                    Fbloginbtn.setReadPermissions("email");
                    Fbloginbtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            String accessToken = loginResult.getAccessToken().getToken();
                            provider_id = accessToken;
                            editor.putString("tokenFB",provider_id);
                            editor.commit();
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.i("LoginActivity", response.toString());
                                    startActivity(new Intent(Login.this,FullscreenActivity.class));
                                    finish();
                                }
                            });

                            request.executeAsync();

                        }

                        @Override
                        public void onCancel() {

                            Toast.makeText(Login.this, "cancel", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(FacebookException error) {

                            Toast.makeText(Login.this, String.valueOf(error.getCause()), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        //Applying font
        textView.setTypeface(typeface);

        //when btnEmail clicked
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,FullscreenActivity.class));
                finish();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
}
