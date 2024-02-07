package algonquin.cst2335.kang0057;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import algonquin.cst2335.kang0057.R;
import algonquin.cst2335.kang0057.data.MainViewModel;
import algonquin.cst2335.kang0057.databinding.ActivityMainBinding;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        model = new ViewModelProvider(this).get(MainViewModel.class);

        // Find the TextView using View Binding
        TextView mytext = binding.textview;
        // Find the EditText using View Binding
        EditText myedit = binding.myedittext;

        Button mybutton = findViewById(R.id.button);
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
    }
}
