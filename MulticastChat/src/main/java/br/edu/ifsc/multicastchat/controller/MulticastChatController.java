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
        System.out.println("Left group " + groupAddress + ":" + port);

        if (!socket.isClosed()) {
            socket.close();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.txData = message.getBytes();
        this.txPacket = new DatagramPacket(this.txData, this.txData.length, this.group, this.port);
        this.socket.send(this.txPacket);
    }

    public MulticastSocket getSocket() {
        return socket;
    }
}
