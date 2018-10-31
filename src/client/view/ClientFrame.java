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
	private SellsPanel sellsPanel;
	
	public ClientFrame(ClientApp client) throws RemoteException {
		super();
		this.client = client;
		registerPanel = new RegisterPanel(client);
		this.tabPanel = new JTabbedPane();

        this.bidsPanel = new BidsPanel(this.client);
		
		this.setTitle("Gringott - Service d'enchère pour sorciers");
		this.setSize(900,600);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.add(tabPanel);
		this.add(registerPanel);
		this.setContentPane(registerPanel);
	}

	public void rebuild() throws RemoteException {
	    this.tabPanel.removeAll();
        this.bidsPanel = new BidsPanel(this.client);
		JScrollPane bidsScroll = new JScrollPane(bidsPanel);
		this.ownedPanel = new OwnedPanel(this.client);
		JScrollPane ownedScroll = new JScrollPane(ownedPanel);
		this.submitPanel = new SubmitPanel(this.client);
		this.sellsPanel = new SellsPanel(client);
		JScrollPane sellsScroll = new JScrollPane(sellsPanel);

        this.tabPanel.add("Enchères", bidsScroll);
		this.tabPanel.add("Mes achats", ownedScroll);
        this.tabPanel.add("Mes ventes", sellsScroll);
        this.tabPanel.addTab("Soummettre un article", submitPanel);
        this.tabPanel.addTab("Me déconnecter", new DisconnectionPanel(client));
		this.tabPanel.setSelectedIndex(0);
	}

    public void openTabPanel() {
        this.setContentPane(this.tabPanel);
    }

    public void addNewItemToSale(Item item) {
        this.submitPanel.clear();
        this.tabPanel.setSelectedIndex(0);
        sellsPanel.appendNewSoldItem(item);
        sellsPanel.revalidate();
        sellsPanel.repaint();
    }

    public void openRegisterPanel() {
        this.setContentPane(this.registerPanel);
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

	public void endItemSale(Item item) {
		bidsPanel.endItemSale(item);
		bidsPanel.revalidate();
		bidsPanel.repaint();

		try {
			if(item.getLeader().equals(client.getPseudo())){
				ownedPanel.appendNewOwnedItem(item);
				ownedPanel.revalidate();
				ownedPanel.repaint();
			} else if(item.getSeller().equals(client.getPseudo())){
                sellsPanel.updateSoldItemWhenSold(item);
                sellsPanel.revalidate();
                sellsPanel.repaint();
            }
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
