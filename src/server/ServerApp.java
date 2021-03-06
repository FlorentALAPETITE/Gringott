package server;

import log.ServerLogSystem;
import shared.IClient;
import shared.IServer;
import shared.Item;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.UUID;

public class ServerApp extends UnicastRemoteObject implements IServer {

	private static final long serialVersionUID = -8168686161180269490L;

	private static BidMonitor monitor = new BidMonitor();

	private DBManager dbManager;
	private HashMap<UUID, IClient> clients;
	private HashMap<UUID,Item> items;
	private ServerLogSystem logSystem;

	ServerApp() throws RemoteException, FileNotFoundException {
		logSystem = new ServerLogSystem();
		this.dbManager = new DBManager();
		this.items = this.dbManager.listItems();
		launchEndSellingThreads();
		this.clients = new HashMap<UUID, IClient>();
	}

	@Override
	public void registerClient(IClient client) throws RemoteException {
		UUID clientID = UUID.randomUUID();
		client.setID(clientID);
		this.clients.put(clientID,client);
		logSystem.writeLog("New client registered : " + client.getPseudo());
		client.addItemsFromServer(items);
	}
	
	@Override
	public void logout(UUID clientId) throws RemoteException {
		if(clients.containsKey(clientId)) {
			logSystem.writeLog(clients.get(clientId).getPseudo() + " logged out.");
			clients.remove(clientId);
			logSystem.writeLog(clients.size() > 0 ? clients.size()+" clients still connected." : "No more clients connected.");
		}else {
			logSystem.writeLog("There is no such client with ID : "+clientId);
		}
	}

	@Override
	public void bid(UUID itemId, double newPrice, UUID bidderId) throws RemoteException {
		double price = monitor.updateBid(itemId, newPrice, clients.get(bidderId).getPseudo(), items, dbManager, logSystem);
		
		for (IClient c : clients.values()) {
			c.update(items.get(itemId), price, clients.get(bidderId).getPseudo());
		}
	}

	@Override
	public void submit(Item item) throws RemoteException {
		Item storedItem = dbManager.addItem(item);
		this.items.put(storedItem.getId(),storedItem);
		logSystem.writeLog("New item registered : " + storedItem);

		for (IClient c : clients.values()) {
			c.addNewItem(storedItem);
		}
		Thread est = new EndSellingThread(storedItem, this);
		est.start();
	}
	
	@Override
	public HashMap<UUID,Item> getItems() {
		return this.dbManager.listItems();
	}
	
	@Override
	public HashMap<UUID, IClient> getClients() throws RemoteException {
		return this.clients;
	}
	
	@Override
	public DBManager getDB() {
		return this.dbManager;
	}

	public void endSale(Item item) {
        item.setSold(true);
        this.getDB().updateItem(item);
        for (IClient c : clients.values()) {
            try {
                c.endSelling(item);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchEndSellingThreads(){
	    for (Item i : items.values()){
	    	if(!i.isSold())
            	new EndSellingThread(i, this).start();
        }
    }
	
	public static void main(String[] args) {
		try {

			int port = 8090;
			LocateRegistry.createRegistry(port);
			IServer s = new ServerApp();
			Naming.bind("//localhost:" + port + "/enchere", s);

			System.out.println("Address : localhost:" + port + "/enchere");

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
