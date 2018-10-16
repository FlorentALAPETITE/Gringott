package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import shared.Item;
import shared.SellableItem;

public class DBManager {

	private static final String dbPath = "db.json";

	private BufferedReader jsonReader;
	private BufferedWriter jsonWritter;
	private JsonObject root;
	private Gson gson;

	public DBManager(boolean recreate) {
		if (recreate) {
			Path file = Paths.get(dbPath);
			try {
				if (Files.exists(file)){
					Files.delete(file);
				}
				this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
				jsonWritter.write("{\n\"items\": []\n}");
				jsonWritter.flush();
				this.jsonReader = new BufferedReader(new FileReader(dbPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
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
		}
		this.gson = new Gson();
		JsonParser parser = new JsonParser();
		this.root = parser.parse(this.jsonReader).getAsJsonObject();
	}

	public void addItem(Item i){
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
	
	public List<Item> listItems() {
		List<Item> items = new ArrayList<Item>();
		if (root!= null ) {
		JsonElement registeredItems = root.get("items");
			if (registeredItems.isJsonArray()){
				for (JsonElement item : registeredItems.getAsJsonArray()){
					Item i = gson.fromJson(item, SellableItem.class);
					items.add(i);
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
