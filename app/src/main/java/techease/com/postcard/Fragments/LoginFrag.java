package techease.com.postcard.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import techease.com.postcard.R;


public class LoginFrag extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferences = getActivity().getSharedPreferences("com.postcard", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        textView=(TextView)view.findViewById(R.id.tvpostcard);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        btnEmail=(Button)view.findViewById(R.id.btnContinueWithEmail);

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

    }
}
