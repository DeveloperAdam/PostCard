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

public class ForgetPasswordFrag extends Fragment {

    TextView title;
    Typeface typeface,typeface2;
    EditText etEmailVerify;
    Button btnSendCode;
    String strEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forget_password, container, false);

        //Declaration
        title=(TextView)view.findViewById(R.id.tvTitleEnterCodeForVerifyEmail);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"SanFrancisco.otf");
        etEmailVerify=(EditText)view.findViewById(R.id.etEmailForForgetPass);
        btnSendCode=(Button)view.findViewById(R.id.btnSendCodeForgetPass);

        //Applying font
        title.setTypeface(typeface);
        etEmailVerify.setTypeface(typeface2);
        btnSendCode.setTypeface(typeface2);

        //When btnSendCode Clicked
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataInput();
            }
        });
        return view;
    }

    private void onDataInput() {
        strEmail=etEmailVerify.getText().toString();
        if (strEmail.equals("")||(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()))
        {
            etEmailVerify.setError("Please enter your valid email address");
        }
        else
            apicall();
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.EmailForgetPass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Fragment fragment=new VerifyEmailForForgetPass();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strEmail);
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
