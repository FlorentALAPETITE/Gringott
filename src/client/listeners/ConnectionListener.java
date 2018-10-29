package client.listeners;

import client.app.ClientApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionListener implements ActionListener {
    ClientApp app;

    public ConnectionListener(ClientApp clientapp){
        this.app = clientapp;
    }

    public void actionPerformed(ActionEvent e) {
        app.connect();
    }
}
