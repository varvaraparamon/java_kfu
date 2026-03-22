package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("127.0.0.1", 7777);
            Scanner scanner = new Scanner(System.in);

            PrintWriter toServer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));

            new Thread(() -> {
                while (true) {
                    try {
                        String messageFromServer = fromServer.readLine();
                        System.out.println(messageFromServer);
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }).start();

            while (true) {
                String messageToServer = scanner.nextLine();
                toServer.println(messageToServer);
            }


        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }


    }
}

