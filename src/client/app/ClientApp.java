package client.app;

import client.view.BidButton;
import client.view.ClientFrame;
import shared.IClient;
import shared.IServer;
import shared.Item;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClientApp extends UnicastRemoteObject implements IClient {

	private static final long serialVersionUID = 1373624286313090112L;
	private int id;
	private boolean isConnected = false;
	private ClientFrame view;
	private String pseudo;
	private List<Item> items;
	private IServer server;

	public ClientApp(String url) throws MalformedURLException, RemoteException, NotBoundException {
		this.items = new ArrayList<Item>();
		this.view = new ClientFrame(this);
		this.view.setVisible(true);
		this.server = (IServer) Naming.lookup("//localhost:8090/enchere");
	}

    //region client

	@Override
	public void update(Item item, double newPrice, String buyer) throws RemoteException {
		for (Item i : items){
			if (i.getName().equals(item.getName()) && !i.isSold()){
				System.out.println("Mise à jour de l'item : " + i.getName());
				i.setPrice(newPrice);
				i.setLeader(buyer);
				this.updateView();
			}
		}
	}

	@Override
	public void endSelling(Item item) {
		for (Item i : items){
			if (i.getName().equals(item.getName())){
				System.out.println("Fin de la vente : " + i.getName());
				i.setSold(true);
				this.endSelling(item);
			}
		}
	}

	//endregion


    //region display

    public void updateView() throws RemoteException {
        this.view.rebuild();
        this.view.repaint();
        this.view.revalidate();
    }

    public void connect(){
        String pseudoEntry = view.getRegisterPanel().getFieldContent();
        if(pseudoEntry.trim().equalsIgnoreCase("") || pseudoEntry.contains("@")){
            new JOptionPane().showMessageDialog(null, "Pseudo Invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
        }else {
            try {
                pseudo = view.getRegisterPanel().getFieldContent();
                id = server.registerClient(ClientApp.this);
                isConnected = true;
                view.setContentPane(view.getTabPanel());
                updateView();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void submit(){
        try{
            Item item = view.getSubmitPanel().getFieldsContent();
            server.submit(item);
            view.getSubmitPanel().clear();
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Merci de mettre des nombres.", "Information", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void bid(BidButton source) {
        try {
            if (Double.parseDouble(source.getContent()) >= source.getItem().getPrice()*0.2) {
                server.bid(source.getItem(), Math.ceil(Double.parseDouble(source.getContent())), id);
            } else {
                JOptionPane.showMessageDialog(null, "Vous devez enchérir d'au moins 20% du prix courant.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Merci de mettre un nombre.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void disconnect(){
        view.setContentPane(view.getRegisterPanel());
        try {
            server.logout(id);
            pseudo = null;
            isConnected = false;
            updateView();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    //endregion

    //region getter / setters

    @Override
    public int getId() throws RemoteException {
        return this.id;
    }

    @Override
    public String getPseudo() throws RemoteException {
        return this.pseudo+"@"+this.id;
    }

    @Override
    public boolean isConnected() throws RemoteException {
        return isConnected;
    }

    @Override
    public void addNewItem(Item item) throws RemoteException {
        boolean contains = false;
        for (Item i : items){
            if (i.getName().equals(item.getName())){
                contains = true;
            }
        }
        if (!contains){
            System.out.println("Nouvel item ajouté : " + item.getName());
            this.items.add(item);
        }
        this.updateView();
    }

	@Override
	public IServer getServer() {
		return this.server;
	}

	@Override
	public List<Item> getItems() throws RemoteException {
		return this.items;
	}

	@Override
	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

    //endregion

	public static void main(String[] args) {
		try {
			String serverURL = "localhost:8090/enchere";
			ClientApp c = new ClientApp(serverURL);
			System.out.println("Connexion au serveur " + serverURL + " reussi.");
		} catch (RemoteException e) {
			System.out.println("Connexion au serveur impossible.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Erreur dans l'adresse du serveur.");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Serveur inconnu.");
			e.printStackTrace();
		}
	}
	
}
