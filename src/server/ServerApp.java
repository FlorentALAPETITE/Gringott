package server;

import shared.IClient;
import shared.IServer;
import shared.Item;
import shared.SellableItem;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class ServerApp extends UnicastRemoteObject implements IServer {

	private static final long serialVersionUID = -8168686161180269490L;
	private static int CLIENT_ID = 0;

	private static BidMonitor monitor = new BidMonitor();

	private DBManager dbManager;
	private HashMap<Integer, IClient> clients;

	public ServerApp() throws RemoteException, FileNotFoundException {
		this.dbManager = new DBManager(this);
		this.clients = new HashMap<Integer, IClient>();
	}

	@Override
	public int registerClient(IClient client) throws RemoteException {
		this.clients.put(CLIENT_ID,client);
		System.out.println("New client registered : " + client.getPseudo().split("@")[0]+"@"+CLIENT_ID); //Oui, c'est très laid, mais sinon l'id n'est pas encore initialisé pour le client, l'idéal serait d'afficher le message après
		for (Item i : this.dbManager.listItems()) {
			client.addNewItem(i);
		}
		return CLIENT_ID++;
	}
	
	@Override
	public void logout(int clientId) throws RemoteException {
		if(clients.containsKey(clientId)) {
			System.out.println(clients.get(clientId).getPseudo() + " logged out.");
			clients.remove(clientId);
			System.out.println(clients.size() > 0 ? clients.size()+" clients still connected." : "No more clients connected.");
		}else {
			System.out.println("There is no such client with ID : "+clientId);
		}
	}

	@Override
	public void bid(Item item, double newPrice, int bidderId) throws RemoteException {

		double price = monitor.updateBid(item, newPrice, clients.get(bidderId).getPseudo(), this.dbManager.listItems(), dbManager);
		
		for (IClient c : clients.values()) {
			c.update(item, price, clients.get(bidderId).getPseudo());
		}
	}

	@Override
	public void submit(Item item) throws RemoteException {
		System.out.println("New item registered : " + item);
		dbManager.addItem(item);
		for (IClient c : clients.values()) {
			c.addNewItem(item);
		}
		Thread est = new EndSellingThread(item, this);
		est.start();
	}
	
	@Override
	public List<Item> getItems() {
		return this.dbManager.listItems();
	}
	
	@Override
	public HashMap<Integer, IClient> getClients() throws RemoteException {
		return this.clients;
	}
	
	@Override
	public DBManager getDB() {
		return this.dbManager;
	}

	public void endSale(Item item) {
        item.setSold(true);
        this.getDB().updateItem(item);;
        for (IClient c : clients.values()) {
            try {
                c.endSelling(item);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void main(String[] args) {
		try {

			int port = 8090;
			LocateRegistry.createRegistry(port);
			IServer s = new ServerApp();
			Naming.bind("//localhost:" + port + "/enchere", s);

			System.out.println("Adresse : localhost:" + port + "/enchere");

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
