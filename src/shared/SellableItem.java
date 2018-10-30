package shared;

import java.util.Date;

public class SellableItem implements Item {

	private static final long serialVersionUID = -4517882019233732317L;
	private int id;
	private String name;
	private String description;
	private String seller;
	private String leader;
	private double price;
	private Date time;
	private boolean sold;
	
	public SellableItem(int id, String name, String description, double price, String seller, long time2leave) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.seller = seller;
		this.leader = null;
		this.time = new Date(System.currentTimeMillis() + time2leave*60000);
		this.sold = false;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getLeader() {
		return this.leader;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	@Override
	public boolean isSold() {
		return this.sold;
	}

	@Override
	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public void setSold(boolean status) {
		this.sold = status;
	}

	@Override
	public Date getTime() {
		return this.time;
	}

	@Override
	public String getSeller() {
		return this.seller;
	}
}
