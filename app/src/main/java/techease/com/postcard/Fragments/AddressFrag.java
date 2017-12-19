package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import techease.com.postcard.R;

public class AddressFrag extends Fragment {

    CountryCodePicker spinner;
    Button btnConfirm;
    EditText etFname,etZip,etBuilding,etState,etStreet,etCity;
    TextView tvTitle;
    Typeface typeface,typeface2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_address, container, false);

        spinner=(CountryCodePicker) view.findViewById(R.id.spinerrCountry);
        btnConfirm=(Button)view.findViewById(R.id.btnConfirm);
        etFname=(EditText)view.findViewById(R.id.etFName);
        etBuilding=(EditText)view.findViewById(R.id.etApartment);
        etCity=(EditText)view.findViewById(R.id.etCity);
        etState=(EditText)view.findViewById(R.id.etState);
        etStreet=(EditText)view.findViewById(R.id.etStreet);
        etZip=(EditText)view.findViewById(R.id.etZipCode);
        tvTitle=(TextView)view.findViewById(R.id.tvAddress);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"SanFrancisco.otf");


        spinner.setTypeFace(typeface2);
        tvTitle.setTypeface(typeface);
        etZip.setTypeface(typeface2);
        etStreet.setTypeface(typeface2);
        etState.setTypeface(typeface2);
        etCity.setTypeface(typeface2);
        etBuilding.setTypeface(typeface2);
        etFname.setTypeface(typeface2);
        btnConfirm.setTypeface(typeface2);

        String arg=getArguments().getString("imagePath");
        final Bundle bundle=new Bundle();
        bundle.putString("imagePath",arg);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Converting text to image
                etBuilding.setCursorVisible(false);
                etBuilding.buildDrawingCache();
                etCity.setCursorVisible(false);
                etCity.buildDrawingCache();
                Bitmap bit=Bitmap.createBitmap(etBuilding.getDrawingCache());

                Fragment fragment=new PCardFrag();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("abkuh").commit();

            }
        });



        return view;
    }


}
