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
 * @author Aluno
 */
public class MulticastChatReceiver extends Thread {

    private final byte[] rxData = new byte[1024];
    private MulticastSocket socket = null;
    private MulticastChatHandle handle = null;

    public MulticastChatReceiver(MulticastSocket socket, MulticastChatHandle handle) {
        this.socket = socket;
        this.handle = handle;
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                DatagramPacket rxPacket = new DatagramPacket(rxData, rxData.length);

                socket.receive(rxPacket);
                
                String received = Crypto.decrypt(new String(rxPacket.getData(), 0, rxPacket.getLength())); //Message decrypted

                String senderAddress = rxPacket.getAddress().getCanonicalHostName();

                System.out.println("recebeu pacote de " + senderAddress);
                System.out.println("Mensagem " + received);

                //if (!senderAddress.equals(myAddress)) {
                    handle.updateChat(received);
                //}

            }
        } catch (IOException ex) {
            interrupt();
        }
    }

}
