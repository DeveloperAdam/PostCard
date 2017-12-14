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


public class EmailVerificationFrag extends Fragment {

    TextView tvTitle,someTExt;
    EditText etCode;
    Typeface typeface,typeface2;
    Button btnVerifyCode;
    String code;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_email_verification, container, false);

        //Declaration
        tvTitle=(TextView)view.findViewById(R.id.tvTitleVerifyEmail);
        etCode=(EditText)view.findViewById(R.id.etCode);
        btnVerifyCode=(Button)view.findViewById(R.id.btnVerifyCode);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"SanFrancisco.otf");
        someTExt=(TextView)view.findViewById(R.id.someText);

        //Applying font
        tvTitle.setTypeface(typeface);
        etCode.setTypeface(typeface2);
        btnVerifyCode.setTypeface(typeface2);
        someTExt.setTypeface(typeface2);

        //When btnVerifycode clicked
        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataInput();
            }
        });
        return  view;
    }

    private void onDataInput() {
        code=etCode.getText().toString();
        if (code.equals(""))
        {
            etCode.setError("Please enter the code here");
        }
        else
            apicall();
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.VerifyEmailApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma respoonse", response);
                Toast.makeText(getActivity(), "Verified", Toast.LENGTH_SHORT).show();

                Fragment fragment=new EmailLoginFrag();
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
                params.put("code", code);
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
