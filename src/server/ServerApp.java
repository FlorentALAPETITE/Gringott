package server;

import java.io.FileNotFoundException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import shared.IClient;
import shared.IServer;
import shared.Item;

public class ServerApp extends UnicastRemoteObject implements IServer {

	private static final long serialVersionUID = -8168686161180269490L;
	private static int CLIENT_ID = 0;

	private static BidMonitor monitor = new BidMonitor();

	private DBManager dbManager;
	private HashMap<Integer, IClient> clients;
	private List<Item> items;

	public ServerApp(int port) throws RemoteException, FileNotFoundException {
		super(port);
		this.dbManager = new DBManager(true, true);
		this.clients = new HashMap<Integer, IClient>();
		this.items = this.dbManager.listItems();
	}

	@Override
	public int registerClient(IClient client) throws RemoteException {
		this.clients.put(CLIENT_ID,client);
		System.out.println("New client registered : " + client.getPseudo().split("@")[0]+"@"+CLIENT_ID); //Oui, c'est très laid, mais sinon l'id n'est pas encore initialisé pour le client, l'idéal serait d'afficher le message après
		for (Item i : items) {
			client.addNewItem(i);
		}
		return CLIENT_ID++;
	}
	
	@Override
	public void logout(IClient client) throws RemoteException {
		System.out.println(client.getPseudo() + " logged out.");
		clients.remove(client.getId());
		System.out.println(clients.size() > 0 ? clients.size()+" clients still connected." : "No more clients connected.");
	}

	@Override
	public void bid(Item item, double newPrice, String buyer) throws RemoteException {

		double price = monitor.updateBid(item, newPrice, buyer, items, dbManager);
		
		for (IClient c : clients.values()) {
			c.update(item, price, buyer);
		}
	}

	@Override
	public void submit(Item item) throws RemoteException {
		System.out.println("New item registered : " + item);
		this.items.add(item);
		dbManager.addItem(item);
		for (IClient c : clients.values()) {
			c.addNewItem(item);
		}
	}
	
	@Override
	public List<Item> getItems() {
		return this.items;
	}
	
	@Override
	public HashMap<Integer, IClient> getClients() throws RemoteException {
		return this.clients;
	}
	
	@Override
	public DBManager getDB() {
		return this.dbManager;
	}
	
	public static void main(String[] args) {
		try {

			String localIp = null;

			try(final DatagramSocket socket = new DatagramSocket()){
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				localIp = socket.getLocalAddress().getHostAddress();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			System.setProperty("java.rmi.server.hostname","192.168.43.95");
			System.setProperty("java.security.policy","file:./server.policy");

			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}


			int port = 8090;
			Registry reg = LocateRegistry.createRegistry(port);
			IServer s = new ServerApp(port);

			reg.bind("enchere", s);

			System.out.println("Adresse : localhost:" + port + "/enchere");

			while (true) {
				for (Item i : s.getItems()) {
					Date localDate = new Date(System.currentTimeMillis());
					if (i.getTime().compareTo(localDate) < 0 && !i.isSold()) {
						for (IClient c : s.getClients().values()) {
							i.setSold(true);
							s.getDB().updateItem(i);
							c.endSelling(i);
						}
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
