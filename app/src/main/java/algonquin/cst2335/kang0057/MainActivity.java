package algonquin.cst2335.kang0057;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
public class MainActivity extends AppCompatActivity {

    // Declaring the TAG variable for logging
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w(TAG, "In onCreate() - Loading Widgets");

        EditText emailEditText = findViewById(R.id.emailEditText);
        Button loginButton = findViewById(R.id.loginButton);

        // Create a SharedPreferences object
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        // Load the saved email address, if exists
        String emailAddress = prefs.getString("LoginName", "");
        // Set the loaded email address to the EditText
        emailEditText.setText(emailAddress);

        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            String email = emailEditText.getText().toString();

            // Save the email address using SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", email);
            editor.apply();
// MainActivity
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", email);
            startActivity(nextPage);

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

