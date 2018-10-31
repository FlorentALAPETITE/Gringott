package client.view;

import shared.IClient;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;

public class SellsPanel extends JPanel {

    private IClient client;
    private HashMap<String, JLabel> items;

    private int height = 600;

    SellsPanel(IClient client) {
        super();

        this.client = client;
        this.items = new HashMap<>();
        this.setPreferredSize(new Dimension(800,height));

        try {
            for(Item item : client.getItems().values()){
                if(item.getSeller().equals(client.getPseudo())){
                    appendNewSoldItem(item);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void appendNewSoldItem(Item i){
        System.out.println("Je met en vente : " + i.getName());
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridBagLayout());

        JLabel name = new JLabel(i.getName());
        JTextArea descLabel = new JTextArea(i.getDescription());
        JLabel price = new JLabel(String.valueOf("En vente au prix inital de " + i.getPrice()) + " mornilles");

        descLabel.setEditable(false);
        descLabel.setWrapStyleWord(true);
        descLabel.setPreferredSize(new Dimension(400, 70));
        descLabel.setBackground(itemPanel.getBackground());
        descLabel.setLineWrap(true);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 50);
        itemPanel.add(name, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        itemPanel.add(price, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        itemPanel.add(descLabel, gbc);

        items.put(i.getName(), price);
        this.add(itemPanel);
        height+=50;
        this.setPreferredSize(new Dimension(800,height));
    }

    public void updateSoldItemWhenSold(Item i){
        System.out.println("J'ai vendu : " + i.getName());
        JLabel itemlabel = items.get(i.getName());
        String label ;
        if (i.getLeader().equals("")){
            label = "Votre objet n'a pas été vendu";
        } else {
            label = "Vendu pour " + i.getPrice() + "mornilles à " + i.getLeader();
        }
        itemlabel.setText(label);
    }

}
