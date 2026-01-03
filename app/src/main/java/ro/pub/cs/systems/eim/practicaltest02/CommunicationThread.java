package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("COMM", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            if (br == null || pw == null) {
                Log.e("COMM", "[COMMUNICATION THREAD] Reader/Writer are null!");
                return;
            }

            String city = br.readLine();
            String informationType = br.readLine();

            if (city == null || city.trim().isEmpty() ||
                    informationType == null || informationType.trim().isEmpty()) {
                pw.println("error: missing city or informationType");
                pw.flush();
                return;
            }

            city = city.trim();
            informationType = informationType.trim().toLowerCase();


            WeatherForecastInformation wfi = serverThread.getFromCache(city);
            if (wfi != null) {
                Log.i("COMM", "[COMMUNICATION THREAD] Cache hit for " + city);
            } else {
                Log.i("COMM", "[COMMUNICATION THREAD] Cache miss, fetching from OpenWeather for " + city);
                wfi = OpenWeatherService.getWeather(city);

                if (wfi == null) {
                    pw.println("error: could not fetch data");
                    pw.flush();
                    return;
                }

                serverThread.putInCache(city, wfi);
            }


            switch (informationType) {
                case "all":
                    pw.println("temperature: " + safe(wfi.getTemperature()));
                    pw.println("wind_speed: " + safe(wfi.getWindSpeed()));
                    pw.println("description: " + safe(wfi.getDescription()));
                    pw.println("pressure: " + safe(wfi.getPressure()));
                    pw.println("humidity: " + safe(wfi.getHumidity()));
                    break;

                case "temperature":
                    pw.println(safe(wfi.getTemperature()));
                    break;

                case "wind_speed":
                case "windspeed":
                case "wind":
                    pw.println(safe(wfi.getWindSpeed()));
                    break;

                case "description":
                case "status":
                    pw.println(safe(wfi.getDescription()));
                    break;

                case "pressure":
                    pw.println(safe(wfi.getPressure()));
                    break;

                case "humidity":
                    pw.println(safe(wfi.getHumidity()));
                    break;

                default:
                    pw.println("error: wrong information type (all/temperature/wind_speed/description/pressure/humidity)");
            }

            pw.flush();

        } catch (Exception e) {
            Log.e("COMM", "[COMMUNICATION THREAD] Exception: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private String safe(String s) {
        return (s == null || s.trim().isEmpty()) ? "N/A" : s.trim();
    }
}
