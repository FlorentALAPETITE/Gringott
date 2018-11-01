package client.view;

import client.app.ClientApp;
import client.listeners.DisconnectionListener;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BidsPanel extends JPanel {

	private static final long serialVersionUID = 341558991057008262L;

    private ClientApp client;
	private Map<UUID, Item> items;
	private Map<UUID, ItemPanel> itemPanels;

	BidsPanel(ClientApp client) throws RemoteException {
		super();
		this.client = client;

		items = new HashMap<>();
		itemPanels = new HashMap<>();
		
		for (Item i : client.getItems().values()) {
			if(!i.getSeller().equals(client.getPseudo())) {
				items.put(i.getId(), i);
				ItemPanel itemPanel = new ItemPanel(i, client);
				this.add(itemPanel);
				itemPanels.put(i.getId(), itemPanel);
			}
		}
		this.setPreferredSize(new Dimension(800, items.size()*150));
	}

	public void addNewItem(Item i) throws RemoteException {
		if (!i.getSeller().equals(client.getPseudo())) {
			ItemPanel itemPanel = new ItemPanel(i, client);
			this.add(itemPanel);
			itemPanels.put(i.getId(), itemPanel);
			items.put(i.getId(),i);
		}
	}

	public void updateBid(Item i, double newPrice, String buyer) {
		try {
			if(!(i.getSeller().equals(client.getPseudo())))
                itemPanels.get(i.getId()).setItemPrice(i, newPrice, buyer);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

    public void endItemSale(Item item) {
		if(itemPanels.containsKey(item.getId()))
        	itemPanels.get(item.getId()).endItemSale(item);
    }
}
