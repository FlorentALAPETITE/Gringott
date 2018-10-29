package client.listeners;

import client.app.ClientApp;
import client.view.BidButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BidListener implements ActionListener {
    private ClientApp app;

    public BidListener(ClientApp clientapp){
        this.app = clientapp;
    }

    public void actionPerformed(ActionEvent e) {
        BidButton source = (BidButton)e.getSource();
        Double offer;
        try {
            offer = Math.ceil(Double.parseDouble(source.getContent()));
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Merci de mettre un nombre.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Double original = source.getItem().getPrice();
        if (offer < original*0.2) {
            JOptionPane.showMessageDialog(null, "Vous devez enchérir d'au moins 20% du prix courant.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        app.bid(source.getItem(), offer);
    }
}
