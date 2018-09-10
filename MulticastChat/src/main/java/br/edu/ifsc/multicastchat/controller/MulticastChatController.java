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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas
 */
public class MulticastChatController extends Thread {

    private int port;
    private InetAddress group;
    private MulticastSocket socket;
    private byte[] data;
    private DatagramPacket msgOut;
    private DatagramPacket msgIn;
    private byte[] buffer;
    private String message;

    public MulticastChatController(int port, String group) throws UnknownHostException, IOException {
        this.port = port;
        this.group = InetAddress.getByName(group);
        this.socket = new MulticastSocket(port);
        start();
    }

    public void logon() throws IOException {
        try {
            socket.joinGroup(group);
        } catch (IOException e) {
            throw new IOException("IOException in logon: " + e.getMessage());
        }
    }

    public void logoff() throws IOException {
        try {
            if (socket.isConnected()) {
                socket.leaveGroup(group);
            }
        } catch (IOException e) {
            throw new IOException("IOException in logoff: " + e.getMessage());
        }
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.data = message.getBytes();
        this.msgOut = new DatagramPacket(this.data, this.data.length, this.group, this.port);
        this.socket.send(this.msgOut);
    }

    public void receiveMessage() throws IOException {
        this.buffer = new byte[1000];
        while (true) {
            this.msgIn = new DatagramPacket(this.buffer, this.buffer.length);
            this.socket.receive(this.msgIn);
            this.message = new String(this.msgIn.getData());
            System.out.println(message);
        }
    }

    @Override
    public void run() {
        try {
            receiveMessage();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
