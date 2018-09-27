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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Aluno
 */
public class RxController extends Thread {

    private byte[] rxData;
    private JTextArea textArea;
    private MulticastSocket socket;
    private DatagramPacket rxPacket;
   

    public RxController(JTextArea textArea, MulticastSocket socket) {
        this.textArea = textArea;
        this.socket = socket;
        this.rxData = new byte[65507];
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.rxPacket = new DatagramPacket(this.rxData, this.rxData.length);
                this.socket.receive(this.rxPacket);
                InetAddress sourceIP = rxPacket.getAddress();
                int sourcePort = rxPacket.getPort();
                String message = new String(rxPacket.getData());

                this.textArea.append("[" + sourceIP + "/" + sourcePort + "]: " + message + "\n");
            } catch (IOException ex) {
                Logger.getLogger(RxController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
