package server;

import shared.Item;

import java.util.Date;

public class EndSellingThread extends Thread {

    private Item item;
    private ServerApp server;


    EndSellingThread(Item i, ServerApp server){
        this.item = i;
        this.server = server;
    }

    @Override
    public void run() {
        // compute sleep time here instead of constructor to be more precise
        Date localDate;
        localDate = new Date(System.currentTimeMillis());
        long sleepDuration = item.getTime().getTime() - localDate.getTime();
        while (sleepDuration > 0){
            // in case of false awake
            try {
                this.sleep(sleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            localDate = new Date(System.currentTimeMillis());
            sleepDuration = item.getTime().getTime() - localDate.getTime();
        }

        // selling time is over
        server.endSale(item);
    }
}
