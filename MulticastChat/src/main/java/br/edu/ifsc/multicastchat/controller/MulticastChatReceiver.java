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
 * @author Aluno
 */
public class MulticastChatReceiver extends Thread {

    private final byte[] rxData = new byte[1024];
    private MulticastSocket socket  = null;
    private MulticastChatHandle handle = null;

    public MulticastChatReceiver(MulticastSocket socket , MulticastChatHandle handle) {
        this.socket = socket;
        this.handle = handle;
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                DatagramPacket rxPacket = new DatagramPacket(rxData, rxData.length);

                socket.receive(rxPacket);
                String received = new String(rxPacket.getData(), 0, rxPacket.getLength());

                String senderAddress = rxPacket.getAddress().getCanonicalHostName();
                String myAddress = InetAddress.getLocalHost().getCanonicalHostName();
                
                System.out.println(senderAddress);
                System.out.println(myAddress);

                System.out.println("recebeu pacote de " + senderAddress);
                System.out.println("Mensagem " + received);

                if (!senderAddress.equals(myAddress) && !myAddress.equals("debian")) {
                    handle.updateChat(received);
                }
                
            }
        } catch (IOException ex) {
            interrupt();
        }
    }

}
