package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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

public class NewPasswordFrag extends Fragment {

    TextView Title;
    EditText etNewPass,etConfirmPass;
    Button btnSet;
    Typeface typeface,typeface2;
    String strNewPass,strConfirmPass,code;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new_password, container, false);

        //Declaration
        Title=(TextView)view.findViewById(R.id.tvTitleNewPass);
        etNewPass=(EditText)view.findViewById(R.id.etNewPass);
        etConfirmPass=(EditText)view.findViewById(R.id.etConfirmPass);
        btnSet=(Button)view.findViewById(R.id.btnSetNewPass);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"SanFrancisco.otf");
        sharedPreferences = getActivity().getSharedPreferences("com.postcard", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        code=sharedPreferences.getString("code","");

        //Applying font
        Title.setTypeface(typeface);
        etNewPass.setTypeface(typeface2);
        etConfirmPass .setTypeface(typeface2);
        btnSet.setTypeface(typeface2);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataInput();
            }
        });
        return view;
    }

    private void onDataInput() {
        strNewPass=etNewPass.getText().toString();
        strConfirmPass=etConfirmPass.getText().toString();
        if (strNewPass.equals("")||strNewPass.length()<6)
        {
            etNewPass.setError("Please more then 6 digit password");
        }
        else if (strConfirmPass.equals("")|| !strNewPass.equals(strConfirmPass))
        {
            etConfirmPass.setError("Password does'nt match");
        }
        else
            apicall();
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.SetNewPassApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma respoonse", response);

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
                Map<String, String> params = new HashMap<>();;
                params.put("password", strConfirmPass);
                params.put("code",code);
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
