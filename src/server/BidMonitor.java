package server;

import log.ServerLogSystem;
import shared.Item;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BidMonitor {

    public synchronized double updateBid(UUID itemId, double newPrice, String buyer, HashMap<UUID,Item> items, DBManager dbManager, ServerLogSystem logSystem){
        double price = items.get(itemId).getPrice() + newPrice;
        logSystem.writeLog("New bid from " + buyer + " recorded for " + items.get(itemId).getName() + " at " + price);
        
        items.get(itemId).setPrice(price);
        items.get(itemId).setLeader(buyer);
        dbManager.updateItem(items.get(itemId));
        
        return price;
    }
}
