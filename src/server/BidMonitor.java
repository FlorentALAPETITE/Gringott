package server;

import log.ServerLogSystem;
import shared.Item;

import java.util.List;

public class BidMonitor {

    public synchronized double updateBid(Item item, double newPrice, String buyer, List<Item> items, DBManager dbManager, ServerLogSystem logSystem){
        double price = item.getPrice() + newPrice;
        logSystem.writeLog("New bid from " + buyer + " recorded for " + item.getName() + " at " + price);

        for (Item i : items) {
            if (i.getName().equals(item.getName())){
                i.setPrice(price);
                i.setLeader(buyer);
                dbManager.updateItem(i);
            }
        }
        return price;
    }
}
