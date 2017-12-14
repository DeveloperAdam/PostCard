package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import techease.com.postcard.Configurations.Links;
import techease.com.postcard.R;


public class EmailLoginFrag extends Fragment {

    TextView tvTitle,tvSignUp,tvForgetPass;
    EditText etUsernameEmail,etPassword;
    Button btnLogin;
    Typeface typeface,typeface2;
    String strUserNameEmail,strPass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_email_login, container, false);

        //Declaration
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"SanFrancisco.otf");
        tvTitle=(TextView)view.findViewById(R.id.tvtitleLoginEmail);
        tvSignUp=(TextView)view.findViewById(R.id.tvSignUp);
        tvForgetPass=(TextView)view.findViewById(R.id.tvforgetPass);
        etUsernameEmail=(EditText)view.findViewById(R.id.etUsernameEmail);
        etPassword=(EditText)view.findViewById(R.id.etPasswordLogin);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);

        //Applying font
        tvTitle.setTypeface(typeface);
        tvSignUp.setTypeface(typeface2);
        tvForgetPass.setTypeface(typeface2);
        etUsernameEmail.setTypeface(typeface2);
        etPassword.setTypeface(typeface2);
        btnLogin.setTypeface(typeface2);

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ForgetPasswordFrag();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("Abc").commit();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new SignUpFrag();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("abc").commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataInput();
            }
        });

        return view;
    }

    private void onDataInput() {
        strUserNameEmail=etUsernameEmail.getText().toString();
        strPass=etPassword.getText().toString();
        if (strUserNameEmail.equals(""))
        {
            etUsernameEmail.setError("Please enter your username or email");
        }else if (strPass.equals(""))
        {
            etPassword.setError("Please enter your password");
        }
        else
            apicall();
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.LoginApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma respoonse", response);
                //  DialogUtils.sweetAlertDialog.dismiss();

                Fragment fragment=new CaptureImageFrag();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("zma error", String.valueOf(error.getCause()));
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                if((!android.util.Patterns.EMAIL_ADDRESS.matcher(strUserNameEmail).matches()))
//                {
//                    params.put("identity", strUserNameEmail);
//                    params.put("password", strPass);
//                    Log.d("zma params", String.valueOf(params));
//                    return checkParams(params);
//                }else

                params.put("identity", strUserNameEmail);
                params.put("password", strPass);
                Log.d("zma params", String.valueOf(params));
                return checkParams(params);
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }
    private Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;

    }


}
