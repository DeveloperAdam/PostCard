package techease.com.postcard.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import techease.com.postcard.R;

import static android.content.Context.MODE_PRIVATE;


public class PCardFrag extends Fragment {

    ImageView ivImage,ivMessage,ivSign,ivLoc;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_pcard, container, false);

        //Declaration
        ivImage=(ImageView)view.findViewById(R.id.ivPic);
        ivSign=(ImageView)view.findViewById(R.id.ivSignature);
        ivMessage=(ImageView)view.findViewById(R.id.ivMesage);
        sharedPreferences = getActivity().getSharedPreferences("com.postcard", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Person Picture
        SharedPreferences myPrefrence = getActivity().getPreferences(MODE_PRIVATE);
        String    image = myPrefrence.getString("takepic", "");
        Bitmap bitmap;
        if (!image.equals("")) {
            bitmap = decodeToBase64(image);
            ivImage.setImageBitmap(bitmap);
        }

        //SignatureImage
        String img=myPrefrence.getString("imagePath","");
      //  Log.d("signa",img);
        Bitmap bitmap1=BitmapFactory.decodeFile(img);
        ivSign.setImageBitmap(bitmap1);
//
//        String sImage=getArguments().getString("imagePath");
//        Bitmap signBitmap=BitmapFactory.decodeFile(sImage);
//        ivSign.setImageBitmap(signBitmap);


        //MessageImage
        Bitmap mImage=getArguments().getParcelable("bit");
//        Log.d("texty",mImage.toString());
       // Bitmap messageBitmap=decodeToBase64(mImage);
        ivMessage.setImageBitmap(mImage);



        return view;
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
