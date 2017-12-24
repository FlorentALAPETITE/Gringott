package serveur;

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

import client.app.IClient;
import client.app.Item;
import client.app.SellableItem;

public class DBManager {

	private static final String dbPath = "db.json";

	private BufferedReader jsonReader;
	private BufferedWriter jsonWritter;

	public DBManager() {
		Path file = Paths.get(dbPath);
		try {
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write("{\n\"items\": []\n}");
			jsonWritter.flush();
			this.jsonReader = new BufferedReader(new FileReader(dbPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printDBFile();
	}

	private void printDBFile() {
		System.out.println("DATABASE :");
		if (jsonReader != null) {
			String line = null;
			try {
				while ((line = jsonReader.readLine()) != null) {
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Not defined");
		}
	}

	public void addItem(Item i){
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			this.jsonReader = new BufferedReader(new FileReader(dbPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObject root = parser.parse(this.jsonReader).getAsJsonObject();
		root.get("items").getAsJsonArray().add(gson.toJson(i));
		//printDBFile();
	}
	
	public List<Item> listItems() {
		List<Item> items = new ArrayList<Item>();

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			this.jsonReader = new BufferedReader(new FileReader(dbPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObject root = parser.parse(this.jsonReader).getAsJsonObject();
		JsonElement registeredItems = root.get("items");
			if (registeredItems.isJsonArray()){
				for (JsonElement item : registeredItems.getAsJsonArray()){
					Item i = gson.fromJson(item, SellableItem.class);
					System.out.println(item);
				}
			}
		
		Item obj1 = new SellableItem("Botruc",
				"Petite créature d'une vingtaine de centimètres ayant un aspect végétal et deux longs doigts pointus à chaque main. - Peut crocheter des serrures -",
				400, "aCarat", 1);
		/*
		 * Item obj2 = new SellableItem("Cerbère nain",
		 * "Chien géant à trois tête servant de gardien - Cet exemplaire est de petite taille -"
		 * , 250, this.client); Item obj3 = new SellableItem("Demiguise",
		 * "Créature pouvant se rendre invisible lorsqu'elle est menacée. - Ses poils servent à tisser des toiles d'invisibilité -"
		 * , 900, this.client); Item obj4 = new SellableItem("Démonzémerveille",
		 * "Créature apparaissant sous forme de boule et se transformant, quand on la lance, en oiseau de proie bleu et vert. - A un attrait particulier pour le cerveau humain -"
		 * , 1000, this.client); Item obj5 = new SellableItem("Éruptif",
		 * "Sorte de Rhinocéros géant vivant en Afrique. Le fluide contenu dans sa corne peut être injecté dans tout type de materiau, provoquant l'explosion de celui-ci. - Sa peau épaisse le rend insensible à la plupart des sorts -"
		 * , 600, this.client); Item obj6 = new
		 * SellableItem("Plume d'Hippogriffe",
		 * "L'hippogriffe est une créature volante mi-aigle, mi- cheval. Il est très dangereux tant qu'il n'est pas dressé. - Cette plume a été récoltée dans les alentours de Poudlard et mesure 50 cm -"
		 * , 150, this.client); Item obj7 = new SellableItem("Niffleur",
		 * "Animal à la fourrure noire et au long museau semblable à un ornithorynque. Ils sont attirés par tout ce qui brille. - Formidable voleur -"
		 * , 250, this.client); Item obj8 = new SellableItem("OEuf d'Occamy",
		 * "Les Occamy sont une sorte d'oiseau-serpent. Ils ont la particularité d'être choranaptyxique : leur taille varient en fonction de l'espace dont ils disposent. - La coquille des oeufs d'Occamy est en argent pur -"
		 * , 700, this.client); Item obj9 = new SellableItem("Oiseau-Tonnerre",
		 * "Vivant en Arizona, ces oiseau provoquent des tempêtes lorsqu'ils se sentent menacés. - Leur plume peuvent être utilisées pour fabriquer des baguettes magiques"
		 * , 1250, this.client); Item obj10 = new
		 * SellableItem("OEuf congelé de Serpencendre",
		 * "Les serpencendres naissent dans des feux magiques laissés sans surveillance. Ils se cachent dans des recoins de la maison pour y pondre leurs oeufs qui, s'ils réussissent à grandir sans être repérés et chassés, enflamment la maison."
		 * , 2000, this.client);
		 */

		items.add(obj1);
		addItem(obj1);
		/*
		 * items.add(obj2); items.add(obj3); items.add(obj4); items.add(obj5);
		 * items.add(obj6); items.add(obj7); items.add(obj8); items.add(obj9);
		 * items.add(obj10);
		 */
		return items;
	}

}