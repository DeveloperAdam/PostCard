package techease.com.postcard.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import techease.com.postcard.R;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("com.postcard", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Thread thread=new Thread()
        {
            public void run()
            {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

         //           getFragmentManager().beginTransaction().replace(R.id.SplashContainer,new SignUpFrag()).commit();
                    String token=sharedPreferences.getString("token","");
                    if (!token.equals(""))
                    {
                       Intent intent=new Intent(SplashScreen.this,FullscreenActivity.class);
                       intent.putExtra("token",token);
                       startActivity(intent);

                    }
                    else

                    startActivity(new Intent(SplashScreen.this,Login.class));
                    finish();

                }

            }
        };
        thread.start();
    }
}
