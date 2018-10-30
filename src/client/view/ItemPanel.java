package client.view;

import client.app.ClientApp;
import client.listeners.BidListener;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class ItemPanel extends JPanel {

    private JLabel price, name, time, plus;
    private JTextArea jta, descLabel;
    private JButton btnbit;
    private ClientApp client;

    ItemPanel(Item i, ClientApp client){
        this.setLayout(new GridBagLayout());
        this.client = client;

        name = new JLabel(i.getName());
        descLabel = new JTextArea(i.getDescription());
        time = new JLabel(i.getTime().toString());


        descLabel.setEditable(false);
        descLabel.setWrapStyleWord(true);
        descLabel.setPreferredSize(new Dimension(400, 70));
        descLabel.setBackground(this.getBackground());
        descLabel.setLineWrap(true);

        GridBagConstraints gbc = new GridBagConstraints();

        if (!i.isSold()) {
            if (i.getLeader() != null) {
                price = new JLabel(String.valueOf(i.getPrice()) + " mornilles. Tenu par : " + i.getLeader());
            } else {
                price = new JLabel(String.valueOf(i.getPrice()) + " mornilles.");
            }

            plus = new JLabel("+");
            jta = new JTextArea(String.valueOf(Math.ceil((i.getPrice() * 0.2))));
            btnbit = new BidButton("Enchérir", i, jta);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 2;
            gbc.insets = new Insets(2, 2, 2, 50);
            this.add(name, gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            this.add(time, gbc);
            gbc.gridx = 2;
            gbc.gridy = 0;
            this.add(price, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            this.add(descLabel, gbc);
            gbc.gridx = 3;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            this.add(plus, gbc);
            gbc.gridx = 4;
            gbc.gridy = 0;
            this.add(jta, gbc);
            gbc.gridx = 3;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            btnbit.addActionListener(new BidListener(client));
            this.add(btnbit, gbc);
        } else {
            price = new JLabel(String.valueOf(i.getPrice()) + " mornilles.");
            if (i.getLeader() == null) {

                JLabel buyer = new JLabel("Aucun acheteur");

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                gbc.insets = new Insets(2, 2, 2, 50);
                this.add(name, gbc);
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                this.add(descLabel, gbc);
                gbc.gridx = 2;
                gbc.gridy = 1;

                this.add(buyer, gbc);
                gbc.gridx = 2;
                gbc.gridy = 2;
                this.add(price, gbc);

            } else {

                JLabel buyer = new JLabel(i.getLeader());

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                gbc.insets = new Insets(2, 2, 2, 50);
                this.add(name, gbc);
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                this.add(descLabel, gbc);
                gbc.gridx = 2;
                gbc.gridy = 1;
                this.add(buyer, gbc);
                gbc.gridx = 2;
                gbc.gridy = 2;
                this.add(price, gbc);
            }

        }
    }

    public void setItemPrice(Item i, double newPrice, String buyer){
        price.setText(String.valueOf(newPrice) + " mornilles. Tenu par : " + buyer);

        jta.setText(String.valueOf(Math.ceil((newPrice * 0.2))));

        name.setForeground(Color.RED);
        price.setForeground(Color.RED);
        time.setForeground(Color.RED);
        descLabel.setForeground(Color.RED);

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            name.setForeground(Color.BLACK);
            time.setForeground(Color.BLACK);
            descLabel.setForeground(Color.BLACK);
            price.setForeground(Color.BLACK);
        }).start();
    }

    public void endItemSale(Item item) {
        this.remove(btnbit);
        this.remove(plus);
        this.remove(jta);
        this.btnbit = null;
        this.plus = null;
        this.jta = null;

        try {
            if(item.getLeader().equals(client.getPseudo())){
                new Thread(() -> JOptionPane.showMessageDialog(client.getView(), "Bravo ! Vous avez gagné : "+item.getName())).start();

                Color wonColor = new Color(95, 197, 96);
                name.setForeground(wonColor);
                price.setForeground(wonColor);
                time.setForeground(wonColor);
                descLabel.setForeground(wonColor);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

}
