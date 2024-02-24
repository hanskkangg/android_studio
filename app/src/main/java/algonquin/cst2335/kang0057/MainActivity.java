package algonquin.cst2335.kang0057;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Hans Kang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This holds the text at the center of the screen
     */
    private TextView tv = null;
    /**
     * This holds ....
     */
    private EditText et = null;
    /**
     * This holds .....
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editText);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if (!checkPasswordComplexity(password)) {

            }
        });
    }

    /**
     * This function .....
     *
     * @param pw the String object that we are checking
     * @return Returns true if .....
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase = false, foundLowerCase = false, foundNumber = false, foundSpecial = false;

        for (char c : pw.toCharArray()) {
            if (Character.isUpperCase(c)) foundUpperCase = true;
            else if (Character.isLowerCase(c)) foundLowerCase = true;
            else if (Character.isDigit(c)) foundNumber = true;
            else if (isSpecialCharacter(c)) foundSpecial = true;
        }

        if (!foundUpperCase) {
            Toast.makeText(MainActivity.this, "Your password does not have an upper case letter.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(MainActivity.this, "Your password does not have a lower case letter.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(MainActivity.this, "Your password does not have a number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(MainActivity.this, "Your password does not have a special symbol.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
                return true;
            default:
                return false;
        }
    }
}
