package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class SignUpFrag extends Fragment {

    TextView Title, tvLogin;
    EditText etFullname, etEmail, etPassword, etUsername;
    Button btnMale, btnFemale, btnSignUp;
    Typeface typeface, typeface2;
    String Gender, strEmail, strUsername, strPassword, strFullname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //Decleration
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "SanFrancisco.otf");
        typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "billabong.ttf");
        Title = (TextView) view.findViewById(R.id.tvTitle);
        tvLogin = (TextView) view.findViewById(R.id.tvHaveAccount);
        etFullname = (EditText) view.findViewById(R.id.etFullname);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        btnFemale = (Button) view.findViewById(R.id.btnFemale);
        btnMale = (Button) view.findViewById(R.id.btnMale);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);

        //Applying font
        Title.setTypeface(typeface2);
        tvLogin.setTypeface(typeface);
        etUsername.setTypeface(typeface);
        etPassword.setTypeface(typeface);
        etEmail.setTypeface(typeface);
        etFullname.setTypeface(typeface);
        btnSignUp.setTypeface(typeface);

        //When click on Sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataInput();
            }
        });

        //when Login textview clicked
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EmailLoginFrag();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("abc").commit();
            }
        });

        btnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // btnFemale.setVisibility(View.INVISIBLE);
                btnMale.setBackgroundResource(R.drawable.male_selected);
                btnFemale.setBackgroundResource(R.drawable.female_unselected);
                Gender="male";
            }
        });
        btnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  btnMale.setVisibility(View.INVISIBLE);
                btnFemale.setBackgroundResource(R.drawable.female_selected);
                btnMale.setBackgroundResource(R.drawable.male_unselected);

                Gender="female";
            }
        });

        return view;
    }

    private void onDataInput() {
        strEmail = etEmail.getText().toString();
        strFullname = etFullname.getText().toString();
        strPassword = etPassword.getText().toString();
        strUsername = etUsername.getText().toString();

        if ((!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            etUsername.setError("Please enter the valid email");
        } else if (strUsername.equals("") || strUsername.length() < 6) {
            etUsername.setError("Please enter more then 6 digit username");
        } else if (strPassword.equals("") || strPassword.length() < 6) {
            etPassword.setError("Please enter more then 6 digit password");
        } else if (strFullname.equals("") || strFullname.length() < 2) {
            etFullname.setError("Please enter your Fullname");
        } else
            if (Gender.equals(""))
            {
                Toast.makeText(getActivity(), "Select your gender", Toast.LENGTH_SHORT).show();
            }
            else
            apicall();
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.SignUpApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   DialogUtils.sweetAlertDialog.dismiss();
                Toast.makeText(getActivity(), "A verification code has been sent to your email", Toast.LENGTH_SHORT).show();
                Fragment fragment=new EmailVerificationFrag();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  DialogUtils.sweetAlertDialog.dismiss();
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
                params.put("fullname", strFullname);
                params.put("username",strUsername);
                params.put("email", strEmail);
                params.put("gender",Gender);
                params.put("password", strPassword);
                params.put("Accept", "application/json");
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

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }

        }
        return map;
    }
}