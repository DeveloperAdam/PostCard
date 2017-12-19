package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import techease.com.postcard.R;


public class LoginFrag extends Fragment {

    TextView textView;
    Typeface typeface;
    Button btnEmail,Fb,btnGoogle;
    LoginButton Fbloginbtn;
    SignInButton signInButton;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
   GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FrameLayout frameLayout;
    GoogleSignInClient mGoogleSignInClient;

    String provider_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferences = getActivity().getSharedPreferences("com.postcard", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        textView=(TextView)view.findViewById(R.id.tvpostcard);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        btnEmail=(Button)view.findViewById(R.id.btnContinueWithEmail);

        signInButton = view.findViewById(R.id.sign_in_button);
        btnGoogle=(Button)view.findViewById(R.id.btnGoogle);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
       // updateUI(account);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInButton.performClick();
                Intent signInIntent =mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        Fbloginbtn=(LoginButton)view.findViewById(R.id.btnFB);
        Fb=(Button)view.findViewById(R.id.btnFbLogin);
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
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.i("LoginActivity", response.toString());

                                    String token="token";
                                    editor.putString("token",token).commit();
                                    Fragment fragment=new CaptureImageFrag();
                                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

                                }
                            });

                            request.executeAsync();

                        }

                        @Override
                        public void onCancel() {

                            Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(FacebookException error) {

                            Toast.makeText(getActivity(), String.valueOf(error.getCause()), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        textView.setTypeface(typeface);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new EmailLoginFrag();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("zma e", String.valueOf(e.getStatusCode()));
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

        Fragment fragment=new CaptureImageFrag();
        getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

    }
}
