package shared;

import server.DBManager;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote, Serializable {

	/**
	 * Register a freshly connected client.
	 * @param client the new client
	 * @throws RemoteException
	 */
	void registerClient(IClient client) throws RemoteException;

	/**
	 * Log out a client.
	 * @param clientId the client to log out.
	 * @throws RemoteException
	 */
	void logout(UUID clientId) throws RemoteException;
	
	/**
	 * Record a new bid from a a client for an item.
	 * @param itemId the item.
	 * @param newPrice the bid amount.
	 * @param bidderId the client.
	 * @throws RemoteException
	 */
	void bid(UUID itemId, double newPrice, UUID bidderId)  throws RemoteException;
	
	/**
	 * Record a new Item.
	 * @param item the item
	 * @throws RemoteException
	 */
	void submit(Item item)  throws RemoteException;
	
	/**
	 * List server's items
	 * @return the items.
	 * @throws RemoteException
	 */
	HashMap<UUID,Item> getItems()  throws RemoteException;
	
	/**
	 * List server's clients
	 * @return the clients.
	 * @throws RemoteException
	 */
	HashMap<UUID, IClient> getClients() throws RemoteException;
	
	/**
	 * Get the server's db
	 * @return the db.
	 * @throws RemoteException
	 */
	DBManager getDB() throws RemoteException;
	
}
