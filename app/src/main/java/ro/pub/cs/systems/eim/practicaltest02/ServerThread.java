package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {

    private ServerSocket serverSocket;
    private final int port;


    private final HashMap<String, WeatherForecastInformation> data = new HashMap<>();

    public ServerThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.e("SERVER", "[SERVER THREAD] Could not create ServerSocket: " + e.getMessage());
            serverSocket = null;
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }


    public synchronized WeatherForecastInformation getFromCache(String city) {
        return data.get(city);
    }

    public synchronized void putInCache(String city, WeatherForecastInformation info) {
        data.put(city, info);
    }

    @Override
    public void run() {
        if (serverSocket == null) {
            Log.e("SERVER", "[SERVER THREAD] ServerSocket is null!");
            return;
        }

        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("SERVER", "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i("SERVER", "[SERVER THREAD] Connection from "
                        + socket.getInetAddress() + ":" + socket.getLocalPort());


                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (IOException e) {
            Log.e("SERVER", "[SERVER THREAD] accept() / run exception: " + e.getMessage());
        }
    }


    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {}
        }
    }
}
