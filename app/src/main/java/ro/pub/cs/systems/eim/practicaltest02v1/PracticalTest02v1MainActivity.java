package ro.pub.cs.systems.eim.practicaltest02v1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02v1MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test02v1_main);

        editText = findViewById(R.id.edittext1);
        button = findViewById(R.id.button1);

        button.setOnClickListener(view -> {
            String prefix = editText.getText().toString();
            new WebRequestTask().execute(prefix);
        });


    }
}

