package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import techease.com.postcard.R;

import static android.content.Context.MODE_PRIVATE;


public class AddingTextAndSign extends Fragment {

    ImageView imageView;
    EditText editTextLOngText;
    Typeface typeface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_adding_text_and_sign, container, false);

        //Declaration
        imageView=(ImageView)view.findViewById(R.id.ivAddingText);
        editTextLOngText=(EditText)view.findViewById(R.id.editText);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"SanFrancisco.otf");

        SharedPreferences myPrefrence = getActivity().getPreferences(MODE_PRIVATE);
        image=myPrefrence.getString("imagePreferance","");
        Bitmap bitmap;
        if(!image.equals(""))
        {
            bitmap=decodeToBase64(image);
            imageView.setImageBitmap(bitmap);
        }

        //Applying font
        editTextLOngText.setTypeface(typeface);



        return  view;
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
