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
import shared.SellableItem;

public class ServerApp extends UnicastRemoteObject implements IServer {

	private static final long serialVersionUID = -8168686161180269490L;
	private static int CLIENT_ID = 0;

	private static BidMonitor monitor = new BidMonitor();

	private DBManager dbManager;
	private HashMap<Integer, IClient> clients;
	private List<Item> items;

	public ServerApp(int port) throws RemoteException, FileNotFoundException {
		super(port);
		this.dbManager = new DBManager(true);
		this.clients = new HashMap<Integer, IClient>();
		this.items = this.dbManager.listItems();
		this.initDB();
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

		double price = monitor.updateBid(item, newPrice, clients.get(bidderId).getPseudo(), items, dbManager);
		
		for (IClient c : clients.values()) {
			c.update(item, price, clients.get(bidderId).getPseudo());
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
		Thread est = new EndSellingThread(item, this);
		est.start();
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

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void initDB() {
        Item obj1 = new SellableItem("Botruc", "Petite créature d'une vingtaine de centimètres ayant un aspect végétal et deux longs doigts pointus à chaque main. - Peut crocheter des serrures -", 400, "nDragonneau", 5);
        Item obj2 = new SellableItem("Cerbère nain", "Chien géant à trois tête servant de gardien - Cet exemplaire est de petite taille -", 250, "nDragonneau", 4);
        Item obj3 = new SellableItem("Demiguise", "Créature pouvant se rendre invisible lorsqu'elle est menacée. - Ses poils servent à tisser des toiles d'invisibilité -" , 900, "nDragonneau", 3);
        Item obj4 = new SellableItem("Démonzémerveille", "Créature apparaissant sous forme de boule et se transformant, quand on la lance, en oiseau de proie bleu et vert. - A un attrait particulier pour le cerveau humain -", 1000, "nDragonneau", 2);
        Item obj5 = new SellableItem("Éruptif", "Sorte de Rhinocéros géant vivant en Afrique. Le fluide contenu dans sa corne peut être injecté dans tout type de materiau, provoquant l'explosion de celui-ci. - Sa peau épaisse le rend insensible à la plupart des sorts -", 600, "nDragonneau", 2);
        Item obj6 = new SellableItem("Plume d'Hippogriffe", "L'hippogriffe est une créature volante mi-aigle, mi- cheval. Il est très dangereux tant qu'il n'est pas dressé. - Cette plume a été récoltée dans les alentours de Poudlard et mesure 50 cm -", 150, "nDragonneau", 3);
        Item obj7 = new SellableItem("Niffleur", "Animal à la fourrure noire et au long museau semblable à un ornithorynque. Ils sont attirés par tout ce qui brille. - Formidable voleur -", 250, "nDragonneau", 4);
        Item obj8 = new SellableItem("OEuf d'Occamy", "Les Occamy sont une sorte d'oiseau-serpent. Ils ont la particularité d'être choranaptyxique : leur taille varient en fonction de l'espace dont ils disposent. - La coquille des oeufs d'Occamy est en argent pur -", 700, "nDragonneau", 6);
        Item obj9 = new SellableItem("Oiseau-Tonnerre", "Vivant en Arizona, ces oiseau provoquent des tempêtes lorsqu'ils se sentent menacés. - Leur plume peuvent être utilisées pour fabriquer des baguettes magiques", 1250, "nDragonneau", 5);
        Item obj10 = new SellableItem("OEuf congelé de Serpencendre", "Les serpencendres naissent dans des feux magiques laissés sans surveillance. Ils se cachent dans des recoins de la maison pour y pondre leurs oeufs qui, s'ils réussissent à grandir sans être repérés et chassés, enflamment la maison." , 2000, "nDragonneau", 1);

        try {
            this.submit(obj1);
            this.submit(obj2);
            this.submit(obj3);
            this.submit(obj4);
            this.submit(obj5);
            this.submit(obj6);
            this.submit(obj7);
            this.submit(obj8);
            this.submit(obj9);
            this.submit(obj10);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
