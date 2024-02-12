package algonquin.cst2335.kang0057;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    // Declaring the TAG variable for logging
    private static final String TAG = "MainActivity";

    ImageView imgView;
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Using the TAG variable for logging
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        sw = findViewById(R.id.languageSwitch);
        imgView=findViewById(R.id.imageView2);
        sw.setOnCheckedChangeListener((btn, isChecked) -> {

            if (isChecked) {
                RotateAnimation rotate = new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(5000);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());

                imgView.startAnimation(rotate);
            } else {
                imgView.clearAnimation();
            }

        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        // Example of using TAG in another lifecycle method
        Log.d(TAG, "onStart called");
        Log.w(TAG, "onStart(): The activity is about to become visible.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        // Log message for onResume
        Log.w(TAG, "onResume(): The activity has become visible (it is now resumed).");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called"); // Log message for onPause
        Log.w(TAG, "onPause(): Another activity is taking focus (this activity is about to be paused).");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");   // Log message for onStop
        Log.w(TAG, "onStop(): The activity is no longer visible (it is now stopped).");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        Log.w(TAG, "onDestroy(): The activity is about to be destroyed.");
    }
}
