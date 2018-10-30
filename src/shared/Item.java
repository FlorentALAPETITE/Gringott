package shared;

import java.io.Serializable;
import java.util.Date;

public interface Item extends Serializable {

	/**
	 * Get the name of the Item.
	 * @return the name
	 */
	String getName();
	
	/**
	 * Get the ID of the Item
	 * @return the id
	 */
	int getId();
	
	/**
	 * Set ID of the Item (called by the DBManager)
	 */
	void setId(int id);
	
	/**
	 * Get the description of the Item.
	 * @return the description
	 */
	String getDescription();
	
	/**
	 * Get the leader of the bid for the Item.
	 * @return the leader
	 */
	String getLeader();
	
	/**
	 * Get the current price of the Item.
	 * @return the price
	 */
	double getPrice();
	
	/**
	 * Get the status of the Item (available or sold).
	 * @return false is available; true if sold.
	 */
	boolean isSold();
	
	/**
	 * Set the leader of the bid for the Item.
	 * @params leader the leader
	 */
	void setLeader(String leader);
	
	/**
	 * Set the new price of the Item.
	 * @return the price
	 */
	void setPrice(double price);
	
	/**
	 * Set the status of the Item (available or sold).
	 * @return false is available; true if sold.
	 */
	void setSold(boolean status);

	/**
	 * Set the end  date of the Item.
	 * @return the date
	 */
	Date getTime();

	/**
	 * Set the seller of the Item.
	 * @return the seller's pseudo
	 */
	String getSeller();
		
}
