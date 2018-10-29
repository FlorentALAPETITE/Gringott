package client.view;

import client.app.ClientApp;
import shared.Item;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 6994145468596380654L;
	private ClientApp client;
	private BidsPanel bidsPanel;
	private OwnedPanel ownedPanel;
	private JTabbedPane tabPanel;
	private RegisterPanel registerPanel;
	private SubmitPanel submitPanel;
	
	public ClientFrame(ClientApp client) throws RemoteException {
		super();
		this.client = client;
		registerPanel = new RegisterPanel(client);
		this.bidsPanel = new BidsPanel(client);
		JScrollPane bidsScroll = new JScrollPane(bidsPanel);
		this.ownedPanel = new OwnedPanel(client);
		JScrollPane ownedScroll = new JScrollPane(ownedPanel);
		this.submitPanel = new SubmitPanel(client);
		this.tabPanel = new JTabbedPane();
		this.tabPanel.addTab("Soummettre un article", submitPanel);
		this.tabPanel.addTab("Mes achats", ownedScroll);
		this.tabPanel.addTab("Enchères", bidsScroll);
		this.tabPanel.setSelectedIndex(2);
		
		
		this.setTitle("Gringott - Service d'enchère pour sorciers");
		this.setSize(900,600);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.add(tabPanel);
		this.add(registerPanel);
		this.setContentPane(registerPanel);
	}

	public Container getTabPanel() {
		return this.tabPanel;
	}
	
	public SubmitPanel getSubmitPanel() {
		return this.submitPanel;
	}

	public RegisterPanel getRegisterPanel() {
		return this.registerPanel;
	}

	public void rebuild() throws RemoteException {
		this.tabPanel.remove(1);
		this.tabPanel.remove(1);
		this.bidsPanel = new BidsPanel(this.client);
		JScrollPane bidsScroll = new JScrollPane(bidsPanel);
		this.ownedPanel = new OwnedPanel(this.client);
		JScrollPane ownedScroll = new JScrollPane(ownedPanel);
		this.tabPanel.add("Mes achats", ownedScroll);
		this.tabPanel.add("Enchères", bidsScroll);
		this.tabPanel.setSelectedIndex(2);
	}

	public void addNewItemInBidsPanel(Item item){
		try {
			bidsPanel.addNewItem(item);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		bidsPanel.revalidate();
		bidsPanel.repaint();
	}

	public void updateItemPrice(Item item, double newPrice, String buyer){
		bidsPanel.updateBid(item,newPrice, buyer);
		bidsPanel.revalidate();
		bidsPanel.repaint();
	}

	@Override
	public void dispose() {
		try {
			if (this.client.isConnected()) {
				this.client.getServer().logout(client.getId());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		super.dispose();
		System.exit(NORMAL);
	}
		
}
