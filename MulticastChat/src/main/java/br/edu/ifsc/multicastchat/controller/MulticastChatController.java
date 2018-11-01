/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.multicastchat.controller;

import br.edu.ifsc.multicastchat.crypto.Crypto;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Lucas
 */
public class MulticastChatController {

    private static MulticastChatController instance;

    private MulticastChatHandle handle = null;
    private MulticastChatReceiver receiver = null;
    private MulticastSocket socket = null;
    private InetAddress group = null;
    private final int port = 50000;
    private byte[] txData = new byte[1024];
    private String username;

    public MulticastChatController() {
    }

    public static MulticastChatController getInstance() {
        if (instance == null) {
            instance = new MulticastChatController();
        }
        return instance;
    }

    public MulticastChatController setUsername(String username) {
        this.username = username;
        return instance;
    }

    public MulticastChatController setHandle(MulticastChatHandle handle) {
        this.handle = handle;
        return instance;
    }

    public void logon(String address) throws IOException {
        try {
            socket = new MulticastSocket(port);
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            receiver = new MulticastChatReceiver(socket, handle);
            receiver.start();

            if (!username.isEmpty()) {
                //handle.updateChat(username + " entrou no chat!");
                send(username, 0);
            }

        } catch (IOException e) {
            throw new IOException("IOException in logon: " + e.getMessage());
        }
    }

    public void logoff() throws IOException {
        try {
            if (socket.isConnected()) {
                socket.leaveGroup(group);
            }

            if (!username.isEmpty()) {
                //handle.updateChat(username + " saiu do chat!");
                send(username, 2);
            }

            receiver.interrupt();
            receiver = null;

            socket.close();
        } catch (IOException e) {
            throw new IOException("IOException in logoff: " + e.getMessage());
        }
    }

    public void send(String message, int type) throws IOException {
        try {
            switch (type) {
                case 0:
                    message = (message + " entrou no grupo!");
                    break;
                case 1:
                    message = (username + " diz: " + message);
                    break;
                case 2:
                    message = (message + " saiu do grupo!");
                    break;
            }
            txData = Crypto.encrypt(message).getBytes(); //Message encrypted
            DatagramPacket txPacket = new DatagramPacket(txData, txData.length, group, port);
            socket.send(txPacket);

            //if (!username.isEmpty()) {
            //   message = messageType(username, 2);
            //}
            //handle.updateChat(message);
        } catch (IOException e) {
            throw new IOException("IOException in send message: " + e.getMessage());
        }
    }

}
