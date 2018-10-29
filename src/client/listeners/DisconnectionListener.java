package client.listeners;

import client.app.ClientApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisconnectionListener implements ActionListener {
    ClientApp app;

    public DisconnectionListener(ClientApp clientapp){
        this.app = clientapp;
    }

    public void actionPerformed(ActionEvent e) {
        app.disconnect();
    }
}
