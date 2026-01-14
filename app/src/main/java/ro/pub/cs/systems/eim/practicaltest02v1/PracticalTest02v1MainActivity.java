package ro.pub.cs.systems.eim.practicaltest02v1;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02v1MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;

    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String output = intent.getStringExtra("output");
            if (output != null) {
                textView.setText(output);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test02v1_main);

        editText = findViewById(R.id.edittext1);
        button = findViewById(R.id.button1);
        textView = findViewById(R.id.textview1);

        button.setOnClickListener(view -> {
            String prefix = editText.getText().toString();
            new WebRequestTask(this).execute(prefix);
        });


    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ro.pub.cs.systems.eim.practicaltest02v1.AUTOCOMPLETE");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(Receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(Receiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(Receiver);
    }


}

