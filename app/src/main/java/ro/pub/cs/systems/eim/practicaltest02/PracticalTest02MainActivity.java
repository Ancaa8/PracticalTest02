package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private ServerThread serverThread;

    private EditText serverPortEditText;
    private Button connectButton;

    private EditText addressEditText;
    private EditText portClientEditText;
    private EditText cityEditText;
    private Spinner informationTypeSpinner;

    private Button getWeatherForecastButton;
    private TextView weatherForecastTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        // SERVER UI
        serverPortEditText = findViewById(R.id.serverPort);
        connectButton = findViewById(R.id.connectButton);

        // CLIENT UI
        addressEditText = findViewById(R.id.address);
        portClientEditText = findViewById(R.id.portClient);
        cityEditText = findViewById(R.id.city);
        informationTypeSpinner = findViewById(R.id.information_type_spinner);

        getWeatherForecastButton = findViewById(R.id.getWeatherForecastButton);
        weatherForecastTextView = findViewById(R.id.weather_forecast_text_view);

        // FĂRĂ default-uri: utilizatorul completează tot din UI
        connectButton.setOnClickListener(v -> startServer());
        getWeatherForecastButton.setOnClickListener(v -> startClient());
    }

    private void startServer() {
        String serverPort = serverPortEditText.getText().toString().trim();
        if (serverPort.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "[MAIN ACTIVITY] Server port should be filled!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int port = Integer.parseInt(serverPort);

        // dacă serverul era pornit, îl oprim (altfel port ocupat)
        if (serverThread != null) {
            serverThread.stopThread();
            serverThread = null;
        }

        serverThread = new ServerThread(port);
        if (serverThread.getServerSocket() == null) {
            Log.e("MAIN", "[MAIN ACTIVITY] Could not create server thread!");
            Toast.makeText(getApplicationContext(),
                    "[MAIN ACTIVITY] Could not start server!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        serverThread.start();
        Toast.makeText(getApplicationContext(),
                "[MAIN ACTIVITY] Server started on port " + port,
                Toast.LENGTH_SHORT).show();
    }

    private void startClient() {
        String clientAddress = addressEditText.getText().toString().trim();
        String clientPort = portClientEditText.getText().toString().trim();

        if (clientAddress.isEmpty() || clientPort.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "[MAIN ACTIVITY] Client connection parameters should be filled!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (serverThread == null || !serverThread.isAlive()) {
            Toast.makeText(getApplicationContext(),
                    "[MAIN ACTIVITY] There is no server to connect to!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String city = cityEditText.getText().toString().trim();
        String informationType = informationTypeSpinner.getSelectedItem().toString().trim();

        if (city.isEmpty() || informationType.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        weatherForecastTextView.setText("");

        ClientThread clientThread = new ClientThread(
                clientAddress,
                Integer.parseInt(clientPort),
                city,
                informationType,
                weatherForecastTextView
        );
        clientThread.start();
    }

    @Override
    protected void onDestroy() {
        Log.i("MAIN", "[MAIN ACTIVITY] onDestroy() called");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
