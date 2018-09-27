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
public class MulticastChatController extends Thread {

    private MulticastSocket socket;
    private InetAddress group;
    private String groupAddress;
    private int port;
    private String ip;
    private int ttl;
    private byte[] txData;
    private DatagramPacket txPacket;
    private DatagramPacket msgIn;
    private byte[] buffer;
    private String message;

    private volatile boolean running = true;

    public MulticastChatController(String groupAddress, int port, int ttl) throws IOException {
        this.groupAddress = groupAddress;
        this.port = port;
        this.group = InetAddress.getByName(groupAddress);
        this.ttl = ttl;
        this.ip = InetAddress.getLocalHost().getHostAddress();
        
        this.socket = new MulticastSocket(port);
        this.socket.setTimeToLive(ttl);

    }

    public void logon() throws IOException {
        try {
            socket.joinGroup(group);
            System.out.println("Joined group " + groupAddress + ":" + port);
            //repeaterController.start();
            this.start();
        } catch (IOException e) {
            throw new IOException("IOException in logon: " + e.getMessage());
        }
    }

    public void logoff() throws IOException {
        running = false;
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
        System.out.println("Left group " + groupAddress + ":" + port);
    }

    public void sendMessage(String message) throws IOException {
        this.txData = message.getBytes();
        this.txPacket = new DatagramPacket(this.txData, this.txData.length, this.group, this.port);
        this.socket.send(this.txPacket);
        //Fazer o append aqui
        System.out.println("Broadcasted message: " + message);
    }

    public void receiveMessage() throws IOException {
        while (running) {
            this.buffer = new byte[1000];
            this.msgIn = new DatagramPacket(this.buffer, this.buffer.length);
            this.socket.receive(this.msgIn);
            this.message = new String(this.msgIn.getData());
            System.out.println("received message: " + message);
        }
    }

    public String getMessage() {
        return message;
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
