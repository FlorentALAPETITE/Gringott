package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClient extends Remote {

	/**
	 * Returns the client ID
	 * @return integer id
	 */
	int getId() throws RemoteException;
	
	/**
	 * Returns the connection status of the client
	 * @return boolean isConnected
	 */
	boolean isConnected() throws RemoteException;
	
	/**
	 * Add a new sellable item.
	 * @param item the item to be sold.
	 */
	void addNewItem(Item item) throws RemoteException;
	
	/**
	 * Update an item (after a bid from another buyer).
	 * @param item the item to be added.
	 * @param newPrice the new price.
	 * @param buyer the new leader.
	 */
	void update(Item item, double newPrice, String buyer)  throws RemoteException;
	
	/**
	 * Notify client that an item clock have reached its end.
	 * @param item the item from which selling has to be ended.
	 */
	void endSelling(Item item) throws RemoteException;

	/**
	 * Get client's pseudo.
	 */
	String getPseudo() throws RemoteException;

	/**
	 * Get client's items.
	 */
	List<Item> getItems() throws RemoteException;

	/**
	 * Get client's server.
	 */
	IServer getServer() throws RemoteException;

	/**
	 * Set client's pseudo.
	 * @param pseudo the new pseudo
	 */
	void setPseudo(String pseudo) throws RemoteException;
	
}
