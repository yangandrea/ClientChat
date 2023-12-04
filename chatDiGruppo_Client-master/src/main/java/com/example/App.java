package com.example;

// Client
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class App {
    public static void main(String[] args) {
        new App().startClient();
    }

    private void startClient() {
        try (Socket socket = new Socket("localHost", 52100);
             BufferedReader inDalServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader leggiConsole = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter scrittore = new PrintWriter(socket.getOutputStream(), true)) {

            // Richiedi all'utente di inserire l'username
            System.out.print("Inserisci il tuo username: ");
            String username = leggiConsole.readLine();
            scrittore.println(username);

            // Avvia un thread per leggere i messaggi dal server
            new Thread(() -> {
                try {
                    String massaggioServer;
                    while ((massaggioServer = inDalServer.readLine()) != null) {
                        System.out.println(massaggioServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Invia messaggi al server
            String in;
            while ((in = leggiConsole.readLine()) != null) {
                scrittore.println(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}