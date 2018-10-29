package client.listeners;

import client.app.ClientApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionListener implements ActionListener {
    private ClientApp app;
    private JTextField pseudoEntry;

    public ConnectionListener(ClientApp clientapp, JTextField pseudoEntry){
        this.app = clientapp;
        this.pseudoEntry = pseudoEntry;
    }

    public void actionPerformed(ActionEvent e) {
        String pseudo = pseudoEntry.getText();
        if(pseudo.trim().equalsIgnoreCase("") || pseudo.contains("@")){
            JOptionPane.showMessageDialog(null, "Pseudo invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        app.connect(pseudo);
    }
}
