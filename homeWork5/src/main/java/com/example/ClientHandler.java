package com.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String nameClient;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.nameClient = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + nameClient + " подключился");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void broadcastMessage (String messageToSend) {
        for (ClientHandler clientHandlers : clientHandlers) {
            try {
                if (!clientHandlers.nameClient.equals(nameClient)) {
                    clientHandlers.bufferedWriter.write(messageToSend);
                    clientHandlers.bufferedWriter.newLine();
                    clientHandlers.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + nameClient + " вышел");
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        closeQuietly(bufferedReader);
        closeQuietly(bufferedWriter);
        closeQuietly(socket);
    }

    // это на тот случай, если бы в прошлом коде ошибка выпала бы в bufferedReader.close(),
    // что миновало бы закрытие bufferedWriter и socket
    private void closeQuietly(AutoCloseable resource) {
        try {
            if (resource != null) resource.close();
        } catch (Exception e) {
            System.out.println("Ошибка при закрытии ресурса: " + e.getMessage());
        }
    }
}
