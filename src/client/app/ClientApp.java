package client.app;

import client.view.ClientFrame;
import shared.IClient;
import shared.IServer;
import shared.Item;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClientApp extends UnicastRemoteObject implements IClient {

	private static final long serialVersionUID = 1373624286313090112L;
	private UUID id;
	private boolean isConnected = false;
	private ClientFrame view;
	private String pseudo;
	private HashMap<UUID,Item> items;
	private IServer server;

	public ClientApp(String url) throws MalformedURLException, RemoteException, NotBoundException {
		this.items = new HashMap<UUID,Item>();
		this.view = new ClientFrame(this);
		this.view.setVisible(true);
		try{
            this.server = (IServer) Naming.lookup(url);
        } catch (RemoteException e){
            JOptionPane.showMessageDialog(null, "Le serveur n'est pas accessible", "Erreur Serveur", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
		}

	}

    //region client

	@Override
	public void update(Item item, double newPrice, String buyer) throws RemoteException {
		if(!items.get(item.getId()).isSold()) {
			System.out.println("Mise à jour de l'item : " + items.get(item.getId()).getName());
			items.get(item.getId()).setPrice(newPrice);
			items.get(item.getId()).setLeader(buyer);
			view.updateItemPrice(item,newPrice, buyer);
		}
	}

	@Override
	public void endSelling(Item item) throws RemoteException{
		System.out.println("Fin de la vente : " + items.get(item.getId()).getName());
		items.get(item.getId()).setSold(true);
        view.endItemSale(item);
		
	}

    public void connect(String validPseudo){
        try {
            pseudo = validPseudo;
            server.registerClient(this);
            isConnected = true;
            view.openTabPanel();
            
            //Display
            this.view.rebuild();
            this.view.repaint();
            this.view.revalidate();
            
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void submit(Item item){
	    try {
            server.submit(item);
            view.addNewItemToSale(item);
        } catch (RemoteException e){
	        e.printStackTrace();
        }
    }

    public void bid(Item item, Double price) {
	    try {
            server.bid(item.getId(), price, id);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            server.logout(id);
            pseudo = null;
            isConnected = false;
            view.openRegisterPanel();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    //endregion

    //region getter / setters

    @Override
    public UUID getId() throws RemoteException {
        return this.id;
    }

    @Override
    public String getPseudo() throws RemoteException {
        return this.pseudo+"@"+this.id.toString();
    }

    @Override
    public boolean isConnected() throws RemoteException {
        return isConnected;
    }

    @Override
    public void addItemsFromServer(HashMap<UUID, Item> items){
	    this.items = new HashMap<UUID, Item>(items);
    }

    @Override
    public void addNewItem(Item item) throws RemoteException {
        if(!items.containsKey(item.getId())) {
        	System.out.println("Nouvel item ajouté : " + item.getName());
            this.items.put(item.getId(),item);
            view.addNewItemInBidsPanel(item);
        }
    }

    public ClientFrame getView(){
	    return view;
    }

	@Override
	public IServer getServer() {
		return this.server;
	}

	@Override
	public HashMap<UUID,Item> getItems() throws RemoteException {
		return this.items;
	}

	@Override
	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
    public void setID(UUID clientID) throws RemoteException{
	    this.id = clientID;
    }

    //endregion

	public static void main(String[] args) {
		try {
			String serverURL = "//localhost:8090/enchere";
			ClientApp c = new ClientApp(serverURL);
			System.out.println("Connexion au serveur " + serverURL + " reussi.");
		} catch (RemoteException e) {
			System.out.println("Connexion au serveur impossible.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Erreur dans l'adresse du serveur.");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Serveur inconnu.");
			e.printStackTrace();
		}
	}

}
