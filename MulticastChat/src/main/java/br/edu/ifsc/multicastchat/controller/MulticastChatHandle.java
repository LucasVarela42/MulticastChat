/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.multicastchat.controller;

import br.edu.ifsc.multicastchat.view.MulticastChatForm;

/**
 *
 * @author Lucas
 */
public class MulticastChatHandle {

    private MulticastChatForm multicastChatForm;

    public MulticastChatHandle(MulticastChatForm multicastChatForm) {
        this.multicastChatForm = multicastChatForm;
    }

    public void updateChat(String message) {
        StringBuilder builder = new StringBuilder();
        
        if (message.isEmpty()) {
            multicastChatForm.getjTextAreaChat().setText(null);
        }
        if (!multicastChatForm.getjTextAreaChat().getText().isEmpty()) {
            builder.append("\n");
        }
        
        builder.append(message);
        multicastChatForm.getjTextAreaChat().append(builder.toString());
    }

}
