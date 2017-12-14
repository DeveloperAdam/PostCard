package techease.com.postcard.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import techease.com.postcard.R;

public class Login extends AppCompatActivity {

    TextView textView;
    Typeface typeface;
    Button btnEmail,Fb;
    LoginButton Fbloginbtn;
    GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Declaration
//     gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
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
        //Fbloginbtn=(LoginButton)findViewById(R.id.btnFBLogin);
        Fb=(Button)findViewById(R.id.btnFbLogin);
        Fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==Fb)
                {
                   // Fbloginbtn.performClick();
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

//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
}
