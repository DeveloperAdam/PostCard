package techease.com.postcard.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import techease.com.postcard.Activities.Login;
import techease.com.postcard.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class CaptureImageFrag extends Fragment {

    ImageView imageView;
    Button btnChoose,btnLogout;
    String SaveImage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tvNext;
    private int PICK_IMAGE_REQUEST = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capture_image, container, false);

        //getHasKey();

        //Declaration
        imageView = (ImageView) view.findViewById(R.id.iv);
        btnChoose = (Button) view.findViewById(R.id.btnPicChooser);
        sharedPreferences = getActivity().getSharedPreferences("com.postcard", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tvNext=(TextView)view.findViewById(R.id.tvNext);
        btnLogout=(Button)view.findViewById(R.id.btnLogout);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new AddingTextAndSign();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("hgdh").commit();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean temp=false;
                Boolean temp2=false;
                editor.putBoolean("token",temp);
                editor.putBoolean("Fbtoken",temp2);
                editor.commit();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();

            }
        });

        SharedPreferences myPrefrence = getActivity().getPreferences(MODE_PRIVATE);
        SaveImage=myPrefrence.getString("imagePreferance","");
        Bitmap bitmap;
        if(!SaveImage.equals(""))
        {
            bitmap=decodeToBase64(SaveImage);
            imageView.setImageBitmap(bitmap);
        }
        //When button clicked
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return view;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, 1);

                } else if (items[which].equals("Choose from Library")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
                    Intent intent = new Intent();
// Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    imageView.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                   // f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    outFile = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, outFile);

                    outFile.flush();

                    outFile.close();
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }else  if (requestCode==PICK_IMAGE_REQUEST)
            {
                InputStream stream;
                try {
                    Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT).show();
                    stream = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap realImage = BitmapFactory.decodeStream(stream);
                    imageView.setImageBitmap(realImage);
                    SharedPreferences myPrefrence = getActivity().getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefrence.edit();
                    editor.putString("imagePreferance", encodeToBase64(realImage));
                    editor.commit();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Method for generating keyhash value
//    void getHasKey()
//    {
//        //Get Has Key
//        try
//        {
//            PackageInfo info = getActivity().getPackageManager().getPackageInfo("techease.com.postcard", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures)
//            {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

