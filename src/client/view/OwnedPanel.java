package client.view;

import shared.IClient;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

//import static javafx.scene.input.KeyCode.J;

class OwnedPanel extends JPanel {

	private IClient client;
	private int height = 600;

	OwnedPanel(IClient client) {
		super();

		this.client = client;
		this.setPreferredSize(new Dimension(800,height));

		try {
			for(Item item : client.getItems().values()){
                if(item.getLeader()!=null && item.getLeader().equals(client.getPseudo()) && item.isSold()){
                	appendNewOwnedItem(item);
				}
            }
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void appendNewOwnedItem(Item i){
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new GridBagLayout());

		JLabel name = new JLabel(i.getName());
		JTextArea descLabel = new JTextArea(i.getDescription());
		JLabel price = new JLabel(String.valueOf(i.getPrice()) + " mornilles");

		descLabel.setEditable(false);
		descLabel.setWrapStyleWord(true);
		descLabel.setPreferredSize(new Dimension(400, 70));
		descLabel.setBackground(itemPanel.getBackground());
		descLabel.setLineWrap(true);

		GridBagConstraints gbc = new GridBagConstraints();

		try {
			if (i.isSold() && client.getPseudo() !=null && client.getPseudo().equals(i.getLeader())) {

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
            }
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.add(itemPanel);
		height+=50;
		this.setPreferredSize(new Dimension(800,height));
	}

}
