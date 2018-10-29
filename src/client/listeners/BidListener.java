package client.listeners;

import client.app.ClientApp;
import client.view.BidButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BidListener implements ActionListener {
    ClientApp app;

    public BidListener(ClientApp clientapp){
        this.app = clientapp;
    }

    public void actionPerformed(ActionEvent e) {
        app.bid((BidButton) e.getSource());
    }
}
