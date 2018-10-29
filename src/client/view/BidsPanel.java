package client.view;

import shared.IClient;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidsPanel extends JPanel {

	private static final long serialVersionUID = 341558991057008262L;

	private IClient client;
	private ActionListener controller;
	private Map<String, Item> items;
	private Map<String, ItemPanel> itemPanels;

	public BidsPanel(IClient client, ActionListener controller) throws RemoteException {
		super();
		this.client = client;
		this.controller = controller;

		items = new HashMap<String, Item>();
		itemPanels = new HashMap<String, ItemPanel>();
		
		for (Item i : client.getItems()) {
			items.put(i.getName(), i); // TODO replace by ID
			ItemPanel itemPanel = new ItemPanel(i, controller);
			this.add(itemPanel);
			itemPanels.put(i.getName(), itemPanel);
		}

		this.setPreferredSize(new Dimension(800, items.size()*150));

		JButton logout = new JButton("Deconnexion");
		logout.addActionListener(this.controller);
		this.add(logout);
		
	}

	public void addNewItem(Item i) throws RemoteException {
		if (!i.getSeller().equals(client.getPseudo())) {
			ItemPanel itemPanel = new ItemPanel(i, controller);
			this.add(itemPanel);
			itemPanels.put(i.getName(), itemPanel);
			items.put(i.getName(),i);
		}
	}

	public void updateBid(Item i, double newPrice, String buyer) {
		try {
			if(!(i.getSeller().equals(client.getPseudo())))
                itemPanels.get(i.getName()).setItemPrice(i, newPrice, buyer);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
