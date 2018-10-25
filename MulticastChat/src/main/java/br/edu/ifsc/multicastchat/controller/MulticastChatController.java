/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.multicastchat.controller;

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
                handle.updateChat(username + " entrou no chat!");
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
                handle.updateChat(username + " saiu do chat!");
            }

            receiver.interrupt();
            receiver = null;

            socket.close();
        } catch (IOException e) {
            throw new IOException("IOException in logoff: " + e.getMessage());
        }
    }

    public void send(String message) throws IOException {
        try {
            txData = message.getBytes();
            DatagramPacket txPacket = new DatagramPacket(txData, txData.length, group, port);
            socket.send(txPacket);

            if (!username.isEmpty()) {
                message = username + " diz: " + message;
            }

            handle.updateChat(message);
        } catch (IOException e) {
            throw new IOException("IOException in send message: " + e.getMessage());
        }
    }

}
