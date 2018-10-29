package client.listeners;

import client.app.ClientApp;
import client.view.SubmitPanel;
import shared.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class SubmissionListener  implements ActionListener {
    private ClientApp app;
    private SubmitPanel panel;

    public SubmissionListener(ClientApp clientapp, SubmitPanel panel){
        this.app = clientapp;
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
        Item item;
        try{
            item = panel.getFieldsContent();
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Merci de mettre des nombres.", "Information", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (RemoteException e1) {
            e1.printStackTrace();
            return;
        }

        if (item.getName().equals("")){
            JOptionPane.showMessageDialog(null, "Merci de mettre un titre.", "Information", JOptionPane.ERROR_MESSAGE);
        }

        app.submit(item);
    }
}
