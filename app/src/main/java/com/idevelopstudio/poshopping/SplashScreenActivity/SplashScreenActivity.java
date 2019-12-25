package com.idevelopstudio.poshopping.SplashScreenActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.idevelopstudio.poshopping.MainActivity;
import com.idevelopstudio.poshopping.R;

public class SplashScreenActivity extends AppCompatActivity {


    private LottieAnimationView lottieAnimationView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottieAnimationView = findViewById(R.id.lottie_bg);
        imageView = findViewById(R.id.iv);
        lottieAnimationView.playAnimation();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startMain();
            }
        }, 1000);

    }


    private void startMain(){
        Intent intent = new Intent(SplashScreenActivity.this.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(SplashScreenActivity.this, imageView, "logoTransition");
            SplashScreenActivity.this.startActivity(intent, options.toBundle());
        } else {
            SplashScreenActivity.this.startActivity(intent);
        }
    }
}
