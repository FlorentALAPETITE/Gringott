package client.view;

import client.app.ClientApp;
import client.listeners.DisconnectionListener;

import javax.swing.*;

public class DisconnectionPanel extends JPanel {

    ClientApp client;

    DisconnectionPanel(ClientApp client){
        super();
        this.client = client;

        JButton logout = new JButton("Deconnexion");
        logout.addActionListener(new DisconnectionListener(client));
        this.add(logout);
    }
}
