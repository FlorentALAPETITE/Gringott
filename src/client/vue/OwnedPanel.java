package client.vue;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.app.IClient;
import client.app.Item;

public class OwnedPanel extends JPanel {

	private ActionListener controller;
	private IClient client;
	private List<Item> items;

	public OwnedPanel(IClient client, ActionListener controller) throws RemoteException {
		super();
		this.client = client;
		this.controller = controller;
		
		items = client.getItems();
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (Item i : items) {
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

			if (i.isSold() && i.getLeader().getPseudo().equals(client.getPseudo())) {
				
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridheight = 2;
				itemPanel.add(name, gbc);
				gbc.gridx = 2;
				gbc.gridy = 0;
				itemPanel.add(price, gbc);
				gbc.gridx = 1;
				gbc.gridy = 1;
				gbc.gridwidth = 2;
				itemPanel.add(descLabel, gbc);
			} 
			this.add(itemPanel);
		}
		
	}

}