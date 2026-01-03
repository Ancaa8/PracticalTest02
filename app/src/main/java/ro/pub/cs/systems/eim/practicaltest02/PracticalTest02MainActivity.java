package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private static final String TAG = "TEST";

    private EditText serverPortEditText;
    private Button connectButton;

    private ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = findViewById(R.id.serverPort);
        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(v -> startServerAndTest());
    }

    private void startServerAndTest() {
        String portStr = serverPortEditText.getText().toString().trim();
        if (portStr.isEmpty()) {
            Toast.makeText(this, "Server port should be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        int port = Integer.parseInt(portStr);

        // oprim dacă era pornit
        if (serverThread != null) {
            serverThread.stopThread();
            serverThread = null;
        }

        serverThread = new ServerThread(port);
        if (serverThread.getServerSocket() == null) {
            Toast.makeText(this, "Could not start server!", Toast.LENGTH_SHORT).show();
            return;
        }

        serverThread.start();
        Toast.makeText(this, "Server started on port " + port, Toast.LENGTH_SHORT).show();

        // mic delay ca serverul să intre în accept()
        new Thread(() -> {
            try {
                Thread.sleep(300);
                runClientTest(port);
            } catch (Exception e) {
                Log.e(TAG, "Test error: " + e.getMessage());
            }
        }).start();
    }

    /**
     * CLIENT DE TEST (NU clientul cerut la Ex. 4)
     * Demonstrează Ex. 3c + 3d
     */
    private void runClientTest(int port) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", port);

            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            // cerere de test
            pw.println("London");
            pw.println("all");
            pw.flush();

            Log.i(TAG, "Response from server:");

            String line;
            while ((line = br.readLine()) != null) {
                Log.i(TAG, line);
            }

        } catch (Exception e) {
            Log.e(TAG, "Client test exception: " + e.getMessage());
        } finally {
            try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
