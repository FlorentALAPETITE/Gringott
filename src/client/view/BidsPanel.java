package client.view;

import client.app.ClientApp;
import client.listeners.BidListener;
import client.listeners.DisconnectionListener;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class BidsPanel extends JPanel {

	private static final long serialVersionUID = 341558991057008262L;

	private ClientApp client;
	private List<Item> items;

	public BidsPanel(ClientApp client) throws RemoteException {
		super();
		this.client = client;

		items = client.getItems();

		this.setPreferredSize(new Dimension(800, items.size()*150));
		
		for (Item i : items) {
			if (!i.getSeller().equals(client.getPseudo())) {
				JPanel itemPanel = new JPanel();
				itemPanel.setLayout(new GridBagLayout());

				JLabel name = new JLabel(i.getName());
				JTextArea descLabel = new JTextArea(i.getDescription());
				JLabel time = new JLabel(i.getTime().toString());
				

				descLabel.setEditable(false);
				descLabel.setWrapStyleWord(true);
				descLabel.setPreferredSize(new Dimension(400, 70));
				descLabel.setBackground(itemPanel.getBackground());
				descLabel.setLineWrap(true);
				
				GridBagConstraints gbc = new GridBagConstraints();

				if (!i.isSold()) {
					JLabel price;
					if (i.getLeader() != null) {
						price = new JLabel(String.valueOf(i.getPrice()) + " mornilles. Tenu par : " + i.getLeader());
					} else {
						price = new JLabel(String.valueOf(i.getPrice()) + " mornilles.");
					}
					JLabel plus = new JLabel("+");
					JTextArea jta = new JTextArea(String.valueOf(Math.ceil((i.getPrice() * 0.2))));
					BidButton btnbit = new BidButton("Enchérir", i, jta);

					gbc.gridx = 0;
					gbc.gridy = 0;
					gbc.gridheight = 2;
					gbc.insets = new Insets(2, 2, 2, 50);
					itemPanel.add(name, gbc);
					gbc.gridx = 1;
					gbc.gridy = 0;
					gbc.gridheight = 1;
					itemPanel.add(time, gbc);
					gbc.gridx = 2;
					gbc.gridy = 0;
					itemPanel.add(price, gbc);
					gbc.gridx = 1;
					gbc.gridy = 1;
					gbc.gridwidth = 2;
					itemPanel.add(descLabel, gbc);
					gbc.gridx = 3;
					gbc.gridy = 0;
					gbc.gridwidth = 1;
					itemPanel.add(plus, gbc);
					gbc.gridx = 4;
					gbc.gridy = 0;
					itemPanel.add(jta, gbc);
					gbc.gridx = 3;
					gbc.gridy = 1;
					gbc.gridwidth = 2;
					btnbit.addActionListener(new BidListener(client));
					itemPanel.add(btnbit, gbc);
				} else {
					JLabel price = new JLabel(String.valueOf(i.getPrice()) + " mornilles.");
					if (i.getLeader() == null) {
						
						JLabel buyer = new JLabel("Aucun acheteur");
						
						gbc.gridx = 0;
						gbc.gridy = 0;
						gbc.gridheight = 2;
						gbc.insets = new Insets(2, 2, 2, 50);
						itemPanel.add(name, gbc);
						gbc.gridx = 1;
						gbc.gridy = 0;
						gbc.gridheight = 2;
						itemPanel.add(descLabel, gbc);
						gbc.gridx = 2;
						gbc.gridy = 1;
						
						itemPanel.add(buyer, gbc);
						gbc.gridx = 2;
						gbc.gridy = 2;
						itemPanel.add(price, gbc);
						
					} else {
	
						JLabel buyer = new JLabel(i.getLeader());

						gbc.gridx = 0;
						gbc.gridy = 0;
						gbc.gridheight = 2;
						gbc.insets = new Insets(2, 2, 2, 50);
						itemPanel.add(name, gbc);
						gbc.gridx = 1;
						gbc.gridy = 0;
						gbc.gridheight = 2;
						itemPanel.add(descLabel, gbc);
						gbc.gridx = 2;
						gbc.gridy = 1;
						itemPanel.add(buyer, gbc);
						gbc.gridx = 2;
						gbc.gridy = 2;
						itemPanel.add(price, gbc);
					}
					
				}
				this.add(itemPanel);
			}
		}
		JButton logout = new JButton("Deconnexion");
		logout.addActionListener(new DisconnectionListener(client));
		this.add(logout);
		
	}
}
