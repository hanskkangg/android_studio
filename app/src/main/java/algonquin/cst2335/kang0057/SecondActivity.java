package algonquin.cst2335.kang0057;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class SecondActivity extends AppCompatActivity {

    // Correctly declare the ActivityResultLauncher as a member variable
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);

        // Retrieve the email address from the Intent
        Intent fromPrevious = getIntent();
        String email = fromPrevious.getStringExtra("EmailAddress");

        // Update the TextView with the email
        if (email != null && !email.isEmpty()) {
            welcomeTextView.setText("Welcome back " + email);
        } else {
            // Fallback text in case email is not provided
            welcomeTextView.setText("Welcome back!");
        }

        ImageView imageView = findViewById(R.id.imageView);
        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(theImage);
        }

        // Initialize the ActivityResultLauncher
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            ImageView imageView = findViewById(R.id.imageView);
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(imageBitmap);

                            // Save the bitmap
                            try (FileOutputStream fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE)) {
                                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        Button takePictureButton = findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(cameraIntent);
        });
    }
}
