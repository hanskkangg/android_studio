package algonquin.cst2335.kang0057;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.kang0057.data.MainViewModel;
import algonquin.cst2335.kang0057.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel model;

    private ImageButton myImageButton;
    private ImageView myImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        model = new ViewModelProvider(this).get(MainViewModel.class);

        // Find the TextView, EditText, and Button using View Binding
        TextView mytext = binding.textview;
        EditText myedit = binding.myedittext;
        Button mybutton = findViewById(R.id.button);

        // Get references to CheckBox, Switch, and RadioButton
        CheckBox checkBox = findViewById(R.id.checkBox);
        Switch switchButton = findViewById(R.id.switchButton);
        RadioButton radioButton = findViewById(R.id.radioButton);

        // Observe changes in LiveData and update CompoundButtons
        model.getCoffeePreference().observe(this, coffeePreference -> {
            checkBox.setChecked(coffeePreference);
            switchButton.setChecked(coffeePreference);
            radioButton.setChecked(coffeePreference);
        });

        // Set OnCheckedChangeListener using Lambda functions for CheckBox, Switch, and RadioButton
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.getCoffeePreference().postValue(isChecked));
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> model.getCoffeePreference().postValue(isChecked));
        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> model.getCoffeePreference().postValue(isChecked));

        // Button click listener
        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to run when the button is clicked
                String editString = myedit.getText().toString();
                model.getEditTextData().postValue(editString);
            }
        });

        // Observe changes in LiveData and update TextView
        model.getEditTextData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newText) {
                mytext.setText("Your edit text has: " + newText);
            }
        });

        // Initialize ImageButton and ImageView
        myImageButton = findViewById(R.id.myImageButton);
        myImageView = findViewById(R.id.myImageView);

        // Add OnClickListener for ImageButton to show Toast message with width and height
        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get width and height of ImageButton
                int width = myImageButton.getWidth();
                int height = myImageButton.getHeight();

                // Show a Toast message with width and height
                Toast.makeText(MainActivity.this, "The width = " + width + " and height = " + height, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
