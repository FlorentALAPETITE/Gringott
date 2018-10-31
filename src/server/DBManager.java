package server;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.security.auth.SolarisNumericUserPrincipal;

import shared.Item;
import shared.SellableItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBManager {

	private static final String dbPath = "db.json";
	private static int ITEM_ID = 0;

	private BufferedReader jsonReader;
	private BufferedWriter jsonWritter;
	private JsonObject root;
	private Gson gson;
	private ServerApp server;

	public DBManager(ServerApp server) {
	    this.server = server;
	    // read the existing db

        try {
            this.jsonReader = new BufferedReader(new FileReader(dbPath));            
        } catch (FileNotFoundException e1) {
            Path file = Paths.get(dbPath);
            try {
                this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
                jsonWritter.write("{\n\"items\": []\n}");
                jsonWritter.flush();
                this.jsonReader = new BufferedReader(new FileReader(dbPath));
                } catch (IOException e) {
                e.printStackTrace();
            }
        }

		this.gson = new Gson();
		JsonParser parser = new JsonParser();
		this.root = parser.parse(this.jsonReader).getAsJsonObject();
		
		//Initialize ITEM_ID as last id used in database
		JsonElement registeredItems = root.get("items");
		if (registeredItems.isJsonArray()){
			int size = registeredItems.getAsJsonArray().size();
			if(size>0) {
				this.ITEM_ID = gson.fromJson(registeredItems.getAsJsonArray().get(size-1),SellableItem.class).getId()+1;
			}
		}
	}

	public Item addItem(Item i){
		i.setId(ITEM_ID++);
		this.root.get("items").getAsJsonArray().add(gson.toJsonTree(i));
		try {
			Path file = Paths.get(dbPath);
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write(root.toString());
			jsonWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public HashMap<Integer,Item> listItems() {
		HashMap<Integer,Item> items = new HashMap<Integer,Item>();
		if (root!= null ) {
		JsonElement registeredItems = root.get("items");
			if (registeredItems.isJsonArray()){
				for (JsonElement item : registeredItems.getAsJsonArray()){
					Item i = gson.fromJson(item, SellableItem.class);
					items.put(i.getId(), i);
				}
			}
		}
		return items;
	}

	public void updateItem(Item i) {
		// TODO Find item, remove it, replace it with new.
		JsonElement registeredItems = root.get("items");
		if (registeredItems.isJsonArray()){
			for (JsonElement item : registeredItems.getAsJsonArray()){
				if (item.getAsJsonObject().get("name").getAsString().equals(i.getName())) {
					registeredItems.getAsJsonArray().remove(item);
					break;
				}
			}
		}
		this.root.get("items").getAsJsonArray().add(gson.toJsonTree(i));
		try {
			Path file = Paths.get(dbPath);
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write(root.toString());
			jsonWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
