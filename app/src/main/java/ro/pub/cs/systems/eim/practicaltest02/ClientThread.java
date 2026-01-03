package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private final String city;
    private final String informationType;
    private final TextView weatherForecastTextView;

    public ClientThread(String address, int port, String city, String informationType, TextView weatherForecastTextView) {
        this.address = address;
        this.port = port;
        this.city = city;
        this.informationType = informationType;
        this.weatherForecastTextView = weatherForecastTextView;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("CLIENT", "[CLIENT THREAD] Could not create socket!");
                return;
            }

            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);
            if (br == null || pw == null) {
                Log.e("CLIENT", "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
                return;
            }

            // trimitem cererea (2 linii)
            pw.println(city);
            pw.println(informationType);
            pw.flush();

            // citim toate liniile până la EOF
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            final String result = sb.toString().trim();

            // update UI din thread-ul principal
            weatherForecastTextView.post(() -> weatherForecastTextView.setText(result));

        } catch (Exception e) {
            Log.e("CLIENT", "[CLIENT THREAD] Exception: " + e.getMessage());
            weatherForecastTextView.post(() -> weatherForecastTextView.setText("error: " + e.getMessage()));
        } finally {
            try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        }
    }
}
